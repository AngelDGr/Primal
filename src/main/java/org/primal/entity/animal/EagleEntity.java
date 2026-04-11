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
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.Unit;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.client.animation.entity.EagleAnimations;
import org.primal.entity.ai.EagleAi;
import org.primal.entity.ai.controls.look.EagleLookControl;
import org.primal.entity.ai.controls.move.EagleMoveControl;
import org.primal.entity.ai.controls.navigation.EaglePathNavigation;
import org.primal.registry.*;
import org.primal.util.Primal_Util;
import org.primal.util.mob_types.DetectsFartherAway;
import org.primal.util.mob_types.HostileMount;
import org.primal.util.mob_types.PrimalTamable;
import org.primal.util.mob_types.VariantHolderWithEgg;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.IntFunction;

//xTODO:
// x Attack behavior
// ? Stand above leaves and nests
// x Fly away when something is nearby, unless sneaking
// x Hunt regularly
// x Breed with rabbit stew
// x Lay egg behavior, they try to lay it on a nest
// x Add spawning nest feature to the world
// x The hatchlings stay in the nest
// x Tamed by feeding raw rabbit to hatchlings
// x Dyeable band around their leg when tamed
// x Follow their owner
// x Loud chirp when spot a threat, applies glowing to that threat
// x Attack when you steal an egg

public class EagleEntity extends TamableAnimal implements VariantHolder<EagleEntity.Variant>, GeoEntity, NeutralMob, VariantHolderWithEgg<EagleEntity.Variant, EagleEntity>, HostileMount, FlyingAnimal, DetectsFartherAway, PrimalTamable {

    //Attributes and Variants
    public enum Variant implements StringRepresentable {
        BALD(0, "bald"),
        GOLDEN(1, "golden"),
        HARPY(2, "harpy"),
        PHILIPPINE(3, "philippine");

        public static final Codec<EagleEntity.Variant> CODEC = StringRepresentable.fromEnum(EagleEntity.Variant::values);

        private static final IntFunction<EagleEntity.Variant> BY_ID = ByIdMap.continuous(EagleEntity.Variant::getId, values(),
                ByIdMap.OutOfBoundsStrategy.CLAMP);

