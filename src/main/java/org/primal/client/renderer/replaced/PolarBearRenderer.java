package org.primal.client.renderer.replaced;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.animal.PolarBear;
import org.primal.client.model.replaced.PolarBearModel;
import org.primal.entity.replaced.PolarBearReplaced;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoReplacedEntityRenderer;

public class PolarBearRenderer extends GeoReplacedEntityRenderer<PolarBear, PolarBearReplaced> {

    public PolarBearRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PolarBearModel(), new PolarBearReplaced());
    }

    @Override
    public void scaleModelForRender(float widthScale, float heightScale, PoseStack poseStack, PolarBearReplaced animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
        GeoBone bone = model.getBone("head").get();
        float headScale = this.currentEntity.isBaby() ? 2.f : 1.f;
        bone.updateScale(headScale, headScale, headScale);
        if (this.currentEntity.isBaby()) {
            widthScale = heightScale = .5f;
        }
        super.scaleModelForRender(widthScale, heightScale, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
    }
}
