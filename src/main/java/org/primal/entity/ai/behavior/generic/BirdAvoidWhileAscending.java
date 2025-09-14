package org.primal.entity.ai.behavior.generic;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.jetbrains.annotations.NotNull;

public class BirdAvoidWhileAscending extends Behavior<LivingEntity> {
    public BirdAvoidWhileAscending() {
        super(ImmutableMap.of(
                MemoryModuleType.AVOID_TARGET,
                MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.IS_PANICKING,
                MemoryStatus.VALUE_ABSENT
        ));
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull LivingEntity owner) {
        return !owner.isBaby();
    }

    @Override
    protected void start(@NotNull ServerLevel level, @NotNull LivingEntity entity, long gameTime) {

        BehaviorUtils.setWalkAndLookTargetMemories(entity,  entity.getOnPos().offset(
                level.random.nextInt(0, 5),
                level.random.nextInt(3, 8),
                level.random.nextInt(0, 5)) ,1, 4);
    }

    @Override
    protected void stop(@NotNull ServerLevel level, @NotNull LivingEntity entity, long gameTime) {
        entity.getBrain().setMemoryWithExpiry(MemoryModuleType.IS_PANICKING, true, 80);
    }
}
