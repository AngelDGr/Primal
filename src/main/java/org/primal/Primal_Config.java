package org.primal;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class Primal_Config {

    // --- Bear ---
    public final ModConfigSpec.BooleanValue enableBearSingleSpawn;
    public final ModConfigSpec.IntValue bearSingleSpawnWeight;
    public final ModConfigSpec.IntValue bearSingleMinGroup;
    public final ModConfigSpec.IntValue bearSingleMaxGroup;
    public final ModConfigSpec.ConfigValue<List<? extends String>> bearSingleExtraBiomes;

    public final ModConfigSpec.BooleanValue enableBearGroupSpawn;
    public final ModConfigSpec.IntValue bearGroupSpawnWeight;
    public final ModConfigSpec.IntValue bearGroupMinGroup;
    public final ModConfigSpec.IntValue bearGroupMaxGroup;
    public final ModConfigSpec.ConfigValue<List<? extends String>> bearGroupExtraBiomes;

    // --- Crocodile ---
    public final ModConfigSpec.BooleanValue enableCrocodileNormalSpawn;
    public final ModConfigSpec.IntValue crocodileNormalSpawnWeight;
    public final ModConfigSpec.IntValue crocodileNormalMinGroup;
    public final ModConfigSpec.IntValue crocodileNormalMaxGroup;
    public final ModConfigSpec.ConfigValue<List<? extends String>> crocodileNormalExtraBiomes;

    public final ModConfigSpec.BooleanValue enableCrocodileWarmSpawn;
    public final ModConfigSpec.IntValue crocodileWarmSpawnWeight;
    public final ModConfigSpec.IntValue crocodileWarmMinGroup;
    public final ModConfigSpec.IntValue crocodileWarmMaxGroup;
    public final ModConfigSpec.ConfigValue<List<? extends String>> crocodileWarmExtraBiomes;

    // --- Shark ---
    public final ModConfigSpec.BooleanValue enableSingleSharkSpawn;
    public final ModConfigSpec.IntValue sharkSingleSpawnWeight;
    public final ModConfigSpec.IntValue sharkSingleMinGroup;
    public final ModConfigSpec.IntValue sharkSingleMaxGroup;
    public final ModConfigSpec.ConfigValue<List<? extends String>> sharkSingleExtraBiomes;

    public final ModConfigSpec.BooleanValue enableSharkGroupSpawn;
    public final ModConfigSpec.IntValue sharkGroupSpawnWeight;
    public final ModConfigSpec.IntValue sharkGroupMinGroup;
    public final ModConfigSpec.IntValue sharkGroupMaxGroup;
    public final ModConfigSpec.ConfigValue<List<? extends String>> sharkGroupExtraBiomes;

    // --- Replaced Models ---
    public final ModConfigSpec.BooleanValue polarBearModelChange;
    public final ModConfigSpec.BooleanValue polarBearIncreasesHealth;
    public final ModConfigSpec.BooleanValue foxModelChange;

    // --- Special Config ---
    public final ModConfigSpec.ConfigValue<List<? extends List<? extends String>>> eggData;
    public final ModConfigSpec.ConfigValue<List<? extends String>> extraPlaceableEggs;

    // --- World Features ---
    public final ModConfigSpec.BooleanValue riverReedsSpawnInWorld;
    public final ModConfigSpec.IntValue riverReedsPatchRarity;
    public final ModConfigSpec.IntValue riverReedsPatchSpreadXZ;
    public final ModConfigSpec.IntValue riverReedsPatchSpreadY;
    public final ModConfigSpec.ConfigValue<List<? extends String>> riverReedsExtraBiomes;

    public final ModConfigSpec.BooleanValue seaShellsSpawnInWorld;
    public final ModConfigSpec.IntValue seaShellsPatchRarity;
    public final ModConfigSpec.IntValue seaShellsPatchTries;
    public final ModConfigSpec.IntValue seaShellsPatchSpreadXZ;
    public final ModConfigSpec.IntValue seaShellsPatchSpreadY;
    public final ModConfigSpec.ConfigValue<List<? extends String>> seaShellsExtraBiomes;

    public final ModConfigSpec.BooleanValue eagleNestSpawnInWorld;
    public final ModConfigSpec.IntValue eagleNestRarity;
    public final ModConfigSpec.IntValue eagleNestTries;
    public final ModConfigSpec.IntValue eagleNestSpreadXZ;
    public final ModConfigSpec.IntValue eagleNestSpreadY;
    public final ModConfigSpec.ConfigValue<List<? extends String>> eagleNestExtraBiomes;


    public Primal_Config(ModConfigSpec.Builder builder) {

        //Misc
        builder.comment("Misc Settings").push("misc");
        {
            eggData = builder
                    .comment("""
                            List of egg data: each entry is [block_id, entity_id].
                            This is used for the nest block, for eggs that can hatch, add the block to the animal_egg tag to be placeable or to the next config
                            Primal eggs are already defined""")
                    .defineList("eggData",
                            List.of(
                                    List.of("minecraft:turtle_egg", "minecraft:turtle"),
                                    List.of("minecraft:sniffer_egg", "minecraft:sniffer"),
                                    List.of("nomansland:tortoise_egg", "nomansland:tortoise")
                            ),
                            obj -> {
                                if (!(obj instanceof List<?> inner)) return false;
                                if (inner.size() != 2) return false;
                                return inner.get(0) instanceof String && inner.get(1) instanceof String;
                            });

            builder.comment("""
                        To add egg blocks directly instead of modifying the animal_egg tag""");
            extraPlaceableEggs = builder.defineList("extraPlaceableEggs",
                    List.of(),
                    o -> o instanceof String s && s.contains(":")
            );
        }
        builder.pop();

        //Mobs
        builder.comment("Mob Settings").push("mob");
        {
            // --- Replaced Models ---
            builder.push("replaced_models");
            {
                builder.comment("Toggle model replacements for certain mobs");
                polarBearModelChange = builder.define("polarBearModelChange", true);
                polarBearIncreasesHealth = builder.define("polarBearIncreasesHealth", true);
                foxModelChange = builder.define("foxModelChange", true);
            }
            builder.pop();

            builder.comment("""
                    Primal Mobs Spawn - tweak natural spawn
                    (Eagles only spawn on nests, tweak the nest spawn in world section)""");

            // --- Bear ---
            builder.push("bear");
            {
                builder.push("single");
                {
                    builder.comment("""
                            Enable or disable bear single spawning
                            Default=true""");
                    enableBearSingleSpawn = builder.define("enableBearSingleSpawn", true);

                    builder.comment("""
                            Spawn weight for single bears (higher = more common)
                            Default=20""");
                    bearSingleSpawnWeight = builder.defineInRange("bearSingleSpawnWeight", 20, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Minimum group size for single bears, it MUST be lesser than max group size
                            Default=1""");
                    bearSingleMinGroup = builder.defineInRange("bearSingleMinGroup", 1, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Maximum group size for single bears, it MUST be greater than min group size
                            Default=1""");
                    bearSingleMaxGroup = builder.defineInRange("bearSingleMaxGroup", 1, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Extra biomes (separated from biome tag) where single bears can spawn.
                            Must be written as full biome IDs (e.g. "minecraft:plains", "nomansland:maple_forest")
                            You can use biome tags too, just put a # before (e.g. "#minecraft:is_forest", "#nomansland:is_river")
                            Default = []""");
                    bearSingleExtraBiomes = builder.defineList("bearSingleExtraBiomes",
                            List.of(),
                            o -> o instanceof String s && s.contains(":")
                    );
                }
                builder.pop();

                builder.push("group");
                {
                    builder.comment("""
                            Enable or disable bear group spawning (with babies)
                            Default=true""");
                    enableBearGroupSpawn = builder.define("enableBearGroupSpawn", true);

                    builder.comment("""
                            Spawn weight for bear groups (higher = more common)
                            Default=5""");
                    bearGroupSpawnWeight = builder.defineInRange("bearGroupSpawnWeight", 5, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Minimum group size for bear groups, it MUST be lesser than max group size
                            Default=2""");
                    bearGroupMinGroup = builder.defineInRange("bearGroupMinGroup", 2, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Maximum group size for bear groups, it MUST be greater than min group size
                            Default=2""");
                    bearGroupMaxGroup = builder.defineInRange("bearGroupMaxGroup", 2, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Extra biomes (separated from biome tag) where bear groups can spawn.
                            Must be written as full biome IDs (e.g. "minecraft:plains", "nomansland:maple_forest")
                            You can use biome tags too, just put a # before (e.g. "#minecraft:is_forest", "#nomansland:is_river")
                            Default = []""");
                    bearGroupExtraBiomes = builder.defineList("bearGroupExtraBiomes",
                            List.of(),
                            o -> o instanceof String s && s.contains(":")
                    );
                }
                builder.pop();
            }
            builder.pop();

            // --- Crocodile ---
            builder.push("crocodile");
            {
                builder.push("normal");
                {
                    builder.comment("""
                            Enable or disable crocodile spawning in swampy/jungle biomes
                            Default=true""");
                    enableCrocodileNormalSpawn = builder.define("enableCrocodileNormalSpawn", true);

                    builder.comment("""
                            Spawn weight for crocodiles in swampy/jungle biomes (higher = more common)
                            Default=20""");
                    crocodileNormalSpawnWeight = builder.defineInRange("crocodileNormalSpawnWeight", 20, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Minimum group size for crocodiles in swampy/jungle biomes, it MUST be lesser than max group size
                            Default=1""");
                    crocodileNormalMinGroup = builder.defineInRange("crocodileNormalMinGroup", 1, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Maximum group size for crocodiles in swampy/jungle biomes, it MUST be greater than min group size
                            Default=1""");
                    crocodileNormalMaxGroup = builder.defineInRange("crocodileNormalMaxGroup", 1, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Extra biomes (separated from biome tag) where normal crocodiles can spawn.
                            Must be written as full biome IDs (e.g. "minecraft:plains", "nomansland:maple_forest")
                            You can use biome tags too, just put a # before (e.g. "#minecraft:is_forest", "#nomansland:is_river")
                            Default = []""");
                    crocodileNormalExtraBiomes = builder.defineList("crocodileNormalExtraBiomes",
                            List.of(),
                            o -> o instanceof String s && s.contains(":")
                    );
                }
                builder.pop();

                builder.push("warm");
                {
                    builder.comment("""
                            Enable or disable crocodile spawning in warm biomes
                            Default=true""");
                    enableCrocodileWarmSpawn = builder.define("enableCrocodileWarmSpawn", true);

                    builder.comment("""
                            Spawn weight for crocodiles in warm biomes (higher = more common)
                            Default=3""");
                    crocodileWarmSpawnWeight = builder.defineInRange("crocodileWarmSpawnWeight", 3, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Minimum group size for crocodiles in warm biomes, it MUST be lesser than max group size
                            Default=1""");
                    crocodileWarmMinGroup = builder.defineInRange("crocodileWarmMinGroup", 1, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Maximum group size for crocodiles in warm biomes, it MUST be greater than min group size
                            Default=1""");
                    crocodileWarmMaxGroup = builder.defineInRange("crocodileWarmMaxGroup", 1, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Extra biomes (separated from biome tag) where warm crocodiles can spawn
                            Must be written as full biome IDs (e.g. "minecraft:plains", "nomansland:maple_forest")
                            You can use biome tags too, just put a # before (e.g. "#minecraft:is_forest", "#nomansland:is_river")
                            Default = []""");
                    crocodileWarmExtraBiomes = builder.defineList("crocodileWarmExtraBiomes",
                            List.of(),
                            o -> o instanceof String s && s.contains(":")
                    );
                }
                builder.pop();
            }
            builder.pop();

            // --- Shark ---
            builder.push("shark");
            {
                builder.push("single");
                {
                    builder.comment("""
                            Enable or disable single shark spawning
                            Default=true""");
                    enableSingleSharkSpawn = builder.define("enableSingleSharkSpawn", true);

                    builder.comment("""
                            Spawn weight for single sharks (higher = more common)
                            Default=5""");
                    sharkSingleSpawnWeight = builder.defineInRange("sharkSingleSpawnWeight", 5, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Minimum group size for single sharks, it MUST be lesser than max group size
                            Default=1""");
                    sharkSingleMinGroup = builder.defineInRange("sharkSingleMinGroup", 1, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Maximum group size for single sharks, it MUST be greater than min group size
                            Default=1""");
                    sharkSingleMaxGroup = builder.defineInRange("sharkSingleMaxGroup", 1, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Extra biomes (separated from biome tag) where single sharks can spawn.
                            Must be written as full biome IDs (e.g. "minecraft:plains", "nomansland:maple_forest")
                            You can use biome tags too, just put a # before (e.g. "#minecraft:is_forest", "#nomansland:is_river")
                            Default = []""");
                    sharkSingleExtraBiomes = builder.defineList("sharkSingleExtraBiomes",
                            List.of(),
                            o -> o instanceof String s && s.contains(":")
                    );
                }
                builder.pop();

                builder.push("group");
                {
                    builder.comment("""
                            Enable or disable group shark spawning
                            Default=true""");
                    enableSharkGroupSpawn = builder.define("enableSharkGroupSpawn", true);

                    builder.comment("""
                            Spawn weight for shark groups (higher = more common)
                            Default=1""");
                    sharkGroupSpawnWeight = builder.defineInRange("sharkGroupSpawnWeight", 1, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Minimum group size for shark groups, it MUST be lesser than max group size
                            Default=1""");
                    sharkGroupMinGroup = builder.defineInRange("sharkGroupMinGroup", 1, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Maximum group size for shark groups, it MUST be greater than min group size
                            Default=3""");
                    sharkGroupMaxGroup = builder.defineInRange("sharkGroupMaxGroup", 3, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Extra biomes (separated from biome tag) where shark groups can spawn.
                            Must be written as full biome IDs (e.g. "minecraft:plains", "nomansland:maple_forest")
                            You can use biome tags too, just put a # before (e.g. "#minecraft:is_forest", "#nomansland:is_river")
                            Default = []""");
                    sharkGroupExtraBiomes = builder.defineList("sharkGroupExtraBiomes",
                            List.of(),
                            o -> o instanceof String s && s.contains(":")
                    );
                }
                builder.pop();
            }
            builder.pop();

        }
        builder.pop();



        // --- World Features ---
        builder.comment("World Settings").push("world");
        {
            // --- River Reeds ---
            builder.push("river_reeds");
            {
                builder.comment("""
                        Enable or disable river reeds spawning
                        Default=true""");
                riverReedsSpawnInWorld = builder.define("riverReedsSpawnInWorld", true);

                builder.comment("""
                        Patch rarity for river reeds
                        Default=2""");
                riverReedsPatchRarity = builder.defineInRange("riverReedsPatchRarity", 2, 0, Integer.MAX_VALUE);

                builder.comment("""
                        Patch horizontal spread (XZ) for river reeds
                        Default=8""");
                riverReedsPatchSpreadXZ = builder.defineInRange("riverReedsPatchSpreadXZ", 8, 0, Integer.MAX_VALUE);

                builder.comment("""
                        Patch vertical spread (Y) for river reeds
                        Default=5""");
                riverReedsPatchSpreadY = builder.defineInRange("riverReedsPatchSpreadY", 5, 0, Integer.MAX_VALUE);

                builder.comment("""
                        Extra biomes (separated from biome tag) where river reeds can spawn.
                        Must be written as full biome IDs (e.g. "minecraft:plains", "nomansland:maple_forest")
                        You can use biome tags too, just put a # before (e.g. "#minecraft:is_forest", "#nomansland:is_river")
                        Default = []""");
                riverReedsExtraBiomes = builder.defineList("riverReedsExtraBiomes",
                        List.of(),
                        o -> o instanceof String s && s.contains(":")
                );
            }
            builder.pop();

            // --- Seashells ---
            builder.push("seashells");
            {
                builder.comment("""
                        Enable or disable seashells spawning
                        Default=true""");
                seaShellsSpawnInWorld = builder.define("seaShellsSpawnInWorld", true);

                builder.comment("""
                        Patch rarity for seashells
                        Default=5""");
                seaShellsPatchRarity = builder.defineInRange("seaShellsPatchRarity", 5, 0, Integer.MAX_VALUE);

                builder.comment("""
                        Number of tries per patch for seashells
                        Default=96""");
                seaShellsPatchTries = builder.defineInRange("seaShellsPatchTries", 96, 0, Integer.MAX_VALUE);

                builder.comment("""
                        Patch horizontal spread (XZ) for seashells
                        Default=6""");
                seaShellsPatchSpreadXZ = builder.defineInRange("seaShellsPatchSpreadXZ", 6, 0, Integer.MAX_VALUE);

                builder.comment("""
                        Patch vertical spread (Y) for seashells
                        Default=2""");
                seaShellsPatchSpreadY = builder.defineInRange("seaShellsPatchSpreadY", 2, 0, Integer.MAX_VALUE);

                builder.comment("""
                        Extra biomes (separated from biome tag) where seashells can spawn.
                        Must be written as full biome IDs (e.g. "minecraft:plains", "nomansland:maple_forest")
                        You can use biome tags too, just put a # before (e.g. "#minecraft:is_forest", "#nomansland:is_river")
                        Default = []""");
                seaShellsExtraBiomes = builder.defineList("seaShellsExtraBiomes",
                        List.of(),
                        o -> o instanceof String s && s.contains(":")
                );
            }
            builder.pop();

            // --- Eagle Nests ---
            builder.push("eagle_nest");
            {
                builder.comment("""
                        Enable or disable eagle nest spawning
                        Default=true""");
                eagleNestSpawnInWorld = builder.define("eagleNestSpawnInWorld", true);

                builder.comment("""
                        Rarity of eagle nests (higher = rarer)
                        Default=30""");
                eagleNestRarity = builder.defineInRange("eagleNestRarity", 30, 0, Integer.MAX_VALUE);

                builder.comment("""
                        Number of tries per patch for eagle nests
                        Default=12""");
                eagleNestTries = builder.defineInRange("eagleNestTries", 12, 0, Integer.MAX_VALUE);

                builder.comment("""
                        Patch horizontal spread (XZ) for eagle nests
                        Default=1""");
                eagleNestSpreadXZ = builder.defineInRange("eagleNestSpreadXZ", 1, 0, Integer.MAX_VALUE);

                builder.comment("""
                        Patch vertical spread (Y) for eagle nests
                        Default=0""");
                eagleNestSpreadY = builder.defineInRange("eagleNestSpreadY", 0, 0, Integer.MAX_VALUE);

                builder.comment("""
                        Extra biomes (separated from biome tag) where eagle nests can spawn.
                        Must be written as full biome IDs (e.g. "minecraft:plains", "nomansland:maple_forest")
                        You can use biome tags too, just put a # before (e.g. "#minecraft:is_forest", "#nomansland:is_river")
                        Default = []""");
                eagleNestExtraBiomes = builder.defineList("eagleNestExtraBiomes",
                        List.of(),
                        o -> o instanceof String s && s.contains(":")
                );
            }
            builder.pop();
        }
        builder.pop();

    }
}
