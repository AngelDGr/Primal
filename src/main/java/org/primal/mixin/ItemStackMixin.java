package org.primal.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.primal.util.Primal_Util;
import org.primal.registry.Primal_Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Unique
    ItemStack p$THIS = (ItemStack)(Object)this;

    @Inject(method = "getTooltipLines(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/TooltipFlag;)Ljava/util/List;",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/nbt/CompoundTag;contains(Ljava/lang/String;I)Z",
                    ordinal = 0))
    private void primal$addConchShellTooltip(Player player,
                                             TooltipFlag flag,
                                             CallbackInfoReturnable<List<Component>> cir,
                                             @Local List<Component> consumer){
        var component = Primal_Util.OneTwentyEquivalent.Components.get(p$THIS, Primal_Items.Components.CONCH_SHELL);
        if(component!=null){
            component.addToTooltip(player.level(), consumer, flag);
        }
    }

    @Inject(method = "getTooltipLines(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/TooltipFlag;)Ljava/util/List;",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;shouldShowInTooltip(ILnet/minecraft/world/item/ItemStack$TooltipPart;)Z",
                    ordinal = 2))
    private void primal$addHelmetDecorationsTooltip(Player player,
                                                    TooltipFlag flag,
                                                    CallbackInfoReturnable<List<Component>> cir,
                                                    @Local List<Component> consumer){
        var component = Primal_Util.OneTwentyEquivalent.Components.get(p$THIS, Primal_Items.Components.HELMET_DECORATION);
        if(component!=null){
            component.addToTooltip(player.level(), consumer, flag);
        }
    }

    @Inject(method = "getTooltipLines(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/TooltipFlag;)Ljava/util/List;",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;shouldShowInTooltip(ILnet/minecraft/world/item/ItemStack$TooltipPart;)Z",
                    ordinal = 2))
    private void primal$addSnakeTooltip(Player player,
                                                    TooltipFlag flag,
                                                    CallbackInfoReturnable<List<Component>> cir,
                                                    @Local List<Component> consumer){
        var component = Primal_Util.OneTwentyEquivalent.Components.get(p$THIS, Primal_Items.Components.SNAKE_SPAWN);
        if(component!=null){
            component.addToTooltip(player.level(), consumer, flag);
        }
    }
}
