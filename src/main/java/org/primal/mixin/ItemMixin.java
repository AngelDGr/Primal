package org.primal.mixin;

import net.minecraft.world.item.*;
import org.primal.registry.Primal_Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {

    @Unique
    Item p$THIS = (Item)(Object)this;

    @Inject(method = "isValidRepairItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z", at = @At("RETURN"), cancellable = true)
    private void primal$addRepairToTrident(ItemStack stack, ItemStack repairCandidate, CallbackInfoReturnable<Boolean> cir){
        if(p$THIS instanceof TridentItem)
            cir.setReturnValue(cir.getReturnValue() || repairCandidate.is(Primal_Items.SHARK_TOOTH.get()));
    }
}
