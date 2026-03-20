package org.primal.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.client.model.entity.LionModel;
import org.primal.client.renderer.entity.layer.lion.LionEyesLayer;
import org.primal.client.renderer.entity.layer.lion.LionPaintLayer;
import org.primal.entity.animal.LionEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class LionRenderer extends GeoEntityRenderer<LionEntity> {
    public LionRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new LionModel());
        this.addRenderLayer(new LionEyesLayer(this));
        this.addRenderLayer(new LionPaintLayer(this));

        shadowRadius=0.75F;
    }

    @Override
    protected float getShadowRadius(@NotNull LionEntity entity) {
        float f = super.getShadowRadius(entity);
        return entity.isBaby() ? f * 0.25F : f;
    }

    @Override
    public void scaleModelForRender(float widthScale, float heightScale, PoseStack poseStack, LionEntity animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
        if(!Primal_Main.COMMON_CONFIG.lionBabyCustomModel.get()){
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
