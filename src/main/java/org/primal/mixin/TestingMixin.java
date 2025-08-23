package org.primal.mixin;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class TestingMixin {

    @Inject(method = "startRiding(Lnet/minecraft/world/entity/Entity;)Z", at = @At("RETURN"))
    private void primal$test(Entity vehicle, CallbackInfoReturnable<Boolean> cir){
//        if(owner instanceof BearEntity bear && !bear.isBaby() && !bear.isTame())
//            System.out.println("HERE");
    }
}
