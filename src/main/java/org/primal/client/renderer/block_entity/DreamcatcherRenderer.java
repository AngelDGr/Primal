package org.primal.client.renderer.block_entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.properties.Half;
import org.primal.block.DreamcatcherBlock;
import org.primal.block_entity.DreamcatcherBlockEntity;
import org.primal.client.model.block.DreamcatcherModel;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class DreamcatcherRenderer extends GeoBlockRenderer<DreamcatcherBlockEntity> {
    public DreamcatcherRenderer(final BlockEntityRendererProvider.Context ctx) {
        super(new DreamcatcherModel());
    }

//    @Override
//    public @NotNull AABB getRenderBoundingBox(@NotNull DreamcatcherBlockEntity blockEntity) {
//        return super.getRenderBoundingBox(blockEntity).inflate(1);
//    }


    @Override
    public void actuallyRender(PoseStack poseStack, DreamcatcherBlockEntity animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if(animatable.getBlockState().getValue(DreamcatcherBlock.HALF).equals(Half.BOTTOM))
            return;

        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
