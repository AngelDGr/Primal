package org.primal.client.renderer.block_entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.Half;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.block.DreamcatcherBlock;
import org.primal.block_entity.DreamcatcherBlockEntity;
import org.primal.client.model.block.DreamcatcherModel;
import org.primal.client.renderer.defaulted.BlockEntityRendererWithAnimations;

public class DreamcatcherRenderer extends BlockEntityRendererWithAnimations<DreamcatcherBlockEntity> {
    public DreamcatcherRenderer(BlockEntityRendererProvider.Context context) {
        super(new DreamcatcherModel(context.bakeLayer(DreamcatcherModel.LAYER_LOCATION)), ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "dreamcatcher"));
    }

//    @Override
//    public @NotNull AABB getRenderBoundingBox(@NotNull DreamcatcherBlockEntity blockEntity) {
//        return super.getRenderBoundingBox(blockEntity).inflate(1);
//    }

    @Override
    public void render(@NotNull DreamcatcherBlockEntity animatable, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if(animatable.getBlockState().getValue(DreamcatcherBlock.HALF).equals(Half.BOTTOM))
            return;

        poseStack.pushPose();
        boolean hanging = animatable.getBlockState().getValue(DreamcatcherBlock.HANGING);
        switch (getFacing(animatable)){
            case EAST ->  poseStack.translate((hanging?  7.075f:  13.975f)/16f, 0f/16f, 0f/16f);
            case WEST ->  poseStack.translate((hanging? -7.075f: -13.975f)/16f, 0f/16f, 0f/16f);
            case NORTH ->  poseStack.translate(0f/16f, 0f/16f, (hanging?  7.075f:  0.175f)/16f);
            case SOUTH ->  poseStack.translate(0f/16f, 0f/16f, (hanging? -7.075f: -0.175f)/16f);
        }
        super.render(animatable, partialTick, poseStack, bufferSource, packedLight, packedOverlay);

        poseStack.popPose();
    }
}
