package org.primal.entity.ai.behavior.generic.roar;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import org.primal.util.mob_types.AnimalRoars;

import java.util.Optional;
import java.util.function.Predicate;

public class SetRoarTarget {

    public static<T extends Mob & AnimalRoars> BehaviorControl<T> create() {
        return create(e -> true);
    }

    public static<T extends Mob & AnimalRoars> BehaviorControl<T> create(Predicate<T> canUse) {
        return BehaviorBuilder.create(
                instance -> instance.group(
                                instance.absent(MemoryModuleType.ROAR_TARGET),
                                instance.absent(MemoryModuleType.ATTACK_TARGET),
                                instance.registered(MemoryModuleType.NEAREST_ATTACKABLE)
                        )
                        .apply(instance, (roar_target,
                                          attack_target,
                                          nearest_attackable) ->
                                (level, mob, l) -> {

                                    if(canUse.test(mob)){
                                        Optional<? extends LivingEntity> optionalTarget = instance.tryGet(nearest_attackable);

                                        if (optionalTarget.isEmpty()) {
                                            return false;
                                        } else if(!optionalTarget.get().isAlive()){
                                            return false;
                                        } else if(mob.canRoarAtEntity(optionalTarget.get())) {
                                            roar_target.set(optionalTarget.get());
                                            return true;
                                        } else {
                                            return false;
                                        }
                                    }

                                    return false;
                                }
                        )
        );
    }
}
