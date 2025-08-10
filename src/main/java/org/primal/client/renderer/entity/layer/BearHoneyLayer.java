package org.primal.client.renderer.entity.layer;

import org.jetbrains.annotations.Nullable;
import org.primal.Primal_Main;
import org.primal.entity.animal.Bear;

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

public class BearHoneyLayer extends GeoRenderLayer<Bear> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Primal_Main.MODID, "textures/entity/bear/grizzly_bear_honey_texture.png");

    public BearHoneyLayer(GeoRenderer<Bear> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, Bear animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (animatable.getHoneyCounter() <= 0)
            return;
        RenderType honeyRenderType = RenderType.entityCutoutNoCull(TEXTURE);

        this.getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, honeyRenderType, bufferSource.getBuffer(honeyRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY, Color.WHITE.argbInt());
    }

}
