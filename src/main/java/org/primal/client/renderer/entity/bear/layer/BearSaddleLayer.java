package org.primal.client.renderer.entity.bear.layer;

import org.jetbrains.annotations.Nullable;
import org.primal.Primal_Main;
import org.primal.entity.animal.BearEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.Color;

public class BearSaddleLayer extends GeoRenderLayer<BearEntity> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/bear/grizzly_bear_saddle.png");

    public BearSaddleLayer(GeoRenderer<BearEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, BearEntity animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (!animatable.isSaddled())
            return;
        RenderType saddleRenderType = RenderType.armorCutoutNoCull(TEXTURE);

        this.getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, saddleRenderType, bufferSource.getBuffer(saddleRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY, Color.WHITE.argbInt());
    }
}
