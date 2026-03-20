package org.primal.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.primal.util.block_types.Snowloggable;

import java.util.function.BiFunction;

public class SeashellsBlock extends Block implements Snowloggable {
    public static final MapCodec<SeashellsBlock> CODEC = simpleCodec(SeashellsBlock::new);
    public static final IntegerProperty AMOUNT = IntegerProperty.create("seashells_amount", 1, 4);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final BiFunction<Direction, Integer, VoxelShape> SHAPE_BY_PROPERTIES = Util.memoize(
            (direction, integer) -> {
                VoxelShape[] avoxelshape = new VoxelShape[]{
                        Block.box(7.0, 0.0, 7.0, 16.0, 1.0, 16.0),
                        Block.box(7.0, 0.0, 0.0, 16.0, 1.0, 9.0),
                        Block.box(0.0, 0.0, 0.0, 9.0, 1.0, 9.0),
                        Block.box(0.0, 0.0, 7.0, 9.0, 1.0, 16.0)
                };
                VoxelShape voxelshape = Shapes.empty();

                for (int i = 0; i < integer; i++) {
                    int j = Math.floorMod(i - direction.get2DDataValue(), 4);
                    voxelshape = Shapes.or(voxelshape, avoxelshape[j]);
                }

                return voxelshape.singleEncompassing();
            }
    );

    public SeashellsBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AMOUNT, 1).setValue(WATERLOGGED, false).setValue(SNOWY, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, AMOUNT, WATERLOGGED, SNOWY);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor levelaccessor = context.getLevel();
        BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos());
        BlockPos blockpos = context.getClickedPos();
        return blockstate.is(this)
                ? blockstate.setValue(AMOUNT, Math.min(4, blockstate.getValue(AMOUNT) + 1))
                : this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, levelaccessor.getFluidState(blockpos).getType() == Fluids.WATER)
                .setValue(SNOWY, levelaccessor.getBlockState(blockpos).is(Blocks.SNOW));
    }

    @Override
    protected @NotNull BlockState updateShape(BlockState state, @NotNull Direction directionUpdated, @NotNull BlockState facingState, @NotNull LevelAccessor level, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {

        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        if(directionUpdated==Direction.DOWN && !canSurvive(state, level, currentPos))
            return Blocks.AIR.defaultBlockState();

        return state;
    }

    @Override
    protected boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, BlockPos pos) {
        return canSupportRigidBlock(level, pos.below());
    }

    @Override
    protected @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if(!state.getValue(SNOWY) && stack.is(Items.SNOW)){
            BlockState blockstate = state.setValue(SNOWY, true);
            stack.consume(1, player);
            level.setBlock(pos, blockstate, 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, blockstate));
            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if(state.getValue(SNOWY) && (player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() || player.getItemInHand(InteractionHand.MAIN_HAND).is(ItemTags.SHOVELS))){
            level.levelEvent(2001, pos, Block.getId(Blocks.SNOW.defaultBlockState()));

            if(player.getItemInHand(InteractionHand.MAIN_HAND).is(ItemTags.SHOVELS))
                popResource(level, pos, new ItemStack(Items.SNOWBALL));

            BlockState blockstate = state.setValue(SNOWY, false);
            level.setBlock(pos, blockstate, 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, blockstate));
            return InteractionResult.SUCCESS;
        }

        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected void randomTick(@NotNull BlockState state, ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (level.getBrightness(LightLayer.BLOCK, pos) > 11) {
            BlockState blockstate = state.setValue(SNOWY, false);
            level.setBlock(pos, blockstate, 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(null, blockstate));
        }
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return state.getValue(SNOWY);
    }

    @Override
    public boolean canBeReplaced(@NotNull BlockState state, BlockPlaceContext context) {
        return
                //Place more seashells
                (!context.isSecondaryUseActive() &&
                context.getItemInHand().is(this.asItem()) && state.getValue(AMOUNT) < 4)
                || super.canBeReplaced(state, context);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext context) {
        if(state.getValue(SNOWY)) return Block.box(0.0, 0.0, 0.0, 16.0, 3.0, 16.0);

        return SHAPE_BY_PROPERTIES.apply(state.getValue(FACING), state.getValue(AMOUNT));
    }

    @Override
    protected @NotNull MapCodec<? extends Block> codec() {
        return CODEC;
    }


    @Override
    protected float getDestroyProgress(BlockState state, @NotNull Player player, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        float f = state.getValue(SNOWY)? 0.1F :state.getDestroySpeed(level, pos);
        if (f == -1.0F) {
            return 0.0F;
        } else {
            int i = net.neoforged.neoforge.event.EventHooks.doPlayerHarvestCheck(player, state, level, pos) ? 30 : 100;
            return player.getDigSpeed(state, pos) / f / (float)i;
        }
    }
}
