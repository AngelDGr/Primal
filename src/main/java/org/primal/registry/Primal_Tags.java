package org.primal.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.primal.Primal_Main;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public final class Primal_Tags {

    public static class BannerPattern {
        public static final TagKey<net.minecraft.world.level.block.entity.BannerPattern> PATTERN_ITEM_PAW = createTag(Registries.BANNER_PATTERN, "pattern_item/paw");
        public static final TagKey<net.minecraft.world.level.block.entity.BannerPattern> PATTERN_ITEM_JAWS = createTag(Registries.BANNER_PATTERN, "pattern_item/jaws");
        public static final TagKey<net.minecraft.world.level.block.entity.BannerPattern> PATTERN_ITEM_MARSH = createTag(Registries.BANNER_PATTERN, "pattern_item/marsh");
        public static final TagKey<net.minecraft.world.level.block.entity.BannerPattern> PATTERN_ITEM_EYRIE = createTag(Registries.BANNER_PATTERN, "pattern_item/eyrie");
    }

    public static class Entity {
        public static final TagKey<EntityType<?>> BEAR_HUNTABLE = createTag(Registries.ENTITY_TYPE, "bear_huntable");
        public static final TagKey<EntityType<?>> SHARK_HUNTABLE = createTag(Registries.ENTITY_TYPE, "shark_huntable");
        public static final TagKey<EntityType<?>> CROCODILE_HUNTABLE = createTag(Registries.ENTITY_TYPE, "crocodile_huntable");
        public static final TagKey<EntityType<?>> EAGLE_HUNTABLE = createTag(Registries.ENTITY_TYPE, "eagle_huntable");

        public static final TagKey<EntityType<?>> NEVER_ATTACK = createTag(Registries.ENTITY_TYPE, "never_attack");
        public static final TagKey<EntityType<?>> CROCODILE_NEVER_ATTACK = createTag(Registries.ENTITY_TYPE, "crocodile_never_attack");
    }

    public static class Block {
        public static final TagKey<net.minecraft.world.level.block.Block> BEAR_REPELLENTS = createTag(Registries.BLOCK, "bear_repellents");
        public static final TagKey<net.minecraft.world.level.block.Block> SHARK_ATTRACTORS = createTag(Registries.BLOCK, "shark_attractors");
        public static final TagKey<net.minecraft.world.level.block.Block> CROCODILE_ATTRACTORS = createTag(Registries.BLOCK, "crocodile_attractors");

        public static final TagKey<net.minecraft.world.level.block.Block> IS_ANIMAL_EGG = createTag(Registries.BLOCK, "animal_egg");
        public static final TagKey<net.minecraft.world.level.block.Block> RIVER_REED_SOIL = createTag(Registries.BLOCK, "river_reed_soil");
    }

    public static class Item {

        public static final TagKey<net.minecraft.world.item.Item> CROCODILE_CANT_EAT = createTag(Registries.ITEM, "crocodile_cant_eat");
        public static final TagKey<net.minecraft.world.item.Item> STRAW = createTag(Registries.ITEM, "straw");
        public static final TagKey<net.minecraft.world.item.Item> BEAR_HEALING_TREATS = createTag(Registries.ITEM, "bear_healing_treats");

        public static final TagKey<net.minecraft.world.item.Item> MAKES_CROCODILE_EXPLODE = createTag(Registries.ITEM, "crocodile_explosive");
        public static final TagKey<net.minecraft.world.item.Item> MAKES_CROCODILE_TICK_TOCK = createTag(Registries.ITEM, "crocodile_tick_tock");
    }

    public static class Biome {

        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_BEAR = createTag(Registries.BIOME, "has_mob/bear");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_BLACK_BEAR = createTag(Registries.BIOME, "has_mob/has_variant/black_bear_variant");

        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_SHARK = createTag(Registries.BIOME, "has_mob/shark");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_TIGER_SHARK = createTag(Registries.BIOME, "has_mob/has_variant/tiger_shark_variant");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_HAMMERHEAD = createTag(Registries.BIOME, "has_mob/has_variant/hammerhead_variant");

        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_CROCODILE = createTag(Registries.BIOME, "has_mob/crocodile");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_BLACK_CROCODILE = createTag(Registries.BIOME, "has_mob/has_variant/black_crocodile_variant");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_BROWN_CROCODILE = createTag(Registries.BIOME, "has_mob/has_variant/brown_crocodile_variant");

        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_EAGLE = createTag(Registries.BIOME, "has_mob/eagle");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_GOLDEN_EAGLE = createTag(Registries.BIOME, "has_mob/has_variant/golden_eagle_variant");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_HARPY_EAGLE = createTag(Registries.BIOME, "has_mob/has_variant/harpy_eagle_variant");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_PHILIPPINE_EAGLE = createTag(Registries.BIOME, "has_mob/has_variant/philippine_eagle_variant");

        //Flora
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_RIVER_REEDS = createTag(Registries.BIOME, "has_flora/river_reeds");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_SEASHELLS = createTag(Registries.BIOME, "has_flora/seashells");
    }

    private static <T> TagKey<T> createTag(ResourceKey<Registry<T>> registryResourceKey, String name) {
        return createTag(registryResourceKey, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, name));
    }

    private static <T> TagKey<T> createTag(ResourceKey<Registry<T>> registryResourceKey, ResourceLocation resourceLocation) {
        return TagKey.create(registryResourceKey, resourceLocation);
    }
}
