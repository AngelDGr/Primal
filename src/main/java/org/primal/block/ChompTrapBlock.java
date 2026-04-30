package org.primal.block;

import com.mojang.authlib.GameProfile;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.block_entity.ChompTrapBlockEntity;
import org.primal.registry.Primal_BlockEntities;
import org.primal.registry.Primal_DamageTypes;
import org.primal.registry.Primal_Sounds;

import java.util.UUID;

public class ChompTrapBlock extends BaseEntityBlock {
    public final String type;
    public static final MapCodec<ChompTrapBlock> CODEC = simpleCodec(ChompTrapBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty BROKEN = BooleanProperty.create("broken");

    public ChompTrapBlock(Properties properties) {
        this(properties, "green");
    }

    public ChompTrapBlock(Properties properties, String type) {
        super(properties);
        this.type=type;
        this.registerDefaultState(this.stateDefinition.any()
                        .setValue(FACING, Direction.NORTH)
                        .setValue(OPEN, false)
                        .setValue(POWERED, false)
                        .setValue(BROKEN, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN, POWERED, BROKEN);
    }

    @Override
    protected boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        return !level.isEmptyBlock(pos.below());
    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return state.getValue(OPEN)? Shapes.empty(): Shapes.block();
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return Block.box(0.0, 0.0, 0.0, 16, (state.getValue(OPEN)?2.0: 16.0), 16);
    }

    @Override
    protected boolean hasAnalogOutputSignal(@NotNull BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos) {
        return 0;
    }

    private void doBreak(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos){
        level.playSound(null, pos, Primal_Sounds.CHOMP_TRAP_BREAKS.get(), SoundSource.BLOCKS, 1.0F, 1.0F);

        // Center of block
        double centerX = pos.getX() + 0.5;
        double centerY = pos.getY() + 0.5;
        double centerZ = pos.getZ() + 0.5;

        // Block debris particles
        BlockParticleOption blockParticles = new BlockParticleOption(ParticleTypes.BLOCK, state);

        level.sendParticles(
                blockParticles,
                centerX,
                centerY,
                centerZ,
                30,
                0.5, 0.2, 0.5,
                0.15
        );

        // Small burst upward
        level.sendParticles(
                ParticleTypes.CRIT,
                centerX,
                centerY + 0.2,
                centerZ,
                10,
                0.2, 0.1, 0.2,
                0.1
        );

        // Light smoke
        level.sendParticles(
                ParticleTypes.SMOKE,
                centerX,
                centerY,
                centerZ,
                8,
                0.2, 0.1, 0.2,
                0.02
        );

        level.setBlock(pos,
                state.setValue(OPEN, true)
                        .setValue(BROKEN, true), Block.UPDATE_ALL);
    }

    private void doRepair(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos){
        // Update state first
        level.setBlock(pos, state.setValue(BROKEN, false), Block.UPDATE_ALL);

        // Sound
        level.playSound(null, pos, Primal_Sounds.CHOMP_TRAP_REPAIRS.get(), SoundSource.BLOCKS, 1.0F, 1.0F
        );

        // Particles
        if (level instanceof ServerLevel serverLevel) {

            double centerX = pos.getX() + 0.5;
            double centerY = pos.getY();
            double centerZ = pos.getZ() + 0.5;

            // Small metal spark burst
            serverLevel.sendParticles(ParticleTypes.CRIT, centerX, centerY, centerZ, 15,
                    0.5, 0.2, 0.5,
                    0.15);
        }
    }

    private void toggleOpen(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos){
        boolean isOpen = state.getValue(OPEN);

        level.setBlock(
                pos,
                state.setValue(OPEN, !isOpen),
                Block.UPDATE_ALL
        );
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        //Only manual if no powered
        if (!state.getValue(POWERED)) {
            if(player.isSecondaryUseActive()){
                // Repairs
                if (state.getValue(BROKEN)) {
                    if (!level.isClientSide)
                        doRepair(state, level, pos);
                }
                else
                    //Manually open
                    if(!level.isClientSide)
                        toggleOpen(state, level, pos);

                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                //FOR SOME FUCKING REASON I COULDN'T TO THE CLIENT, SO FUCK IT, JUST IS GOING TO BE IN THE SERVER
                if (level.getBlockEntity(pos) instanceof ChompTrapBlockEntity chompTrapBlock && !chompTrapBlock.isEmpty() && !level.isClientSide) {
                    // Normal right click: return stored items
                    this.returnItemsToPlayer(level, pos, player, chompTrapBlock);

                    return InteractionResult.SUCCESS;
                }
            }
        }

        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    private void returnItemsToPlayer(Level level, BlockPos pos, Player player, ChompTrapBlockEntity trap) {

        for (int i = 0; i < trap.getContainerSize(); i++) {
            ItemStack stack = trap.removeItem(i, trap.getItem(i).getCount());

            if (!stack.isEmpty()) {
                if (!player.getInventory().add(stack)) {
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
                }
            }
        }

        trap.setChanged();
        level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    @Override
    public void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Block block, @NotNull BlockPos fromPos, boolean isMoving) {
        boolean flag = level.hasNeighborSignal(pos);
        if (!this.defaultBlockState().is(block) && flag != state.getValue(POWERED)) {
            if(state.getValue(BROKEN))
                doRepair(state, level, pos);
            else {
                if (flag != state.getValue(OPEN))
                    level.gameEvent(null, flag ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);

                level.setBlock(pos,
                        state.setValue(POWERED, flag)
                                .setValue(OPEN, flag)
                                .setValue(BROKEN, false), 2);
            }
        }
    }

    @Override
    protected void tick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (state.getValue(OPEN) || state.getValue(BROKEN) || state.getValue(POWERED)) return;

        AABB checkBox = new AABB(pos); // full block space

        boolean hasEntityInside = !level.getEntitiesOfClass(
                LivingEntity.class,
                checkBox
        ).isEmpty();

        //Breaks the trap
        if (hasEntityInside) doBreak(state, level, pos);
    }

    @Override
    protected void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {

        if (!state.getValue(OPEN)) {
            entity.hurt(Primal_DamageTypes.chompTrap(level, pos.getBottomCenter()), 10.0F);
            if (entity instanceof LivingEntity living) {
                //To drop items only dropped when killed by a player
                if(!level.isClientSide){
                    FakePlayer chompTrapFakePlayer = new FakePlayer((ServerLevel) level, new GameProfile(UUID.randomUUID(), "Chomp Trap"));
                    living.setLastHurtByPlayer(chompTrapFakePlayer);
                }

                // Stop movement
                living.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SLOWDOWN,
                        10,
                        10,
                        false,
                        false,
                        false
                ));

                // Prevent jump
                entity.setDeltaMovement(
                        entity.getDeltaMovement().x,
                        -entity.getDeltaMovement().y,
                        entity.getDeltaMovement().z
                );

                //Can't mine
                living.addEffect(new MobEffectInstance(
                        MobEffects.DIG_SLOWDOWN,
                        10,
                        10,
                        false,
                        false,
                        false
                ));
            }
        }

