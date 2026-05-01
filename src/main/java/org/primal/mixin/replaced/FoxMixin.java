package org.primal.mixin.replaced;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.level.Level;
import org.primal.entity.animal.SnakeEntity;
import org.primal.entity.replaced.FoxReplaced;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(Fox.class)
public abstract class FoxMixin extends Animal implements VariantHolder<Fox.Type>, FoxReplaced {

    protected FoxMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }
    @Unique
    Fox p$THIS = (Fox)(Object)this;

    @ModifyArg(
            method = "registerGoals",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/ai/goal/target/NearestAttackableTargetGoal;<init>(Lnet/minecraft/world/entity/Mob;Ljava/lang/Class;IZZLjava/util/function/Predicate;)V",
                    ordinal = 0)
    )
    private Predicate<LivingEntity> primal$addSnakeTarget(Predicate<LivingEntity> targetPredicate) {
        return targetPredicate.or( m-> (m instanceof SnakeEntity s &&
                //Only attack untamed or tamed ones that attack
                (!s.isTame() ||
                (this.getLastDamageSource()!=null && this.getLastDamageSource().getEntity()!=null && this.getLastDamageSource().getEntity().is(s)))) );
    }


    @SuppressWarnings("unused")
    @Mixin(targets = "net.minecraft.world.entity.animal.Fox$FoxPanicGoal")
    private abstract static class FoxPanicMixin extends PanicGoal {
        //This is the Fox.this element, it can be seen using view bytecode
        @Final
        @Shadow
        Fox this$0;

        public FoxPanicMixin(PathfinderMob mob, double speedModifier) {
            super(mob, speedModifier);
        }

        @ModifyReturnValue(method = "shouldPanic", at = @At(value = "RETURN"))
        private boolean primal$dontPanicWithSnake(boolean original){
            return original && !(this.mob.getLastDamageSource()!=null && this.mob.getLastDamageSource().getEntity() instanceof SnakeEntity);
        }
    }

    //──────────────────────────────────── Animations ────────────────────────────────────
    @Unique
    public final AnimationState primal$idleAnimationState = new AnimationState();
    @Unique
    public final AnimationState primal$sitAnimationState = new AnimationState();
    @Unique
    public final AnimationState primal$sleepAnimationState = new AnimationState();
    @Unique
    public final AnimationState primal$stuckAnimationState = new AnimationState();
    @Unique
    public final AnimationState primal$unstuckAnimationState = new AnimationState();
    @Unique
    public final AnimationState primal$pounceAnimationState = new AnimationState();

    @Override
    public AnimationState primal$idleAnimationState() {
        return primal$idleAnimationState;
    }

    @Override
    public AnimationState primal$sitAnimationState() {
        return primal$sitAnimationState;
    }

    @Override
    public AnimationState primal$sleepAnimationState() {
        return primal$sleepAnimationState;
    }

    @Override
    public AnimationState primal$stuckAnimationState() {
        return primal$stuckAnimationState;
    }

    @Override
    public AnimationState primal$unstuckAnimationState() {
        return primal$unstuckAnimationState;
    }

    @Override
    public AnimationState primal$pounceAnimationState() {
        return primal$pounceAnimationState;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void primal$addAnimationIntoTick(CallbackInfo ci) {
        if(this.level().isClientSide()) {
            this.primal$idleAnimationState.animateWhen(true, this.tickCount);

            this.primal$sleepAnimationState.animateWhen(p$THIS.isSleeping(), this.tickCount);

            this.primal$sitAnimationState.animateWhen(p$THIS.isSitting(), this.tickCount);

            this.primal$pounceAnimationState.animateWhen(p$THIS.isPouncing(), this.tickCount);

            this.primal$stuckAnimationState.animateWhen(p$THIS.isFaceplanted(), this.tickCount);
        }
        if(primal$unstuckCounter >0) primal$unstuckCounter--;
    }

    @Inject(method = "handleEntityEvent", at = @At("HEAD"))
    private void primal$addTriggerAnimation(byte id, CallbackInfo ci) {
        if(p$THIS instanceof FoxReplaced fox){
            if (id == 78)
                fox.primal$unstuckAnimationState().start(this.tickCount);
        }
    }

    @Unique
    public int primal$unstuckCounter = 0;

    @Inject(method = "setFaceplanted", at = @At("HEAD"))
    private void primal$triggerAttackAnimation(boolean faceplanted, CallbackInfo ci) {
        if(faceplanted && !this.level().isClientSide() && primal$unstuckCounter <=0){
            primal$unstuckCounter =40;

            this.level().broadcastEntityEvent(this, (byte)78);
        }
    }
}
