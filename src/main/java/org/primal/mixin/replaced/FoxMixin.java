package org.primal.mixin.replaced;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.VariantHolder;
import net.minecraft.world.entity.ai.goal.JumpGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.level.Level;
import org.primal.entity.animal.SnakeEntity;
import org.primal.injection.FoxUnstuck;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.animatable.GeoReplacedEntity;
import software.bernie.geckolib.util.RenderUtil;

import java.util.function.Predicate;

@Mixin(Fox.class)
public abstract class FoxMixin extends Animal implements VariantHolder<Fox.Type>, FoxUnstuck {
    protected FoxMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

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

    @Unique
    private static final EntityDataAccessor<Integer> primal$mustDoUnstuckAnim = SynchedEntityData.defineId(FoxMixin.class, EntityDataSerializers.INT);

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void primal$IsEagleAttackingSynchedData(final SynchedEntityData.Builder builder, final CallbackInfo ci){
        builder.define(primal$mustDoUnstuckAnim, 0);
    }

    @Override
    public int primal$mustDoUnstuck() {
        return this.entityData.get(primal$mustDoUnstuckAnim);
    }

    @Override
    public void primal$setMustDoUnstuck(int mustDo) {
        this.entityData.set(primal$mustDoUnstuckAnim, mustDo);
    }

    @Unique
    int primal$lastSeen = 0;
    @Inject(method = "tick", at = @At(value = "TAIL"))
    private void primal$doUnstuckAnimation(CallbackInfo ci){
        if (!this.level().isClientSide) return;

        if (RenderUtil.getReplacedAnimatable(this.getType()) instanceof GeoReplacedEntity replacedEntity){
            if(primal$lastSeen != primal$mustDoUnstuck()){
                primal$lastSeen = primal$mustDoUnstuck();
                replacedEntity.triggerAnim(this, "base_controller", "unstuck");
            }
        }

    }

    @SuppressWarnings("unused")
    @Mixin(Fox.FoxPounceGoal.class)
    private abstract static class FoxPounceMixin extends JumpGoal {
        //This is the Fox.this element, it can be seen using view bytecode
        @Final
        @Shadow
        Fox this$0;

        @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Fox;setFaceplanted(Z)V"))
        private void primal$triggerUnstuckAnimation(CallbackInfo ci){
            ((FoxUnstuck)this$0).primal$setMustDoUnstuck(((FoxUnstuck)this$0).primal$mustDoUnstuck()+1);
        }
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
}
