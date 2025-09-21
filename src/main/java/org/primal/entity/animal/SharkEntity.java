package org.primal.entity.animal;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.Brain.Provider;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.client.animation.entity.SharkAnimations;
import org.primal.entity.ai.SharkAi;
import org.primal.entity.ai.controls.BreachingWaterBoundPathNavigation;
import org.primal.registry.*;
import org.primal.util.MiscUtil;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.UUID;
import java.util.function.IntFunction;

//xTODO
// x Spawn on oceans, rarer than dolphins
// x Don't despawn (duh)
// x Spawn in one or 2-3
// x Just swim around and occasionally hunt squids
// x Attack any entity below max health
// x Damage equal to zombified piglin with a bite
// x It can jump out of the water, but it tries to return to the water afterwards
// x Ignore you if you regained max health
// x Circle nearby conduits and follow players with conduit power
// x Drop 1-2 tooth when attacking enemies
// x Drops 2-4 of fish + 1 shark tooth
// x Put triggers for advancements
// x Tooth can repair tridents
// x Tooth can be used as blocks
public class SharkEntity extends WaterAnimal implements VariantHolder<SharkEntity.Variant>, GeoEntity, NeutralMob {

    //Attributes and Variants
    public enum Variant implements StringRepresentable {
        GREAT_WHITE(0, "great_white"),
        HAMMERHEAD(1, "hammerhead"),
        TIGER(2, "tiger");

        public static final Codec<SharkEntity.Variant> CODEC = StringRepresentable.fromEnum(SharkEntity.Variant::values);

        private static final IntFunction<SharkEntity.Variant> BY_ID = ByIdMap.continuous(SharkEntity.Variant::getId, values(),
                ByIdMap.OutOfBoundsStrategy.CLAMP);

        public static SharkEntity.Variant byId(int id) {
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
                .add(Attributes.MAX_HEALTH, 30f)
                .add(Attributes.MOVEMENT_SPEED, 1.0f)
                .add(Attributes.ATTACK_DAMAGE, 8f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.4f);
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public SharkEntity(EntityType<? extends WaterAnimal> entityType, Level level) {
        super(entityType, level);
        this.setHealth(this.getMaxHealth());
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
    }

    @SuppressWarnings("all")
    @Override
    public @NotNull SpawnGroupData finalizeSpawn(ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        Holder<Biome> holder = level.getBiome(this.blockPosition());

        if (holder.is(Primal_Tags.SPAWNS_TIGER_SHARK)) {
            this.setVariant(Variant.TIGER);
        } else if (holder.is(Primal_Tags.SPAWNS_HAMMERHEAD)) {
            this.setVariant(Variant.HAMMERHEAD);
        } else {
            this.setVariant(Variant.GREAT_WHITE);
        }

        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(
                SharkAnimations.mainController(this)
                        .receiveTriggeredAnimations());
    }

    @Override
    public int getMaxHeadYRot() {
        return 1;
    }

    @Override
    public int getMaxHeadXRot() {
        return 1;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isNoAi())
            this.setAirSupply(this.getMaxAirSupply());

        //Make it jump out of the water like a fish
        if (this.onGround() && !this.isInWater() && !this.isDeadOrDying() && !this.shouldBeBeached()) {
            this.setDeltaMovement(
                    this.getDeltaMovement().add((this.random.nextFloat() - 1.0F) * 0.2F, 0.5, (this.random.nextFloat() - 1.0F) * 0.2F)
            );
            this.setYRot(this.random.nextFloat() * 360.0F);
            this.setOnGround(false);
            this.hasImpulse = true;
            this.playSound(this.getFlopSound(), this.getSoundVolume(), this.getVoicePitch());
        }
    }

    //SynchedData
    private static final EntityDataAccessor<Integer> DATA_VARIANT_ID = SynchedEntityData.defineId(SharkEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_JOCKEY = SynchedEntityData.defineId(SharkEntity.class, EntityDataSerializers.BOOLEAN);


    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT_ID, SharkEntity.Variant.GREAT_WHITE.id);
        builder.define(IS_JOCKEY, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant().id);

        compound.putBoolean("SharkJockey", this.isSharkJockey());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(SharkEntity.Variant.byId(compound.getInt("Variant")));

        this.setSharkJockey(compound.getBoolean("SharkJockey"));
    }

    @Override
    public void setVariant(SharkEntity.@NotNull Variant variant) {
        this.entityData.set(DATA_VARIANT_ID, variant.id);
    }

    @Override
    public SharkEntity.@NotNull Variant getVariant() {
        return SharkEntity.Variant.byId(this.entityData.get(DATA_VARIANT_ID));
    }

    public boolean isSharkJockey() {
        return this.entityData.get(IS_JOCKEY);
    }

    public void setSharkJockey(boolean isJockey) {
        this.entityData.set(IS_JOCKEY, isJockey);
    }

    //Movement
    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new BreachingWaterBoundPathNavigation(this, level);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();

        Brain<SharkEntity> brain = this.getBrain();
        brain.tick((ServerLevel) this.level(), this);

        this.setSprinting(brain.getMemory(MemoryModuleType.ATTACK_TARGET).isPresent());

        SharkAi.updateActivity(this);

        if(this.isSharkJockey() && this.shouldBeBeached()){
            this.ejectPassengers();
        }

