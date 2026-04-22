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
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.block.NestBlock;
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

public class EagleEntity extends TamableAnimal implements VariantHolder<EagleEntity.Variant>, NeutralMob, VariantHolderWithEgg<EagleEntity.Variant, EagleEntity>, HostileMount, FlyingAnimal, DetectsFartherAway, PrimalTamable {

    //──────────────────────────────────── Variants ────────────────────────────────────
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

    @Override
    public void setVariant(@NotNull EagleEntity.Variant variant) {
        this.entityData.set(DATA_VARIANT_ID, variant.id);
    }

    @Override
    public @NotNull EagleEntity.Variant getVariant() {
        return EagleEntity.Variant.byId(this.entityData.get(DATA_VARIANT_ID));
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

    //──────────────────────────────────── Init ────────────────────────────────────
    public EagleEntity(EntityType<EagleEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new EagleMoveControl(this);
        this.lookControl = new EagleLookControl(this);
        this.setPathfindingMalus(PathType.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0F);
        this.setPathfindingMalus(PathType.LEAVES, 1.0F);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20f)
                .add(Attributes.MOVEMENT_SPEED, 0.24f)
                .add(Attributes.ATTACK_DAMAGE, 1.5f)
                .add(Attributes.FLYING_SPEED, 0.4F)
                .add(Attributes.FOLLOW_RANGE, 48.0F)
                .add(Attributes.STEP_HEIGHT, 2.0f);
    }

    private static final EntityDataAccessor<Integer> DATA_VARIANT_ID = SynchedEntityData.defineId(EagleEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> HEALTH_WHEN_START_RIDING = SynchedEntityData.defineId(EagleEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DATA_COLLAR_COLOR = SynchedEntityData.defineId(EagleEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> FOLLOWER_STATE = SynchedEntityData.defineId(EagleEntity.class, EntityDataSerializers.INT);

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT_ID, EagleEntity.Variant.BALD.id);
        builder.define(HEALTH_WHEN_START_RIDING, 0f);
        builder.define(FOLLOWER_STATE, 0);
        builder.define(DATA_COLLAR_COLOR, DyeColor.RED.getId());
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

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnType, @javax.annotation.Nullable SpawnGroupData spawnGroupData) {
        setVariantFromBiome(this, level.getBiome(this.blockPosition()));
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    public void onAddedToLevel() {
        super.onAddedToLevel();
        if(this.isOrderedToSit() && !this.isSitting()) this.setFollowerState(SITTING_STATE);
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

    //──────────────────────────────────── AI & Movement ────────────────────────────────────
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
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new EaglePathNavigation(this, level);
    }

    @Override
    public void setSpeed(float speed) {
        this.speed = speed;
        this.setZza(speed);
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
    protected void checkFallDamage(double y, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos) {
        //Only fall damage when baby
        if(this.isBaby()){
            super.checkFallDamage(y, onGround, state, pos);
        }
    }

    //──────────────────────────────────── Combat ────────────────────────────────────
    @Nullable
    @Override
    public LivingEntity getTarget() {
        return this.getTargetFromBrain();
    }

    @Override
    public boolean canAttack(@NotNull LivingEntity target) {
        Optional<List<UUID>> attackedList = this.getBrain().getMemory(Primal_MemoryModuleTypes.ATTACKED_LIST.get());

        Optional<UUID> lastUuid = attackedList.map(List::getLast);

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

    @Override
    public @NotNull AABB getHitbox() {
        return super.getHitbox().inflate(-0.2);
    }

    @Override
    public boolean isWithinMeleeAttackRange(LivingEntity entity) {
        return this.getAttackBoundingBox().inflate(0.5).intersects(entity.getHitbox());
    }

    //──────────────────────────────────── Feeding & Interaction ────────────────────────────────────
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

    @Override
    public @Nullable AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob otherParent) {
        if(otherParent instanceof EagleEntity otherParentCasted)
            return Primal_Util.createFromParents(Primal_Entities.EAGLE.get(),
                    this,
                    otherParentCasted, c-> EagleAi.initMemories(c, level.getRandom()));

        return null;
    }

    @Override
    public void spawnChildFromBreeding(@NotNull ServerLevel level, @NotNull Animal mate) {
        this.finalizeSpawnChildFromBreeding(level, mate, null);
        this.getBrain().setMemory(MemoryModuleType.IS_PREGNANT, Unit.INSTANCE);
    }

    //──────────────────────────────────── Sounds ────────────────────────────────────
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

    public @Nullable SoundEvent getShriekSound() {
        if(this.getVariant()==Variant.BALD && this.random.nextIntBetweenInclusive(0, 500)==0){
            return Primal_Sounds.EAGLE_FREEDOM.get();
        }

        return Primal_Sounds.EAGLE_SHRIEK.get();
    }

    //──────────────────────────────────── Visuals ────────────────────────────────────
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState sittingAnimationState = new AnimationState();

    public final AnimationState flyAnimationState = new AnimationState();

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            setupAnimationStates();
        }
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

    private void setupAnimationStates() {
        this.sittingAnimationState.animateWhen((this.isInSittingPose() || isBabyOnNest(this)) && (this.onGround()), this.tickCount);
        this.idleAnimationState.animateWhen((!this.isFlying() || this.isBaby()) && !this.sittingAnimationState.isStarted(), this.tickCount);
        this.flyAnimationState.animateWhen(this.isFlying(), this.tickCount);
    }

    private static boolean isBabyOnNest(EagleEntity eagle){
        return eagle.isBaby() &&
                !eagle.walkAnimation.isMoving() &&
                eagle.level().getBlockState(eagle.getOnPos()).is(Primal_Blocks.NEST_BLOCK.get())
                && !eagle.level().getBlockState(eagle.getOnPos()).getValue(NestBlock.HAS_EGG);
    }

    //──────────────────────────────────── Misc ────────────────────────────────────
    @Override
    public int getMaxHeadXRot() {
        return this.onGround()? 40: 90;
    }

    @Override
    public int getMaxHeadYRot() {
        return this.onGround()?  15: 1;
    }

    @Override
    public boolean isFlying() {
        return !onGround();
    }

    @Override
    protected boolean canFlyToOwner() {
        return !this.isBaby();
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
    public boolean showVehicleHealth() {
        return false;
    }

    @Override
    public @Nullable LivingEntity getControllingPassenger() {
        return null;
    }

    @Override
    protected @NotNull Vec3 getLeashOffset() {
        return new Vec3(0.0, this.getEyeHeight()*0.7f, this.getBbWidth() * 0.2F);
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
    protected void teleportToAroundBlockPos(@NotNull BlockPos pos) {
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
    protected boolean canTeleportTo(@NotNull BlockPos pos) {
        BlockPos blockpos = pos.subtract(this.blockPosition());
        return this.level().noCollision(this, this.getBoundingBox().move(blockpos));
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
}
