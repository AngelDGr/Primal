package org.primal.entity.animal;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.Brain.Provider;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import org.primal.client.animation.entity.BearAnimations;
import org.primal.entity.ai.BearAi;
import org.primal.entity.ai.controls.look.WaterOrLandLookControl;
import org.primal.entity.ai.controls.move.WaterOrLandMoveControl;
import org.primal.registry.*;
import org.primal.util.Primal_Util;
import org.primal.util.mob_types.AnimalRoars;
import org.primal.util.mob_types.AttackVillagers;
import org.primal.util.mob_types.PrimalTamable;
import org.primal.util.mob_types.SemiAquaticAnimal;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.IntFunction;
import java.util.function.Predicate;

//xTODO
// x Add Grolar implementation, breeding with a polar bear
// X Make it eat honey and sweet berries
// x Added being able to tame it with 10-20 Honeycombs
// x Heal with sweet berries
// x Breed with salmon in a bucket
// x Make it scared from campfires
// x Roars before combat
// x Make it slower the less health it has, until collapses (Can automatically heal with sweet berries on the barrels)
// x Equipable with barrels
// x Attack on wide area, and slow
// x Add Bear Follower states
// x Add Bear Jockeys
// x Made them only attack when they have a baby near
// x Have colored collars
// x Make it untargetable if faint
// x Fix the look
public class BearEntity extends TamableAnimal implements VariantHolder<BearEntity.Variant>, GeoEntity, ContainerListener, HasCustomInventoryScreen, OwnableEntity, NeutralMob, AnimalRoars, PrimalTamable, AttackVillagers, SemiAquaticAnimal {

    //──────────────────────────────────── Variants ────────────────────────────────────
    public enum Variant implements StringRepresentable {
        GRIZZLY(0, "grizzly"),
        WARM(1, "warm"),
        GROLAR(2, "grolar");

        public static final Codec<BearEntity.Variant> CODEC = StringRepresentable.fromEnum(Variant::values);

        private static final IntFunction<Variant> BY_ID = ByIdMap.continuous(Variant::getId, values(),
                ByIdMap.OutOfBoundsStrategy.CLAMP);

        public static Variant byId(int id) {
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
    public void setVariant(Variant variant) {
        this.entityData.set(DATA_VARIANT_ID, variant.id);
    }

    @Override
    public @NotNull Variant getVariant() {
        return Variant.byId(this.entityData.get(DATA_VARIANT_ID));
    }

    public void setVariantFromBiome(BearEntity animal, Holder<Biome> holder) {
        if (holder.is(Primal_Tags.Biome.SPAWNS_BLACK_BEAR))
            animal.setVariant(Variant.WARM);
        else
            animal.setVariant(Variant.GRIZZLY);
    }

    @Override
    protected @NotNull Component getTypeName() {
        return Primal_Util.getCustomVariantName(this, super.getTypeName(), Variant.GROLAR);
    }

    //──────────────────────────────────── Init ────────────────────────────────────
    public BearEntity(EntityType<BearEntity> entityType, Level level) {
        super(entityType, level);
        this.getNavigation().setCanFloat(true);
        this.moveControl = new WaterOrLandMoveControl<>(this, 85, 50, 0.15f, 0.01f, false, Predicate.not(BearEntity::isBearSleeping));
        this.lookControl = new WaterOrLandLookControl<>(this, 10, Predicate.not(BearEntity::isBearSleeping));
        this.setMaxUpStep(1.5f);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 50f)
                .add(Attributes.MOVEMENT_SPEED, 0.14f)
                .add(Attributes.ATTACK_DAMAGE, 8f)
                .add(Attributes.ATTACK_KNOCKBACK, 1.8f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6f);
    }

    protected SimpleContainer inventory=new SimpleContainer(27);
    @Override
    public void containerChanged(@NotNull Container container) {
    }

