package org.primal.item.component;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.primal.util.Primal_Util;

import java.util.List;
import java.util.UUID;

public record ConchShellComponent(Component name, UUID pet) implements Primal_Util.OneTwentyEquivalent.Components.BaseComponent<ConchShellComponent> {

    @Override
    public CompoundTag addComponent(CompoundTag compoundTag){
        CompoundTag nbt = new CompoundTag();
        nbt.putString("name", Component.Serializer.toJson(this.name));
        nbt.putUUID("pet", pet);
        compoundTag.put(this.getComponentName(), nbt);

        return compoundTag;
    }

    @Override
    public ConchShellComponent create(CompoundTag compoundTag) {
        return new ConchShellComponent(Component.Serializer.fromJson(compoundTag.getString("name")), compoundTag.getUUID("pet"));
    }

    @Override
    public String getComponentName() {
        return "ConchShell";
    }

    public static ItemStack of(final ItemStack stack, final UUID pet, Component name){
        var compoundTag = new CompoundTag();
        var component = new ConchShellComponent(name, pet);
        stack.addTagElement(component.getComponentName(), component.addComponent(compoundTag));

        return stack;
    }

    @Override
    public void addToTooltip(Level level, @NotNull List<Component> tooltipAdder, @NotNull TooltipFlag tooltipFlag) {
        tooltipAdder.add(Component.translatable("item.primal.conch_shell.tooltip.name").withStyle(ChatFormatting.GRAY).append(this.name).withStyle(this.name.getStyle()));
    }
}
