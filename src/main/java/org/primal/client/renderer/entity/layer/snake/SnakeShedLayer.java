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
import software.bernie.geckolib.util.Color;

public class SnakeShedLayer extends GeoRenderLayer<SnakeEntity> {
    public SnakeShedLayer(GeoRenderer<SnakeEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, SnakeEntity animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (!animatable.isShedding() || animatable.isInvisible()) return;

        RenderType collarRenderType = RenderType.entityTranslucentCull(
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID,
                        "textures/entity/snake/"+(animatable.isBaby()? "baby/": "")+ "shed" + ".png"));

        this.getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, collarRenderType, bufferSource.getBuffer(collarRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY, Color.WHITE.argbInt());
    }
}
