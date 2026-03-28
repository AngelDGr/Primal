package org.primal.entity.animal;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.util.*;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.client.animation.entity.SnakeAnimations;
import org.primal.entity.ai.SnakeAi;
import org.primal.entity.ai.controls.look.WaterOrLandLookControl;
import org.primal.entity.ai.controls.move.SnakeMoveControl;
import org.primal.entity.ai.controls.navigation.SnakePathNavigation;
import org.primal.entity.hitboxes.SnakeParts;
import org.primal.entity.parts.SnakePart;
import org.primal.injection.SetNeckEntity;
import org.primal.registry.*;
import org.primal.util.Primal_Util;
import org.primal.util.mob_types.*;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

//xTODO:
// Add egg eating
//They spawn in various warm and temperate climate biomes.
// They are capable of swimming underwater.
// - Verdant Snake live in Plains
// - Lumber Snake live in Taigas
// - Tenebrous Snake lives in Savannas
// - Dusty Snake lives in Badlands
// - Browed Snake lives in Deserts
// - Cerulean Snake lives in Jungles
// - Brackish Snake in Mangrove Swamps
// - Aposematic Snake lives in Sparse Jungles
// - Marine Snake only live inside the chest of sea structures
// - Skeleton spawns on the nether
// - Null Snake is where snakes don’t naturally spawn
// They have two preferred move sets (idle & slither) whereas their head stays low on ground or stays upwards.
// When it becomes hostile, it lashes out and upon bitten it inflicts one of three possible effects:
// - Weakness (0:10)
// - Poison (0:10)
// - Slowness (0:10)
// The effects of their attacks vary between the three effects for each snake, meaning every snake inflicts different effects.
// When danger is close by it won’t immediately attack, instead they raise their heads up to observe their threat.
// Snakes can be tamed by playing a jukebox near them by doing that the snake will start to dance emitting note particles around them while in that state the snake
// will be distracted and no longer hostile perfect moment to feed the raw rabbit to tame them
// The player can equip a tamed snake which makes them coil around the players neck during combat the snake can attack and inflict potion effects on the entity you attack
// You can unequip the snake by crouching
// Their effects can be milked with an empty glass bottle, which acquires the effect depending on their assigned effects.
// Snakes can change their skin by rapidly growing new skin after eating chicken eggs, the mold can be removed with shears.
// The variant of the snake depends on the biome the snake is located in during shearing
// Block particles emit when transitioning to a variant.
// Snakes can eat fermented spider eye to breed.
// When bred, they will lay their eggs/have babies in their hidey-holes
// Add sounds
// Add natural spawn
public class SnakeEntity extends TamableAnimal implements VariantHolder<SnakeEntity.Variant>, GeoEntity, NeutralMob, PrimalTamable, SemiAquaticAnimal, DetectsFartherAway, AttackVillagers, HideOnLog, CustomFieldGuideState {

    //──────────────────────────────────── Variants ────────────────────────────────────
    public enum Variant implements StringRepresentable {
        NULL(0, "null"),
        CERULEAN(1, "cerulean"),
        BROWNED(2, "browned"),
        DUSTY(3, "dusty"),
        TENEBROUS(4, "tenebrous"),
        APOSEMATIC(5, "aposematic"),
        BRACKISH(6, "brackish"),
        LUMBER(7, "lumber"),
        VERDANT(8, "verdant"),
        MARINE(9, "marine"),
        SKELETON(10, "skeleton"),;

        public static final Codec<SnakeEntity.Variant> CODEC = StringRepresentable.fromEnum(SnakeEntity.Variant::values);

        private static final IntFunction<SnakeEntity.Variant> BY_ID = ByIdMap.continuous(SnakeEntity.Variant::getId, values(), ByIdMap.OutOfBoundsStrategy.CLAMP);

        public static SnakeEntity.Variant byId(int id) {
            return BY_ID.apply(id);
        }

        public static final Map<TagKey<Biome>, SnakeEntity.Variant> VARIANTS_FOR_LOG =
                Map.of(
                        Primal_Tags.Biome.SPAWNS_VERDANT_SNAKE, SnakeEntity.Variant.VERDANT,
                        Primal_Tags.Biome.SPAWNS_LUMBER_SNAKE, SnakeEntity.Variant.LUMBER,
                        Primal_Tags.Biome.SPAWNS_TENEBROUS_SNAKE, SnakeEntity.Variant.TENEBROUS,
                        Primal_Tags.Biome.SPAWNS_DUSTY_SNAKE, SnakeEntity.Variant.DUSTY,
                        Primal_Tags.Biome.SPAWNS_BROWED_SNAKE, SnakeEntity.Variant.BROWNED,
                        Primal_Tags.Biome.SPAWNS_CERULEAN_SNAKE, SnakeEntity.Variant.CERULEAN,
                        Primal_Tags.Biome.SPAWNS_BRACKISH_SNAKE, SnakeEntity.Variant.BRACKISH,
                        Primal_Tags.Biome.SPAWNS_APOSEMATIC_SNAKE, SnakeEntity.Variant.APOSEMATIC,
                        Primal_Tags.Biome.SPAWNS_MARINE_SNAKE, SnakeEntity.Variant.MARINE
                );

        public static final Map<TagKey<Biome>, Integer> VARIANT_IDS_FOR_LOG =
                VARIANTS_FOR_LOG.entrySet().stream()
                        .collect(Collectors.toUnmodifiableMap(
                                Map.Entry::getKey,
                                entry -> entry.getValue().id
                        ));

        final int id;

        private final String name;

        Variant(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return this.id;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }

