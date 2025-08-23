package org.primal.datagen.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.Primal_Main;
import org.primal.registry.Primal_Tags;

import java.util.concurrent.CompletableFuture;

public class Primal_BiomeTagGenerator extends BiomeTagsProvider {

    public Primal_BiomeTagGenerator(final PackOutput output, final CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable final ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Primal_Main.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(final HolderLookup.@NotNull Provider arg) {

        //Bear
        this.tag(Primal_Tags.SPAWNS_BEAR)
                .add(Biomes.BIRCH_FOREST)
                .add(Biomes.FLOWER_FOREST)
                .add(Biomes.DARK_FOREST)
                .add(Biomes.TAIGA)
                .add(Biomes.OLD_GROWTH_SPRUCE_TAIGA).add(Biomes.OLD_GROWTH_PINE_TAIGA).add(Biomes.OLD_GROWTH_BIRCH_FOREST)
                .add(Biomes.WINDSWEPT_FOREST);

        this.tag(Primal_Tags.SPAWNS_BLACK_BEAR)
                .add(Biomes.JUNGLE).add(Biomes.SPARSE_JUNGLE)
                .add(Biomes.WOODED_BADLANDS);

        //Shark
        this.tag(Primal_Tags.SPAWNS_SHARK)
                .addTag(BiomeTags.IS_OCEAN);

        this.tag(Primal_Tags.SPAWNS_TIGER_SHARK)
                .add(Biomes.WARM_OCEAN);

        this.tag(Primal_Tags.SPAWNS_HAMMERHEAD)
                .add(Biomes.LUKEWARM_OCEAN)
                .add(Biomes.DEEP_LUKEWARM_OCEAN);
    }
}