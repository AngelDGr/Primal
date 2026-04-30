package org.primal.entity.ai.sensors.bear;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.BearEntity;
import org.primal.registry.Primal_MemoryModuleTypes;

import java.util.Optional;
import java.util.Set;

public class BearNearestImportantBlock extends Sensor<BearEntity> {

    public BearNearestImportantBlock() {
        super(50);
    }

    @Override
    public @NotNull Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(Primal_MemoryModuleTypes.NEAREST_SWEET_BERRY_BUSH.get(), Primal_MemoryModuleTypes.NEAREST_BEEHIVE.get());
    }

    @Override
    protected void doTick(@NotNull ServerLevel level, @NotNull BearEntity entity) {
        if(entity.isBearSleeping()) return;

        BlockPos origin = entity.blockPosition();

        BlockPos bestBerry = null;
        BlockPos bestHive = null;

        double bestBerryDist = Double.MAX_VALUE;
        double bestHiveDist = Double.MAX_VALUE;

        for (BlockPos pos : BlockPos.withinManhattan(origin, 10, 6, 10)) {
            double dist = pos.distSqr(origin);

            if (bestBerry == null || dist < bestBerryDist) {
                if (isSweetBerryBush(level, pos)) {
                    bestBerry = pos.immutable();
                    bestBerryDist = dist;
                }
            }

            if (bestHive == null || dist < bestHiveDist) {
                if (isBeehive(level, pos)) {
                    bestHive = pos.immutable();
                    bestHiveDist = dist;
                }
            }
        }

        entity.getBrain().setMemory(Primal_MemoryModuleTypes.NEAREST_SWEET_BERRY_BUSH.get(), Optional.ofNullable(bestBerry));
        entity.getBrain().setMemory(Primal_MemoryModuleTypes.NEAREST_BEEHIVE.get(), Optional.ofNullable(bestHive));
    }

    public boolean isBeehive(@NotNull ServerLevel level, BlockPos blockPos) {
        return level.getBlockState(blockPos).is(BlockTags.BEEHIVES)
                && level.getBlockEntity(blockPos) instanceof BeehiveBlockEntity
                && BeehiveBlockEntity.getHoneyLevel(level.getBlockState(blockPos)) >= 5;
    }

    public boolean isSweetBerryBush(@NotNull ServerLevel level, BlockPos blockPos) {
        return level.getBlockState(blockPos).is(Blocks.SWEET_BERRY_BUSH)
                && level.getBlockState(blockPos).hasProperty(SweetBerryBushBlock.AGE)
                && level.getBlockState(blockPos).getValue(SweetBerryBushBlock.AGE)>=2;
    }
}
