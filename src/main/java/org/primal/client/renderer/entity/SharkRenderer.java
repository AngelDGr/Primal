package org.primal.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.primal.client.model.entity.SharkModel;
import org.primal.client.renderer.entity.layer.SharkConduitEyesLayer;
import org.primal.entity.animal.SharkEntity;
import org.primal.util.Primal_Util;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SharkRenderer extends GeoEntityRenderer<SharkEntity> {
    public SharkRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SharkModel());
        addRenderLayer(new SharkConduitEyesLayer(this));

        shadowRadius=0.9F;
    }

    @Override
    public void scaleModelForRender(float widthScale, float heightScale, PoseStack poseStack, SharkEntity animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
        super.scaleModelForRender(widthScale, heightScale, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
    }

    @Override
    protected void applyRotations(SharkEntity animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick) {
        Primal_Util.Visuals.bodyFullRotations(animatable, partialTick, poseStack);
    }
}
