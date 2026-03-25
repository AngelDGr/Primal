package org.primal.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.block_entity.DreamcatcherBlockEntity;
import org.primal.registry.Primal_BlockEntities;
import org.primal.util.Primal_Util;

//xTODO: Add actual use to the dreamcatcher
//Kills nearby phantoms
//Reduce the monster prevent sleeping detection from 8x5 to 4x2
//If there's hostile mobs at 8 block of distance, it shakes a little
//If there's hostiles mob at 4 block of distance, it shakes a lot
public class DreamcatcherBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty HANGING = BlockStateProperties.HANGING;
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;

    public DreamcatcherBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(HANGING, false)
                .setValue(HALF, Half.TOP));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HANGING, HALF);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new DreamcatcherBlockEntity(Primal_BlockEntities.DREAMCATCHER.get(), pos, state);
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        //Destroy block
        if(!canSurvive(state, level, pos))
            return Blocks.AIR.defaultBlockState();

        if(state.getValue(HALF).equals(Half.TOP)){
            if(direction.equals(Direction.DOWN)){

                return neighborState.is(this)?
                        neighborState.setValue(HALF, Half.TOP) :
                        Blocks.AIR.defaultBlockState();

            }
        }

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity placer, @NotNull ItemStack stack) {
        //Put the other part below
        if(state.getValue(HALF).equals(Half.TOP)){
            level.setBlock(pos.below(), state.setValue(HALF, Half.BOTTOM), 3);
        }
    }

    @Override
    public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {

        if(state.getValue(HALF).equals(Half.TOP)){
            if(!level.getBlockState(pos.below()).isAir()
                    && !(level.getBlockState(pos.below()).is(this) && level.getBlockState(pos.below()).getValue(HALF).equals(Half.BOTTOM)))
                return false;
        }

        if(state.getValue(HALF).equals(Half.BOTTOM))
            return level.getBlockState(pos.above()).is(this);

        var facing = state.getValue(FACING);
        BlockPos blockpos =  pos.relative(facing);
        BlockState blockstate = level.getBlockState(blockpos);

        if(state.getValue(HANGING))
            return Block.canSupportCenter(level, pos.relative(Direction.UP), Direction.UP);

        return blockstate.isFaceSturdy(level, blockpos, Direction.DOWN, SupportType.FULL);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return Primal_Util.rotateShape(Direction.NORTH, state.getValue(FACING),
                state.getValue(HANGING)?
                        Block.box(0.0, 0.0, 6.0, 16.0, 16.0, 10f):
                        Block.box(0.0, 0.0, 0, 16.0, 16.0, 2f));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection())
                .setValue(HANGING, context.getClickedFace().equals(Direction.DOWN));
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        if(state.getValue(HALF).equals(Half.BOTTOM))
            return null;

        if (level.isClientSide)
            return createTickerHelper(blockEntityType, Primal_BlockEntities.DREAMCATCHER.get(), DreamcatcherBlockEntity::clientTick);
        else
            return createTickerHelper(blockEntityType, Primal_BlockEntities.DREAMCATCHER.get(), DreamcatcherBlockEntity::serverTick);
    }
}
