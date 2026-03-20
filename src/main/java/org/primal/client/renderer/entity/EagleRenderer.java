package org.primal.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.primal.client.model.entity.EagleModel;
import org.primal.client.renderer.entity.layer.EagleCollarLayer;
import org.primal.entity.animal.EagleEntity;
import org.primal.util.Primal_Util;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public final class EagleRenderer extends GeoEntityRenderer<EagleEntity> {
    public EagleRenderer(EntityRendererProvider.Context context) {
        super(context, new EagleModel());

        this.addRenderLayer(new EagleCollarLayer(this));

        shadowRadius=0.5F;
    }

    @Override
    protected float getShadowRadius(@NotNull EagleEntity entity) {
        float f = super.getShadowRadius(entity);
        return entity.isBaby() ? f * 0.3F : f;
    }

    @Override
    public float getMotionAnimThreshold(EagleEntity animatable) {
        return 0.0015f;
    }

    @Override
    protected void applyRotations(EagleEntity animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        if(!animatable.onGround() && !animatable.isBaby())
            Primal_Util.Visuals.bodyFullRotations(animatable, partialTick, poseStack);
        else
            super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick, nativeScale);
    }
}