package org.primal.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.primal.util.Primal_Util;

public class DeerAntlerBlock extends Block {
    public static final BooleanProperty ATTACHED = BlockStateProperties.ATTACHED;
    public static final BooleanProperty RIGHT_SIDE = BooleanProperty.create("right");
    public static final BooleanProperty DOUBLE = BooleanProperty.create("double");
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public DeerAntlerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(ATTACHED, false)
                .setValue(RIGHT_SIDE, false)
                .setValue(DOUBLE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ATTACHED, RIGHT_SIDE, DOUBLE);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        VoxelShape shape;

        if (state.getValue(DOUBLE))
            //Double Hitbox
            shape = Block.box(0.0, 0.0, 5.5, 16.0, 9.0, 10.5);
        else if (state.getValue(ATTACHED))
            //Wall Hitbox
            shape = state.getValue(RIGHT_SIDE) ?
                    Block.box(4.0, 4.0, 5.5, 16.0, 13.0, 10.5) :
                    Block.box(0.0, 4.0, 5.5, 12.0, 13.0, 10.5);
        else
            //Ground Hitbox
            shape = state.getValue(RIGHT_SIDE) ?
                    Block.box(7.0, 0.0, 5.5, 16.0, 9.0, 10.5) :
                    Block.box(0.0, 0.0, 5.5, 9.0, 9.0, 10.5);

        return Primal_Util.rotateShape(Direction.NORTH, state.getValue(FACING), shape);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos());

        boolean sideX = !(context.getClickLocation().x - context.getClickedPos().getX() > 0.5);
        boolean sideZ = !(context.getClickLocation().z - context.getClickedPos().getZ() > 0.5);
        boolean mustBeAttached = false;
        var facing = context.getHorizontalDirection().getOpposite();

        boolean mustBeRight = switch (context.getHorizontalDirection()) {
            case NORTH -> sideX;
            case SOUTH -> !sideX;
            case EAST -> sideZ;
            case WEST -> !sideZ;
            default -> false;
        };

        if(context.getClickedFace().getAxis().isHorizontal()) {
            mustBeAttached = true;
            if(mustBeRight)
                facing = context.getClickedFace().getClockWise();
            else
                facing = context.getClickedFace().getCounterClockWise();
        }

        return blockstate.is(this)
                ? blockstate.setValue(DOUBLE, true)
                : this.defaultBlockState()
                .setValue(FACING, facing)
                .setValue(RIGHT_SIDE, mustBeRight)
                .setValue(ATTACHED, mustBeAttached);
    }

    @Override
    public boolean canBeReplaced(@NotNull BlockState state, BlockPlaceContext context) {
        return
                //Place more antlers, only if no on wall
                (!context.isSecondaryUseActive() &&
                        context.getItemInHand().is(this.asItem()) && !state.getValue(DOUBLE) && !state.getValue(ATTACHED))
                        || super.canBeReplaced(state, context);
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

    @Override
    public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        var facing = state.getValue(FACING);
        BlockPos blockpos =  pos.below();
        BlockState blockstate = level.getBlockState(blockpos);

        if(state.getValue(ATTACHED)){
            if(state.getValue(RIGHT_SIDE))
                blockpos = pos.relative(facing.getClockWise());
            else
                blockpos = pos.relative(facing.getCounterClockWise());

            blockstate = level.getBlockState(blockpos);

            return blockstate.isFaceSturdy(level, blockpos, facing, SupportType.CENTER) || blockstate.is(BlockTags.LEAVES);
        }

        return blockstate.isFaceSturdy(level, blockpos, Direction.DOWN, SupportType.CENTER) || blockstate.is(BlockTags.LEAVES);
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction directionUpdated, @NotNull BlockState facingState, @NotNull LevelAccessor level, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {
        //Destroy block
        if(!canSurvive(state, level, currentPos))
            return Blocks.AIR.defaultBlockState();

        return super.updateShape(state, directionUpdated, facingState, level, currentPos, facingPos);
    }
}
