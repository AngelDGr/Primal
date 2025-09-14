package org.primal.entity.ai.behavior.eagle;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.EagleEntity;
import org.primal.registry.Primal_Blocks;
import org.primal.registry.Primal_MemoryModuleTypes;

public class EagleSearchHome extends Behavior<EagleEntity> {
    public EagleSearchHome() {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET,
                MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.HOME,
                MemoryStatus.VALUE_ABSENT,
                Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get(),
                MemoryStatus.VALUE_PRESENT
        ));
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull EagleEntity eagle) {
        return eagle.getBrain().hasMemoryValue(Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get()) &&
                level.getBlockState(eagle.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get()).get()).is(Primal_Blocks.NEST_BLOCK.get());
    }

    @Override
    protected void start(@NotNull ServerLevel level, @NotNull EagleEntity eagle, long gameTime) {
        if(!checkExtraStartConditions(level, eagle) || eagle.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get()).isEmpty()){
            stop(level, eagle, gameTime);
            return;
        }

        GlobalPos pos = new GlobalPos(level.dimension(), eagle.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get()).get());

        eagle.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        eagle.getBrain().setMemory(MemoryModuleType.WALK_TARGET,
                new WalkTarget(pos.pos(), 1.0f, 3));

        eagle.getBrain().setMemory(MemoryModuleType.HOME, pos);

        level.broadcastEntityEvent(eagle, (byte)14);
    }
}
