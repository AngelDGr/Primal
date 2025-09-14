package org.primal.entity.ai.behavior.eagle;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.EagleEntity;
import org.primal.registry.Primal_Blocks;

public class EagleRemoveHome extends Behavior<EagleEntity> {
    public EagleRemoveHome() {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET,
                MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.HOME,
                MemoryStatus.VALUE_PRESENT
        ));
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull EagleEntity eagle) {
        return eagle.getBrain().hasMemoryValue(MemoryModuleType.HOME) &&
                (!level.getBlockState(eagle.getBrain().getMemory(MemoryModuleType.HOME).get().pos()).is(Primal_Blocks.NEST_BLOCK.get())
                        //Or if it is 150 blocks away while a baby
                        || (eagle.getOnPos().distSqr(eagle.getBrain().getMemory(MemoryModuleType.HOME).get().pos())>22500 && eagle.isBaby()));
    }

    @Override
    protected void start(@NotNull ServerLevel level, @NotNull EagleEntity eagle, long gameTime) {
        eagle.getBrain().eraseMemory(MemoryModuleType.HOME);

        level.broadcastEntityEvent(eagle, (byte)13);
    }
}
