package org.primal.datagen.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
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

                ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, Primal_Blocks.CHOMP_TRAP_GREEN.get())
                        .define('#', Primal_Items.CROCODILE_SCUTE.get())
                        .define('S', Primal_Items.SHARK_TOOTH.get())
                        .define('R', Items.REDSTONE)
                        .define('B', Primal_Items.CROCODILE_SCUTE_BLOCK.get())
                        .pattern("#S#")
                        .pattern("#S#")
                        .pattern("RBR")
                        .group("chomp_trap")
                        .unlockedBy(getHasName(Primal_Items.CROCODILE_SCUTE.get()), has(Primal_Items.CROCODILE_SCUTE.get()))
                        .save(exporter);
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

                ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, Primal_Blocks.CHOMP_TRAP_ARID.get())
                        .define('#', Primal_Items.CROCODILE_SCUTE.get())
                        .define('S', Primal_Items.SHARK_TOOTH.get())
                        .define('R', Items.REDSTONE)
                        .define('B', Primal_Items.ARID_CROCODILE_SCUTE_BLOCK.get())
                        .pattern("#S#")
                        .pattern("#S#")
                        .pattern("RBR")
                        .group("chomp_trap")
                        .unlockedBy(getHasName(Primal_Items.CROCODILE_SCUTE.get()), has(Primal_Items.CROCODILE_SCUTE.get()))
                        .save(exporter);
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

                ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, Primal_Blocks.CHOMP_TRAP_HUMID.get())
                        .define('#', Primal_Items.CROCODILE_SCUTE.get())
                        .define('S', Primal_Items.SHARK_TOOTH.get())
                        .define('R', Items.REDSTONE)
                        .define('B', Primal_Items.HUMID_CROCODILE_SCUTE_BLOCK.get())
                        .pattern("#S#")
                        .pattern("#S#")
                        .pattern("RBR")
                        .group("chomp_trap")
                        .unlockedBy(getHasName(Primal_Items.CROCODILE_SCUTE.get()), has(Primal_Items.CROCODILE_SCUTE.get()))
                        .save(exporter);
            }
        }

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Primal_Blocks.STRAW_BALE.get(), 1)
                .define('#', Primal_Tags.Item.STRAW)
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_straw", has(Primal_Tags.Item.STRAW))
                .save(exporter);

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(Primal_Blocks.STRAW_BALE.get()),
                        RecipeCategory.BUILDING_BLOCKS,
                        Primal_Blocks.DRIED_STRAW_BALE.get(),
                        0.35F,
                        200)
                .unlockedBy("has_straw_bale", has(Primal_Blocks.STRAW_BALE.get()))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Primal_Blocks.WEAVED_STRAW.get(), 4)
                .define('#', Primal_Blocks.DRIED_STRAW_BALE.get())
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_straw", has(Primal_Blocks.DRIED_STRAW_BALE.get()))
                .save(exporter);

        stairBuilder(Primal_Blocks.WEAVED_STRAW_STAIRS.get(), Ingredient.of(Primal_Blocks.WEAVED_STRAW.get().asItem()))
                .unlockedBy("has_straw", has(Primal_Blocks.DRIED_STRAW_BALE.get()))
                .save(exporter);

        slabBuilder(RecipeCategory.BUILDING_BLOCKS, Primal_Blocks.WEAVED_STRAW_SLAB.get(), Ingredient.of(Primal_Blocks.WEAVED_STRAW.get().asItem()))
                .unlockedBy("has_straw", has(Primal_Blocks.DRIED_STRAW_BALE.get()))
                .save(exporter);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Primal_Blocks.STRAW_BASKET.get())
                .define('W', Primal_Blocks.WEAVED_STRAW.get())
                .pattern("WWW")
                .pattern("W W")
                .pattern("WWW")
                .unlockedBy("has_straw", has(Primal_Blocks.DRIED_STRAW_BALE.get()))
                .save(exporter);

        //Conch Shell
        {
            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Primal_Items.WARM_CONCH_SHELL.get(), 1)
                    .define('T', Primal_Items.SHARK_TOOTH.get())
                    .define('N', Items.NAUTILUS_SHELL)
                    .define('H', Items.HEART_OF_THE_SEA)
                    .define('S', Primal_Items.WARM_SEASHELLS.get())
                    .pattern("SHS")
                    .pattern("TNT")
                    .pattern("STS")
                    .unlockedBy("has_nautilus", has(Items.NAUTILUS_SHELL))
                    .save(exporter);

            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Primal_Items.TEMPERATE_CONCH_SHELL.get(), 1)
                    .define('T', Primal_Items.SHARK_TOOTH.get())
                    .define('N', Items.NAUTILUS_SHELL)
                    .define('H', Items.HEART_OF_THE_SEA)
                    .define('S', Primal_Items.TEMPERATE_SEASHELLS.get())
                    .pattern("SHS")
                    .pattern("TNT")
                    .pattern("STS")
                    .unlockedBy("has_nautilus", has(Items.NAUTILUS_SHELL))
                    .save(exporter);

            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Primal_Items.COLD_CONCH_SHELL.get(), 1)
                    .define('T', Primal_Items.SHARK_TOOTH.get())
                    .define('N', Items.NAUTILUS_SHELL)
                    .define('H', Items.HEART_OF_THE_SEA)
                    .define('S', Primal_Items.COLD_SEASHELLS.get())
                    .pattern("SHS")
                    .pattern("TNT")
                    .pattern("STS")
                    .unlockedBy("has_nautilus", has(Items.NAUTILUS_SHELL))
                    .save(exporter);
        }

        //Hollow Blocks
        {
            planksFromHollowLog(exporter, Blocks.OAK_PLANKS, Primal_Items.HOLLOW_OAK_LOG.get());
            planksFromHollowLog(exporter, Blocks.SPRUCE_PLANKS, Primal_Items.HOLLOW_SPRUCE_LOG.get());
            planksFromHollowLog(exporter, Blocks.BIRCH_PLANKS, Primal_Items.HOLLOW_BIRCH_LOG.get());
            planksFromHollowLog(exporter, Blocks.JUNGLE_PLANKS, Primal_Items.HOLLOW_JUNGLE_LOG.get());
            planksFromHollowLog(exporter, Blocks.ACACIA_PLANKS, Primal_Items.HOLLOW_ACACIA_LOG.get());
            planksFromHollowLog(exporter, Blocks.DARK_OAK_PLANKS, Primal_Items.HOLLOW_DARK_OAK_LOG.get());
            planksFromHollowLog(exporter, Blocks.MANGROVE_PLANKS, Primal_Items.HOLLOW_MANGROVE_LOG.get());
        }

        woodFromLogs(exporter, Primal_Blocks.THORNY_ACACIA_WOOD.get(), Primal_Blocks.THORNY_ACACIA_LOG.get());

        createCookFood(exporter, Primal_Items.VENISON.get(), Primal_Items.COOKED_VENISON.get());

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Primal_Blocks.DREAMCATCHER.get())
                .define('A', Primal_Tags.Item.DEER_ANTLERS)
                .define('C', Items.COBWEB)
                .define('F', Tags.Items.FEATHERS)
                .pattern(" A ")
                .pattern("ACA")
                .pattern("FFF")
                .unlockedBy("has_antler", has(Primal_Tags.Item.DEER_ANTLERS))
                .save(exporter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BONE_MEAL, 9)
                .requires(Primal_Tags.Item.DEER_ANTLERS)
                .group("bonemeal")
                .unlockedBy("has_antler", has(Primal_Tags.Item.DEER_ANTLERS))
                .save(exporter, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "antler_bonemeal"));
    }

    protected static void createCookFood(RecipeOutput exporter,ItemLike rawItem, ItemLike cookedItem) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(rawItem), RecipeCategory.FOOD, cookedItem, 0.35F, 200)
                .unlockedBy(getHasName(rawItem), has(rawItem))
                .save(exporter);

        simpleCookingRecipe(exporter, "smoking", RecipeSerializer.SMOKING_RECIPE, SmokingRecipe::new, 100, rawItem, cookedItem, 0.35F);
        simpleCookingRecipe(exporter, "campfire_cooking", RecipeSerializer.CAMPFIRE_COOKING_RECIPE, CampfireCookingRecipe::new, 600, rawItem, cookedItem, 0.35F);
    }

    protected static void planksFromHollowLog(RecipeOutput recipeOutput, ItemLike planks, ItemLike log) {

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, planks, 2)
                .requires(log)
                .group("planks")
                .unlockedBy("has_log", has(log))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID,
                        //oak_planks_from_hollow
                        BuiltInRegistries.ITEM.getKey(planks.asItem()).getPath()+ "_from_hollow"));
    }
}