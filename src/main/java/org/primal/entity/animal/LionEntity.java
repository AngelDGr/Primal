package org.primal.entity.animal;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.entity.ai.LionAi;
import org.primal.registry.*;
import org.primal.util.Primal_Util;
import org.primal.util.mob_types.*;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.function.IntFunction;

//xTODO
// Add sounds
//The lion sleeps tonight in Savannas and Savanna Plateaus.
//They spawn very rarely in these biomes.
//They always spawn in packs of 2–4.
//Small chance of one being maned.
//It will scare off creepers and phantoms.
//When harmed, the pride becomes hostile.
// When hunting, lions start off stalking, then pouncing and chasing prey until killed.
// Lions will hunt down prey.
// When hostile, its eyes change, showing its hostility.
// Maned Lions will chill around and lay down way more often than the maneless lion.
// Maned lions roar, signaling the maneless lions to attack.
// Lions attack in two ways:
//  - A simple pounce that deals 4 damage (2 hearts).
//  - A rarer pounce that grabs you.
// When grabbed, you take 2 damage (1 heart) for 6 seconds (enough to kill a cow).
// It deals over 4 damage (2 hearts).
// Has over 40 health (20 hearts) by default.
// It can run faster than wolves and bears.
// It regains 8 health (4 hearts) after killing a mob.
// They're very territorial and will become hostile if you stay too close for a long period.
// When holding meat while shifting, it won't attack for a period of time and will approach the target.
// It can be tamed with any type of meat.
// It becomes aggressive when meat is held for too long.
// Upon feeding them, it will reach a threshold where it becomes neutral and can now be tamed.
// Babies wouldn't be able to attack you, so it is easier to tame a baby than an adult.
// It displays an accessory and changes when tamed.
// Tribe marks are displayed when tamed
// Tribe marks are dyeable.
// You can remove the marks by using a water bucket on the lion.
// When tamed, it functions similar to wolves.
// It will always grab its target when tamed.
// Healing is indicated by several small heart particles rising up.
// Like cats, they can gift you with an item like: Leather, Raw Meat (any), Spider Eye, Bone, Gunpowder, Phantom Membrane
// It retained some behavior traits from their cute relatives; Cats.
// It will lay rest on you when sleeping on the bed.
// It will randomly lay on top of chests when it's not set to sleep.

public class LionEntity extends TamableAnimal implements VariantHolder<LionEntity.Variant>, MobWithTransitionablePoseAnimations, NeutralMob, AnimalRoars, DetectsFartherAway, IsPackAnimal<LionEntity>, HostileMount, PrimalTamable, AttackVillagers {

    //──────────────────────────────────── Variants ────────────────────────────────────
    public enum Variant implements StringRepresentable {
        GOLDEN(0, "golden"),
        CARAMEL(1, "caramel"),
        CAVE(2, "cave");

        public static final Codec<LionEntity.Variant> CODEC = StringRepresentable.fromEnum(LionEntity.Variant::values);

        private static final IntFunction<LionEntity.Variant> BY_ID = ByIdMap.continuous(LionEntity.Variant::getId, values(),
                ByIdMap.OutOfBoundsStrategy.CLAMP);

        public static LionEntity.Variant byId(int id) {
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
    public void setVariant(@NotNull LionEntity.Variant variant) {
        this.entityData.set(DATA_VARIANT_ID, variant.id);
    }

    @Override
    public @NotNull LionEntity.Variant getVariant() {
        return LionEntity.Variant.byId(this.entityData.get(DATA_VARIANT_ID));
    }

    public void setVariantFromBiome(LionEntity animal, Holder<Biome> holder) {
        if (holder.is(Primal_Tags.Biome.SPAWNS_CAVE_LION) || holder.value().coldEnoughToSnow(this.getOnPos())) {
            animal.setVariant(LionEntity.Variant.CAVE);
        } else if (holder.is(Primal_Tags.Biome.SPAWNS_CARAMEL_LION)) {
            animal.setVariant(LionEntity.Variant.CARAMEL);
        } else {
            animal.setVariant(LionEntity.Variant.GOLDEN);
        }
    }

    @Override
    protected @NotNull Component getTypeName() {
        return Primal_Util.getCustomVariantName(this, super.getTypeName(), Variant.CAVE);
    }

    public boolean isSigma(){
        return this.hasCustomName() && this.getName().getString().toLowerCase(Locale.ROOT).contains("the lion");
    }

    //──────────────────────────────────── Init ────────────────────────────────────
    public LionEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
        this.getNavigation().setCanFloat(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40f)
                .add(Attributes.MOVEMENT_SPEED, 0.32f)
                .add(Attributes.ATTACK_DAMAGE, 4f)
                .add(Attributes.ATTACK_KNOCKBACK, 1.2f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.4f)
                .add(Attributes.STEP_HEIGHT, 1.5f);
    }

