package org.primal.entity.animal;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.*;
import net.minecraft.util.valueproviders.UniformInt;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.client.animation.entity.DeerAnimations;
import org.primal.entity.ai.DeerAi;
import org.primal.entity.ai.controls.look.DeerLookControl;
import org.primal.entity.ai.controls.move.DeerMoveControl;
import org.primal.entity.ai.controls.navigation.DeerPathNavigation;
import org.primal.networking.DelayedTasks;
import org.primal.registry.*;
import org.primal.util.Primal_Util;
import org.primal.util.mob_types.DetectsFartherAway;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.IntFunction;

//xTODO:
//If killed by a lightning bolt, it can drop a special disc.
// Deer spawn in various forested biomes.
// - Fallow Deer live in Birch/flower Forest/meadows and cherry grove
// - Musk Deer live in Mega Taigas (both probably) and windswept forest
// - Reindeer live in Snowy Taigas and grove
// Due to their scaredy cat-like personality, they're quivering in fear during thunderstorms.
// Deers will be cautious when it sports a target that displays possible hostility.
// By default it's set to player, bear, wolf, etc. (any mobs that attacks them)
// Upon reaching 10 blocks, their movement is halted and they proceed to stare at their target.
// If it reaches a close distance (5b), this is when it becomes panicked.
// Deers can become panicked, in which they proceed to sprint and make panicked noises.
// Obviously, it goes panicked if harmed.
// The herd will flee if one of them is harmed.
// They can be panicked either from predators like Bears & Wolves, as well as loud noises like horns, explosions, lightning bolts and bells.
// After breeding, they will no longer become cautious and flee from their owner, unless harmed.
// The baby will begin trusting their owners and not become cautious when provoked.
// - They're always found in groups
// - When sprinting, it runs twice or thrice more than normal mobs would, easily scaling over 2 blocks if necessary. (Easily jumps fences when sprinting.)
// - Upon its death, it drops their Venison (cooked if killed in flames.)
// - Deer can be bred with apples.
// Deer always stick together in groups, but wander around if they're alone.
// It usually grazes the grasses.
// Occasionally, Deer with antlers will start headbutting to assert dominance.
// Usually it results in one of them retreating away.
// There's a chance that one of the deer’s antlers gets dislodged, resulting in them retreating.
// When fed with golden apples, it will start regrowing their antler by instant.
// Green sparkle then emits as if it grew a crop.
// They're usually found in groups of 3 to 6 deer
//TODO: ADD THIS ALONGSIDE SLEDS IN FUTURE UPDATES (If someone sees this on Github, take it as a little sneek peak, hi)
// Feeding it with Enchanted Golden Apple causes it to become Golden Deer.
// It gives them the ability to levitate (switch new land & fly like an eagle), which will happen randomly and when it goes panicked.
// They do not get new skin, but instead their textures will glow.
// Gains a particle trail.
public class DeerEntity extends Animal implements VariantHolder<DeerEntity.Variant>, GeoEntity, DetectsFartherAway {

    //──────────────────────────────────── Variants ────────────────────────────────────
    public enum Variant implements StringRepresentable {
        FALLOW(0, "fallow"),
        WHITETAIL(1, "whitetail"),
        REINDEER(2, "reindeer"),
        MUSK(3, "musk");

        public static final Codec<DeerEntity.Variant> CODEC = StringRepresentable.fromEnum(DeerEntity.Variant::values);

        private static final IntFunction<DeerEntity.Variant> BY_ID = ByIdMap.continuous(DeerEntity.Variant::getId, values(),
                ByIdMap.OutOfBoundsStrategy.CLAMP);

        public static final Map<Variant, Item> ANTLERS = Map.of(
                FALLOW, Primal_Items.FALLOW_DEER_ANTLER.get(),
                WHITETAIL, Primal_Items.WHITETAIL_DEER_ANTLER.get(),
                REINDEER, Primal_Items.REINDEER_ANTLER.get()
        );

