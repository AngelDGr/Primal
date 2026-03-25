package org.primal.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.client.model.entity.CrocodileModel;
import org.primal.entity.animal.CrocodileEntity;
import org.primal.util.Primal_Util;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class CrocodileRenderer extends GeoEntityRenderer<CrocodileEntity> {
    public CrocodileRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CrocodileModel());

        addRenderLayer(new AutoGlowingGeoLayer<>(this) {

            @Override
            public ResourceLocation getTextureResource(CrocodileEntity crocodile){
                return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/crocodile/"+ (animatable.isBaby()? "baby/": "") +crocodile.getVariant().getSerializedName()+".png");
            }
        });

        shadowRadius=0.9F;
    }

    @Override
    public void render(@NotNull CrocodileEntity entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        if (entity.isBaby()) this.shadowRadius *= 0.3F;
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    protected void applyRotations(CrocodileEntity animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick) {
        if(animatable.isInWater())
            Primal_Util.Visuals.bodyFullRotations(animatable, partialTick, poseStack);
        else
            super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick);
    }

    @Override
    public void scaleModelForRender(float widthScale, float heightScale, PoseStack poseStack, CrocodileEntity animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
        if(!Primal_Main.COMMON_CONFIG.crocodileBabyCustomModel.get()){
            if (animatable.isBaby()) {
                widthScale = heightScale = .5f;
            }
        }

        super.scaleModelForRender(widthScale, heightScale, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
    }

    @Override
    public float getMotionAnimThreshold(CrocodileEntity animatable) {
        return super.getMotionAnimThreshold(animatable);
    }
}
