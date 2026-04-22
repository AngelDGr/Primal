package org.primal.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.client.model.entity.SharkModel;
import org.primal.entity.animal.SharkEntity;

public class SharkConduitEyesLayer<T extends SharkEntity, M extends SharkModel<T>> extends RenderLayer<T, M> {

    public SharkConduitEyesLayer(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, @NotNull T animatable, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if(!animatable.hasConduitEyes()) return;

        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.eyes(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID,
                "textures/entity/shark/conduit_eyes"+
                        (animatable.getVariant().equals(SharkEntity.Variant.HAMMERHEAD)? "_hammerhead": animatable.getVariant().equals(SharkEntity.Variant.SLEEPER)? "_sleeper":"") +".png")));


        this.getParentModel().renderToBuffer(poseStack, vertexconsumer, 15728640, OverlayTexture.NO_OVERLAY);
    }
}
