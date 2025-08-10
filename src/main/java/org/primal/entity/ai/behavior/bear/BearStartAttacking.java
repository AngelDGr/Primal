package org.primal.entity.ai.behavior.bear;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.BearEntity;

import com.google.common.collect.ImmutableMap;

import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public final class BearStartAttacking extends Behavior<BearEntity> {
    private final Predicate<BearEntity> canAttackPredicate;
    private final Function<BearEntity, Optional<? extends LivingEntity>> targetFinderFunction;

    public BearStartAttacking(Predicate<BearEntity> canAttack, Function<BearEntity, Optional<? extends LivingEntity>> targetFinder) {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET,
                MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.ROAR_TARGET,
                MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.WALK_TARGET,
                MemoryStatus.VALUE_ABSENT), 50);
        this.canAttackPredicate = canAttack;
        this.targetFinderFunction = targetFinder;
    }

    @SuppressWarnings("null")
    @Override
    protected void start(@NotNull ServerLevel level, @Nonnull BearEntity entity, long gameTime) {
        Optional<? extends LivingEntity> target = targetFinderFunction.apply(entity);
        if (!canAttackPredicate.test(entity) || target.isEmpty()) {
            this.stop(level, entity, gameTime);
            return;
        }

        if (target.get().distanceTo(entity) < 10) {
            entity.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, target);
            return;
        }

        entity.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
        entity.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        entity.getBrain().setMemory(MemoryModuleType.ROAR_TARGET, target.get());
        entity.lookAt(Anchor.FEET, target.get().position());
        entity.setPose(Pose.ROARING);
        entity.triggerAnim("base_controller", "roar");
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, @NotNull BearEntity entity, long gameTime) {
        return true;
    }

    @SuppressWarnings("null")
    @Override
    protected void stop(@NotNull ServerLevel level, BearEntity entity, long gameTime) {
        if (!entity.getBrain().hasMemoryValue(MemoryModuleType.ROAR_TARGET)) {
            return;
        }
        entity.setPose(Pose.STANDING);

        entity.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET,
                entity.getBrain().getMemory(MemoryModuleType.ROAR_TARGET));
        entity.getBrain().eraseMemory(MemoryModuleType.ROAR_TARGET);
    }
}
