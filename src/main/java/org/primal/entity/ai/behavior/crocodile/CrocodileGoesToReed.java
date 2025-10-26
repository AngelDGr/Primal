package org.primal.entity.ai.behavior.crocodile;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.CrocodileEntity;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.registry.Primal_Tags;

public class CrocodileGoesToReed extends Behavior<CrocodileEntity> {

    public CrocodileGoesToReed() {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET,
                MemoryStatus.VALUE_ABSENT,
                Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get(),
                MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.ADMIRING_ITEM,
                MemoryStatus.VALUE_ABSENT)
        );
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, CrocodileEntity owner) {
        return level.getBlockState(owner.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get()).get()).is(Primal_Tags.Block.CROCODILE_ATTRACTORS);
    }

    @Override
    protected void start(@NotNull ServerLevel level, CrocodileEntity crocodile, long gameTime) {
        crocodile.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        crocodile.getBrain().setMemory(MemoryModuleType.WALK_TARGET,
                new WalkTarget(crocodile.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get()).get(),
                        1.0f, 1));
    }

    @Override
    protected void stop(@NotNull ServerLevel level, CrocodileEntity crocodile, long gameTime) {
        crocodile.getBrain().setMemoryWithExpiry(MemoryModuleType.ADMIRING_ITEM, true, level.getRandom().nextIntBetweenInclusive(0, 100)+400L);
    }
}
