package org.primal.datagen.providers.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.Primal_Main;
import org.primal.registry.Primal_Items;
import org.primal.registry.Primal_Tags;
import org.primal.util.Primal_Util;

import java.util.concurrent.CompletableFuture;

public class Primal_ItemTagsGenerator extends ItemTagsProvider {

    public Primal_ItemTagsGenerator(final PackOutput arg, final CompletableFuture<HolderLookup.Provider> completableFuture, final CompletableFuture<TagLookup<Block>> completableFuture2, @Nullable final ExistingFileHelper existingFileHelper) {
        super(arg, completableFuture, completableFuture2, Primal_Main.MOD_ID, existingFileHelper);
    }

    ResourceLocation seashells = Primal_Util.nomanslandLoc("seashells");

    @Override
    protected void addTags(final HolderLookup.@NotNull Provider lookup) {
        this.tag(Primal_Tags.Item.CROCODILE_CANT_EAT)
                .addTag(Tags.Items.TOOLS_BRUSH)
                .addTag(Tags.Items.FEATHERS);

        this.tag(Tags.Items.EGGS)
                .add(Primal_Items.CROCODILE_EGG.get())
                .add(Primal_Items.EAGLE_EGG.get())
                .add(Primal_Items.CASSOWARY_EGG.get());

        this.tag(Tags.Items.FOODS_GOLDEN)
                .add(Primal_Items.GOLDEN_APPLE_FRITTER.get())
                .add(Primal_Items.ENCHANTED_GOLDEN_APPLE_FRITTER.get());

        this.tag(Primal_Tags.Item.STRAW)
                .add(Primal_Items.RIVER_REEDS.get())
                .add(Primal_Items.SHORT_RIVER_REEDS.get())
                .add(Primal_Items.CATTAILS.get())
                .add(Items.SHORT_GRASS)
                .add(Items.TALL_GRASS);

        this.tag(Primal_Tags.Item.BEAR_HEALING_TREATS)
                .add(Items.SWEET_BERRIES)
                .add(Items.GLOW_BERRIES)
                .add(Items.SUGAR)
                .add(Items.COOKIE)
                .add(Items.PUMPKIN_PIE);

        this.tag(Primal_Tags.Item.MAKES_CROCODILE_EXPLODE)
                .add(Items.TNT)
                .add(Items.TNT_MINECART);

        this.tag(Primal_Tags.Item.MAKES_CROCODILE_TICK_TOCK)
                .add(Items.CLOCK);

        this.tag(Primal_Tags.Item.SEASHELLS)
                .add(Primal_Items.WARM_SEASHELLS.get())
                .add(Primal_Items.COLD_SEASHELLS.get())
                .add(Primal_Items.TEMPERATE_SEASHELLS.get())
                .addOptional(seashells);

        this.tag(Primal_Tags.Item.EXOTIC_FRUITS)
                .add(Primal_Items.LITCHI.get())
                .add(Primal_Items.KIWANO.get())
                .add(Primal_Items.STARFRUIT.get());

        this.tag(Primal_Tags.Item.DISTRACTS_CASSOWARY)
                .add(Items.GLISTERING_MELON_SLICE);

        this.tag(Primal_Tags.Item.PROCESSES_CASSOWARY)
                .add(Primal_Items.PETRIFIED_FRUIT.get());

        this.tag(Primal_Tags.Item.WALRUS_INSTRUMENT)
                .add(Primal_Items.WARM_CONCH_SHELL.get())
                .add(Primal_Items.TEMPERATE_CONCH_SHELL.get())
                .add(Primal_Items.COLD_CONCH_SHELL.get());

        this.tag(ItemTags.LEAVES)
                .add(Primal_Items.THORNY_ACACIA_LEAVES.get());

        this.tag(ItemTags.ACACIA_LOGS)
                .add(Primal_Items.THORNY_ACACIA_LOG.get())
                .add(Primal_Items.THORNY_ACACIA_WOOD.get());

        this.tag(ItemTags.SAPLINGS)
                .add(Primal_Items.THORNY_ACACIA_SAPLING.get());

        this.tag(Tags.Items.SEEDS)
                .add(Primal_Items.STARFRUIT_SEEDS.get())
                .add(Primal_Items.KIWANO_SEEDS.get())
                .add(Primal_Items.LITCHI_SEEDS.get());

        this.tag(ItemTags.PARROT_FOOD)
                .add(Primal_Items.STARFRUIT_SEEDS.get())
                .add(Primal_Items.KIWANO_SEEDS.get())
                .add(Primal_Items.LITCHI_SEEDS.get());

        this.tag(ItemTags.CHICKEN_FOOD)
                .add(Primal_Items.STARFRUIT_SEEDS.get())
                .add(Primal_Items.KIWANO_SEEDS.get())
                .add(Primal_Items.LITCHI_SEEDS.get());

        this.tag(Tags.Items.MUSIC_DISCS)
                .add(Primal_Items.MUSIC_DISC_OH_DEER.get());

        this.tag(Primal_Tags.Item.HOLLOW_LOGS)
                .add(Primal_Items.HOLLOW_OAK_LOG.get())
                .add(Primal_Items.HOLLOW_SPRUCE_LOG.get())
                .add(Primal_Items.HOLLOW_BIRCH_LOG.get())
                .add(Primal_Items.HOLLOW_JUNGLE_LOG.get())
                .add(Primal_Items.HOLLOW_ACACIA_LOG.get())
                .add(Primal_Items.HOLLOW_DARK_OAK_LOG.get())
                .add(Primal_Items.HOLLOW_MANGROVE_LOG.get());

        this.tag(ItemTags.LOGS)
                .addTag(Primal_Tags.Item.HOLLOW_LOGS);

        this.tag(Primal_Tags.Item.SNAKE_EDIBLE_EGGS)
                .add(Items.TURTLE_EGG)
                .addTag(Tags.Items.EGGS);

        this.tag(ItemTags.MEAT)
                .add(Primal_Items.VENISON.get())
                .add(Primal_Items.COOKED_VENISON.get());

        this.tag(Tags.Items.FOODS_RAW_MEAT)
                .add(Primal_Items.VENISON.get());

        this.tag(Tags.Items.FOODS_COOKED_MEAT)
                .add(Primal_Items.COOKED_VENISON.get());

        this.tag(Primal_Tags.Item.HELMET_ATTACHMENTS)
                .addTag(Primal_Tags.Item.DEER_ANTLERS)
                .add(Items.GOAT_HORN);

        this.tag(Primal_Tags.Item.DEER_ANTLERS)
                .add(Primal_Items.FALLOW_DEER_ANTLER.get())
                .add(Primal_Items.REINDEER_ANTLER.get())
                .add(Primal_Items.WHITETAIL_DEER_ANTLER.get());

        this.tag(Primal_Tags.Item.BEAR_BREED_FOOD)
                .add(Items.SALMON_BUCKET);

        this.tag(Primal_Tags.Item.EAGLE_BREED_FOOD)
                .add(Items.RABBIT_STEW);

        this.tag(Primal_Tags.Item.CROCODILE_BREED_FOOD)
                .add(Items.CHICKEN);

        this.tag(Primal_Tags.Item.CASSOWARY_BREED_FOOD)
                .addTag(Primal_Tags.Item.EXOTIC_FRUITS);

        this.tag(Primal_Tags.Item.WALRUS_BREED_FOOD)
                .add(Items.COD_BUCKET);

        this.tag(Primal_Tags.Item.LION_BREED_FOOD)
                .addTag(ItemTags.MEAT);

        this.tag(Primal_Tags.Item.SNAKE_BREED_FOOD)
                .add(Items.FERMENTED_SPIDER_EYE);

        this.tag(Primal_Tags.Item.DEER_BREED_FOOD)
                .add(Items.APPLE);
    }
}