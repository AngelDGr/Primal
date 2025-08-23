package org.primal.entity.animal;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.client.animation.entity.BearAnimations;
import org.primal.entity.ai.BearAi;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;

import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.Brain.Provider;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import org.primal.registry.Primal_Entities;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.registry.Primal_Tags;
import org.primal.util.MiscUtil;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.util.GeckoLibUtil;

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
public class BearEntity extends TamableAnimal implements VariantHolder<BearEntity.Variant>, GeoEntity, ContainerListener, HasCustomInventoryScreen, OwnableEntity {

    protected SimpleContainer inventory=new SimpleContainer(27);
    @Override
    public void containerChanged(@NotNull Container container) {
    }

    //Attributes and Variants
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

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 80f)
                .add(Attributes.MOVEMENT_SPEED, 0.1f)
                .add(Attributes.ATTACK_DAMAGE, 8f)
                .add(Attributes.ATTACK_KNOCKBACK, 1.8f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6f);
    }

    //Init
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public BearEntity(EntityType<BearEntity> entityType, Level level) {
        super(entityType, level);
        this.setHealth(this.getMaxHealth());
        this.getNavigation().setCanFloat(true);
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        Holder<Biome> holder = level.getBiome(this.blockPosition());

        if (holder.is(Primal_Tags.SPAWNS_BLACK_BEAR)) {
            this.setVariant(Variant.WARM);
        } else {
            this.setVariant(Variant.GRIZZLY);
        }

        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        controllers.add(
                BearAnimations.mainController(this)
                        .receiveTriggeredAnimations()
                        .triggerableAnim("roar", BearAnimations.ROAR)
                        .triggerableAnim("beg_end", RawAnimation.begin().thenPlay("animation.grizzly_bear.beg_end"))
                        .triggerableAnim("sleep_start", RawAnimation.begin().thenPlay("animation.grizzly_bear.sleep_start"))
                        .triggerableAnim("sleep_end", RawAnimation.begin().thenPlay("animation.grizzly_bear.sleep_end")),

                new AnimationController<>(this, "alt_idle", state -> PlayState.STOP).triggerableAnim("alt_idle", BearAnimations.IDLE_ALT),
                new AnimationController<>(this, "attack", state -> PlayState.STOP).triggerableAnim("attack", BearAnimations.ATTACK).triggerableAnim("attack2", BearAnimations.ATTACK_ALT));
    }

    @Override
    public int getMaxHeadYRot() {
        return 35;
    }

    @Override
    public int getMaxHeadXRot() {
        return 35;
    }

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
    public boolean doHurtTarget(@NotNull Entity entity) {
        boolean hurt = super.doHurtTarget(entity);

        List<Entity> attackablesEntities =
                new ArrayList<>(this.level().getEntities(this, this.getBoundingBox().inflate(1))
                        .stream().filter(entity1 -> MiscUtil.isSeeingTarget(entity1, this, -0.8f)).toList());

        // To not attack more than 5 entities at the same time
        if (attackablesEntities.size() > 5) {
            attackablesEntities = attackablesEntities.subList(0, 5);
        }

        for(Entity otherEntity: attackablesEntities) {
            doHurtOther(otherEntity);
        }

        return hurt;
    }

    public void doHurtOther(@NotNull Entity entity){
        float f = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE)*0.6f;
        DamageSource damagesource = this.damageSources().mobAttack(this);
        if (this.level() instanceof ServerLevel serverlevel) {
            f = EnchantmentHelper.modifyDamage(serverlevel, this.getWeaponItem(), entity, damagesource, f);
        }

        boolean flag = entity.hurt(damagesource, f);
        if (flag) {
            float f1 = this.getKnockback(entity, damagesource);
            if (f1 > 0.0F && entity instanceof LivingEntity livingentity) {
                livingentity.knockback(
                        f1 * 0.2F,
                        Mth.sin(this.getYRot() * (float) (Math.PI / 180.0)),
                        -Mth.cos(this.getYRot() * (float) (Math.PI / 180.0))
                );
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.6, 1.0, 0.6));
            }

            if (this.level() instanceof ServerLevel serverlevel1) {
                EnchantmentHelper.doPostAttackEffects(serverlevel1, entity, damagesource);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            if (this.tickCount % (20 * 15) == 0 && this.getRandom().nextBoolean()) {
                this.triggerAnim("alt_idle", "alt_idle");
            }
        } else {
            setCounters();
        }
    }

    //SynchedData
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
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(BEAR_SLEEPING, false);
        builder.define(DATA_VARIANT_ID, Variant.GRIZZLY.id);
        builder.define(DATA_HONEY_COUNTER, 0);
        builder.define(AWAKE_COUNTER, 0);
        builder.define(HEALING_COOLDOWN, 0);
        builder.define(HAS_CHEST, false);
        builder.define(FOLLOWER_STATE, 0);
        builder.define(DATA_COLLAR_COLOR, DyeColor.RED.getId());
        builder.define(IS_JOCKEY, false);
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
        if(this.getOwnerUUID()!=null){
            compound.putInt("FollowerState", this.getFollowerState());
        }

        if (this.hasChest()) {
            ListTag listtag = new ListTag();

            for (int i = 0; i < this.inventory.getContainerSize(); i++) {
                ItemStack itemstack = this.inventory.getItem(i);
                if (!itemstack.isEmpty()) {
                    CompoundTag compoundtag = new CompoundTag();
                    compoundtag.putByte("Slot", (byte)(i));
                    listtag.add(itemstack.save(this.registryAccess(), compoundtag));
                }
            }

            compound.put("Items", listtag);
        }

        compound.putByte("CollarColor", (byte)this.getCollarColor().getId());

        compound.putBoolean("BearJockey", this.isBearJockey());
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

        if (compound.hasUUID("Owner")) {
            this.setFollowerState(compound.getInt("FollowerState"));
        }

        if (this.hasChest()) {
            ListTag listtag = compound.getList("Items", 10);

            for (int i = 0; i < listtag.size(); i++) {
                CompoundTag compoundtag = listtag.getCompound(i);
                int j = compoundtag.getByte("Slot") & 255;
                if (j < this.inventory.getContainerSize()) {
                    this.inventory.setItem(j, ItemStack.parse(this.registryAccess(), compoundtag).orElse(ItemStack.EMPTY));
                }
            }
        }

        if (compound.contains("CollarColor", 99)) {
            this.setCollarColor(DyeColor.byId(compound.getInt("CollarColor")));
        }

        this.setBearJockey(compound.getBoolean("BearJockey"));
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

    public DyeColor getCollarColor() {
        return DyeColor.byId(this.entityData.get(DATA_COLLAR_COLOR));
    }

    private void setCollarColor(DyeColor collarColor) {
        this.entityData.set(DATA_COLLAR_COLOR, collarColor.getId());
    }

    public boolean isBearJockey() {
        return this.entityData.get(IS_JOCKEY);
    }

    public void setBearJockey(boolean isJockey) {
        this.entityData.set(IS_JOCKEY, isJockey);
    }

    public int getFollowerState() {
        return this.entityData.get(FOLLOWER_STATE);
    }

    public void setFollowerState(int state) {
        this.entityData.set(FOLLOWER_STATE, state);
    }

    public boolean isSitting() {
        return this.getFollowerState() == 2;
    }

    public boolean isFollowing() {
        return this.getFollowerState() == 1;
    }

    public boolean isWandering() {
        return this.getFollowerState() == 0;
    }

    @Override
    public void setVariant(Variant variant) {
        this.entityData.set(DATA_VARIANT_ID, variant.id);
    }

    @Override
    public @NotNull Variant getVariant() {
        return Variant.byId(this.entityData.get(DATA_VARIANT_ID));
    }

    private void setCounters(){
        if (this.getHoneyCounter() > 0) {
            this.setHoneyCounter(this.getHoneyCounter() - 1);
        }

        if (this.getAwakeCounter() > 0) {
            this.setAwakeCounter(this.getAwakeCounter() - 1);
        }

        if (this.getHealingCooldown() > 0) {
            this.setHealingCooldown(this.getHealingCooldown()- 1);
        }
    }

    //Breeding
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
        return createBearFromParents(this, otherParent);
    }

    public static BearEntity createBearFromParents(AgeableMob parent, AgeableMob otherParent){
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
        }

        return offspring;
    }

    //Movement
    public boolean refuseToMove() {
        return ImmutableList.of(Pose.ROARING, Pose.SNIFFING, Pose.CROAKING).contains(this.getPose());
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();

        Brain<BearEntity> brain = this.getBrain();
        brain.tick((ServerLevel) this.level(), this);

        if(!this.isInWater())
            this.setSprinting(brain.getMemory(MemoryModuleType.ATTACK_TARGET).isPresent());
        if(this.isSprinting() && this.isInWater())
            this.setSprinting(false);

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
                heal(2);
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
    }

    @Override
    public float getSpeed() {
        return this.isSprinting()? super.getSpeed()*1.3f : this.getPose().equals(Pose.ROARING)? 0: super.getSpeed() ;
    }

    public AttributeModifier createSpeedModifier(float amount){
        return new AttributeModifier(
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "health_speed_modifier"),
                Mth.clamp(amount, 0.0, 0.13),
                AttributeModifier.Operation.ADD_VALUE);
    }

    public void applySpeedModifiers(){
        AttributeModifier modifier = createSpeedModifier((this.getHealth()/this.getMaxHealth())* 0.13f);

        final AttributeInstance entityAttributeInstance_speed = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if(entityAttributeInstance_speed!=null) {
            entityAttributeInstance_speed.removeModifier(modifier.id());
            entityAttributeInstance_speed.addTransientModifier(modifier);
        }
    }

    public boolean bearCollapses(){
        return (this.getMaxHealth()*0.20f) > this.getHealth();
    }

    @Override
    public float getWalkTargetValue(@NotNull BlockPos pos, @NotNull LevelReader level) {
        if (BearAi.isPosNearNearestRepellent(this, pos)) {
            return -1.0F;
        } else {
            return level.getBlockState(pos.below()).is(Blocks.GRASS_BLOCK) ? 10.0F : level.getPathfindingCostFromLightLevels(pos);
        }
    }

    @Override
    protected float getWaterSlowDown() {
        return this.getVariant()==Variant.GROLAR? 0.96F: 0.9f;
    }

    //AI
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
    public boolean hurt(@NotNull DamageSource source, float amount) {
        boolean hurt = super.hurt(source, amount);
        if (this.level().isClientSide) {
            return false;
        } else {
            if (hurt && source.getEntity() instanceof LivingEntity target && target!=this.getOwner()) {
                BearAi.wasHurtBy(this, target);
            }

            return hurt;
        }
    }

    public boolean canTargetEntity(LivingEntity target) {
        return !(this.isBaby() || this.bearCollapses())
                //To detect attackable
                && this.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE).isPresent() && this.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE).get()==target
                //To see if Bear can attack (If it is near a baby or is tamed it attacks normally, otherwise only attacks at less than <8 blocks without shifting)
                && ((this.getBrain().hasMemoryValue(Primal_MemoryModuleTypes.NEAREST_VISIBLE_BABY.get())) || (this.isTame()) || (target.distanceTo(this)<8 && !target.isShiftKeyDown()))
                //To not double trigger the moments the enemy is dying and is attackable
                && target.getHealth()>0 && target.canBeSeenAsEnemy();
    }

    public void setAttackTarget(LivingEntity attackTarget) {
        this.getBrain().eraseMemory(MemoryModuleType.ROAR_TARGET);
        this.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, attackTarget);
        this.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
    }

    //Feeding
    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        //Tame/Heal/Mating
        return isTameFood(stack) || isHealFood(stack) || isMatingFood(stack);
    }

    public static boolean isTameFood(@NotNull ItemStack stack){
        return stack.is(Items.HONEYCOMB);
    }

    public static boolean isHealFood(@NotNull ItemStack stack){
        return stack.is(Items.SWEET_BERRIES);
    }

    public static boolean isMatingFood(@NotNull ItemStack stack){
        return stack.is(Items.SALMON_BUCKET);
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack stackInHand = player.getItemInHand(hand);

        //To handle dye collar logic
        if (stackInHand.getItem() instanceof DyeItem dyeitem && this.isOwnedBy(player)) {
            DyeColor dyecolor = dyeitem.getDyeColor();
            if (dyecolor != this.getCollarColor()) {
                this.setCollarColor(dyecolor);
                stackInHand.consume(1, player);
                return InteractionResult.SUCCESS;
            }
        }

        //To handle food logic
        if(this.isFood(stackInHand)){
            boolean wasFeed = this.handleEating(player, stackInHand);
            if (wasFeed) {
                stackInHand.consume(1, player);
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
            stackInHand.consume(1, player);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        //Only if tame
        if (this.isTame()) {
            //Awake bear if it is sleeping by their own
            if(this.isBearSleeping() && !this.isSitting()) {
                this.setAwakeCounter((20)*30);
                return InteractionResult.sidedSuccess(this.level().isClientSide());
            }
            //Make it change the follow state (Wandering/Follow/Sitting)
            else {
                return changeFollowState(player, hand);
            }
        }

        return InteractionResult.PASS;
    }

    public InteractionResult changeFollowState(Player player, InteractionHand hand){
        if(player!=this.getOwner()){
            if (this.level().isClientSide) {
                return InteractionResult.CONSUME;
            } else {
                return InteractionResult.PASS;
            }
        }

        if(!this.level().isClientSide && hand.equals(InteractionHand.MAIN_HAND)){
            this.setFollowerState(this.isWandering()? 1: this.isFollowing()? 2: 0);

            player.displayClientMessage(Component.translatable(
                    this.isFollowing()? "primal.gui.animal_following":
                            this.isSitting()? "primal.gui.animal_sitting":
                                    "primal.gui.animal_wandering",
                    this.getName()), true);
        }


        return InteractionResult.sidedSuccess(this.level().isClientSide());
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

    protected boolean handleEating(@NotNull Player player, @NotNull ItemStack stack) {
        if (this.isFood(stack)) {
            //To try to mate
            if(isMatingFood(stack) && !this.isBearSleeping()){
                if (this.getAge() == 0 && this.canFallInLove()) {
                    this.setInLove(player);

                    return playEatingSound();
                }
            }

            //To try to tame it, unless it is sleeping
            if (!this.isTame() && isTameFood(stack) && !(this.isBearSleeping() && !this.bearCollapses())) {
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
            if (this.isTame()) {
                //To heal the bear
                if(isHealFood(stack)){
                    if (this.getHealth() < this.getMaxHealth()) {
                        this.heal(2f);

                        return playEatingSound();
                    }
                }
            }

            //To age up if baby, with sweet berries or salmon bucket
            if (this.isBaby() && !isTameFood(stack)) {
                this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0), 0.0, 0.0, 0.0);
                if (!this.level().isClientSide) {
                    this.ageUp(isMatingFood(stack)? 20: 10);
                }

                return playEatingSound();
            }
        }

        return false;
    }

    private int tameAttempts = 0;

    //Sounds
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
        this.playSound(SoundEvents.DONKEY_CHEST, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
    }

    public SoundEvent getEatingSound() {
        return null;
    }

    //Misc
    @Override
    public boolean isPushable() {
        return super.isPushable() && !this.isBearSleeping();
    }

    @Override
    public void knockback(double strength, double x, double z) {
        if(this.isBearSleeping())
            return;

        super.knockback(strength, x, z);
    }

    @Override
    public boolean shouldTryTeleportToOwner() {
        LivingEntity livingentity = this.getOwner();
        return livingentity != null && this.distanceToSqr(this.getOwner()) >= 225.0;
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
    public boolean canBeSeenAsEnemy() {
        return super.canBeSeenAsEnemy() && !this.isBearSleeping();
    }

    @Override
    public boolean canBeLeashed() {
        return super.canBeLeashed() && !this.isBearSleeping();
    }

    @Override
    protected @NotNull Vec3 getLeashOffset() {
        return new Vec3(0.0, this.getEyeHeight()*0.6f, this.getBbWidth() * 0.2F);
    }
}
