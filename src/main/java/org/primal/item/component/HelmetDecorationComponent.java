package org.primal.item.component;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.primal.item.HelmetDecorationType;
import org.primal.registry.Primal_Items;
import org.primal.util.Primal_Util;

import java.util.List;

public record HelmetDecorationComponent(HelmetDecorationType right, HelmetDecorationType left) implements Primal_Util.OneTwentyEquivalent.Components.BaseComponent<HelmetDecorationComponent> {

    @Override
    public CompoundTag addComponent(CompoundTag compoundTag) {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("right", right.getId());
        nbt.putInt("left", left.getId());
        compoundTag.put(this.getComponentName(), nbt);

        return compoundTag;
    }

    @Override
    public HelmetDecorationComponent create(CompoundTag compoundTag) {
        int r = compoundTag.getInt("right");
        int l = compoundTag.getInt("left");
        return new HelmetDecorationComponent(HelmetDecorationType.BY_ID.apply(r), HelmetDecorationType.BY_ID.apply(l));
    }

    @Override
    public String getComponentName() {
        return "HelmetDecorations";
    }

    @Override
    public void addToTooltip(Level level, @NotNull List<Component> tooltipAdder, @NotNull TooltipFlag tooltipFlag) {
        tooltipAdder.add(Component.translatable("helmet_decoration.decorations").withStyle(ChatFormatting.GRAY));

        if(!this.left.equals(HelmetDecorationType.NONE))
            tooltipAdder.add(CommonComponents.space().append( Component.translatable("helmet_decoration.primal."+this.left.getName())).withStyle(this.left.getDescriptionStyle()));

        if(!this.right.equals(HelmetDecorationType.NONE))
            tooltipAdder.add(CommonComponents.space().append(Component.translatable("helmet_decoration.primal."+this.right.getName())).withStyle(this.right.getDescriptionStyle()));
    }

    public static ItemStack of(final ItemStack helmet, ItemStack toAttach, boolean left){

        var decoration = HelmetDecorationType.fromItem(toAttach.getItem());

        //Avoids
        if(decoration == HelmetDecorationType.NONE)
            return helmet;

        var currentDecoration = Primal_Util.OneTwentyEquivalent.Components.get(helmet, Primal_Items.Components.HELMET_DECORATION);
        //For combine
        if(currentDecoration!=null){
            Primal_Util.OneTwentyEquivalent.Components.set(helmet,
                    new HelmetDecorationComponent(left? currentDecoration.right(): decoration, left? decoration: currentDecoration.left()));

        }
        //To add new
        else {
            Primal_Util.OneTwentyEquivalent.Components.set(helmet,
                    new HelmetDecorationComponent(left? HelmetDecorationType.NONE: decoration, left? decoration: HelmetDecorationType.NONE));
        }

        return helmet;
    }

}