    public static final EntityDataAccessor<Long> LAST_POSE_CHANGE_TICK_LAYING = SynchedEntityData.defineId(LionEntity.class, EntityDataSerializers.LONG);
    private static final EntityDataAccessor<Integer> DATA_VARIANT_ID = SynchedEntityData.defineId(LionEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> MANELESS = SynchedEntityData.defineId(LionEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ANGRY = SynchedEntityData.defineId(LionEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_LEADER = SynchedEntityData.defineId(LionEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> MAULING = SynchedEntityData.defineId(LionEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Float> HEALTH_WHEN_START_RIDING = SynchedEntityData.defineId(LionEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> FOLLOWER_STATE = SynchedEntityData.defineId(LionEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_COLLAR_COLOR = SynchedEntityData.defineId(LionEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> HAS_COLLAR = SynchedEntityData.defineId(LionEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_LAYING = SynchedEntityData.defineId(LionEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> MEAT_EATEN = SynchedEntityData.defineId(LionEntity.class, EntityDataSerializers.INT);

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(LAST_POSE_CHANGE_TICK_LAYING, 0L);
        builder.define(DATA_VARIANT_ID, LionEntity.Variant.GOLDEN.id);
        builder.define(MANELESS, false);
        builder.define(ANGRY, false);
        builder.define(IS_LEADER, false);
        builder.define(MAULING, false);
        builder.define(HEALTH_WHEN_START_RIDING, 0f);
        builder.define(FOLLOWER_STATE, 0);
        builder.define(DATA_COLLAR_COLOR, DyeColor.RED.getId());
        builder.define(HAS_COLLAR, true);
        builder.define(IS_LAYING, false);
        builder.define(MEAT_EATEN, 0);
    }

    public void setManeless(boolean isManeless) {
        this.entityData.set(MANELESS, isManeless);
    }

    public boolean isManeless() {
        return this.entityData.get(MANELESS);
    }

    public void setAngry(boolean isAngry) {
        this.entityData.set(ANGRY, isAngry);
    }

    public boolean isAngry() {
        return this.entityData.get(ANGRY);
    }

    @Override
    public void setLeader(boolean isLeader) {
        this.entityData.set(IS_LEADER, isLeader);
    }

    @Override
    public boolean isLeader() {
        return this.entityData.get(IS_LEADER);
    }

    public boolean isMauling() {
        return this.entityData.get(MAULING);
    }

    public void setMauling(boolean mauling) {
        this.entityData.set(MAULING, mauling);
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

    public int getFollowerState() {
        return this.entityData.get(FOLLOWER_STATE);
    }

    public void setFollowerState(int state) {
        this.entityData.set(FOLLOWER_STATE, state);
    }

    @Override
    public boolean canLayOnBed() {
        return true;
    }

    @Override
    public boolean isLaying() {
        return this.entityData.get(IS_LAYING);
    }

    @Override
    public void setIsLaying(boolean isLaying) {
        this.entityData.set(IS_LAYING, isLaying);
    }

    public void setMeatEaten(int meatEaten){
        this.entityData.set(MEAT_EATEN, meatEaten);
    }

    public int getMeatEaten() {
        return this.entityData.get(MEAT_EATEN);
    }

    public void addMeatEaten(int meatEaten){
        this.setMeatEaten(Math.clamp(this.getMeatEaten()+meatEaten, 0, Integer.MAX_VALUE));
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        LionAi.initMemories(this, this.level().random);
        //Maneless with 50%
        if(spawnType.equals(MobSpawnType.SPAWN_EGG) || spawnType.equals(MobSpawnType.BREEDING))
            this.setManeless(level.getRandom().nextBoolean());
        else {
            if (spawnGroupData == null) {
                spawnGroupData = new AgeableMob.AgeableMobGroupData(false);
            }

            boolean isManed = level.getRandom().nextFloat() < getManedChance((AgeableMobGroupData) spawnGroupData);
            this.setManeless(!isManed);

            boolean isBaby = level.getRandom().nextFloat() < getBabyChance((AgeableMobGroupData) spawnGroupData);
            this.setBaby(isBaby);
        }

        setVariantFromBiome(this, level.getBiome(this.blockPosition()));
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    private static float getManedChance(AgeableMobGroupData spawnGroupData) {
        int groupSize = spawnGroupData.getGroupSize();

        // Base chance for small groups
        float manedChance = 0.15f; // 15% for solo / pair

        // Increase chance with group size (+15% per extra lion)
        manedChance += Math.max(0, groupSize - 1) * 0.15f;

        // Cap the chance so it never becomes guaranteed
        manedChance = Math.min(manedChance, 0.75f); // max 75%
        return manedChance;
    }

    private static float getBabyChance(AgeableMobGroupData data) {
        int groupSize = data.getGroupSize();

        // Base baby chance
        float babyChance = 0.10f; // 10% solo

        // Packs are more likely to have cubs
        babyChance += Math.max(0, groupSize - 2) * 0.10f;

        // Cap so it never dominates the spawn
        return Math.min(babyChance, 0.40f); // max 40%
    }

    @Override
    public void onAddedToLevel() {
        super.onAddedToLevel();
        if(this.isOrderedToSit() && !this.isSitting()) this.setFollowerState(SITTING_STATE);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.addAdditionalSaveDataTransitionablePoseAnimations(compound);
        compound.putInt("Variant", this.getVariant().id);
        compound.putBoolean("IsManeless", this.isManeless());
        compound.putInt("MeatEaten", this.getMeatEaten());
        this.addAdditionalSaveDataHostileMount(compound);
        this.addAdditionalSaveDataTamable(compound);
        this.addAdditionalDataPackAnimal(compound);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.readAdditionalSaveDataTransitionablePoseAnimations(compound);
        this.setVariant(LionEntity.Variant.byId(compound.getInt("Variant")));
        this.setManeless(compound.getBoolean("IsManeless"));
        this.setMeatEaten(compound.getInt("MeatEaten"));
        this.readAdditionalSaveDataHostileMount(compound);
        this.readAdditionalSaveDataTamable(compound);
        this.readAdditionalDataPackAnimal(compound);
    }

    @Override
    public @NotNull EntityDimensions getDefaultDimensions(@NotNull Pose pose) {
        return pose.equals(Pose.SITTING) || pose.equals(Pose.SLIDING)? EntityDimensions.scalable(1.5f, 1.0f).withEyeHeight(0.8f).scale(getAgeScale()): super.getDefaultDimensions(pose);
    }

    //──────────────────────────────────── AI & Movement ────────────────────────────────────
    @Override
    protected void registerGoals() {}

    @Override
    protected @NotNull Brain.Provider<LionEntity> brainProvider() {
        return LionAi.brainProvider();
    }

    @Override
    protected @NotNull Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
        return LionAi.makeBrain(this.brainProvider().makeBrain(dynamic));
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull Brain<LionEntity> getBrain() {
        return (Brain<LionEntity>) super.getBrain();
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();

        //Starts the brain
        Brain<LionEntity> brain = this.getBrain();
        brain.tick((ServerLevel) this.level(), this);
        LionAi.updateActivity(this);

        //For the angry eyes
        this.setAngry(brain.getMemory(MemoryModuleType.ATTACK_TARGET).isPresent() || brain.getMemory(Primal_MemoryModuleTypes.IS_GRABBING.get()).isPresent());

        //Makes it sprint when attacking or running on land
        if(!this.isInWater())
            this.setSprinting(brain.getMemory(MemoryModuleType.ATTACK_TARGET).isPresent() || brain.getMemory(MemoryModuleType.AVOID_TARGET).isPresent());
        if(this.isSprinting() && this.isInWater())
            this.setSprinting(false);

        this.sprintWhenFollowing(6, 3, 0.087);

        var mauling= this.getBrain().getMemory(Primal_MemoryModuleTypes.IS_GRABBING.get());
        //Fallback to reset, just in case
        if(mauling.isPresent() && mauling.get() && this.getPassengers().isEmpty()){
            this.getBrain().eraseMemory(Primal_MemoryModuleTypes.IS_GRABBING.get());
            this.setPose(Pose.STANDING);
            if(this.isMauling()) this.setMauling(false);
        }

        if(this.isStalking())
            this.setPose(Pose.CROUCHING);

        boolean aboveImportant =
                this.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get()).isPresent()
                && getOnPos().equals(this.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get()).get());

        //LayOnBed
        if(this.isLaying() && (this.level().getBlockState(this.getOnPos()).is(BlockTags.BEDS) || aboveImportant) && !Primal_Util.Ai.isMovingOrNoInGround(this)){
            this.setPose(Pose.SLIDING);
        } else if(this.hasPose(Pose.SLIDING)) {
            this.setPose(Pose.STANDING);
        }

        this.assignPackLeaderAndHome(LionEntity.class);

        //Reduces each 3s
        if(this.tickCount%60==0) this.addMeatEaten(-1);
    }

    @Override
    public float getSpeed() {
        return this.isCrouching()? super.getSpeed()*0.3f : this.isSprinting()? super.getSpeed()*1.3f : super.getSpeed();
    }

    public boolean refuseToMove() {
        return hasPose(Pose.SITTING) || hasPose(Pose.SLIDING) || this.isOnPoseTransition();
    }

    //──────────────────────────────────── Combat ────────────────────────────────────
    @Nullable
    @Override
    public LivingEntity getTarget() {
        return this.getTargetFromBrain();
    }

    public static int TERRITORIAL_DISTANCE = 8;
    @Override
    public boolean canAttack(@NotNull LivingEntity target) {
        //To not attack owner
        if(this.getOwner()!=null && this.getOwner().equals(target))
            return false;

        if((this.getBrain().isMemoryValue(MemoryModuleType.ATTACK_TARGET, target) || this.getBrain().isMemoryValue(Primal_MemoryModuleTypes.LAST_ATTACK_TARGET.get(), target)) && super.canAttack(target))
            return true;

        //Pass to the sensor to handle tamed logic
        if(this.isTame())
            return super.canAttack(target) && Primal_Util.isNotNeverAttack(target, Primal_Tags.Entity.LION_NEVER_ATTACK);

        var nearestAttackableCautious = this.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_ATTACKABLE_CAUTIOUS.get());

        return Primal_Util.isNotNeverAttack(target, Primal_Tags.Entity.LION_NEVER_ATTACK)
                //Hunts regularly
                && (target.getType().is(Primal_Tags.Entity.LION_HUNTABLE) && !this.getBrain().hasMemoryValue(MemoryModuleType.HAS_HUNTING_COOLDOWN)
                    //It is the nearest cautious attackable
                    || (nearestAttackableCautious.isPresent() && nearestAttackableCautious.get().equals(target)))
                && !this.isPacified()
                && super.canAttack(target);
    }

    public boolean canBeCautious(@NotNull LivingEntity target){
        return Primal_Util.isNotNeverAttack(target, Primal_Tags.Entity.LION_NEVER_ATTACK)
                && !this.isPacified()
                && this.distanceTo(target) < LionEntity.TERRITORIAL_DISTANCE
                && !(target instanceof LionEntity)
                //Not be cautious of owner
                && !(this.getOwner()!=null && this.getOwner().equals(target))
                //Not be cautious if tamed
                && !this.isTame()
                && super.canAttack(target);
    }

    public boolean isStalking(){
        return this.getBrain().getMemory(Primal_MemoryModuleTypes.STALK_TARGET.get()).isPresent() && this.getBrain().isActive(Primal_Activities.STALK.get());
    }

    public boolean isPacified(){
        return this.getBrain().hasMemoryValue(MemoryModuleType.PACIFIED);
    }

    public static boolean canPickUpEntity(@NotNull Entity target, @NotNull LionEntity lion){
        return target.getBoundingBox().getSize()<lion.getBoundingBox().inflate(-0.22f).getSize()
                && !lion.getBrain().hasMemoryValue(Primal_MemoryModuleTypes.IS_STUNNED.get()) && lion.level().getRandom().nextBoolean()
                && !lion.isBaby();
    }

    @Override
    public boolean canBabyAttack(@NotNull Entity target) {
        return target.getBoundingBox().getSize()<this.getBoundingBox().getSize()*1.3 && this.isBaby();
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        boolean hurt = super.hurt(source, amount);

        if(hurt){
            this.getBrain().setMemoryWithExpiry(Primal_MemoryModuleTypes.WAS_IDLE_ANIMATION.get(), true, 40);

            this.hurtAndReleasePassenger(2.0f, 80);

            if (source.getEntity() instanceof LivingEntity target && !(target instanceof Player player && player.isCreative()))
                Primal_Util.Ai.wasHurtByAndAttacks(this, target, LionEntity.class, true, true, !canBabyAttack(target));
        }

        return hurt;
    }

    @Override
    public boolean killedEntity(@NotNull ServerLevel level, @NotNull LivingEntity killed) {
        //Put the cooldown to attack prey each 30s
        if(killed.getType().is(Primal_Tags.Entity.LION_HUNTABLE) && !this.isBaby() && this.getRandom().nextBoolean())
            this.getBrain().setMemoryWithExpiry(MemoryModuleType.HAS_HUNTING_COOLDOWN, true, Primal_Util.toTicks(30));

        if(this.getHealth()<this.getMaxHealth()) this.heal(8);

        return super.killedEntity(level, killed);
    }

    @Override
    protected void positionRider(@NotNull Entity passenger, @NotNull MoveFunction callback) {
        super.positionRider(passenger, callback);

        if(this.getFirstPassenger() instanceof LivingEntity rider){
            // Direction from rider to this entity
            double dx = this.getX() - rider.getX();
            double dz = this.getZ() - rider.getZ();

            float yaw = (float)(Math.atan2(dz, dx) * (180F / Math.PI)) - 90F;

            rider.yRotO = yaw;
            rider.yBodyRotO = yaw;
            rider.yHeadRotO = yaw;
            rider.setYRot(yaw);
            rider.setYBodyRot(yaw);
            rider.setYHeadRot(yaw);
        }
    }

    @Override
    protected @NotNull Vec3 getPassengerAttachmentPoint(@NotNull Entity entity, @NotNull EntityDimensions dimensions, float partialTick) {
        double preyYSize= entity.getBoundingBox().getYsize();

        var passengerAttachment = entity.getVehicleAttachmentPoint(this);

        double finalHeight = (entity.getEyeHeight()*0.7)- passengerAttachment.y;

        return new Vec3(0.0, -finalHeight, preyYSize*1.1)
                .yRot(-this.getYRot() * Mth.DEG_TO_RAD);
    }

    @Override
    public @NotNull Vec3 getDismountLocationForPassenger(@NotNull LivingEntity passenger) {
        return new Vec3(this.getX(), this.getY()+0.1, this.getZ());
    }

    public boolean isWithinLungeAttackRange(LivingEntity entity) {
        return this.getAttackBoundingBox().inflate(2).intersects(entity.getHitbox());
    }

    public boolean isSeeingTarget(LivingEntity target) {
        Vec3 vec3d = target.position();

        Vec3 vec3d2 = this.calculateViewVector(0.0f, this.getYHeadRot());
        Vec3 vec3d3 = vec3d.vectorTo(this.position());
        vec3d3 = new Vec3(vec3d3.x, 0.0, vec3d3.z).normalize();

        return vec3d3.dot(vec3d2) < -0.85;
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

    public int TAMING_THRESHOLD_ADULT=32;
    public int TAMING_THRESHOLD_BABY=8;

    public boolean handleEating(@NotNull Player player, @NotNull ItemStack stack) {
        if (this.isFood(stack)) {

            //To try to tame it
            if (!this.isTame() && isTameFood(stack) && !this.level().isClientSide) {

                this.addMeatEaten(1);
                //Pacified by 20s after meeting the threshold
                if(!this.isBaby() && getMeatEaten()>TAMING_THRESHOLD_ADULT && this.getBrain().getMemory(MemoryModuleType.PACIFIED).isEmpty()){
                    this.getBrain().setMemoryWithExpiry(MemoryModuleType.PACIFIED, true, 400);
                    this.level().broadcastEntityEvent(this, (byte)14);
                }

                //25% probability as baby, 10% probability as adult
                if (this.isBaby() && getMeatEaten()>TAMING_THRESHOLD_BABY) {
                    if(this.random.nextIntBetweenInclusive(0, 4)==0){
                        this.tame(player);
                        this.navigation.stop();
                        this.setTarget(null);
                        this.level().broadcastEntityEvent(this, (byte) 7);
                    } else
                        this.level().broadcastEntityEvent(this, (byte) 6);
                } else if(getMeatEaten()>TAMING_THRESHOLD_ADULT){
                    if(this.random.nextIntBetweenInclusive(0, 10)==0){
                        this.tame(player);
                        this.navigation.stop();
                        this.setTarget(null);
                        this.level().broadcastEntityEvent(this, (byte) 7);
                    } else
                        this.level().broadcastEntityEvent(this, (byte) 6);
                } else {
                    this.level().broadcastEntityEvent(this, (byte) 8);
                }



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
                //To heal the lion
                if(isHealFood(stack)){
                    if (this.getHealth() < this.getMaxHealth()) {
                        FoodProperties foodProperties = stack.getFoodProperties(this);
                        float nutrition=0;
                        if(foodProperties!=null){
                            nutrition = foodProperties.nutrition()/2f;
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
        return isMatingFood(stack);
    }

    public static boolean isHealFood(@NotNull ItemStack stack){
        return isMatingFood(stack);
    }

    public static boolean isMatingFood(@NotNull ItemStack stack){
        return stack.is(Primal_Tags.Item.LION_BREED_FOOD);
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
    public @Nullable AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob otherParent) {
        if(otherParent instanceof LionEntity otherParentCasted)
            return Primal_Util.createFromParents(Primal_Entities.LION.get(),
                    this,
                    otherParentCasted, c-> LionAi.initMemories(c, level.getRandom()));

        return null;
    }

    @Override
    public void heal(float healAmount) {
        super.heal(healAmount);
        this.level().broadcastEntityEvent(this, (byte) 22);
    }

    //──────────────────────────────────── Visuals ────────────────────────────────────
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState roarAnimationState = new AnimationState();
    public final AnimationState maulAnimationState = new AnimationState();
    public final AnimationState pounceAnimationState = new AnimationState();

    public final AnimationState startLayingAnimationState = new AnimationState();
    public final AnimationState layingAnimationState = new AnimationState();
    public final AnimationState stopLayingAnimationState = new AnimationState();

    public static final byte POUNCE=4;
    private int timeAttacking = 0;
    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            setupAnimationStates();
        }

        if(this.level().isClientSide()) return;

        if(this.isPouncing()){
            timeAttacking++;
            if(timeAttacking >=10 || this.isMauling()){
                timeAttacking=0;
                this.setPose(Pose.STANDING);
            }
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 22) {
            Primal_Util.Visuals.addParticleAboveSelf(this, ParticleTypes.HEART, 1);
        } else if (id == 8) {
            Primal_Util.Visuals.addParticleAboveSelf(this, ParticleTypes.HAPPY_VILLAGER, 2);
        } else if (id == 13) {
            Primal_Util.Visuals.addParticlesAroundSelf(this, ParticleTypes.ANGRY_VILLAGER, 5);
        } else if (id == 14) {
            Primal_Util.Visuals.addParticlesAroundSelf(this, ParticleTypes.HAPPY_VILLAGER, 5);
        }  else if (id == POUNCE) {
            this.pounceAnimationState.start(this.tickCount);
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> key) {
        if (DATA_POSE.equals(key)) {
            if (this.getPose().equals(Pose.ROARING)) {
                this.roarAnimationState.startIfStopped(this.tickCount);
            } else {
                this.roarAnimationState.stop();
            }
        }

        super.onSyncedDataUpdated(key);
    }

    @Override
    public Map<String, TransitionablePoseAnimation> transitionableAnimations() {
        return Map.of(
                "Laying",
                new MobWithTransitionablePoseAnimations.TransitionablePoseAnimation(this.isLayingPose(),
                        Pose.SITTING, LAST_POSE_CHANGE_TICK_LAYING,
                        startLayingAnimationState, layingAnimationState, stopLayingAnimationState, 20));
    }

    private void setupAnimationStates() {
        this.transitionablePoseAnimationsSetupAnimationStates();
        this.maulAnimationState.animateWhen(this.isMauling(), this.tickCount);
        this.pounceAnimationState.animateWhen(this.isPouncing(), this.tickCount);
        this.idleAnimationState.animateWhen(!isOnPoseAnimation() && !isOnPoseTransition() && !this.isPouncing() && !this.roarAnimationState.isStarted(), this.tickCount);
    }

    public boolean isLayingPose(){
        return this.hasPose(Pose.SITTING);
    }

    private boolean isPouncing() {
        return this.hasPose(Pose.SPIN_ATTACK);
    }
    //──────────────────────────────────── Misc ────────────────────────────────────
    public static boolean checkLionSpawnRules(
            EntityType<? extends Animal> animal, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random
    ) {
        return level.getBlockState(pos.below()).is(Primal_Tags.Block.LION_SPAWN_ON) && isBrightEnoughToSpawn(level, pos);
    }

    @Override
    public float getAgeScale() {
        return this.isBaby() ? 0.3F : 1.0F;
    }

    @Override
    public boolean canBeLeashed() {
        return super.canBeLeashed() && !hasPose(Pose.SITTING) && !hasPose(Pose.SLIDING);
    }

    @Override
    protected @NotNull Vec3 getLeashOffset() {
        return new Vec3(0.0, this.getEyeHeight()*0.6f, this.getBbWidth() * 0.2F);
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
    public boolean canBeLeader() {
        return !this.isManeless() && !this.isBaby();
    }

    @Override
    public boolean showVehicleHealth() {
        return false;
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
    public boolean shouldTryTeleportToOwner() {
        LivingEntity livingentity = this.getOwner();
        return livingentity != null && this.distanceToSqr(this.getOwner()) >= (25*25);
    }

    @Override
    public boolean isTargetingVillager() {
        return this.getTarget() instanceof Villager
                //Last Target
                || this.getLastHurtMob() instanceof Villager
                //Pickup target
                || this.getFirstPassenger() instanceof Villager
                //Roared target
                || (this.getBrain().getMemory(MemoryModuleType.ROAR_TARGET).isPresent() && this.getBrain().getMemory(MemoryModuleType.ROAR_TARGET).get() instanceof Villager);
    }

    //──────────────────────────────────── Sounds ────────────────────────────────────
    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return this.getBrain().isActive(Activity.ROAR)? null: this.hasPose(Pose.SITTING) || this.hasPose(Pose.SLIDING) || this.isLaying()? Primal_Sounds.LION_SNORING.get(): this.isAggressive()? Primal_Sounds.LION_IDLE_ANGRY.get() : Primal_Sounds.LION_IDLE.get();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return Primal_Sounds.LION_HURT.get();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return Primal_Sounds.LION_DEATH.get();
    }

    public SoundEvent getEatingSound() {
        return Primal_Sounds.LION_EAT.get();
    }

    public boolean playEatingSound(){
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
    public @Nullable SoundEvent getRoarSound() {
        return Primal_Sounds.LION_ROAR.get();
    }

    public @Nullable SoundEvent getLungeSound() {
        return null;
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