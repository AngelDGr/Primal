package org.primal.datagen.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.Primal_Main;
import org.primal.registry.Primal_Blocks;
import org.primal.registry.Primal_Tags;

import java.util.concurrent.CompletableFuture;

public class Primal_BlockTagsGenerator extends BlockTagsProvider {

    public Primal_BlockTagsGenerator(final PackOutput output, final CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable final ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Primal_Main.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(final HolderLookup.@NotNull Provider lookup) {
        this.tag(Primal_Tags.BEAR_REPELLENTS)
                .addTag(BlockTags.CAMPFIRES);

        this.tag(Primal_Tags.SHARK_ATTRACTORS)
                .add(Blocks.CONDUIT);

        this.tag(Primal_Tags.CROCODILE_ATTRACTORS)
                .add(Primal_Blocks.RIVER_REEDS.get())
                .add(Primal_Blocks.SHORT_RIVER_REEDS.get());
    }
}