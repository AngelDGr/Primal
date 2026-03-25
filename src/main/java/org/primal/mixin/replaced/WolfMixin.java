package org.primal.mixin.replaced;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.VariantHolder;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.animatable.GeoReplacedEntity;
import software.bernie.geckolib.util.RenderUtils;

@Mixin(Wolf.class)
public abstract class WolfMixin extends Animal implements VariantHolder<Rabbit.Variant> {

    @Shadow private float shakeAnim;

    protected WolfMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }
    @Unique Wolf p$THIS = (Wolf)(Object)this;

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void primal$rearLogic(CallbackInfo ci) {
        //Only triggers on client and not in water and only if is actually replaced
        if (!this.level().isClientSide || this.isInWater() || !(RenderUtils.getReplacedAnimatable(this.getType()) instanceof GeoReplacedEntity replacedEntity)) return;

        if(this.shakeAnim>0 && this.shakeAnim<=0.1){
            replacedEntity.stopTriggeredAnimation(p$THIS, "base_controller", "wet");
            replacedEntity.triggerAnim(p$THIS, "base_controller", "wet");
        }
    }
}
