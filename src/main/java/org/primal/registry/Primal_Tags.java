package org.primal.registry;


import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import org.primal.Primal_Main;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public final class Primal_Tags {

    public static final TagKey<EntityType<?>> BEAR_HUNTABLE = createTag(Registries.ENTITY_TYPE, "bear_huntable");
    public static final TagKey<EntityType<?>> SHARK_HUNTABLE = createTag(Registries.ENTITY_TYPE, "shark_huntable");
    public static final TagKey<EntityType<?>> CROCODILE_HUNTABLE = createTag(Registries.ENTITY_TYPE, "crocodile_huntable");

    public static final TagKey<Block> BEAR_REPELLENTS = createTag(Registries.BLOCK, "bear_repellents");
    public static final TagKey<Block> SHARK_ATTRACTORS = createTag(Registries.BLOCK, "shark_attractors");
    public static final TagKey<Block> CROCODILE_ATTRACTORS = createTag(Registries.BLOCK, "crocodile_attractors");

    public static final TagKey<Item> CROCODILE_CANT_EAT = createTag(Registries.ITEM, "crocodile_cant_eat");


    public static final TagKey<Biome> SPAWNS_BEAR = createTag(Registries.BIOME, "has_mob/bear");
    public static final TagKey<Biome> SPAWNS_BLACK_BEAR = createTag(Registries.BIOME, "has_mob/has_variant/black_bear_variant");

    public static final TagKey<Biome> SPAWNS_SHARK = createTag(Registries.BIOME, "has_mob/shark");
    public static final TagKey<Biome> SPAWNS_TIGER_SHARK = createTag(Registries.BIOME, "has_mob/has_variant/tiger_shark_variant");
    public static final TagKey<Biome> SPAWNS_HAMMERHEAD = createTag(Registries.BIOME, "has_mob/has_variant/hammerhead_variant");

    public static final TagKey<Biome> SPAWNS_CROCODILE = createTag(Registries.BIOME, "has_mob/crocodile");
    public static final TagKey<Biome> SPAWNS_BLACK_CROCODILE = createTag(Registries.BIOME, "has_mob/black_crocodile_variant");
    public static final TagKey<Biome> SPAWNS_BROWN_CROCODILE = createTag(Registries.BIOME, "has_mob/brown_crocodile_variant");

    public static final TagKey<Biome> SPAWNS_RIVER_REEDS = createTag(Registries.BIOME, "has_flora/river_reeds");
    public static final TagKey<Biome> SPAWNS_SEASHELLS = createTag(Registries.BIOME, "has_flora/seashells");

    private static <T> TagKey<T> createTag(ResourceKey<Registry<T>> registryResourceKey, String name) {
        return createTag(registryResourceKey, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, name));
    }

    private static <T> TagKey<T> createTag(ResourceKey<Registry<T>> registryResourceKey, ResourceLocation resourceLocation) {
        return TagKey.create(registryResourceKey, resourceLocation);
    }
}
