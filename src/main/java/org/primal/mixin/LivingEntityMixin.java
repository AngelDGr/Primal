package org.primal.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.extensions.ILivingEntityExtension;
import org.primal.Primal_Main;
import org.primal.entity.animal.SharkEntity;
import org.primal.injection.IsEagleTarget;
import org.primal.registry.Primal_EntityAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable, ILivingEntityExtension, IsEagleTarget {

    @Shadow @Nullable public abstract AttributeInstance getAttribute(Holder<Attribute> attribute);

    @Shadow public abstract float getSpeed();

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/ai/attributes/AttributeMap;<init>(Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;)V"))
    private AttributeSupplier primal$modifyMobAttributes(AttributeSupplier supplier,
                                                      @Local(argsOnly = true) EntityType<? extends LivingEntity> entityType) {
        var builder = primal$getAttributeBuilder(supplier);
        builder.add(Primal_EntityAttributes.REFLECTED_DAMAGE);

        if (entityType.equals(EntityType.POLAR_BEAR)){
            //Optional health increase
            if(Primal_Main.COMMON_CONFIG.polarBearIncreasesHealth.get())
                builder.add(Attributes.MAX_HEALTH, 60);

            return builder
                    .add(Attributes.STEP_HEIGHT, 1.5f)
                    .build();
        }

        if(entityType.equals(EntityType.FOX) && Primal_Main.COMMON_CONFIG.foxIncreasesHealth.get())
            return builder
                    .add(Attributes.MAX_HEALTH, 15)
                    .build();

        if(entityType.equals(EntityType.WOLF) && Primal_Main.COMMON_CONFIG.wolfIncreasesHealth.get())
            return builder
                    .add(Attributes.MAX_HEALTH, 20)
                    .build();

        return builder.build();
    }

    @Unique
    private AttributeSupplier.Builder primal$getAttributeBuilder(AttributeSupplier supplier){
        AttributeSupplier.Builder builder = AttributeSupplier.builder();
        supplier.instances.forEach((attribute, instance) ->
                builder.add(attribute, instance.getBaseValue()));

        return builder;
    }

    @Unique
    LivingEntity p$THIS = (LivingEntity)(Object)this;

    @Unique
    private static final EntityDataAccessor<Optional<UUID>> primal$eagleAttacking = SynchedEntityData.defineId(LivingEntityMixin.class, EntityDataSerializers.OPTIONAL_UUID);

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void primal$IsEagleAttackingSynchedData(final SynchedEntityData.Builder builder, final CallbackInfo ci){
        builder.define(primal$eagleAttacking, Optional.empty());
    }

    @Override
    public Optional<UUID> primal$eagleAttacking() {
        return this.entityData.get(primal$eagleAttacking);
    }

    @Override
    public void primal$setEagleAttacking(@Nullable UUID eagleUUID) {
        if(eagleUUID==null){
            this.entityData.set(primal$eagleAttacking, Optional.empty());
        } else {
            this.entityData.set(primal$eagleAttacking, Optional.of(eagleUUID));
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void primal$removeEagle(CallbackInfo ci) {
        if(((IsEagleTarget) p$THIS).primal$eagleAttacking().isPresent()
                && !this.level().isClientSide
                //Erase the eagle value if its null or the eagle is dead
                && (((ServerLevel)this.level()).getEntity(((IsEagleTarget) p$THIS).primal$eagleAttacking().get())==null
                || ((ServerLevel)this.level()).getEntity(((IsEagleTarget) p$THIS).primal$eagleAttacking().get()) instanceof LivingEntity eagle && eagle.isDeadOrDying())
        ){
            ((IsEagleTarget) p$THIS).primal$setEagleAttacking(null);
        }
    }


    @Inject(method = "getSwimAmount", at = @At(value = "RETURN"), cancellable = true)
    private void primal$drownedAvoidSwimAnimationWhileJockey(float partialTicks, CallbackInfoReturnable<Float> cir){
        if(p$THIS instanceof Drowned && this.getVehicle() instanceof SharkEntity)
            cir.setReturnValue(0f);
    }

    @Inject(method = "hurt", at = @At("TAIL"))
    private void primal$reflectedDamage(final DamageSource source, final float amount, final CallbackInfoReturnable<Boolean> cir){
        final var reflectedDamageAttribute = this.getAttribute(Primal_EntityAttributes.REFLECTED_DAMAGE);
        if (reflectedDamageAttribute == null || reflectedDamageAttribute.getValue()<=1 || amount<=0) return;

        if(source.getEntity() != null && source.getEntity() instanceof final LivingEntity attacker){
                attacker.hurt(attacker.damageSources().magic(), amount* (float) (reflectedDamageAttribute.getValue()-1));
        }
    }

    @WrapMethod(method = "travel")
    private void primal$modifyPolarBearMovement(Vec3 travelVector, Operation<Void> superTravel){
        if(this.getType().equals(EntityType.POLAR_BEAR)){
            if ((this.isEffectiveAi()) && this.isInWater()) {
                this.moveRelative(this.getSpeed(), travelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
            } else {
                superTravel.call(travelVector);
            }

            return;
        }

        superTravel.call(travelVector);
    }

    @ModifyReturnValue(method = "getFluidFallingAdjustedMovement", at = @At("RETURN"))
    private Vec3 primal$modifyPolarBearFluid(Vec3 original, @Local(argsOnly = true) Vec3 deltaMovement){
        if(this.getType().equals(EntityType.POLAR_BEAR))
            return deltaMovement;

        return original;
    }

    @ModifyReturnValue(method = "canAttack(Lnet/minecraft/world/entity/LivingEntity;)Z", at = @At("RETURN"))
    private boolean primal$avoidGoingForUnderwaterPreyIfDrowning(boolean original, @Local(argsOnly = true) LivingEntity target){
        if(this.getType().equals(EntityType.POLAR_BEAR))
            //Doesn't attack if the target is underwater, and it has less than 25% of air supply and isn't underwater already
            if(target.isUnderWater() && p$THIS.getAirSupply()<p$THIS.getMaxAirSupply()*0.25f && !p$THIS.isUnderWater())
                return false;

        return original;
    }
}
