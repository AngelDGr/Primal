package org.primal.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.CrocodileEntity;
import org.primal.registry.Primal_Entities;
import org.primal.util.block_types.AnimalEgg;
import org.primal.util.Primal_Util;

import javax.annotation.Nullable;

public class CrocodileEgg extends Block implements AnimalEgg {
    public static final MapCodec<CrocodileEgg> CODEC = simpleCodec(CrocodileEgg::new);
    public static final IntegerProperty EGGS = Primal_Util.EGGS_3;

    public CrocodileEgg(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(HATCH, 0)
                .setValue(EGGS, 1));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(EGGS, HATCH);
    }

    @Override
    public DeferredHolder<EntityType<?>, ? extends EntityType<? extends Animal>> getAnimal() {
        return Primal_Entities.CROCODILE;
    }

    @Override
    public boolean entityCannotDestroyEgg(Entity entity) {
        return entity instanceof CrocodileEntity || entity instanceof Bat || entity instanceof Turtle;
    }

    @Override
    public void stepOn(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, Entity entity) {
        if (!entity.isSteppingCarefully())
            this.destroyEgg(level, state, pos, entity, 100, this, EGGS);

        super.stepOn(level, pos, state, entity);
    }

    @Override
    public void fallOn(@NotNull Level level, @NotNull BlockState state, @NotNull BlockPos pos, @NotNull Entity entity, float fallDistance) {
        if (!(entity instanceof Zombie)) {
            this.destroyEgg(level, state, pos, entity, 3, this, EGGS);
        }

        super.fallOn(level, state, pos, entity, fallDistance);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos());
        return blockstate.is(this)
                ? blockstate.setValue(EGGS, Math.min(3, blockstate.getValue(EGGS) + 1))
                : super.getStateForPlacement(context);
    }

    @Override
    public void playerDestroy(@NotNull Level level, @NotNull Player player, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable BlockEntity te, @NotNull ItemStack stack) {
        super.playerDestroy(level, player, pos, state, te, stack);
        this.decreaseEggs(level, pos, state, EGGS);
    }

    @Override
    protected boolean canBeReplaced(@NotNull BlockState state, BlockPlaceContext useContext) {
        return !useContext.isSecondaryUseActive() && useContext.getItemInHand().is(this.asItem()) && state.getValue(EGGS) < 3
                || super.canBeReplaced(state, useContext);
    }

    @Override
    protected void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        randomTick(state, level, pos, random, EGGS);
    }

    private static final VoxelShape ONE_EGG_AABB = Block.box(1.0, 0.0, 1.0, 7.0, 6.0, 7.0);
    private static final VoxelShape MULTIPLE_EGGS_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
    @Override
    protected @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return state.getValue(EGGS) > 1 ? MULTIPLE_EGGS_AABB : ONE_EGG_AABB;
    }

    @Override
    protected @NotNull MapCodec<? extends Block> codec() {
        return CODEC;
    }
}
