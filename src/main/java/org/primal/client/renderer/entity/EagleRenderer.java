package org.primal.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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
    public void render(@NotNull EagleEntity entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        if (entity.isBaby()) this.shadowRadius *= 0.3F;
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public float getMotionAnimThreshold(EagleEntity animatable) {
        return 0.0015f;
    }

    @Override
    protected void applyRotations(EagleEntity animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick) {
        if(!animatable.onGround() && !animatable.isBaby())
            Primal_Util.Visuals.bodyFullRotations(animatable, partialTick, poseStack);
        else
            super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick);
    }
}