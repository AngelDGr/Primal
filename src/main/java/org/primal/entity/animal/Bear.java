package org.primal.entity.animal;

import java.util.List;
import java.util.function.IntFunction;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;
import org.primal.client.animation.entity.BearAnimations;
import org.primal.entity.ai.BearAi;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;

import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.VariantHolder;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.Brain.Provider;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Bear extends AbstractChestedHorse implements VariantHolder<Bear.Variant>, GeoEntity {

    public static enum Variant implements StringRepresentable {
        GRIZZLY(0, "grizzly"),
        ASIATIC(1, "asiatic");

        public static final Codec<Variant> CODEC = StringRepresentable.fromEnum(Variant::values);
        private static final IntFunction<Variant> BY_ID = ByIdMap.continuous(Variant::getId, values(),
                ByIdMap.OutOfBoundsStrategy.CLAMP);

        public static Variant byId(int id) {
            return BY_ID.apply(id);
        }

        final int id;

        private final String name;

        private Variant(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return this.id;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

    }

    private static final EntityDataAccessor<Integer> DATA_VARIANT_ID = SynchedEntityData.defineId(Bear.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_HONEY_COUNTER = SynchedEntityData.defineId(Bear.class, EntityDataSerializers.INT);

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 100f)
                .add(Attributes.MOVEMENT_SPEED, .25f)
                .add(Attributes.ATTACK_DAMAGE, 10f)
                .add(Attributes.ATTACK_KNOCKBACK, 2.5f);
    }

    private boolean isSleeping;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public Bear(EntityType<Bear> entityType, Level level) {
        super(entityType, level);
        this.setHealth(this.getMaxHealth());
    }

    @Override
    public void onPlayerJump(int jumpPower) {
        jumpPower = jumpPower/50;
        this.setDeltaMovement(this.getDeltaMovement().add(this.getForward().multiply(jumpPower, 0, jumpPower)));
    }

    @Override
    protected boolean canRide(Entity vehicle) {
        return super.canRide(vehicle) && !this.isBaby();
    }

    public boolean getSleeping() {
        return isSleeping;
    }

    public void setSleeping(boolean isSleeping) {
        this.isSleeping = isSleeping;
    }

    public int getHoneyCounter() {
        return this.entityData.get(DATA_HONEY_COUNTER);
    }

    public void setHoneyCounter(int value) {
        this.entityData.set(DATA_HONEY_COUNTER, value);
    }

    @SuppressWarnings("null")
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant().id);
        compound.putInt("HoneyCounter", this.getHoneyCounter());
        compound.putBoolean("IsSleeping", this.isSleeping);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(Variant.byId(compound.getInt("Variant")));
        this.setHoneyCounter(compound.getInt("HoneyCounter"));
        this.isSleeping = compound.getBoolean("IsSleeping");
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        Holder<Biome> holder = level.getBiome(this.blockPosition());
        if (holder.is(BiomeTags.IS_FOREST) && holder.is(BiomeTags.SPAWNS_WARM_VARIANT_FROGS)) { // your bear is a little bit a frog
            this.setVariant(Variant.ASIATIC);
        } else {
            this.setVariant(Variant.GRIZZLY);
        }
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            if (this.tickCount % (20 * 15) == 0 && this.getRandom().nextBoolean()) {
                this.triggerAnim("alt_idle", "alt_idle");
            }
        } else {
            if (this.getHoneyCounter() > 0) {
                this.setHoneyCounter(this.getHoneyCounter() - 1);
            }
            if (this.refuseToMove()) {
                this.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
                this.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
            }
        }
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        controllers.add(BearAnimations.mainController(this)
                .receiveTriggeredAnimations()
                .triggerableAnim("roar", BearAnimations.ROAR)
                .triggerableAnim("beg_end", RawAnimation.begin().thenPlay("animation.grizzly_bear.beg_end"))
                .triggerableAnim("sleep_start", RawAnimation.begin().thenPlay("animation.grizzly_bear.sleep_start"))
                .triggerableAnim("sleep_end", RawAnimation.begin().thenPlay("animation.grizzly_bear.sleep_end")),
                new AnimationController<>(this, "alt_idle", state -> PlayState.STOP).triggerableAnim("alt_idle", BearAnimations.IDLE_ALT),
                new AnimationController<>(this, "attack", state -> PlayState.STOP).triggerableAnim("attack", BearAnimations.ATTACK).triggerableAnim("attack2", BearAnimations.ATTACK_ALT));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public boolean refuseToMove() {
        return ImmutableList.of(Pose.ROARING, Pose.SNIFFING, Pose.CROAKING).contains(this.getPose());
    }

    @Override
    public void setVariant(Variant variant) {
        this.entityData.set(DATA_VARIANT_ID, variant.id);
    }

    @Override
    public Variant getVariant() {
        return Variant.byId(this.entityData.get(DATA_VARIANT_ID));
    }

    @Override
    public void swing(InteractionHand hand) {
        this.triggerAnim("attack", this.getRandom().nextBoolean() ? "attack" : "attack2");
        super.swing(hand);
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        throw new UnsupportedOperationException("Unimplemented method 'getBreedOffspring'");
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(Items.HONEYCOMB)) {
            if (this.isBaby() && !this.isTamed()) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }

                if (this.random.nextInt(99) == 0) {
                    this.tameWithName(player);
                    this.navigation.stop();
                    this.level().broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level().broadcastEntityEvent(this, (byte) 6);
                }
                return InteractionResult.SUCCESS;
            } else if (this.isTamed()) {
                this.heal(10);
                stack.consume(1, player);
                return InteractionResult.CONSUME;
            }
        }
        if (!this.isBaby() && stack.is(ItemTags.FISHES)) {
            this.setInLove(player);
            stack.consume(1, player);
            return InteractionResult.CONSUME;
        }
        if (this.isTamed()) {
            if (player.isSecondaryUseActive()) {
                this.openCustomInventoryScreen(player);
                return InteractionResult.sidedSuccess(this.level().isClientSide());
            }
            if (!this.hasChest() && stack.is(Items.BARREL)) {
                this.setChest(true);
                this.playChestEquipsSound();
                stack.consume(1, player);
                this.createInventory();
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
            this.doPlayerRide(player);
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }
        return InteractionResult.PASS;
    }

    @Override
    protected float getRiddenSpeed(Player player) {
        return .1f;
    }

    @Override
    protected void randomizeAttributes(RandomSource random) {
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();

        Brain<?> brain = this.getBrain();
        ((Brain<Bear>) brain).tick((ServerLevel) this.level(), this);

        this.setSprinting(brain.getMemory(MemoryModuleType.ATTACK_TARGET).isPresent());

        BearAi.updateActivity(this);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT_ID, Variant.GRIZZLY.id);
        builder.define(DATA_HONEY_COUNTER, 0);
    }

    @Override
    protected Provider<Bear> brainProvider() {
        return BearAi.brainProvider();
    }

    @Override
    protected void registerGoals() {

    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return BearAi.makeBrain(this.brainProvider().makeBrain(dynamic));
    }

}
