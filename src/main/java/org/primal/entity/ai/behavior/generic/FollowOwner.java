package org.primal.entity.ai.behavior.generic;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import org.jetbrains.annotations.NotNull;
import org.primal.registry.Primal_Activities;

import java.util.Optional;
import java.util.function.Predicate;

public class FollowOwner extends Behavior<TamableAnimal> {

    private final Predicate<TamableAnimal> canFollowPredicate;

    private final int minDistance;

    private final int distanceStopFollowingBehavior;

    public FollowOwner(Predicate<TamableAnimal> canFollow) {
        this(canFollow, 2, 20);
    }

    public FollowOwner(Predicate<TamableAnimal> canFollow, int minDistance) {
        this(canFollow, minDistance, 20);
    }

    public FollowOwner(Predicate<TamableAnimal> canFollow, int minDistance, int distanceStopFollowingBehavior) {
        super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT), Integer.MAX_VALUE);

        this.canFollowPredicate=canFollow;
        this.minDistance = minDistance;
        this.distanceStopFollowingBehavior = distanceStopFollowingBehavior;
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, @NotNull TamableAnimal pet, long gameTime) {
        return pet.getOwner()!=null
                && pet.getBrain().isActive(Primal_Activities.FOLLOW.get())
                && canFollowPredicate.test(pet)
                && !pet.unableToMoveToOwner()
                && !(pet.distanceToSqr(pet.getOwner()) <= (double)(this.distanceStopFollowingBehavior * this.distanceStopFollowingBehavior));
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull TamableAnimal pet) {
        return pet.getOwner()!=null
                && canFollowPredicate.test(pet)
                && !pet.unableToMoveToOwner();
    }

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull TamableAnimal pet, long gameTime) {
        start(level, pet, gameTime);
    }

    @Override
    protected void start(@NotNull ServerLevel level, @NotNull TamableAnimal pet, long gameTime) {
        if(!canFollowPredicate.test(pet)){
            stop(level, pet, gameTime);
            return;
        }

        Optional<LivingEntity> owner = Optional.ofNullable(pet.getOwner());
        if(owner.isEmpty()){
            stop(level, pet, gameTime);
            return;
        }

        if(pet.shouldTryTeleportToOwner()){
            pet.tryToTeleportToOwner();
        } else {
            pet.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(owner.get(), 1.0f, minDistance));
            BehaviorUtils.lookAtEntity(pet, owner.get());
        }
    }
}
