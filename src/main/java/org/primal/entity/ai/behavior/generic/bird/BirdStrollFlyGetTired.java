package org.primal.entity.ai.behavior.generic.bird;

import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.OneShot;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.util.Primal_Util;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class BirdStrollFlyGetTired {

    public static<T extends PathfinderMob> OneShot<T> create(float speedModifier,
                                                                                     int xzBigRange, int yBigRange,
                                                                                     int xzSmallRange, int ySmallRange,
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

                                Function<PathfinderMob, Vec3> target = m -> {
                                    int attempts = 0;

                                    while (attempts < 15) {
                                        boolean useLand = attempts > 10;
                                        boolean useSmall = attempts > 5;

                                        Vec3 wantedPos = useLand?  LandRandomPos.getPos(mob, xzSmallRange, ySmallRange) :
                                                      useSmall? Primal_Util.Ai.getTargetFlyPos(m, xzSmallRange, ySmallRange):
                                                                Primal_Util.Ai.getTargetFlyPos(m, xzBigRange, yBigRange);

                                        if (wantedPos != null && Primal_Util.Ai.canReachPos(m, BlockPos.containing(wantedPos)))
                                            return wantedPos;

                                        attempts++;
                                    }

                                    return null; // nothing valid found after 10 tries
                                };
                                Optional<Vec3> possiblePos = Optional.ofNullable(target.apply(mob));
                                walkTargetMemoryAccessor.setOrErase(possiblePos.map(desiredPos -> new WalkTarget(desiredPos, speedModifier, 0)));

                                if (possiblePos.isEmpty()) return true;

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
