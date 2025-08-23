package org.primal.entity.ai.behavior.bear;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import org.primal.entity.animal.BearEntity;

import java.util.Optional;

public class SetBearRoarTarget {
    public static BehaviorControl<BearEntity> create() {
        return BehaviorBuilder.create(
                instance -> instance.group(
                                instance.absent(MemoryModuleType.ROAR_TARGET),
                                instance.absent(MemoryModuleType.ATTACK_TARGET),
                                instance.registered(MemoryModuleType.NEAREST_ATTACKABLE)
                        )
                        .apply(instance, (roar_target,
                                          attack_target,
                                          nearest_attackable) ->
                                (level, bear, l) -> {

                                    Optional<? extends LivingEntity> optionalTarget = instance.tryGet(nearest_attackable);

                                    if (optionalTarget.isEmpty()) {
                                        return false;
                                    } else if(bear.canTargetEntity(optionalTarget.get())) {
                                        roar_target.set(optionalTarget.get());
                                        return true;
                                    } else {
                                        return false;
                                    }
                                }
                        )
        );
    }
}
