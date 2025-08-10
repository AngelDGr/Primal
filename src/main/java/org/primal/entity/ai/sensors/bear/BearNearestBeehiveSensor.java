package org.primal.entity.ai.sensors.bear;

import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.entity.animal.BearEntity;

import com.google.common.collect.ImmutableSet;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;

public class BearNearestBeehiveSensor extends Sensor<BearEntity> {

    @Override
    protected void doTick(@NotNull ServerLevel level, BearEntity entity) {
        entity.getBrain().setMemory(Primal_MemoryModuleTypes.NEAREST_BEEHIVE.get(), BlockPos.findClosestMatch(entity.blockPosition(), 10, 10, block -> entity.level().getBlockState(block).is(BlockTags.BEEHIVES)));
    }

    @Override
    public @NotNull Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(Primal_MemoryModuleTypes.NEAREST_BEEHIVE.get());
    }
    
}
