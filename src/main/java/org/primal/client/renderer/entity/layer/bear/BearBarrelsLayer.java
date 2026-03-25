package org.primal.client.renderer.entity.layer.bear;

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

public class BearBarrelsLayer extends GeoRenderLayer<BearEntity> {

    public BearBarrelsLayer(GeoRenderer<BearEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, BearEntity animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (!animatable.hasChest()) return;
        RenderType barrelsRenderType = RenderType.entityCutoutNoCull(getTexture(animatable));

        this.getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, barrelsRenderType, bufferSource.getBuffer(barrelsRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    }

    private static ResourceLocation getTexture(BearEntity bear){
        return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID,
                "textures/entity/bear/barrels"+ (bear.getVariant().equals(BearEntity.Variant.GROLAR)? "_grolar": "") +".png");
    }
}