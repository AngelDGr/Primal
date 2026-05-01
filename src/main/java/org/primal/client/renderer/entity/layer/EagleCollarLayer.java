package org.primal.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.client.model.entity.EagleModel;
import org.primal.entity.animal.EagleEntity;

public class EagleCollarLayer<T extends EagleEntity, M extends EagleModel<T>> extends RenderLayer<T, M> {

    public EagleCollarLayer(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, @NotNull T animatable, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!animatable.isTame() || animatable.isInvisible()) return;

        RenderType collarRenderType = RenderType.entityCutoutNoCull(
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID,
                        "textures/entity/eagle/"+(animatable.isBaby()? "baby/": "")+"collar"+ (animatable.isLeftHanded()? "_l": "_r") +".png"));

        float[] i = animatable.getCollarColor().getTextureDiffuseColors();

        this.getParentModel().renderToBuffer(poseStack, bufferSource.getBuffer(collarRenderType), packedLight, OverlayTexture.NO_OVERLAY,
                i[0],
                i[1],
                i[2],
                1);
    }
}