        public static DeerEntity.Variant byId(int id) {
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
    public void setVariant(@NotNull DeerEntity.Variant variant) {
        this.entityData.set(DATA_VARIANT_ID, variant.id);
    }

    @Override
    public @NotNull DeerEntity.Variant getVariant() {
        return DeerEntity.Variant.byId(this.entityData.get(DATA_VARIANT_ID));
    }

    public void setVariantFromBiome(DeerEntity animal, Holder<Biome> holder) {
        if (holder.is(Primal_Tags.Biome.SPAWNS_MUSK_DEER)) {
            animal.setVariant(DeerEntity.Variant.MUSK);
        } else if (holder.is(Primal_Tags.Biome.SPAWNS_REINDEER) || holder.value().coldEnoughToSnow(this.getOnPos())) {
            animal.setVariant(DeerEntity.Variant.REINDEER);
        } else if (holder.is(Primal_Tags.Biome.SPAWNS_WHITETAIL_DEER)) {
            animal.setVariant(DeerEntity.Variant.WHITETAIL);
        } else {
            animal.setVariant(DeerEntity.Variant.FALLOW);
        }
    }

    @Override
    protected @NotNull Component getTypeName() {
        return Primal_Util.getCustomVariantName(this, super.getTypeName(), DeerEntity.Variant.REINDEER);
    }

    //──────────────────────────────────── Init ────────────────────────────────────
    public DeerEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new DeerMoveControl(this);
        this.lookControl = new DeerLookControl(this);
        this.getNavigation().setCanFloat(true);
        this.gameEventHandler = new DynamicGameEventListener<>(new DeerEntity.ScarySoundsListener(this, new EntityPositionSource(this, this.getEyeHeight()), 16));
        this.setMaxUpStep(1.0f);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 15f)
                .add(Attributes.MOVEMENT_SPEED, 0.24f)
                .add(Attributes.JUMP_STRENGTH, 0.65f)
                .add(Attributes.ATTACK_KNOCKBACK, 1.2f);
    }

    @Override
    protected float getJumpPower() {
        return this.getJumpPower(1.0F);
    }

    protected float getJumpPower(float multiplier) {
        return (float)this.getAttributeValue(Attributes.JUMP_STRENGTH) * multiplier * this.getBlockJumpFactor() + this.getJumpBoostPower();
    }

    @Override
    protected int calculateFallDamage(float f, float f2) {
        return Mth.ceil((f - 8.0F) * f2);
    }

