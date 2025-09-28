package org.primal.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EntityType.class)
public class EntityTypeMixin<T extends Entity> {

    @ModifyArg(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/EntityType$Builder;sized(FF)Lnet/minecraft/world/entity/EntityType$Builder;",
                    ordinal = 42
            ),
            index = 0
    )
    private static float primal$modifyFoxWidth(float original) {
        return (original == 0.6F) ? 0.9f : original;
    }

    @ModifyArg(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/EntityType$Builder;sized(FF)Lnet/minecraft/world/entity/EntityType$Builder;",
                    ordinal = 81
            ),
            index = 0
    )
    private static float primal$modifyPolarBearWidth(float original) {
        return (original == 1.4f) ? 1.9f : original;
    }

    @ModifyArg(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/EntityType$Builder;sized(FF)Lnet/minecraft/world/entity/EntityType$Builder;",
                    ordinal = 81
            ),
            index = 1
    )
    private static float primal$modifyPolarBearHeight(float original) {
        return (original == 1.4f) ? 1.75f : original;
    }

}
