package org.primal.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.primal.block.properties.SharkToothThickness;
import org.primal.registry.Primal_DamageTypes;

import javax.annotation.Nullable;

public class SharkToothBlock extends Block {
    public static final MapCodec<SharkToothBlock> CODEC = simpleCodec(SharkToothBlock::new);
    public static final DirectionProperty TIP_DIRECTION = BlockStateProperties.VERTICAL_DIRECTION;
    public static final EnumProperty<SharkToothThickness> THICKNESS = EnumProperty.create("thickness", SharkToothThickness.class);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public SharkToothBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(TIP_DIRECTION, Direction.UP).setValue(THICKNESS, SharkToothThickness.TIP).setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TIP_DIRECTION, THICKNESS, WATERLOGGED);
    }

    @Override
    protected boolean canSurvive(BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        return isValidPointedDripstonePlacement(level, pos, state.getValue(TIP_DIRECTION));
    }

    @Override
    protected @NotNull BlockState updateShape(BlockState state, @NotNull Direction directionUpdated, @NotNull BlockState facingState, @NotNull LevelAccessor level, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        //Destroy block
        if(((state.getValue(TIP_DIRECTION)==Direction.UP && directionUpdated==Direction.DOWN) || ((state.getValue(TIP_DIRECTION)==Direction.DOWN && directionUpdated==Direction.UP)))
                && !facingState.is(this))
            return Blocks.AIR.defaultBlockState();

        //Handle connection for up
        if (state.getValue(THICKNESS)==SharkToothThickness.TIP && state.getValue(TIP_DIRECTION)==Direction.UP
                && directionUpdated==Direction.UP && facingState.is(this) && facingState.getValue(TIP_DIRECTION)==Direction.UP)
            return state.setValue(THICKNESS, SharkToothThickness.BASE);

        //Handle connection for down
        if (state.getValue(THICKNESS)==SharkToothThickness.TIP && state.getValue(TIP_DIRECTION)==Direction.DOWN
                && directionUpdated==Direction.DOWN && facingState.is(this) && facingState.getValue(TIP_DIRECTION)==Direction.DOWN)
            return state.setValue(THICKNESS, SharkToothThickness.BASE);

        if ((directionUpdated==Direction.DOWN || directionUpdated==Direction.UP) && state.getValue(THICKNESS)==SharkToothThickness.BASE && !facingState.is(this))
            return state.setValue(THICKNESS, SharkToothThickness.TIP);

        return state;
    }

    private boolean isValidPointedDripstonePlacement(LevelReader level, BlockPos pos, Direction dir) {
        BlockPos blockpos = pos.relative(dir.getOpposite());
        BlockState blockstate = level.getBlockState(blockpos);
        return (blockstate.isFaceSturdy(level, blockpos, dir) || blockstate.is(this)) && !blockstate.isAir();
    }

    private boolean isPointedDripstoneWithDirection(BlockState state, Direction dir) {
        return state.is(this) && state.getValue(TIP_DIRECTION) == dir;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor levelaccessor = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        Direction direction = context.getNearestLookingVerticalDirection().getOpposite();
        Direction direction1 = calculateTipDirection(levelaccessor, blockpos, direction);
        if (direction1 == null) {
            return null;
        } else {
            boolean flag = !context.isSecondaryUseActive();
            SharkToothThickness sharkToothThickness = calculateDripstoneThickness(levelaccessor, blockpos, direction1, flag);
            return sharkToothThickness == null
                    ? null
                    : this.defaultBlockState()
                    .setValue(TIP_DIRECTION, direction1)
                    .setValue(THICKNESS, sharkToothThickness)
                    .setValue(WATERLOGGED, levelaccessor.getFluidState(blockpos).getType() == Fluids.WATER);
        }
    }

    private SharkToothThickness calculateDripstoneThickness(LevelReader level, BlockPos pos, Direction dir, boolean isTipMerge) {
        Direction direction = dir.getOpposite();
        BlockState blockstate = level.getBlockState(pos.relative(dir));
        if (isPointedDripstoneWithDirection(blockstate, direction) && !isTipMerge ) {
            return SharkToothThickness.BASE;
        } else if (!isPointedDripstoneWithDirection(blockstate, dir)) {
            return SharkToothThickness.TIP;
        } else {
            return SharkToothThickness.TIP;
        }
    }

    @Nullable
    private Direction calculateTipDirection(LevelReader level, BlockPos pos, Direction dir) {
        Direction direction;
        if (isValidPointedDripstonePlacement(level, pos, dir)) {
            direction = dir;
        } else {
            if (!isValidPointedDripstonePlacement(level, pos, dir.getOpposite())) {
                return null;
            }

            direction = dir.getOpposite();
        }

        return direction;
    }

    @Override
    protected @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected @NotNull VoxelShape getOcclusionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return Shapes.empty();
    }

    private static final VoxelShape BASE_SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 16.0, 11.0);
    private static final VoxelShape TIP_SHAPE_UP = Block.box(5.0, 0.0, 5.0, 11.0, 11.0, 11.0);
    private static final VoxelShape TIP_SHAPE_DOWN = Block.box(5.0, 5.0, 5.0, 11.0, 16.0, 11.0);
    @Override
    protected @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        VoxelShape voxelshape;
        if(state.getValue(THICKNESS) == SharkToothThickness.TIP) {
            if (state.getValue(TIP_DIRECTION) == Direction.DOWN) {
                voxelshape = TIP_SHAPE_DOWN;
            } else {
                voxelshape = TIP_SHAPE_UP;
            }
        } else {
            voxelshape = BASE_SHAPE;
        }

        Vec3 vec3 = state.getOffset(level, pos);
        return voxelshape.move(vec3.x, 0.0, vec3.z);
    }

    @Override
    protected boolean isCollisionShapeFullBlock(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return false;
    }

    @Override
    public void fallOn(@NotNull Level level, BlockState state, @NotNull BlockPos pos, @NotNull Entity entity, float fallDistance) {
        if (state.getValue(TIP_DIRECTION) == Direction.UP && state.getValue(THICKNESS) == SharkToothThickness.TIP) {
            entity.causeFallDamage(fallDistance + 4.0F, 2.0F, Primal_DamageTypes.sharkTooth(level));
        } else {
            super.fallOn(level, state, pos, entity, fallDistance);
        }
    }

    @Override
    protected boolean isPathfindable(@NotNull BlockState state, @NotNull PathComputationType pathComputationType) {
        return false;
    }

    @Override
    protected @NotNull MapCodec<? extends Block> codec() {
        return CODEC;
    }
}
