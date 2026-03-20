package org.primal.entity.ai.behavior.generic;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.primal.util.Primal_Util;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class TryFindWaterSurface {

    public static<E extends Mob> BehaviorControl<E> create(int range, float speedModifier, float airThresholdWithoutTarget) {
        return create(range, speedModifier, airThresholdWithoutTarget, Primal_Util.Ai.MIN_LOW_AIR_DEFAULT);
    }

    public static<E extends Mob> BehaviorControl<E> create(int range, float speedModifier, float airThresholdWithoutTarget, int minAir) {
        return create(range, speedModifier, m-> (m.getTarget()==null && m.getAirSupply()<m.getMaxAirSupply()*airThresholdWithoutTarget) || m.getAirSupply()<minAir, m->{});
    }

    public static<E extends Mob> BehaviorControl<E> create(int range, float speedModifier, Predicate<E> whenStart) {
        return create(range, speedModifier, whenStart, m->{});
    }

    public static<E extends Mob> BehaviorControl<E> create(int range, float speedModifier, Predicate<E> whenStart, Consumer<E> extraFunction) {
        return BehaviorBuilder.create(
            pathfinderMobInstance -> pathfinderMobInstance.group(
                        pathfinderMobInstance.registered(MemoryModuleType.WALK_TARGET),
                        pathfinderMobInstance.registered(MemoryModuleType.LOOK_TARGET)
                    )
                    .apply(
                        pathfinderMobInstance,
                        (walkTargetAccessor, lookTargetAccessor)
                                -> (serverLevel, mob, l) -> {
                                if(mob.isUnderWater() && mob.isInWater() && whenStart.test(mob)){
                                    BlockPos blockpos = mob.blockPosition();
                                    BlockPos fallback = blockpos.above();
                                    BlockPos foundSurface = null;

                                    for (BlockPos blockPos1 : BlockPos.withinManhattan(blockpos, range, range, range)) {
                                        if (blockPos1.getX() != blockpos.getX() || blockPos1.getZ() != blockpos.getZ()) {

                                            BlockState blockState = serverLevel.getBlockState(blockPos1);

                                            if (blockState.is(Blocks.WATER)
                                                    && serverLevel.getFluidState(blockPos1).is(FluidTags.WATER)
                                                    && serverLevel.getBlockState(blockPos1.above()).isAir()) {

                                                foundSurface = blockPos1.above();
                                                break;
                                            }
                                        }
                                    }

                                    BlockPos targetPos = foundSurface != null ? foundSurface : fallback;

                                    extraFunction.accept(mob);
                                    lookTargetAccessor.set(new BlockPosTracker(targetPos));
                                    walkTargetAccessor.set(new WalkTarget(new BlockPosTracker(targetPos), speedModifier, 0));

                                    return true;
                                }

                                return false;
                            }
                    )
        );
    }
}
