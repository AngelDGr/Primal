package org.primal.entity.ai.behavior.crocodile;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.CrocodileEntity;
import org.primal.registry.Primal_MemoryModuleTypes;

public class CrocodileExploding extends Behavior<CrocodileEntity> {

    public CrocodileExploding(int maxDuration) {
        super(ImmutableMap.of(
                Primal_MemoryModuleTypes.IS_EXPLODING.get(), MemoryStatus.VALUE_PRESENT),
                maxDuration);
    }

    @Override
    protected void start(@NotNull ServerLevel level, @NotNull CrocodileEntity crocodile, long gameTime) {
        crocodile.setPose(Pose.STANDING);
        crocodile.ejectPassengers();
        crocodile.stopMoving();
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, @NotNull CrocodileEntity crocodile, long gameTime) {
        return checkExtraStartConditions(level, crocodile) && this.hasRequiredMemories(crocodile);
    }

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull CrocodileEntity crocodile, long gameTime) {}

    @Override
    protected void stop(@NotNull ServerLevel level, @NotNull CrocodileEntity crocodile, long gameTime) {
        if(crocodile.hasTNT())
            crocodile.explode();
    }
}