    @Override
    public void setVariant(SnakeEntity.Variant variant) {
        this.entityData.set(DATA_VARIANT_ID, variant.id);
    }

    @Override
    public @NotNull SnakeEntity.Variant getVariant() {
        return SnakeEntity.Variant.byId(this.entityData.get(DATA_VARIANT_ID));
    }

    public void setVariantFromBiome(SnakeEntity animal, Holder<Biome> holder) {
        //Skeleton on nether only
        if (animal.level().dimension() == Level.NETHER) {
            animal.setVariant(SnakeEntity.Variant.SKELETON);
        } else {

            for (Map.Entry<TagKey<Biome>, SnakeEntity.Variant> entry : Variant.VARIANTS_FOR_LOG.entrySet()) {
                if (holder.is(entry.getKey())) {
                    animal.setVariant(entry.getValue());
                    return;
                }
            }

            animal.setVariant(SnakeEntity.Variant.NULL);
        }
    }

    //──────────────────────────────────── Init ────────────────────────────────────
    public final SnakeParts parts = new SnakeParts(this);
    public SnakeEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
        this.getNavigation().setCanFloat(true);
        this.moveControl = new SnakeMoveControl(this, 85, 50, 0.18f, 0.01f, false, s->true);
        this.lookControl = new WaterOrLandLookControl<>(this, 10);
        this.setId(ENTITY_COUNTER.getAndAdd(this.parts.getSubEntities().length + 1) + 1);
        this.gameEventHandler = new DynamicGameEventListener<>(new SongListener(this, new EntityPositionSource(this, this.getEyeHeight()), 8));
        this.setMaxUpStep(1.0f);
    }

