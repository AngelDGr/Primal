package org.primal.entity.ai.behavior.generic;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.jetbrains.annotations.NotNull;

public class BirdReturnHome extends Behavior<LivingEntity> {
    private final int startDistance;
    private final int stopDistance;
    private final boolean hasOffset;

    public BirdReturnHome(int startDistance, int stopDistance, boolean hasOffset) {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET,
                MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.HOME,
                MemoryStatus.VALUE_PRESENT
        ));
        this.startDistance = startDistance;
        this.stopDistance = stopDistance;

        this.hasOffset=hasOffset;
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull LivingEntity eagle) {
        if(eagle.getBrain().getMemory(MemoryModuleType.HOME).isEmpty()){
            return false;
        }

        GlobalPos pos = eagle.getBrain().getMemory(MemoryModuleType.HOME).get();
        return eagle.getOnPos().distSqr(pos.pos())>= startDistance;
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, @NotNull LivingEntity eagle, long gameTime) {
        if(eagle.getBrain().getMemory(MemoryModuleType.HOME).isEmpty()){
            return false;
        }

        GlobalPos pos = eagle.getBrain().getMemory(MemoryModuleType.HOME).get();

        return eagle.getOnPos().distSqr(pos.pos())> stopDistance;
    }

    @Override
    protected void start(@NotNull ServerLevel level, @NotNull LivingEntity eagle, long gameTime) {
        if(eagle.getBrain().getMemory(MemoryModuleType.HOME).isEmpty()){
            stop(level, eagle, gameTime);
            return;
        }

        GlobalPos pos = eagle.getBrain().getMemory(MemoryModuleType.HOME).get();
        BlockPos finalPos= pos.pos();

        if(hasOffset)
            finalPos= pos.pos().offset(
                eagle.getRandom().nextInt(0, 5),
                eagle.getRandom().nextInt(0, 5),
                eagle.getRandom().nextInt(0, 5));

        BehaviorUtils.setWalkAndLookTargetMemories(eagle, finalPos, 1.0f, 0);
    }
}
