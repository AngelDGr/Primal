package org.primal.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.primal.entity.animal.BearEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import software.bernie.geckolib.animatable.GeoReplacedEntity;
import software.bernie.geckolib.util.RenderUtil;

@Mixin(PolarBear.class)
public abstract class PolarBearMixin extends Animal implements NeutralMob {

    @Shadow public abstract boolean isStanding();

    protected PolarBearMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    PolarBear p$THIS = (PolarBear)(Object)this;

    @Inject(method = "registerGoals", at = @At("HEAD"))
    private void primal$makePolarBearBreedable(final CallbackInfo ci){

        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0, BearEntity.class));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0, PolarBear.class));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, stack -> stack.is(Items.SALMON_BUCKET), false));
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

    @Inject(method = "tick", at = @At("TAIL"))
    private void primal$implementAttackAnimation(CallbackInfo ci){
        //This triggers the animation directly from the replacing class
        if(this.isStanding() && !this.isInWater()){
            if (RenderUtil.getReplacedAnimatable(this.getType()) instanceof GeoReplacedEntity replacedEntity){
                replacedEntity.triggerAnim(p$THIS, "base_controller", "attack");
            }
        }
    }
}
