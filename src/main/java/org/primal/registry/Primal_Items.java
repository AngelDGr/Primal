package org.primal.registry;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.primal.Primal_Main;
import org.primal.Primal_Registries;
import org.primal.item.Primal_Food;

import java.util.function.Supplier;

public class Primal_Items {

    //Bear
    public static DeferredHolder<Item, Item> BEAR_SPAWN_EGG;

    //Shark
    public static DeferredHolder<Item, Item> SHARK_SPAWN_EGG;
    public static DeferredHolder<Item, Item> SHARK_TOOTH;

    //Crocodile
    public static DeferredHolder<Item, Item> CROCODILE_SPAWN_EGG;
    public static DeferredHolder<Item, Item> CROCODILE_SCUTE;
    public static DeferredHolder<Item, Item> CROCODILE_SCUTE_BLOCK;
    public static DeferredHolder<Item, Item> CROCODILE_SCUTE_SHINGLE;
    public static DeferredHolder<Item, Item> CHISELED_CROCODILE_SCUTE;
    public static DeferredHolder<Item, Item> CROCODILE_SCUTE_STAIRS;
    public static DeferredHolder<Item, Item> CROCODILE_SCUTE_SLAB;
    public static DeferredHolder<Item, Item> CROCODILE_EGG;

    //Eagle
    public static DeferredHolder<Item, Item> EAGLE_SPAWN_EGG;
    public static DeferredHolder<Item, Item> EAGLE_EGG;
    public static DeferredHolder<Item, Item> NEST;

    //Flora
    public static DeferredHolder<Item, Item> RIVER_REEDS;
    public static DeferredHolder<Item, Item> SHORT_RIVER_REEDS;
    public static DeferredHolder<Item, Item> SEASHELLS;

    //Food
    public static DeferredHolder<Item, Item> APPLE_FRITTER;
    public static DeferredHolder<Item, Item> GOLDEN_APPLE_FRITTER;
    public static DeferredHolder<Item, Item> ENCHANTED_GOLDEN_APPLE_FRITTER;

    //Misc
    public static DeferredHolder<Item, Item> PAW_BANNER_PATTERN;
    public static DeferredHolder<Item, Item> JAWS_BANNER_PATTERN;
    public static DeferredHolder<Item, Item> MARSH_BANNER_PATTERN;
    public static DeferredHolder<Item, Item> EYRIE_BANNER_PATTERN;

    public static void initItems(){
        //Spawn eggs
        {

            BEAR_SPAWN_EGG= register("bear_spawn_egg",
                    () -> new DeferredSpawnEggItem(Primal_Entities.BEAR, 0x924d36, 0xc19060, new Item.Properties()));

            SHARK_SPAWN_EGG= register("shark_spawn_egg",
                    () -> new DeferredSpawnEggItem(Primal_Entities.SHARK, 0x94b0c0, 0xe4e6f0, new Item.Properties()));

            CROCODILE_SPAWN_EGG= register("crocodile_spawn_egg",
                    () -> new DeferredSpawnEggItem(Primal_Entities.CROCODILE, 0xeceec9, 0x7a984e, new Item.Properties()));

            EAGLE_SPAWN_EGG= register("eagle_spawn_egg",
                    () -> new DeferredSpawnEggItem(Primal_Entities.EAGLE, 0xe0b887, 0x805146, new Item.Properties()));
        }


        //Mob-related Items
        {
            SHARK_TOOTH= register("shark_tooth",
                    ()-> new BlockItem(Primal_Blocks.SHARK_TOOTH.get(), new Item.Properties()));

            CROCODILE_SCUTE= register("crocodile_scute",
                    ()-> new Item(new Item.Properties()));
        }

        //Food
        {
            APPLE_FRITTER= register("apple_fritter",
                    ()-> new Item(new Item.Properties().food(Primal_Food.APPLE_FRITTER)));

            GOLDEN_APPLE_FRITTER= register("golden_apple_fritter",
                    ()-> new Item(new Item.Properties().rarity(Rarity.RARE).food(Primal_Food.GOLDEN_APPLE_FRITTER)));

            ENCHANTED_GOLDEN_APPLE_FRITTER= register("enchanted_golden_apple_fritter",
                    ()-> new Item(new Item.Properties().rarity(Rarity.EPIC).food(Primal_Food.ENCHANTED_GOLDEN_APPLE_FRITTER)
                            .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)));
        }