        public static EagleEntity.Variant byId(int id) {
            return BY_ID.apply(id);
        }

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

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20f)
                .add(Attributes.MOVEMENT_SPEED, 0.24f)
                .add(Attributes.ATTACK_DAMAGE, 1.5f)
                .add(Attributes.FLYING_SPEED, 0.4F)
                .add(Attributes.FOLLOW_RANGE, 48.0F);
    }

    //Init
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public EagleEntity(EntityType<EagleEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new EagleMoveControl(this);
        this.lookControl = new EagleLookControl(this);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.LEAVES, 1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.setMaxUpStep(2.0f);
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnType, @javax.annotation.Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        setVariantFromBiome(this, level.getBiome(this.blockPosition()));
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData, compoundTag);
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        if(this.isOrderedToSit() && !this.isSitting()) this.setFollowerState(SITTING_STATE);
    }

    @Override
    public void setVariantFromBiome(EagleEntity animal, Holder<Biome> holder){
        if (holder.is(Primal_Tags.Biome.SPAWNS_GOLDEN_EAGLE)) {
            animal.setVariant(Variant.GOLDEN);
        } else if(holder.is(Primal_Tags.Biome.SPAWNS_HARPY_EAGLE)){
            animal.setVariant(Variant.HARPY);
        } else if(holder.is(Primal_Tags.Biome.SPAWNS_PHILIPPINE_EAGLE)) {
            animal.setVariant(Variant.PHILIPPINE);
        } else {
            animal.setVariant(Variant.BALD);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(
                EagleAnimations.mainController(this)
        );
    }

    @Override
    public int getMaxHeadXRot() {
        return this.onGround()? 40: 90;
    }

    @Override
    public int getMaxHeadYRot() {
        return this.onGround()?  15: 1;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void swing(@NotNull InteractionHand hand) {
        super.swing(hand);
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entity) {
        return super.doHurtTarget(entity);
    }

    @Override
    public void tick() {
        super.tick();
    }

    //SynchedData
    private static final EntityDataAccessor<Integer> DATA_VARIANT_ID = SynchedEntityData.defineId(EagleEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> HEALTH_WHEN_START_RIDING = SynchedEntityData.defineId(EagleEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DATA_COLLAR_COLOR = SynchedEntityData.defineId(EagleEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> FOLLOWER_STATE = SynchedEntityData.defineId(EagleEntity.class, EntityDataSerializers.INT);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_VARIANT_ID, EagleEntity.Variant.BALD.id);
        this.entityData.define(HEALTH_WHEN_START_RIDING, 0f);
        this.entityData.define(FOLLOWER_STATE, 0);
        this.entityData.define(DATA_COLLAR_COLOR, DyeColor.RED.getId());
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant().id);
        this.addAdditionalSaveDataHostileMount(compound);
        this.addAdditionalSaveDataTamable(compound);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(EagleEntity.Variant.byId(compound.getInt("Variant")));
        this.readAdditionalSaveDataHostileMount(compound);
        this.readAdditionalSaveDataTamable(compound);
    }

    @Override
    public float getHealthWhenStartRiding() {
        return this.entityData.get(HEALTH_WHEN_START_RIDING);
    }

    @Override
    public void setHealthWhenStartRiding(float healthWhenStartRiding) {
        this.entityData.set(HEALTH_WHEN_START_RIDING, healthWhenStartRiding);
    }

    @Override
    public void setVariant(@NotNull EagleEntity.Variant variant) {
        this.entityData.set(DATA_VARIANT_ID, variant.id);
    }

    @Override
    public @NotNull EagleEntity.Variant getVariant() {
        return EagleEntity.Variant.byId(this.entityData.get(DATA_VARIANT_ID));
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
    public int getFollowerState() {
        return this.entityData.get(FOLLOWER_STATE);
    }

    @Override
    public void setFollowerState(int state) {
        this.entityData.set(FOLLOWER_STATE, state);
    }

    //Breeding
    @Override
    public void spawnChildFromBreeding(@NotNull ServerLevel level, @NotNull Animal mate) {
        this.finalizeSpawnChildFromBreeding(level, mate, null);
        this.getBrain().setMemory(MemoryModuleType.IS_PREGNANT, Unit.INSTANCE);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob otherParent) {
        if(otherParent instanceof EagleEntity otherParentCasted)
            return Primal_Util.createFromParents(Primal_Entities.EAGLE.get(),
                    this,
                    otherParentCasted, c-> EagleAi.initMemories(c, level.getRandom()));

        return null;
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return isMatingFood(stack) || isHealFood(stack) || isTameFood(stack);
    }

    public static boolean isTameFood(@NotNull ItemStack stack){
        return stack.is(Items.CHICKEN);
    }

    public static boolean isHealFood(@NotNull ItemStack stack){
        return isTameFood(stack) || isMatingFood(stack);
    }

    public static boolean isMatingFood(@NotNull ItemStack stack){
        return stack.is(Items.RABBIT_STEW);
    }

    //Movement
    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new EaglePathNavigation(this, level);
    }

    @Override
    public boolean isFlying() {
        return !onGround();
    }

    @Override
    public boolean canFlyToOwner() {
        return !this.isBaby();
    }

    public void stopMoving(){
        this.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        this.getNavigation().stop();
        this.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
    }

    @Override
    public boolean isAffectedByFluids() {
        return this.isBaby() || this.getBrain().isActive(Primal_Activities.SIT.get());
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();

        Brain<EagleEntity> brain = this.getBrain();
        brain.tick((ServerLevel) this.level(), this);
        var snatching= this.getBrain().getMemory(Primal_MemoryModuleTypes.IS_GRABBING.get());

        //Fallback to reset, just in case
        if(snatching.isPresent() && snatching.get() && this.getPassengers().isEmpty()){
            this.getBrain().eraseMemory(Primal_MemoryModuleTypes.IS_GRABBING.get());
            this.setPose(Pose.STANDING);
        }

        if(snatching.isEmpty() && !this.getPassengers().isEmpty())
            this.ejectPassengers();

        EagleAi.updateActivity(this);
    }

    @Override
    public void aiStep() {
        super.aiStep();
    }

//    @Override
//    public void travel(@NotNull Vec3 travelVector) {
//        if (this.isControlledByLocalInstance()) {
//            double d0 = this.getGravity();
//            boolean flag = this.getDeltaMovement().y <= 0.0;
//            if (flag && this.hasEffect(MobEffects.SLOW_FALLING)) {
//                d0 = Math.min(d0, 0.01);
//            }
//
//            FluidState fluidstate = this.level().getFluidState(this.blockPosition());
//            if ((this.isInWater() || (this.isInFluidType(fluidstate) && fluidstate.getFluidType() != net.neoforged.neoforge.common.NeoForgeMod.LAVA_TYPE.value())) && this.isAffectedByFluids() && !this.canStandOnFluid(fluidstate)) {
//                if (this.isInWater() || (this.isInFluidType(fluidstate) && !this.moveInFluid(fluidstate, travelVector, d0))) {
//                    double d9 = this.getY();
//                    float f4 = this.isSprinting() ? 0.9F : this.getWaterSlowDown();
//                    float f5 = 0.02F;
//                    float f6 = (float)this.getAttributeValue(Attributes.WATER_MOVEMENT_EFFICIENCY);
//                    if (!this.onGround()) {
//                        f6 *= 0.5F;
//                    }
//
//                    if (f6 > 0.0F) {
//                        f4 += (0.54600006F - f4) * f6;
//                        f5 += (this.getSpeed() - f5) * f6;
//                    }
//
//                    if (this.hasEffect(MobEffects.DOLPHINS_GRACE)) {
//                        f4 = 0.96F;
//                    }
//
//                    f5 *= (float)this.getAttributeValue(net.neoforged.neoforge.common.NeoForgeMod.SWIM_SPEED);
//                    this.moveRelative(f5, travelVector);
//                    this.move(MoverType.SELF, this.getDeltaMovement());
//                    Vec3 vec36 = this.getDeltaMovement();
//                    if (this.horizontalCollision && this.onClimbable()) {
//                        vec36 = new Vec3(vec36.x, 0.2, vec36.z);
//                    }
//
//                    this.setDeltaMovement(vec36.multiply(f4, 0.8F, f4));
//                    Vec3 vec32 = this.getFluidFallingAdjustedMovement(d0, flag, this.getDeltaMovement());
//                    this.setDeltaMovement(vec32);
//                    if (this.horizontalCollision && this.isFree(vec32.x, vec32.y + 0.6F - this.getY() + d9, vec32.z)) {
//                        this.setDeltaMovement(vec32.x, 0.3F, vec32.z);
//                    }
//                }
//            } else if (this.isInLava() && this.isAffectedByFluids() && !this.canStandOnFluid(fluidstate)) {
//                double d8 = this.getY();
//                this.moveRelative(0.02F, travelVector);
//                this.move(MoverType.SELF, this.getDeltaMovement());
//                if (this.getFluidHeight(FluidTags.LAVA) <= this.getFluidJumpThreshold()) {
//                    this.setDeltaMovement(this.getDeltaMovement().multiply(0.5, 0.8F, 0.5));
//                    Vec3 vec33 = this.getFluidFallingAdjustedMovement(d0, flag, this.getDeltaMovement());
//                    this.setDeltaMovement(vec33);
//                } else {
//                    this.setDeltaMovement(this.getDeltaMovement().scale(0.5));
//                }
//
//                if (d0 != 0.0) {
//                    this.setDeltaMovement(this.getDeltaMovement().add(0.0, -d0 / 4.0, 0.0));
//                }
//
//                Vec3 vec34 = this.getDeltaMovement();
//                if (this.horizontalCollision && this.isFree(vec34.x, vec34.y + 0.6F - this.getY() + d8, vec34.z)) {
//                    this.setDeltaMovement(vec34.x, 0.3F, vec34.z);
//                }
//            } else if (this.isFallFlying()) {
//                this.checkSlowFallDistance();
//                Vec3 vec3 = this.getDeltaMovement();
//                Vec3 vec31 = this.getLookAngle();
//                float f = this.getXRot() * (float) (Math.PI / 180.0);
//                double d1 = Math.sqrt(vec31.x * vec31.x + vec31.z * vec31.z);
//                double d3 = vec3.horizontalDistance();
//                double d4 = vec31.length();
//                double d5 = Math.cos(f);
//                d5 = d5 * d5 * Math.min(1.0, d4 / 0.4);
//                vec3 = this.getDeltaMovement().add(0.0, d0 * (-1.0 + d5 * 0.75), 0.0);
//                if (vec3.y < 0.0 && d1 > 0.0) {
//                    double d6 = vec3.y * -0.1 * d5;
//                    vec3 = vec3.add(vec31.x * d6 / d1, d6, vec31.z * d6 / d1);
//                }
//
//                if (f < 0.0F && d1 > 0.0) {
//                    double d10 = d3 * (double)(-Mth.sin(f)) * 0.04;
//                    vec3 = vec3.add(-vec31.x * d10 / d1, d10 * 3.2, -vec31.z * d10 / d1);
//                }
//
//                if (d1 > 0.0) {
//                    vec3 = vec3.add((vec31.x / d1 * d3 - vec3.x) * 0.1, 0.0, (vec31.z / d1 * d3 - vec3.z) * 0.1);
//                }
//
//                this.setDeltaMovement(vec3.multiply(0.99F, 0.98F, 0.99F));
//                this.move(MoverType.SELF, this.getDeltaMovement());
//                if (this.horizontalCollision && !this.level().isClientSide) {
//                    double d11 = this.getDeltaMovement().horizontalDistance();
//                    double d7 = d3 - d11;
//                    float f1 = (float)(d7 * 10.0 - 3.0);
//                    if (f1 > 0.0F) {
//                        this.playSound(this.getFallDamageSound((int)f1), 1.0F, 1.0F);
//                        this.hurt(this.damageSources().flyIntoWall(), f1);
//                    }
//                }
//
//                if (this.onGround() && !this.level().isClientSide) {
//                    this.setSharedFlag(7, false);
//                }
//            } else {
//                BlockPos blockpos = this.getBlockPosBelowThatAffectsMyMovement();
//                float f2 = this.level().getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).getFriction(level(), this.getBlockPosBelowThatAffectsMyMovement(), this);
//                float f3 = this.onGround() ? f2 * 0.91F : 0.98F;
//                Vec3 vec35 = this.handleRelativeFrictionAndCalculateMovement(travelVector, f2);
//                double d2 = vec35.y;
//                if (this.hasEffect(MobEffects.LEVITATION)) {
//                    d2 += (0.05 * (double)(this.getEffect(MobEffects.LEVITATION).getAmplifier() + 1) - vec35.y) * 0.2;
//                } else if (!this.level().isClientSide || this.level().hasChunkAt(blockpos)) {
//                    d2 -= d0;
//                } else if (this.getY() > (double)this.level().getMinBuildHeight()) {
//                    d2 = -0.1;
//                } else {
//                    d2 = 0.0;
//                }
//
//                if (this.shouldDiscardFriction()) {
//                    this.setDeltaMovement(vec35.x, d2, vec35.z);
//                } else {
//                    this.setDeltaMovement(vec35.x * (double)f3, this instanceof FlyingAnimal ? d2 * (double)f3 : d2 * 0.98F, vec35.z * (double)f3);
//                }
//            }
//        }
//
//        this.calculateEntityAnimation(this instanceof FlyingAnimal);
//    }

    @Override
    public boolean shouldDiscardFriction() {
        return super.shouldDiscardFriction();
    }

    @Override
    public void setSpeed(float speed) {
        super.setSpeed(speed);
        this.setZza(speed);
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos) {
        //Only fall damage when baby
        if(this.isBaby()){
            super.checkFallDamage(y, onGround, state, pos);
        }
    }

    //AI
    @Override
    protected void registerGoals() {}

    @Override
    protected @NotNull Brain.Provider<EagleEntity> brainProvider() {
        return EagleAi.brainProvider();
    }

    @Override
    protected @NotNull Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
        return EagleAi.makeBrain(this.brainProvider().makeBrain(dynamic));
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull Brain<EagleEntity> getBrain() {
        return (Brain<EagleEntity>) super.getBrain();
    }

    @Nullable
    @Override
    public LivingEntity getTarget() {
        return Primal_Util.OneTwentyEquivalent.getTargetFromBrain(this);
    }

    @Override
    public boolean canAttack(@NotNull LivingEntity target) {
        Optional<List<UUID>> attackedList = this.getBrain().getMemory(Primal_MemoryModuleTypes.ATTACKED_LIST.get());

        Optional<UUID> lastUuid = attackedList.map(l->l.get(l.size()-1));

        Optional<LivingEntity> lastEntity =
                lastUuid.isPresent() && !this.level().isClientSide && ((ServerLevel)this.level()).getEntity(lastUuid.get()) instanceof LivingEntity living ?
                        Optional.of(living):
                        Optional.empty();

        if(super.canAttack(target) && lastEntity.isPresent() && !lastEntity.get().isDeadOrDying() && lastEntity.get()==target && Primal_Util.isSameEagleAttacking(target, this))
            return true;

        if((this.getBrain().isMemoryValue(MemoryModuleType.ATTACK_TARGET, target))
                && super.canAttack(target)
                && Primal_Util.isSameEagleAttacking(target, this))
            return true;

        //Pass to the sensor to handle tamed logic
        if(this.isTame())
            return super.canAttack(target) && Primal_Util.isNotNeverAttack(target) && Primal_Util.isSameEagleAttacking(target, this);

        return super.canAttack(target)
                && Primal_Util.isNotNeverAttack(target)
                //Don't attack an eagle unless that eagle attacks first
                && (!(target instanceof EagleEntity) && this.getLastHurtByMob()!=target)
                &&
                        //Hunts regularly
                        ((target.getType().is(Primal_Tags.Entity.EAGLE_HUNTABLE) && !this.getBrain().hasMemoryValue(MemoryModuleType.HAS_HUNTING_COOLDOWN))
                        //To attack the last one that hurt it
                        || this.getLastHurtByMob()==target)
                //To no set attackable underwater
                && !this.isUnderWater()
                && Primal_Util.isSameEagleAttacking(target, this);
    }

    public boolean canPickUpEntity(@NotNull Entity target){
        //Eagle Size: 1.0166666507720947 * 1.049 = 1.0664833166599272
        return target.getBoundingBox().getSize()<this.getBoundingBox().getSize()*1.049
                && !this.getBrain().hasMemoryValue(Primal_MemoryModuleTypes.IS_STUNNED.get())
                && (target instanceof LivingEntity livingTarget && livingTarget.getHealth()>0)
                && !(target instanceof FlyingAnimal || target instanceof FlyingMob)
                && !this.isBaby();
    }

    public boolean canBabyAttack(@NotNull Entity target){
        //Eagle Size: 1.0166666507720947 * 1.049 = 1.0664833166599272
        return target.getBoundingBox().getSize()<this.getBoundingBox().getSize()*1.049 && this.isBaby();
    }

    @Override
    public boolean killedEntity(@NotNull ServerLevel level, @NotNull LivingEntity killed) {
        //Put the cooldown to attack prey each 600 ticks (30s)
        if(killed.getType().is(Primal_Tags.Entity.EAGLE_HUNTABLE) && !this.isBaby()){
            this.getBrain().setMemoryWithExpiry(MemoryModuleType.HAS_HUNTING_COOLDOWN, true, 600L);
        }

        return super.killedEntity(level, killed);
    }

    @SuppressWarnings("all")
    //Damage necessary to being released
    private final float healthLossToBeReleased =2.0f;

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        boolean hurt = super.hurt(source, amount);

        if(hurt){
            this.hurtAndReleasePassenger(2, 60L);

            if (source.getEntity() instanceof LivingEntity target
                    //Not be angry with creative player
                    && !(target instanceof Player player && player.isCreative())
                    //To not get angry with owner
                    && target!=this.getOwner()
                    //Not be angry other owned eagle
                    && !(target instanceof EagleEntity eagle2 && eagle2.getOwner()!=null && eagle2.getOwner()==this.getOwner())
            ) {
                Primal_Util.Ai.wasHurtByAndAttacks(this, target, EagleEntity.class, false, false, true);
            }

            if(source.getEntity()!=null && source.getEntity() instanceof LivingEntity target)
                this.getBrain().setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, target, this.level().getRandom().nextIntBetweenInclusive(20, 80));
        }

        return hurt;
    }

    //Snatch
    @Override
    public @NotNull Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
        return new Vec3(passenger.getX(), passenger.getY()+0.1, passenger.getZ());
    }

    @Override
    public boolean shouldRiderSit() {
        return false;
    }

    @Override
    public boolean canRiderInteract() {
        return true;
    }

    @Override
    public @Nullable LivingEntity getControllingPassenger() {
        return null;
    }

    @Override
    protected void positionRider(@NotNull Entity passenger, @NotNull MoveFunction callback) {
        if (this.hasPassenger(passenger)) {

            boolean isAirBelow=this.level().getBlockState(this.blockPosition().below()).isAir();

            // Vertical position
            double y = isAirBelow? this.getY()- passenger.getBoundingBox().getYsize()*1: this.getY();

            // Offset forward and right relative to the body rotation
            float bodyYawRad = this.yBodyRot * Mth.DEG_TO_RAD;
            double forwardOffset = isAirBelow? +0.2: -passenger.getBoundingBox().getXsize()*0.7; // Forward (Z direction)
            double sideOffset = -0.0;    // Right (X direction)

            double x = this.getX() + (Mth.sin(bodyYawRad) * forwardOffset) + (Mth.cos(bodyYawRad) * sideOffset);
            double z = this.getZ() - (Mth.cos(bodyYawRad) * forwardOffset) + (Mth.sin(bodyYawRad) * sideOffset);

            callback.accept(passenger, x, y, z);
        }
    }

    //Eating
    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stackInHand = player.getItemInHand(hand);

        //To handle dye collar logic
        if (this.dyeCollar(player, hand))
            return InteractionResult.sidedSuccess(this.level().isClientSide);

        //To handle food logic
        if(this.isFood(stackInHand)){
            boolean wasFeed = this.handleEating(player, stackInHand);
            if (wasFeed) {
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
            //To try to mate
            if(isMatingFood(stack) && !this.level().isClientSide){
                if (this.getAge() == 0 && this.canFallInLove()) {
                    this.setInLove(player);

                    return playEatingSound();
                }
            }

            //To try to tame it
            if (!this.isTame() && isTameFood(stack) && this.isBaby() && !this.level().isClientSide) {

                if (this.random.nextIntBetweenInclusive(0, 5)==0) {
                    this.tame(player);
                    this.navigation.stop();
                    this.setTarget(null);
                    this.level().broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level().broadcastEntityEvent(this, (byte) 6);
                }

                return playEatingSound();
            }

            //Only when tamed
            if (this.isTame() && !this.level().isClientSide) {
                //To heal the bear
                if(isHealFood(stack)){
                    if (this.getHealth() < this.getMaxHealth()) {
                        this.heal(2f);

                        return playEatingSound();
                    }
                }
            }

            //To age up if baby
            if (this.isBaby() && !isTameFood(stack)) {
                int i = this.getAge();
                this.ageUp(getSpeedUpSecondsWhenFeeding(-i), true);

                return playEatingSound();
            }
        }

        return false;
    }

    //Misc
    @Override
    protected @NotNull Vec3 getLeashOffset() {
        return new Vec3(0.0, this.getEyeHeight()*0.7f, this.getBbWidth() * 0.2F);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 13) {
            Primal_Util.Visuals.addParticlesAroundSelf(this, ParticleTypes.ANGRY_VILLAGER, 5);
        } else if (id == 14) {
            Primal_Util.Visuals.addParticlesAroundSelf(this, ParticleTypes.HAPPY_VILLAGER, 5);
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public boolean isEntityTargetable(LivingEntity attacker, LivingEntity target) {
        return attacker.getBrain().isMemoryValue(MemoryModuleType.ATTACK_TARGET, target)
                ? Primal_Util.Ai.getTargetConditions(48, true).test(attacker, target)
                : Primal_Util.Ai.getTargetConditions(48, false).test(attacker, target);
    }

    @Override
    public boolean isEntityAttackable(LivingEntity attacker, LivingEntity target) {
        return attacker.getBrain().isMemoryValue(MemoryModuleType.ATTACK_TARGET, target)
                ? Primal_Util.Ai.getTargetConditions(48, true).test(attacker, target)
                : Primal_Util.Ai.getTargetConditions(48, false).test(attacker, target);
    }

    @Override
    public boolean shouldTryTeleportToOwner() {
        LivingEntity owner = this.getOwner();
        if(owner == null) return false;

        if(this.onGround())
            return this.distanceToSqr(this.getOwner()) >= (15*15);
        else
            return this.distanceToSqr(this.getOwner()) >= (25*25);
    }

    @Override
    public void teleportToAroundBlockPos(@NotNull BlockPos pos) {
        // ---- PASS 1: prefer ground ----
        for (int i = 0; i < 5; i++) {
            int j = this.random.nextIntBetweenInclusive(-3, 3);
            int k = this.random.nextIntBetweenInclusive(-3, 3);

            if (Math.abs(j) >= 2 || Math.abs(k) >= 2) {
                int x = pos.getX() + j;
                int z = pos.getZ() + k;

                // find ground below owner Y
                int y = findGroundY(x, pos.getY(), z);

                if (y != Integer.MIN_VALUE) {
                    if (this.maybeTeleportTo(x, y, z)) {
                        return;
                    }
                }
            }
        }

        // ---- PASS 2: allow air teleport (elytra / flying follow) ----
        for (int i = 0; i < 10; i++) {
            int j = this.random.nextIntBetweenInclusive(-3, 3);
            int k = this.random.nextIntBetweenInclusive(-3, 3);

            if (Math.abs(j) >= 2 || Math.abs(k) >= 2) {
                int l = this.random.nextIntBetweenInclusive(-1, 1);
                if (this.maybeTeleportTo(pos.getX() + j, pos.getY() + l, pos.getZ() + k)) {
                    return;
                }
            }
        }
    }

    protected @NotNull Vec3 getMeleeAttackReferencePosition() {
        return super.getMeleeAttackReferencePosition().add(0, 0.5, 0);
    }


    @Override
    public double getMeleeAttackRangeSqr(@NotNull LivingEntity target) {
        return this.getBbWidth() * 2.5F * this.getBbWidth() * 2.5F;
    }

    private int findGroundY(int x, int startY, int z) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, startY, z);

        int minY = Math.max(startY - 2, this.level().getMinBuildHeight());

        for (int y = startY; y >= minY; y--) {
            pos.setY(y);

            BlockState state = this.level().getBlockState(pos);
            BlockState below = this.level().getBlockState(pos.below());

            if (state.isAir() && below.isSolid()) {
                return y;
            }
        }

        return Integer.MIN_VALUE;
    }

    //Teleports to any block, even air blocks
    @Override
    public boolean canTeleportTo(@NotNull BlockPos pos) {
        BlockPos blockpos = pos.subtract(this.blockPosition());
        return this.level().noCollision(this, this.getBoundingBox().move(blockpos));
    }

    protected boolean playEatingSound(){
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
    public boolean showVehicleHealth() {
        return false;
    }

    //Sounds
    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return Primal_Sounds.EAGLE_IDLE.get();
    }

    @Override
    protected boolean isFlapping() {
        //Above 0.3 is gliding so shouldn't have a flapping sound
        double speed = this.getDeltaMovement().length();

        return !this.onGround() && !this.isInWater() && speed<0.3 && (float)this.tickCount % 15.0F == 0.0F;
    }

    @Override
    protected void onFlap() {
        this.playSound(Primal_Sounds.EAGLE_FLAP.get(), 0.15F, 1.0F);
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return Primal_Sounds.EAGLE_HURT.get();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return Primal_Sounds.EAGLE_DEATH.get();
    }

    public @Nullable SoundEvent getEatingSound() {
        return Primal_Sounds.EAGLE_EAT.get();
    }

    public @Nullable SoundEvent getShriekSound() {
        if(this.getVariant()==Variant.BALD && this.random.nextIntBetweenInclusive(0, 500)==0){
            return Primal_Sounds.EAGLE_FREEDOM.get();
        }

        return Primal_Sounds.EAGLE_SHRIEK.get();
    }

    //Just to being classified as neutral
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
}
