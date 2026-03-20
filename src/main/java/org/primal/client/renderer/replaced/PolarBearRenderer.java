package org.primal.client.renderer.replaced;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.animal.PolarBear;
import org.jetbrains.annotations.NotNull;
import org.primal.client.model.replaced.PolarBearModel;
import org.primal.entity.replaced.PolarBearReplaced;
import org.primal.util.Primal_Util;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoReplacedEntityRenderer;

public class PolarBearRenderer extends GeoReplacedEntityRenderer<PolarBear, PolarBearReplaced> {

    public PolarBearRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PolarBearModel(), new PolarBearReplaced());
        shadowRadius=1.0F;
    }

    @Override
    protected float getShadowRadius(@NotNull PolarBear entity) {
        float f = super.getShadowRadius(entity);
        return entity.isBaby() ? f * 0.35F : f;
    }

    @Override
    public void scaleModelForRender(float widthScale, float heightScale, PoseStack poseStack, PolarBearReplaced animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
        var bone = model.getBone("head");

        float headScale = this.currentEntity.isBaby() ? 2.f : 1.f;
        bone.ifPresent(geoBone ->
                geoBone.updateScale(headScale, headScale, headScale));
        if (this.currentEntity.isBaby()) {
            widthScale = heightScale = .5f;
        }
        super.scaleModelForRender(widthScale, heightScale, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
    }

    @Override
    protected void applyRotations(PolarBearReplaced animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        if(this.currentEntity.isInWater())
            Primal_Util.Visuals.bodyFullRotations(this.currentEntity, partialTick, poseStack);
        else
            super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick, nativeScale);
    }
}
