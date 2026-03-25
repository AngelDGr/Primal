package org.primal.block_entity;

import net.minecraft.core.*;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.primal.block.ChompTrapBlock;
import org.primal.registry.Primal_DamageTypes;
import org.primal.util.Primal_Util;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ChompTrapBlockEntity extends BlockEntity implements GeoBlockEntity, GameEventListener.Holder<ChompTrapBlockEntity.ChompTrapListener>, Container, Clearable {

    private final ChompTrapBlockEntity.ChompTrapListener chompTrapListener;
    private final NonNullList<ItemStack> items = NonNullList.withSize(9, ItemStack.EMPTY);
    public String colorType;
    //──────────────────────────────────── Init ────────────────────────────────────

    public ChompTrapBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, String colorType) {
        super(type, pos, blockState);
        this.colorType = colorType;
        this.chompTrapListener = new ChompTrapBlockEntity.ChompTrapListener(new BlockPositionSource(pos));
    }

    public ChompTrapBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        this(type, pos, blockState, "green");
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString("Type", colorType);
        ContainerHelper.saveAllItems(tag, this.getItems());
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        this.colorType = tag.getString("Type");
        ContainerHelper.loadAllItems(tag, this.getItems());
    }

    // Create an update tag here. For block entities with only a few fields, this can just call #saveAdditional.
    @Override
    public @NotNull CompoundTag getUpdateTag() {
        var tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    // Handle a received update tag here. The default implementation calls #loadAdditional here,
    // so you do not need to override this method if you don't plan to do anything beyond that.
    @Override
    public void handleUpdateTag(@NotNull CompoundTag tag) {
        super.handleUpdateTag(tag);
    }

    // Return our packet here. This method returning a non-null result tells the game to use this packet for syncing.
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        // The packet uses the CompoundTag returned by #getUpdateTag. An alternative overload of #create exists
        // that allows you to specify a custom update tag, including the ability to omit data the client might not need.
        return ClientboundBlockEntityDataPacket.create(this);
    }

    // Optionally: Run some custom logic when the packet is received.
    // The super/default implementation forwards to #loadAdditional.
    @Override
    public void onDataPacket(@NotNull Connection connection, @NotNull ClientboundBlockEntityDataPacket packet) {
        super.onDataPacket(connection, packet);
    }

    //──────────────────────────────────── Inventory ────────────────────────────────────
    @Override
    public void setChanged() {
        super.setChanged();
        //Sends the packet correctly
//        if (level instanceof ServerLevel serverLevel)
//            serverLevel.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
    }

    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public void clearContent() {
        this.getItems().clear();
        this.setChanged();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.getItems())
            //If it finds any non-empty stack, is false
            if (!itemstack.isEmpty())
                return false;

        return true;
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        return this.getItems().get(slot);
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int amount) {
        ItemStack itemstack = ContainerHelper.removeItem(this.getItems(), slot, amount);
        if (!itemstack.isEmpty())
            this.setChanged();

        return itemstack;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.getItems(), slot);
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack stack) {
        this.getItems().set(slot, stack);
        Primal_Util.OneTwentyEquivalent.limitSize(this.getMaxStackSize(), stack);
        this.setChanged();
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    public ItemStack insertItem(ItemStack stack) {
        int i = this.getMaxStackSize();

        for (int j = 0; j < this.getItems().size(); j++) {
            ItemStack itemstack = this.getItems().get(j);

            if (itemstack.isEmpty() || ItemStack.isSameItemSameTags(stack, itemstack)) {
                int k = Math.min(stack.getCount(), i - itemstack.getCount());

                if (k > 0) {
                    if (itemstack.isEmpty()) {
                        this.setItem(j, stack.split(k));
                    } else {
                        stack.shrink(k);
                        itemstack.grow(k);
                    }
                }

                if (stack.isEmpty()) {
                    break;
                }
            }
        }

        // Drop remaining items if they don't fit
        if (!stack.isEmpty() && this.level != null && !this.level.isClientSide) {
            Containers.dropItemStack(
                    this.level,
                    this.worldPosition.getX(),
                    this.worldPosition.getY(),
                    this.worldPosition.getZ(),
                    stack
            );
            stack = ItemStack.EMPTY;
        }

        this.setChanged();
        return stack;
    }

    //──────────────────────────────────── Drop Experience ────────────────────────────────────
    public ChompTrapBlockEntity.@NotNull ChompTrapListener getListener() {
        return this.chompTrapListener;
    }

    public static class ChompTrapListener implements GameEventListener {
        private final PositionSource positionSource;

        public ChompTrapListener(PositionSource positionSource) {
            this.positionSource = positionSource;
        }

        @Override
        public @NotNull PositionSource getListenerSource() {
            return this.positionSource;
        }

        @Override
        public int getListenerRadius() {
            return 1;
        }

        @Override
        public GameEventListener.@NotNull DeliveryMode getDeliveryMode() {
            return GameEventListener.DeliveryMode.BY_DISTANCE;
        }

        @Override
        public boolean handleGameEvent(@NotNull ServerLevel level, GameEvent gameEvent, GameEvent.@NotNull Context context, @NotNull Vec3 pos) {
            if (gameEvent.equals(GameEvent.ENTITY_DIE) && context.sourceEntity() instanceof LivingEntity livingentity) {

                if (!livingentity.wasExperienceConsumed()) {
                    DamageSource damagesource = livingentity.getLastDamageSource();
                    //Only killed by a chomp trap
                    if(damagesource!=null && !damagesource.is(Primal_DamageTypes.CHOMP_TRAP)) return false;

                    int i = livingentity.getExperienceReward();
                    if (livingentity.shouldDropExperience() && i > 0) {
                        livingentity.skipDropExperience();
                        ExperienceOrb.award(level, pos.add(0, 2, 0), i);
                    }
                }

                return true;
            } else {
                return false;
            }
        }
    }

    //──────────────────────────────────── Visuals ────────────────────────────────────
    public static final RawAnimation LOWER = RawAnimation.begin().thenPlay("lower");
    public static final RawAnimation SNAP = RawAnimation.begin().thenPlay("snap");
    public static final RawAnimation LOWER_BROKEN = RawAnimation.begin().thenPlay("lower_broken");

    public static final RawAnimation LOWERED = RawAnimation.begin().thenLoop("lowered");
    public static final RawAnimation SNAPPED = RawAnimation.begin().thenLoop("snapped");
    public static final RawAnimation BROKEN = RawAnimation.begin().thenLoop("broken");
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, state -> {
            state.getController().transitionLength(0);
            state.setControllerSpeed(1f);
            var blockState = this.getBlockState();

            if (state.getController().isPlayingTriggeredAnimation()) {
                return PlayState.CONTINUE;
            }

            return state.setAndContinue(blockState.getValue(ChompTrapBlock.BROKEN)? BROKEN: blockState.getValue(ChompTrapBlock.OPEN)? LOWERED : SNAPPED);
        }).receiveTriggeredAnimations()
                        .triggerableAnim("lower", LOWER)
                        .triggerableAnim("lower_broken", LOWER_BROKEN)
                        .triggerableAnim("snap", SNAP)
                        .setCustomInstructionKeyframeHandler(c-> {
                            if("chompParticles;".equals(c.getKeyframeData().getInstructions()))
                                spawnChompParticles(this.getLevel(), getBlockPos());

                            if("groundParticles;".equals(c.getKeyframeData().getInstructions()))
                                spawnGroundParticles(this.getLevel(), getBlockPos());
                        })
        );
    }

    private static void spawnChompParticles(
            Level level,
            BlockPos blockPos
    ) {
        for (int i = 0; i < 12; i++) {
            double d0 = blockPos.getX() + (level.getRandom().nextDouble());
            double d1 = blockPos.getY() + level.getRandom().nextDouble();
            double d2 = blockPos.getZ() + (level.getRandom().nextDouble());

            double d3 = (level.getRandom().nextDouble() * 2.0 - 1.0) * 0.3;
            double d4 = 0.3 + level.getRandom().nextDouble() * 0.3;
            double d5 = (level.getRandom().nextDouble() * 2.0 - 1.0) * 0.3;

            level.addParticle(ParticleTypes.CRIT,
                    d0, d1 + 0.8, d2,
                    d3, d4, d5);
        }
    }

    private static void spawnGroundParticles(
            Level level,
            BlockPos blockPos
    ) {
        if (level == null) return;

        var state = level.getBlockState(blockPos.below());
        ParticleOptions particle = new BlockParticleOption(ParticleTypes.BLOCK, state);

        double centerX = blockPos.getCenter().x;
        double centerY = blockPos.getY();
        double centerZ = blockPos.getCenter().z;

        for (int i = 0; i < 2; i++) {

            // Radius around block (adjust for spread)
            double radius = 0.7;

            double offsetX = (level.getRandom().nextDouble() * 2 - 1) * radius;
            double offsetZ = (level.getRandom().nextDouble() * 2 - 1) * radius;

            double x = centerX + offsetX;
            double y = centerY + (level.getRandom().nextDouble() * 0.5);
            double z = centerZ + offsetZ;

            double motionX = (level.getRandom().nextDouble() * 2 - 1) * 0.1;
            double motionY = 0.2 + level.getRandom().nextDouble() * 0.2;
            double motionZ = (level.getRandom().nextDouble() * 2 - 1) * 0.1;

            level.addParticle(particle, x, y, z, motionX, motionY, motionZ);
        }
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}