    private static final EntityDataAccessor<Integer> DATA_VARIANT_ID = SynchedEntityData.defineId(DeerEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_JUMPING = SynchedEntityData.defineId(DeerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> ALERT_OTHERS_TIME = SynchedEntityData.defineId(DeerEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> RIGHT_ANTLER = SynchedEntityData.defineId(DeerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> LEFT_ANTLER = SynchedEntityData.defineId(DeerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> TRUSTING = SynchedEntityData.defineId(DeerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HAS_NATURAL_ANTLERS = SynchedEntityData.defineId(DeerEntity.class, EntityDataSerializers.BOOLEAN);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_VARIANT_ID, DeerEntity.Variant.FALLOW.id);
        this.entityData.define(IS_JUMPING, false);
        this.entityData.define(ALERT_OTHERS_TIME, 0);
        this.entityData.define(RIGHT_ANTLER, true);
        this.entityData.define(LEFT_ANTLER, true);
        this.entityData.define(TRUSTING, false);
        this.entityData.define(HAS_NATURAL_ANTLERS, true);
    }

    public boolean isJumping() {
        return this.entityData.get(IS_JUMPING);
    }

    public void setIsJumping(boolean jumping) {
        this.entityData.set(IS_JUMPING, jumping);
    }

    public int getAlertOthersTime() {
        return this.entityData.get(ALERT_OTHERS_TIME);
    }

    public void setAlertOthersTime(int alertOthersTime) {
        this.entityData.set(ALERT_OTHERS_TIME, alertOthersTime);
    }

    public boolean hasRightAntler() {
        return this.entityData.get(RIGHT_ANTLER);
    }

    public void setRightAntler(boolean rightAntler) {
        this.entityData.set(RIGHT_ANTLER, rightAntler);
    }

    public boolean hasLeftAntler() {
        return this.entityData.get(LEFT_ANTLER);
    }

    public void setLeftAntler(boolean leftAntler) {
        this.entityData.set(LEFT_ANTLER, leftAntler);
    }

    public void setAntlers(boolean hasRightAntler, boolean hasLeftAntler) {
        this.setRightAntler(hasRightAntler);
        this.setLeftAntler(hasLeftAntler);
    }

    public boolean hasNaturalAntlers() {
        return this.entityData.get(HAS_NATURAL_ANTLERS);
    }

    public void setNaturalAntlers(boolean setNaturalAntlers) {
        this.entityData.set(HAS_NATURAL_ANTLERS, setNaturalAntlers);
    }

    public boolean isTrusting() {
        return this.entityData.get(TRUSTING);
    }

    public void setTrusting(boolean trusting) {
        this.entityData.set(TRUSTING, trusting);
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        if (spawnGroupData == null)
            spawnGroupData = new AgeableMob.AgeableMobGroupData(0.25f);

        DeerAi.initMemories(this, this.level().random);
        setVariantFromBiome(this, level.getBiome(this.blockPosition()));
        if(level.getRandom().nextBoolean()){
            this.setAntlers(false, false);
            this.setNaturalAntlers(false);
        }

        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData, compoundTag);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant().id);
        compound.putInt("AlertOthersTime", this.getAlertOthersTime());
        compound.putBoolean("HasRightAntler", this.hasRightAntler());
        compound.putBoolean("HasLeftAntler", this.hasLeftAntler());
        compound.putBoolean("HasNaturalAntlers", this.hasNaturalAntlers());
        compound.putBoolean("IsTrusting", this.isTrusting());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(DeerEntity.Variant.byId(compound.getInt("Variant")));
        this.setAlertOthersTime(compound.getInt("AlertOthersTime"));
        this.setAntlers(
                compound.getBoolean("HasRightAntler"),
                compound.getBoolean("HasLeftAntler"));
        this.setNaturalAntlers(compound.getBoolean("HasNaturalAntlers"));
        this.setTrusting(compound.getBoolean("IsTrusting"));
    }

    //──────────────────────────────────── AI & Movement ────────────────────────────────────
    @Override
    protected void registerGoals() {}

    @Override
    protected @NotNull Brain.Provider<DeerEntity> brainProvider() {
        return DeerAi.brainProvider();
    }

    @Override
    protected @NotNull Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
        return DeerAi.makeBrain(this.brainProvider().makeBrain(dynamic));
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull Brain<DeerEntity> getBrain() {
        return (Brain<DeerEntity>) super.getBrain();
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new DeerPathNavigation(this, level);
    }

    private boolean wasOnGround;
    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();

        //Starts the brain
        Brain<DeerEntity> brain = this.getBrain();
        this.level().getProfiler().push("deerBrain");
        brain.tick((ServerLevel)this.level(), this);
        this.level().getProfiler().pop();
        this.level().getProfiler().push("deerActivityUpdate");
        DeerAi.updateActivity(this);
        this.level().getProfiler().pop();

        //Makes it sprint when avoiding on land
        if(!this.isInWater())
            this.setSprinting(brain.getMemory(MemoryModuleType.AVOID_TARGET).isPresent()
                    || brain.getMemory(MemoryModuleType.NEAREST_REPELLENT).isPresent()
                    || brain.getMemory(Primal_MemoryModuleTypes.NEAREST_PLAY_MOB.get()).isPresent());
        if(this.isSprinting() && this.isInWater())
            this.setSprinting(false);

        //Reduce time
        if(getAlertOthersTime()>0)
            this.setAlertOthersTime(getAlertOthersTime()-1);

        //Smooth jumping transition
        if (this.isJumping() || !this.onGround()) {
            double vy = this.getDeltaMovement().y;

            float targetPitch = (float)(-vy * (80.0F)); // scale factor
            targetPitch = Mth.clamp(targetPitch, -70F, 70F);

            this.setXRot(Mth.lerp(0.5F, this.getXRot(), targetPitch));
        } else {
            this.setXRot(Mth.lerp(0.5F, this.getXRot(), 0F));
        }

        //Makes them run when thundering, it just set the repellent to its feet
        if(this.level().isThundering()){
            if(this.getBrain().getMemory(MemoryModuleType.NEAREST_REPELLENT).isEmpty())
                this.getBrain().setMemoryWithExpiry(
                        MemoryModuleType.NEAREST_REPELLENT,
                        this.getOnPos(),
                        TimeUtil.rangeOfSeconds(2, 3).sample(this.getRandom()));
        }
    }

    @Override
    public float getSpeed() {
        return this.isSprinting()? super.getSpeed()*1.4f : super.getSpeed();
    }

    public static int SCARED_DISTANCE = 6;
    public static int EXTRA_SCARED_DISTANCE = 16;
    public boolean canBeScared(LivingEntity target) {
        //To be already scared of scared
        if(this.getBrain().isMemoryValue(MemoryModuleType.AVOID_TARGET, target)) return true;

        //No scared of tamed animals only if trusted
        if(target instanceof TamableAnimal tamableAnimal && tamableAnimal.isTame() && this.isTrusting()) return false;

        //No scared of players only if trusted, or they are holding an apple
        if(target instanceof Player player &&
                (this.isTrusting()
                || isMatingFood(player.getItemInHand(InteractionHand.MAIN_HAND))
                        || isMatingFood(player.getItemInHand(InteractionHand.OFF_HAND))))
            return false;

        //Entities from which runs farther away, or if it is sprinting as long as is not another deer
        if((target.getType().is(Primal_Tags.Entity.DEER_VERY_SCARED) || (target.isSprinting() && !(target instanceof DeerEntity)))){
            return target.distanceTo(this) < EXTRA_SCARED_DISTANCE;
        }

        return target.getType().is(Primal_Tags.Entity.DEER_SCARED)
                //6 distance by default
                && target.distanceTo(this)<SCARED_DISTANCE;
    }

    public static int CAUTIOUS_DISTANCE = 10;
    public boolean canBeCautious(LivingEntity target) {
        //No scared of tamed animals only if trusted
        if(target instanceof TamableAnimal tamableAnimal && tamableAnimal.isTame() && this.isTrusting()) return false;

        //No scared of players only if trusted
        if(target instanceof Player && this.isTrusting()) return false;

        return target.getType().is(Primal_Tags.Entity.DEER_SCARED)
                && target.distanceTo(this)<CAUTIOUS_DISTANCE
                && !target.isCrouching();
    }

    public boolean canHeadbutt(LivingEntity target) {
        if (this.getBrain().isMemoryValue(Primal_MemoryModuleTypes.NEAREST_PLAY_MOB.get(), target)
                && target.getBrain().isMemoryValue(Primal_MemoryModuleTypes.NEAREST_PLAY_MOB.get(), this))
            return true;

        if (!(target instanceof DeerEntity otherDeer)) return false;

        // Musk deer and babies don't headbutt
        if (this.getVariant() == Variant.MUSK || otherDeer.getVariant() == Variant.MUSK || this.isBaby() || otherDeer.isBaby())
            return false;

        // Self checks
        if (this.getBrain().getMemory(Primal_MemoryModuleTypes.LUNGE_COOLDOWN.get()).isPresent()
                || !this.hasLeftAntler() || !this.hasRightAntler()
                || this.isLeashed()
                || !this.getBrain().isActive(Activity.IDLE))
            return false;

        boolean thisCan = target.distanceTo(this) < 20 && target.distanceTo(this) > 10;

        boolean otherCan =
                otherDeer.hasLeftAntler()
                        && otherDeer.hasRightAntler()
                        && otherDeer.getBrain().getMemory(Primal_MemoryModuleTypes.LUNGE_COOLDOWN.get()).isEmpty()
                        && !otherDeer.isLeashed()
                        && otherDeer.getBrain().isActive(Activity.IDLE);

        if (thisCan && otherCan
                && otherDeer.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_PLAY_MOB.get()).isEmpty()) {
            otherDeer.getBrain().setMemory(Primal_MemoryModuleTypes.NEAREST_PLAY_MOB.get(), this);
        }

        return thisCan && otherCan;
    }

