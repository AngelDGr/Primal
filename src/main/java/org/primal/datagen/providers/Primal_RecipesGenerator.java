package org.primal.datagen.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.registry.Primal_Blocks;
import org.primal.registry.Primal_Items;
import org.primal.registry.Primal_Tags;

import java.util.concurrent.CompletableFuture;

public class Primal_RecipesGenerator extends RecipeProvider {

    public Primal_RecipesGenerator(final PackOutput output, final CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    public void buildRecipes(final @NotNull RecipeOutput exporter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, Items.ARROW, 2)
                .define('X', Primal_Items.SHARK_TOOTH.get())
                .define('#', Items.STICK)
                .define('Y', Items.FEATHER)
                .pattern("X")
                .pattern("#")
                .pattern("Y")
                .group("arrow")
                .unlockedBy("has_feather", has(Items.FEATHER))
                .unlockedBy("has_tooth", has(Primal_Items.SHARK_TOOTH.get()))
                .save(exporter, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "arrow_shark"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, Items.TRIDENT, 1)
                .define('X', Primal_Items.SHARK_TOOTH.get())
                .define('Y', Items.PRISMARINE_SHARD)
                .define('D', Items.DIAMOND)
                .pattern("XXX")
                .pattern("YDY")
                .pattern(" D ")
                .unlockedBy("has_tooth", has(Primal_Items.SHARK_TOOTH.get()))
                .save(exporter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Primal_Items.APPLE_FRITTER.get())
                .requires(Items.APPLE)
                .requires(Items.SUGAR)
                .requires(Items.WHEAT)
                .requires(Items.EGG)
                .unlockedBy("has_apple", has(Items.APPLE))
                .save(exporter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Primal_Items.GOLDEN_APPLE_FRITTER.get())
                .requires(Items.GOLDEN_APPLE)
                .requires(Items.SUGAR)
                .requires(Items.WHEAT)
                .requires(Items.EGG)
                .unlockedBy("has_golden_apple", has(Items.GOLDEN_APPLE))
                .save(exporter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Primal_Items.ENCHANTED_GOLDEN_APPLE_FRITTER.get())
                .requires(Items.ENCHANTED_GOLDEN_APPLE)
                .requires(Items.SUGAR)
                .requires(Items.WHEAT)
                .requires(Items.EGG)
                .unlockedBy("has_enchanted_golden_apple", has(Items.ENCHANTED_GOLDEN_APPLE))
                .save(exporter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Primal_Items.PAW_BANNER_PATTERN.get())
                .requires(Items.PAPER)
                .requires(Items.HONEYCOMB_BLOCK)
                .unlockedBy("has_honey_comb", has(Items.HONEYCOMB_BLOCK))
                .save(exporter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Primal_Items.JAWS_BANNER_PATTERN.get())
                .requires(Items.PAPER)
                .requires(Primal_Items.SHARK_TOOTH.get())
                .unlockedBy("has_shark_tooth", has(Primal_Items.SHARK_TOOTH.get()))
                .save(exporter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Primal_Items.MARSH_BANNER_PATTERN.get())
                .requires(Items.PAPER)
                .requires(Primal_Items.CROCODILE_SCUTE.get())
                .unlockedBy("has_crocodile_scute", has(Primal_Items.CROCODILE_SCUTE.get()))
                .save(exporter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Primal_Items.EYRIE_BANNER_PATTERN.get())
                .requires(Items.PAPER)
                .requires(Primal_Items.EAGLE_EGG.get())
                .unlockedBy("has_eagle_egg", has(Primal_Items.EAGLE_EGG.get()))
                .save(exporter);

        //Croc Blocks
        {
            //Normal
            {
                ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, Primal_Blocks.CROCODILE_SCUTE_BLOCK.get())
                        .requires(Primal_Blocks.HUMID_CROCODILE_SCUTE_BLOCK.get()).requires(ItemTags.SAND)
                        .group("crocodile_scute_block")
                        .unlockedBy("has_crocodile_scute", has(Primal_Items.CROCODILE_SCUTE.get()))
                        .save(exporter, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "humid_to_normal"));

                ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, Primal_Blocks.CROCODILE_SCUTE_BLOCK.get())
                        .requires(Primal_Blocks.ARID_CROCODILE_SCUTE_BLOCK.get()).requires(Items.MUD)
                        .group("crocodile_scute_block")
                        .unlockedBy("has_crocodile_scute", has(Primal_Items.CROCODILE_SCUTE.get()))
                        .save(exporter, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "arid_to_normal"));

                ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Primal_Blocks.CROCODILE_SCUTE_BLOCK.get())
                        .define('#', Primal_Items.CROCODILE_SCUTE.get()).pattern("##").pattern("##")
                        .group("crocodile_scute_block")
                        .unlockedBy(getHasName(Primal_Items.CROCODILE_SCUTE.get()), has(Primal_Items.CROCODILE_SCUTE.get()))
                        .save(exporter);

                twoByTwoPacker(exporter, RecipeCategory.BUILDING_BLOCKS, Primal_Blocks.CROCODILE_SCUTE_SHINGLE.get(), Primal_Blocks.CROCODILE_SCUTE_BLOCK.get());

                chiseledBuilder(RecipeCategory.BUILDING_BLOCKS, Primal_Blocks.CHISELED_CROCODILE_SCUTE.get(), Ingredient.of(Primal_Blocks.CROCODILE_SCUTE_SLAB.get()))
                        .unlockedBy("has_crocodile_scute", has(Primal_Items.CROCODILE_SCUTE.get()))
                        .save(exporter);

                stairBuilder(Primal_Blocks.CROCODILE_SCUTE_STAIRS.get(), Ingredient.of(Primal_Blocks.CROCODILE_SCUTE_BLOCK.get().asItem()))
                        .unlockedBy("has_crocodile_scute", has(Primal_Items.CROCODILE_SCUTE.get()))
                        .save(exporter);

                slabBuilder(RecipeCategory.BUILDING_BLOCKS, Primal_Blocks.CROCODILE_SCUTE_SLAB.get(), Ingredient.of(Primal_Blocks.CROCODILE_SCUTE_BLOCK.get().asItem()))
                        .unlockedBy("has_crocodile_scute", has(Primal_Items.CROCODILE_SCUTE.get()))
                        .save(exporter);

                //Stone cutting
                {
                    stonecutterResultFromBase(exporter, RecipeCategory.BUILDING_BLOCKS, Primal_Blocks.CROCODILE_SCUTE_SHINGLE.get(), Primal_Blocks.CROCODILE_SCUTE_BLOCK.get());
                    stonecutterResultFromBase(exporter, RecipeCategory.BUILDING_BLOCKS, Primal_Blocks.CHISELED_CROCODILE_SCUTE.get(), Primal_Blocks.CROCODILE_SCUTE_BLOCK.get());
                    stonecutterResultFromBase(exporter, RecipeCategory.BUILDING_BLOCKS, Primal_Blocks.CROCODILE_SCUTE_SLAB.get(), Primal_Blocks.CROCODILE_SCUTE_BLOCK.get(), 2);
                    stonecutterResultFromBase(exporter, RecipeCategory.BUILDING_BLOCKS, Primal_Blocks.CROCODILE_SCUTE_STAIRS.get(), Primal_Blocks.CROCODILE_SCUTE_BLOCK.get());
                }
            }

            //Arid
            {
                ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, Primal_Blocks.ARID_CROCODILE_SCUTE_BLOCK.get())
                        .requires(Primal_Blocks.CROCODILE_SCUTE_BLOCK.get()).requires(ItemTags.SAND)
                        .unlockedBy("has_crocodile_scute", has(Primal_Items.CROCODILE_SCUTE.get()))
                        .save(exporter);

                twoByTwoPacker(exporter, RecipeCategory.BUILDING_BLOCKS, Primal_Blocks.ARID_CROCODILE_SCUTE_SHINGLE.get(), Primal_Blocks.ARID_CROCODILE_SCUTE_BLOCK.get());

                chiseledBuilder(RecipeCategory.BUILDING_BLOCKS, Primal_Blocks.ARID_CHISELED_CROCODILE_SCUTE.get(), Ingredient.of(Primal_Blocks.ARID_CROCODILE_SCUTE_SLAB.get()))
                        .unlockedBy("has_crocodile_scute", has(Primal_Items.CROCODILE_SCUTE.get()))
                        .save(exporter);

                stairBuilder(Primal_Blocks.ARID_CROCODILE_SCUTE_STAIRS.get(), Ingredient.of(Primal_Blocks.ARID_CROCODILE_SCUTE_BLOCK.get().asItem()))
                        .unlockedBy("has_crocodile_scute", has(Primal_Items.CROCODILE_SCUTE.get()))
                        .save(exporter);

                slabBuilder(RecipeCategory.BUILDING_BLOCKS, Primal_Blocks.ARID_CROCODILE_SCUTE_SLAB.get(), Ingredient.of(Primal_Blocks.ARID_CROCODILE_SCUTE_BLOCK.get().asItem()))
                        .unlockedBy("has_crocodile_scute", has(Primal_Items.CROCODILE_SCUTE.get()))
                        .save(exporter);

                //Stone cutting
                {
                    stonecutterResultFromBase(exporter, RecipeCategory.BUILDING_BLOCKS, Primal_Blocks.ARID_CROCODILE_SCUTE_SHINGLE.get(), Primal_Blocks.ARID_CROCODILE_SCUTE_BLOCK.get());
                    stonecutterResultFromBase(exporter, RecipeCategory.BUILDING_BLOCKS, Primal_Blocks.ARID_CHISELED_CROCODILE_SCUTE.get(), Primal_Blocks.ARID_CROCODILE_SCUTE_BLOCK.get());
                    stonecutterResultFromBase(exporter, RecipeCategory.BUILDING_BLOCKS, Primal_Blocks.ARID_CROCODILE_SCUTE_SLAB.get(), Primal_Blocks.ARID_CROCODILE_SCUTE_BLOCK.get(), 2);
                    stonecutterResultFromBase(exporter, RecipeCategory.BUILDING_BLOCKS, Primal_Blocks.ARID_CROCODILE_SCUTE_STAIRS.get(), Primal_Blocks.ARID_CROCODILE_SCUTE_BLOCK.get());
                }
            }

            //Humid
            {
                ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, Primal_Blocks.HUMID_CROCODILE_SCUTE_BLOCK.get())
                        .requires(Primal_Blocks.CROCODILE_SCUTE_BLOCK.get()).requires(Items.MUD)
                        .unlockedBy("has_crocodile_scute", has(Primal_Items.CROCODILE_SCUTE.get()))
                        .save(exporter);

                twoByTwoPacker(exporter, RecipeCategory.BUILDING_BLOCKS, Primal_Blocks.HUMID_CROCODILE_SCUTE_SHINGLE.get(), Primal_Blocks.HUMID_CROCODILE_SCUTE_BLOCK.get());

                chiseledBuilder(RecipeCategory.BUILDING_BLOCKS, Primal_Blocks.HUMID_CHISELED_CROCODILE_SCUTE.get(), Ingredient.of(Primal_Blocks.HUMID_CROCODILE_SCUTE_SLAB.get()))
                        .unlockedBy("has_crocodile_scute", has(Primal_Items.CROCODILE_SCUTE.get()))
                        .save(exporter);

                stairBuilder(Primal_Blocks.HUMID_CROCODILE_SCUTE_STAIRS.get(), Ingredient.of(Primal_Blocks.HUMID_CROCODILE_SCUTE_BLOCK.get().asItem()))
                        .unlockedBy("has_crocodile_scute", has(Primal_Items.CROCODILE_SCUTE.get()))
                        .save(exporter);

                slabBuilder(RecipeCategory.BUILDING_BLOCKS, Primal_Blocks.HUMID_CROCODILE_SCUTE_SLAB.get(), Ingredient.of(Primal_Blocks.HUMID_CROCODILE_SCUTE_BLOCK.get().asItem()))
                        .unlockedBy("has_crocodile_scute", has(Primal_Items.CROCODILE_SCUTE.get()))
                        .save(exporter);

                //Stone cutting
                {
                    stonecutterResultFromBase(exporter, RecipeCategory.BUILDING_BLOCKS, Primal_Blocks.HUMID_CROCODILE_SCUTE_SHINGLE.get(), Primal_Blocks.HUMID_CROCODILE_SCUTE_BLOCK.get());
                    stonecutterResultFromBase(exporter, RecipeCategory.BUILDING_BLOCKS, Primal_Blocks.HUMID_CHISELED_CROCODILE_SCUTE.get(), Primal_Blocks.HUMID_CROCODILE_SCUTE_BLOCK.get());
                    stonecutterResultFromBase(exporter, RecipeCategory.BUILDING_BLOCKS, Primal_Blocks.HUMID_CROCODILE_SCUTE_SLAB.get(), Primal_Blocks.HUMID_CROCODILE_SCUTE_BLOCK.get(), 2);
                    stonecutterResultFromBase(exporter, RecipeCategory.BUILDING_BLOCKS, Primal_Blocks.HUMID_CROCODILE_SCUTE_STAIRS.get(), Primal_Blocks.HUMID_CROCODILE_SCUTE_BLOCK.get());
                }
            }
        }


        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Primal_Blocks.STRAW_BALE.get(), 1)
                .define('#', Primal_Tags.Item.STRAW)
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_reeds", has(Primal_Items.RIVER_REEDS.get()))
                .unlockedBy("has_short_reeds", has(Primal_Items.SHORT_RIVER_REEDS.get()))
                .unlockedBy("has_short_grass", has(Items.SHORT_GRASS))
                .unlockedBy("has_tall_grass", has(Items.TALL_GRASS))
                .save(exporter);
    }
}
