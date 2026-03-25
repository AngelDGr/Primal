package org.primal.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.primal.registry.Primal_Blocks;

public class RiverReedBlock extends ThreeTallPlantBlock{
    public RiverReedBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void spreadPlant(Level level, BlockPos initialPos, int lateralDistance) {
        BlockPos.MutableBlockPos check = new BlockPos.MutableBlockPos();

        for (int dx = -lateralDistance; dx <= lateralDistance; dx++) {
            for (int dz = -lateralDistance; dz <= lateralDistance; dz++) {
                check.set(initialPos.getX() + dx, initialPos.getY(), initialPos.getZ() + dz);

                ShortRiverReeds reeds= (ShortRiverReeds) Primal_Blocks.SHORT_RIVER_REEDS.get();
                FluidState fluidstate = level.getFluidState(check);
                if (reeds.canSurvive(reeds.defaultBlockState(), level, check)
                        && level.getRandom().nextIntBetweenInclusive(0, 3)==0
                        && !(check.getX()==initialPos.getX() && check.getZ()==initialPos.getZ() && check.getY()==initialPos.getY())
                        && (level.getBlockState(check).isAir() || (fluidstate.is(Fluids.WATER)) && fluidstate.getAmount() == 8 && level.getBlockState(check).is(Blocks.WATER))) {

                    BlockState finalState= reeds.defaultBlockState();
                    if(fluidstate.is(Fluids.WATER)){
                        finalState= finalState.setValue(WATERLOGGED, true);
                    }

                    level.setBlock(check, finalState, 2);

                    if(level.getBlockState(check).getBlock() instanceof ShortRiverReeds shortRiverReeds && level instanceof ServerLevel serverLevel
                            && level.getRandom().nextIntBetweenInclusive(0,3)==0)
                        shortRiverReeds.performBonemeal(serverLevel, level.getRandom(), check, level.getBlockState(check));

                }
            }
        }
    }
}