    public boolean refuseToMove() {
        return hasPose(Pose.SITTING);
    }

    private final DynamicGameEventListener<DeerEntity.ScarySoundsListener> gameEventHandler;
    @Override
    public void updateDynamicGameEventListener(@NotNull final BiConsumer<DynamicGameEventListener<?>, ServerLevel> callback) {
        final Level world = this.level();
        if (world instanceof final ServerLevel serverWorld) {
            callback.accept(this.gameEventHandler, serverWorld);
        }
    }

    //──────────────────────────────────── Combat ────────────────────────────────────
    @Nullable
    @Override
    public LivingEntity getTarget() {
        return Primal_Util.OneTwentyEquivalent.getTargetFromBrain(this);
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        boolean hurt = super.hurt(source, amount);

        if(hurt){
            this.getBrain().setMemoryWithExpiry(Primal_MemoryModuleTypes.WAS_IDLE_ANIMATION.get(), true, 40);

            if (source.getEntity() instanceof LivingEntity target){
                Primal_Util.Ai.wasHurtByAndAvoids(this, target, DeerEntity.class, true, true, DeerAi.RETREAT_DURATION);
                this.setAlertOthersTime(Primal_Util.toTicks(10));

                //Removes trusting
                if(source.getEntity() instanceof Player && this.isTrusting()) this.setTrusting(false);
            }
        }

        return hurt;
    }

