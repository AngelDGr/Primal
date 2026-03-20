package org.primal.datagen.providers.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.Primal_Main;
import org.primal.registry.Primal_BannerPatterns;
import org.primal.registry.Primal_Tags;

import java.util.concurrent.CompletableFuture;

public class Primal_BannerPatternTagsGenerator extends TagsProvider<BannerPattern> {

    public Primal_BannerPatternTagsGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.BANNER_PATTERN, lookupProvider, Primal_Main.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        this.tag(Primal_Tags.BannerPattern.PATTERN_ITEM_PAW)
                .add(Primal_BannerPatterns.PAW);

        this.tag(Primal_Tags.BannerPattern.PATTERN_ITEM_JAWS)
                .add(Primal_BannerPatterns.JAWS);

        this.tag(Primal_Tags.BannerPattern.PATTERN_ITEM_MARSH)
                .add(Primal_BannerPatterns.MARSH);

        this.tag(Primal_Tags.BannerPattern.PATTERN_ITEM_EYRIE)
                .add(Primal_BannerPatterns.EYRIE);

        this.tag(Primal_Tags.BannerPattern.PATTERN_ITEM_SLITHER)
                .add(Primal_BannerPatterns.SLITHER);

        this.tag(Primal_Tags.BannerPattern.PATTERN_ITEM_ROYAL)
                .add(Primal_BannerPatterns.ROYAL);
    }
}
