package org.primal.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.primal.registry.Primal_Tags;

public class FruitBulk extends BushBlock implements BonemealableBlock {

    private final RegistryObject<Item> seed;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_1;

    //──────────────────────────────────── Init ────────────────────────────────────
    public FruitBulk(Properties properties) {
        this(properties, null);
    }

    //Age 0 -> Tiny thing
    //Age 1 -> Full block
    public FruitBulk(Properties properties, RegistryObject<Item> seed) {
        super(properties);
        this.seed=seed;
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(AGE, 0));
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return state.getValue(AGE)==0? Shapes.empty(): state.getShape(level, pos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

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

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        //Juvenile
        if(state.getValue(AGE) == 0){
            return Block.box(1.0, 0.0, 1.0,
                    15.0, 10.0, 15.0);
        }

        //Full
        return Block.box(0.0, 0.0, 0.0,
                        16.0, 16.0, 16.0);
    }

    //──────────────────────────────────── Grow Logic ────────────────────────────────────
    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        //Normal grow
        if ((random.nextInt(5) == 0 && level.getRawBrightness(pos, 0) >= 9)
                //Fast grow
                || (random.nextInt(3)==0 && level.getBlockState(pos.below()).is(Primal_Tags.Block.FRUIT_TREE_GROW_FAST_ON))) {
            this.growBulk(level, pos);
        }
    }

    protected void growBulk(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        int age = state.getValue(FruitBulk.AGE);

        //Increases the age
        if (age<1){
            level.setBlock(pos, state.setValue(FruitBulk.AGE, age+1), 3);
        }
    }

    @Override
    public void spawnAfterBreak(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull ItemStack stack, boolean dropExperience) {
        super.spawnAfterBreak(state, level, pos, stack, dropExperience);
        //Replant seed
        if(state.getValue(AGE)==1 && seed.get() instanceof BlockItem blockItem){
            level.setBlock(pos, blockItem.getBlock().defaultBlockState(), 3);
        }
    }

    @Override
    public boolean isValidBonemealTarget(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state, boolean isOn) {
        return state.getValue(AGE)<1;
    }

    @Override
    public boolean isBonemealSuccess(@NotNull Level level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(@NotNull ServerLevel level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        this.growBulk(level, pos);
    }
}
