package org.primal.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.progress.ChunkProgressListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class TestingMixin {

    @Inject(method = "createLevels", at = @At(value = "TAIL"))
    private void primal$test(ChunkProgressListener listener, CallbackInfo ci){
//        var thing = this.registryAccess().registryOrThrow(Registries.PLACED_FEATURE).getHolderOrThrow(VegetationPlacements.TREES_WINDSWEPT_SAVANNA).value();
//        System.out.println("EXISTS: "+ thing);
    }
}
