package org.primal.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import org.primal.util.block_types.Snowloggable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Biome.class)
public class BiomeMixin {
    @ModifyExpressionValue(method = "shouldSnow",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;isAir()Z"))
    private boolean primal$canSnowOnSnowloggable(boolean original, @Local BlockState blockstate){
        return original || blockstate.getBlock() instanceof Snowloggable;
    }
}
