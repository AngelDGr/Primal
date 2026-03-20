package org.primal.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.SnakeEntity;

import java.util.function.Consumer;

public record SnakeComponent(SnakeEntity.Variant variant) implements TooltipProvider {

    public static final Codec<SnakeComponent> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    Codec.INT
                            .fieldOf("variant")
                            .forGetter(c-> c.variant.getId())
            ).apply(instance, (variantId)-> new SnakeComponent(SnakeEntity.Variant.byId(variantId))));

    @Override
    public void addToTooltip(Item.@NotNull TooltipContext context, @NotNull Consumer<Component> tooltipAdder, @NotNull TooltipFlag tooltipFlag) {}
}
