package org.primal.mixin.replaced;

import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.VariantHolder;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
import org.primal.entity.replaced.RabbitReplaced;
import org.primal.registry.Primal_Tags;
import org.primal.util.mob_types.ReplacedEntityNewVariantHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import software.bernie.geckolib.animatable.GeoReplacedEntity;
import software.bernie.geckolib.util.RenderUtils;

@Mixin(Rabbit.class)
public abstract class RabbitMixin extends Animal implements VariantHolder<Rabbit.Variant>, ReplacedEntityNewVariantHolder<RabbitReplaced.PrimalVariant> {

    protected RabbitMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }
    @Unique Rabbit p$THIS = (Rabbit)(Object)this;
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
        //Only triggers on client and not in water and only if is actually replaced
        if (!this.level().isClientSide || this.isInWater() || !(RenderUtils.getReplacedAnimatable(this.getType()) instanceof GeoReplacedEntity anim)) return;

        long gameTime = this.level().getGameTime();

        boolean isMoving =
                !this.getNavigation().isDone()
                        || this.getDeltaMovement().horizontalDistanceSqr() > MOVE_EPSILON
                        || !this.onGround()
                        || this.isInWater();

        // Track last movement time
        if (isMoving) {
            anim.stopTriggeredAnimation(p$THIS, "base_controller", "rear");
            primal$lastMovingTick = gameTime;
        }

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

        if (RenderUtils.getReplacedAnimatable(this.getType()) instanceof GeoReplacedEntity anim) {
            anim.stopTriggeredAnimation(p$THIS, "base_controller", "rear");
            anim.triggerAnim(p$THIS, "base_controller", "rear");
        }
    }

    @Unique
    private void primal$stopRear() {
        primal$isRearing = false;
    }

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
}