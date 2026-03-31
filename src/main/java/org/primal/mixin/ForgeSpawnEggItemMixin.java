package org.primal.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraftforge.common.ForgeSpawnEggItem;
import org.primal.Primal_Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraftforge.common.ForgeSpawnEggItem$ColorRegisterHandler")
public class ForgeSpawnEggItemMixin {

    @ModifyReturnValue(method = "lambda$registerSpawnEggColors$0", at = @At(value = "RETURN")
            //Because we are calling something from forge
            ,remap = false
    )
    private static int primal$interceptPrimalEggs(int original, @Local(argsOnly = true) ForgeSpawnEggItem egg, @Local(argsOnly = true) int tintIndex) {
        if(BuiltInRegistries.ITEM.getKey(egg).getNamespace().equals(Primal_Main.MOD_ID))
            return Minecraft.getInstance().getResourceManager().listPacks()
                    .anyMatch(packResources -> packResources.packId().equals("primal_modern_spawn_eggs"))?
                    -1
                    : ((egg)).getColor(tintIndex);

        return original;
    }
}
