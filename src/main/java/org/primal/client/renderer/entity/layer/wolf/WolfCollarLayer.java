package org.primal.client.renderer.entity.layer.wolf;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;
import org.jetbrains.annotations.Nullable;
import org.primal.Primal_Main;
import org.primal.entity.replaced.WolfReplaced;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoReplacedEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class WolfCollarLayer extends GeoRenderLayer<WolfReplaced> {

    private final GeoReplacedEntityRenderer<Wolf, WolfReplaced> renderer;
    public WolfCollarLayer(GeoReplacedEntityRenderer<Wolf, WolfReplaced> entityRendererIn) {
        super(entityRendererIn);
        this.renderer=entityRendererIn;
    }

    @Override
    public void render(PoseStack poseStack, WolfReplaced animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        Wolf wolf = renderer.getCurrentEntity();

        if (!wolf.isTame()) return;

//        RenderType collarRenderType = RenderType.entityCutoutNoCull(
//                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/wolf/collar"+ WolfRenderer.addSlimSuffix(wolf.getVariant())+ ".png"));
        RenderType collarRenderType = RenderType.entityCutoutNoCull(
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/wolf/collar.png"));

        float[] i = wolf.getCollarColor().getTextureDiffuseColors();

        this.getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, collarRenderType, bufferSource.getBuffer(collarRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                i[0],
                i[1],
                i[2],
                1);
    }
}
