package org.primal.registry;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.primal.Primal_Main;
import org.primal.Primal_Registries;
import org.primal.block_entity.HollowLogBlockEntity;
import org.primal.item.ConchShellItem;
import org.primal.item.ExploseedItem;
import org.primal.item.SnakeItem;
import org.primal.item.component.ConchShellComponent;
import org.primal.item.component.HelmetDecorationComponent;
import org.primal.item.component.SnakeComponent;

import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class Primal_Items {

    public static DeferredHolder<Item, Item> PRIMAL;

    //Bear
    public static DeferredHolder<Item, Item> BEAR_SPAWN_EGG;

    //Shark
    public static DeferredHolder<Item, Item> SHARK_SPAWN_EGG;
    public static DeferredHolder<Item, Item> SHARK_TOOTH;

    //Crocodile
    public static DeferredHolder<Item, Item> CROCODILE_SPAWN_EGG;
    public static DeferredHolder<Item, Item> CROCODILE_EGG;
    public static DeferredHolder<Item, Item> CROCODILE_SCUTE;
    public static DeferredHolder<Item, Item> CROCODILE_SCUTE_BLOCK;
    public static DeferredHolder<Item, Item> CROCODILE_SCUTE_SHINGLE;
    public static DeferredHolder<Item, Item> CHISELED_CROCODILE_SCUTE;
    public static DeferredHolder<Item, Item> CROCODILE_SCUTE_STAIRS;
    public static DeferredHolder<Item, Item> CROCODILE_SCUTE_SLAB;

    public static DeferredHolder<Item, Item> ARID_CROCODILE_SCUTE_BLOCK;
    public static DeferredHolder<Item, Item> ARID_CROCODILE_SCUTE_SHINGLE;
    public static DeferredHolder<Item, Item> ARID_CHISELED_CROCODILE_SCUTE;
    public static DeferredHolder<Item, Item> ARID_CROCODILE_SCUTE_STAIRS;
    public static DeferredHolder<Item, Item> ARID_CROCODILE_SCUTE_SLAB;

    public static DeferredHolder<Item, Item> HUMID_CROCODILE_SCUTE_BLOCK;
    public static DeferredHolder<Item, Item> HUMID_CROCODILE_SCUTE_SHINGLE;
    public static DeferredHolder<Item, Item> HUMID_CHISELED_CROCODILE_SCUTE;
    public static DeferredHolder<Item, Item> HUMID_CROCODILE_SCUTE_STAIRS;
    public static DeferredHolder<Item, Item> HUMID_CROCODILE_SCUTE_SLAB;

    public static DeferredHolder<Item, Item> CHOMP_TRAP_GREEN;
    public static DeferredHolder<Item, Item> CHOMP_TRAP_ARID;
    public static DeferredHolder<Item, Item> CHOMP_TRAP_HUMID;

    public static DeferredHolder<Item, Item> NEST;

    public static DeferredHolder<Item, Item> DREAMCATCHER;

    //Eagle
    public static DeferredHolder<Item, Item> EAGLE_SPAWN_EGG;
    public static DeferredHolder<Item, Item> EAGLE_EGG;

    //Cassowary
    public static DeferredHolder<Item, Item> CASSOWARY_SPAWN_EGG;
    public static DeferredHolder<Item, Item> CASSOWARY_EGG;
    public static DeferredHolder<Item, Item> PETRIFIED_FRUIT;

    //Walrus
    public static DeferredHolder<Item, Item> WALRUS_SPAWN_EGG;
    public static DeferredHolder<Item, Item> WARM_CONCH_SHELL;
    public static DeferredHolder<Item, Item> TEMPERATE_CONCH_SHELL;
    public static DeferredHolder<Item, Item> COLD_CONCH_SHELL;

    //Lion
    public static DeferredHolder<Item, Item> LION_SPAWN_EGG;

    //Snake
    public static DeferredHolder<Item, Item> SNAKE_SPAWN_EGG;
    public static DeferredHolder<Item, Item> PLACEHOLDER_CHESTED_SNAKE;

    //Deer
    public static DeferredHolder<Item, Item> DEER_SPAWN_EGG;
    public static DeferredHolder<Item, Item> VENISON;
    public static DeferredHolder<Item, Item> COOKED_VENISON;
    public static DeferredHolder<Item, Item> FALLOW_DEER_ANTLER;
    public static DeferredHolder<Item, Item> REINDEER_ANTLER;
    public static DeferredHolder<Item, Item> WHITETAIL_DEER_ANTLER;

    public static DeferredHolder<Item, Item> LITCHI;
    public static DeferredHolder<Item, Item> KIWANO;
    public static DeferredHolder<Item, Item> STARFRUIT;
    public static DeferredHolder<Item, Item> WEIRD_APPLE;

    public static DeferredHolder<Item, Item> LITCHI_SEEDS;
    public static DeferredHolder<Item, Item> KIWANO_SEEDS;
    public static DeferredHolder<Item, Item> STARFRUIT_SEEDS;

    //Flora
    public static DeferredHolder<Item, Item> RIVER_REEDS;
    public static DeferredHolder<Item, Item> SHORT_RIVER_REEDS;
    public static DeferredHolder<Item, Item> CATTAILS;
    public static DeferredHolder<Item, Item> WARM_SEASHELLS;
    public static DeferredHolder<Item, Item> COLD_SEASHELLS;
    public static DeferredHolder<Item, Item> TEMPERATE_SEASHELLS;

    public static DeferredHolder<Item, Item> THORNY_ACACIA_LOG;
    public static DeferredHolder<Item, Item> THORNY_ACACIA_WOOD;
    public static DeferredHolder<Item, Item> THORNY_ACACIA_LEAVES;
    public static DeferredHolder<Item, Item> THORNY_ACACIA_SAPLING;
    public static DeferredHolder<Item, Item> EXPLOSEED;

    public static DeferredHolder<Item, Item> HOLLOW_OAK_LOG;
    public static DeferredHolder<Item, Item> HOLLOW_SPRUCE_LOG;
    public static DeferredHolder<Item, Item> HOLLOW_BIRCH_LOG;
    public static DeferredHolder<Item, Item> HOLLOW_JUNGLE_LOG;
    public static DeferredHolder<Item, Item> HOLLOW_ACACIA_LOG;
    public static DeferredHolder<Item, Item> HOLLOW_DARK_OAK_LOG;
    public static DeferredHolder<Item, Item> HOLLOW_MANGROVE_LOG;

    //Food
    public static DeferredHolder<Item, Item> APPLE_FRITTER;
    public static DeferredHolder<Item, Item> GOLDEN_APPLE_FRITTER;
    public static DeferredHolder<Item, Item> ENCHANTED_GOLDEN_APPLE_FRITTER;

    //Misc
    public static DeferredHolder<Item, Item> MUSIC_DISC_OH_DEER;
    public static DeferredHolder<Item, Item> PAW_BANNER_PATTERN;
    public static DeferredHolder<Item, Item> JAWS_BANNER_PATTERN;
    public static DeferredHolder<Item, Item> MARSH_BANNER_PATTERN;
    public static DeferredHolder<Item, Item> EYRIE_BANNER_PATTERN;
    public static DeferredHolder<Item, Item> SLITHER_BANNER_PATTERN;
    public static DeferredHolder<Item, Item> ROYAL_BANNER_PATTERN;

    public static DeferredHolder<Item, Item> STRAW_BLOCK;
    public static DeferredHolder<Item, Item> DRIED_STRAW_BLOCK;
    public static DeferredHolder<Item, Item> WEAVED_STRAW;
    public static DeferredHolder<Item, Item> WEAVED_STRAW_STAIRS;
    public static DeferredHolder<Item, Item> WEAVED_STRAW_SLAB;
    public static DeferredHolder<Item, Item> STRAW_BASKET;

    public static void initItems(){
        PRIMAL = register("primal",
                () -> new Item(new Item.Properties().stacksTo(1)));

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

            CASSOWARY_SPAWN_EGG= register("cassowary_spawn_egg",
                    () -> new DeferredSpawnEggItem(Primal_Entities.CASSOWARY, 0x6eb924, 0x2b721f, new Item.Properties()));

            WALRUS_SPAWN_EGG= register("walrus_spawn_egg",
                    () -> new DeferredSpawnEggItem(Primal_Entities.WALRUS, 0xa46a47, 0xecb881, new Item.Properties()));

            LION_SPAWN_EGG= register("lion_spawn_egg",
                    () -> new DeferredSpawnEggItem(Primal_Entities.LION, 0xffbe75, 0x4a2f24, new Item.Properties()));

            SNAKE_SPAWN_EGG= register("snake_spawn_egg",
                    () -> new DeferredSpawnEggItem(Primal_Entities.SNAKE, 0x3ac1ee, 0x8ff0ea, new Item.Properties()));

            DEER_SPAWN_EGG= register("deer_spawn_egg",
                    () -> new DeferredSpawnEggItem(Primal_Entities.DEER, 0xee8b54, 0xf7e9e3, new Item.Properties()));

            PLACEHOLDER_CHESTED_SNAKE = register("placeholder_chested_snake",
                    () -> new SnakeItem(new Item.Properties().stacksTo(1)));
        }


        //Mob-related Items
        {
            SHARK_TOOTH= register("shark_tooth",
                    ()-> new BlockItem(Primal_Blocks.SHARK_TOOTH.get(), new Item.Properties()));

            CROCODILE_SCUTE= register("crocodile_scute",
                    ()-> new Item(new Item.Properties()));

            WARM_CONCH_SHELL = register("warm_conch_shell", ()-> new ConchShellItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
            TEMPERATE_CONCH_SHELL = register("temperate_conch_shell", ()-> new ConchShellItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
            COLD_CONCH_SHELL = register("cold_conch_shell", ()-> new ConchShellItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));

            FALLOW_DEER_ANTLER = register("fallow_deer_antler",
                    () -> new BlockItem(Primal_Blocks.FALLOW_DEER_ANTLER.get(), new Item.Properties()));

            REINDEER_ANTLER = register("reindeer_antler",
                    () -> new BlockItem(Primal_Blocks.REINDEER_ANTLER.get(), new Item.Properties()));

            WHITETAIL_DEER_ANTLER = register("whitetail_deer_antler",
                    () -> new BlockItem(Primal_Blocks.WHITETAIL_DEER_ANTLER.get(), new Item.Properties()));

            MUSIC_DISC_OH_DEER= register("music_disc_oh_deer",
                    ()-> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(Primal_JukeboxSongs.OH_DEER)));
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

            VENISON = register("venison",
                    () -> new Item(new Item.Properties().food(Primal_Food.VENISON)));

            COOKED_VENISON = register("cooked_venison",
                    () -> new Item(new Item.Properties().food(Primal_Food.COOKED_VENISON)));
        }

        //Animal Eggs
        {
            NEST=register("nest",
                    ()-> new BlockItem(Primal_Blocks.NEST_BLOCK.get(), new Item.Properties()));

            CROCODILE_EGG=register("crocodile_egg",
                    ()-> new BlockItem(Primal_Blocks.CROCODILE_EGG.get(), new Item.Properties()));

            EAGLE_EGG=register("eagle_egg",
                    ()-> new BlockItem(Primal_Blocks.EAGLE_EGG.get(), new Item.Properties()));

            CASSOWARY_EGG=register("cassowary_egg",
                    ()-> new BlockItem(Primal_Blocks.CASSOWARY_EGG.get(), new Item.Properties()));
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

            CHOMP_TRAP_GREEN=register("chomp_trap_green",
                    ()-> new BlockItem(Primal_Blocks.CHOMP_TRAP_GREEN.get(), new Item.Properties()));


            ARID_CROCODILE_SCUTE_BLOCK=register("arid_crocodile_scute_block",
                    ()-> new BlockItem(Primal_Blocks.ARID_CROCODILE_SCUTE_BLOCK.get(), new Item.Properties()));

            ARID_CROCODILE_SCUTE_SHINGLE=register("arid_crocodile_scute_shingle",
                    ()-> new BlockItem(Primal_Blocks.ARID_CROCODILE_SCUTE_SHINGLE.get(), new Item.Properties()));

            ARID_CHISELED_CROCODILE_SCUTE=register("arid_chiseled_crocodile_scute",
                    ()-> new BlockItem(Primal_Blocks.ARID_CHISELED_CROCODILE_SCUTE.get(), new Item.Properties()));

            ARID_CROCODILE_SCUTE_STAIRS=register("arid_crocodile_scute_stairs",
                    ()-> new BlockItem(Primal_Blocks.ARID_CROCODILE_SCUTE_STAIRS.get(), new Item.Properties()));

            ARID_CROCODILE_SCUTE_SLAB=register("arid_crocodile_scute_slab",
                    ()-> new BlockItem(Primal_Blocks.ARID_CROCODILE_SCUTE_SLAB.get(), new Item.Properties()));

            CHOMP_TRAP_ARID=register("chomp_trap_arid",
                    ()-> new BlockItem(Primal_Blocks.CHOMP_TRAP_ARID.get(), new Item.Properties()));


            HUMID_CROCODILE_SCUTE_BLOCK=register("humid_crocodile_scute_block",
                    ()-> new BlockItem(Primal_Blocks.HUMID_CROCODILE_SCUTE_BLOCK.get(), new Item.Properties()));

            HUMID_CROCODILE_SCUTE_SHINGLE=register("humid_crocodile_scute_shingle",
                    ()-> new BlockItem(Primal_Blocks.HUMID_CROCODILE_SCUTE_SHINGLE.get(), new Item.Properties()));

            HUMID_CHISELED_CROCODILE_SCUTE=register("humid_chiseled_crocodile_scute",
                    ()-> new BlockItem(Primal_Blocks.HUMID_CHISELED_CROCODILE_SCUTE.get(), new Item.Properties()));

            HUMID_CROCODILE_SCUTE_STAIRS=register("humid_crocodile_scute_stairs",
                    ()-> new BlockItem(Primal_Blocks.HUMID_CROCODILE_SCUTE_STAIRS.get(), new Item.Properties()));

            HUMID_CROCODILE_SCUTE_SLAB=register("humid_crocodile_scute_slab",
                    ()-> new BlockItem(Primal_Blocks.HUMID_CROCODILE_SCUTE_SLAB.get(), new Item.Properties()));

            CHOMP_TRAP_HUMID=register("chomp_trap_humid",
                    ()-> new BlockItem(Primal_Blocks.CHOMP_TRAP_HUMID.get(), new Item.Properties()));

            DREAMCATCHER=register("dreamcatcher",
                    ()-> new BlockItem(Primal_Blocks.DREAMCATCHER.get(), new Item.Properties()));
        }

        //Flora
        {
            RIVER_REEDS=register("river_reeds",
                    ()-> new BlockItem(Primal_Blocks.RIVER_REEDS.get(), new Item.Properties()));

            SHORT_RIVER_REEDS=register("short_river_reeds",
                    ()-> new BlockItem(Primal_Blocks.SHORT_RIVER_REEDS.get(), new Item.Properties()));

            CATTAILS=register("cattails",
                    ()-> new BlockItem(Primal_Blocks.CATTAILS.get(), new Item.Properties()));

            WARM_SEASHELLS =register("seashells",
                    ()-> new BlockItem(Primal_Blocks.WARM_SEASHELLS.get(), new Item.Properties()));

            COLD_SEASHELLS=register("cold_seashells",
                    ()-> new BlockItem(Primal_Blocks.COLD_SEASHELLS.get(), new Item.Properties()));

            TEMPERATE_SEASHELLS=register("temperate_seashells",
                    ()-> new BlockItem(Primal_Blocks.TEMPERATE_SEASHELLS.get(), new Item.Properties()));

            //Thorny Acacia
            {

                THORNY_ACACIA_LOG=register("thorny_acacia_log",
                        ()-> new BlockItem(Primal_Blocks.THORNY_ACACIA_LOG.get(), new Item.Properties()));

                THORNY_ACACIA_WOOD=register("thorny_acacia_wood",
                        ()-> new BlockItem(Primal_Blocks.THORNY_ACACIA_WOOD.get(), new Item.Properties()));

                THORNY_ACACIA_LEAVES=register("thorny_acacia_leaves",
                        ()-> new BlockItem(Primal_Blocks.THORNY_ACACIA_LEAVES.get(), new Item.Properties()));

                THORNY_ACACIA_SAPLING=register("thorny_acacia_sapling",
                        ()-> new BlockItem(Primal_Blocks.THORNY_ACACIA_SAPLING.get(), new Item.Properties()));

                EXPLOSEED=register("exploseed",
                        ()-> new ExploseedItem(new Item.Properties()));
            }

            //Hollow Logs
            {
                HOLLOW_OAK_LOG = register("hollow_oak_log",
                        ()-> new BlockItem(Primal_Blocks.HOLLOW_OAK_LOG.get(),
                                new Item.Properties().component(Components.ANIMALS_INSIDE.get(), List.of())));

                HOLLOW_SPRUCE_LOG = register("hollow_spruce_log",
                        ()-> new BlockItem(Primal_Blocks.HOLLOW_SPRUCE_LOG.get(),
                                new Item.Properties().component(Components.ANIMALS_INSIDE.get(), List.of())));

                HOLLOW_BIRCH_LOG = register("hollow_birch_log",
                        ()-> new BlockItem(Primal_Blocks.HOLLOW_BIRCH_LOG.get(),
                                new Item.Properties().component(Components.ANIMALS_INSIDE.get(), List.of())));

                HOLLOW_JUNGLE_LOG = register("hollow_jungle_log",
                        ()-> new BlockItem(Primal_Blocks.HOLLOW_JUNGLE_LOG.get(),
                                new Item.Properties().component(Components.ANIMALS_INSIDE.get(), List.of())));

                HOLLOW_ACACIA_LOG = register("hollow_acacia_log",
                        ()-> new BlockItem(Primal_Blocks.HOLLOW_ACACIA_LOG.get(),
                                new Item.Properties().component(Components.ANIMALS_INSIDE.get(), List.of())));

                HOLLOW_DARK_OAK_LOG = register("hollow_dark_oak_log",
                        ()-> new BlockItem(Primal_Blocks.HOLLOW_DARK_OAK_LOG.get(),
                                new Item.Properties().component(Components.ANIMALS_INSIDE.get(), List.of())));

                HOLLOW_MANGROVE_LOG = register("hollow_mangrove_log",
                        ()-> new BlockItem(Primal_Blocks.HOLLOW_MANGROVE_LOG.get(),
                                new Item.Properties().component(Components.ANIMALS_INSIDE.get(), List.of())));
            }
        }

        //Misc
        {
            PAW_BANNER_PATTERN=register("paw_banner_pattern", ()-> new BannerPatternItem(Primal_Tags.BannerPattern.PATTERN_ITEM_PAW, new Item.Properties().stacksTo(1)));

            JAWS_BANNER_PATTERN=register("jaws_banner_pattern", ()-> new BannerPatternItem(Primal_Tags.BannerPattern.PATTERN_ITEM_JAWS, new Item.Properties().stacksTo(1)));

            MARSH_BANNER_PATTERN=register("marsh_banner_pattern", ()-> new BannerPatternItem(Primal_Tags.BannerPattern.PATTERN_ITEM_MARSH, new Item.Properties().stacksTo(1)));

            EYRIE_BANNER_PATTERN=register("eyrie_banner_pattern", ()-> new BannerPatternItem(Primal_Tags.BannerPattern.PATTERN_ITEM_EYRIE, new Item.Properties().stacksTo(1)));

            SLITHER_BANNER_PATTERN=register("slither_banner_pattern", ()-> new BannerPatternItem(Primal_Tags.BannerPattern.PATTERN_ITEM_SLITHER, new Item.Properties().stacksTo(1)));

            ROYAL_BANNER_PATTERN=register("royal_banner_pattern", ()-> new BannerPatternItem(Primal_Tags.BannerPattern.PATTERN_ITEM_ROYAL, new Item.Properties().stacksTo(1)));

            PETRIFIED_FRUIT=register("petrified_fruit", ()-> new BlockItem(Primal_Blocks.PETRIFIED_FRUIT.get(), new Item.Properties()));

            LITCHI=register("litchi", ()-> new Item(new Item.Properties().food(Primal_Food.LITCHI)));
            LITCHI_SEEDS=register("litchi_seeds", ()-> new ItemNameBlockItem(Primal_Blocks.LITCHI_SAPLING.get(), new Item.Properties()));

            KIWANO=register("kiwano", ()-> new Item(new Item.Properties().food(Primal_Food.KIWANO)));
            KIWANO_SEEDS=register("kiwano_seeds", ()-> new ItemNameBlockItem(Primal_Blocks.KIWANO_SAPLING.get(), new Item.Properties()));

            STARFRUIT=register("starfruit", ()-> new Item(new Item.Properties().food(Primal_Food.STARFRUIT)));
            STARFRUIT_SEEDS=register("starfruit_seeds", ()-> new ItemNameBlockItem(Primal_Blocks.STARFRUIT_SAPLING.get(), new Item.Properties()));

            WEIRD_APPLE=register("weird_apple", ()-> new Item(new Item.Properties().food(Primal_Food.WEIRD_APPLE)));

            STRAW_BLOCK=register("straw_bale", ()-> new BlockItem(Primal_Blocks.STRAW_BALE.get(), new Item.Properties()));

            DRIED_STRAW_BLOCK=register("dried_straw_bale", ()-> new BlockItem(Primal_Blocks.DRIED_STRAW_BALE.get(), new Item.Properties()));

            WEAVED_STRAW=register("weaved_straw", ()-> new BlockItem(Primal_Blocks.WEAVED_STRAW.get(), new Item.Properties()));

            WEAVED_STRAW_STAIRS=register("weaved_straw_stairs", ()-> new BlockItem(Primal_Blocks.WEAVED_STRAW_STAIRS.get(), new Item.Properties()));

            WEAVED_STRAW_SLAB=register("weaved_straw_slab", ()-> new BlockItem(Primal_Blocks.WEAVED_STRAW_SLAB.get(), new Item.Properties()));

            STRAW_BASKET=register("straw_basket", ()-> new BlockItem(Primal_Blocks.STRAW_BASKET.get(), new Item.Properties()));
        }
    }

    public static void initGroups(){
        Primal_Registries.CREATIVE_MODE_TABS.register(
                "item_group." + Primal_Main.MOD_ID,
                () -> CreativeModeTab.builder()
                        .title(Component.translatable("itemGroup." + Primal_Main.MOD_ID))
                        .icon(() -> new ItemStack(Primal_Items.PRIMAL.get()))
                        .displayItems((params, output) -> {

                            // ───────── Spawn Eggs ─────────
                            output.accept(BEAR_SPAWN_EGG.get());
                            output.accept(CROCODILE_SPAWN_EGG.get());
                            output.accept(EAGLE_SPAWN_EGG.get());
                            output.accept(SHARK_SPAWN_EGG.get());
                            output.accept(CASSOWARY_SPAWN_EGG.get());
                            output.accept(WALRUS_SPAWN_EGG.get());
                            output.accept(LION_SPAWN_EGG.get());
                            output.accept(SNAKE_SPAWN_EGG.get());
                            output.accept(DEER_SPAWN_EGG.get());

                            // ───────── Animal Eggs ─────────
                            output.accept(NEST.get());
                            output.accept(EAGLE_EGG.get());
                            output.accept(CROCODILE_EGG.get());
                            output.accept(CASSOWARY_EGG.get());

                            // ───────── Straw Things ─────────
                            output.accept(SHORT_RIVER_REEDS.get());
                            output.accept(RIVER_REEDS.get());
                            output.accept(CATTAILS.get());

                            output.accept(STRAW_BLOCK.get());
                            output.accept(DRIED_STRAW_BLOCK.get());
                            output.accept(WEAVED_STRAW.get());
                            output.accept(WEAVED_STRAW_STAIRS.get());
                            output.accept(WEAVED_STRAW_SLAB.get());
                            output.accept(STRAW_BASKET.get());

                            // ───────── Hollow Logs ─────────
                            output.accept(HOLLOW_OAK_LOG.get());
                            output.accept(HOLLOW_SPRUCE_LOG.get());
                            output.accept(HOLLOW_BIRCH_LOG.get());
                            output.accept(HOLLOW_JUNGLE_LOG.get());
                            output.accept(HOLLOW_ACACIA_LOG.get());
                            output.accept(HOLLOW_DARK_OAK_LOG.get());
                            output.accept(HOLLOW_MANGROVE_LOG.get());

                            // ───────── Thorny Acacia ─────────
                            output.accept(THORNY_ACACIA_LOG.get());
                            output.accept(THORNY_ACACIA_WOOD.get());
                            output.accept(THORNY_ACACIA_LEAVES.get());
                            output.accept(THORNY_ACACIA_SAPLING.get());
                            output.accept(EXPLOSEED.get());

                            // ───────── Seashells ─────────
                            output.accept(WARM_SEASHELLS.get());
                            output.accept(COLD_SEASHELLS.get());
                            output.accept(TEMPERATE_SEASHELLS.get());

                            output.accept(WARM_CONCH_SHELL.get());
                            output.accept(TEMPERATE_CONCH_SHELL.get());
                            output.accept(COLD_CONCH_SHELL.get());

                            // ───────── Food ─────────
                            output.accept(APPLE_FRITTER.get());
                            output.accept(GOLDEN_APPLE_FRITTER.get());
                            output.accept(ENCHANTED_GOLDEN_APPLE_FRITTER.get());

                            // ───────── Fruit Trees ─────────
                            output.accept(PETRIFIED_FRUIT.get());
                            output.accept(LITCHI_SEEDS.get());
                            output.accept(LITCHI.get());
                            output.accept(KIWANO_SEEDS.get());
                            output.accept(KIWANO.get());
                            output.accept(STARFRUIT_SEEDS.get());
                            output.accept(STARFRUIT.get());

                            // ───────── Ingredients ─────────
                            output.accept(VENISON.get());
                            output.accept(COOKED_VENISON.get());
                            output.accept(FALLOW_DEER_ANTLER.get());
                            output.accept(REINDEER_ANTLER.get());
                            output.accept(WHITETAIL_DEER_ANTLER.get());
                            output.accept(DREAMCATCHER.get());
                            output.accept(SHARK_TOOTH.get());
                            output.accept(CROCODILE_SCUTE.get());

                            // ───────── Crocodile Scute Blocks (Default) ─────────
                            output.accept(CROCODILE_SCUTE_BLOCK.get());
                            output.accept(CROCODILE_SCUTE_STAIRS.get());
                            output.accept(CROCODILE_SCUTE_SLAB.get());
                            output.accept(CHISELED_CROCODILE_SCUTE.get());
                            output.accept(CROCODILE_SCUTE_SHINGLE.get());
                            output.accept(CHOMP_TRAP_GREEN.get());

                            // ───────── Crocodile Scute Blocks (Arid) ─────────
                            output.accept(ARID_CROCODILE_SCUTE_BLOCK.get());
                            output.accept(ARID_CROCODILE_SCUTE_STAIRS.get());
                            output.accept(ARID_CROCODILE_SCUTE_SLAB.get());
                            output.accept(ARID_CHISELED_CROCODILE_SCUTE.get());
                            output.accept(ARID_CROCODILE_SCUTE_SHINGLE.get());
                            output.accept(CHOMP_TRAP_ARID.get());

                            // ───────── Crocodile Scute Blocks (Humid) ─────────
                            output.accept(HUMID_CROCODILE_SCUTE_BLOCK.get());
                            output.accept(HUMID_CROCODILE_SCUTE_STAIRS.get());
                            output.accept(HUMID_CROCODILE_SCUTE_SLAB.get());
                            output.accept(HUMID_CHISELED_CROCODILE_SCUTE.get());
                            output.accept(HUMID_CROCODILE_SCUTE_SHINGLE.get());
                            output.accept(CHOMP_TRAP_HUMID.get());

                            // ───────── Music Discs ─────────
                            output.accept(MUSIC_DISC_OH_DEER.get());

                            // ───────── Banner Patterns ─────────
                            output.accept(PAW_BANNER_PATTERN.get());
                            output.accept(JAWS_BANNER_PATTERN.get());
                            output.accept(MARSH_BANNER_PATTERN.get());
                            output.accept(EYRIE_BANNER_PATTERN.get());
                            output.accept(SLITHER_BANNER_PATTERN.get());
                            output.accept(ROYAL_BANNER_PATTERN.get());
                        })
                        .build()
        );
    }

    public static DeferredHolder<Item, Item> register(final String name, final Supplier<Item> item) {
        return Primal_Registries.ITEMS.register(name, item);
    }

    public static class Components {
        public static Supplier<DataComponentType<ConchShellComponent>> CONCH_SHELL;

        public static Supplier<DataComponentType<SnakeComponent>> SNAKE_SPAWN;

        public static Supplier<DataComponentType<List<HollowLogBlockEntity.Occupant>>> ANIMALS_INSIDE;

        public static Supplier<DataComponentType<HelmetDecorationComponent>> HELMET_DECORATION;

        public static void init(){
            CONCH_SHELL = register("conch_shell", builder -> builder.persistent(ConchShellComponent.CODEC));

            SNAKE_SPAWN = register("snake_spawn", builder -> builder.persistent(SnakeComponent.CODEC));

            ANIMALS_INSIDE = register("animals_inside", builder -> builder.persistent(HollowLogBlockEntity.Occupant.LIST_CODEC));

            HELMET_DECORATION = register("helmet_decoration", builder -> builder.persistent(HelmetDecorationComponent.CODEC));
        }

        private static <T> Supplier<DataComponentType<T>> register(final String id, final UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
            return Primal_Registries.DATA_COMPONENTS.registerComponentType(id, builderOperator);
        }
    }
}
