package org.primal.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.client.model.entity.DeerModel;
import org.primal.entity.animal.DeerEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class DeerRenderer extends GeoEntityRenderer<DeerEntity> {
    public DeerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DeerModel());
        shadowRadius=0.65f;
    }

    @Override
    protected void applyRotations(DeerEntity deer, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        super.applyRotations(deer, poseStack, ageInTicks, rotationYaw, partialTick, nativeScale);
        if (deer.isJumping()) {
            float f = -Mth.lerp(partialTick, deer.xRotO, deer.getXRot());
            poseStack.mulPose(Axis.XP.rotationDegrees(f));
        }
    }

    @Override
    protected float getShadowRadius(@NotNull DeerEntity entity) {
        float f = super.getShadowRadius(entity);
        return entity.isBaby() ? f * 0.25F : f;
    }

    @Override
    public boolean isShaking(DeerEntity animatable) {
        return super.isShaking(animatable) || animatable.level().isThundering();
    }


    @Override
    public void scaleModelForRender(float widthScale, float heightScale, PoseStack poseStack, DeerEntity animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
        if(!Primal_Main.COMMON_CONFIG.deerBabyCustomModel.get()){
            var bone = model.getBone("head");
            float headScale = animatable.isBaby() ? 1.25f : 1.f;
            bone.ifPresent(geoBone ->
                    geoBone.updateScale(headScale, headScale, headScale));
            if (animatable.isBaby()) {
                widthScale = heightScale = .5f;
            }
        }

        super.scaleModelForRender(widthScale, heightScale, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
    }

    @Override
    public float getMotionAnimThreshold(DeerEntity animatable) {
        return 0.0015f;
    }
}
