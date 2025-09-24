package org.primal.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.VariantHolder;
import net.minecraft.world.entity.ai.goal.JumpGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.animatable.GeoReplacedEntity;
import software.bernie.geckolib.util.RenderUtil;

@Mixin(Fox.class)
public abstract class FoxMixin extends Animal implements VariantHolder<Fox.Type> {
    protected FoxMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
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
            //This triggers the animation directly from the replacing class
            if (RenderUtil.getReplacedAnimatable(this$0.getType()) instanceof GeoReplacedEntity replacedEntity){
                replacedEntity.triggerAnim(this$0, "base_controller", "unstuck");
            }
        }
    }
}