        //To remove the bear jockey value if the rider dies
        if(this.isSharkJockey() && !this.isVehicle()){
            this.setSharkJockey(false);
        }
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.005, 0.0));
            }
        } else {
            super.travel(travelVector);
        }
    }

    @Override
    public float getSpeed() {
        return this.isSprinting()? super.getSpeed()*1.3f : super.getSpeed() ;
    }

    //AI
    @Override
    protected void registerGoals() {}

    @Override
    protected @NotNull Provider<SharkEntity> brainProvider() {
        return SharkAi.brainProvider();
    }

    @Override
    protected @NotNull Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
        return SharkAi.makeBrain(this.brainProvider().makeBrain(dynamic));
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull Brain<SharkEntity> getBrain() {
        return (Brain<SharkEntity>) super.getBrain();
    }

    @Nullable
    @Override
    public LivingEntity getTarget() {
        return this.getTargetFromBrain();
    }

    @Override
    public boolean canAttack(@NotNull LivingEntity target) {
        return super.canAttack(target)
                //To only attack if inside water
                && this.isInWater()
                //Attack enemies at low health
                && (target.getHealth()<target.getMaxHealth()
                //Attack squids if it hasn't hunted in a while
                || (!this.getBrain().hasMemoryValue(MemoryModuleType.HAS_HUNTING_COOLDOWN) && target.getType().is(Primal_Tags.SHARK_HUNTABLE))
                //To defend itself
                || this.getLastHurtByMob()==target
                //To defend players near conduits
                || (this.getBrain().hasMemoryValue(Primal_MemoryModuleTypes.NEAREST_CONDUIT_PLAYER.get()) && (this.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_CONDUIT_PLAYER.get()).get().getLastHurtMob()==target || this.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_CONDUIT_PLAYER.get()).get().getLastHurtByMob()==target)))
                //To never attack someone with conduit power
                && !target.hasEffect(MobEffects.CONDUIT_POWER)
                && MiscUtil.isNotNeverAttack(target);
    }

    @Override
    public boolean killedEntity(@NotNull ServerLevel level, @NotNull LivingEntity killed) {
        //Put the cooldown to attack squids each 600 ticks (30s)
        if(killed.getType().is(Primal_Tags.SHARK_HUNTABLE)){
            this.getBrain().setMemoryWithExpiry(MemoryModuleType.HAS_HUNTING_COOLDOWN, true, 600L);
        }

        //Trigger advancement
        if(Primal_Advancements.ANIMALS_SHARK_NEEDS_TO_KILL.contains(killed.getType())){
            List<ServerPlayer> playersList= this.level().getEntitiesOfClass(ServerPlayer.class, this.getBoundingBox().inflate(24));

            if(!playersList.isEmpty()){
                playersList.forEach(player -> Primal_Advancements.FEED_SHARK.get().trigger(player, killed));
            }
        }

        return super.killedEntity(level, killed);
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        boolean hurt = super.hurt(source, amount);
        if (this.level().isClientSide) {
            return false;
        } else {
            if (hurt && source.getEntity() instanceof LivingEntity target) {
                SharkAi.wasHurtBy(this, target);
            }

            return hurt;
        }
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entity) {
        boolean hurt = super.doHurtTarget(entity);

        //Drops shark tooth on attack
        if(this.getRandom().nextIntBetweenInclusive(0, 100)<5){
            ItemStack toothStack=new ItemStack(Primal_Items.SHARK_TOOTH, this.getRandom().nextBoolean()? 2: 1);
            this.spawnAtLocation(toothStack);
        }

        if(entity instanceof LivingEntity target)
            this.getBrain().setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, target, this.getRandom().nextIntBetweenInclusive(20, 100));

        return hurt;
    }

    @Override
    protected void positionRider(@NotNull Entity passenger, @NotNull MoveFunction callback) {
        if (this.hasPassenger(passenger)) {
//Fin -> 0.6y +0.3forward

            // Vertical position
            double y = this.getY()+0.2;

            // Offset forward and right relative to the body rotation
            float bodyYawRad = this.yBodyRot * Mth.DEG_TO_RAD;
            double forwardOffset = -0.8; // Forward (Z direction)
            double sideOffset = -0.0;    // Right (X direction)

            double x = this.getX() + (Mth.sin(bodyYawRad) * forwardOffset) + (Mth.cos(bodyYawRad) * sideOffset);
            double z = this.getZ() - (Mth.cos(bodyYawRad) * forwardOffset) + (Mth.sin(bodyYawRad) * sideOffset);

            callback.accept(passenger, x, y, z);
        }
    }

    //Sounds
    protected SoundEvent getFlopSound() {
        return Primal_Sounds.SHARK_FLOP.get();
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return Primal_Sounds.SHARK_IDLE.get();
    }

    @Override
    public int getAmbientSoundInterval() {
        return 200;
    }

    @Override
    protected void playAttackSound() {
        this.playSound(Primal_Sounds.SHARK_ATTACK.get(), 1.0F, 1.0F);
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return Primal_Sounds.SHARK_HURT.get();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return Primal_Sounds.SHARK_DEATH.get();
    }

    //Misc
    public static int AirSupplyWhenBeached=1200;

    public boolean shouldBeBeached(){
        return this.getAirSupply()<=AirSupplyWhenBeached;
    }

    @Override
    public int getMaxAirSupply() {
        return 2400;
    }

    @Override
    protected void handleAirSupply(int airSupply) {
        if (this.isAlive() && !this.isInWaterOrBubble()) {
            this.setAirSupply(airSupply - 1);
            if (this.getAirSupply() == -20) {
                this.setAirSupply(0);
                this.hurt(this.damageSources().drown(), 2.0F);
            }
        } else {
            this.setAirSupply(this.getMaxAirSupply());
        }
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return isSharkJockey();
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
