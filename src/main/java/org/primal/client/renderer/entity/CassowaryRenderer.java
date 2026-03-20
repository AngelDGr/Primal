package org.primal.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.client.model.entity.CassowaryModel;
import org.primal.entity.animal.CassowaryEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class CassowaryRenderer extends GeoEntityRenderer<CassowaryEntity> {
    public CassowaryRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CassowaryModel());

        shadowRadius=0.35F;
    }

    @Override
    protected float getShadowRadius(@NotNull CassowaryEntity entity) {
        float f = super.getShadowRadius(entity);
        return entity.isBaby() ? f * 0.5F : f;
    }

    @Override
    public void scaleModelForRender(float widthScale, float heightScale, PoseStack poseStack, CassowaryEntity animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
        if(!Primal_Main.COMMON_CONFIG.cassowaryBabyCustomModel.get()){
            var bone = model.getBone("head");
            float headScale = animatable.isBaby() ? 2.f : 1.f;
            bone.ifPresent(geoBone ->
                    geoBone.updateScale(headScale, headScale, headScale));
            if (animatable.isBaby()) {
                widthScale = heightScale = .6f;
            }
        }

        super.scaleModelForRender(widthScale, heightScale, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
    }
}
