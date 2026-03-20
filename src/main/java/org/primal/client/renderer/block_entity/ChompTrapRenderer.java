package org.primal.client.renderer.block_entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import org.jetbrains.annotations.Nullable;
import org.primal.block_entity.ChompTrapBlockEntity;
import org.primal.client.model.block.ChompTrapModel;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

@SuppressWarnings("unused")
public class ChompTrapRenderer extends GeoBlockRenderer<ChompTrapBlockEntity> {
    public ChompTrapRenderer(final BlockEntityRendererProvider.Context ctx) {
        super(new ChompTrapModel());
    }

    @Override
    public void actuallyRender(PoseStack poseStack, ChompTrapBlockEntity animatable, BakedGeoModel model, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }
}