    //──────────────────────────────────── Feeding & Interaction ────────────────────────────────────
    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stackInHand = player.getItemInHand(hand);
        if(this.isFood(stackInHand)){
            boolean wasFeed = this.handleEating(player, stackInHand);
            if (wasFeed) {
                this.usePlayerItem(player, hand, stackInHand);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    public boolean handleEating(@NotNull Player player, @NotNull ItemStack stack) {
        if (this.isFood(stack)) {
            if(isHealFood(stack) && (!this.hasRightAntler() || !this.hasLeftAntler())) {
                if(!this.hasNaturalAntlers())
                    this.setNaturalAntlers(true);
                //Regrow antlers
                this.setAntlers(true, true);
                this.level().broadcastEntityEvent(this, (byte)22);
                this.setTrusting(true);
                return playEatingSound();
            }

            //To try to mate
            if (isMatingFood(stack)) {
                //Breeding
                if (this.getAge() == 0 && this.canFallInLove()) {
                    this.setInLove(player);
                    this.setTrusting(true);
                    return playEatingSound();
                }
                //AgeUp
                else if (this.isBaby()) {
                    int i = this.getAge();
                    this.ageUp(getSpeedUpSecondsWhenFeeding(-i), true);
                    this.setTrusting(true);
                    return playEatingSound();
                }
            }
        }

        return false;
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return isMatingFood(stack) || isHealFood(stack);
    }

    public static boolean isMatingFood(@NotNull ItemStack stack){
        return stack.is(Items.APPLE);
    }

    public static boolean isHealFood(@NotNull ItemStack stack){
        return stack.is(Items.GOLDEN_APPLE);
    }

    @Override
    protected void usePlayerItem(@NotNull Player player, @NotNull InteractionHand hand, @NotNull ItemStack stack) {
        super.usePlayerItem(player, hand, stack);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob otherParent) {
        if(otherParent instanceof DeerEntity otherParentCasted){
            var baby = Primal_Util.createFromParents(Primal_Entities.DEER.get(),
                    this,
                    otherParentCasted, c-> DeerAi.initMemories(c, level.getRandom()));

            //Makes the baby trusting if both parents are trusting
            if(this.isTrusting() && otherParentCasted.isTrusting())
                baby.setTrusting(true);

            return baby;
        }


        return null;
    }

    @Override
    public void heal(float healAmount) {
        super.heal(healAmount);
    }

    @Override
    public void thunderHit(@NotNull ServerLevel level, @NotNull LightningBolt lightning) {
        super.thunderHit(level, lightning);
        this.spawnAtLocation(Primal_Items.MUSIC_DISC_OH_DEER.get(), 0);
        this.level().playSound(null, this.blockPosition(), SoundEvents.FIRE_EXTINGUISH, this.getSoundSource(), 1.0F, 1.0F);
        this.level().broadcastEntityEvent(this, (byte)38);

        //Triggers advancement
        List<ServerPlayer> playersList= this.level().getEntitiesOfClass(ServerPlayer.class, this.getBoundingBox().inflate(16));
        if(!playersList.isEmpty()){
            playersList.forEach(Primal_Advancements.DEER_DISC::trigger);
        }

        DelayedTasks.runLater(1, this::discard);
    }

    //──────────────────────────────────── GeckoLib & Visuals ────────────────────────────────────
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(DeerAnimations.mainController(this));
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 38){
            Primal_Util.Visuals.addParticlesAroundSelf(this, ParticleTypes.POOF, 0.02, 12, 0, 0.2, 1);
            Primal_Util.Visuals.addParticlesAroundSelf(this, ParticleTypes.LAVA, 0.02, 12, 0, 0.2, 1);
        } else if (id == 22){
            Primal_Util.Visuals.addParticlesAroundSelf(this, ParticleTypes.HAPPY_VILLAGER, 0.02, 12, 1, 0.5, 0.5);
        }  else if (id == 36){
            Primal_Util.Visuals.addParticlesAroundSelf(this,
                    new BlockParticleOption(ParticleTypes.BLOCK, Blocks.BONE_BLOCK.defaultBlockState()),
                    0.02, 12, 0, 0.2, 1);
        }
        else
            super.handleEntityEvent(id);
    }

