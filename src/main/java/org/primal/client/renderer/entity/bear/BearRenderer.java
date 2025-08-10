package org.primal.client.renderer.entity.bear;

import org.primal.client.model.entity.BearModel;
import org.primal.client.renderer.entity.bear.layer.BearChestLayer;
import org.primal.client.renderer.entity.bear.layer.BearHoneyLayer;
import org.primal.client.renderer.entity.bear.layer.BearSaddleLayer;
import org.primal.client.renderer.entity.bear.layer.BearSleepLayer;
import org.primal.entity.animal.BearEntity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public final class BearRenderer extends GeoEntityRenderer<BearEntity> {
    public BearRenderer(EntityRendererProvider.Context context) {
        super(context, new BearModel());

        this.addRenderLayer(new BearSaddleLayer(this));
        this.addRenderLayer(new BearChestLayer(this));
        this.addRenderLayer(new BearHoneyLayer(this));
        this.addRenderLayer(new BearSleepLayer(this));
    }

    @Override
    public void scaleModelForRender(float widthScale, float heightScale, PoseStack poseStack, BearEntity animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
        GeoBone bone = model.getBone("head").get();
        float headScale = animatable.isBaby() ? 2.f : 1.f;
        bone.updateScale(headScale, headScale, headScale);
        if (animatable.isBaby()) {
            widthScale = heightScale = .5f;
        }
        super.scaleModelForRender(widthScale, heightScale, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
    }
}