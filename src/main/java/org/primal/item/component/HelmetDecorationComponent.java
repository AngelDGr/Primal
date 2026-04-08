package org.primal.item.component;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Registries;
import org.primal.item.HelmetDecoration;
import org.primal.registry.Primal_HelmetDecorations;
import org.primal.registry.Primal_Items;
import org.primal.util.Primal_Util;

import java.util.List;
import java.util.Objects;

public record HelmetDecorationComponent(HelmetDecoration<?> right, HelmetDecoration<?>  left) implements Primal_Util.OneTwentyEquivalent.Components.BaseComponent<HelmetDecorationComponent> {

    @Override
    public CompoundTag addComponent(CompoundTag compoundTag) {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("right", Objects.requireNonNull(Primal_Registries.HELMET_DECORATIONS_REGISTRY.get().getKey(right)).toString());
        nbt.putString("left", Objects.requireNonNull(Primal_Registries.HELMET_DECORATIONS_REGISTRY.get().getKey(left)).toString());
        compoundTag.put(this.getComponentName(), nbt);

        return compoundTag;
    }

    @Override
    public HelmetDecorationComponent create(CompoundTag compoundTag) {
        var r = compoundTag.getString("right");
        var l = compoundTag.getString("left");
        var rDec= Primal_Registries.HELMET_DECORATIONS_REGISTRY.get().getValue(ResourceLocation.parse(r));
        var lDec= Primal_Registries.HELMET_DECORATIONS_REGISTRY.get().getValue(ResourceLocation.parse(l));
        return new HelmetDecorationComponent(
                rDec!=null? rDec: Primal_HelmetDecorations.NONE.get(),
                lDec!=null? lDec: Primal_HelmetDecorations.NONE.get());
    }

    @Override
    public String getComponentName() {
        return "HelmetDecorations";
    }

    @Override
    public void addToTooltip(Level level, @NotNull List<Component> tooltipAdder, @NotNull TooltipFlag tooltipFlag) {
        tooltipAdder.add(Component.translatable("helmet_decoration.decorations").withStyle(ChatFormatting.GRAY));

        if(!this.left.equals(Primal_HelmetDecorations.NONE.get())){
            var key = Primal_Registries.HELMET_DECORATIONS_REGISTRY.get().getKey(left);
            if(key != null)
                tooltipAdder.add(CommonComponents.space().append( Component.translatable("helmet_decoration."+key.getNamespace()+"."+key.getPath()).withStyle(this.left.getDescriptionStyle())));
        }

        if(!this.right.equals(Primal_HelmetDecorations.NONE.get())){
            var key = Primal_Registries.HELMET_DECORATIONS_REGISTRY.get().getKey(right);
            if(key != null)
                tooltipAdder.add(CommonComponents.space().append( Component.translatable("helmet_decoration."+key.getNamespace()+"."+key.getPath()).withStyle(this.right.getDescriptionStyle())));
        }
    }


    public static ItemStack of(final ItemStack helmet, ItemStack toAttach, boolean left){

        var decoration = HelmetDecoration.fromItem(toAttach.getItem());

        //Avoids
        if(decoration == Primal_HelmetDecorations.NONE.get())
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
                    new HelmetDecorationComponent(left? Primal_HelmetDecorations.NONE.get(): decoration, left? decoration: Primal_HelmetDecorations.NONE.get()));
        }

        return helmet;
    }

}
