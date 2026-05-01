package org.primal.client.renderer.entity.layer.walrus;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.client.model.entity.WalrusModel;
import org.primal.entity.animal.WalrusEntity;

public class WalrusSaddleLayer<T extends WalrusEntity, M extends WalrusModel<T>> extends RenderLayer<T, M> {

    public WalrusSaddleLayer(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, @NotNull T entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entity.isSaddled()) return;
        RenderType barrelsRenderType = RenderType.entityCutoutNoCull(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/walrus/saddle.png"));

        this.getParentModel().renderToBuffer(poseStack, bufferSource.getBuffer(barrelsRenderType), packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    }
}