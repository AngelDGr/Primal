package org.primal.entity.ai.sensors.bear;

import java.util.Set;

import org.primal.entity.ai.memory.ModMemoryModuleTypes;
import org.primal.entity.animal.Bear;

import com.google.common.collect.ImmutableSet;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;

public class BearNearestBeehiveSensor extends Sensor<Bear> {

    @Override
    protected void doTick(ServerLevel level, Bear entity) {
        entity.getBrain().setMemory(ModMemoryModuleTypes.NEAREST_BEEHIVE.get(), BlockPos.findClosestMatch(entity.blockPosition(), 10, 10, block -> entity.level().getBlockState(block).is(BlockTags.BEEHIVES)));
    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(ModMemoryModuleTypes.NEAREST_BEEHIVE.get());
    }
    
}
