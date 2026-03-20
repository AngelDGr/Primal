package org.primal.block_entity;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.primal.block.HollowLogBlock;
import org.primal.registry.Primal_Codecs;
import org.primal.registry.Primal_Items;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.registry.Primal_Sounds;
import org.primal.util.Primal_Util;
import org.primal.util.mob_types.HideOnLog;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HollowLogBlockEntity extends BlockEntity {

    //──────────────────────────────────── Init ────────────────────────────────────
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final int MAX_OCCUPANTS = 3;
    public HollowLogBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    static final List<String> IGNORED_ANIMAL_TAGS = Arrays.asList(
            "Air",
            "Fire",
            "HandDropChances",
            "HandItems",
            "HurtByTimestamp",
            "HurtTime",
            "LeftHanded",
            "Motion",
            "NoGravity",
            "OnGround",
            "Pos",
            "Rotation",
            "CannotEnterLogTicks",
            "leash",
            "UUID"
    );

    //──────────────────────────────────── Animals Stored ────────────────────────────────────
    private final List<AnimalData> stored = Lists.newArrayList();

    private List<HollowLogBlockEntity.Occupant> getAnimals() {
        return this.stored.stream().map(HollowLogBlockEntity.AnimalData::toOccupant).toList();
    }

    public boolean isEmpty() {
        return this.stored.isEmpty();
    }

    public void storeAnimal(HollowLogBlockEntity.Occupant occupant) {
        this.stored.add(new HollowLogBlockEntity.AnimalData(occupant, occupant.isPregnant));
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        this.stored.clear();
        if (tag.contains("animals")) {
            HollowLogBlockEntity.Occupant.LIST_CODEC
                    .parse(NbtOps.INSTANCE, tag.get("animals"))
                    .resultOrPartial(p_330133_ -> LOGGER.error("Failed to parse animals: '{}'", p_330133_))
                    .ifPresent(occupant -> occupant.forEach(this::storeAnimal));
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("animals", HollowLogBlockEntity.Occupant.LIST_CODEC.encodeStart(NbtOps.INSTANCE, this.getAnimals()).getOrThrow());
    }

    @Override
    protected void applyImplicitComponents(BlockEntity.@NotNull DataComponentInput componentInput) {
        super.applyImplicitComponents(componentInput);
        this.stored.clear();
        List<HollowLogBlockEntity.Occupant> list = componentInput.getOrDefault(Primal_Items.Components.ANIMALS_INSIDE, List.of());
        list.forEach(this::storeAnimal);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.@NotNull Builder components) {
        super.collectImplicitComponents(components);
        components.set(Primal_Items.Components.ANIMALS_INSIDE, this.getAnimals());
    }

    @SuppressWarnings("deprecation")
    @Override
    public void removeComponentsFromTag(@NotNull CompoundTag tag) {
        super.removeComponentsFromTag(tag);
        tag.remove("animals");
    }

    public<T extends LivingEntity & HideOnLog> void addOccupant(T occupant, int ticksWantedOnLog, boolean isOffspring) {
        if (this.stored.size() < MAX_OCCUPANTS) {
            occupant.stopRiding();
            occupant.ejectPassengers();
            this.storeAnimal(HollowLogBlockEntity.Occupant.of(occupant, ticksWantedOnLog, isOffspring));
            if (this.level != null) {
                BlockPos blockpos = this.getBlockPos();
                if(!isOffspring)
                    this.level
                            .playSound(
                                    null,
                                    blockpos.getX(),
                                    blockpos.getY(),
                                    blockpos.getZ(),
                                    occupant.getEnterLogSound(),
                                    SoundSource.BLOCKS,
                                    1.0F,
                                    1.0F
                            );

                this.level.gameEvent(GameEvent.BLOCK_CHANGE, blockpos, GameEvent.Context.of(occupant, this.getBlockState()));
            }

            occupant.discard();
            super.setChanged();
        }
    }

    private static boolean releaseOccupant(
            Level level,
            BlockPos pos,
            BlockState state,
            HollowLogBlockEntity.Occupant occupant,
            @Nullable List<Entity> storedInLog,
            HollowLogBlockEntity.AnimalReleaseStatus releaseStatus
    ) {
        Entity entity = occupant.createEntity(level, pos, false);

        //Avoids exiting if it wants to stay on the log
        if(entity instanceof HideOnLog hideOnLog)
            if(hideOnLog.staysOnLog() && releaseStatus != AnimalReleaseStatus.EMERGENCY) return false;

        Direction directionToRelease = state.getValue(HollowLogBlock.FACING);
        var axis = state.getValue(HollowLogBlock.AXIS);
        //Adjust correctly the log hole
        switch (axis){
            //X Axis
            case X -> directionToRelease =
                    // North: Up
                    directionToRelease==Direction.NORTH?
                            Direction.UP:
                            // South: Down
                            directionToRelease==Direction.SOUTH?
                                    Direction.DOWN:
                                    // West: North
                                    directionToRelease==Direction.WEST?
                                            Direction.NORTH:
                                            // East: South
                                            Direction.SOUTH;
            //Z Axis
            case Z -> directionToRelease =
                    // North: Up
                    directionToRelease==Direction.NORTH?
                            Direction.UP:
                            // South: Down
                            directionToRelease==Direction.SOUTH?
                                    Direction.DOWN:
                                    // West: West
                                    // East: East
                                    directionToRelease;
        }

        BlockPos blockPosToRelease = pos.relative(directionToRelease);
        boolean enoughSpaceInFront = !level.getBlockState(blockPosToRelease).getCollisionShape(level, blockPosToRelease).isEmpty();

        if (enoughSpaceInFront && releaseStatus != AnimalReleaseStatus.EMERGENCY) {
            return false;
        } else {
            if (entity != null) {
                if (storedInLog != null) {
                    storedInLog.add(entity);
                }

                float width = entity.getBbWidth();

                double d = enoughSpaceInFront ? 0.0 : 0.55 + (double)(width / 2.0F);

                double x = (double)pos.getX() + 0.5 + d * (double)directionToRelease.getStepX();
                double y = (double)pos.getY() + (directionToRelease.equals(Direction.UP)? 1.5: directionToRelease.equals(Direction.DOWN)? -0.5: 0.5) - (double)(entity.getBbHeight() / 2.0F);
                double z = (double)pos.getZ() + 0.5 + d * (double)directionToRelease.getStepZ();
                entity.moveTo(x, y, z, entity.getYRot(), entity.getXRot());

                if(entity instanceof LivingEntity livingEntity && entity instanceof HideOnLog hideOnLog){
                    livingEntity.getBrain().setMemoryWithExpiry(Primal_MemoryModuleTypes.HOLLOW_LOG_ENTER_COOLDOWN.get(), true, hideOnLog.getEnterCooldown());
                    level.playSound(null, pos, hideOnLog.getExitLogSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
                }

                level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(entity, level.getBlockState(pos)));
                return level.addFreshEntity(entity);
            } else {
                return false;
            }
        }
    }

    public List<Entity> releaseAllOccupants(BlockState state, HollowLogBlockEntity.AnimalReleaseStatus releaseStatus) {
        List<Entity> list = Lists.newArrayList();
        this.stored
                .removeIf(animalData -> releaseOccupant(this.level,
                        this.worldPosition, state,
                        animalData.toOccupant(),
                        list,
                        releaseStatus));

        if (!list.isEmpty()) {
            super.setChanged();
        }

        return list;
    }

    public void emptyAllLivingFromLog(@Nullable Player player, BlockState state, HollowLogBlockEntity.AnimalReleaseStatus releaseStatus) {
        List<Entity> list = this.releaseAllOccupants(state, releaseStatus);
        if (player != null) {
            for (Entity entity : list) {
                if (entity instanceof Mob mob) {
                    if (player.position().distanceToSqr(entity.position()) <= 16.0) {
                        mob.setTarget(player);
//                        if (!this.isSedated()) {
//
//                        } else {
//                            bee.setStayOutOfHiveCountdown(400);
//                        }
                    }
                }
            }
        }
    }

    public record Occupant(CustomData entityData, int ticksInsideLog, int ticksWantedOnLog, ResourceLocation idleSound, boolean isPregnant) {
        public static final Codec<HollowLogBlockEntity.Occupant> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                                CustomData.CODEC.optionalFieldOf("entity_data", CustomData.EMPTY).forGetter(HollowLogBlockEntity.Occupant::entityData),
                                Codec.INT.fieldOf("ticks_inside_log").forGetter(HollowLogBlockEntity.Occupant::ticksInsideLog),
                                Codec.INT.fieldOf("ticks_wanted_on_log").forGetter(HollowLogBlockEntity.Occupant::ticksWantedOnLog),
                                ResourceLocation.CODEC.fieldOf("idle_sound").forGetter(HollowLogBlockEntity.Occupant::idleSound),
                                Codec.BOOL.fieldOf("is_pregnant").forGetter(HollowLogBlockEntity.Occupant::isPregnant)
                        )
                        .apply(instance, HollowLogBlockEntity.Occupant::new)
        );
        public static final Codec<List<HollowLogBlockEntity.Occupant>> LIST_CODEC = CODEC.listOf();

        @SuppressWarnings("all")
        public static final StreamCodec<FriendlyByteBuf, HollowLogBlockEntity.Occupant> STREAM_CODEC = StreamCodec.composite(
                CustomData.STREAM_CODEC,
                HollowLogBlockEntity.Occupant::entityData,
                ByteBufCodecs.VAR_INT,
                HollowLogBlockEntity.Occupant::ticksInsideLog,
                ByteBufCodecs.VAR_INT,
                HollowLogBlockEntity.Occupant::ticksWantedOnLog,
                Primal_Codecs.RESOURCE_LOCATION,
                HollowLogBlockEntity.Occupant::idleSound,
                ByteBufCodecs.BOOL,
                HollowLogBlockEntity.Occupant::isPregnant,
                HollowLogBlockEntity.Occupant::new
        );

        public static<T extends LivingEntity & HideOnLog> HollowLogBlockEntity.Occupant of(T entity, int ticksWantedOnLog, boolean isOffspring) {
            CompoundTag compoundtag = new CompoundTag();
            entity.save(compoundtag);

            if(isOffspring) {
                entity.getBrain().eraseMemory(MemoryModuleType.IS_PREGNANT);
                entity.getBrain().eraseMemory(Primal_MemoryModuleTypes.MATE_VARIANT.get());
                HollowLogBlockEntity.IGNORED_ANIMAL_TAGS.forEach(compoundtag::remove);
            }

            return new HollowLogBlockEntity.Occupant(CustomData.of(compoundtag), 0, ticksWantedOnLog, BuiltInRegistries.SOUND_EVENT.getKey(entity.getInsideLogSound()), entity.isPregnant());
        }

        public static HollowLogBlockEntity.Occupant create(ResourceLocation entityType,
                                                           ResourceLocation idleSound,
                                                           Map<TagKey<Biome>, Integer> variantMap,
                                                           Holder<Biome> biomeHolder) {
            return create(entityType, idleSound, biomeHolder, variantMap, -1);
        }

        public static HollowLogBlockEntity.Occupant create(ResourceLocation entityType,
                                                           ResourceLocation idleSound,
                                                           Holder<Biome> biomeHolder,
                                                           Map<TagKey<Biome>, Integer> variantMap,
                                                           int directVariant) {
            CompoundTag compoundtag = new CompoundTag();
            compoundtag.putString("id", entityType.toString());
            int variantId = 0;

            for (Map.Entry<TagKey<Biome>, Integer> entry : variantMap.entrySet()) {
                if (biomeHolder.is(entry.getKey())) {
                    variantId = entry.getValue();
                    // use variantId
                    break;
                }
            }

            compoundtag.putInt("Variant", directVariant!=-1? directVariant: variantId);

            return new HollowLogBlockEntity.Occupant(CustomData.of(compoundtag),
                    40,
                    600,
                    idleSound,
                    false);
        }

        @Nullable
        public Entity createEntity(Level level, BlockPos ignoredPos, boolean isOffspring) {
            CompoundTag compoundtag = this.entityData.copyTag();
            Entity entity = EntityType.loadEntityRecursive(compoundtag, level, entity1 -> entity1);
            if (entity != null) {

                if (entity instanceof Animal animal)
                    setAnimalReleaseData(this.ticksInsideLog, animal, isOffspring);

                return entity;
            } else {
                return null;
            }
        }

        private void setAnimalReleaseData(int ticksInHive, Animal animal, boolean isOffspring) {
            int i = animal.getAge();
            if (i < 0) {
                animal.setAge(Math.min(0, i + ticksInHive));
            } else if (i > 0) {
                animal.setAge(Math.max(0, i - ticksInHive));
            }

            animal.setInLoveTime(Math.max(0, animal.getInLoveTime() - ticksInHive));

            //Removes the pregnant status
            if(!this.isPregnant || isOffspring){
                if(animal.getBrain().getMemory(MemoryModuleType.IS_PREGNANT).isPresent()){
                    animal.getBrain().eraseMemory(MemoryModuleType.IS_PREGNANT);
                    if(animal.getBrain().getMemory(Primal_MemoryModuleTypes.MATE_VARIANT.get()).isPresent())
                        animal.getBrain().eraseMemory(Primal_MemoryModuleTypes.MATE_VARIANT.get());
                }
            }

            if(isOffspring)
                animal.setAge(-24000);
        }
    }

    static class AnimalData {
        private final HollowLogBlockEntity.Occupant occupant;
        private int ticksInsideLog;
        private boolean isPregnant;

        AnimalData(HollowLogBlockEntity.Occupant occupant, boolean isPregnant) {
            this.occupant = occupant;
            this.ticksInsideLog = occupant.ticksInsideLog();
            this.isPregnant=isPregnant;
        }

        public boolean canRelease() {
            return this.ticksInsideLog++ > this.occupant.ticksWantedOnLog;
        }

        public HollowLogBlockEntity.Occupant toOccupant() {
            return new HollowLogBlockEntity.Occupant(this.occupant.entityData, this.ticksInsideLog, this.occupant.ticksWantedOnLog, this.occupant.idleSound, this.isPregnant);
        }

        public boolean isPregnant() {
            return isPregnant;
        }

        public void removePregnant(){
            this.isPregnant=false;
        }
    }

    public enum AnimalReleaseStatus {
        ANIMAL_RELEASED,
        EMERGENCY
    }

    @Override
    public void setChanged() {
        if (this.isFireNearby()) {
            if(level!=null)
                this.emptyAllLivingFromLog(null, this.level.getBlockState(this.getBlockPos()), AnimalReleaseStatus.EMERGENCY);
        }

        super.setChanged();
    }

    public boolean isFireNearby() {
        if (this.level != null) {
            for (BlockPos blockpos : BlockPos.betweenClosed(this.worldPosition.offset(-1, -1, -1), this.worldPosition.offset(1, 1, 1))) {
                if (this.level.getBlockState(blockpos).getBlock() instanceof FireBlock) {
                    return true;
                }
            }
        }

        return false;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, HollowLogBlockEntity log) {
        tickOccupants(level, pos, state, log.stored, log);

        if (!log.stored.isEmpty() && level.getRandom().nextDouble() < 0.005) {
            double d0 = pos.getX() + 0.5;
            double d1 = pos.getY();
            double d2 = pos.getZ() + 0.5;

            int index = level.getRandom().nextInt(log.stored.size());
            var entry = log.stored.get(index);

            var idleSound = BuiltInRegistries.SOUND_EVENT.get(entry.occupant.idleSound);

            if (idleSound != null)
                level.playSound(null, d0, d1, d2, idleSound, SoundSource.BLOCKS, 1.0F, 1.0F);
        }

//        DebugPackets.sendHiveInfo(level, pos, state, log);
    }

    private static void tickOccupants(Level level, BlockPos pos, BlockState state, List<HollowLogBlockEntity.AnimalData> data, HollowLogBlockEntity log) {
        boolean flag = false;
        List<Animal> babiesToAdd = new ArrayList<>();
        List<Integer> pregnantIndexes = new ArrayList<>();

        for (int i = data.size() - 1; i >= 0; i--) {
            HollowLogBlockEntity.AnimalData animalData = data.get(i);

            //Lays the baby after 8s
            if (animalData.isPregnant() && animalData.toOccupant().ticksInsideLog> Primal_Util.toTicks(8)) {
                // Creates the baby
                if (log.stored.size() < MAX_OCCUPANTS) {
                    Entity entity = animalData.occupant.createEntity(level, pos, true);
                    if (entity instanceof Animal animal && animal instanceof HideOnLog hideOnLog) {

                        if (hideOnLog.getMateVariant() != -1) hideOnLog.setVariantFromLog(hideOnLog.getMateVariant());

                        babiesToAdd.add(animal);
                        pregnantIndexes.add(i);
                    }
                }
            }

            if (animalData.canRelease()) {
                HollowLogBlockEntity.AnimalReleaseStatus status = HollowLogBlockEntity.AnimalReleaseStatus.ANIMAL_RELEASED;
                if (releaseOccupant(level, pos, state, animalData.toOccupant(), null, status)) {
                    flag = true;
                    data.remove(i);
                }
            }
        }

        if(log.stored.size() < MAX_OCCUPANTS && !babiesToAdd.isEmpty() && !pregnantIndexes.isEmpty()){
            // Add after iteration
            for (Animal baby : babiesToAdd) {

                //Keeps inside per 5s-15s after born
                log.addOccupant((Animal & HideOnLog) baby, Primal_Util.toTicks(level.getRandom().nextIntBetweenInclusive(5, 15)), true);

                level.playSound(null,
                        pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
                        Primal_Sounds.HOLLOW_LOG_OFFSPRING, SoundSource.BLOCKS,
                        1.0F, 1.0F);
            }

            for(Integer indexPregnant: pregnantIndexes){
                data.get(indexPregnant).removePregnant();
            }

            flag= true;
        }

        if (flag) {
            setChanged(level, pos, state);
        }
    }
}
