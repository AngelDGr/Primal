package org.primal.block_entity;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.primal.block.HollowLogBlock;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.registry.Primal_Sounds;
import org.primal.util.Primal_Util;
import org.primal.util.mob_types.HideOnLog;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HollowLogBlockEntity extends BlockEntity {

    //──────────────────────────────────── Init ────────────────────────────────────
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

    static void removeIgnoredAnimalTags(CompoundTag compoundTag) {
        for(String s : IGNORED_ANIMAL_TAGS) {
            compoundTag.remove(s);
        }

    }

    //──────────────────────────────────── Animals Stored ────────────────────────────────────
    private final List<AnimalData> stored = Lists.newArrayList();

    public boolean isEmpty() {
        return this.stored.isEmpty();
    }

    public void storeAnimal(AnimalData animalData) {
        this.stored.add(animalData);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        this.stored.clear();
        ListTag listtag = tag.getList("Animals", 10);

        for(int i = 0; i < listtag.size(); ++i) {
            CompoundTag compoundtag = listtag.getCompound(i);

            AnimalData animalData = new AnimalData(
                    compoundtag.getCompound("EntityData"),
                    compoundtag.getInt("ticksInsideLog"),
                    compoundtag.getInt("ticksWantedOnLog"),
                    ResourceLocation.tryParse(compoundtag.getString("idleSound")),
                    compoundtag.getBoolean("isPregnant"));
            this.stored.add(animalData);
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Animals", this.writeAnimals());
    }

    public ListTag writeAnimals() {
        ListTag listtag = new ListTag();

        for(AnimalData animalData : this.stored) {
            CompoundTag compoundtag = animalData.entityData.copy();
            CompoundTag compoundTag1 = new CompoundTag();
            compoundTag1.put("EntityData", compoundtag);
            compoundTag1.putInt("ticksInsideLog", animalData.ticksInsideLog);
            compoundTag1.putInt("ticksWantedOnLog", animalData.ticksWantedOnLog);
            listtag.add(compoundTag1);
        }

        return listtag;
    }

    public<T extends LivingEntity & HideOnLog> void addOccupant(T occupant, int ticksWantedOnLog, boolean isOffspring) {
        if (this.stored.size() < MAX_OCCUPANTS) {
            occupant.stopRiding();
            occupant.ejectPassengers();
            CompoundTag compoundtag = new CompoundTag();
            occupant.save(compoundtag);
            this.storeAnimal(new AnimalData(compoundtag, 0, ticksWantedOnLog, BuiltInRegistries.SOUND_EVENT.getKey(occupant.getInsideLogSound()), occupant.isPregnant()));
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
            AnimalData occupant,
            @Nullable List<Entity> storedInLog,
            HollowLogBlockEntity.AnimalReleaseStatus releaseStatus
    ) {
        CompoundTag compoundtag = occupant.entityData.copy();
        removeIgnoredAnimalTags(compoundtag);
//        compoundtag.put("HivePos", NbtUtils.writeBlockPos(pos));
//        compoundtag.putBoolean("NoGravity", true);
        Entity entity = EntityType.loadEntityRecursive(compoundtag, level, (entity1) -> entity1);

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
                occupant.setAnimalReleaseData(occupant.ticksInsideLog, (Animal & HideOnLog) entity, occupant.isPregnant);
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

    //xTODO
    public List<Entity> releaseAllOccupants(BlockState state, HollowLogBlockEntity.AnimalReleaseStatus releaseStatus) {
        List<Entity> list = Lists.newArrayList();
        this.stored
                .removeIf(animalData -> releaseOccupant(this.level,
                        this.worldPosition, state,
                        animalData,
                        list,
                        releaseStatus));

        if (!list.isEmpty()) {
            super.setChanged();
        }

        return list;
    }

    //xTODO
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

    public static class AnimalData {
        public final CompoundTag entityData;
        private int ticksInsideLog;
        private final int ticksWantedOnLog;
        private boolean isPregnant;

        private final ResourceLocation idleSound;

        AnimalData(CompoundTag entityData, int ticksInsideLog, int ticksWantedOnLog, ResourceLocation idleSound, boolean isPregnant) {
            this.entityData = entityData;
            this.ticksInsideLog = ticksInsideLog;
            this.ticksWantedOnLog = ticksWantedOnLog;
            this.idleSound=idleSound;
            this.isPregnant=isPregnant;
        }

        public boolean canRelease() {
            return this.ticksInsideLog++ > this.ticksWantedOnLog;
        }


        public boolean isPregnant() {
            return isPregnant;
        }

        public void removePregnant(){
            this.isPregnant=false;
        }

        public static AnimalData create(ResourceLocation entityType,
                                                           ResourceLocation idleSound,
                                                           Map<TagKey<Biome>, Integer> variantMap,
                                                           Holder<Biome> biomeHolder) {
            return create(entityType, idleSound, biomeHolder, variantMap, -1);
        }

        public static AnimalData create(ResourceLocation entityType,
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

            return new AnimalData(compoundtag,
                    40,
                    600,
                    idleSound,
                    false);
        }

        @Nullable
        public Entity createEntity(Level level, BlockPos ignoredPos, boolean isOffspring) {
            CompoundTag compoundtag = this.entityData;
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

            var idleSound = BuiltInRegistries.SOUND_EVENT.get(entry.idleSound);

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
            if (animalData.isPregnant() && animalData.ticksInsideLog> Primal_Util.toTicks(8)) {
                // Creates the baby
                if (log.stored.size() < MAX_OCCUPANTS) {
                    Entity entity = animalData.createEntity(level, pos, true);
                    if (entity instanceof Animal animal && animal instanceof HideOnLog hideOnLog) {

                        if (hideOnLog.getMateVariant() != -1) hideOnLog.setVariantFromLog(hideOnLog.getMateVariant());

                        babiesToAdd.add(animal);
                        pregnantIndexes.add(i);
                    }
                }
            }

            if (animalData.canRelease()) {
                HollowLogBlockEntity.AnimalReleaseStatus status = HollowLogBlockEntity.AnimalReleaseStatus.ANIMAL_RELEASED;
                if (releaseOccupant(level, pos, state, animalData, null, status)) {
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
                        Primal_Sounds.HOLLOW_LOG_OFFSPRING.get(), SoundSource.BLOCKS,
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
