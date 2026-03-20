package org.primal.entity.ai.behavior.snake;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import org.primal.block.NestBlock;
import org.primal.block_entity.NestBlockEntity;
import org.primal.entity.animal.SnakeEntity;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.util.Primal_Util;

public class SnakeGoesToNestAndRemovesEdibleEgg {

    public static BehaviorControl<SnakeEntity> create() {
        return BehaviorBuilder.create(
                instance -> instance.group(
                                instance.registered(MemoryModuleType.WALK_TARGET),
                                instance.registered(MemoryModuleType.LOOK_TARGET),
                                instance.present(Primal_MemoryModuleTypes.NEAREST_EDIBLE_EGG.get())
                        )
                        .apply(instance, (walkTarget,
                                          positionTracker,
                                          nearestVisibleEgg) ->
                                (serverLevel, mob, l) -> {
                                    BlockPos nestPos = instance.get(nearestVisibleEgg);

                                    if(!Primal_Util.Ai.canReachPos(mob, nestPos))
                                        return false;

                                    if (mob.distanceToSqr(nestPos.getCenter()) > 1) {

                                        positionTracker.set(new BlockPosTracker(nestPos));
                                        walkTarget.set(new WalkTarget(new BlockPosTracker(nestPos), 1, 0));
                                        return true;

                                    } else {

                                        if (serverLevel.getBlockEntity(nestPos) instanceof NestBlockEntity) {
                                            mob.swing(InteractionHand.MAIN_HAND);
                                            NestBlock.removeEgg(mob, serverLevel, nestPos, serverLevel.getBlockState(nestPos), true);
                                            nearestVisibleEgg.erase();
                                            return true;
                                        }

                                    }

                                    return false;
                                })
        );
    }
}
