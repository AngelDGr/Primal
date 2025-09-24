package org.primal.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.primal.Primal_Main;
import org.primal.client.model.entity.CrocodileModel;
import org.primal.entity.animal.CrocodileEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class CrocodileRenderer extends GeoEntityRenderer<CrocodileEntity> {
    public CrocodileRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CrocodileModel());

        addRenderLayer(new AutoGlowingGeoLayer<>(this) {

            @Override
            public ResourceLocation getTextureResource(CrocodileEntity crocodile){
                return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/crocodile/"+crocodile.getVariant().getSerializedName()+".png");
            }
        });

        shadowRadius=1.1F;
    }

    @Override
    public void scaleModelForRender(float widthScale, float heightScale, PoseStack poseStack, CrocodileEntity animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
        if (animatable.isBaby()) {
            widthScale = heightScale = 0.3f;
        }
        super.scaleModelForRender(widthScale, heightScale, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
    }

    @Override
    protected void applyRotations(CrocodileEntity animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        if(animatable.isUnderWater())
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
    public float getMotionAnimThreshold(CrocodileEntity animatable) {
        return super.getMotionAnimThreshold(animatable);
    }
}
