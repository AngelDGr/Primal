package org.primal.datagen.providers;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.primal.Primal_Main;
import org.primal.biome_modifiers.features.*;
import org.primal.biome_modifiers.mobs.*;

import java.util.List;
import java.util.function.Supplier;

public class Primal_BiomeModifiersGenerator {

    public static void bootstrap(final BootstrapContext<BiomeModifier> bootstrap){

        //Animals
        register(bootstrap, "spawn/bear_single", BearSingle_BiomeModifier::new);
        register(bootstrap, "spawn/bear_group", BearGroup_BiomeModifier::new);

        register(bootstrap, "spawn/shark_single", SharkSingle_BiomeModifier::new);
        register(bootstrap, "spawn/shark_group", SharkGroup_BiomeModifier::new);

        register(bootstrap, "spawn/crocodile", CrocodileNormal_BiomeModifier::new);
        register(bootstrap, "spawn/crocodile_warm", CrocodileWarm_BiomeModifier::new);

        register(bootstrap, "spawn/walrus", WalrusNormal_BiomeModifier::new);
        register(bootstrap, "spawn/walrus_ocean", WalrusOcean_BiomeModifier::new);

        register(bootstrap, "spawn/lion", LionSavanna_BiomeModifier::new);
        register(bootstrap, "spawn/lion_snowy", LionSnowy_BiomeModifier::new);

        register(bootstrap, "spawn/snake", Snake_BiomeModifier::new);

        register(bootstrap, "spawn/deer", DeerForest_BiomeModifier::new);
        register(bootstrap, "spawn/deer_snowy", DeerSnowy_BiomeModifier::new);

        register(bootstrap, "spawn/vanilla/dolphin_cold", DolphinCold_BiomeModifier::new);
        register(bootstrap, "spawn/vanilla/rabbit_badlands", RabbitBadlands_BiomeModifier::new);

        //Flora
        register(bootstrap, "feature/river_reeds", RiverReeds_BiomeModifier::new);

        register(bootstrap, "feature/cattails", Cattails_BiomeModifier::new);

        register(bootstrap, "feature/seashells", Seashells_BiomeModifier::new);

        //Nests
        register(bootstrap, "feature/eagle_nest", EagleNest_BiomeModifier::new);
        register(bootstrap, "feature/cassowary_nest", CassowaryNest_BiomeModifier::new);
    }

    public static void register(BootstrapContext<BiomeModifier> bootstrap, String name, Supplier<? extends BiomeModifier> sup){
        bootstrap.register(modifierFor(name), sup.get());
    }

    public static void registerVegetation(final BootstrapContext<BiomeModifier> bootstrap, final String biomeModifier, final TagKey<Biome> spawnTag, final ResourceKey<PlacedFeature> placedFeature){
        registerVegetation(bootstrap, modifierFor(biomeModifier), spawnTag, placedFeature);
    }

    public static void registerVegetation(final BootstrapContext<BiomeModifier> bootstrap, final ResourceKey<BiomeModifier> biomeModifierResourceKey, final TagKey<Biome> spawnTag, final ResourceKey<PlacedFeature> placedFeature){
        registerFeature(bootstrap, biomeModifierResourceKey, spawnTag, placedFeature, GenerationStep.Decoration.VEGETAL_DECORATION);
    }

    public static void registerFeature(final BootstrapContext<BiomeModifier> bootstrap, final ResourceKey<BiomeModifier> biomeModifierResourceKey, final TagKey<Biome> spawnTag, final ResourceKey<PlacedFeature> placedFeature, final GenerationStep.Decoration step){
        final HolderGetter<Biome> biomes = bootstrap.lookup(Registries.BIOME);
        final HolderGetter<PlacedFeature> placedFeatures = bootstrap.lookup(Registries.PLACED_FEATURE);

        // Register the biome modifiers.
        bootstrap.register(biomeModifierResourceKey,
                new BiomeModifiers.AddFeaturesBiomeModifier(
                        // The biome(s) to generate within
                        (biomes.getOrThrow(spawnTag)),
                        // The feature(s) to generate within the biomes
                        HolderSet.direct(placedFeatures.getOrThrow(placedFeature)),
                        // The generation step
                        step
                )
        );
    }

    public static void registerMobSpawn(final BootstrapContext<BiomeModifier> bootstrap, final String biomeModifier, final TagKey<Biome> spawnTag, final EntityType<?> mob, final int weight, final int minGroupSize, final int maxGroupSize){
        registerMobSpawn(bootstrap, modifierFor(biomeModifier), spawnTag, mob, weight, minGroupSize, maxGroupSize);
    }

    public static void registerMobSpawn(final BootstrapContext<BiomeModifier> bootstrap, final ResourceKey<BiomeModifier> biomeModifierResourceKey, final TagKey<Biome> spawnTag, final EntityType<?> mob, final int weight, final int minGroupSize, final int maxGroupSize){
        final HolderGetter<Biome> biomes = bootstrap.lookup(Registries.BIOME);

        // Register the biome modifiers.
        bootstrap.register(biomeModifierResourceKey,
                new BiomeModifiers.AddSpawnsBiomeModifier(
                        // The biome(s) to spawn the mobs within
                        biomes.getOrThrow(spawnTag),
                        // The spawners of the entities to add
                        List.of(
                                new MobSpawnSettings.SpawnerData(mob, weight, minGroupSize, maxGroupSize)
                        )
                )
        );
    }

    public static ResourceKey<BiomeModifier> modifierFor(final String id){
        return ResourceKey.create(
                NeoForgeRegistries.Keys.BIOME_MODIFIERS, // The registry this key is for
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, id) // The registry variant
        );
    }
}
