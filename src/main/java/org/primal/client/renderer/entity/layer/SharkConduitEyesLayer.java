package org.primal.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.primal.Primal_Main;
import org.primal.entity.animal.SharkEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.Color;

public class SharkConduitEyesLayer extends GeoRenderLayer<SharkEntity> {
    public SharkConduitEyesLayer(GeoRenderer<SharkEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, SharkEntity animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if(!animatable.hasConduitEyes()) return;

        //Default glowing eyes
        RenderType eyesrenderType = RenderType.entityTranslucentEmissive(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID,
                "textures/entity/shark/conduit_eyes"+
                        (animatable.getVariant().equals(SharkEntity.Variant.HAMMERHEAD)? "_hammerhead": animatable.getVariant().equals(SharkEntity.Variant.SLEEPER)? "_sleeper":"") +".png"));

        this.getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, eyesrenderType, bufferSource.getBuffer(eyesrenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY, Color.WHITE.argbInt());
    }
}
