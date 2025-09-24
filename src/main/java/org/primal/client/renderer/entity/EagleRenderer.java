package org.primal.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.primal.client.model.entity.EagleModel;
import org.primal.client.renderer.entity.layer.EagleCollarLayer;
import org.primal.entity.animal.EagleEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public final class EagleRenderer extends GeoEntityRenderer<EagleEntity> {
    public EagleRenderer(EntityRendererProvider.Context context) {
        super(context, new EagleModel());

        this.addRenderLayer(new EagleCollarLayer(this));

        shadowRadius=0.5F;
    }

    @Override
    public void scaleModelForRender(float widthScale, float heightScale, PoseStack poseStack, EagleEntity animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
        super.scaleModelForRender(widthScale, heightScale, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
    }

    @Override
    protected void applyRotations(EagleEntity animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        if(!animatable.onGround() && !animatable.isBaby())
        {
            float yaw = Mth.lerp(partialTick, animatable.yRotO, animatable.getYRot());
            poseStack.mulPose(Axis.YP.rotationDegrees(180f - yaw));

            float pitch = Mth.lerp(partialTick, animatable.xRotO, animatable.getXRot());
            poseStack.mulPose(Axis.XP.rotationDegrees(-pitch));
        } else {
            super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick, nativeScale);
        }
    }

    @Override
    public float getMotionAnimThreshold(EagleEntity animatable) {
        return 0.0015f;
    }
}