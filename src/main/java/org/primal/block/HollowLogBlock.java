package org.primal.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.block_entity.HollowLogBlockEntity;
import org.primal.registry.Primal_BlockEntities;
import org.primal.util.mob_types.HideOnLog;

import java.util.List;

public class HollowLogBlock extends BaseEntityBlock {
    public static final MapCodec<HollowLogBlock> CODEC = simpleCodec(HollowLogBlock::new);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;

    //──────────────────────────────────── Init ────────────────────────────────────
    public HollowLogBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.defaultBlockState()
                        .setValue(AXIS, Direction.Axis.Y)
                        .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, AXIS);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new HollowLogBlockEntity(Primal_BlockEntities.HOLLOW_LOG.get(), pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, Primal_BlockEntities.HOLLOW_LOG.get(), HollowLogBlockEntity::serverTick);
    }

    //──────────────────────────────────── Release Logic ────────────────────────────────────
    @Override
    public void playerDestroy(@NotNull Level level, @NotNull Player player, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable BlockEntity te, @NotNull ItemStack stack) {
        super.playerDestroy(level, player, pos, state, te, stack);
        if (!level.isClientSide && te instanceof HollowLogBlockEntity hollowLogBlockEntity) {
            if (!EnchantmentHelper.hasTag(stack, EnchantmentTags.PREVENTS_BEE_SPAWNS_WHEN_MINING)) {
                hollowLogBlockEntity.emptyAllLivingFromLog(player, state, HollowLogBlockEntity.AnimalReleaseStatus.EMERGENCY);
                level.updateNeighbourForOutputSignal(pos, this);
                this.angerNearHideLogAnimals(level, pos);
            }

            //Triggers advancement
//            CriteriaTriggers.BEE_NEST_DESTROYED.trigger((ServerPlayer)player, state, stack, hollowLogBlockEntity.getOccupantCount());
        }
    }

    @Override
    protected @NotNull List<ItemStack> getDrops(@NotNull BlockState state, LootParams.Builder params) {
        Entity entity = params.getOptionalParameter(LootContextParams.THIS_ENTITY);
        if (entity instanceof PrimedTnt
                || entity instanceof Creeper
                || entity instanceof WitherSkull
                || entity instanceof WitherBoss
                || entity instanceof MinecartTNT) {
            BlockEntity blockentity = params.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
            if (blockentity instanceof HollowLogBlockEntity hollowLogBlockEntity) {
                hollowLogBlockEntity.emptyAllLivingFromLog(null, state, HollowLogBlockEntity.AnimalReleaseStatus.EMERGENCY);
            }
        }

        return super.getDrops(state, params);
    }

    @Override
    protected @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction facing, @NotNull BlockState facingState, LevelAccessor level, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {
        if (level.getBlockState(facingPos).getBlock() instanceof FireBlock && level.getBlockEntity(currentPos) instanceof HollowLogBlockEntity hollowLogBlockEntity) {
            hollowLogBlockEntity.emptyAllLivingFromLog(null, state, HollowLogBlockEntity.AnimalReleaseStatus.EMERGENCY);
        }

        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    public @NotNull BlockState playerWillDestroy(Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        if (!level.isClientSide
                && player.isCreative()
                && level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)
                && level.getBlockEntity(pos) instanceof HollowLogBlockEntity hollowLogBlockEntity) {
            if (!hollowLogBlockEntity.isEmpty()) {
                ItemStack itemstack = new ItemStack(this);
                itemstack.applyComponents(hollowLogBlockEntity.collectComponents());
                ItemEntity itementity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), itemstack);
                itementity.setDefaultPickUpDelay();
                level.addFreshEntity(itementity);
            }
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

    private void angerNearHideLogAnimals(Level level, BlockPos pos) {
        AABB aabb = new AABB(pos).inflate(8.0, 6.0, 8.0);

        List<Animal> animals = level.getEntitiesOfClass(Animal.class, aabb);
        if (!animals.isEmpty()) {

            List<Player> players = level.getEntitiesOfClass(Player.class, aabb);
            if (players.isEmpty()) {
                return;
            }

            for (Animal animal : animals) {
                if(animal instanceof HideOnLog){
                    if (animal.getTarget() == null) {
                        Player player = Util.getRandom(players, level.random);
                        animal.setTarget(player);
                        animal.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, player);
                    }
                }
            }
        }
    }

    //──────────────────────────────────── Rotation ────────────────────────────────────
    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        var facing = context.getHorizontalDirection().getOpposite();
        var axis = context.getClickedFace().getAxis();

        boolean bottom = !(context.getClickLocation().y - context.getClickedPos().getY() > 0.5);

        //Proper transformations
        switch (axis){
            case X -> {
                boolean IsValid = facing.equals(Direction.SOUTH) || facing.equals(Direction.NORTH);
                if(IsValid){
                    facing = facing.equals(Direction.SOUTH)? Direction.WEST: Direction.EAST;
                } else
                    facing= bottom? Direction.NORTH: Direction.SOUTH;
            }
            case Z -> {
                boolean IsValid = facing.equals(Direction.WEST) || facing.equals(Direction.EAST);
                if(!IsValid) facing= bottom? Direction.NORTH: Direction.SOUTH;
            }
            case Y -> {
            }
        }

        return this.defaultBlockState()
                .setValue(FACING, facing)
                .setValue(AXIS, axis);
    }
}
