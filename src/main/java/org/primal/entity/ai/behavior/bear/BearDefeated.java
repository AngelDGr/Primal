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
    protected void start(@NotNull ServerLevel level, BearEntity bear, long gameTime) {
        bear.stopMoving();

        if (!bear.isBearSleeping())
            bear.triggerAnim("base_controller", "sleep_start");
        bear.setPose(Pose.CROAKING);
        if(bear.isVehicle()){
            bear.ejectPassengers();
        }
        bear.setBearSleeping(true);
    }

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull BearEntity bear, long gameTime) {
        bear.stopMoving();
    }

    @Override
    protected void stop(@NotNull ServerLevel level, BearEntity entity, long gameTime) {
        entity.triggerAnim("base_controller", "sleep_end");
        entity.setPose(Pose.STANDING);
        if(entity.getWakeUpSound()!=null)
            entity.playSound(entity.getWakeUpSound(), 1,0.8f+ (entity.getRandom().nextIntBetweenInclusive(0, 2)*0.1f));
        entity.setBearSleeping(false);
    }
}
