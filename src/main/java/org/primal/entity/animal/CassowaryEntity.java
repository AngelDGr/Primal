package org.primal.entity.animal;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.Unit;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.client.animation.entity.CassowaryAnimations;
import org.primal.entity.ai.CassowaryAi;
import org.primal.registry.*;
import org.primal.util.Primal_Util;
import org.primal.util.mob_types.AttackVillagers;
import org.primal.util.mob_types.VariantHolderWithEgg;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.IntFunction;

//xTODO
// Add sounds
//They attack each other until their health is low, in which both will run away from each other
//They will take their time to eat it while you sneak to the nest
//They can be distracted with glistening melon witch they love
//Add the cassowary egg.
//It usually wanders around, sits on the ground and pecks on the ground.
//When angry, a particle of villager hurt emits from their head before going towards you.
//It searches for a spot to make a nest and lay one large green egg.
//After some time it hatches into a baby cassowary.
//It hates villagers & illagers and will attack these guys.
//  Upon death, they drop lots of feathers and little experience.
// It can easily break your armor and shield's durability.
// Their attacks are quite deadly, when attacking it jumps and raises their claws.
// They deal 3 hearts of damage by default.
// It's highly aggressive upon seeing the player,
// Add Spawning (Cassowary Nests): Bamboo Jungle & Jungle.
// Add Suspicious Gravel to nests, which you can uncover various loot, including Petrified Fruit
// Cassowaries will eat “Petrified fruit”, which in return it poops out seeds of either Litchi, Kiwano, or Starfruit
// It can be bred with any of the exotic fruits.
public class CassowaryEntity extends Animal implements VariantHolder<CassowaryEntity.Variant>, VariantHolderWithEgg<CassowaryEntity.Variant, CassowaryEntity>, GeoEntity, NeutralMob, AttackVillagers {

    //──────────────────────────────────── Variants ────────────────────────────────────
    public enum Variant implements StringRepresentable {
        MIDNIGHT(0, "midnight"),
        SUNSET(1, "sunset");

        public static final Codec<CassowaryEntity.Variant> CODEC = StringRepresentable.fromEnum(CassowaryEntity.Variant::values);

        private static final IntFunction<CassowaryEntity.Variant> BY_ID = ByIdMap.continuous(CassowaryEntity.Variant::getId, values(),
                ByIdMap.OutOfBoundsStrategy.CLAMP);

        public static CassowaryEntity.Variant byId(int id) {
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
    public void setVariant(@NotNull CassowaryEntity.Variant variant) {
        this.entityData.set(DATA_VARIANT_ID, variant.id);
    }

    @Override
    public @NotNull CassowaryEntity.Variant getVariant() {
        return CassowaryEntity.Variant.byId(this.entityData.get(DATA_VARIANT_ID));
    }

    @Override
    public void setVariantFromBiome(CassowaryEntity animal, Holder<Biome> holder) {
        if (holder.is(Primal_Tags.Biome.SPAWNS_SUNSET_CASSOWARY)) {
            animal.setVariant(CassowaryEntity.Variant.SUNSET);
        } else {
            animal.setVariant(CassowaryEntity.Variant.MIDNIGHT);
        }
    }

    //──────────────────────────────────── Init ────────────────────────────────────
    public CassowaryEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.getNavigation().setCanFloat(true);
        this.xpReward=10;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30f)
                .add(Attributes.MOVEMENT_SPEED, 0.23f)
                .add(Attributes.ATTACK_KNOCKBACK, 1.2f)
                .add(Attributes.ATTACK_DAMAGE, 3f)
                .add(Attributes.STEP_HEIGHT, 1.5f);
    }