    //──────────────────────────────────── Misc ────────────────────────────────────
    public static boolean checkDeerSpawnRules(
            EntityType<? extends Animal> animal, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random
    ) {
        return level.getBlockState(pos.below()).is(Primal_Tags.Block.DEER_SPAWN_ON) && isBrightEnoughToSpawn(level, pos);
    }

    @Override
    public boolean canBeLeashed(@NotNull Player player) {
        return super.canBeLeashed(player) && !hasPose(Pose.SITTING);
    }

    @Override
    protected @NotNull Vec3 getLeashOffset() {
        return new Vec3(0.0, this.getEyeHeight()*0.6f, this.getBbWidth() * 0.2F);
    }

    @Override
    public boolean isEntityTargetable(LivingEntity attacker, LivingEntity target) {
        return attacker.getBrain().isMemoryValue(MemoryModuleType.ATTACK_TARGET, target)
                ? Primal_Util.Ai.getTargetConditions(24, true).test(attacker, target)
                : Primal_Util.Ai.getTargetConditions(24, false).test(attacker, target);
    }

    @Override
    public boolean isEntityAttackable(LivingEntity attacker, LivingEntity target) {
        return attacker.getBrain().isMemoryValue(MemoryModuleType.ATTACK_TARGET, target)
                ? Primal_Util.Ai.getTargetConditions(24, true).test(attacker, target)
                : Primal_Util.Ai.getTargetConditions(24, false).test(attacker, target);
    }

    //──────────────────────────────────── Sounds ────────────────────────────────────
    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return Primal_Sounds.DEER_IDLE.get();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return Primal_Sounds.DEER_HURT.get();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return Primal_Sounds.DEER_DEATH.get();
    }

    public SoundEvent getEatingSound() {
        return Primal_Sounds.DEER_EAT.get();
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

    public static class ScarySoundsListener implements GameEventListener {
        private final DeerEntity deer;
        private final PositionSource listenerSource;
        private final int listenerRadius;

        public ScarySoundsListener(DeerEntity deer, PositionSource listenerSource, int listenerRadius) {
            this.listenerSource = listenerSource;
            this.listenerRadius = listenerRadius;
            this.deer = deer;
        }

        @Override
        public @NotNull PositionSource getListenerSource() {
            return this.listenerSource;
        }

        @Override
        public int getListenerRadius() {
            return this.listenerRadius;
        }

        private final UniformInt ESCAPE_TIME = TimeUtil.rangeOfSeconds(5, 10);

        @Override
        public boolean handleGameEvent(@NotNull ServerLevel level, GameEvent gameEvent, GameEvent.@NotNull Context context, @NotNull Vec3 pos) {
            if (gameEvent.is(Primal_Tags.GameEvent.SCARE_DEER)) {
                //Alert other deer per 5s
                deer.setAlertOthersTime(Primal_Util.toTicks(5));
                deer.getBrain().setMemoryWithExpiry(MemoryModuleType.NEAREST_REPELLENT, BlockPos.containing(pos), ESCAPE_TIME.sample(level.getRandom()));
                return true;
            } else {
                return false;
            }
        }
    }
}