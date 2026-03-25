package org.primal.client.renderer.block_entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.primal.block.Rotation16Block;
import org.primal.block_entity.Rotation16BlockEntity;

public class Rotation16BlockEntityRenderer implements BlockEntityRenderer<Rotation16BlockEntity> {
    private final BlockRenderDispatcher blockRenderer;

    public Rotation16BlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderer= context.getBlockRenderDispatcher();
    }

    @Override
    public void render(@NotNull Rotation16BlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockState state = blockEntity.getBlockState();
        BlockPos pos = blockEntity.getBlockPos();
        BlockAndTintGetter level = blockEntity.getLevel();

        BakedModel model = blockRenderer.getBlockModel(state);

        if(level==null || blockEntity.getLevel()==null) return;

        poseStack.pushPose();

        // rotation
        poseStack.translate(0.5D, 0.5D, 0.5D);
        if (state.getBlock() instanceof Rotation16Block)
            poseStack.mulPose(Axis.YP.rotationDegrees(-22.5F * state.getValue(Rotation16Block.ROTATION)));
        poseStack.translate(-0.5D, -0.5D, -0.5D);

        ModelData modelData = model.getModelData(level, pos, state, ModelData.EMPTY);
        long seed = state.getSeed(pos);

        blockRenderer.getModelRenderer().tesselateBlock(
                level,
                model,
                state,
                pos,
                poseStack,
                bufferSource.getBuffer(RenderType.cutout()),
                false,
                blockEntity.getLevel().getRandom(),
                seed,
                OverlayTexture.NO_OVERLAY,
                modelData,
                RenderType.cutout()
        );

        poseStack.popPose();
    }

}
