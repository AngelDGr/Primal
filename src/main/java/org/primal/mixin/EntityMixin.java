package org.primal.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.primal.util.HostileMount;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Unique
    Entity primal$THIS = (Entity)(Object)this;

    @Shadow public abstract @Nullable Entity getVehicle();

    @Inject(method = "isShiftKeyDown", at = @At("RETURN"), cancellable = true)
    private void primal$cantShiftIfHostileMount(CallbackInfoReturnable<Boolean> cir) {
        if(this.getVehicle()!=null && this.getVehicle() instanceof HostileMount && !(primal$THIS instanceof Player player && player.isCreative())){
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "canRide", at = @At("RETURN"), cancellable = true)
    private void primal$shiftCantPreventRidingIfHostileMount(Entity vehicle, CallbackInfoReturnable<Boolean> cir) {
        if(vehicle instanceof HostileMount){
            cir.setReturnValue(true);
        }
    }
}
