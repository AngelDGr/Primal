package org.primal.entity.ai.behavior.generic.hide_on_log;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.jetbrains.annotations.NotNull;
import org.primal.block_entity.HollowLogBlockEntity;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.util.mob_types.HideOnLog;

public class AnimalEntersLog<T extends LivingEntity & HideOnLog> extends Behavior<T> {

    private final UniformInt ticksWantedOnLog;
    private final boolean cooldownMatters;

    public AnimalEntersLog(UniformInt ticksWantedOnLog, boolean cooldownMatters) {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET,
                MemoryStatus.VALUE_ABSENT,
                Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get(),
                MemoryStatus.VALUE_PRESENT)
        );
        this.ticksWantedOnLog=ticksWantedOnLog;
        this.cooldownMatters=cooldownMatters;
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull T owner) {
        var hasBlock = owner.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get()).isPresent();

        //Prevents if cooldown
        if(cooldownMatters && owner.getBrain().getMemory(Primal_MemoryModuleTypes.HOLLOW_LOG_ENTER_COOLDOWN.get()).isPresent()) return false;

        if(hasBlock){
            return owner.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get()).get().closerToCenterThan(owner.position(), 2);
        }

        return false;
    }

    @Override
    protected void start(@NotNull ServerLevel level, @NotNull T entity, long gameTime) {
        var importantBlock = entity.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get());

        if (importantBlock.isPresent() && entity.level().getBlockEntity(importantBlock.get()) instanceof HollowLogBlockEntity hollowLogBlockEntity) {
            hollowLogBlockEntity.addOccupant(entity, entity.getRandom().nextIntBetweenInclusive(ticksWantedOnLog.getMinValue(), ticksWantedOnLog.getMaxValue()), false);
        }
    }

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull T owner, long gameTime) {
        super.tick(level, owner, gameTime);
    }

    @Override
    protected void stop(@NotNull ServerLevel level, @NotNull T entity, long gameTime) {
        super.stop(level, entity, gameTime);
    }
}
