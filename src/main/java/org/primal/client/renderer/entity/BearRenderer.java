package org.primal.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.client.model.entity.BearModel;
import org.primal.client.renderer.entity.layer.bear.BearBarrelsLayer;
import org.primal.client.renderer.entity.layer.bear.BearCollarLayer;
import org.primal.client.renderer.entity.layer.bear.BearHoneyLayer;
import org.primal.client.renderer.entity.layer.bear.BearSleepLayer;
import org.primal.entity.animal.BearEntity;
import org.primal.util.Primal_Util;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public final class BearRenderer extends GeoEntityRenderer<BearEntity> {
    public BearRenderer(EntityRendererProvider.Context context) {
        super(context, new BearModel());

        this.addRenderLayer(new BearCollarLayer(this));
        this.addRenderLayer(new BearBarrelsLayer(this));
        this.addRenderLayer(new BearHoneyLayer(this));
        this.addRenderLayer(new BearSleepLayer(this));

        shadowRadius=1.0F;
    }

    @Override
    protected float getShadowRadius(@NotNull BearEntity entity) {
        float f = super.getShadowRadius(entity);
        return entity.isBaby() ? f * 0.35F : f;
    }

    @Override
    public float getMotionAnimThreshold(BearEntity animatable) {
        return animatable.isInWater()? 0.0f: 0.0015f;
    }

    @Override
    protected void applyRotations(BearEntity animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        if(animatable.isInWater() && !animatable.isVehicle() && !animatable.isBearSleeping())
            Primal_Util.Visuals.bodyFullRotations(animatable, partialTick, poseStack);
        else
            super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick, nativeScale);
    }

    @Override
    public void scaleModelForRender(float widthScale, float heightScale, PoseStack poseStack, BearEntity animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
        if(!Primal_Main.COMMON_CONFIG.bearBabyCustomModel.get()){
            var bone = model.getBone("head");
            float headScale = animatable.isBaby() ? 2.f : 1.f;
            bone.ifPresent(geoBone ->
                    geoBone.updateScale(headScale, headScale, headScale));
            if (animatable.isBaby()) {
                widthScale = heightScale = .4f;
            }
        }

        super.scaleModelForRender(widthScale, heightScale, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
    }
}