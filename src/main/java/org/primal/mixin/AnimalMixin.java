package org.primal.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import org.primal.entity.animal.BearEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Animal.class)
public abstract class AnimalMixin {

    @Unique
    Animal p$THIS = (Animal)(Object)this;

    @Inject(method = "canMate", at = @At("RETURN"), cancellable = true)
    private void primal$makePolarBearMateBear(Animal otherAnimal, CallbackInfoReturnable<Boolean> cir){
        if(p$THIS instanceof PolarBear){
            cir.setReturnValue(cir.getReturnValue() || (otherAnimal instanceof BearEntity bear && p$THIS.isInLove() && bear.isInLove() && bear.getVariant() == BearEntity.Variant.GRIZZLY));
        }
    }

    @Inject(method = "usePlayerItem", at = @At("HEAD"), cancellable = true)
    private void primal$notConsumeBucketForPolarBear(Player player, InteractionHand hand, ItemStack stack, CallbackInfo ci){
        if(p$THIS instanceof PolarBear){
            if (stack.is(Items.SALMON_BUCKET)) {
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.WATER_BUCKET)));
                ci.cancel();
            }
        }
    }
}
