package org.primal.block_entity;

import net.minecraft.core.*;
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
import net.minecraft.world.entity.AnimationState;
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
import org.primal.registry.Primal_DamageTypes;
import org.primal.util.Primal_Util;
import org.primal.util.block_types.AnimatableBlockEntity;

public class ChompTrapBlockEntity extends BlockEntity implements AnimatableBlockEntity, GameEventListener.Holder<ChompTrapBlockEntity.ChompTrapListener>, Container, Clearable {

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
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState transitionAnimationState = new AnimationState();

    private float animationTick = 0;
    private float prevAnimationTick = 0;

    public boolean mustReset = false;

    /** Smooth animation time in ticks with partial-tick interpolation. */
    public float getAnimationTime(float partialTick) {
        return prevAnimationTick + (animationTick - prevAnimationTick) * partialTick;
    }

    public void clientTick(Level level, BlockPos pos, BlockState state) {
        prevAnimationTick = animationTick;
        animationTick++;

        if(mustReset) mustReset = false;
    }

    public void setOnTransitionTime() {
        this.mustReset = true;
    }
}