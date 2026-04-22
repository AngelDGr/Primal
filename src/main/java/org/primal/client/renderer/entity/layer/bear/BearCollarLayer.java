package org.primal.client.renderer.entity.layer.bear;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.client.model.entity.BearModel;
import org.primal.entity.animal.BearEntity;

public class BearCollarLayer<T extends BearEntity, M extends BearModel<T>> extends RenderLayer<T, M> {
    public BearCollarLayer(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, @NotNull T livingEntity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!livingEntity.isTame() || livingEntity.isInvisible()) return;

        RenderType barrelsRenderType = RenderType.entityCutoutNoCull(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/bear/"+(livingEntity.isBaby() && Primal_Main.COMMON_CONFIG.bearBabyCustomModel.get()? "baby/": "")+"collar.png"));

        int i = livingEntity.getCollarColor().getTextureDiffuseColor();

        this.getParentModel().renderToBuffer(poseStack, bufferSource.getBuffer(barrelsRenderType), packedLight, OverlayTexture.NO_OVERLAY, i);
    }
}
