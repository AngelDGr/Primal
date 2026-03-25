package org.primal.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.primal.block.properties.TripleBlockHalf;

public class CattailsBlock extends ThreeTallPlantBlock{

    public CattailsBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void spreadPlant(Level level, BlockPos initialPos, int lateralDistance) {
        BlockPos.MutableBlockPos posToGrow = new BlockPos.MutableBlockPos();

        for (int dx = -lateralDistance; dx <= lateralDistance; dx++) {
            for (int dz = -lateralDistance; dz <= lateralDistance; dz++) {
                posToGrow.set(initialPos.getX() + dx, initialPos.getY(), initialPos.getZ() + dz);
                BlockPos abovePosToGrow = posToGrow.above();
                FluidState fluidState = level.getFluidState(posToGrow);
                FluidState fluidStateAbove = level.getFluidState(abovePosToGrow);

                if (//33% probability of growing
                        level.getRandom().nextIntBetweenInclusive(0, 3)==0
                        //To no replace the main plant
                        && !(posToGrow.getX()==initialPos.getX() && posToGrow.getZ()==initialPos.getZ() && posToGrow.getY()==initialPos.getY())
                        && this.canSurvive(this.defaultBlockState(), level, posToGrow)
                        && canSpreadOn(level, posToGrow, fluidState)
                        && canSpreadOn(level, abovePosToGrow, fluidStateAbove)) {

                    BlockState lowerFinalState= this.defaultBlockState();
                    BlockState upperFinalState= this.defaultBlockState().setValue(ThreeTallPlantBlock.HALF, TripleBlockHalf.UPPER);

                    if(fluidState.is(Fluids.WATER))
                        lowerFinalState= lowerFinalState.setValue(WATERLOGGED, true);
                    if(fluidStateAbove.is(Fluids.WATER))
                        upperFinalState= upperFinalState.setValue(WATERLOGGED, true);


                    level.setBlock(posToGrow, lowerFinalState, 2);
                    level.setBlock(abovePosToGrow, upperFinalState, 2);

                    if(level.getBlockState(posToGrow).getBlock() instanceof CattailsBlock cattailsBlock && level instanceof ServerLevel serverLevel
                            && level.getRandom().nextIntBetweenInclusive(0,3)==0){
                        cattailsBlock.performBonemeal(serverLevel, level.getRandom(), posToGrow, level.getBlockState(posToGrow));
                    }

                }
            }
        }
    }

    private boolean canSpreadOn(Level level, BlockPos posToGrow, FluidState fluidState){
        return (level.getBlockState(posToGrow).isAir() || (fluidState.is(Fluids.WATER)) && fluidState.getAmount() == 8 && level.getBlockState(posToGrow).is(Blocks.WATER));
    }
}
