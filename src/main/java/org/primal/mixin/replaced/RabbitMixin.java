package org.primal.mixin.replaced;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.primal.entity.replaced.RabbitReplaced;
import org.primal.registry.Primal_Tags;
import org.primal.util.mob_types.ReplacedEntityNewVariantHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Rabbit.class)
public abstract class RabbitMixin extends Animal implements VariantHolder<Rabbit.Variant>, ReplacedEntityNewVariantHolder<RabbitReplaced.PrimalVariant>, RabbitReplaced {

    protected RabbitMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }
    @Unique Rabbit p$THIS = (Rabbit)(Object)this;

    @Unique
    private static final EntityDataAccessor<Integer> PRIMAL$DATA_VARIANT_ID = SynchedEntityData.defineId(RabbitMixin.class, EntityDataSerializers.INT);

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void primal$rabbitVariantSynchedData(final CallbackInfo ci){
        this.entityData.define(PRIMAL$DATA_VARIANT_ID, RabbitReplaced.PrimalVariant.NONE.getId());
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "finalizeSpawn", at = @At("TAIL"))
    private void primal$rabbitFinalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, SpawnGroupData spawnGroupData, CompoundTag tag, CallbackInfoReturnable<SpawnGroupData> cir){
        this.primal$setVariantFromBiome((ReplacedEntityNewVariantHolder<RabbitReplaced.PrimalVariant>) p$THIS, level.getBiome(this.blockPosition()));
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void primal$rabbitAddAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        compound.putInt("PrimalVariant", this.primal$getVariant().getId());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void primal$rabbitReadAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        this.primal$setVariant(RabbitReplaced.PrimalVariant.byId(compound.getInt("PrimalVariant")));
    }

    @Override
    public <M extends ReplacedEntityNewVariantHolder<RabbitReplaced.PrimalVariant>> void primal$setVariantFromBiome(M animal, Holder<Biome> holder) {
        if (holder.is(Primal_Tags.Biome.SPAWNS_RABBIT_GRAY))
            animal.primal$setVariant(RabbitReplaced.PrimalVariant.GRAY);
        else if(holder.is(Primal_Tags.Biome.SPAWNS_RABBIT_SAND))
            animal.primal$setVariant(RabbitReplaced.PrimalVariant.SAND);
        else
            animal.primal$setVariant(RabbitReplaced.PrimalVariant.NONE);
    }

    @Override
    public void primal$setVariant(RabbitReplaced.PrimalVariant variant) {
        this.entityData.set(PRIMAL$DATA_VARIANT_ID, variant.getId());
    }

    @Override
    public RabbitReplaced.PrimalVariant primal$getVariant() {
        return RabbitReplaced.PrimalVariant.byId(this.entityData.get(PRIMAL$DATA_VARIANT_ID));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Inject(method = "getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/animal/Rabbit;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Rabbit;setVariant(Lnet/minecraft/world/entity/animal/Rabbit$Variant;)V"))
    public void primal$addPrimalVariantOnBreed(ServerLevel level, @Nullable AgeableMob otherParent, CallbackInfoReturnable<Rabbit> cir, @Local(ordinal= 1) Rabbit rabbit) {
        var babyVariantHolder = rabbit instanceof ReplacedEntityNewVariantHolder babyVariant? babyVariant: null;

        if(babyVariantHolder != null) {
            var parentVariant = p$THIS instanceof ReplacedEntityNewVariantHolder<?> casted && !casted.primal$getVariant().equals(PrimalVariant.NONE)? casted.primal$getVariant(): null;
            var otherParentVariant = otherParent instanceof ReplacedEntityNewVariantHolder<?> casted && !casted.primal$getVariant().equals(PrimalVariant.NONE)? casted.primal$getVariant(): null;

            //Only main parent has primal variant
            if(parentVariant != null && otherParentVariant == null) {
                //If it is the only parent, it directly assigns it
                if(otherParent == null)
                    babyVariantHolder.primal$setVariant(parentVariant);
                //If it has other parent, it has 50% probability of being a primal variant
                else if(this.random.nextBoolean())
                    babyVariantHolder.primal$setVariant(parentVariant);
            }

            //If the other parent has a variant but the normal parent not, 50% probability
            if(parentVariant==null && otherParentVariant!=null){
                if(this.random.nextBoolean())
                    babyVariantHolder.primal$setVariant(otherParentVariant);
            }

            //If both parents have a primal variant, 50% probability of the two
            if(parentVariant != null && otherParentVariant != null){
                babyVariantHolder.primal$setVariant(this.random.nextBoolean()? parentVariant: otherParentVariant);
            }
        }
    }

    //──────────────────────────────────── Animations ────────────────────────────────────
    @Unique
    public final AnimationState primal$idleAnimationState = new AnimationState();
    @Unique
    public final AnimationState primal$rearAnimationState = new AnimationState();
    @Unique
    public final AnimationState primal$hopAnimationState = new AnimationState();

    @Override
    public AnimationState primal$idleAnimationState() {
        return primal$idleAnimationState;
    }

    @Override
    public AnimationState primal$rearAnimationState() {
        return primal$rearAnimationState;
    }

    @Override
    public AnimationState primal$hopAnimationState() {
        return primal$hopAnimationState;
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void primal$addAnimationIntoTick(CallbackInfo ci) {
        if(this.level().isClientSide()) {
            this.primal$idleAnimationState.animateWhen(true, this.tickCount);

            this.primal$rearAnimationState.animateWhen(primal$isRearing, this.tickCount);
        }
    }

    @Unique private boolean primal$isRearing = false;
    @Unique private long primal$rearEndTick = 0L;
    @Unique private long primal$rearCooldownEndTick = 0L;

    @Unique private static final int REAR_DURATION_TICKS = 50; // 2.5s
    @Unique private static final int REAR_COOLDOWN_TICKS = 200; // 10s
    @Unique private static final float MOVE_EPSILON = 0.0025F;
    @Unique private long primal$lastMovingTick = 0L;
    @Unique private static final int IDLE_REQUIRED_TICKS = 40; // 2s

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void primal$rearLogic(CallbackInfo ci) {
        //Only triggers on client and not in water
        if (!this.level().isClientSide || this.isInWater()) return;

        long gameTime = this.level().getGameTime();

        boolean isMoving =
                !this.getNavigation().isDone()
                        || this.getDeltaMovement().horizontalDistanceSqr() > MOVE_EPSILON
                        || !this.onGround()
                        || this.isInWater();

        // Track last movement time
        if (isMoving)
            primal$lastMovingTick = gameTime;

        //Cancel if rearing but forced
        if (primal$isRearing) {
            boolean wantsToMove =
                    isMoving || this.getTarget() != null;

            if (wantsToMove || gameTime >= primal$rearEndTick) {
                primal$stopRear();
            } else {
                this.getNavigation().stop();
                this.setDeltaMovement(Vec3.ZERO);
            }
            return;
        }

        //Must be idle for 2s
        if (gameTime - primal$lastMovingTick < IDLE_REQUIRED_TICKS) return;

        //Normal trigger conditions
        if (gameTime < primal$rearCooldownEndTick) return;

        // Random chance (unchanged)
        if (this.random.nextInt(160) != 0) return;

        primal$startRear(gameTime);
    }


    @Unique
    private void primal$startRear(long gameTime) {
        primal$isRearing = true;
        primal$rearEndTick = gameTime + REAR_DURATION_TICKS;
        primal$rearCooldownEndTick = gameTime + REAR_COOLDOWN_TICKS;

        this.getNavigation().stop();
        this.setDeltaMovement(Vec3.ZERO);
    }

    @Unique
    private void primal$stopRear() {
        primal$isRearing = false;
    }
}