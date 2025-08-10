package org.primal.datagen.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class Primal_RecipesGenerator extends RecipeProvider {

    public Primal_RecipesGenerator(final PackOutput output, final CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    public void buildRecipes(final @NotNull RecipeOutput exporter) {

    }
}
