package org.primal.datagen;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;
import org.primal.Primal_Main;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public final class ModEntityTypeTagsProvider extends EntityTypeTagsProvider {

    public ModEntityTypeTagsProvider(PackOutput output, CompletableFuture<Provider> provider,
            @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, Primal_Main.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(Provider provider) {
        this.tag(ModEntityTypeTags.BEAR_HUNTABLE).add(
                EntityType.PLAYER,
                EntityType.PIG,
                EntityType.SHEEP,
                EntityType.CHICKEN,
                EntityType.COW,
                EntityType.HORSE,
                EntityType.DONKEY,
                EntityType.LLAMA,
                EntityType.RABBIT);
    }
}