    private static final EntityDataAccessor<Boolean> BEAR_SLEEPING = SynchedEntityData.defineId(BearEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_VARIANT_ID = SynchedEntityData.defineId(BearEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_HONEY_COUNTER = SynchedEntityData.defineId(BearEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> AWAKE_COUNTER = SynchedEntityData.defineId(BearEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> HEALING_COOLDOWN = SynchedEntityData.defineId(BearEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> HAS_CHEST = SynchedEntityData.defineId(BearEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> FOLLOWER_STATE = SynchedEntityData.defineId(BearEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_JOCKEY = SynchedEntityData.defineId(BearEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_COLLAR_COLOR = SynchedEntityData.defineId(BearEntity.class, EntityDataSerializers.INT);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(BEAR_SLEEPING, false);
        this.entityData.define(DATA_VARIANT_ID, Variant.GRIZZLY.id);
        this.entityData.define(DATA_HONEY_COUNTER, 0);
        this.entityData.define(AWAKE_COUNTER, 0);
        this.entityData.define(HEALING_COOLDOWN, 0);
        this.entityData.define(HAS_CHEST, false);
        this.entityData.define(IS_JOCKEY, false);
        this.entityData.define(FOLLOWER_STATE, 0);
        this.entityData.define(DATA_COLLAR_COLOR, DyeColor.RED.getId());
    }

    public boolean isBearSleeping() { return this.entityData.get(BEAR_SLEEPING);}

    public void setBearSleeping(boolean isSleeping) { this.entityData.set(BEAR_SLEEPING, isSleeping);}

    public int getHoneyCounter() { return this.entityData.get(DATA_HONEY_COUNTER);}

    public void setHoneyCounter(int value) { this.entityData.set(DATA_HONEY_COUNTER, value);}

    public int getAwakeCounter() { return this.entityData.get(AWAKE_COUNTER);}

    public void setAwakeCounter(int value) { this.entityData.set(AWAKE_COUNTER, value);}

    public int getHealingCooldown() { return this.entityData.get(HEALING_COOLDOWN);}

    public void setHealingCooldown(int value) { this.entityData.set(HEALING_COOLDOWN, value);}

    public boolean hasChest() {
        return this.entityData.get(HAS_CHEST);
    }

    public void setChest(boolean chested) {
        this.entityData.set(HAS_CHEST, chested);
    }

    public boolean isBearJockey() {
        return this.entityData.get(IS_JOCKEY);
    }

    public void setBearJockey(boolean isJockey) {
        this.entityData.set(IS_JOCKEY, isJockey);
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
    public @NotNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        if (spawnGroupData == null) {
            spawnGroupData = new AgeableMob.AgeableMobGroupData(false);
        }

        AgeableMob.AgeableMobGroupData ageableData = (AgeableMob.AgeableMobGroupData) spawnGroupData;

        // Alternate baby/adult based on group size, only if is more than 1
        if(ageableData.getGroupSize()>0) this.setBaby(ageableData.getGroupSize() % 2 != 0);

        this.setVariantFromBiome(this, level.getBiome(this.blockPosition()));
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData, compoundTag);
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        if(this.isOrderedToSit() && !this.isSitting()) this.setFollowerState(SITTING_STATE);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant().id);
        compound.putInt("HoneyCounter", this.getHoneyCounter());
        compound.putInt("AwakeCounter", this.getAwakeCounter());
        compound.putInt("HealingCooldown", this.getHealingCooldown());
        compound.putBoolean("IsSleeping", this.isBearSleeping());
        compound.putBoolean("HasChest", this.hasChest());
        if (this.hasChest()) {
            ListTag listtag = new ListTag();

            for (int i = 0; i < this.inventory.getContainerSize(); i++) {
                ItemStack itemstack = this.inventory.getItem(i);
                if (!itemstack.isEmpty()) {
                    CompoundTag compoundtag = new CompoundTag();
                    compoundtag.putByte("Slot", (byte)(i));
                    listtag.add(itemstack.save(compoundtag));
                }
            }

            compound.put("Items", listtag);
        }
        compound.putBoolean("BearJockey", this.isBearJockey());
        this.addAdditionalSaveDataTamable(compound);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(Variant.byId(compound.getInt("Variant")));
        this.setHoneyCounter(compound.getInt("HoneyCounter"));
        this.setAwakeCounter(compound.getInt("AwakeCounter"));
        this.setBearSleeping(compound.getBoolean("IsSleeping"));
        this.setHealingCooldown(compound.getInt("HealingCooldown"));
        this.setChest(compound.getBoolean("HasChest"));
        if (this.hasChest()) {
            ListTag listtag = compound.getList("Items", 10);

            for (int i = 0; i < listtag.size(); i++) {
                CompoundTag compoundtag = listtag.getCompound(i);
                int j = compoundtag.getByte("Slot") & 255;
                if (j < this.inventory.getContainerSize()) {
                    this.inventory.setItem(j, ItemStack.of(compoundtag));
                }
            }
        }
        this.setBearJockey(compound.getBoolean("BearJockey"));
        this.readAdditionalSaveDataTamable(compound);
    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose pose) {
        return pose.equals(Pose.CROAKING)? EntityDimensions.scalable(1.5f, 1.25f).scale(getScale()): super.getDimensions(pose);
    }

    @Override
    protected float getStandingEyeHeight(@NotNull Pose pose, @NotNull EntityDimensions entityDimensions) {
        return pose.equals(Pose.CROAKING)? 0.8f * getScale(): 1.25f * getScale();
    }

    //──────────────────────────────────── AI & Movement ────────────────────────────────────
    @Override
    protected void registerGoals() {}

    @Override
    protected @NotNull Provider<BearEntity> brainProvider() {
        return BearAi.brainProvider();
    }

    @Override
    protected @NotNull Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
        return BearAi.makeBrain(this.brainProvider().makeBrain(dynamic));
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull Brain<BearEntity> getBrain() {
        return (Brain<BearEntity>) super.getBrain();
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();

        Brain<BearEntity> brain = this.getBrain();
        brain.tick((ServerLevel) this.level(), this);

        //Sprint when attacking
        if(!this.isInWater() && (this.isAggressive()))
            this.setSprinting(brain.getMemory(MemoryModuleType.ATTACK_TARGET).isPresent());

        //Removes sprint if swimming
        if(this.isSprinting() && this.isInWater())
            this.setSprinting(false);

        this.sprintWhenFollowing(8, 3, 0.087);

        BearAi.updateActivity(this);

        //Heal automatically when has berries on its inventory
        if(this.getHealth()<this.getMaxHealth() && this.random.nextInt(0,100)<=20 && this.getHealingCooldown()==0){
            int indexOfHealItem=-1;

            for(int i=0; i< this.inventory.getContainerSize(); i++){
                if(isHealFood(this.inventory.getItem(i))){
                    indexOfHealItem=i;
                }
            }

            if(indexOfHealItem!=-1){
                FoodProperties foodProperties = this.inventory.getItem(indexOfHealItem).getFoodProperties(this);
                float nutrition=0;
                if(foodProperties!=null){
                    nutrition = foodProperties.getNutrition()/2f;
                }

                this.heal(1f + nutrition);

                this.inventory.setItem(indexOfHealItem, new ItemStack(this.inventory.getItem(indexOfHealItem).getItem(), this.inventory.getItem(indexOfHealItem).getCount()-1));
                this.playEatingSound();
                this.setHealingCooldown(50);
            }
        }

        applySpeedModifiers();

        //To always remain as a baby if is a jockey
        if(isBearJockey() && this.isBaby()){
            this.setAge(-24000);
        }
        //To remove the bear jockey value if the rider dies
        if(this.isBearJockey() && !this.isVehicle()){
            this.setBearJockey(false);
        }

        if(!this.bearCollapses() && !this.level().isNight() && !this.getBrain().isActive(Primal_Activities.SIT.get()) && this.isBearSleeping())
            this.setBearSleeping(false);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new AmphibiousPathNavigation(this, level){
            @Override
            public void setCanFloat(boolean canSwim) {
                this.nodeEvaluator.setCanFloat(canSwim);
            }
        };
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        if (this.isEffectiveAi() && this.isInWater() && !this.isBearSleeping()) {
            this.moveRelative(this.getSpeed(), travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
        }
        else super.travel(travelVector);
    }

    public boolean refuseToMove() {
        return ImmutableList.of(Pose.ROARING, Pose.SNIFFING, Pose.CROAKING).contains(this.getPose());
    }

    @Override
    public float getSpeed() {
        return this.isSprinting()? super.getSpeed()*1.3f : this.getPose().equals(Pose.ROARING)? 0: super.getSpeed() ;
    }

    public void stopMoving(){
        this.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        this.getNavigation().stop();
        this.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
    }

    public AttributeModifier createSpeedModifier(float amount){
        return new AttributeModifier(
                UUID.fromString("46362845-4d60-4b8b-a275-bd6d32f665a7"),
                "health_speed_modifier",
                Mth.clamp(amount, 0.0, 0.13),
                AttributeModifier.Operation.ADDITION);
    }

    public void applySpeedModifiers(){
        AttributeModifier modifier = createSpeedModifier((this.getHealth()/this.getMaxHealth())* 0.13f);

        final AttributeInstance entityAttributeInstance_speed = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if(entityAttributeInstance_speed!=null) {
            entityAttributeInstance_speed.removeModifier(modifier.getId());
            entityAttributeInstance_speed.addTransientModifier(modifier);
        }
    }

    public boolean bearCollapses(){
        return (this.getMaxHealth()*0.20f) > this.getHealth();
    }

    @Override
    public boolean canRoarAtEntity(LivingEntity target) {
        return !(this.isBaby() || this.bearCollapses())
                //To detect attackable
                && this.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE).isPresent() && this.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE).get()==target
                //To see if Bear can attack (If it is near a baby or is tamed it attacks normally, otherwise only attacks at less than <8 blocks without shifting)
                && ((this.getBrain().hasMemoryValue(Primal_MemoryModuleTypes.NEAREST_VISIBLE_BABY.get()))
                || (this.isTame())
                || (target.distanceTo(this)<8 && !target.isShiftKeyDown()))
                //To not double trigger the moments the enemy is dying and is attackable
                && target.getHealth()>0 && target.canBeSeenAsEnemy() && Primal_Util.isNotNeverAttack(target);
    }

    @Override
    public boolean hasCustomWaterRoar() {
        return true;
    }

    @Override
    public void positionRider(@NotNull Entity passenger, Entity.@NotNull MoveFunction moveFunction) {
        super.positionRider(passenger, moveFunction);
        float f = Mth.sin(this.yBodyRot * ((float)Math.PI / 180F));
        float f1 = Mth.cos(this.yBodyRot * ((float)Math.PI / 180F));

        moveFunction.accept(passenger, this.getX() + (double)(0.1F * f),
                this.getY(0.5D) + passenger.getMyRidingOffset() - 0.1D,
                this.getZ() - (double)(0.15F * f1));

        if (passenger instanceof LivingEntity) {
            ((LivingEntity)passenger).yBodyRot = this.yBodyRot;
        }
    }

    //──────────────────────────────────── Combat ────────────────────────────────────
    @Nullable
    @Override
    public LivingEntity getTarget() {
        return Primal_Util.OneTwentyEquivalent.getTargetFromBrain(this);
    }

    @Override
    public boolean canAttack(@NotNull LivingEntity target) {
        return super.canAttack(target);
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        boolean hurt = super.hurt(source, amount);
        if (this.level().isClientSide) {
            return false;
        } else {
            if (hurt && source.getEntity() instanceof LivingEntity target
                    //Not be angry with owner
                    && target!=this.getOwner()
                    //Not be angry other owned bear
                    && !(target instanceof BearEntity bear2 && bear2.getOwner()!=null && bear2.getOwner()==this.getOwner())) {
                BearAi.wasHurtBy(this, target);
            }

            return hurt;
        }
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entity) {
        boolean hurt = super.doHurtTarget(entity);

        List<Entity> attackableEntities =
                new ArrayList<>(this.level().getEntities(this, this.getBoundingBox().inflate(1))
                        .stream().filter(
                                //Is seeing the target
                                target -> Primal_Util.isSeeingTarget(target, this, -0.8f)
                                        //Is not owned bear
                                        && !(target instanceof TamableAnimal pet2 && pet2.getOwner()!=null && pet2.getOwner()==this.getOwner()))
                        .toList());

        // To not attack more than 5 entities at the same time
        if (attackableEntities.size() > 5) {
            attackableEntities = attackableEntities.subList(0, 5);
        }

        for(Entity otherEntity: attackableEntities) {
            doHurtOther(otherEntity);
        }

        return hurt;
    }

    public void doHurtOther(@NotNull Entity entity){
        float f = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE)*0.6f;
        float f1 = (float)this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        DamageSource damagesource = this.damageSources().mobAttack(this);
        if (this.level() instanceof ServerLevel && entity instanceof LivingEntity) {
            f += EnchantmentHelper.getDamageBonus(this.getMainHandItem(), ((LivingEntity)entity).getMobType());
            f1 += (float)EnchantmentHelper.getKnockbackBonus(this);
        }

        boolean flag = entity.hurt(damagesource, f);
        if (flag) {
            if (f1 > 0.0F && entity instanceof LivingEntity livingentity) {
                livingentity.knockback(
                        f1 * 0.2F,
                        Mth.sin(this.getYRot() * (float) (Math.PI / 180.0)),
                        -Mth.cos(this.getYRot() * (float) (Math.PI / 180.0))
                );
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.6, 1.0, 0.6));
            }

            if (this.level() instanceof ServerLevel) {
                this.doEnchantDamageEffects(this, entity);
            }
        }
    }

    //──────────────────────────────────── Feeding & Interaction ────────────────────────────────────
    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
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

        //To open the inventory if tamed
        if (this.isTame() && player.isSecondaryUseActive() && this.hasChest()) {
            this.openCustomInventoryScreen(player);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        //To equip barrel if tamed and not baby
        if (this.isTame() && !this.hasChest() && stackInHand.is(Items.BARREL) && !this.isBaby()) {
            this.setChest(true);
            this.playChestEquipsSound();
            Primal_Util.OneTwentyEquivalent.consumeStack(1, player, stackInHand);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        //Only if tame and if is the owner
        if (this.isTame() && this.isOwnedBy(player)) {

            //Remove chest with a shears if empty inventory
            if(!this.level().isClientSide && this.hasChest() && stackInHand.is(Tags.Items.SHEARS) && this.inventory.isEmpty()){
                this.setChest(false);
                Primal_Util.useShearsOnEntityAndDamage(this, player, stackInHand, hand);
                this.spawnAtLocation(Blocks.BARREL);
                return InteractionResult.sidedSuccess(this.level().isClientSide());
            }

            //Awake bear if it is sleeping by their own
            if(this.isBearSleeping() && !this.isSitting()) {
                //30s of delay + 1-10 extra seconds
                this.setAwakeCounter(600+this.getRandom().nextIntBetweenInclusive(20, 200));
                return InteractionResult.sidedSuccess(this.level().isClientSide());
            }
            //Make it change the follow state (Wandering/Follow/Sitting)
            else {
                return changeFollowState(player, hand);
            }
        }

        return InteractionResult.PASS;
    }

    private int tameAttempts = 0;
    public boolean handleEating(@NotNull Player player, @NotNull ItemStack stack) {
        if (this.isFood(stack)) {
            //To try to mate
            if(isMatingFood(stack) && !this.isBearSleeping() && !this.level().isClientSide){
                if (this.getAge() == 0 && this.canFallInLove()) {
                    this.setInLove(player);

                    return playEatingSound();
                }
            }

            //To try to tame it, unless it is sleeping
            if (!this.isTame() && isTameFood(stack) && (!this.isBearSleeping() || this.bearCollapses()) && !this.level().isClientSide) {
                tameAttempts++;

                boolean canTameNow = tameAttempts >= 10 && this.random.nextInt(21 - tameAttempts) == 0;

                if (canTameNow) {
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

            //To age up if baby, with sweet berries or salmon bucket
            if (this.isBaby() && !isTameFood(stack)) {
                int i = this.getAge();
                this.ageUp(getSpeedUpSecondsWhenFeeding(-i), true);

                return playEatingSound();
            }
        }

        return false;
    }

    @Override
    protected void usePlayerItem(@NotNull Player player, @NotNull InteractionHand hand, ItemStack stack) {
        if (stack.is(Items.SALMON_BUCKET)) player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.WATER_BUCKET)));
        else super.usePlayerItem(player, hand, stack);
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        //Tame/Heal/Mating
        return isTameFood(stack) || isHealFood(stack) || isMatingFood(stack);
    }

    public static boolean isTameFood(@NotNull ItemStack stack){
        return stack.is(Items.HONEYCOMB);
    }

    public static boolean isHealFood(@NotNull ItemStack stack){
        return stack.is(Primal_Tags.Item.BEAR_HEALING_TREATS);
    }

    public static boolean isMatingFood(@NotNull ItemStack stack){
        return stack.is(Items.SALMON_BUCKET);
    }

    @Override
    public boolean canMate(@NotNull Animal otherAnimal) {
        //Breed with other bear
        if (otherAnimal instanceof BearEntity bear && bear.canParent() && this.canParent()) {
            return true;
        }
        //Breed with polar bear only if Grizzly
        else return otherAnimal instanceof PolarBear polarBear && polarBear.canMate(this) && this.canParent() && this.getVariant() == Variant.GRIZZLY;
    }

    public boolean canParent() {
        return !this.isVehicle() && !this.isPassenger() && !this.isBaby() && this.isInLove();
    }

    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob otherParent) {
        return createFromParents(this, otherParent);
    }

