package org.primal.entity.ai.behavior.bear;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

import java.util.Optional;

public class SetBearLookRoarTarget {
    public static BehaviorControl<LivingEntity> create() {
        return BehaviorBuilder.create(
                instance -> instance.group(
                                instance.registered(MemoryModuleType.LOOK_TARGET),
                                instance.registered(MemoryModuleType.ROAR_TARGET),
                                instance.absent(MemoryModuleType.ATTACK_TARGET)
                        )
                        .apply(instance, (look_target,
                                          roar_target,
                                          attackTarget)
                                -> (level, entity, l) -> {
                            Optional<BlockPos> optionalRoarTarget =
                                    instance
                                    .tryGet(roar_target)
                                    .map(Entity::blockPosition);
                            if (optionalRoarTarget.isEmpty()) {
                                return false;
                            } else {
                                look_target.set(new BlockPosTracker(optionalRoarTarget.get()));
                                return true;
                            }
                        })
        );
    }
}
