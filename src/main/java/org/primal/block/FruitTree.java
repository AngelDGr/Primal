package org.primal.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.primal.registry.Primal_Items;
import org.primal.registry.Primal_Tags;

public class FruitTree extends BushBlock implements BonemealableBlock {

    private final RegistryObject<Item> fruit;
    private final RegistryObject<Item> seed;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

    //──────────────────────────────────── Init ────────────────────────────────────
    public FruitTree(Properties properties) {
        this(properties, null, null);
    }

    //Age 0 -> One block tall
    //Age 1 -> Two block tall
    //Age 2 -> Two block tall - Little fruit
    //Age 3 -> Two block tall - A lot of fruits
    public FruitTree(Properties properties, RegistryObject<Item> fruit, RegistryObject<Item> seed) {
        super(properties);
        this.fruit=fruit;
        this.seed=seed;
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(HALF, DoubleBlockHalf.LOWER)
                .setValue(AGE, 0)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HALF, AGE, FACING);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return new ItemStack(seed.get());
    }

    //──────────────────────────────────── Basic things ────────────────────────────────────
    @Override
    public boolean canSurvive(@NotNull BlockState state, LevelReader level, BlockPos pos) {
        if(level.getBlockState(pos.below()).is(this)) return true;
        return level.getBlockState(pos.below()).is(Primal_Tags.Block.FRUIT_TREE_PLANTABLE_ON);
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        if(direction==Direction.DOWN && !canSurvive(state, level, pos)) return Blocks.AIR.defaultBlockState();

        //Removes trunk if the upper half is removed
        if(state.getValue(HALF).equals(DoubleBlockHalf.LOWER)
                && state.getValue(AGE) > 0
                && direction==Direction.UP
                && !neighborState.is(this))
            return Blocks.AIR.defaultBlockState();

        //Update age
        if((direction==Direction.UP || direction==Direction.DOWN) && neighborState.is(this)){
            int age= neighborState.getValue(AGE);
            var facing= neighborState.getValue(FACING);
            return state.setValue(AGE, age).setValue(FACING, facing);
        }

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        //Juvenile
        if(state.getValue(AGE) == 0 && state.getValue(HALF).equals(DoubleBlockHalf.LOWER)){
            if(fruit.get().equals(Primal_Items.LITCHI.get()))
                return Shapes.or(
                        Block.box(7.0, 0.0, 7.0,
                                9.0, 9.0, 9.0),
                        Block.box(4.0, 7.0, 4.0,
                                12.0,14.0, 12.0));

            return Shapes.or(
                    Block.box(7.0, 0.0, 7.0,
                            9.0, 9.0, 9.0),
                    Block.box(3.0, 7.0, 3.0,
                            13.0, 14.0, 13.0));
        }
        //Trunk
        else if(state.getValue(AGE) >= 1 && state.getValue(HALF).equals(DoubleBlockHalf.LOWER)){
            return Block.box(6.5, 0.0, 6.5,
                    9.5, 16.0, 9.5);
        }

        return Block.box(0.0, 0.0, 0.0,
                        16.0, 16.0, 16.0);
    }

    //──────────────────────────────────── Grow Logic ────────────────────────────────────


    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {

        int i = state.getValue(AGE);
        boolean flag = i == 3;
        if (!flag && player.getItemInHand(hand).is(Items.BONE_MEAL)) {
            return InteractionResult.PASS;
        } else if (i > 1) {
            //Age 2: 1 - 2
            //Age 3: 1 - 3
            int j = 1 + level.random.nextInt(2);

            popResource(level, pos, new ItemStack(this.fruit.get(), j + (flag ? 1 : 0)));

            level.playSound(
                    null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F
            );

            BlockState blockstate = state.setValue(AGE, 1);
            level.setBlock(pos, blockstate, 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, blockstate));
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return super.use(state, level, pos, player, hand, hitResult);
        }
    }

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {

        //Normal grow
        if ((random.nextInt(5) == 0 && level.getRawBrightness(pos, 0) >= 9)
                //Fast grow
                || (random.nextInt(3)==0 && level.getBlockState(pos.below()).is(Primal_Tags.Block.FRUIT_TREE_GROW_FAST_ON))) {
            this.growFruitTree(level, pos);
        }
    }

    protected void growFruitTree(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);

        BlockPos posAbove = pos.above();
        BlockState stateAbove = level.getBlockState(posAbove);
        int age = state.getValue(FruitTree.AGE);

        //From Juvenile to double (Age 1)
        if(isJuvenile(state)){
            //If space available above
            if(stateAbove.isAir()){
                //Put trunk
                level.setBlock(pos, state
                        .setValue(FruitTree.HALF, DoubleBlockHalf.LOWER)
                        .setValue(FruitTree.AGE, 1)
                        .setValue(FruitTree.FACING, state.getValue(FACING)), 3);

                //Put the upper part
                level.setBlock(posAbove, state
                        .setValue(FruitTree.HALF, DoubleBlockHalf.UPPER)
                        .setValue(FruitTree.AGE, 1)
                        .setValue(FruitTree.FACING, state.getValue(FACING)), 3);
            }
        }
        //Increases the age
        else if (age<3){
            level.setBlock(pos, state.setValue(FruitTree.AGE, age+1), 3);
        }
    }

    private boolean isJuvenile(BlockState state){
        return state.getValue(HALF).equals(DoubleBlockHalf.LOWER) && state.getValue(AGE) == 0;
    }

    @Override
    public boolean isValidBonemealTarget(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state, boolean isOn) {
        return state.getValue(AGE)<3;
    }

    @Override
    public boolean isBonemealSuccess(@NotNull Level level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(@NotNull ServerLevel level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        this.growFruitTree(level, pos);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection());
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
}