    public static BearEntity createFromParents(AgeableMob parent, AgeableMob otherParent){
        BearEntity offspring = Primal_Entities.BEAR.get().create(parent.level());

        if(offspring!=null){
            //To being randomly of one of the parents variant
            if(parent instanceof BearEntity bear1 && otherParent instanceof BearEntity bear2) {
                offspring.setVariant(offspring.random.nextBoolean()? bear1.getVariant() : bear2.getVariant());
            }
            //To being a Grolar if one of the parents is a polar bear
            else if(parent instanceof PolarBear || otherParent instanceof PolarBear) {
                offspring.setVariant(Variant.GROLAR);
            }

            //For taming and assigning a random collar color
            if((parent instanceof BearEntity bear1 && bear1.isTame()) || (otherParent instanceof BearEntity bear2 && bear2.isTame())) {
                var tamedParent = parent instanceof BearEntity bear1? bear1: null;
                var otherTamedParent = otherParent instanceof BearEntity bear2? bear2: null;

                if ((tamedParent!=null && tamedParent.isTame()) || (otherTamedParent!=null && otherTamedParent.isTame())) {
                    offspring.setOwnerUUID(tamedParent!=null? tamedParent.getOwnerUUID(): otherTamedParent.getOwnerUUID());
                    offspring.setTame(true);
                    //Assign randomly one collar color
                    if (otherTamedParent==null) {
                        offspring.setCollarColor(tamedParent.getCollarColor());
                    } else if (tamedParent==null) {
                        offspring.setCollarColor(otherTamedParent.getCollarColor());
                    } else {
                        offspring.setCollarColor(offspring.getRandom().nextBoolean()? tamedParent.getCollarColor(): otherTamedParent.getCollarColor());
                    }
                }
            }
        }

        return offspring;
    }

