package org.primal.entity.ai.behavior.generic;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.jetbrains.annotations.NotNull;

public class TriesReturnToWater extends Behavior<Mob> {
    public TriesReturnToWater() {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET,
                MemoryStatus.VALUE_ABSENT));
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull Mob owner) {
        return owner.onGround() && !owner.level().getFluidState(owner.blockPosition()).is(FluidTags.WATER);
    }

    @Override
    protected void start(@NotNull ServerLevel level, @NotNull Mob owner, long gameTime) {
        BlockPos blockpos = null;

        for (BlockPos blockPos1 : BlockPos.betweenClosed(
                Mth.floor(owner.getX() - 8.0),
                Mth.floor(owner.getY() - 8.0),
                Mth.floor(owner.getZ() - 8.0),
                Mth.floor(owner.getX() + 8.0),
                owner.getBlockY(),
                Mth.floor(owner.getZ() + 8.0)
        )) {
            if (owner.level().getFluidState(blockPos1).is(FluidTags.WATER)) {
                blockpos = blockPos1;
                break;
            }
        }

        if (blockpos != null) {
            owner.getMoveControl().setWantedPosition(blockpos.getX(), blockpos.getY(), blockpos.getZ(), 1.0);
        }
    }
}
