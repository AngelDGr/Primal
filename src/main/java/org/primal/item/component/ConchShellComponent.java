package org.primal.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import org.jetbrains.annotations.NotNull;
import org.primal.registry.Primal_Items;

import java.util.UUID;
import java.util.function.Consumer;

public record ConchShellComponent(Component name, UUID pet) implements TooltipProvider {

    public static final Codec<ConchShellComponent> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    ComponentSerialization.CODEC
                            .fieldOf("variant")
                            .forGetter(ConchShellComponent::name),
                    UUIDUtil.CODEC
                            .fieldOf("variantFromBiome")
                            .forGetter(ConchShellComponent::pet)
            ).apply(instance, ConchShellComponent::new));

    public static ItemStack of(final ItemStack stack, final UUID pet, Component name){
        stack.set(Primal_Items.Components.CONCH_SHELL, new ConchShellComponent(name, pet));

        return stack;
    }

    @Override
    public void addToTooltip(Item.@NotNull TooltipContext context, @NotNull Consumer<Component> tooltipAdder, @NotNull TooltipFlag tooltipFlag) {
        tooltipAdder.accept(Component.translatable("item.primal.conch_shell.tooltip.name").withStyle(ChatFormatting.GRAY).append(this.name).withStyle(this.name.getStyle()));
    }
}
