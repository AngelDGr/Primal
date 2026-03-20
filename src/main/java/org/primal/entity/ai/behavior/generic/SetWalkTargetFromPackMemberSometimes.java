package org.primal.entity.ai.behavior.generic;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import org.primal.registry.Primal_MemoryModuleTypes;

import java.util.function.Function;
import java.util.function.Predicate;

public class SetWalkTargetFromPackMemberSometimes {


    public static<T extends Mob> BehaviorControl<T> create(float speedModifier,
                                              float startDistance,
                                              int closeEnoughDist,
                                              UniformInt cooldown,
                                              Predicate<T> canUse) {
        return create(livingEntity -> speedModifier, startDistance, closeEnoughDist, cooldown, canUse);
    }

    public static<T extends Mob> BehaviorControl<T> create(Function<T, Float> speedModifier,
                                              float startDistance,
                                              int closeEnoughDist,
                                              UniformInt cooldown,
                                              Predicate<T> canUse) {
        return BehaviorBuilder.create(
                mobInstance -> mobInstance.group(
                                mobInstance.registered(MemoryModuleType.WALK_TARGET),
                                mobInstance.registered(MemoryModuleType.LOOK_TARGET),
                                mobInstance.present(Primal_MemoryModuleTypes.NEAREST_PACK_MEMBER.get()),
                                mobInstance.absent(Primal_MemoryModuleTypes.GO_TO_PACK_MEMBER_COOLDOWN.get())
                        )
                        .apply(mobInstance, (walkTargetMemoryAccessor, lookTargetMemoryAccessor,
                                             packMemberMemoryAccessor, goToPackMemberCooldown) ->
                                (level, mob, l) -> {
                                    LivingEntity packMember = mobInstance.get(packMemberMemoryAccessor);

                                    //Avoids if too close or can't use it at all
                                    if(mob.distanceTo(packMember)<startDistance || !canUse.test(mob)) return false;

                                    lookTargetMemoryAccessor.set(new EntityTracker(packMember, true));

                                    walkTargetMemoryAccessor.set(new WalkTarget(new EntityTracker(
                                            packMember, false),
                                            speedModifier.apply(mob),
                                            closeEnoughDist));

                                    //Cooldown
                                    goToPackMemberCooldown.setWithExpiry(true, cooldown.sample(level.getRandom()));

                                    return true;
                                }
                        )
        );
    }
}