    //──────────────────────────────────── Sounds ────────────────────────────────────
    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return this.getBrain().isActive(Activity.ROAR)? null: this.isBearSleeping()? Primal_Sounds.BEAR_SNORING.get(): this.isAggressive()? Primal_Sounds.BEAR_IDLE_ANGRY.get() : Primal_Sounds.BEAR_IDLE.get();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return Primal_Sounds.BEAR_HURT.get();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return Primal_Sounds.BEAR_DEATH.get();
    }

    @Override
    public @Nullable SoundEvent getRoarSound() {
        return Primal_Sounds.BEAR_ROAR.get();
    }

    public @Nullable SoundEvent getWakeUpSound() {
        return Primal_Sounds.BEAR_WAKE_UP.get();
    }

    public SoundEvent getEatingSound() {
        return Primal_Sounds.BEAR_EAT.get();
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

    protected void playChestEquipsSound() {
        this.playSound(Primal_Sounds.ANIMAL_CHEST.get(), 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
    }

    //──────────────────────────────────── GeckoLib & Visuals ────────────────────────────────────
    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        controllers.add(
                BearAnimations.mainController(this),
                BearAnimations.attackController(this));
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void swing(@NotNull InteractionHand hand) {
        this.triggerAnim("attack", this.getRandom().nextBoolean() ? "attack" : "attack2");
        super.swing(hand);
    }

    @Override
    public void tick() {
        super.tick();

        if(!this.level().isClientSide){
            if (this.getHoneyCounter() > 0) {
                this.setHoneyCounter(this.getHoneyCounter() - 1);
            }

            if (this.getAwakeCounter() > 0 && !this.isAggressive() && !this.getBrain().isActive(Activity.ROAR)) {
                this.setAwakeCounter(this.getAwakeCounter() - 1);
            }

            if (this.getHealingCooldown() > 0) {
                this.setHealingCooldown(this.getHealingCooldown()- 1);
            }
        }
    }

    //──────────────────────────────────── Misc ────────────────────────────────────
    @Override
    public float getWalkTargetValue(@NotNull BlockPos pos, @NotNull LevelReader level) {
        if (BearAi.isPosNearNearestRepellent(this, pos)) {
            return -1.0F;
        } else {
            return level.getBlockState(pos.below()).is(Blocks.GRASS_BLOCK) ? 10.0F : level.getPathfindingCostFromLightLevels(pos);
        }
    }

    @Override
    public int getMaxHeadYRot() {
        return this.isInWater()? 10: 35;
    }

    @Override
    public int getMaxHeadXRot() {
        return this.isInWater()? 20: 35;
    }

    @Override
    public int getMaxAirSupply() {
        if(this.tickCount<=0) return 1200;

        return this.getVariant() ==Variant.GROLAR? 2000: 1200;
    }

    @Override
    protected float getWaterSlowDown() {
        return this.getVariant()==Variant.GROLAR? 0.96F: 0.9f;
    }

    @Override
    public void openCustomInventoryScreen(@NotNull Player player) {
        if(this.hasChest()){
            player.openMenu(
                    new SimpleMenuProvider(
                            (i, inventory, player1) -> ChestMenu.threeRows(i, inventory, this.inventory), this.getName()
                    )
            );
        }
    }

    @Override
    public float getScale() {
        return this.isBaby() ? 0.4F : 1.0F;
    }

    @Override
    public boolean shouldTryTeleportToOwner() {
        LivingEntity livingentity = this.getOwner();
        return livingentity != null && this.distanceToSqr(this.getOwner()) >= (25*25);
    }

    @Override
    protected void dropEquipment() {
        if (this.inventory != null) {
            for (int i = 0; i < this.inventory.getContainerSize(); i++) {
                ItemStack itemstack = this.inventory.getItem(i);
                if (!itemstack.isEmpty()) {
                    this.spawnAtLocation(itemstack);
                }
            }
        }

        if (this.hasChest()) {
            if (!this.level().isClientSide) {
                this.spawnAtLocation(Blocks.BARREL);
            }

            this.setChest(false);
        }
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return this.isBearJockey();
    }

    @Override
    public boolean isPushable() {
        return super.isPushable() && !this.isBearSleeping();
    }

    @Override
    public void knockback(double strength, double x, double z) {
        if(this.isBearSleeping()) return;

        super.knockback(strength, x, z);
    }

    @Override
    public boolean canBeSeenAsEnemy() {
        return super.canBeSeenAsEnemy() && !this.isBearSleeping();
    }

    @Override
    public boolean canBeLeashed(@NotNull Player player) {
        return super.canBeLeashed(player) && !this.isBearSleeping();
    }

    @Override
    protected @NotNull Vec3 getLeashOffset() {
        return new Vec3(0.0, this.getEyeHeight()*0.6f, this.getBbWidth() * 0.2F);
    }

    @Override
    public boolean isTargetingVillager() {
        return this.getTarget() instanceof Villager
                //Last Target
                || this.getLastHurtMob() instanceof Villager
                //Roared target
                || (this.getBrain().getMemory(MemoryModuleType.ROAR_TARGET).isPresent() && this.getBrain().getMemory(MemoryModuleType.ROAR_TARGET).get() instanceof Villager);
    }

    //──────────────────────────────────── Neutral Behavior ────────────────────────────────────
    @Override
    public int getRemainingPersistentAngerTime() {return 0;}

    @Override
    public void setRemainingPersistentAngerTime(int remainingPersistentAngerTime) {}

    @Override
    public @org.jetbrains.annotations.Nullable UUID getPersistentAngerTarget() {return null;}

    @Override
    public void setPersistentAngerTarget(@org.jetbrains.annotations.Nullable UUID persistentAngerTarget) {}

    @Override
    public void startPersistentAngerTimer() {}
}