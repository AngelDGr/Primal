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

@Mixin(value = Fox.class)
public abstract class FoxMixin extends Animal implements VariantHolder<Fox.Type>, FoxReplaced {

    protected FoxMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        p$THIS = (Fox) (Object) this;
        primal$idleAnimationState = new AnimationState();
        primal$sitAnimationState = new AnimationState();
        primal$sleepAnimationState = new AnimationState();
        primal$stuckAnimationState = new AnimationState();
        primal$unstuckAnimationState = new AnimationState();
        primal$pounceAnimationState = new AnimationState();
    }

    @Unique
    private Fox getP$THIS(){
        if (p$THIS == null){
            p$THIS = (Fox) (Object) this;
        }
        return p$THIS;
    }
    @Unique
    Fox p$THIS;

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
    public AnimationState primal$idleAnimationState;
    @Unique
    public AnimationState primal$sitAnimationState;
    @Unique
    public AnimationState primal$sleepAnimationState;
    @Unique
    public AnimationState primal$stuckAnimationState;
    @Unique
    public AnimationState primal$unstuckAnimationState;
    @Unique
    public AnimationState primal$pounceAnimationState;

    @Override
    public AnimationState primal$idleAnimationState() {
        if (this.primal$idleAnimationState == null){
            this.primal$idleAnimationState = new AnimationState();
        }
        return this.primal$idleAnimationState;
    }

    @Override
    public AnimationState primal$sitAnimationState() {
        if (this.primal$sitAnimationState == null){
            this.primal$sitAnimationState = new AnimationState();
        }
        return this.primal$sitAnimationState;
    }

    @Override
    public AnimationState primal$sleepAnimationState() {
        if (this.primal$sleepAnimationState == null){
            this.primal$sleepAnimationState = new AnimationState();
        }
        return this.primal$sleepAnimationState;
    }

    @Override
    public AnimationState primal$stuckAnimationState() {
        if (this.primal$stuckAnimationState == null){
            this.primal$stuckAnimationState = new AnimationState();
        }
        return this.primal$stuckAnimationState;
    }

    @Override
    public AnimationState primal$unstuckAnimationState() {
        if (this.primal$unstuckAnimationState == null){
            this.primal$unstuckAnimationState = new AnimationState();
        }
        return this.primal$unstuckAnimationState;
    }

    @Override
    public AnimationState primal$pounceAnimationState() {
        if (this.primal$pounceAnimationState == null){
            this.primal$pounceAnimationState = new AnimationState();
        }
        return primal$pounceAnimationState;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void primal$addAnimationIntoTick(CallbackInfo ci) {
        if(this.level().isClientSide()) {
            this.primal$idleAnimationState().animateWhen(true, this.tickCount);

            this.primal$sleepAnimationState().animateWhen(getP$THIS().isSleeping(), this.tickCount);

            this.primal$sitAnimationState().animateWhen(getP$THIS().isSitting(), this.tickCount);

            this.primal$pounceAnimationState().animateWhen(getP$THIS().isPouncing(), this.tickCount);

            this.primal$stuckAnimationState().animateWhen(getP$THIS().isFaceplanted(), this.tickCount);
        }
        if(primal$unstuckCounter >0) primal$unstuckCounter--;
    }

    @Inject(method = "handleEntityEvent", at = @At("HEAD"))
    private void primal$addTriggerAnimation(byte id, CallbackInfo ci) {
        if(getP$THIS() instanceof FoxReplaced fox){
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
