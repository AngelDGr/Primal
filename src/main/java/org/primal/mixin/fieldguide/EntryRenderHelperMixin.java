package org.primal.mixin.fieldguide;

import com.evandev.fieldguide.client.gui.util.EntryRenderHelper;
import net.minecraft.world.entity.Entity;
import org.primal.util.mob_types.CustomFieldGuideState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntryRenderHelper.class)
public class EntryRenderHelperMixin {

    @Inject(method = "renderEntity", at = @At(value = "HEAD"), remap = false)
    private static void primal$customFieldGuideRender(Entity entity, Object entrySource, boolean isPage, float yRotation, CallbackInfo ci){
        if(entity instanceof CustomFieldGuideState customFieldGuideState) {
            customFieldGuideState.setFieldGuideState(true);
        }
    }
}