package org.primal.client.renderer.entity.layer.wolf;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.client.model.entity.replaced.WolfModel;
import org.primal.client.renderer.replaced.WolfRenderer;

public class WolfCollarLayer<T extends Wolf, M extends WolfModel<T>> extends RenderLayer<T, M> {

    public WolfCollarLayer(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, @NotNull T livingEntity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!livingEntity.isTame()) return;

        RenderType collarRenderType = RenderType.entityCutoutNoCull(
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/wolf/collar"+ WolfRenderer.addSlimSuffix(livingEntity)+ ".png"));

        float[] i = livingEntity.getCollarColor().getTextureDiffuseColors();

        this.getParentModel().renderToBuffer(poseStack, bufferSource.getBuffer(collarRenderType), packedLight, OverlayTexture.NO_OVERLAY,
                i[0],
                i[1],
                i[2],
                1);
    }
}
