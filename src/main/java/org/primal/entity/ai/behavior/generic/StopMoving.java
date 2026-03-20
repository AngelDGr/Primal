package org.primal.entity.ai.behavior.generic;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;

import java.util.Optional;

public class StopMoving {
    public static BehaviorControl<Mob> create() {
        return BehaviorBuilder.create(
                instance -> instance.group(
                                instance.registered(MemoryModuleType.WALK_TARGET)
                        )
                        .apply(instance, (walk_target)
                                -> (level, entity, l) -> {
                            Optional<WalkTarget> optionalWalkTarget = instance.tryGet(walk_target);

                            if(optionalWalkTarget.isPresent())
                                walk_target.erase();

                            entity.getNavigation().stop();
                            return true;
                        })
        );
    }
}
