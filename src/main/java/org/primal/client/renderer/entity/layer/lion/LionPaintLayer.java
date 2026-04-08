package org.primal.client.renderer.entity.layer.lion;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.primal.Primal_Main;
import org.primal.entity.animal.LionEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class LionPaintLayer extends GeoRenderLayer<LionEntity> {
    public LionPaintLayer(GeoRenderer<LionEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, LionEntity animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (!animatable.isTame() || animatable.isInvisible() || !animatable.hasCollar()) return;

        RenderType collarRenderType = RenderType.entityCutoutNoCull(
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID,
                        "textures/entity/lion/"+(animatable.isBaby() && Primal_Main.COMMON_CONFIG.lionBabyCustomModel.get()? "baby/": "")+ "paint" + ".png"));

        float[] i = animatable.getCollarColor().getTextureDiffuseColors();

        this.getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, collarRenderType, bufferSource.getBuffer(collarRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                i[0],
                i[1],
                i[2],
                1);
    }
}