        //Animal Eggs
        {
            NEST=register("nest",
                    ()-> new BlockItem(Primal_Blocks.NEST_BLOCK.get(), new Item.Properties()));

            CROCODILE_EGG=register("crocodile_egg",
                    ()-> new BlockItem(Primal_Blocks.CROCODILE_EGG.get(), new Item.Properties()));

            EAGLE_EGG=register("eagle_egg",
                    ()-> new BlockItem(Primal_Blocks.EAGLE_EGG.get(), new Item.Properties()));
        }

        //Blocks
        {
            CROCODILE_SCUTE_BLOCK=register("crocodile_scute_block",
                    ()-> new BlockItem(Primal_Blocks.CROCODILE_SCUTE_BLOCK.get(), new Item.Properties()));

            CROCODILE_SCUTE_SHINGLE=register("crocodile_scute_shingle",
                    ()-> new BlockItem(Primal_Blocks.CROCODILE_SCUTE_SHINGLE.get(), new Item.Properties()));

            CHISELED_CROCODILE_SCUTE=register("chiseled_crocodile_scute",
                    ()-> new BlockItem(Primal_Blocks.CHISELED_CROCODILE_SCUTE.get(), new Item.Properties()));

            CROCODILE_SCUTE_STAIRS=register("crocodile_scute_stairs",
                    ()-> new BlockItem(Primal_Blocks.CROCODILE_SCUTE_STAIRS.get(), new Item.Properties()));

            CROCODILE_SCUTE_SLAB=register("crocodile_scute_slab",
                    ()-> new BlockItem(Primal_Blocks.CROCODILE_SCUTE_SLAB.get(), new Item.Properties()));
        }

        //Flora
        {
            RIVER_REEDS=register("river_reeds",
                    ()-> new BlockItem(Primal_Blocks.RIVER_REEDS.get(), new Item.Properties()));

            SHORT_RIVER_REEDS=register("short_river_reeds",
                    ()-> new BlockItem(Primal_Blocks.SHORT_RIVER_REEDS.get(), new Item.Properties()));

            SEASHELLS=register("seashells",
                    ()-> new BlockItem(Primal_Blocks.SEASHELLS.get(), new Item.Properties()));
        }

        //Misc
        {
            PAW_BANNER_PATTERN=register("paw_banner_pattern", ()-> new BannerPatternItem(Primal_Tags.PATTERN_ITEM_PAW, new Item.Properties()));

            JAWS_BANNER_PATTERN=register("jaws_banner_pattern", ()-> new BannerPatternItem(Primal_Tags.PATTERN_ITEM_JAWS, new Item.Properties()));

            MARSH_BANNER_PATTERN=register("marsh_banner_pattern", ()-> new BannerPatternItem(Primal_Tags.PATTERN_ITEM_MARSH, new Item.Properties()));

            EYRIE_BANNER_PATTERN=register("eyrie_banner_pattern", ()-> new BannerPatternItem(Primal_Tags.PATTERN_ITEM_EYRIE, new Item.Properties()));
        }
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
                                        output.accept(CROCODILE_SPAWN_EGG.get());
                                        output.accept(EAGLE_SPAWN_EGG.get());
                                    }

                                    {
                                        output.accept(APPLE_FRITTER.get());
                                        output.accept(GOLDEN_APPLE_FRITTER.get());
                                        output.accept(ENCHANTED_GOLDEN_APPLE_FRITTER.get());
                                        output.accept(SHARK_TOOTH.get());
                                        output.accept(NEST.get());
                                        output.accept(EAGLE_EGG.get());
                                        output.accept(CROCODILE_EGG.get());
                                        output.accept(CROCODILE_SCUTE.get());
                                        output.accept(CROCODILE_SCUTE_BLOCK.get());
                                        output.accept(CROCODILE_SCUTE_SHINGLE.get());
                                        output.accept(CHISELED_CROCODILE_SCUTE.get());
                                        output.accept(CROCODILE_SCUTE_STAIRS.get());
                                        output.accept(CROCODILE_SCUTE_SLAB.get());


                                        output.accept(SHORT_RIVER_REEDS.get());
                                        output.accept(RIVER_REEDS.get());
                                        output.accept(SEASHELLS.get());

                                        output.accept(PAW_BANNER_PATTERN.get());
                                        output.accept(JAWS_BANNER_PATTERN.get());
                                        output.accept(MARSH_BANNER_PATTERN.get());
                                        output.accept(EYRIE_BANNER_PATTERN.get());
                                    }

                                }
                        )
                        .build()
        );
    }

    public static DeferredHolder<Item, Item> register(final String name, final Supplier<Item> item) {
        return Primal_Registries.ITEMS.register(name, item);
    }
}
