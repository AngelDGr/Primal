package org.primal.entity.ai.behavior.generic.bird;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.OneShot;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.phys.Vec3;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.util.Primal_Util;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class BirdStrollFlyGetTired {

    public static<T extends PathfinderMob> OneShot<T> create(float speedModifier,
                                                                                     int xzRange, int yRange,
                                                                                     Predicate<T> canStroll,
                                                                                     UniformInt restNeeded) {
        return BehaviorBuilder.create(
                instance -> instance.group(
                                instance.absent(MemoryModuleType.WALK_TARGET),
                                instance.absent(Primal_MemoryModuleTypes.REST_NEEDED.get()),
                                instance.registered(Primal_MemoryModuleTypes.RESTED_TIME.get()),
                                instance.registered(Primal_MemoryModuleTypes.LANDING_POS.get())
                        )
                        .apply(instance, (walkTargetMemoryAccessor,
                                          restNeededMemoryAccessor,
                                          restedTimeMemoryAccessor,
                                          landingPosMemoryAccessor)
                                -> (serverLevel, mob, l) -> {
                            if (!canStroll.test(mob)) {
                                return false;
                            } else {

                                Function<PathfinderMob, Vec3> target = m-> Primal_Util.Ai.getTargetFlyPos(m, xzRange, yRange);
                                Optional<Vec3> optional = Optional.ofNullable(target.apply(mob));
                                walkTargetMemoryAccessor.setOrErase(optional.map(desiredPos -> new WalkTarget(desiredPos, speedModifier, 0)));

                                Optional<Integer> restedTime = instance.tryGet(restedTimeMemoryAccessor);

                                if(restedTime.isPresent()){
                                    //If above 0, decreases the rested time
                                    if(restedTime.get()>0)
                                        restedTimeMemoryAccessor.setOrErase(Optional.of(restedTime.get() - 1));

                                    if(restedTime.get()==0){
                                        mob.getBrain().setMemory(
                                                Primal_MemoryModuleTypes.REST_NEEDED.get(),
                                                mob.getRandom().nextIntBetweenInclusive(restNeeded.getMinValue(), restNeeded.getMaxValue()));
                                        restedTimeMemoryAccessor.erase();
                                    }
                                }
                                else {
                                    mob.getBrain().setMemory(
                                            Primal_MemoryModuleTypes.REST_NEEDED.get(),
                                            mob.getRandom().nextIntBetweenInclusive(restNeeded.getMinValue(), restNeeded.getMaxValue()));
                                }


                                return true;
                            }
                        })
        );
    }

}
