package org.primal.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.primal.registry.Primal_Tags;

public class FruitSapling extends BushBlock implements BonemealableBlock {
    public static final MapCodec<FruitSapling> CODEC = simpleCodec(FruitSapling::new);

    private final DeferredHolder<Item, Item> seed;
    private final DeferredHolder<Block, Block> fruitTree;

    //──────────────────────────────────── Init ────────────────────────────────────
    public FruitSapling(Properties properties) {
        this(properties, null, null);
    }

    public FruitSapling(Properties properties, DeferredHolder<Item, Item> seed, DeferredHolder<Block, Block> fruitTree) {
        super(properties);
        this.seed=seed;
        this.fruitTree=fruitTree;
    }

    @Override
    protected @NotNull MapCodec<FruitSapling> codec() {
        return CODEC;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull ItemStack getCloneItemStack(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state) {
        return new ItemStack(seed.get());
    }

    //──────────────────────────────────── Basic things ────────────────────────────────────
    protected static final VoxelShape SAPLING_SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 10.0, 13.0);
    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SAPLING_SHAPE;
    }

    @Override
    protected boolean canSurvive(@NotNull BlockState state, LevelReader level, BlockPos pos) {
        net.neoforged.neoforge.common.util.TriState soilDecision = level.getBlockState(pos.below()).canSustainPlant(level, pos.below(), net.minecraft.core.Direction.UP, state);
        if (!soilDecision.isDefault()) return soilDecision.isTrue();
        return level.getBlockState(pos.below()).is(Primal_Tags.Block.FRUIT_TREE_PLANTABLE_ON);
    }

    @Override
    protected @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        if(direction==Direction.DOWN && !canSurvive(state, level, pos))
            return Blocks.AIR.defaultBlockState();

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    //──────────────────────────────────── Grow Logic ────────────────────────────────────
    @Override
    protected void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, RandomSource random) {
        //Normal grow
        if ((random.nextInt(5) == 0 && level.getRawBrightness(pos, 0) >= 9)
                //Fast grow
                || (random.nextInt(3)==0 && level.getBlockState(pos.below()).is(Primal_Tags.Block.FRUIT_TREE_GROW_FAST_ON))) {
            this.growFruitTree(level, pos);
        }
    }

    protected void growFruitTree(Level level, BlockPos pos) {
        var randomDirection = Direction.Plane.HORIZONTAL.getRandomDirection(level.getRandom());

        if(fruitTree.get() instanceof FruitTree){
            level.setBlock(pos, fruitTree.get().defaultBlockState()
                    .setValue(FruitTree.HALF, DoubleBlockHalf.LOWER)
                    .setValue(FruitTree.AGE, 0)
                    .setValue(FruitTree.FACING, randomDirection), 3);
        } else if (fruitTree.get() instanceof FruitBulk) {
            level.setBlock(pos, fruitTree.get().defaultBlockState()
                    .setValue(FruitBulk.AGE, 0), 3);
        } else {
            level.setBlock(pos, fruitTree.get().defaultBlockState(),
                    3);
        }
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
        this.growFruitTree(level, pos);
    }
}
