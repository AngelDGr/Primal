package org.primal.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.primal.block.properties.TripleBlockHalf;
import org.primal.registry.Primal_Blocks;
import org.primal.registry.Primal_Tags;

import javax.annotation.Nullable;

public class RiverReeds extends BushBlock implements BonemealableBlock, SimpleWaterloggedBlock {
    public static final EnumProperty<TripleBlockHalf> HALF = EnumProperty.create("half", TripleBlockHalf.class);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_1;

    public static final MapCodec<RiverReeds> CODEC = simpleCodec(RiverReeds::new);
    public RiverReeds(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(HALF, TripleBlockHalf.LOWER)
                .setValue(WATERLOGGED, false)
                .setValue(AGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HALF).add(WATERLOGGED).add(AGE);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        boolean isUnderwater = fluidstate.getType() == Fluids.WATER;

        BlockState finalState = super.getStateForPlacement(context).setValue(WATERLOGGED, isUnderwater);

        return finalState.setValue(HALF, TripleBlockHalf.LOWER);
    }

    @Override
    public void setPlacedBy(@NotNull Level level, BlockPos pos, @NotNull BlockState state, LivingEntity placer, @NotNull ItemStack stack) {
        BlockPos blockPosAbove = pos.above();
        BlockPos blockPosAboveAbove = pos.above().above();

        //Put the other parts above
        if(state.getValue(HALF)==TripleBlockHalf.LOWER && !level.isClientSide){
            if(state.getValue(WATERLOGGED) && level.getRandom().nextBoolean()){
                level.setBlock(blockPosAbove, copyWaterloggedFrom(level, blockPosAbove, this.defaultBlockState().setValue(HALF, TripleBlockHalf.MIDDLE)), 3);
                level.setBlock(blockPosAboveAbove, copyWaterloggedFrom(level, blockPosAboveAbove, this.defaultBlockState().setValue(HALF, TripleBlockHalf.UPPER)), 3);
            } else {
                level.setBlock(blockPosAbove, copyWaterloggedFrom(level, blockPosAbove, this.defaultBlockState().setValue(HALF, TripleBlockHalf.UPPER)), 3);
            }
        }
    }

    @Override
    protected @NotNull BlockState updateShape(BlockState state, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor level, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        TripleBlockHalf half = state.getValue(HALF);

        //For lower and middle part, checks if the block above is another reed, otherwise breaks
        if((half==TripleBlockHalf.LOWER || half==TripleBlockHalf.MIDDLE) && facing==Direction.UP && !facingState.is(this))
            return Blocks.AIR.defaultBlockState();

        //For upper part, checks if the block below is another reed, otherwise breaks
        if(half==TripleBlockHalf.UPPER && facing==Direction.DOWN && !facingState.is(this))
            return Blocks.AIR.defaultBlockState();

        //Update age
        if((facing==Direction.UP || facing==Direction.DOWN) && facingState.is(this)){
            int age= facingState.getValue(AGE);
            return state.setValue(AGE, age);
        }

        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    protected boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        BlockState blockBelow = level.getBlockState(pos.below());
        BlockState blockAbove = level.getBlockState(pos.above());

        //Top ONLY can survive if it has another reed below
        if(state.getValue(HALF)==TripleBlockHalf.UPPER){
            return blockBelow.is(this);
        }
        //Middle ONLY can survive if it has another block below and above
        else if(state.getValue(HALF)==TripleBlockHalf.MIDDLE){
            return blockAbove.is(this) && blockBelow.is(this);
        }
        //Lower always tries to survive normally
        else {
            return super.canSurvive(state, level, pos);
        }
    }

    @Override
    protected boolean mayPlaceOn(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return super.mayPlaceOn(state, level, pos) || state.is(Primal_Tags.Block.RIVER_REED_SOIL);
    }

    @Override
    protected @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    public static BlockState copyWaterloggedFrom(LevelReader level, BlockPos pos, BlockState state) {
        return state.hasProperty(BlockStateProperties.WATERLOGGED)
                ? state.setValue(BlockStateProperties.WATERLOGGED, level.isWaterAt(pos))
                : state;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected long getSeed(BlockState state, BlockPos pos) {
        return Mth.getSeed(pos.getX(), pos.below(state.getValue(HALF) == TripleBlockHalf.LOWER ? 0: state.getValue(HALF) == TripleBlockHalf.MIDDLE? 1: 2).getY(), pos.getZ());
    }

    @Override
    protected @NotNull MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    @Override
    public boolean isValidBonemealTarget(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(@NotNull Level level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(@NotNull ServerLevel level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        performBonemeal(level, random, pos, state, true);
    }

    public void performBonemeal(@NotNull ServerLevel level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state, boolean canSpread) {

        if(state.getValue(AGE)==0){
            level.setBlock(pos, state.setValue(AGE, 1), 2);
        }
        //Spread logic
        else {
            BlockState blockBelow = level.getBlockState(pos.below());

            //To always get the lowest point
            if(state.getValue(HALF)==TripleBlockHalf.UPPER && blockBelow.is(this)){
                pos=pos.below();
            }
            if(level.getBlockState(pos).getValue(HALF)==TripleBlockHalf.MIDDLE
                    && level.getBlockState(pos.below()).is(this)){
                pos=pos.below();
            }

            BlockPos lowestPos=pos;

            //if it can actually grow up (Not blocks above.above)
            if(level.getFluidState(lowestPos).is(Fluids.WATER) && (RiverReeds.isValidPosForRiverReed(level, pos.above().above())) && !level.getBlockState(pos.above().above()).is(this)){
                BlockState defaultState= this.defaultBlockState();

                level.setBlock(lowestPos,
                        defaultState.setValue(WATERLOGGED, true)
                                .setValue(HALF, TripleBlockHalf.LOWER)
                                .setValue(AGE, 1),
                        2);

                level.setBlock(lowestPos.above(),
                        defaultState.setValue(WATERLOGGED, level.getFluidState(lowestPos.above()).is(Fluids.WATER))
                                .setValue(HALF, TripleBlockHalf.MIDDLE)
                                .setValue(AGE, 1),
                        2);

                level.setBlock(lowestPos.above().above(),
                        defaultState.setValue(WATERLOGGED, level.getFluidState(lowestPos.above().above()).is(Fluids.WATER))
                                .setValue(HALF, TripleBlockHalf.UPPER)
                                .setValue(AGE, 1),
                        2);

            } else if (canSpread) {
                setRiverReedsNear(level, lowestPos, level.getRandom().nextBoolean()?2: 1);
            }
        }
    }

    protected void setRiverReedsNear(Level level, BlockPos initialPos, int lateralDistance) {
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

    public static boolean isValidPosForRiverReed(LevelReader level, BlockPos pos){
        return level.getBlockState(pos).isAir() || level.getBlockState(pos).is(Blocks.WATER) || level.getBlockState(pos).is(Primal_Blocks.RIVER_REEDS.get());
    }

    @Override
    protected boolean isRandomlyTicking(@NotNull BlockState state) {
        return state.getValue(HALF)==TripleBlockHalf.LOWER;
    }

    @Override
    protected void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (!level.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light

        if (level.getRawBrightness(pos, 0) >= 6) {

            if (level.getRandom().nextIntBetweenInclusive(1, 50)==1) {
                this.performBonemeal(level, random, pos, state, false);
            }
        }
    }
}
