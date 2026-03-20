package org.primal.mixin;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.primal.registry.Primal_Tags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {

    public AnvilMenuMixin(@Nullable MenuType<?> type, int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(type, containerId, playerInventory, access);
    }

    @ModifyArg(method = "onTake", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Container;setItem(ILnet/minecraft/world/item/ItemStack;)V", ordinal = 0))
    private ItemStack primal$avoidWastingHelmetAttachments(ItemStack stack){
        ItemStack currentLeft = this.inputSlots.getItem(0);
        ItemStack currentRight = this.inputSlots.getItem(1);

        if(currentLeft.is(Primal_Tags.Item.HELMET_ATTACHMENTS) && currentRight.is(ItemTags.HEAD_ARMOR)){

            var result = currentLeft.copy();
            result.shrink(1);
            return result;
        }


        return stack;
    }
}
