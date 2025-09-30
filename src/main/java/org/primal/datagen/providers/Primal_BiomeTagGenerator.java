package org.primal.datagen.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.Primal_Main;
import org.primal.registry.Primal_Tags;
import org.primal.util.MiscUtil;

import java.util.concurrent.CompletableFuture;

public class Primal_BiomeTagGenerator extends BiomeTagsProvider {

    public Primal_BiomeTagGenerator(final PackOutput output, final CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable final ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Primal_Main.MOD_ID, existingFileHelper);
    }

    //No Man's Land Biomes
    ResourceLocation autumnal_forest = MiscUtil.nomanslandLoc("autumnal_forest");
    ResourceLocation maple_forest = MiscUtil.nomanslandLoc("maple_forest");
    ResourceLocation maple_grove = MiscUtil.nomanslandLoc("maple_grove");
    ResourceLocation frozen_woods = MiscUtil.nomanslandLoc("frozen_woods");
    ResourceLocation old_growth_forest = MiscUtil.nomanslandLoc("old_growth_forest");
    ResourceLocation old_growth_forest_clearing = MiscUtil.nomanslandLoc("old_growth_forest_clearing");
    ResourceLocation old_growth_forest_edge = MiscUtil.nomanslandLoc("old_growth_forest_edge");
    ResourceLocation boreal_forest = MiscUtil.nomanslandLoc("boreal_forest");
    ResourceLocation dark_taiga = MiscUtil.nomanslandLoc("dark_taiga");
    ResourceLocation dark_swamp = MiscUtil.nomanslandLoc("dark_swamp");
    ResourceLocation bayou = MiscUtil.nomanslandLoc("bayou");
    ResourceLocation bog = MiscUtil.nomanslandLoc("bog");
    ResourceLocation prairie = MiscUtil.nomanslandLoc("prairie");
    ResourceLocation lavender_field = MiscUtil.nomanslandLoc("lavender_field");
    ResourceLocation lush_river = MiscUtil.nomanslandLoc("lush_river");
    ResourceLocation blackwater_river = MiscUtil.nomanslandLoc("blackwater_river");
    ResourceLocation desert_river = MiscUtil.nomanslandLoc("desert_river");
    ResourceLocation mud_beach = MiscUtil.nomanslandLoc("mud_beach");
    ResourceLocation frozen_shore = MiscUtil.nomanslandLoc("frozen_shore");
    ResourceLocation tropical_beach = MiscUtil.nomanslandLoc("tropical_beach");
    ResourceLocation downfall_isle = MiscUtil.nomanslandLoc("downfall_isle");

    ResourceLocation caves = MiscUtil.nomanslandLoc("caves");
    ResourceLocation cave_depths = MiscUtil.nomanslandLoc("cave_depths");

    @Override
    protected void addTags(final HolderLookup.@NotNull Provider arg) {

        //Animals
        {
            //Bear
            this.tag(Primal_Tags.SPAWNS_BEAR)
                    .add(Biomes.BIRCH_FOREST)
                    .add(Biomes.FLOWER_FOREST)
                    .add(Biomes.DARK_FOREST)
                    .add(Biomes.TAIGA)
                    .add(Biomes.OLD_GROWTH_SPRUCE_TAIGA).add(Biomes.OLD_GROWTH_PINE_TAIGA).add(Biomes.OLD_GROWTH_BIRCH_FOREST)
                    .add(Biomes.WINDSWEPT_FOREST)
                    .add(Biomes.GROVE)
                    .addOptional(old_growth_forest)
                    .addOptional(old_growth_forest_edge)
                    .addOptional(boreal_forest)
                    .addOptional(dark_taiga)
                    .addTag(Primal_Tags.SPAWNS_BLACK_BEAR);

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

            //Crocodile
            this.tag(Primal_Tags.SPAWNS_CROCODILE)
                    .addOptional(dark_swamp)
                    .add(Biomes.SWAMP)

                    .addTag(Primal_Tags.SPAWNS_BLACK_CROCODILE);

            this.tag(Primal_Tags.SPAWNS_BLACK_CROCODILE)
                    .addOptional(bayou)
                    .add(Biomes.JUNGLE)
                    .add(Biomes.SPARSE_JUNGLE)
                    .add(Biomes.BAMBOO_JUNGLE)
                    .add(Biomes.MANGROVE_SWAMP);

            this.tag(Primal_Tags.SPAWNS_BROWN_CROCODILE)
                    .addOptional(bog)
                    .add(Biomes.DESERT)
                    .add(Biomes.SAVANNA)
                    .add(Biomes.WINDSWEPT_SAVANNA);

            //Eagle
            this.tag(Primal_Tags.SPAWNS_EAGLE)
                    .add(Biomes.FROZEN_PEAKS)
                    .addTag(Primal_Tags.SPAWNS_GOLDEN_EAGLE)
                    .addTag(Primal_Tags.SPAWNS_HARPY_EAGLE)
                    .addTag(Primal_Tags.SPAWNS_PHILIPPINE_EAGLE);

            this.tag(Primal_Tags.SPAWNS_GOLDEN_EAGLE)
                    .add(Biomes.JAGGED_PEAKS);

            this.tag(Primal_Tags.SPAWNS_HARPY_EAGLE)
                    .add(Biomes.STONY_PEAKS);

            this.tag(Primal_Tags.SPAWNS_PHILIPPINE_EAGLE)
                    .add(Biomes.WINDSWEPT_SAVANNA);
        }

        //Flora
        {
            this.tag(Primal_Tags.SPAWNS_RIVER_REEDS)
                    .addOptional(lush_river)
                    .addOptional(desert_river)
                    .addOptional(blackwater_river)
                    .add(Biomes.RIVER)
                    .add(Biomes.SWAMP)
                    .addOptional(dark_swamp)
                    .add(Biomes.MANGROVE_SWAMP);

            this.tag(Primal_Tags.SPAWNS_SEASHELLS)
                    .add(Biomes.BEACH)
                    .addOptional(tropical_beach)

                    .add(Biomes.OCEAN)
                    .add(Biomes.DEEP_OCEAN)
                    .add(Biomes.LUKEWARM_OCEAN)
                    .add(Biomes.DEEP_LUKEWARM_OCEAN)
                    .add(Biomes.WARM_OCEAN);
        }
    }
}