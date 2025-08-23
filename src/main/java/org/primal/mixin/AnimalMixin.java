package org.primal.mixin;

import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.PolarBear;
import org.primal.entity.animal.BearEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Animal.class)
public class AnimalMixin {

    @Unique
    Animal p$THIS = (Animal)(Object)this;

    @Inject(method = "canMate", at = @At("RETURN"), cancellable = true)
    private void primal$makePolarBearMateBear(Animal otherAnimal, CallbackInfoReturnable<Boolean> cir){
        if(p$THIS instanceof PolarBear){
            cir.setReturnValue(cir.getReturnValue() || (otherAnimal instanceof BearEntity bear && p$THIS.isInLove() && bear.isInLove() && bear.getVariant() == BearEntity.Variant.GRIZZLY));
        }
    }
}
