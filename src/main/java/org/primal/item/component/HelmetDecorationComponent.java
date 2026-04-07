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
import org.primal.Primal_Registries;
import org.primal.item.HelmetDecoration;
import org.primal.registry.Primal_HelmetDecorations;
import org.primal.registry.Primal_Items;

import java.util.function.Consumer;

public record HelmetDecorationComponent(HelmetDecoration<?> right, HelmetDecoration<?> left) implements TooltipProvider {

    public static final Codec<HelmetDecorationComponent> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    Codec.INT
                            .fieldOf("right")
                            .forGetter(c-> Primal_Registries.HELMET_DECORATIONS_REGISTRY.getId(c.right)),
                    Codec.INT
                            .fieldOf("left")
                            .forGetter(c-> Primal_Registries.HELMET_DECORATIONS_REGISTRY.getId(c.left))
            ).apply(instance, (r, l)-> {
                var rDec= Primal_Registries.HELMET_DECORATIONS_REGISTRY.getHolder(r);
                var lDec= Primal_Registries.HELMET_DECORATIONS_REGISTRY.getHolder(l);

                return new HelmetDecorationComponent(
                        rDec.isPresent()? rDec.get().value(): Primal_HelmetDecorations.NONE.get(),
                        lDec.isPresent()? lDec.get().value(): Primal_HelmetDecorations.NONE.get());
            }));

    @Override
    public void addToTooltip(Item.@NotNull TooltipContext context, @NotNull Consumer<Component> tooltipAdder, @NotNull TooltipFlag tooltipFlag) {

        tooltipAdder.accept(Component.translatable("helmet_decoration.decorations").withStyle(ChatFormatting.GRAY));

        if(!this.left.equals(Primal_HelmetDecorations.NONE.get())){
            var key = Primal_Registries.HELMET_DECORATIONS_REGISTRY.getKey(left);
            if(key != null)
                tooltipAdder.accept(CommonComponents.space().append( Component.translatable("helmet_decoration."+key.getNamespace()+"."+key.getPath()).withStyle(this.left.getDescriptionStyle())));
        }

        if(!this.right.equals(Primal_HelmetDecorations.NONE.get())){
            var key = Primal_Registries.HELMET_DECORATIONS_REGISTRY.getKey(right);
            if(key != null)
                tooltipAdder.accept(CommonComponents.space().append( Component.translatable("helmet_decoration."+key.getNamespace()+"."+key.getPath()).withStyle(this.right.getDescriptionStyle())));
        }
    }


    public static ItemStack of(final ItemStack helmet, ItemStack toAttach, boolean left){

        var decoration = HelmetDecoration.fromItem(toAttach.getItem());

        //Avoids
        if(decoration == Primal_HelmetDecorations.NONE.get())
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
                    new HelmetDecorationComponent(left? Primal_HelmetDecorations.NONE.get(): decoration, left? decoration: Primal_HelmetDecorations.NONE.get()));
        }

        return helmet;
    }

}
