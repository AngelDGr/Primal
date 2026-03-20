package org.primal.mixin;

import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import org.primal.Primal_Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityType.class)
public class EntityTypeMixin {
    @Unique
    EntityType<?> p$THIS = (EntityType<?>)(Object)this;

    @Inject(method = "getDimensions",
            at = @At("HEAD"), cancellable = true)
    private void primal$modifyHitboxesForNewModels(CallbackInfoReturnable<EntityDimensions> cir) {

        // Fox Hitbox: 0.6 x 0.7 -> 0.75 x 0.85
        if(p$THIS == EntityType.FOX && Primal_Main.ConfigCache.foxModelChange){
            cir.setReturnValue(EntityDimensions.scalable(0.6f, 0.85F));
        }

        // Polar Bear Hitbox: 1.4 x 1.4 -> 1.9 x 1.75
        if(p$THIS == EntityType.POLAR_BEAR && Primal_Main.ConfigCache.polarBearModelChange){
            cir.setReturnValue(EntityDimensions.scalable(1.9F, 1.75F));
        }

        // Rabbit Hitbox: 0.4 x 0.5 -> 0.55 x 0.7
        if(p$THIS == EntityType.RABBIT && Primal_Main.ConfigCache.rabbitModelChange){
            cir.setReturnValue(EntityDimensions.scalable(0.55F, 0.7F));
        }

        // Wolf Hitbox: 0.6 x 0.5 -> 1.0 x 1.15
        if(p$THIS == EntityType.WOLF && Primal_Main.ConfigCache.wolfModelChange){
            cir.setReturnValue(EntityDimensions.scalable(1.0F, 1.15F));
        }

        // Dolphin Hitbox: (Doesn't change)
//        if(p$THIS == EntityType.DOLPHIN && Primal_Main.ConfigCache.dolphinModelChange){
//            cir.setReturnValue(EntityDimensions.scalable(1.9F, 1.75F));
//        }
    }
}
