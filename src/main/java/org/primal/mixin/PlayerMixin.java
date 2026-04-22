package org.primal.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.primal.entity.animal.SnakeEntity;
import org.primal.injection.SetNeckEntity;
import org.primal.registry.Primal_Entities;
import org.primal.registry.Primal_Particles;
import org.primal.util.Primal_Util;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements net.neoforged.neoforge.common.extensions.IPlayerExtension, SetNeckEntity {
    @Shadow @Final private Abilities abilities;

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {super(entityType, level);}

    @Unique
    Player p$THIS = (Player)(Object)this;

    @Unique
    private static final EntityDataAccessor<CompoundTag> primal$neckEntity = SynchedEntityData.defineId(PlayerMixin.class, EntityDataSerializers.COMPOUND_TAG);

    @Override
    public CompoundTag primal$getNeckEntity() {
        return this.entityData.get(primal$neckEntity);
    }

    @Override
    public void primal$setNeckEntity(CompoundTag entityCompound) {
        this.entityData.set(primal$neckEntity, entityCompound);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void primal$neckEntitySynchedData(final SynchedEntityData.Builder builder, final CallbackInfo ci){
        builder.define(primal$neckEntity, new CompoundTag());
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void primal$playerAddAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        if (!this.primal$getNeckEntity().isEmpty()) {
            compound.put("PrimalNeckEntity", this.primal$getNeckEntity());
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void primal$playerReadAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        if (compound.contains("PrimalNeckEntity", 10)) {
            this.primal$setNeckEntity(compound.getCompound("PrimalNeckEntity"));
        }
    }

    @Unique
    private long primal$timeEntitySatOnNeck;
    @Override
    public void primal$setTimeEntitySatOnNeck(long gameTime) {
        this.primal$timeEntitySatOnNeck=gameTime;
    }

    @Override
    public long primal$getTimeEntitySatOnNeck() {
        return primal$timeEntitySatOnNeck;
    }

    @Inject(method = "setEntityOnShoulder", at = @At("HEAD"), cancellable = true)
    private void primal$hasEntityOnNeckSoAvoidShoulders(CompoundTag entityCompound, CallbackInfoReturnable<Boolean> cir){
        if(!this.primal$getNeckEntity().isEmpty())
            cir.setReturnValue(false);
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void primal$neckEntityAiStep(CallbackInfo ci) {
        this.playNeckEntityAmbientSound(this.primal$getNeckEntity());

        //Removes automatically if on water/flying/sleeping/powder snow/crouching
        if (!primal$getNeckEntity().isEmpty() && !this.level().isClientSide && (this.isInWater()) || this.isSleeping() || this.isInPowderSnow || this.isCrouching()) {
            this.removeEntitiesOnNeck();
        }

        //Shedding particles for the snake on neck
        if(primal$isSnakeOnNeck()){
            if(this.primal$getNeckEntity().getBoolean("IsShedding") && this.tickCount%20==0){
                Primal_Util.Visuals.addParticleAboveSelf(p$THIS, Primal_Particles.SNAKE_SKIN_FLAKE,
                        0.05,
                        this.getRandom().nextIntBetweenInclusive(1, 3),
                        0.15);
            }
        }
    }

    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getKnockback(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;)F"))
    private void primal$setSnakeEffect(Entity target, CallbackInfo ci){
        if(target instanceof LivingEntity living){
            if(primal$isSnakeOnNeck()){
                //Avoid baby applying effect
                if(this.primal$getNeckEntity().getInt("Age")<0) return;

                var snakeEffect = SnakeEntity.SnakeEffect.byId(this.primal$getNeckEntity().getInt("SnakeEffect"));
                living.addEffect(snakeEffect.getEffect(), this);
            }
        }
    }

    @Unique
    private boolean primal$isSnakeOnNeck(){
        return !this.primal$getNeckEntity().isEmpty()
                && EntityType.byString(this.primal$getNeckEntity().getString("id")).isPresent()
                && EntityType.byString(this.primal$getNeckEntity().getString("id")).get().equals(Primal_Entities.SNAKE.get());
    }
}
