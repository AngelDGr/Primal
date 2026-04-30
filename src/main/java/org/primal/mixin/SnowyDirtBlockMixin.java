package org.primal.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.primal.registry.Primal_Tags;
import org.primal.util.block_types.Snowloggable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SnowyDirtBlock.class)
public class SnowyDirtBlockMixin {

    @ModifyReturnValue(method = "isSnowySetting", at = @At("RETURN"))
    private static boolean primal$addSnowyWithSeashells(boolean original, @Local(argsOnly = true) BlockState state) {
        return original || (state.getBlock().asItem().getDefaultInstance().is(Primal_Tags.Item.SEASHELLS)
                && state.getOptionalValue(Snowloggable.SNOWY).orElse(false));
    }
}
