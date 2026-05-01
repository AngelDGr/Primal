package org.primal.entity.ai.sensors.crocodile;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.CrocodileEntity;
import org.primal.registry.Primal_Blocks;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.registry.Primal_Tags;

import java.util.Optional;
import java.util.Set;

public class CrocodileNearestImportantBlockSensor extends Sensor<CrocodileEntity> {

    public CrocodileNearestImportantBlockSensor() {
        super(50);
    }

    @Override
    public @NotNull Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get());
    }

    @Override
    protected void doTick(@NotNull ServerLevel level, @NotNull CrocodileEntity entity) {
        BlockPos origin = entity.blockPosition();

        BlockPos bestEgg = null;
        BlockPos bestAttractor = null;

        double bestEggDist = Double.MAX_VALUE;
        double bestAttractorDist = Double.MAX_VALUE;

        for (BlockPos pos : BlockPos.withinManhattan(origin, 24, 4, 24)) {
            double dist = pos.distSqr(origin);

            if (bestEgg == null || dist < bestEggDist) {
                if (isEgg(level, pos)) {
                    bestEgg = pos.immutable();
                    bestEggDist = dist;
                }
            }

            if (bestAttractor == null || dist < bestAttractorDist) {
                if (isAttractor(level, pos)) {
                    bestAttractor = pos.immutable();
                    bestAttractorDist = dist;
                }
            }
        }

        entity.getBrain().setMemory(Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get(), Optional.ofNullable(bestAttractor));
        entity.getBrain().setMemory(Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get(), Optional.ofNullable(bestEgg));
    }

    public boolean isAttractor(@NotNull ServerLevel level, BlockPos blockPos) {
        return level.getBlockState(blockPos).is(Primal_Tags.Block.CROCODILE_ATTRACTORS);
    }

    public boolean isEgg(@NotNull ServerLevel level, BlockPos blockPos) {
        return level.getBlockState(blockPos).is(Primal_Blocks.CROCODILE_EGG.get());
    }
}
