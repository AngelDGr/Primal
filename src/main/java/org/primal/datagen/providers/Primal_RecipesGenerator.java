package org.primal.datagen.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
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
    }
}
