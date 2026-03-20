package org.primal.datagen.providers.tag;

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
import org.primal.util.Primal_Util;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public class Primal_BiomeTagGenerator extends BiomeTagsProvider {

    public Primal_BiomeTagGenerator(final PackOutput output, final CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable final ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Primal_Main.MOD_ID, existingFileHelper);
    }

    //No Man's Land Biomes
    ResourceLocation autumnal_forest = Primal_Util.nomanslandLoc("autumnal_forest");
    ResourceLocation maple_forest = Primal_Util.nomanslandLoc("maple_forest");
    ResourceLocation maple_grove = Primal_Util.nomanslandLoc("maple_grove");
    ResourceLocation frozen_woods = Primal_Util.nomanslandLoc("frozen_woods");
    ResourceLocation old_growth_forest = Primal_Util.nomanslandLoc("old_growth_forest");
    ResourceLocation old_growth_forest_clearing = Primal_Util.nomanslandLoc("old_growth_forest_clearing");
    ResourceLocation old_growth_forest_edge = Primal_Util.nomanslandLoc("old_growth_forest_edge");
    ResourceLocation boreal_forest = Primal_Util.nomanslandLoc("boreal_forest");
    ResourceLocation dark_taiga = Primal_Util.nomanslandLoc("dark_taiga");
    ResourceLocation dark_swamp = Primal_Util.nomanslandLoc("dark_swamp");
    ResourceLocation bayou = Primal_Util.nomanslandLoc("bayou");
    ResourceLocation bog = Primal_Util.nomanslandLoc("bog");
    ResourceLocation prairie = Primal_Util.nomanslandLoc("prairie");
    ResourceLocation lavender_field = Primal_Util.nomanslandLoc("lavender_field");
    ResourceLocation lush_river = Primal_Util.nomanslandLoc("lush_river");
    ResourceLocation blackwater_river = Primal_Util.nomanslandLoc("blackwater_river");
    ResourceLocation desert_river = Primal_Util.nomanslandLoc("desert_river");
    ResourceLocation mud_beach = Primal_Util.nomanslandLoc("mud_beach");
    ResourceLocation frozen_shore = Primal_Util.nomanslandLoc("frozen_shore");
    ResourceLocation tropical_beach = Primal_Util.nomanslandLoc("tropical_beach");
    ResourceLocation downfall_isle = Primal_Util.nomanslandLoc("downfall_isle");

    ResourceLocation caves = Primal_Util.nomanslandLoc("caves");
    ResourceLocation cave_depths = Primal_Util.nomanslandLoc("cave_depths");

    @Override
    protected void addTags(final HolderLookup.@NotNull Provider arg) {

        //Animals
        {
            //Bear
            this.tag(Primal_Tags.Biome.SPAWNS_BEAR)
                    .addTag(BiomeTags.IS_FOREST)
                    .add(Biomes.TAIGA)
                    .add(Biomes.OLD_GROWTH_SPRUCE_TAIGA).add(Biomes.OLD_GROWTH_PINE_TAIGA)
                    .add(Biomes.WINDSWEPT_FOREST)
                    .addTag(Primal_Tags.Biome.SPAWNS_BLACK_BEAR)
                    .addOptional(old_growth_forest)
                    .addOptional(old_growth_forest_edge)
                    .addOptional(boreal_forest)
                    .addOptional(dark_taiga);

            this.tag(Primal_Tags.Biome.SPAWNS_BLACK_BEAR)
                    .add(Biomes.JUNGLE).add(Biomes.SPARSE_JUNGLE)
                    .add(Biomes.WOODED_BADLANDS);

            //Shark
            this.tag(Primal_Tags.Biome.SPAWNS_SHARK)
                    .addTag(BiomeTags.IS_OCEAN);

            this.tag(Primal_Tags.Biome.SPAWNS_HAMMERHEAD)
                    .add(Biomes.LUKEWARM_OCEAN)
                    .add(Biomes.DEEP_LUKEWARM_OCEAN);

            this.tag(Primal_Tags.Biome.SPAWNS_TIGER_SHARK)
                    .add(Biomes.WARM_OCEAN);

            this.tag(Primal_Tags.Biome.SPAWNS_MACKEREL_SHARK)
                    .add(Biomes.COLD_OCEAN)
                    .add(Biomes.DEEP_COLD_OCEAN);

            this.tag(Primal_Tags.Biome.SPAWNS_SLEEPER_SHARK)
                    .add(Biomes.FROZEN_OCEAN)
                    .add(Biomes.DEEP_FROZEN_OCEAN);

            //Crocodile
            this.tag(Primal_Tags.Biome.SPAWNS_CROCODILE)
                    .addOptional(dark_swamp)
                    .add(Biomes.SWAMP)

                    .addTag(Primal_Tags.Biome.SPAWNS_BLACK_CROCODILE);

            this.tag(Primal_Tags.Biome.SPAWNS_BLACK_CROCODILE)
                    .addOptional(bayou)
                    .add(Biomes.JUNGLE)
                    .add(Biomes.SPARSE_JUNGLE)
                    .add(Biomes.BAMBOO_JUNGLE)
                    .add(Biomes.MANGROVE_SWAMP);

            this.tag(Primal_Tags.Biome.SPAWNS_BROWN_CROCODILE)
                    .addOptional(bog)
                    .add(Biomes.DESERT)
                    .add(Biomes.SAVANNA)
                    .add(Biomes.WINDSWEPT_SAVANNA);

            //Eagle
            this.tag(Primal_Tags.Biome.SPAWNS_EAGLE)
                    .add(Biomes.FROZEN_PEAKS)
                    .addTag(Primal_Tags.Biome.SPAWNS_GOLDEN_EAGLE)
                    .addTag(Primal_Tags.Biome.SPAWNS_HARPY_EAGLE)
                    .addTag(Primal_Tags.Biome.SPAWNS_PHILIPPINE_EAGLE);

            this.tag(Primal_Tags.Biome.SPAWNS_GOLDEN_EAGLE)
                    .add(Biomes.JAGGED_PEAKS);

            this.tag(Primal_Tags.Biome.SPAWNS_HARPY_EAGLE)
                    .add(Biomes.STONY_PEAKS);

            this.tag(Primal_Tags.Biome.SPAWNS_PHILIPPINE_EAGLE)
                    .add(Biomes.WINDSWEPT_SAVANNA);

            //Cassowary
            this.tag(Primal_Tags.Biome.SPAWNS_CASSOWARY)
                    .add(Biomes.JUNGLE)
                    .addTag(Primal_Tags.Biome.SPAWNS_SUNSET_CASSOWARY);

            this.tag(Primal_Tags.Biome.SPAWNS_SUNSET_CASSOWARY)
                .add(Biomes.BAMBOO_JUNGLE);

            //Walrus
            this.tag(Primal_Tags.Biome.SPAWNS_WALRUS)
                    .add(Biomes.SNOWY_BEACH)
                    .addTag(Primal_Tags.Biome.SPAWNS_SUNBURNT_WALRUS);

            this.tag(Primal_Tags.Biome.SPAWNS_SUNBURNT_WALRUS)
                    .add(Biomes.STONY_SHORE);

            this.tag(Primal_Tags.Biome.SPAWNS_WALRUS_OCEAN)
                    .add(Biomes.FROZEN_OCEAN)
                    .add(Biomes.DEEP_FROZEN_OCEAN);

            //Lion
            this.tag(Primal_Tags.Biome.SPAWNS_LION)
                    .add(Biomes.SAVANNA)
                    .addTag(Primal_Tags.Biome.SPAWNS_CARAMEL_LION);

            this.tag(Primal_Tags.Biome.SPAWNS_CARAMEL_LION)
                    .add(Biomes.SAVANNA_PLATEAU);

            this.tag(Primal_Tags.Biome.SPAWNS_CAVE_LION)
                    .add(Biomes.SNOWY_PLAINS)
                    .add(Biomes.ICE_SPIKES);

            //Snake
            {
                this.tag(Primal_Tags.Biome.SPAWNS_SNAKE)
                        .addTag(Primal_Tags.Biome.SPAWNS_VERDANT_SNAKE)
                        .addTag(Primal_Tags.Biome.SPAWNS_LUMBER_SNAKE)
                        .addTag(Primal_Tags.Biome.SPAWNS_TENEBROUS_SNAKE)
                        .addTag(Primal_Tags.Biome.SPAWNS_DUSTY_SNAKE)
                        .addTag(Primal_Tags.Biome.SPAWNS_BROWED_SNAKE)
                        .addTag(Primal_Tags.Biome.SPAWNS_CERULEAN_SNAKE)
                        .addTag(Primal_Tags.Biome.SPAWNS_BRACKISH_SNAKE)
                        .addTag(Primal_Tags.Biome.SPAWNS_APOSEMATIC_SNAKE);

                this.tag(Primal_Tags.Biome.SPAWNS_VERDANT_SNAKE)
                        .add(Biomes.PLAINS)
                        .addTag(BiomeTags.IS_FOREST);

                this.tag(Primal_Tags.Biome.SPAWNS_LUMBER_SNAKE)
                        .add(Biomes.TAIGA)
                        .add(Biomes.OLD_GROWTH_PINE_TAIGA)
                        .add(Biomes.OLD_GROWTH_SPRUCE_TAIGA);

                this.tag(Primal_Tags.Biome.SPAWNS_TENEBROUS_SNAKE)
                        .addTag(BiomeTags.IS_SAVANNA);

                this.tag(Primal_Tags.Biome.SPAWNS_DUSTY_SNAKE)
                        .addTag(BiomeTags.IS_BADLANDS);

                this.tag(Primal_Tags.Biome.SPAWNS_BROWED_SNAKE)
                        .add(Biomes.DESERT);

                this.tag(Primal_Tags.Biome.SPAWNS_CERULEAN_SNAKE)
                        .add(Biomes.JUNGLE)
                        .add(Biomes.BAMBOO_JUNGLE);

                this.tag(Primal_Tags.Biome.SPAWNS_BRACKISH_SNAKE)
                        .add(Biomes.MANGROVE_SWAMP);

                this.tag(Primal_Tags.Biome.SPAWNS_APOSEMATIC_SNAKE)
                        .add(Biomes.SPARSE_JUNGLE);

                this.tag(Primal_Tags.Biome.SPAWNS_MARINE_SNAKE)
                        .addTag(BiomeTags.IS_OCEAN)
                        .addTag(BiomeTags.IS_BEACH);
            }

            //Deer
            this.tag(Primal_Tags.Biome.SPAWNS_DEER)
                    .add(Biomes.BIRCH_FOREST)
                    .add(Biomes.OLD_GROWTH_BIRCH_FOREST)
                    .add(Biomes.FLOWER_FOREST)
                    .add(Biomes.MEADOW)
                    .add(Biomes.CHERRY_GROVE)
                    .addTag(Primal_Tags.Biome.SPAWNS_WHITETAIL_DEER)
                    .addTag(Primal_Tags.Biome.SPAWNS_MUSK_DEER)
                    .addTag(Primal_Tags.Biome.SPAWNS_REINDEER);

            this.tag(Primal_Tags.Biome.SPAWNS_WHITETAIL_DEER)
                    .add(Biomes.WOODED_BADLANDS);

            this.tag(Primal_Tags.Biome.SPAWNS_REINDEER)
                    .add(Biomes.SNOWY_PLAINS)
                    .add(Biomes.ICE_SPIKES)
                    .add(Biomes.SNOWY_TAIGA)
                    .add(Biomes.GROVE);

            this.tag(Primal_Tags.Biome.SPAWNS_MUSK_DEER)
                    .add(Biomes.OLD_GROWTH_SPRUCE_TAIGA)
                    .add(Biomes.OLD_GROWTH_PINE_TAIGA)
                    .add(Biomes.WINDSWEPT_FOREST)
                    .add(Biomes.WINDSWEPT_GRAVELLY_HILLS)
                    .add(Biomes.WINDSWEPT_HILLS);

            //Dolphin
            {
                this.tag(Primal_Tags.Biome.SPAWNS_DOLPHIN_WARM)
                        .add(Biomes.WARM_OCEAN);

                this.tag(Primal_Tags.Biome.SPAWNS_DOLPHIN_COLD)
                        .add(Biomes.COLD_OCEAN)
                        .add(Biomes.DEEP_COLD_OCEAN);
            }

            //Rabbit
            this.tag(Primal_Tags.Biome.SPAWNS_RABBIT_GRAY)
                    .addTag(BiomeTags.IS_TAIGA)
                    .add(Biomes.MEADOW);

            this.tag(Primal_Tags.Biome.SPAWNS_RABBIT_SAND)
                    .addTag(BiomeTags.IS_BADLANDS);
        }

        //Flora
        {
            this.tag(Primal_Tags.Biome.SPAWNS_RIVER_REEDS)
                    .addTag(BiomeTags.IS_RIVER);

            this.tag(Primal_Tags.Biome.SPAWNS_CATTAILS)
                    .add(Biomes.SWAMP)
                    .addOptional(dark_swamp)
                    .add(Biomes.MANGROVE_SWAMP);

            this.tag(Primal_Tags.Biome.SPAWNS_SEASHELLS)
                    .add(Biomes.WARM_OCEAN)
                    .addOptional(tropical_beach)
                    .add(Biomes.BEACH)

                    .addTag(Primal_Tags.Biome.SPAWNS_SEASHELLS_COLD)
                    .addTag(Primal_Tags.Biome.SPAWNS_SEASHELLS_TEMPERATE);

            this.tag(Primal_Tags.Biome.SPAWNS_SEASHELLS_COLD)
                    .add(Biomes.COLD_OCEAN)
                    .add(Biomes.DEEP_COLD_OCEAN)
                    .add(Biomes.SNOWY_BEACH)
                    .addOptional(frozen_shore);

            this.tag(Primal_Tags.Biome.SPAWNS_SEASHELLS_TEMPERATE)
                    .add(Biomes.OCEAN)
                    .add(Biomes.DEEP_OCEAN)
                    .add(Biomes.LUKEWARM_OCEAN)
                    .add(Biomes.DEEP_LUKEWARM_OCEAN);

            this.tag(Primal_Tags.Biome.SPAWNS_THORNY_ACACIA)
                    .add(Biomes.SAVANNA_PLATEAU);

            this.tag(Primal_Tags.Biome.SPAWNS_HOLLOW_TREE)
                    .addTag(Primal_Tags.Biome.SPAWNS_SNAKE);
        }
    }
}