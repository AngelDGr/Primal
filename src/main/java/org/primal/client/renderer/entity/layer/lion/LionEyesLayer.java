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

public class LionEyesLayer extends GeoRenderLayer<LionEntity> {
    public LionEyesLayer(GeoRenderer<LionEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, LionEntity lion, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if(lion.isBaby() && Primal_Main.COMMON_CONFIG.lionBabyCustomModel.get()) return;

        //Default glowing eyes
        RenderType eyesrenderType = RenderType.entityTranslucentEmissive(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/lion/"+ lion.getVariant().getSerializedName() +"_eyes.png"));

        //Angry glowing eyes
        if(lion.isAngry() && !lion.isTame())
            eyesrenderType = RenderType.entityTranslucentEmissive(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/lion/angry.png"));

        this.getRenderer().reRender(bakedModel, poseStack, bufferSource, lion, eyesrenderType, bufferSource.getBuffer(eyesrenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    }
}
