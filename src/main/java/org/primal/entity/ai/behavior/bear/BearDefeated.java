package org.primal.entity.ai.behavior.bear;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.behavior.Behavior;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.BearEntity;

public class BearDefeated extends Behavior<BearEntity> {
    public BearDefeated() {
        super(ImmutableMap.of(), Integer.MAX_VALUE);
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull BearEntity owner) {
        return this.canStillUse(level, owner, 0);
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, @NotNull BearEntity entity, long gameTime) {
        return entity.bearCollapses();
    }

    @Override
    protected void start(@NotNull ServerLevel level, BearEntity entity, long gameTime) {
        if (!entity.isBearSleeping())
            entity.triggerAnim("base_controller", "sleep_start");
        entity.setPose(Pose.CROAKING);
        if(entity.isVehicle()){
            entity.ejectPassengers();
        }
        entity.setBearSleeping(true);
    }

    @Override
    protected void stop(@NotNull ServerLevel level, BearEntity entity, long gameTime) {
        entity.triggerAnim("base_controller", "sleep_end");
        entity.setPose(Pose.STANDING);
        entity.setBearSleeping(false);
    }
}
