package org.primal.entity.ai.behavior.generic;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.mutable.MutableLong;

public class TryFindWaterSurface {

    public static BehaviorControl<PathfinderMob> create(int range, float speedModifier) {
        MutableLong mutablelong = new MutableLong(0L);
        return BehaviorBuilder.create(
            pathfinderMobInstance -> pathfinderMobInstance.group(
                        pathfinderMobInstance.absent(MemoryModuleType.ATTACK_TARGET),
                        pathfinderMobInstance.absent(MemoryModuleType.WALK_TARGET),
                        pathfinderMobInstance.registered(MemoryModuleType.LOOK_TARGET)
                    )
                    .apply(
                        pathfinderMobInstance,
                        (attackTargetAccessor, walkTargetAccessor, lookTargetAccessor)
                                -> (serverLevel, mob, l) -> {
                                //If it's not underwater, or is not in the water at all, just doesn't tries to find surface
                                if (!mob.isUnderWater() || !mob.isInWater()) {

                                    return false;
                                }
                                else if (l < mutablelong.getValue()) {
                                    mutablelong.setValue(l + 40L);
                                    return true;
                                }
                                else {
                                    BlockPos blockpos = mob.blockPosition();

                                    for (BlockPos blockPos1 : BlockPos.withinManhattan(blockpos, range, range, range)) {
                                        if (blockPos1.getX() != blockpos.getX() || blockPos1.getZ() != blockpos.getZ()) {
                                            BlockState blockState = serverLevel.getBlockState(blockPos1);
                                            if (blockState.is(Blocks.WATER)
                                                && serverLevel.getFluidState(blockPos1).is(FluidTags.WATER)
                                                && serverLevel.getBlockState(blockPos1.above()).isAir()) {

                                                lookTargetAccessor.set(new BlockPosTracker(blockPos1));
                                                walkTargetAccessor.set(new WalkTarget(new BlockPosTracker(blockPos1), speedModifier, 0));
                                                break;
                                            }
                                        }
                                    }

                                    mutablelong.setValue(l + 40L);
                                    return true;
                                }
                            }
                    )
        );
    }
}
