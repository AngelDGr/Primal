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
        public static final TagKey<net.minecraft.world.level.block.entity.BannerPattern> PATTERN_ITEM_SLITHER = createTag(Registries.BANNER_PATTERN, "pattern_item/slither");
        public static final TagKey<net.minecraft.world.level.block.entity.BannerPattern> PATTERN_ITEM_ROYAL = createTag(Registries.BANNER_PATTERN, "pattern_item/royal");
    }

    public static class Entity {
        public static final TagKey<EntityType<?>> BEAR_HUNTABLE = createTag(Registries.ENTITY_TYPE, "bear_huntable");
        public static final TagKey<EntityType<?>> SHARK_HUNTABLE = createTag(Registries.ENTITY_TYPE, "shark_huntable");
        public static final TagKey<EntityType<?>> CROCODILE_HUNTABLE = createTag(Registries.ENTITY_TYPE, "crocodile_huntable");
        public static final TagKey<EntityType<?>> EAGLE_HUNTABLE = createTag(Registries.ENTITY_TYPE, "eagle_huntable");
        public static final TagKey<EntityType<?>> CASSOWARY_HUNTABLE = createTag(Registries.ENTITY_TYPE, "cassowary_huntable");
        public static final TagKey<EntityType<?>> LION_HUNTABLE = createTag(Registries.ENTITY_TYPE, "lion_huntable");
        public static final TagKey<EntityType<?>> SNAKE_HUNTABLE = createTag(Registries.ENTITY_TYPE, "snake_huntable");
        public static final TagKey<EntityType<?>> DEER_SCARED = createTag(Registries.ENTITY_TYPE, "deer_scared");
        public static final TagKey<EntityType<?>> DEER_VERY_SCARED = createTag(Registries.ENTITY_TYPE, "deer_very_scared");

        public static final TagKey<EntityType<?>> NEVER_ATTACK = createTag(Registries.ENTITY_TYPE, "never_attack");
        public static final TagKey<EntityType<?>> CROCODILE_NEVER_ATTACK = createTag(Registries.ENTITY_TYPE, "crocodile_never_attack");
    }

    public static class Block {
        public static final TagKey<net.minecraft.world.level.block.Block> BEAR_REPELLENTS = createTag(Registries.BLOCK, "bear_repellents");
        public static final TagKey<net.minecraft.world.level.block.Block> SHARK_ATTRACTORS = createTag(Registries.BLOCK, "shark_attractors");
        public static final TagKey<net.minecraft.world.level.block.Block> CROCODILE_ATTRACTORS = createTag(Registries.BLOCK, "crocodile_attractors");

        public static final TagKey<net.minecraft.world.level.block.Block> IS_ANIMAL_EGG = createTag(Registries.BLOCK, "animal_egg");
        public static final TagKey<net.minecraft.world.level.block.Block> RIVER_REED_SOIL = createTag(Registries.BLOCK, "river_reed_soil");

        public static final TagKey<net.minecraft.world.level.block.Block> FRUIT_TREE_PLANTABLE_ON = createTag(Registries.BLOCK, "fruit_tree_plantable_on");
        public static final TagKey<net.minecraft.world.level.block.Block> FRUIT_TREE_GROW_FAST_ON = createTag(Registries.BLOCK, "fruit_tree_grow_fast_on");

        public static final TagKey<net.minecraft.world.level.block.Block> CASSOWARY_NEST_CAN_SPAWN_ON = createTag(Registries.BLOCK, "cassowary_nest_can_spawn_on");

        public static final TagKey<net.minecraft.world.level.block.Block> WALRUS_SPAWN_ON = createTag(Registries.BLOCK, "walrus_spawn_on");
        public static final TagKey<net.minecraft.world.level.block.Block> WALRUS_SLAM_CAN_BREAK = createTag(Registries.BLOCK, "walrus_slam_can_break");

        public static final TagKey<net.minecraft.world.level.block.Block> SNAKE_SPAWN_ON = createTag(Registries.BLOCK, "snake_spawn_on");
        public static final TagKey<net.minecraft.world.level.block.Block> DEER_SPAWN_ON = createTag(Registries.BLOCK, "deer_spawn_on");

        public static final TagKey<net.minecraft.world.level.block.Block> CREATES_WIDE_SMOKE = createTag(Registries.BLOCK, "creates_wide_smoke");

        public static final TagKey<net.minecraft.world.level.block.Block> HOLLOW_LOGS = createTag(Registries.BLOCK, "hollow_logs");

        public static final TagKey<net.minecraft.world.level.block.Block> NEVER_OBSTRUCT_NEST = createTag(Registries.BLOCK, "never_obstruct_nest");
    }

    public static class Item {

        public static final TagKey<net.minecraft.world.item.Item> CROCODILE_CANT_EAT = createTag(Registries.ITEM, "crocodile_cant_eat");
        public static final TagKey<net.minecraft.world.item.Item> STRAW = createTag(Registries.ITEM, "straw");
        public static final TagKey<net.minecraft.world.item.Item> BEAR_HEALING_TREATS = createTag(Registries.ITEM, "bear_healing_treats");

        public static final TagKey<net.minecraft.world.item.Item> MAKES_CROCODILE_EXPLODE = createTag(Registries.ITEM, "crocodile_explosive");
        public static final TagKey<net.minecraft.world.item.Item> MAKES_CROCODILE_TICK_TOCK = createTag(Registries.ITEM, "crocodile_tick_tock");

        public static final TagKey<net.minecraft.world.item.Item> SEASHELLS = createTag(Registries.ITEM, "seashells");

        public static final TagKey<net.minecraft.world.item.Item> EXOTIC_FRUITS = createTag(Registries.ITEM, "exotic_fruits");
        public static final TagKey<net.minecraft.world.item.Item> DISTRACTS_CASSOWARY = createTag(Registries.ITEM, "distracts_cassowary");
        public static final TagKey<net.minecraft.world.item.Item> PROCESSES_CASSOWARY = createTag(Registries.ITEM, "processes_cassowary");

        public static final TagKey<net.minecraft.world.item.Item> WALRUS_INSTRUMENT = createTag(Registries.ITEM, "walrus_instrument");

        public static final TagKey<net.minecraft.world.item.Item> HOLLOW_LOGS = createTag(Registries.ITEM, "hollow_logs");

        public static final TagKey<net.minecraft.world.item.Item> SNAKE_EDIBLE_EGGS = createTag(Registries.ITEM, "snake_edible_eggs");

        public static final TagKey<net.minecraft.world.item.Item> DEER_ANTLERS = createTag(Registries.ITEM, "deer_antlers");
        public static final TagKey<net.minecraft.world.item.Item> HELMET_ATTACHMENTS = createTag(Registries.ITEM, "helmet_attachments");
    }

    public static class Biome {

        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_BEAR = createTag(Registries.BIOME, "has_mob/bear");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_BLACK_BEAR = createTag(Registries.BIOME, "has_mob/has_variant/bear/black");

        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_SHARK = createTag(Registries.BIOME, "has_mob/shark");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_TIGER_SHARK = createTag(Registries.BIOME, "has_mob/has_variant/shark/tiger");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_HAMMERHEAD = createTag(Registries.BIOME, "has_mob/has_variant/shark/hammerhead");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_MACKEREL_SHARK = createTag(Registries.BIOME, "has_mob/has_variant/shark/mackerel");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_SLEEPER_SHARK = createTag(Registries.BIOME, "has_mob/has_variant/shark/sleeper");

        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_CROCODILE = createTag(Registries.BIOME, "has_mob/crocodile");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_BLACK_CROCODILE = createTag(Registries.BIOME, "has_mob/has_variant/crocodile/black");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_BROWN_CROCODILE = createTag(Registries.BIOME, "has_mob/has_variant/crocodile/brown");

        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_EAGLE = createTag(Registries.BIOME, "has_mob/eagle");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_GOLDEN_EAGLE = createTag(Registries.BIOME, "has_mob/has_variant/eagle/golden");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_HARPY_EAGLE = createTag(Registries.BIOME, "has_mob/has_variant/eagle/harpy");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_PHILIPPINE_EAGLE = createTag(Registries.BIOME, "has_mob/has_variant/eagle/philippine");

        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_CASSOWARY = createTag(Registries.BIOME, "has_mob/cassowary");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_SUNSET_CASSOWARY = createTag(Registries.BIOME, "has_mob/has_variant/cassowary/sunset");

        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_WALRUS = createTag(Registries.BIOME, "has_mob/walrus");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_WALRUS_OCEAN = createTag(Registries.BIOME, "has_mob/has_habitat/walrus/ocean");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_SUNBURNT_WALRUS = createTag(Registries.BIOME, "has_mob/has_variant/walrus/sunburnt");

        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_LION = createTag(Registries.BIOME, "has_mob/lion");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_CARAMEL_LION = createTag(Registries.BIOME, "has_mob/has_variant/lion/caramel");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_CAVE_LION = createTag(Registries.BIOME, "has_mob/has_variant/lion/cave");

        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_SNAKE = createTag(Registries.BIOME, "has_mob/snake");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_VERDANT_SNAKE = createTag(Registries.BIOME, "has_mob/has_variant/snake/verdant");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_LUMBER_SNAKE = createTag(Registries.BIOME, "has_mob/has_variant/snake/lumber");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_TENEBROUS_SNAKE = createTag(Registries.BIOME, "has_mob/has_variant/snake/tenebrous");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_DUSTY_SNAKE = createTag(Registries.BIOME, "has_mob/has_variant/snake/dusty");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_BROWED_SNAKE = createTag(Registries.BIOME, "has_mob/has_variant/snake/browed");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_CERULEAN_SNAKE = createTag(Registries.BIOME, "has_mob/has_variant/snake/cerulean");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_BRACKISH_SNAKE = createTag(Registries.BIOME, "has_mob/has_variant/snake/brackish");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_APOSEMATIC_SNAKE = createTag(Registries.BIOME, "has_mob/has_variant/snake/aposematic");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_MARINE_SNAKE = createTag(Registries.BIOME, "has_mob/has_variant/snake/marine");

        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_DEER = createTag(Registries.BIOME, "has_mob/deer");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_MUSK_DEER = createTag(Registries.BIOME, "has_mob/has_variant/deer/musk");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_REINDEER = createTag(Registries.BIOME, "has_mob/has_variant/deer/reindeer");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_WHITETAIL_DEER = createTag(Registries.BIOME, "has_mob/has_variant/deer/whitetail");

        //Vanilla
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_DOLPHIN_WARM = createTag(Registries.BIOME, "has_mob/has_variant/dolphin/warm");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_DOLPHIN_COLD = createTag(Registries.BIOME, "has_mob/has_variant/dolphin/cold");

        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_RABBIT_GRAY = createTag(Registries.BIOME, "has_mob/has_variant/rabbit/gray");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_RABBIT_SAND = createTag(Registries.BIOME, "has_mob/has_variant/rabbit/sand");

        //Flora
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_RIVER_REEDS = createTag(Registries.BIOME, "has_flora/river_reeds");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_CATTAILS = createTag(Registries.BIOME, "has_flora/cattails");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_SEASHELLS = createTag(Registries.BIOME, "has_flora/seashells");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_SEASHELLS_COLD = createTag(Registries.BIOME, "has_flora/has_variant/seashells/cold_seashells");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_SEASHELLS_TEMPERATE = createTag(Registries.BIOME, "has_flora/has_variant/seashells/temperate_seashells");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_THORNY_ACACIA = createTag(Registries.BIOME, "has_flora/thorned_acacia");

        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_HOLLOW_TREE = createTag(Registries.BIOME, "has_flora/hollow_tree");
    }

    public static class GameEvent {
        public static final TagKey<net.minecraft.world.level.gameevent.GameEvent> SCARE_DEER = createTag(Registries.GAME_EVENT, "scare_deer");
    }

    private static <T> TagKey<T> createTag(ResourceKey<Registry<T>> registryResourceKey, String name) {
        return createTag(registryResourceKey, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, name));
    }

    private static <T> TagKey<T> createTag(ResourceKey<Registry<T>> registryResourceKey, ResourceLocation resourceLocation) {
        return TagKey.create(registryResourceKey, resourceLocation);
    }
}
