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

    //Bear
    public static DeferredHolder<Item, Item> BEAR_SPAWN_EGG;

    //Shark
    public static DeferredHolder<Item, Item> SHARK_SPAWN_EGG;
    public static DeferredHolder<Item, Item> SHARK_TOOTH;

    public static void initItems(){

        BEAR_SPAWN_EGG=registerItem("bear_spawn_egg",
                () -> new DeferredSpawnEggItem(Primal_Entities.BEAR, 0x924d36, 0xc19060, new Item.Properties()));

        SHARK_SPAWN_EGG=registerItem("shark_spawn_egg",
                () -> new DeferredSpawnEggItem(Primal_Entities.SHARK, 0x94b0c0, 0xe4e6f0, new Item.Properties()));

        SHARK_TOOTH=registerItem("shark_tooth",
                ()-> new Item(new Item.Properties()));
    }

    public static void initGroups(){
        Primal_Registries.CREATIVE_MODE_TABS.register(
                "item_group." + Primal_Main.MOD_ID,
                () -> CreativeModeTab.builder()
                        .title(Component.translatable("itemGroup." + Primal_Main.MOD_ID))
                        .icon(() -> new ItemStack(Primal_Items.SHARK_TOOTH.get()))
                        .displayItems((itemDisplayParameters, output) ->
                                {

                                    //Spawn Eggs
                                    {
                                        output.accept(BEAR_SPAWN_EGG.get());
                                        output.accept(SHARK_SPAWN_EGG.get());
                                    }

                                    {
                                        output.accept(SHARK_TOOTH.get());
                                    }

                                }
                        )
                        .build()
        );
    }

    public static DeferredHolder<Item, Item> registerItem(final String name, final Supplier<Item> item) {
        return Primal_Registries.ITEMS.register(name, item);
    }
}
