package org.primal.entity.ai.behavior.generic.bird;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;

public class BirdAvoidWhileAscending  {

    public static OneShot<PathfinderMob> entity(MemoryModuleType<? extends Entity> walkTargetAwayFromMemory, float speedModifier, int desiredDistance, boolean hasTarget) {
        return create(walkTargetAwayFromMemory, speedModifier, desiredDistance, hasTarget, Entity::position);
    }

    private static <T> OneShot<PathfinderMob> create(
            MemoryModuleType<T> walkTargetAwayFromMemory, float speedModifier, int desiredDistance, boolean hasTarget, Function<T, Vec3> toPosition
    ) {
        return BehaviorBuilder.create(
                pathfinderMobInstance -> pathfinderMobInstance.group(pathfinderMobInstance.registered(MemoryModuleType.WALK_TARGET), pathfinderMobInstance.present(walkTargetAwayFromMemory))
                        .apply(pathfinderMobInstance, (muWalkTargetMemoryAccessor, muTMemoryAccessor) -> (serverLevel, mob, l) -> {
                            Optional<WalkTarget> optional = pathfinderMobInstance.tryGet(muWalkTargetMemoryAccessor);
                            if (optional.isPresent() && !hasTarget) {
                                return false;
                            } else {
                                Vec3 vec3 = mob.position();
                                Vec3 vec31 = toPosition.apply(pathfinderMobInstance.get(muTMemoryAccessor));
                                if (!vec3.closerThan(vec31, desiredDistance)) {
                                    return false;
                                } else {
                                    if (optional.isPresent() && optional.get().getSpeedModifier() == speedModifier) {
                                        Vec3 vec32 = optional.get().getTarget().currentPosition().subtract(vec3);
                                        Vec3 vec33 = vec31.subtract(vec3);
                                        if (vec32.dot(vec33) < 0.0) {
                                            return false;
                                        }
                                    }

                                    for (int i = 0; i < 10; i++) {
                                        Vec3 vec34 = getPosAway(mob, 25, 25, vec31, 2);
                                        if (vec34 != null) {
                                            muWalkTargetMemoryAccessor.set(new WalkTarget(vec34, speedModifier, 0));
                                            mob.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(vec34));
                                            break;
                                        }
                                    }

                                    return true;
                                }
                            }
                        })
        );
    }

    @Nullable
    public static Vec3 getPosAway(
            PathfinderMob mob,
            int radius,
            int yRange,
            Vec3 avoidPosition,
            double amplifier
    ) {
        // Direction away from the target
        Vec3 direction = mob.position().subtract(avoidPosition);

        boolean restricted = GoalUtils.mobRestricted(mob, radius);

        return RandomPos.generateRandomPos(
                mob,
                () -> generateRandomPosInDirection(
                        mob,
                        radius,
                        yRange,
                        direction,
                        amplifier,
                        restricted
                )
        );
    }

    @Nullable
    private static BlockPos generateRandomPosInDirection(
            PathfinderMob mob,
            int radius,
            int yRange,
            Vec3 direction,
            double amplifier,
            boolean restricted
    ) {
        BlockPos randomDir = RandomPos.generateRandomDirectionWithinRadians(
                mob.getRandom(),
                radius,
                yRange,
                (int) direction.y,
                direction.x,
                direction.z,
                amplifier // controls cone width
        );

        if (randomDir == null) {
            return null;
        }

        BlockPos toward = RandomPos.generateRandomPosTowardDirection(
                mob,
                radius,
                mob.getRandom(),
                randomDir
        );

        if (toward == null
                || GoalUtils.isOutsideLimits(toward, mob)
                || GoalUtils.isRestricted(restricted, mob, toward)) {
            return null;
        }

        toward = RandomPos.moveUpOutOfSolid(
                toward,
                mob.level().getMaxBuildHeight(),
                pos -> GoalUtils.isSolid(mob, pos)
        );

        return GoalUtils.hasMalus(mob, toward) ? null : toward.above(yRange);
    }
}
