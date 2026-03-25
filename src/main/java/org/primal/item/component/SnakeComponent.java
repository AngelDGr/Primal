package org.primal.item.component;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.SnakeEntity;
import org.primal.util.Primal_Util;

import java.util.function.Consumer;

public record SnakeComponent(SnakeEntity.Variant variant) implements Primal_Util.OneTwentyEquivalent.Components.BaseComponent<SnakeComponent> {

    @Override
    public CompoundTag addComponent(CompoundTag compoundTag) {
        CompoundTag nbt = new CompoundTag();
        if(variant!=null) {
            nbt.putInt("variant", variant.getId());
        }
        compoundTag.put(this.getComponentName(), nbt);

        return compoundTag;
    }

    @Override
    public SnakeComponent create(CompoundTag compoundTag) {
        if(compoundTag.contains("variant")) {
            var variantId = compoundTag.getInt("variant");
            return new SnakeComponent(SnakeEntity.Variant.byId(variantId));
        }

        return new SnakeComponent(null);
    }

    @Override
    public String getComponentName() {
        return "SnakeComponent";
    }

    public void addToTooltip(Level context, @NotNull Consumer<Component> tooltipAdder, @NotNull TooltipFlag tooltipFlag) {}
}
