package org.primal.client.model.block;

import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.primal.util.block_types.AnimatableBlockEntity;

public abstract class HierarchicalBlockEntityModel<T extends BlockEntity & AnimatableBlockEntity> extends HierarchicalModel<Entity> {
    protected static final Vector3f ANIM_VECTOR_CACHE = new Vector3f();
    @Override
    public final void setupAnim(@NotNull Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {}

    public abstract void setupAnim(T blockEntity, float partialTick, BlockState blockState, BlockPos blockPos, float ageInPartialTicks);

    protected boolean isStateBetweenMS(AnimationState state, long startTicks, long endTicks) {
        return state.getAccumulatedTime()>=startTicks && state.getAccumulatedTime()<=endTicks;
    }

    protected void animate(T blockEntity, float partialTick, AnimationDefinition animation) {
        float animTimeTicks = blockEntity.getAnimationTime(partialTick);
        float animTimeSeconds = animTimeTicks / 20.0F;
        long animTimeMs = (long)(animTimeSeconds * 1000.0F);
        KeyframeAnimations.animate(this, animation, animTimeMs, 1.0F, ANIM_VECTOR_CACHE);
    }
}
