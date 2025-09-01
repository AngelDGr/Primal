package org.primal.entity.ai.behavior.bear;

import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.BearEntity;

import com.google.common.collect.ImmutableMap;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class BearSleep extends Behavior<BearEntity> {
    public BearSleep() {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET,
                MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.ROAR_TARGET,
                MemoryStatus.VALUE_ABSENT), Integer.MAX_VALUE);
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull BearEntity bear) {
        return canStillUse(level, bear, 0);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, @NotNull BearEntity bear, long gameTime) {
        return level.isNight()
                && !bear.isVehicle()
                && (bear.getLastHurtByMobTimestamp() > bear.tickCount || bear.tickCount-bear.getLastHurtByMobTimestamp() >= 20*10)
                && bear.getAwakeCounter()==0
                && hasRequiredMemories(bear);
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
        entity.setBearSleeping(false);
        //30s of delay + 1-10 extra seconds
        entity.setAwakeCounter(600+entity.getRandom().nextIntBetweenInclusive(20, 200));
    }
}
