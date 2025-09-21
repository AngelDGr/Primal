package org.primal.entity.ai.behavior.crocodile;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.CrocodileEntity;
import org.primal.registry.Primal_MemoryModuleTypes;

public class CrocodileBasking extends Behavior<CrocodileEntity> {
    private long endTimestamp=10;

    public CrocodileBasking(int defaultDuration) {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET,
                MemoryStatus.VALUE_ABSENT,
                Primal_MemoryModuleTypes.WAS_BASKING.get(),
                MemoryStatus.VALUE_ABSENT), defaultDuration);
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull CrocodileEntity crocodile) {
        return level.isDay() && !crocodile.isInWater();
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, @NotNull CrocodileEntity entity, long gameTime) {
        return level.isDay() && hasRequiredMemories(entity);
    }

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull CrocodileEntity crocodile, long gameTime) {
        crocodile.stopMoving();
    }

    @Override
    protected void start(@NotNull ServerLevel level, @NotNull CrocodileEntity crocodile, long gameTime) {
        int finalDuration = level.getRandom().nextIntBetweenInclusive(300, 500);

        crocodile.stopMoving();

        crocodile.setPose(Pose.INHALING);

        this.endTimestamp = gameTime + (long)finalDuration;
    }

    @Override
    protected void stop(@NotNull ServerLevel level, @NotNull CrocodileEntity entity, long gameTime) {
        entity.triggerAnim("base_controller", "basking_end");
        entity.setPose(Pose.STANDING);
        //20s of cooldown
        entity.getBrain().setMemoryWithExpiry(Primal_MemoryModuleTypes.WAS_BASKING.get(), true, 400);
    }

    @Override
    protected boolean timedOut(long gameTime) {
        return gameTime > this.endTimestamp;
    }
}