    @Override
    public void setId(int id) {
        super.setId(id);
        for (int i = 0; i < this.parts.getSubEntities().length; i++) // Forge: Fix MC-158205: Set part ids to successors of parent mob id
            this.parts.getSubEntities()[i].setId(id + i + 1);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20f)
                .add(Attributes.MOVEMENT_SPEED, 0.20f)
                .add(Attributes.ATTACK_DAMAGE, 3f);
    }

    @Override
    protected int calculateFallDamage(float f, float f2) {
        return Mth.ceil((f - 8.0F) * f2);
    }

    private static final EntityDataAccessor<Integer> DATA_VARIANT_ID = SynchedEntityData.defineId(SnakeEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_COLLAR_COLOR = SynchedEntityData.defineId(SnakeEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> HAS_COLLAR = SynchedEntityData.defineId(SnakeEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> FOLLOWER_STATE = SynchedEntityData.defineId(SnakeEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SLITHER_COOLDOWN = SynchedEntityData.defineId(SnakeEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> FLICK_COOLDOWN = SynchedEntityData.defineId(SnakeEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> POTION_COOLDOWN = SynchedEntityData.defineId(SnakeEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<String> SNAKE_STATE = SynchedEntityData.defineId(SnakeEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> SNAKE_EFFECT = SynchedEntityData.defineId(SnakeEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_CAUTIOUS = SynchedEntityData.defineId(SnakeEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_DANCING = SynchedEntityData.defineId(SnakeEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_SHEDDING = SynchedEntityData.defineId(SnakeEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> SHEDDING_TIME = SynchedEntityData.defineId(SnakeEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> FIELDGUIDE_STATE = SynchedEntityData.defineId(SnakeEntity.class, EntityDataSerializers.BOOLEAN);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_VARIANT_ID, SnakeEntity.Variant.NULL.id);
        this.entityData.define(DATA_COLLAR_COLOR, DyeColor.RED.getId());
        this.entityData.define(HAS_COLLAR, true);
        this.entityData.define(FOLLOWER_STATE, 0);
        this.entityData.define(SLITHER_COOLDOWN, 0);
        this.entityData.define(FLICK_COOLDOWN, this.getRandom().nextIntBetweenInclusive(20, 100));
        this.entityData.define(POTION_COOLDOWN, 0);
        this.entityData.define(SNAKE_STATE, SnakeState.STANDING.getSerializedName());
        this.entityData.define(SNAKE_EFFECT, SnakeEffect.POISON.id);
        this.entityData.define(IS_CAUTIOUS, false);
        this.entityData.define(IS_DANCING, false);
        this.entityData.define(IS_SHEDDING, false);
        this.entityData.define(SHEDDING_TIME, 0);
        this.entityData.define(FIELDGUIDE_STATE, false);
    }

    @Override
    public DyeColor getCollarColor() {
        return DyeColor.byId(this.entityData.get(DATA_COLLAR_COLOR));
    }

    @Override
    public void setCollarColor(DyeColor collarColor) {
        this.entityData.set(DATA_COLLAR_COLOR, collarColor.getId());
    }

    @Override
    public boolean hasCollar() {
        return this.entityData.get(HAS_COLLAR);
    }

    @Override
    public void setHasCollar(boolean hasCollar){
        this.entityData.set(HAS_COLLAR, hasCollar);
    }

    @Override
    public boolean hasRemovableCollar() {
        return true;
    }

    @Override
    public int getFollowerState() {
        return this.entityData.get(FOLLOWER_STATE);
    }

    @Override
    public void setFollowerState(int state) {
        this.entityData.set(FOLLOWER_STATE, state);
        this.setInSittingPose(state==2);
    }

    public int getSlitherCooldown() {
        return this.entityData.get(SLITHER_COOLDOWN);
    }

    public void setSlitherCooldown(int slitherCooldown) {
        this.entityData.set(SLITHER_COOLDOWN, slitherCooldown);
    }

    public int getFlickCooldown() {
        return this.entityData.get(FLICK_COOLDOWN);
    }

    public void setFlickCooldown(int flickCooldown) {
        this.entityData.set(FLICK_COOLDOWN, flickCooldown);
    }

    public int getPotionCooldown() {
        return this.entityData.get(POTION_COOLDOWN);
    }

    public void setPotionCooldown(int potionCooldown) {
        this.entityData.set(POTION_COOLDOWN, potionCooldown);
    }

    public SnakeState getSnakeState() {
        return SnakeState.fromName(this.entityData.get(SNAKE_STATE));
    }

    public void setSnakeState(SnakeState state) {
        //Transition animations
        //Standing -> Slithering
        if(getSnakeState().equals(SnakeState.STANDING) && state.equals(SnakeState.SLITHERING)){
            this.stopTriggeredAnimation("base_controller", "slither_end");
            this.triggerAnim("base_controller", "slither_start");
        }
        //Slithering -> Standing
        if(getSnakeState().equals(SnakeState.SLITHERING) && state.equals(SnakeState.STANDING)){
            this.stopTriggeredAnimation("base_controller", "slither_start");
            this.triggerAnim("base_controller", "slither_end");
        }

        this.entityData.set(SNAKE_STATE, state.getSerializedName());
    }

    public boolean isSlithering() {
        return getSnakeState().equals(SnakeState.SLITHERING);
    }

    public SnakeEffect getSnakeEffect() {
        return SnakeEffect.byId(this.entityData.get(SNAKE_EFFECT));
    }

    public void setSnakeEffect(SnakeEffect snakeEffect) {
        this.entityData.set(SNAKE_EFFECT, snakeEffect.id);
    }

    public boolean isCautious() {
        return this.entityData.get(IS_CAUTIOUS);
    }

    public void setIsCautious(boolean isCautious) {
        this.entityData.set(IS_CAUTIOUS, isCautious);
    }

    public boolean isDancing() {
        return this.entityData.get(IS_DANCING);
    }

    public void setIsDancing(boolean isDancing) {
        this.entityData.set(IS_DANCING, isDancing);
    }

    public boolean isShedding() {
        return this.entityData.get(IS_SHEDDING);
    }

    public void setIsShedding(boolean isShedding) {
        this.entityData.set(IS_SHEDDING, isShedding);
    }

    public int getSheddingTime() {
        return this.entityData.get(SHEDDING_TIME);
    }

    public void setSheddingTime(int sheddingTime) {
        this.entityData.set(SHEDDING_TIME, sheddingTime);
    }

    @Override
    public void setFieldGuideState(boolean state) {
        this.entityData.set(FIELDGUIDE_STATE, state);
    }

    @Override
    public boolean hasFieldGuideState() {
        return this.entityData.get(FIELDGUIDE_STATE);
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> key) {
        if (SNAKE_STATE.equals(key) || FOLLOWER_STATE.equals(key)) this.refreshDimensions();

        super.onSyncedDataUpdated(key);
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnType, @javax.annotation.Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        this.setSnakeEffect(SnakeEffect.byId(level.getRandom().nextIntBetweenInclusive(0, SnakeEffect.values().length-1)));
        this.setVariantFromBiome(this, level.getBiome(this.blockPosition()));
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData, compoundTag);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant().id);
        this.addAdditionalSaveDataTamable(compound);
        compound.putInt("SlitherCooldown", this.getSlitherCooldown());
        compound.putInt("FlickCooldown", this.getFlickCooldown());
        compound.putInt("PotionCooldown", this.getPotionCooldown());
        compound.putString("SnakeState", this.getSnakeState().getSerializedName());
        compound.putInt("SnakeEffect", this.getSnakeEffect().id);
        compound.putBoolean("IsCautious", this.isCautious());
        compound.putBoolean("IsDancing", this.isDancing());
        compound.putBoolean("IsShedding", this.isShedding());
        compound.putInt("SheddingTime", this.getSheddingTime());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(SnakeEntity.Variant.byId(compound.getInt("Variant")));
        this.readAdditionalSaveDataTamable(compound);
        this.setSlitherCooldown(compound.getInt("SlitherCooldown"));
        this.setFlickCooldown(compound.getInt("FlickCooldown"));
        this.setPotionCooldown(compound.getInt("PotionCooldown"));
        this.setSnakeState(SnakeState.fromName(compound.getString("SnakeState")));
        this.setSnakeEffect(SnakeEffect.byId(compound.getInt("SnakeEffect")));
        this.setIsCautious(compound.getBoolean("IsCautious"));
        this.setIsDancing(compound.getBoolean("IsDancing"));
        this.setIsShedding(compound.getBoolean("IsShedding"));
        this.setSheddingTime(compound.getInt("SheddingTime"));
    }

    //──────────────────────────────────── Multipart ────────────────────────────────────
    @Override
    public boolean isMultipartEntity() {
        //To be added to correctly to the level
        if(this.tickCount==0) return true;

        return isSlithering() && !this.isBaby();
    }

    @Override
    public @Nullable PartEntity<?> @NotNull [] getParts() {
        return parts.getSubEntities();
    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose pose) {
        return isSlithering() || this.isSitting()?
                EntityDimensions
                        .scalable((this.isBaby()? 8f: 15f)/16f, 5f/16f):
                super.getDimensions(pose);
    }

    @Override
    protected float getStandingEyeHeight(@NotNull Pose pose, @NotNull EntityDimensions entityDimensions) {
        return isSlithering() || this.isSitting()?
                4f/16f * getScale():
                0.6f * getScale();
    }

    public boolean isStalking() {
        return this.brain.getMemory(Primal_MemoryModuleTypes.STALK_TARGET.get()).isPresent();
    }

    //──────────────────────────────────── AI & Movement ────────────────────────────────────
    @Override
    protected void registerGoals() {}

    @Override
    protected @NotNull Brain.Provider<SnakeEntity> brainProvider() {
        return SnakeAi.brainProvider();
    }

    @Override
    protected @NotNull Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
        return SnakeAi.makeBrain(this.brainProvider().makeBrain(dynamic));
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull Brain<SnakeEntity> getBrain() {
        return (Brain<SnakeEntity>) super.getBrain();
    }

    @Override
    public void tick() {
        super.tick();
        for(SnakePart part: this.parts.getSubEntities()) part.tick();
    }

    @Override
    protected void pushEntities() {
        if(!this.isSlithering() && !this.isBaby() && !this.isSitting())
            super.pushEntities();
    }

    @Override
    public void push(@NotNull Entity entity) {
        if(!(entity instanceof SnakeEntity snake) || snake.isAggressive() || snake.isSitting())
            super.push(entity);
        this.setSlitherCooldown(10);
    }

    private int intersectTime=0;
    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();

        //Starts the brain
        Brain<SnakeEntity> brain = this.getBrain();
        this.level().getProfiler().push("snakeBrain");
        brain.tick((ServerLevel)this.level(), this);
        this.level().getProfiler().pop();
        this.level().getProfiler().push("snakeActivityUpdate");
        SnakeAi.updateActivity(this);
        this.level().getProfiler().pop();

        //Timers
        {
            if(getSlitherCooldown()>0)
                setSlitherCooldown(this.getSlitherCooldown()-1);

            if(getFlickCooldown()>0)
                setFlickCooldown(this.getFlickCooldown()-1);

            if(getPotionCooldown()>0)
                setPotionCooldown(this.getPotionCooldown()-1);
        }

        //Trigger flick
        if(getFlickCooldown()==0){
            if(!this.isDancing())
                this.triggerAnim("misc", "flick");
            this.setFlickCooldown(
                    this.getRandom().nextIntBetweenInclusive(60, 200)
            );
        }

        //Slither things
        if(!canSlither()) this.setSlitherCooldown(10);
        setSnakeState(canSlither() && this.getSlitherCooldown()==0? SnakeState.SLITHERING: SnakeState.STANDING);

        //Cautious
        setIsCautious(this.brain.getMemory(Primal_MemoryModuleTypes.CAUTIOUS_LIST.get()).isPresent());

        //Dancing logic
        {
            setIsDancing(brain.getMemory(MemoryModuleType.DANCING).isPresent() && brain.getMemory(MemoryModuleType.DANCING).get());

            //Music notes
            if (isDancing() && this.tickCount % this.getRandom().nextIntBetweenInclusive(10, 20) == 0)
                this.level().broadcastEntityEvent(this, (byte) 22);

            //Removes jukebox if too far
            var jukeboxMemory = this.brain.getMemory(Primal_MemoryModuleTypes.MUSIC_BLOCK.get());
            if (jukeboxMemory.isPresent()) {
                BlockPos pos = jukeboxMemory.get();
                BlockState state = this.level().getBlockState(pos);

                if ((state.is(Blocks.JUKEBOX) || state.is(Blocks.NOTE_BLOCK)) && this.distanceToSqr(Vec3.atCenterOf(pos)) < 64.0D)
                    return;

                this.brain.eraseMemory(MemoryModuleType.DANCING);
                this.brain.eraseMemory(Primal_MemoryModuleTypes.MUSIC_BLOCK.get());
            }
        }

        //Mount on neck
        if(this.getOwner()!=null && !this.isSitting()){
            //When intersecting the owner but the owner isn't crouching
            if (this.getBoundingBox().intersects(this.getOwner().getBoundingBox()) && this.getOwner() instanceof ServerPlayer serverPlayer && !serverPlayer.isCrouching()) {
                this.intersectTime++;

                //After 10 ticks of colliding
                if(intersectTime>10)
                    if(this.setEntityOnNeck(serverPlayer))
                        intersectTime=0;
            }
        }

        if(this.isShedding()){
            this.setSheddingTime(this.getSheddingTime()+1);

            if(getSheddingTime()>=Primal_Util.toTicks(60)){
                removeShedding();
            }

            //Particles
            if(this.tickCount%20==0)
                this.level().broadcastEntityEvent(this, (byte) 33);
        }
    }

    public boolean canSlither(){
        return
                //Water
                this.isInWater() && !this.isCollidingWithBlocksHorizontally()

                //Land
                || (Primal_Util.isMoving(this)
                && canSlitherOnBlock(this.blockPosition())
                && canSlitherOnBlock(this.blockPosition().east())
                && canSlitherOnBlock(this.blockPosition().west())
                && canSlitherOnBlock(this.blockPosition().south())
                && canSlitherOnBlock(this.blockPosition().north())
                && !isCautious()
                && this.onGround()
                && this.getTarget()==null
                && this.getNavigation().isInProgress()
                && !this.horizontalCollision);
    }

    private boolean isCollidingWithBlocksHorizontally() {
        AABB box = this.getBoundingBox();
        Level level = this.level();

        // Slight horizontal inflation to detect side contact
        AABB horizontalBox = box.inflate(0.05, 0.0, 0.05);

        return level.getBlockCollisions(this, horizontalBox).iterator().hasNext();
    }

    public boolean canSlitherOnBlock(BlockPos pos) {
        BlockState state = this.level().getBlockState(pos);
        BlockState belowState = this.level().getBlockState(pos.below());

        boolean passable = state.getCollisionShape(this.level(), pos).isEmpty();
        boolean hasGround = !belowState.getCollisionShape(this.level(), pos.below()).isEmpty();

        return (passable && hasGround);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new SnakePathNavigation(this, level);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        this.parts.positionParts();
    }

    @Override
    public void aiStep() {
        super.aiStep();
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
        }
        else super.travel(travelVector);
    }

    public boolean refuseToMove() {
//        return ImmutableList.of(Pose.ROARING, Pose.SNIFFING, Pose.CROAKING).contains(this.getPose());
        return false;
    }

    @Override
    public @NotNull Vec3 getFluidFallingAdjustedMovement(double gravity, boolean isFalling, @NotNull Vec3 deltaMovement) {
        return deltaMovement.scale(0.94);
    }

    public boolean increaseSpeed(){
        boolean isTooFar = this.getOwner()!=null && this.isFollowing() && this.distanceToSqr(this.getOwner())>(5*5);

        return isTooFar || this.isAggressive() || this.isCautious() || this.isStalking();
    }

    @Override
    public float getSpeed() {
        return increaseSpeed()? super.getSpeed()*1.8f: super.getSpeed();
    }

    private final DynamicGameEventListener<SongListener> gameEventHandler;
    @Override
    public void updateDynamicGameEventListener(@NotNull final BiConsumer<DynamicGameEventListener<?>, ServerLevel> callback) {
        final Level world = this.level();
        if (world instanceof final ServerLevel serverWorld) {
            callback.accept(this.gameEventHandler, serverWorld);
        }
    }

    @Override
    public boolean wantsToPickUp(@NotNull ItemStack stack) {
        return stack.is(Primal_Tags.Item.SNAKE_EDIBLE_EGGS);
    }

    //──────────────────────────────────── Combat ────────────────────────────────────
    @Nullable
    @Override
    public LivingEntity getTarget() {
        return Primal_Util.OneTwentyEquivalent.getTargetFromBrain(this);
    }

    public static int TERRITORIAL_DISTANCE = 4;
    @Override
    public boolean canAttack(@NotNull LivingEntity target) {
        //To not attack owner
        if(this.getOwner()!=null && this.getOwner().equals(target)) return false;

        if((this.getBrain().isMemoryValue(MemoryModuleType.ATTACK_TARGET, target) || this.getBrain().isMemoryValue(Primal_MemoryModuleTypes.LAST_ATTACK_TARGET.get(), target)) && super.canAttack(target))
            return true;

        var nearestAttackableCautious = this.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_ATTACKABLE_CAUTIOUS.get());

        //Pass to the sensor to handle tamed logic
        if(this.isTame())
            return super.canAttack(target) && Primal_Util.isNotNeverAttack(target);

        return Primal_Util.isNotNeverAttack(target)
                //Hunts regularly
                && (target.getType().is(Primal_Tags.Entity.SNAKE_HUNTABLE) && !this.getBrain().hasMemoryValue(MemoryModuleType.HAS_HUNTING_COOLDOWN)
                //It is the nearest cautious attackable
                || (nearestAttackableCautious.isPresent() && nearestAttackableCautious.get().equals(target)))
//                && !this.isPacified()
                && super.canAttack(target);
    }

    public boolean canBeCautious(@NotNull LivingEntity target){
        return Primal_Util.isNotNeverAttack(target)
                && this.distanceTo(target) < SnakeEntity.TERRITORIAL_DISTANCE
                && !(target instanceof SnakeEntity)
                //Not be cautious of owner
                && !(this.getOwner()!=null && this.getOwner().equals(target))
                //Not be cautious if tamed
                && !this.isTame()
                && super.canAttack(target);
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if(source.getEntity() instanceof Fox) amount=amount*6;

        boolean hurt = super.hurt(source, amount);

        if(hurt){
            if (source.getEntity() instanceof LivingEntity target && !(target instanceof Player player && player.isCreative()))
                Primal_Util.Ai.wasHurtByAndAttacks(this, target, SnakeEntity.class, !isDancing(), false, !canBabyAttack(target));
        }

        return hurt;
    }

    protected boolean reallyHurt(DamageSource source, float amount) {
        return super.hurt(source, amount);
    }

    public boolean hurt(SnakePart part, DamageSource source, float amount) {
        if (amount < 0.01F) {
            return false;
        } else {
            this.reallyHurt(source, amount);
            return true;
        }
    }


    public boolean setEntityOnNeck(ServerPlayer player) {
        CompoundTag compoundtag = new CompoundTag();
        if(this.getEncodeId()!=null)
            compoundtag.putString("id", this.getEncodeId());
        this.saveWithoutId(compoundtag);
        if (((SetNeckEntity)(player)).setEntityOnNeck(compoundtag)) {
            this.discard();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity target) {
        var hurt = super.doHurtTarget(target);
        if(hurt){
            if(target instanceof LivingEntity living) living.addEffect(this.getSnakeEffect().getEffect(), this);
        }
        return hurt;
    }

    @Override
    public boolean killedEntity(@NotNull ServerLevel level, @NotNull LivingEntity killed) {
        //Put the cooldown to attack prey each 100 ticks (5s)
        if(killed.getType().is(Primal_Tags.Entity.SNAKE_HUNTABLE) && !this.isBaby() && this.getRandom().nextBoolean())
            this.getBrain().setMemoryWithExpiry(MemoryModuleType.HAS_HUNTING_COOLDOWN, true, 100L);

        return super.killedEntity(level, killed);
    }

    //──────────────────────────────────── Feeding & Interaction ────────────────────────────────────
    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stackInHand = player.getItemInHand(hand);

        //Remove dye
        if(stackInHand.is(Items.WATER_BUCKET) && hasCollar()){
            this.setHasCollar(false);
            this.usePlayerItem(player, hand, stackInHand);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        //To handle dye collar logic
        if (this.dyeCollar(player, hand)){
            this.setHasCollar(true);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        //Fill potion
        if(stackInHand.is(Items.GLASS_BOTTLE) && getPotionCooldown()==0 && !isAggressive()){
            // Consume bottle
            stackInHand.shrink(1);

            if (!this.level().isClientSide) {
                // Creates potion
                ItemStack effectPotion = new ItemStack(Items.POTION);

                PotionUtils.setPotion(effectPotion, this.getSnakeEffect().getHarvestablePotion());

                this.playSound(SoundEvents.BOTTLE_FILL, 1.0f, 1.0f);

                // Give to player or drop if full
                if (!player.getInventory().add(effectPotion))
                    player.drop(effectPotion, false);

                //Advancement
                if(player instanceof ServerPlayer serverPlayer)
                    Primal_Advancements.SNAKE_FILL_BOTTLE.trigger(serverPlayer);
            }

            // 30s cooldown (600 ticks)
            this.setPotionCooldown(600);

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        //Set shedding
        if(stackInHand.is(Primal_Tags.Item.SNAKE_EDIBLE_EGGS) && !this.isShedding() && this.isTame() && this.isOwnedBy(player)){
            this.triggerAnim("misc", "eat");
            stackInHand.shrink(1);
            this.level().playSound(null, this, this.getEatingSound(), this.getSoundSource(), 1.0F, 1.0F);
            this.setIsShedding(true);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        //Removes the shedding, only by the owner of is untamed
        if(isShedding() && stackInHand.is(Tags.Items.SHEARS) && (this.isOwnedBy(player) || !this.isTame())){
            Primal_Util.useShearsOnEntityAndDamage(this, player, stackInHand, hand);
            removeShedding();
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        //To handle food logic
        if(this.isFood(stackInHand)){
            boolean wasFeed = this.handleEating(player, stackInHand);
            if (wasFeed){
                this.usePlayerItem(player, hand, stackInHand);
                return InteractionResult.SUCCESS;
            }
        }

        //Only if tame and if is the owner
        if (this.isTame() && this.isOwnedBy(player))
            return changeFollowState(player, hand);

        return InteractionResult.PASS;
    }

    public boolean handleEating(@NotNull Player player, @NotNull ItemStack stack) {
        if (this.isFood(stack)) {

            //To try to tame it
            if (!this.isTame() && isTameFood(stack) && !this.level().isClientSide && this.isDancing()) {

                //25% probability of taming
                if(this.random.nextIntBetweenInclusive(0, 4)==0){
                    this.tame(player);
                    this.navigation.stop();
                    this.setTarget(null);
                    this.level().broadcastEntityEvent(this, (byte) 7);
                } else
                    this.level().broadcastEntityEvent(this, (byte) 6);

                return playEatingSound();
            }

            //To try to mate
            if(isMatingFood(stack) && !this.level().isClientSide){
                if (this.getAge() == 0 && this.canFallInLove()) {
                    this.setInLove(player);

                    return playEatingSound();
                }
            }

            //Only when tamed
            if (this.isTame() && !this.level().isClientSide) {
                //To heal the snake
                if(isHealFood(stack)){
                    if (this.getHealth() < this.getMaxHealth()) {
                        FoodProperties foodProperties = stack.getFoodProperties(this);
                        float nutrition=0;
                        if(foodProperties!=null){
                            nutrition = foodProperties.getNutrition()/2f;
                        }

                        this.heal(1f + nutrition);

                        return playEatingSound();
                    }
                }
            }

            //To age up if baby
            if (this.isBaby()) {
                int i = this.getAge();
                this.ageUp(getSpeedUpSecondsWhenFeeding(-i), true);

                return playEatingSound();
            }
        }

        return false;
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return isTameFood(stack) || isHealFood(stack) || isMatingFood(stack);
    }

    public static boolean isTameFood(@NotNull ItemStack stack){
        return stack.is(Items.RABBIT);
    }

    public static boolean isHealFood(@NotNull ItemStack stack){
        return isMatingFood(stack) || isTameFood(stack);
    }

    public static boolean isMatingFood(@NotNull ItemStack stack){
        return stack.is(Items.FERMENTED_SPIDER_EYE);
    }

    @Override
    protected void usePlayerItem(@NotNull Player player, @NotNull InteractionHand hand, @NotNull ItemStack stack) {
        if (stack.is(Items.WATER_BUCKET)) {
            player.playSound(SoundEvents.BUCKET_EMPTY);
            player.setItemInHand(hand, new ItemStack(Items.BUCKET));
        }
        else super.usePlayerItem(player, hand, stack);
    }

    @Override
    public void spawnChildFromBreeding(@NotNull ServerLevel level, @NotNull Animal mate) {
        if(this.getBrain().getMemory(MemoryModuleType.HOME).isPresent()){
            this.finalizeSpawnChildFromBreeding(level, mate, null);
            this.getBrain().setMemory(MemoryModuleType.IS_PREGNANT, Unit.INSTANCE);
            if(mate instanceof SnakeEntity mateSnake)
                this.getBrain().setMemory(Primal_MemoryModuleTypes.MATE_VARIANT.get(), mateSnake.getVariant().id);
        } else {
            super.spawnChildFromBreeding(level, mate);
        }
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob otherParent) {
        if(otherParent instanceof SnakeEntity otherParentCasted)
            return Primal_Util.createFromParents(Primal_Entities.SNAKE.get(),
                    this,
                    otherParentCasted, c-> SnakeAi.initMemories(c, level.getRandom()));

        return null;
    }

    private void removeShedding(){
        this.level().broadcastEntityEvent(this, (byte) 44);
        this.level().playSound(null, this, this.getSheddingSound(), this.getSoundSource(), 1.0F, 1.0F);
        this.setVariantFromBiome(this, this.level().getBiome(this.blockPosition()));
        this.setIsShedding(false);
        this.setSheddingTime(0);
    }

    //──────────────────────────────────── GeckoLib & Visuals ────────────────────────────────────
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(
                SnakeAnimations.mainController(this),
                SnakeAnimations.attackController(this),
                SnakeAnimations.miscController(this));
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 13)
            Primal_Util.Visuals.addParticlesAroundSelf(this, ParticleTypes.ANGRY_VILLAGER, 5);
        else if (id == 14)
            Primal_Util.Visuals.addParticlesAroundSelf(this, ParticleTypes.HAPPY_VILLAGER, 5);
        if (id == 22)
            Primal_Util.Visuals.addParticleAboveSelf(this, ParticleTypes.NOTE, 0.3, 1, 0.5);
        else if(id == 33)
            Primal_Util.Visuals.addParticleAboveSelf(this, Primal_Particles.SNAKE_SKIN_FLAKE, 0.03,
                    this.getRandom().nextIntBetweenInclusive(1, 3),
                    0.0);
        else if(id == 44)
            Primal_Util.Visuals.addParticlesAroundSelf(this,
                    Primal_Particles.SNAKE_SKIN_FLAKE,
                    0.05,
                    this.getRandom().nextIntBetweenInclusive(10, 15),
                    0.1,
                    this.isSlithering()? 0.8f: 0.1f,
                    this.isSlithering()? 0.8f: 0.1f);
        else
            super.handleEntityEvent(id);
    }

    @Override
    public void swing(@NotNull InteractionHand hand) {
        //Just in case
        this.stopTriggeredAnimation("attack", "flick");

        this.triggerAnim("attack", "bite_"+this.getSnakeState().name);
        super.swing(hand);
    }

    //──────────────────────────────────── Misc ────────────────────────────────────
    @Override
    public int getMaxHeadXRot() {
        return this.isSlithering()? 1: super.getMaxHeadXRot();
    }

    @Override
    public int getMaxHeadYRot() {
        return this.isSlithering()? 1: super.getMaxHeadYRot();
    }

    @Override
    public int getMaxAirSupply() {
        return 2400;
    }

    @Override
    public boolean canDrownInFluidType(@NotNull FluidType type) {
        if(this.getVariant() == SnakeEntity.Variant.MARINE) return false;

        return super.canDrownInFluidType(type);
    }

    @Override
    public boolean shouldTryTeleportToOwner() {
        LivingEntity livingentity = this.getOwner();
        return livingentity != null && this.distanceToSqr(this.getOwner()) >= (20*20);
    }

    @Override
    public boolean isTargetingVillager() {
        return this.getTarget() instanceof Villager
                //Last Target
                || this.getLastHurtMob() instanceof Villager
                //Cautious target
                || (this.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_CAUTIOUS.get()).isPresent() && this.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_CAUTIOUS.get()).get() instanceof Villager);
    }

    @Override
    public boolean canBeLeashed(@NotNull Player entity) {
        return false;
    }

    @Override
    public boolean isEntityTargetable(LivingEntity attacker, LivingEntity target) {
        return attacker.getBrain().isMemoryValue(MemoryModuleType.ATTACK_TARGET, target)
                ? Primal_Util.Ai.getTargetConditions(36, true).test(attacker, target)
                : Primal_Util.Ai.getTargetConditions(36, false).test(attacker, target);
    }

    @Override
    public boolean isEntityAttackable(LivingEntity attacker, LivingEntity target) {
        return attacker.getBrain().isMemoryValue(MemoryModuleType.ATTACK_TARGET, target)
                ? Primal_Util.Ai.getTargetConditions(36, true).test(attacker, target)
                : Primal_Util.Ai.getTargetConditions(36, false).test(attacker, target);
    }

    @Override
    protected float getWaterSlowDown() {
        return this.getVariant()== SnakeEntity.Variant.MARINE? 0.96F: 0.9f;
    }

    //──────────────────────────────────── Sounds ────────────────────────────────────
    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return Primal_Sounds.SNAKE_IDLE.get();
    }

    @Override
    public int getAmbientSoundInterval() {
        if(isDancing())
            return this.getRandom().nextIntBetweenInclusive(Primal_Util.toTicks(25), Primal_Util.toTicks(50));

        return super.getAmbientSoundInterval();
    }

    @Override
    public SoundEvent getInsideLogSound() {
        return Primal_Sounds.SNAKE_SNORING.get();
    }

    @Override
    public SoundEvent getEnterLogSound() {
        return Primal_Sounds.SNAKE_POP_IN.get();
    }

    @Override
    public SoundEvent getExitLogSound() {
        return Primal_Sounds.SNAKE_POP_OUT.get();
    }

    public SoundEvent getSheddingSound(){
        return Primal_Sounds.SNAKE_BREAKS_SKIN.get();
    }

    @Override
    public void setVariantFromLog(int otherParentVariant) {
        this.setVariant(this.level().getRandom().nextBoolean()? this.getVariant(): SnakeEntity.Variant.byId(otherParentVariant));
    }

    @Override
    public boolean staysOnLog() {
        return this.level().isRaining() || (Primal_Util.getDaytime(this.level()) >= 0 && Primal_Util.getDaytime(this.level()) < 8000);
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return Primal_Sounds.SNAKE_HURT.get();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return Primal_Sounds.SNAKE_DEATH.get();
    }

    public SoundEvent getEatingSound() {
        return Primal_Sounds.SNAKE_EATS.get();
    }

    public boolean playEatingSound(){
        this.triggerAnim("misc", "eat");
        //Play the sound
        if (!this.isSilent()) {
            SoundEvent soundEvent = this.getEatingSound();
            if (soundEvent != null) {
                this.level()
                        .playSound(
                                null, this.getX(), this.getY(), this.getZ(), soundEvent, this.getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F
                        );
            }
        }

        return true;
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {}

    //──────────────────────────────────── Misc ────────────────────────────────────
    @Override
    public int getEnterCooldown() {
        //Set cooldown between 10 and 20s
        return this.getRandom().nextIntBetweenInclusive(Primal_Util.toTicks(10), Primal_Util.toTicks(20));
    }

    @SuppressWarnings("unused")
    public static boolean checkSnakeSpawnRules(
            EntityType<SnakeEntity> snake, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random
    ) {
        return level.getBlockState(pos.below()).is(Primal_Tags.Block.SNAKE_SPAWN_ON) && isBrightEnoughToSpawn(level, pos);
    }

    //──────────────────────────────────── Neutral Behavior ────────────────────────────────────
    @Override
    public int getRemainingPersistentAngerTime() {return 0;}

    @Override
    public void setRemainingPersistentAngerTime(int remainingPersistentAngerTime) {}

    @Override
    public @Nullable UUID getPersistentAngerTarget() {return null;}

    @Override
    public void setPersistentAngerTarget(@Nullable UUID persistentAngerTarget) {}

    @Override
    public void startPersistentAngerTimer() {}

    public enum SnakeState implements StringRepresentable {
        STANDING("standing"),
        SLITHERING("slithering");

        @SuppressWarnings("deprecation")
        private static final StringRepresentable.EnumCodec<SnakeEntity.SnakeState> CODEC = StringRepresentable.fromEnum(SnakeEntity.SnakeState::values);

        private final String name;

        SnakeState(String name) {
            this.name = name;
        }

        public static SnakeEntity.SnakeState fromName(String name) {
            return CODEC.byName(name, STANDING);
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }

    }

    public enum SnakeEffect implements StringRepresentable {
        POISON(0, "poison", MobEffects.POISON, 200, 0, Potions.POISON),
        WEAKNESS(1, "weakness", MobEffects.WEAKNESS, 200, 0, Potions.WEAKNESS),
        SLOWNESS(2, "slowness", MobEffects.MOVEMENT_SLOWDOWN, 200, 0, Potions.SLOWNESS);

        private static final IntFunction<SnakeEntity.SnakeEffect> BY_ID = ByIdMap.continuous(SnakeEntity.SnakeEffect::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);

        private final String name;
        private final int id;
        private final MobEffect effect;
        private final int duration;
        private final int amplifier;
        private final Potion harvestablePotion;

        SnakeEffect(int id, String name, MobEffect effect, int duration, int amplifier, Potion harvestablePotion) {
            this.name = name;
            this.effect=effect;
            this.id = id;
            this.duration = duration;
            this.amplifier = amplifier;
            this.harvestablePotion=harvestablePotion;
        }

        public static SnakeEntity.SnakeEffect byId(int id) {
            return BY_ID.apply(id);
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }

        private int id() {
            return this.id;
        }

        public MobEffectInstance getEffect() {
            return new MobEffectInstance(effect, duration, amplifier);
        }

        public Potion getHarvestablePotion() {
            return harvestablePotion;
        }
    }

    public static class SongListener implements GameEventListener {
        private final SnakeEntity snake;
        private final PositionSource listenerSource;
        private final int listenerRadius;

        public SongListener(SnakeEntity snake, PositionSource listenerSource, int listenerRadius) {
            this.listenerSource = listenerSource;
            this.listenerRadius = listenerRadius;
            this.snake=snake;
        }

        @Override
        public @NotNull PositionSource getListenerSource() {
            return this.listenerSource;
        }

        @Override
        public int getListenerRadius() {
            return this.listenerRadius;
        }

        @Override
        public boolean handleGameEvent(@NotNull ServerLevel level, GameEvent gameEvent, GameEvent.@NotNull Context context, @NotNull Vec3 pos) {
            if (gameEvent.equals(GameEvent.JUKEBOX_PLAY) || gameEvent.equals(GameEvent.NOTE_BLOCK_PLAY)) {

                if(gameEvent.equals(GameEvent.JUKEBOX_PLAY))
                    snake.brain.setMemory(MemoryModuleType.DANCING, true);

                if(gameEvent.equals(GameEvent.NOTE_BLOCK_PLAY))
                    snake.brain.setMemoryWithExpiry(MemoryModuleType.DANCING, true, 10);

                if(snake.brain.getMemory(Primal_MemoryModuleTypes.MUSIC_BLOCK.get()).isEmpty())
                    snake.brain.setMemory(Primal_MemoryModuleTypes.MUSIC_BLOCK.get(), BlockPos.containing(pos));

                return true;
            } else if (gameEvent.equals(GameEvent.JUKEBOX_STOP_PLAY)){
                if(snake.brain.getMemory(MemoryModuleType.DANCING).isPresent())
                    snake.brain.eraseMemory(MemoryModuleType.DANCING);
                if(snake.brain.getMemory(Primal_MemoryModuleTypes.MUSIC_BLOCK.get()).isEmpty())
                    snake.brain.eraseMemory(Primal_MemoryModuleTypes.MUSIC_BLOCK.get());
                return false;
            } else {
                return false;
            }
        }
    }
}