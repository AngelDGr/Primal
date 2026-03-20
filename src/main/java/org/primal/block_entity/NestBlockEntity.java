package org.primal.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.primal.util.block_types.AnimalEgg;

import javax.annotation.Nullable;
import java.util.List;

public class NestBlockEntity extends BlockEntity {

    private final NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);

    public NestBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, this.items, registries);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        ContainerHelper.loadAllItems(tag, this.items, registries);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        return this.saveCustomOnly(registries);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(@NotNull Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.@NotNull Provider lookup) {
        this.loadAdditional(pkt.getTag(), lookup);
    }

    public void updateBlock(){
        this.setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    //Set egg logic
    public void removeEgg(@Nullable LivingEntity target) {
        //This makes animals of the same egg type mad if you steal eggs
        if(getEgg().getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof AnimalEgg animalEgg && target!=null){
            if(this.getLevel()!=null){
                //Get all entities nearby, then filters them by the same type as the egg
                List<Entity> allEntitiesList = this.getLevel().getEntitiesOfClass(
                                Entity.class,
                                new AABB(this.getBlockPos()).inflate(40))
                        .stream().filter(
                                entity -> entity.getType() == animalEgg.getAnimal().get())
                        .toList();

                for (Entity entityNearby: allEntitiesList){
                    if(entityNearby instanceof LivingEntity animal && !animal.isBaby()){
                        Brain<?> brain = animal.getBrain();
                        brain.eraseMemory(MemoryModuleType.PACIFIED);
                        brain.eraseMemory(MemoryModuleType.BREED_TARGET);
                        brain.setMemory(MemoryModuleType.ATTACK_TARGET, target);
                    }
                }
            }
        }

        this.setItem(
                //If the egg amount is equal or less than 0, put an empty stack
                this.getEgg().getCount() - 1 <= 0?
                ItemStack.EMPTY:
                new ItemStack(getEgg().getItem(), this.getEgg().getCount() - 1));

        this.updateBlock();
    }

    public void addEgg(ItemStack stack, @Nullable LivingEntity player) {
        this.setItem(new ItemStack(stack.getItem(), this.getEgg().getCount() + 1) );

        this.updateBlock();
    }

    public void setEgg(ItemStack stack, @Nullable LivingEntity player) {
        this.setItem(stack);

        this.updateBlock();
    }

    public ItemStack getEgg() {
        return this.items.getFirst();
    }

    public int getEggsAmount() {
        return this.items.getFirst().getCount();
    }

    public void setItem(ItemStack stack) {
        this.items.set(0, stack);
    }

}