        //Auto close logic
        // Smaller trigger area
        double sizeXZ = 0.4;
        double height = 0.3;

        double minX = pos.getX() + (0.5 - sizeXZ / 2);
        double minZ = pos.getZ() + (0.5 - sizeXZ / 2);

        AABB triggerBox = new AABB(
                minX,
                pos.getY(),
                minZ,
                minX + sizeXZ,
                pos.getY() + height,
                minZ + sizeXZ
        );

        if (!entity.getBoundingBox().intersects(triggerBox))
            return; // Not deep enough inside

        //Closes when an entity is nearby, only if not broken and no powered
        if (!level.isClientSide && state.getValue(OPEN) && !state.getValue(BROKEN) && !state.getValue(POWERED)) {
            level.setBlock(
                    pos,
                    state.setValue(OPEN, false),
                    Block.UPDATE_ALL
            );
        }
    }

    @Override
    protected void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        if(level.getBlockEntity(pos) instanceof ChompTrapBlockEntity){
            if (!level.isClientSide) {
                BlockEntity be = level.getBlockEntity(pos);

                if (be instanceof ChompTrapBlockEntity trap) {
                    if(newState.getBlock()!=this)
                        Containers.dropContents(level, pos, trap.getItems());
                    level.updateNeighbourForOutputSignal(pos, this);
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public void onBlockStateChange(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState oldState, @NotNull BlockState newState) {
        super.onBlockStateChange(level, pos, oldState, newState);
        //Trigger transition animation and sound
        if(oldState.is(this) && newState.is(this)){
            if(level.getBlockEntity(pos) instanceof ChompTrapBlockEntity chompTrap && (oldState.getValue(OPEN)!=newState.getValue(OPEN))){
                //Triggers animation (lower/lower_broken/snap)
                if(level.isClientSide()) chompTrap.setOnTransitionTime();
                //Trigger Sound
                if(level instanceof Level realLevel && !newState.getValue(OPEN)){
                    realLevel.scheduleTick(pos, this, 60);
                    realLevel.playSound(null, pos, Primal_Sounds.CHOMP_TRAP_CLOSES.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            }
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    protected @NotNull BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    protected @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new ChompTrapBlockEntity(Primal_BlockEntities.CHOMP_TRAP.get(), pos, state, this.type);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        if (level.isClientSide)
            return createTickerHelper(blockEntityType, Primal_BlockEntities.CHOMP_TRAP.get(),
                    (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.clientTick(pLevel1, pPos, pState1));

        else return null;
    }
}