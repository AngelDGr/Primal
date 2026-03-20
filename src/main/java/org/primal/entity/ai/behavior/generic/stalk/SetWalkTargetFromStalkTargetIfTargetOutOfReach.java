package org.primal.entity.ai.behavior.generic.stalk;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import org.primal.registry.Primal_MemoryModuleTypes;

import java.util.Optional;
import java.util.function.Function;

public class SetWalkTargetFromStalkTargetIfTargetOutOfReach {

    public static BehaviorControl<Mob> create(float speedModifier) {
        return create(p_147908_ -> speedModifier);
    }

    public static BehaviorControl<Mob> create(Function<LivingEntity, Float> speedModifier) {
        return BehaviorBuilder.create(
            mobInstance -> mobInstance.group(
                        mobInstance.registered(MemoryModuleType.WALK_TARGET),
                        mobInstance.registered(MemoryModuleType.LOOK_TARGET),
                        mobInstance.present(Primal_MemoryModuleTypes.STALK_TARGET.get()),
                        mobInstance.registered(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)
                    )
                    .apply(mobInstance, (walkTarget, positionTracker, livingEntityMemoryAccessor, nearestVisibleLivingEntity) ->
                            (serverLevel, mob, l) -> {
                            LivingEntity target = mobInstance.get(livingEntityMemoryAccessor);
                            Optional<NearestVisibleLivingEntities> optional = mobInstance.tryGet(nearestVisibleLivingEntity);
                            if (optional.isPresent() && optional.get().contains(target) && BehaviorUtils.isWithinAttackRange(mob, target, 1)) {
                                walkTarget.erase();
                            } else {
                                positionTracker.set(new EntityTracker(target, true));
                                walkTarget.set(new WalkTarget(new EntityTracker(target, false), speedModifier.apply(mob), 0));
                            }

                            return true;
                        })
        );
    }
}
