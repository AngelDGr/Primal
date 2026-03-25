package org.primal.client.renderer.entity.layer.snake;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.primal.Primal_Main;
import org.primal.entity.animal.SnakeEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class SnakePaintLayer extends GeoRenderLayer<SnakeEntity> {
    public SnakePaintLayer(GeoRenderer<SnakeEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, SnakeEntity animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (!animatable.isTame() || animatable.isInvisible() || !animatable.hasCollar()) return;

        RenderType collarRenderType = RenderType.entityCutoutNoCull(
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID,
                        "textures/entity/snake/"+(animatable.isBaby()? "baby/": "")+ "marks" + ".png"));

        float[] i = animatable.getCollarColor().getTextureDiffuseColors();

        this.getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, collarRenderType, bufferSource.getBuffer(collarRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                i[0],
                i[1],
                i[2],
                1);
    }
}
