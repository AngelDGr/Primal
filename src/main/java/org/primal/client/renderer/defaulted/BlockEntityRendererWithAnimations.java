package org.primal.client.renderer.defaulted;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.primal.client.model.block.HierarchicalBlockEntityModel;
import org.primal.util.block_types.AnimatableBlockEntity;

public abstract class BlockEntityRendererWithAnimations<T extends BlockEntity & AnimatableBlockEntity> implements BlockEntityRenderer<T> {

    private final HierarchicalBlockEntityModel<T> model;
    private final ResourceLocation assetSubpath;

    public BlockEntityRendererWithAnimations(HierarchicalBlockEntityModel<T> model, ResourceLocation assetSubpath){
        this.model = model;
        this.assetSubpath = assetSubpath;
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull T entity) {
        return buildFormattedTexturePath(assetSubpath, entity);
    }

    public ResourceLocation buildFormattedTexturePath(ResourceLocation basePath, T entity) {
        return basePath.withPath("textures/block/" + basePath.getPath()+".png");
    }

    @Override
    public void render(@NotNull T entity, float partialTick, PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        poseStack.translate(0.5D, 1.5D, 0.5D);
        poseStack.scale(-1.0F, -1.0F, 1.0F);

        rotateBlock(getFacing(entity), poseStack);

        this.model.setupAnim(entity, partialTick, entity.getBlockState(), entity.getBlockPos(), entity.getAnimationTime(partialTick));
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(entity)));
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay);

        poseStack.popPose();
    }

    /**
     * Rotate the {@link PoseStack} based on the determined {@link Direction} the block is facing
     */
    protected void rotateBlock(Direction facing, PoseStack poseStack) {
        switch (facing) {
            case SOUTH -> poseStack.mulPose(Axis.YP.rotationDegrees(180));
            case WEST -> poseStack.mulPose(Axis.YP.rotationDegrees(90));
            case NORTH -> poseStack.mulPose(Axis.YP.rotationDegrees(0));
            case EAST -> poseStack.mulPose(Axis.YP.rotationDegrees(270));
            case UP -> poseStack.mulPose(Axis.XP.rotationDegrees(90));
            case DOWN -> poseStack.mulPose(Axis.XN.rotationDegrees(90));
        }
    }

    /**
     * Attempt to extract a direction from the block so that the model can be oriented correctly
     */
    protected Direction getFacing(T block) {
        BlockState blockState = block.getBlockState();

        if (blockState.hasProperty(HorizontalDirectionalBlock.FACING))
            return blockState.getValue(HorizontalDirectionalBlock.FACING);

        if (blockState.hasProperty(DirectionalBlock.FACING))
            return blockState.getValue(DirectionalBlock.FACING);

        return Direction.NORTH;
    }
}
