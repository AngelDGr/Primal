package org.primal.item;

import net.minecraft.network.chat.Style;
import net.minecraft.util.ByIdMap;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.primal.registry.Primal_Items;

import java.util.function.IntFunction;

public enum HelmetDecorationType {
    FALLOW_DEER(0, "fallow_antler", Primal_Items.FALLOW_DEER_ANTLER.get(), Style.EMPTY.withColor(0xa5857d)),
    REINDEER(1, "reindeer_antler", Primal_Items.REINDEER_ANTLER.get(), Style.EMPTY.withColor(0xd2dbee)),
    WHITETAIL(2, "whitetail_antler", Primal_Items.WHITETAIL_DEER_ANTLER.get(), Style.EMPTY.withColor(0x986e47)),
    GOAT(3, "goat_horn", Items.GOAT_HORN, Style.EMPTY.withColor(0x575757)),


    NONE(-1, "none", ItemStack.EMPTY.getItem(), Style.EMPTY);

    public static final IntFunction<HelmetDecorationType> BY_ID = ByIdMap.sparse(HelmetDecorationType::getId, values(), NONE);

    private final int id;
    private final String name;
    private final Item asItem;
    private final Style descriptionStyle;

    HelmetDecorationType(int id, String name, Item asItem, Style descriptionStyle) {
        this.id = id;
        this.name = name;
        this.asItem = asItem;
        this.descriptionStyle = descriptionStyle;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Style getDescriptionStyle() {
        return descriptionStyle;
    }

    public Item getAsItem() {
        return asItem;
    }

    public static HelmetDecorationType fromItem(Item item) {
        for (HelmetDecorationType type : values()) {
            if (type.asItem == item) {
                return type;
            }
        }
        return NONE;
    }
}
