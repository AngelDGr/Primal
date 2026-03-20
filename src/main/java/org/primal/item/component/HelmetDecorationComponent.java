package org.primal.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import org.jetbrains.annotations.NotNull;
import org.primal.item.HelmetDecorationType;
import org.primal.registry.Primal_Items;

import java.util.function.Consumer;

public record HelmetDecorationComponent(HelmetDecorationType right, HelmetDecorationType left) implements TooltipProvider {

    public static final Codec<HelmetDecorationComponent> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    Codec.INT
                            .fieldOf("right")
                            .forGetter(c-> c.right.getId()),
                    Codec.INT
                            .fieldOf("left")
                            .forGetter(c-> c.left.getId())
            ).apply(instance, (r, l)-> new HelmetDecorationComponent(HelmetDecorationType.BY_ID.apply(r), HelmetDecorationType.BY_ID.apply(l))));

    @Override
    public void addToTooltip(Item.@NotNull TooltipContext context, @NotNull Consumer<Component> tooltipAdder, @NotNull TooltipFlag tooltipFlag) {

        tooltipAdder.accept(Component.translatable("helmet_decoration.decorations").withStyle(ChatFormatting.GRAY));

        if(!this.left.equals(HelmetDecorationType.NONE))
            tooltipAdder.accept(CommonComponents.space().append( Component.translatable("helmet_decoration.primal."+this.left.getName())).withStyle(this.left.getDescriptionStyle()));

        if(!this.right.equals(HelmetDecorationType.NONE))
            tooltipAdder.accept(CommonComponents.space().append(Component.translatable("helmet_decoration.primal."+this.right.getName())).withStyle(this.right.getDescriptionStyle()));
    }


    public static ItemStack of(final ItemStack helmet, ItemStack toAttach, boolean left){

        var decoration = HelmetDecorationType.fromItem(toAttach.getItem());

        //Avoids
        if(decoration == HelmetDecorationType.NONE)
            return helmet;

        var currentDecoration = helmet.get(Primal_Items.Components.HELMET_DECORATION);
        //For combine
        if(currentDecoration!=null){
            helmet.set(Primal_Items.Components.HELMET_DECORATION,
                    new HelmetDecorationComponent(left? currentDecoration.right(): decoration, left? decoration: currentDecoration.left()));

        }
        //To add new
        else {
            helmet.set(Primal_Items.Components.HELMET_DECORATION,
                    new HelmetDecorationComponent(left? HelmetDecorationType.NONE: decoration, left? decoration: HelmetDecorationType.NONE));
        }

        return helmet;
    }

}
