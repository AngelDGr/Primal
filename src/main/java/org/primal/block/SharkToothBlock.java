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
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.primal.block.properties.SharkToothThickness;
import org.primal.registry.Primal_DamageTypes;
import org.primal.util.Primal_Util;

import javax.annotation.Nullable;
import java.util.Map;

public class SharkToothBlock extends Block {
    public static final MapCodec<SharkToothBlock> CODEC = simpleCodec(SharkToothBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final EnumProperty<SharkToothThickness> THICKNESS = EnumProperty.create("thickness", SharkToothThickness.class);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public SharkToothBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(FACING, Direction.UP)
                        .setValue(THICKNESS, SharkToothThickness.TIP)
                        .setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, THICKNESS, WATERLOGGED);
    }

    @Override
    protected boolean canSurvive(BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        return isValidPointedDripstonePlacement(level, pos, state.getValue(FACING));
    }

    @Override
    protected @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected @NotNull BlockState updateShape(BlockState state, @NotNull Direction directionUpdated, @NotNull BlockState facingState, @NotNull LevelAccessor level, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        //Destroy block
        if(updateOpposites(state, directionUpdated) && !facingState.is(this) && !canSurvive(state, level, currentPos))
            return Blocks.AIR.defaultBlockState();

        if(isValueForConnection(state, Direction.UP, directionUpdated, facingState)
                || isValueForConnection(state, Direction.DOWN, directionUpdated, facingState)
                || isValueForConnection(state, Direction.NORTH, directionUpdated, facingState)
                || isValueForConnection(state, Direction.SOUTH, directionUpdated, facingState)
                || isValueForConnection(state, Direction.EAST, directionUpdated, facingState)
                || isValueForConnection(state, Direction.WEST, directionUpdated, facingState))
            return state.setValue(THICKNESS, SharkToothThickness.BASE);

        if (state.getValue(THICKNESS)==SharkToothThickness.BASE && !facingState.is(this) && directionUpdated==state.getValue(FACING))
            return state.setValue(THICKNESS, SharkToothThickness.TIP);

        return state;
    }

    private boolean isValueForConnection(BlockState state, Direction directionToCheck, Direction directionUpdated, BlockState facingState){
        return state.getValue(THICKNESS)==SharkToothThickness.TIP && state.getValue(FACING)==directionToCheck
                && directionUpdated==directionToCheck && facingState.is(this) && facingState.getValue(FACING)==directionToCheck;
    }

    private boolean updateOpposite(BlockState state, Direction directionToCheck, Direction directionUpdated){
        return state.getValue(FACING)==directionToCheck && directionUpdated==directionToCheck.getOpposite();
    }

    private boolean updateOpposites(BlockState state, Direction directionUpdated){
        return updateOpposite(state, Direction.UP, directionUpdated)
                || updateOpposite(state, Direction.DOWN, directionUpdated)
                || updateOpposite(state, Direction.NORTH, directionUpdated)
                || updateOpposite(state, Direction.SOUTH, directionUpdated)
                || updateOpposite(state, Direction.EAST, directionUpdated)
                || updateOpposite(state, Direction.WEST, directionUpdated);
    }

    private boolean isValidPointedDripstonePlacement(LevelReader level, BlockPos pos, Direction dir) {
        BlockPos blockpos = pos.relative(dir.getOpposite());
        BlockState blockstate = level.getBlockState(blockpos);
        return (blockstate.is(Blocks.MOVING_PISTON) || blockstate.isFaceSturdy(level, blockpos, dir, SupportType.CENTER) || (blockstate.is(this) && blockstate.getValue(FACING)==dir)) && !blockstate.isAir();
    }

    private boolean isToothWithDirection(BlockState state, Direction dir) {
        return state.is(this) && state.getValue(FACING) == dir;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor levelaccessor = context.getLevel();
        BlockPos blockpos = context.getClickedPos();

        SharkToothThickness sharkToothThickness = calculateToothThickness(levelaccessor, blockpos, context.getClickedFace().getOpposite());
        return sharkToothThickness == null
                ? null
                : this.defaultBlockState()
                .setValue(FACING, context.getClickedFace())
                .setValue(THICKNESS, sharkToothThickness)
                .setValue(WATERLOGGED, levelaccessor.getFluidState(blockpos).getType() == Fluids.WATER);
    }

    private SharkToothThickness calculateToothThickness(LevelReader level, BlockPos pos, Direction dir) {
        BlockState onBlockPlacedState = level.getBlockState(pos.relative(dir));
        if (isToothWithDirection(onBlockPlacedState, dir)) {
            return SharkToothThickness.BASE;
        } else {
            return SharkToothThickness.TIP;
        }
    }

    @Override
    protected @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected @NotNull VoxelShape getOcclusionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return Shapes.empty();
    }

    private final Map<Direction, VoxelShape> tipShapesByDirection = Map.of(
            Direction.UP, Block.box(5.0, 0.0, 5.0, 11.0, 11.0, 11.0),
            Direction.DOWN, Block.box(5.0, 5.0, 5.0, 11.0, 16.0, 11.0),

            Direction.NORTH, Block.box(5.0, 5.0, 5.0, 11.0, 11.0, 16.0),
            Direction.SOUTH, Block.box(5.0, 5.0, 0.0, 11.0, 11.0, 11.0),
            Direction.EAST,  Block.box(0.0, 5.0, 5.0, 11.0, 11.0, 11.0),
            Direction.WEST,  Block.box(5.0, 5.0, 5.0, 16.0, 11.0, 11.0)
    );

    private final Map<Direction, VoxelShape> baseShapesByDirection = Map.of(
            Direction.UP, Block.box(5.0, 0.0, 5.0, 11.0, 16.0, 11.0),
            Direction.DOWN, Block.box(5.0, 0.0, 5.0, 11.0, 16.0, 11.0),

            Direction.NORTH, Block.box(5.0, 5.0, 0.0, 11.0, 11.0, 16.0),
            Direction.SOUTH, Block.box(5.0, 5.0, 0.0, 11.0, 11.0, 16.0),
            Direction.EAST,  Block.box(0.0, 5.0, 5.0, 16.0, 11.0, 11.0),
            Direction.WEST,  Block.box(0.0, 5.0, 5.0, 16.0, 11.0, 11.0)
    );

    @Override
    protected @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return state.getValue(THICKNESS) == SharkToothThickness.TIP?
                tipShapesByDirection.get(state.getValue(FACING)):
                baseShapesByDirection.get(state.getValue(FACING));
    }

    @Override
    protected boolean isCollisionShapeFullBlock(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return false;
    }

    @Override
    public void fallOn(@NotNull Level level, BlockState state, @NotNull BlockPos pos, @NotNull Entity entity, float fallDistance) {
        if (state.getValue(FACING) == Direction.UP && state.getValue(THICKNESS) == SharkToothThickness.TIP) {
            entity.causeFallDamage(fallDistance + 4.0F, 2.0F, Primal_DamageTypes.sharkTooth(level));
        } else {
            super.fallOn(level, state, pos, entity, fallDistance);
        }
    }

    @Override
    public boolean collisionExtendsVertically(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull Entity collidingEntity) {
        return super.collisionExtendsVertically(state, level, pos, collidingEntity);
    }

    @Override
    protected void entityInside(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (level.isClientSide) return;

        // Block AABB (full cube)
        AABB blockBox = new AABB(pos);

        // Entity AABB
        AABB entityBox = entity.getBoundingBox();

        if (!blockBox.intersects(entityBox)) return; // not touching

        // Distances from faces
        Direction face = Primal_Util.getFaceTouchedByEntity(entityBox, blockBox);


        if (face != null) {
            if(entity.isShiftKeyDown() && face==Direction.UP) return;

            if(state.getValue(FACING)==face)
                entity.hurt(Primal_DamageTypes.sharkTooth(level), 2.0f);
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
