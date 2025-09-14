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
import org.primal.registry.Primal_Entities;
import org.primal.registry.Primal_Tags;
import org.primal.registry.Primal_WorldGen;

import java.util.List;

public class Primal_BiomeModifiersGenerator {

    public static void bootstrap(final BootstrapContext<BiomeModifier> bootstrap){

        //Animals
        registerMobSpawn(bootstrap, "spawn/bear", Primal_Tags.SPAWNS_BEAR, Primal_Entities.BEAR.get(), 50, 1, 1);

        registerMobSpawn(bootstrap, "spawn/shark_single", Primal_Tags.SPAWNS_SHARK, Primal_Entities.SHARK.get(), 5, 1, 1);
        registerMobSpawn(bootstrap, "spawn/shark_group", Primal_Tags.SPAWNS_SHARK, Primal_Entities.SHARK.get(), 1, 1, 3);

        registerMobSpawn(bootstrap, "spawn/crocodile", Primal_Tags.SPAWNS_CROCODILE, Primal_Entities.CROCODILE.get(), 20, 1, 1);
        registerMobSpawn(bootstrap, "spawn/crocodile_warm", Primal_Tags.SPAWNS_BROWN_CROCODILE, Primal_Entities.CROCODILE.get(), 3, 1, 1);


        //Flora
        registerVegetation(bootstrap, "feature/river_reeds", Primal_Tags.SPAWNS_RIVER_REEDS, Primal_WorldGen.RIVER_REEDS_PLACED);

        registerVegetation(bootstrap, "feature/seashells", Primal_Tags.SPAWNS_SEASHELLS, Primal_WorldGen.SEASHELLS_PLACED);

        registerVegetation(bootstrap, "feature/eagle_nest", Primal_Tags.SPAWNS_EAGLE, Primal_WorldGen.EAGLES_NEST_SPAWN);
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
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, id) // The registry name
        );
    }
}
