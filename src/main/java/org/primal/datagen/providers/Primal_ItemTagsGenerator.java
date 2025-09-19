package org.primal.datagen.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.Primal_Main;
import org.primal.registry.Primal_Items;
import org.primal.registry.Primal_Tags;

import java.util.concurrent.CompletableFuture;

public class Primal_ItemTagsGenerator extends ItemTagsProvider {

    public Primal_ItemTagsGenerator(final PackOutput arg, final CompletableFuture<HolderLookup.Provider> completableFuture, final CompletableFuture<TagLookup<Block>> completableFuture2, @Nullable final ExistingFileHelper existingFileHelper) {
        super(arg, completableFuture, completableFuture2, Primal_Main.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(final HolderLookup.@NotNull Provider lookup) {
        this.tag(Primal_Tags.CROCODILE_CANT_EAT)
                .addTag(Tags.Items.TOOLS_BRUSH)
                .addTag(Tags.Items.FEATHERS);

        this.tag(Tags.Items.EGGS)
                .add(Primal_Items.CROCODILE_EGG.get())
                .add(Primal_Items.EAGLE_EGG.get());

        this.tag(Tags.Items.FOODS_GOLDEN)
                .add(Primal_Items.GOLDEN_APPLE_FRITTER.get())
                .add(Primal_Items.ENCHANTED_GOLDEN_APPLE_FRITTER.get());
    }
}