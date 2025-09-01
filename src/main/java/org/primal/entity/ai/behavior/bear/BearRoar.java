package org.primal.entity.ai.behavior.bear;

import javax.annotation.Nonnull;

import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.BearEntity;

import com.google.common.collect.ImmutableMap;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Optional;

public final class BearRoar extends Behavior<BearEntity> {

    public BearRoar() {
        super(ImmutableMap.of(
                MemoryModuleType.ROAR_TARGET,
                MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.ATTACK_TARGET,
                MemoryStatus.VALUE_ABSENT),
                45);
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, @NotNull BearEntity bear, long p_217590_) {
        return true;
    }

    @Override
    protected void start(@NotNull ServerLevel level, @Nonnull BearEntity bear, long gameTime) {
        bear.stopMoving();

        Optional<LivingEntity> target = bear.getBrain().getMemory(MemoryModuleType.ROAR_TARGET);
        target.ifPresent(entity -> BehaviorUtils.lookAtEntity(bear, entity));

        bear.setPose(Pose.ROARING);
        bear.triggerAnim("base_controller", "roar");
    }

    @Override
    protected void stop(@NotNull ServerLevel level, BearEntity bear, long gameTime) {
        if (bear.hasPose(Pose.ROARING)) {
            bear.setPose(Pose.STANDING);
        }

        bear.getBrain().getMemory(MemoryModuleType.ROAR_TARGET).ifPresent(bear::setAttackTarget);
        bear.getBrain().eraseMemory(MemoryModuleType.ROAR_TARGET);
    }
}
