package org.primal.datagen.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.primal.registry.Primal_Blocks;
import org.primal.registry.Primal_Items;

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
                .unlockedBy("has_feather", has(Items.FEATHER))
                .unlockedBy("has_tooth", has(Primal_Items.SHARK_TOOTH.get()))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, Items.TRIDENT, 1)
                .define('X', Primal_Items.SHARK_TOOTH.get())
                .define('Y', Items.PRISMARINE_SHARD)
                .define('D', Items.DIAMOND)
                .pattern("XXX")
                .pattern("YDY")
                .pattern(" D ")
                .unlockedBy("has_tooth", has(Primal_Items.SHARK_TOOTH.get()))
                .save(exporter);

        twoByTwoPacker(exporter, RecipeCategory.BUILDING_BLOCKS, Primal_Blocks.CROCODILE_SCUTE_BLOCK.get(), Primal_Items.CROCODILE_SCUTE.get());

        twoByTwoPacker(exporter, RecipeCategory.BUILDING_BLOCKS, Primal_Blocks.CROCODILE_SCUTE_SHINGLE.get(), Primal_Blocks.CROCODILE_SCUTE_BLOCK.get());

        chiseledBuilder(RecipeCategory.BUILDING_BLOCKS, Primal_Blocks.CHISELED_CROCODILE_SCUTE.get(), Ingredient.of(Primal_Blocks.CROCODILE_SCUTE_SLAB.get()))
                .unlockedBy("has_crocodile_scute", has(Primal_Items.CROCODILE_SCUTE.get()))
                .save(exporter);

        stairBuilder(Primal_Blocks.CROCODILE_SCUTE_STAIRS.get(),  Ingredient.of(Primal_Blocks.CROCODILE_SCUTE_BLOCK.get().asItem()))
                .unlockedBy("has_crocodile_scute", has(Primal_Items.CROCODILE_SCUTE.get()))
                .save(exporter);

        slabBuilder(RecipeCategory.BUILDING_BLOCKS, Primal_Blocks.CROCODILE_SCUTE_SLAB.get(),  Ingredient.of(Primal_Blocks.CROCODILE_SCUTE_BLOCK.get().asItem()))
                .unlockedBy("has_crocodile_scute", has(Primal_Items.CROCODILE_SCUTE.get()))
                .save(exporter);
    }
}
