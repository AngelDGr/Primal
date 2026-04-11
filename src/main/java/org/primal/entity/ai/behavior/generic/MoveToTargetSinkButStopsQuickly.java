package org.primal.entity.ai.behavior.generic;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import org.jetbrains.annotations.NotNull;
import org.primal.util.Primal_Util;

public class MoveToTargetSinkButStopsQuickly extends MoveToTargetSink {

    private int tryingTicks=0;

    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, @NotNull Mob mob, long time) {

        if(mob.getBrain().getMemory(MemoryModuleType.WALK_TARGET).isPresent()){
            if(!Primal_Util.Ai.canReachPos(mob, mob.getBrain().getMemory(MemoryModuleType.WALK_TARGET).get().getTarget().currentBlockPosition())){
                tryingTicks++;
                if(tryingTicks>30){
                    return false;
                }
            } else
                tryingTicks=0;
        }

        return super.canStillUse(level, mob, time);
    }
}
