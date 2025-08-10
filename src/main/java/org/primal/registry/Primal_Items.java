package org.primal.registry;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.primal.Primal_Main;
import org.primal.Primal_Registries;

import java.util.function.Supplier;

public class Primal_Items {

    public static DeferredHolder<Item, Item> BEAR_SPAWN_EGG;

    public static void initItems(){

        Primal_Items.BEAR_SPAWN_EGG=registerItem("bear_spawn_egg",
                () -> new DeferredSpawnEggItem(Primal_Entities.BEAR, 0x924d36, 0xc19060, new Item.Properties()));

    }

    public static void initGroups(){
        Primal_Registries.CREATIVE_MODE_TABS.register(
                "item_group." + Primal_Main.MOD_ID,
                () -> CreativeModeTab.builder()
                        .title(Component.translatable("itemGroup." + Primal_Main.MOD_ID))
                        .icon(() -> new ItemStack(Primal_Items.BEAR_SPAWN_EGG.get()))
                        .displayItems((itemDisplayParameters, output) ->

                                //Spawn Eggs
                                {
                                    output.accept(BEAR_SPAWN_EGG.get());
                                }
                        )
                        .build()
        );
    }

    public static DeferredHolder<Item, Item> registerItem(final String name, final Supplier<Item> item) {
        return Primal_Registries.ITEMS.register(name, item);
    }
}
