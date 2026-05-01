package org.primal.mixin.replaced;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.level.Level;
import org.primal.entity.replaced.WolfReplaced;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Wolf.class)
public abstract class WolfMixin extends Animal implements VariantHolder<Rabbit.Variant>, WolfReplaced {

    @Shadow private float shakeAnim;

    protected WolfMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }
    @Unique Wolf p$THIS = (Wolf)(Object)this;

    //──────────────────────────────────── Animations ────────────────────────────────────
    @Unique
    public final AnimationState primal$idleAnimationState = new AnimationState();
    @Unique
    public final AnimationState primal$wetAnimationState = new AnimationState();
    @Unique
    public final AnimationState primal$sitAnimationState = new AnimationState();

    @Override
    public AnimationState primal$idleAnimationState() {
        return primal$idleAnimationState;
    }

    @Override
    public AnimationState primal$wetAnimationState() {
        return primal$wetAnimationState;
    }

    @Override
    public AnimationState primal$sitAnimationState() {
        return primal$sitAnimationState;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void primal$addAnimationIntoTick(CallbackInfo ci) {
        if(this.level().isClientSide()) {
            this.primal$idleAnimationState.animateWhen(true, this.tickCount);
            if(this.shakeAnim > 0.0F)
                this.primal$wetAnimationState.startIfStopped(this.tickCount);
            else if (shakeAnim==0) {
                this.primal$wetAnimationState.stop();
            }
            this.primal$sitAnimationState.animateWhen(p$THIS.isInSittingPose(), this.tickCount);
        }
    }

    @Mixin(AgeableMob.class)
    public abstract static class InitSitAnimation extends PathfinderMob {
        protected InitSitAnimation(EntityType<? extends PathfinderMob> entityType, Level level) {
            super(entityType, level);
        }

        @Unique
        AgeableMob p$THIS = (AgeableMob)(Object)this;

        @Inject(method = "onSyncedDataUpdated", at = @At("HEAD"))
        private void primal$addAnimationIntoPose(EntityDataAccessor<?> key, CallbackInfo ci) {
            if(p$THIS instanceof WolfReplaced wolfReplaced)
                if (p$THIS instanceof TamableAnimal tame && TamableAnimal.DATA_FLAGS_ID.equals(key)) {
                    if(tame.isInSittingPose()){
                        wolfReplaced.primal$sitAnimationState().start(this.tickCount);
                    } else {
                        wolfReplaced.primal$sitAnimationState().stop();
                    }
                }

        }
    }
}
