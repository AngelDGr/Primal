package org.primal.entity.ai.behavior.bear;

import org.primal.entity.animal.Bear;

import com.google.common.collect.ImmutableMap;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class BearSleep extends Behavior<Bear> {
    public BearSleep() {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET,
                MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.ROAR_TARGET,
                MemoryStatus.VALUE_ABSENT), Integer.MAX_VALUE);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, Bear owner) {
        return this.canStillUse(level, owner, 0);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, Bear entity, long gameTime) {
        return level.isNight() && !entity.hasControllingPassenger() && (entity.getLastHurtByMobTimestamp() > entity.tickCount || entity.tickCount-entity.getLastHurtByMobTimestamp() >= 20*10);
    }

    @Override
    protected void start(ServerLevel level, Bear entity, long gameTime) {
        entity.setPose(Pose.CROAKING);
        if (!entity.getSleeping())
            entity.triggerAnim("base_controller", "sleep_start");
        entity.setSleeping(true);
    }

    @Override
    protected void stop(ServerLevel level, Bear entity, long gameTime) {
        entity.setPose(Pose.STANDING);
        if (!entity.hasControllingPassenger())
            entity.triggerAnim("base_controller", "sleep_end");
        entity.setSleeping(false);
    }
}
