package org.primal.mixin.replaced;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.primal.entity.ai.controls.look.WaterOrLandLookControl;
import org.primal.entity.ai.controls.move.WaterOrLandMoveControl;
import org.primal.entity.ai.goals.TryFindWaterSurfaceGoal;
import org.primal.entity.animal.BearEntity;
import org.primal.entity.animal.WalrusEntity;
import org.primal.entity.replaced.PolarBearReplaced;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PolarBear.class)
public abstract class PolarBearMixin extends Animal implements NeutralMob, PolarBearReplaced {

    @Shadow public abstract boolean isStanding();

    protected PolarBearMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void primal$onInit(CallbackInfo ci) {
        this.moveControl = new WaterOrLandMoveControl<>(this, 85, 50, 0.38f, 0.01f, false);
        this.lookControl = new WaterOrLandLookControl<>(this, 10);
    }

    @Unique
    PolarBear p$THIS = (PolarBear)(Object)this;

    @Inject(method = "registerGoals", at = @At("HEAD"))
    private void primal$addNewPolarBearGoals(final CallbackInfo ci){
        this.goalSelector.addGoal(0, new TryFindWaterSurfaceGoal<>(this, 32, 1, pB -> (pB.getTarget() == null && pB.getAirSupply()<pB.getMaxAirSupply() * 0.75) || pB.getAirSupply() < 40));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0, BearEntity.class));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0, PolarBear.class));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, stack -> stack.is(Items.SALMON_BUCKET), false));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, WalrusEntity.class, 10, true, true, null));
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void primal$removePolarBearGoals(final CallbackInfo ci){
        this.goalSelector.getAvailableGoals().removeIf(
                wrapped -> wrapped.getGoal() instanceof FloatGoal
        );
    }

    @Inject(method = "isFood", at = @At("RETURN"), cancellable = true)
    private void primal$addPolarBearFoodForMating(ItemStack stack, CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(cir.getReturnValue() || stack.is(Items.SALMON_BUCKET));
    }

    @Inject(method = "getBreedOffspring", at = @At("HEAD"), cancellable = true)
    private void primal$makeGrolar(ServerLevel level, AgeableMob otherParent, CallbackInfoReturnable<AgeableMob> cir){
        if(otherParent instanceof BearEntity bear && bear.getVariant() == BearEntity.Variant.GRIZZLY){
            cir.setReturnValue(BearEntity.createFromParents(p$THIS, otherParent));
        }
    }

    //──────────────────────────────────── Animations ────────────────────────────────────
    @Unique
    public final AnimationState primal$idleAnimationState = new AnimationState();
    @Unique
    public final AnimationState primal$attackAnimationState = new AnimationState();

    @Override
    public AnimationState primal$idleAnimationState() {
        return primal$idleAnimationState;
    }

    @Override
    public AnimationState primal$attackAnimationState() {
        return primal$attackAnimationState;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void primal$addAnimationIntoTick(CallbackInfo ci) {
        if(this.level().isClientSide()) {
            this.primal$idleAnimationState.animateWhen(true, this.tickCount);
        }
        if(primal$standingCounter>0) primal$standingCounter--;
    }

    @Unique
    public int primal$standingCounter = 0;

    @Inject(method = "setStanding", at = @At("HEAD"))
    private void primal$triggerAttackAnimation(boolean standing, CallbackInfo ci) {
        if(standing && !this.level().isClientSide() && primal$standingCounter<=0){
            if(this.isInWaterOrBubble()) primal$standingCounter=10;
            else primal$standingCounter=20;

            this.level().broadcastEntityEvent(this, (byte)4);
        }
    }

    @Mixin(Animal.class)
    public abstract static class TriggerAttackAnimation extends PathfinderMob {

        protected TriggerAttackAnimation(EntityType<? extends PathfinderMob> entityType, Level level) {
            super(entityType, level);
        }

        @Unique
        Animal p$THIS = (Animal)(Object)this;

        @Inject(method = "handleEntityEvent", at = @At("HEAD"))
        private void primal$addTriggerAnimation(byte id, CallbackInfo ci) {
            if(p$THIS instanceof PolarBearReplaced polarBear){
                if (id == 4)
                    polarBear.primal$attackAnimationState().start(this.tickCount);
            }
        }
    }


}
