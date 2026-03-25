package org.primal.entity.ai.behavior.generic.pet;

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
import org.primal.util.mob_types.PrimalTamable;

import java.util.Optional;
import java.util.function.Predicate;

public class FollowOwner<T extends TamableAnimal & PrimalTamable> extends Behavior<T> {

    private final Predicate<T> canFollowPredicate;
    private final int distanceToGet;
    private final int distanceStartFollowing;

    public FollowOwner(Predicate<T> canFollow) {
        this(canFollow, 3, 10);
    }

    public FollowOwner() {
        this(pet -> true, 3, 10);
    }

    public FollowOwner(Predicate<T> canFollow, int distanceToGet) {
        this(canFollow, distanceToGet, 20);
    }

    public FollowOwner(Predicate<T> canFollow, int distanceToGet, int distanceStartFollowing) {
        super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT), Integer.MAX_VALUE);

        this.canFollowPredicate=canFollow;
        this.distanceToGet = distanceToGet;
        this.distanceStartFollowing = distanceStartFollowing;
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, @NotNull T pet, long gameTime) {
        return pet.getOwner()!=null
                && pet.getBrain().isActive(Primal_Activities.FOLLOW.get())
                && canFollowPredicate.test(pet)
                && !pet.unableToMoveToOwner()
                && pet.distanceToSqr(pet.getOwner()) >= (double)(this.distanceStartFollowing * this.distanceStartFollowing);
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull T pet) {
        return pet.getOwner()!=null
                && canFollowPredicate.test(pet)
                && !pet.unableToMoveToOwner()
                && pet.distanceToSqr(pet.getOwner()) >= (double)(this.distanceStartFollowing * this.distanceStartFollowing);
    }

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull T pet, long gameTime) {
        start(level, pet, gameTime);
    }

    @Override
    protected void start(@NotNull ServerLevel level, @NotNull T pet, long gameTime) {
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
            BehaviorUtils.lookAtEntity(pet, owner.get());
            pet.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(owner.get(), 1.0f, distanceToGet));
        }
    }
}
