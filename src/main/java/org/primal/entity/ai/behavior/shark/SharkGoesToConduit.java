package org.primal.entity.ai.behavior.shark;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.block.entity.ConduitBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.SharkEntity;
import org.primal.registry.Primal_MemoryModuleTypes;

public class SharkGoesToConduit extends Behavior<SharkEntity> {
    public SharkGoesToConduit() {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET,
                MemoryStatus.VALUE_ABSENT,
                Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get(),
                MemoryStatus.VALUE_PRESENT,
                Primal_MemoryModuleTypes.NEAREST_CONDUIT_PLAYER.get(),
                MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.ADMIRING_ITEM,
                MemoryStatus.VALUE_ABSENT)
        );
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, SharkEntity owner) {
        return level.getBlockEntity(owner.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get()).get()) instanceof ConduitBlockEntity conduit
                && conduit.isActive();
    }

    @Override
    protected void start(@NotNull ServerLevel level, SharkEntity shark, long gameTime) {
        shark.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        shark.getBrain().setMemory(MemoryModuleType.WALK_TARGET,
                new WalkTarget(shark.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get()).get(),
                        1.0f, 2));
    }

    @Override
    protected void stop(@NotNull ServerLevel level, SharkEntity shark, long gameTime) {
        shark.heal(2);
        shark.getBrain().setMemoryWithExpiry(MemoryModuleType.ADMIRING_ITEM, true, 200L);
    }
}
