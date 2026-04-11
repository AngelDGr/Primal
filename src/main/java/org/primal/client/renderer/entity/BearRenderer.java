package org.primal.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLLoader;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.client.model.entity.BearModel;
import org.primal.client.renderer.entity.layer.bear.BearBarrelsLayer;
import org.primal.client.renderer.entity.layer.bear.BearCollarLayer;
import org.primal.client.renderer.entity.layer.bear.BearHoneyLayer;
import org.primal.client.renderer.entity.layer.bear.BearSleepLayer;
import org.primal.compat.DomesticationInnovationCompat;
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
        if(FMLLoader.getLoadingModList().getModFileById("domesticationinnovation")!=null) DomesticationInnovationCompat.addEnchantmentsLayer(this);

        shadowRadius=1.0F;
    }

    @Override
    public void render(@NotNull BearEntity entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        if (entity.isBaby()) this.shadowRadius *= 0.35F;
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public float getMotionAnimThreshold(BearEntity animatable) {
        return animatable.isInWater()? 0.0f: 0.0015f;
    }

    @Override
    protected void applyRotations(BearEntity animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick) {
        if(animatable.isInWater() && !animatable.isVehicle() && !animatable.isBearSleeping())
            Primal_Util.Visuals.bodyFullRotations(animatable, partialTick, poseStack);
        else
            super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick);
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