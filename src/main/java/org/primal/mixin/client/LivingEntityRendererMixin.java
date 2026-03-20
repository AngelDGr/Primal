package org.primal.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import org.primal.entity.animal.LionEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {

    @Shadow protected abstract float getFlipDegrees(T livingEntity);

    @Inject(
            method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "hasPose(Lnet/minecraft/world/entity/Pose;)Z", ordinal = 0)
    )
    private void primal$modifyRotationsWhenMauledByLionRender(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci){
        if(entity.getVehicle() instanceof LionEntity mount){
            float bodyYawRad = mount.yBodyRot * Mth.DEG_TO_RAD;
            double forwardOffset = entity.getBoundingBox().getYsize()-0.3; // Forward (Z direction)
            double sideOffset = -0.0;    // Right (X direction)

            double x = (Mth.sin(bodyYawRad) * forwardOffset) + (Mth.cos(bodyYawRad) * sideOffset);
            double z = -(Mth.cos(bodyYawRad) * forwardOffset) + (Mth.sin(bodyYawRad) * sideOffset);

            float f3 = entity.getEyeHeight(Pose.STANDING) - 0.3F;
            poseStack.translate(x, f3, z);
        }
    }

    @ModifyExpressionValue(
            method = "setupRotations",
            at = @At(value = "INVOKE", target = "hasPose(Lnet/minecraft/world/entity/Pose;)Z", ordinal = 0)
    )
    private boolean primal$modifyRotationsWhenMauledByLion(boolean original, @Local(argsOnly = true) T entity){
        return original && !(entity.getVehicle() instanceof LionEntity);
    }

    @Inject(
            method = "setupRotations",
            at = @At(value = "INVOKE", target = "hasPose(Lnet/minecraft/world/entity/Pose;)Z", ordinal = 1)
    )
    private void primal$modifyRotationsWhenMauledByLion2(T entity, PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale, CallbackInfo ci){
        if(entity.getVehicle() instanceof LionEntity)
            poseStack.mulPose(Axis.XP.rotationDegrees(this.getFlipDegrees(entity)));
    }
}
