package org.primal.entity.ai.sensors.bear;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.BearEntity;
import org.primal.registry.Primal_MemoryModuleTypes;

import java.util.Set;

public class BearNearestSweetBerryBushSensor extends Sensor<BearEntity> {

    @Override
    protected void doTick(@NotNull ServerLevel level, BearEntity entity) {
        //Puts the specified memory
        entity.getBrain().setMemory(Primal_MemoryModuleTypes.NEAREST_SWEET_BERRY_BUSH.get(),
                BlockPos.findClosestMatch(entity.blockPosition(), 10, 10,
                        block ->
                        //Is Berry Bush
                        entity.level().getBlockState(block).is(Blocks.SWEET_BERRY_BUSH)
                                //And is at berries age
                                && entity.level().getBlockState(block).hasProperty(SweetBerryBushBlock.AGE)
                                && entity.level().getBlockState(block).getValue(SweetBerryBushBlock.AGE)>=2));
    }

    @Override
    public @NotNull Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(Primal_MemoryModuleTypes.NEAREST_SWEET_BERRY_BUSH.get());
    }
}
