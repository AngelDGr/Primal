package org.primal.client.renderer.entity.layer.bear;

import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.client.model.entity.BearModel;
import org.primal.entity.animal.BearEntity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class BearBarrelsLayer<T extends BearEntity, M extends BearModel<T>> extends RenderLayer<T, M> {

    public BearBarrelsLayer(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, @NotNull T livingEntity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!livingEntity.hasChest()) return;
        RenderType barrelsRenderType = RenderType.entityCutoutNoCull(getTexture(livingEntity));

        this.getParentModel().renderToBuffer(poseStack,bufferSource.getBuffer(barrelsRenderType), packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    }

    private static ResourceLocation getTexture(BearEntity bear){
        return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID,
                "textures/entity/bear/barrels"+ (bear.getVariant().equals(BearEntity.Variant.GROLAR)? "_grolar": "") +".png");
    }
}