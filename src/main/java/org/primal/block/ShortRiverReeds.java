package org.primal.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.primal.block.properties.TripleBlockHalf;
import org.primal.registry.Primal_Blocks;

import javax.annotation.Nullable;

public class ShortRiverReeds extends BushBlock implements BonemealableBlock, SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final MapCodec<ShortRiverReeds> CODEC = simpleCodec(ShortRiverReeds::new);
    public ShortRiverReeds(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        boolean isUnderwater = fluidstate.getType() == Fluids.WATER;

        return super.getStateForPlacement(context).setValue(WATERLOGGED, isUnderwater);
    }

    @Override
    protected @NotNull BlockState updateShape(BlockState state, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor level, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    protected @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        FluidState fluidstate = level.getFluidState(pos);
        return (super.canSurvive(state, level, pos)) || (super.canSurvive(state, level, pos) && fluidstate.is(Fluids.WATER) && fluidstate.getAmount() == 8);
    }

    @Override
    public boolean isValidBonemealTarget(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state) {
        return RiverReeds.isValidPosForRiverReed(level, pos.above());
    }

    @Override
    public boolean isBonemealSuccess(@NotNull Level level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(@NotNull ServerLevel level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        BlockState defaultState = Primal_Blocks.RIVER_REEDS.get().defaultBlockState();

        BlockState lowerRiverReed = defaultState.setValue(RiverReeds.HALF, TripleBlockHalf.LOWER);
        BlockState middleRiverReed = defaultState.setValue(RiverReeds.HALF, TripleBlockHalf.MIDDLE);
        BlockState upperRiverReed = defaultState.setValue(RiverReeds.HALF, TripleBlockHalf.UPPER);

        BlockPos posAbove = pos.above();
        BlockPos posAboveAbove = pos.above().above();

        boolean spawnTriple = level.getRandom().nextBoolean() && level.getFluidState(pos).getType()==Fluids.WATER;

        if(spawnTriple && RiverReeds.isValidPosForRiverReed(level, posAboveAbove)){
            if(level.getFluidState(pos).getType()==Fluids.WATER)
                lowerRiverReed=lowerRiverReed.setValue(WATERLOGGED, true);

            if(level.getFluidState(posAbove).getType()==Fluids.WATER)
                middleRiverReed=middleRiverReed.setValue(WATERLOGGED, true);

            if(level.getFluidState(posAboveAbove).getType()==Fluids.WATER)
                upperRiverReed=upperRiverReed.setValue(WATERLOGGED, true);

            level.setBlock(pos, lowerRiverReed, 2);
            level.setBlock(posAbove, middleRiverReed, 2);
            level.setBlock(posAboveAbove, upperRiverReed, 2);
        } else {
            if(level.getFluidState(pos).getType()==Fluids.WATER)
                middleRiverReed=middleRiverReed.setValue(WATERLOGGED, true);

            if(level.getFluidState(posAbove).getType()==Fluids.WATER)
                upperRiverReed=upperRiverReed.setValue(WATERLOGGED, true);

            level.setBlock(pos, middleRiverReed, 2);
            level.setBlock(posAbove, upperRiverReed, 2);
        }
    }

    @Override
    protected @NotNull MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    @Override
    protected void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (!level.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light

        if (level.getRawBrightness(pos, 0) >= 6) {

            if (level.getRandom().nextIntBetweenInclusive(0, 10)==0) {
                performBonemeal(level, random, pos, state);
            }
        }
    }

    @Override
    protected boolean isRandomlyTicking(@NotNull BlockState state) {
        return state.getValue(WATERLOGGED);
    }
}
