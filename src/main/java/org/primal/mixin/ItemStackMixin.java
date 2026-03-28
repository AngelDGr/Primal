package org.primal.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import org.primal.registry.Primal_Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract <T extends TooltipProvider> void addToTooltip(DataComponentType<T> component, Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag);

    @Inject(method = "getTooltipLines(Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/TooltipFlag;)Ljava/util/List;",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;addToTooltip(Lnet/minecraft/core/component/DataComponentType;Lnet/minecraft/world/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V",
                    ordinal = 4))
    private void primal$addConchShellTooltip(Item.TooltipContext tooltipContext,
                                        @Nullable Player player,
                                        TooltipFlag tooltipFlag, final CallbackInfoReturnable<AttributeSupplier.Builder> cir,
                                        @Local Consumer<Component> consumer){

        this.addToTooltip(Primal_Items.Components.CONCH_SHELL.get(), tooltipContext, consumer, tooltipFlag);
    }

    @Inject(method = "getTooltipLines(Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/TooltipFlag;)Ljava/util/List;",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;addToTooltip(Lnet/minecraft/core/component/DataComponentType;Lnet/minecraft/world/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V",
                    ordinal = 2))
    private void primal$addHelmetDecorationsTooltip(Item.TooltipContext tooltipContext,
                                             @Nullable Player player,
                                             TooltipFlag tooltipFlag, final CallbackInfoReturnable<AttributeSupplier.Builder> cir,
                                             @Local Consumer<Component> consumer){

        this.addToTooltip(Primal_Items.Components.HELMET_DECORATION.get(), tooltipContext, consumer, tooltipFlag);
    }

    @Inject(method = "getTooltipLines(Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/TooltipFlag;)Ljava/util/List;",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;addToTooltip(Lnet/minecraft/core/component/DataComponentType;Lnet/minecraft/world/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V",
                    ordinal = 2))
    private void primal$addSnakeTooltip(Item.TooltipContext tooltipContext,
                                                    @Nullable Player player,
                                                    TooltipFlag tooltipFlag, final CallbackInfoReturnable<AttributeSupplier.Builder> cir,
                                                    @Local Consumer<Component> consumer){

        this.addToTooltip(Primal_Items.Components.SNAKE_SPAWN.get(), tooltipContext, consumer, tooltipFlag);
    }
}