    private static final EntityDataAccessor<Integer> DATA_VARIANT_ID = SynchedEntityData.defineId(CassowaryEntity.class, EntityDataSerializers.INT);

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT_ID, Variant.MIDNIGHT.id);
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        setVariantFromBiome(this, level.getBiome(this.blockPosition()));
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant().id);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(CassowaryEntity.Variant.byId(compound.getInt("Variant")));
    }

    //──────────────────────────────────── AI & Movement ────────────────────────────────────
    @Override
    protected void registerGoals() {}

    @Override
    protected @NotNull Brain.Provider<CassowaryEntity> brainProvider() {
        return CassowaryAi.brainProvider();
    }

    @Override
    protected @NotNull Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
        return CassowaryAi.makeBrain(this.brainProvider().makeBrain(dynamic));
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull Brain<CassowaryEntity> getBrain() {
        return (Brain<CassowaryEntity>) super.getBrain();
    }

    @Override
    protected void customServerAiStep() {
        boolean hadTarget = this.getTarget()!=null;
        super.customServerAiStep();

        //Starts the brain
        Brain<CassowaryEntity> brain = this.getBrain();
        brain.tick((ServerLevel) this.level(), this);
        CassowaryAi.updateActivity(this);

        //Makes it sprint when attacking on land
        if(!this.isInWater())
            this.setSprinting(brain.getMemory(MemoryModuleType.ATTACK_TARGET).isPresent());
        if(this.isSprinting() && this.isInWater())
            this.setSprinting(false);

        boolean hasTarget = this.getTarget()!=null;

        if(!hadTarget && hasTarget)
            this.level().broadcastEntityEvent(this, (byte)72);
    }

    @Override
    public void aiStep() {
        super.aiStep();
    }

    @Override
    public float getSpeed() {
        return this.isSprinting()? super.getSpeed()*1.3f : super.getSpeed();
    }

    public void stopMoving(){
        this.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        this.getNavigation().stop();
        this.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
    }

    @Override
    public boolean wantsToPickUp(@NotNull ItemStack stack) {
        return stack.is(Primal_Tags.Item.DISTRACTS_CASSOWARY) || stack.is(Primal_Tags.Item.PROCESSES_CASSOWARY);
    }

    public List<ItemStack> getDroppedSeeds() {
        if(this.level().getServer() == null) return Collections.emptyList();

        final LootTable lootTable = this.level().getServer().reloadableRegistries().getLootTable(Primal_LootTables.CASSOWARY_PROCESSED_SEEDS);
        return lootTable.getRandomItems(new LootParams.Builder((ServerLevel)this.level())
                .withParameter(LootContextParams.THIS_ENTITY, this)
                .withParameter(LootContextParams.ORIGIN, this.position())
                .create(LootContextParamSets.GIFT));
    }

    public boolean refuseToMove() {
//        return ImmutableList.of(Pose.ROARING, Pose.SNIFFING, Pose.CROAKING).contains(this.getPose());
        return false;
    }

    //──────────────────────────────────── Combat ────────────────────────────────────
    @Nullable
    @Override
    public LivingEntity getTarget() {
        return this.getTargetFromBrain();
    }

    @Override
    public boolean canAttack(@NotNull LivingEntity target) {
        //To stop attacking
        if(target.getType().equals(this.getType()) && !target.isBaby() && target.getHealth()<=target.getMaxHealth()*0.30){
            return false;
        }

        if(this.getBrain().isMemoryValue(MemoryModuleType.ATTACK_TARGET, target) && super.canAttack(target))
            return true;

        return super.canAttack(target) &&
                //Hunts
                ((target.getType().is(Primal_Tags.Entity.CASSOWARY_HUNTABLE))
                        //Attack other adult cassowaries with more than 30% health
                        || (target.getType().equals(this.getType()) && !target.isBaby() && target.getHealth()>=target.getMaxHealth()*0.30 && this.getHealth()>=this.getMaxHealth()*0.30)
                )
                && !this.isPacified()
                && Primal_Util.isNotNeverAttack(target);
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if(source.getEntity() !=null && source.getEntity().getType().equals(this.getType()) && this.random.nextBoolean())
            amount=amount+ this.getRandom().nextIntBetweenInclusive(1, 2);

        boolean hurt = super.hurt(source, amount);

        if(hurt){
            this.getBrain().setMemoryWithExpiry(Primal_MemoryModuleTypes.WAS_IDLE_ANIMATION.get(), true, 40);

            if (source.getEntity() instanceof LivingEntity target && !(target instanceof Player player && player.isCreative()))
                Primal_Util.Ai.wasHurtByAndAttacks(this, target, CassowaryEntity.class, false, true, true);
        }

        //The loser one
        if(source.getEntity() !=null && source.getEntity().getType().equals(this.getType()) && this.getHealth()<this.getMaxHealth()*0.31){
            this.getBrain().eraseMemory(MemoryModuleType.HOME);
            this.level().broadcastEntityEvent(this, (byte) 13);
        }

        return hurt;
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity target) {
        var hurt = super.doHurtTarget(target);
        if(hurt){
            //Damages shield a lot
            if(target instanceof final Player player && player.isBlocking()){
                player.getOffhandItem().hurtAndBreak(10, player, getSlotForHand(player.getUsedItemHand()));
            }

            if(target instanceof LivingEntity livingTarget){
                //Damages the armor a lot
                Primal_Util.damageEquipment(livingTarget, this.damageSources().mobAttack(this), 10,
                        EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD,
                        EquipmentSlot.BODY);

                //If it was another cassowary the last hurt, then it's the winer
                if(target.getType().equals(this.getType()) && livingTarget.getHealth()<livingTarget.getMaxHealth()*0.31){
                    this.level().broadcastEntityEvent(this, (byte) 14);
                    this.heal(this.getRandom().nextIntBetweenInclusive(4, 6));
                }
            }
        }
        return hurt;
    }

    public boolean isPacified(){
        return this.getBrain().hasMemoryValue(MemoryModuleType.PACIFIED);
    }

    public boolean isWithinLungeAttackRange(LivingEntity entity) {
        return this.getAttackBoundingBox().inflate(2).intersects(entity.getHitbox());
    }

    public boolean isSeeingTarget(LivingEntity target) {
        Vec3 vec3d = target.position();

        Vec3 vec3d2 = this.calculateViewVector(0.0f, this.getYHeadRot());
        Vec3 vec3d3 = vec3d.vectorTo(this.position());
        vec3d3 = new Vec3(vec3d3.x, 0.0, vec3d3.z).normalize();

        return vec3d3.dot(vec3d2) < -0.9;
    }

    @Override
    protected int getBaseExperienceReward() {
        return this.xpReward;
    }

    //──────────────────────────────────── Feeding & Interaction ────────────────────────────────────
    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return isMatingFood(stack);
    }

    public static boolean isMatingFood(@NotNull ItemStack stack){
        return stack.is(Primal_Tags.Item.EXOTIC_FRUITS);
    }

    @Override
    public void spawnChildFromBreeding(@NotNull ServerLevel level, @NotNull Animal mate) {
        this.finalizeSpawnChildFromBreeding(level, mate, null);
        this.getBrain().setMemory(MemoryModuleType.IS_PREGNANT, Unit.INSTANCE);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob otherParent) {
        if(otherParent instanceof CassowaryEntity otherParentCasted)
            return Primal_Util.createFromParents(Primal_Entities.CASSOWARY.get(),
                    this,
                    otherParentCasted, c-> CassowaryAi.initMemories(c, level.getRandom()));

        return null;
    }

    //──────────────────────────────────── GeckoLib/Visuals ────────────────────────────────────
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(CassowaryAnimations.mainController(this));
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if(id == 72){
            Primal_Util.Visuals.addParticleAboveSelf(this, ParticleTypes.ANGRY_VILLAGER, 5);
        } else if (id == 13) {
            Primal_Util.Visuals.addParticlesAroundSelf(this, ParticleTypes.ANGRY_VILLAGER, 5);
        } else if (id == 14) {
            Primal_Util.Visuals.addParticlesAroundSelf(this, ParticleTypes.HAPPY_VILLAGER, 5);
        } else {
            super.handleEntityEvent(id);
        }
    }

    //──────────────────────────────────── Sounds ────────────────────────────────────
    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return Primal_Sounds.CASSOWARY_IDLE.get();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return Primal_Sounds.CASSOWARY_HURT.get();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return Primal_Sounds.CASSOWARY_DEATH.get();
    }

    public @Nullable SoundEvent getLungeSound() {
        return Primal_Sounds.CASSOWARY_LUNGE.get();
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

    public SoundEvent getEatingSound() {
        return Primal_Sounds.CASSOWARY_EAT.get();
    }

    public SoundEvent getPlopSound() {
        return Primal_Sounds.CASSOWARY_PLOP.get();
    }

    //──────────────────────────────────── Misc ────────────────────────────────────
    @Override
    public boolean canBeLeashed() {
        return !isAggressive();
    }

    @Override
    protected @NotNull Vec3 getLeashOffset() {
        return new Vec3(0.0, this.getEyeHeight()*0.8f, this.getBbWidth() * 0.2F);
    }

    @Override
    public boolean isTargetingVillager() {
        return this.getTarget() instanceof Villager
                //Last Target
                || this.getLastHurtMob() instanceof Villager;
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
