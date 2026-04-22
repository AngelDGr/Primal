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

    // --- Walrus ---
    public final ModConfigSpec.BooleanValue enableWalrusCoastSpawn;
    public final ModConfigSpec.IntValue walrusCoastSpawnWeight;
    public final ModConfigSpec.IntValue walrusCoastMinGroup;
    public final ModConfigSpec.IntValue walrusCoastMaxGroup;
    public final ModConfigSpec.ConfigValue<List<? extends String>> walrusCoastExtraBiomes;

    public final ModConfigSpec.BooleanValue enableWalrusOceanSpawn;
    public final ModConfigSpec.IntValue walrusOceanSpawnWeight;
    public final ModConfigSpec.IntValue walrusOceanMinGroup;
    public final ModConfigSpec.IntValue walrusOceanMaxGroup;
    public final ModConfigSpec.ConfigValue<List<? extends String>> walrusOceanExtraBiomes;

    // --- Lion ---
    public final ModConfigSpec.BooleanValue enableLionSavannaSpawn;
    public final ModConfigSpec.IntValue lionSavannaSpawnWeight;
    public final ModConfigSpec.IntValue lionSavannaMinGroup;
    public final ModConfigSpec.IntValue lionSavannaMaxGroup;
    public final ModConfigSpec.ConfigValue<List<? extends String>> lionSavannaExtraBiomes;

    public final ModConfigSpec.BooleanValue enableLionSnowySpawn;
    public final ModConfigSpec.IntValue lionSnowySpawnWeight;
    public final ModConfigSpec.IntValue lionSnowyMinGroup;
    public final ModConfigSpec.IntValue lionSnowyMaxGroup;
    public final ModConfigSpec.ConfigValue<List<? extends String>> lionSnowyExtraBiomes;

    // --- Snake ---
    public final ModConfigSpec.BooleanValue enableSnakeSpawn;
    public final ModConfigSpec.IntValue snakeSpawnWeight;
    public final ModConfigSpec.IntValue snakeMinGroup;
    public final ModConfigSpec.IntValue snakeMaxGroup;
    public final ModConfigSpec.ConfigValue<List<? extends String>> snakeExtraBiomes;

    // --- Deer ---
    public final ModConfigSpec.BooleanValue enableDeerForestSpawn;
    public final ModConfigSpec.IntValue deerForestSpawnWeight;
    public final ModConfigSpec.IntValue deerForestMinGroup;
    public final ModConfigSpec.IntValue deerForestMaxGroup;
    public final ModConfigSpec.ConfigValue<List<? extends String>> deerForestExtraBiomes;

    public final ModConfigSpec.BooleanValue enableDeerSnowySpawn;
    public final ModConfigSpec.IntValue deerSnowySpawnWeight;
    public final ModConfigSpec.IntValue deerSnowyMinGroup;
    public final ModConfigSpec.IntValue deerSnowyMaxGroup;
    public final ModConfigSpec.ConfigValue<List<? extends String>> deerSnowyExtraBiomes;


    // --- Replaced Models ---
    public final ModConfigSpec.BooleanValue polarBearModelChange;
    public final ModConfigSpec.BooleanValue foxModelChange;
    public final ModConfigSpec.BooleanValue rabbitModelChange;
    public final ModConfigSpec.BooleanValue wolfModelChange;
    public final ModConfigSpec.BooleanValue dolphinModelChange;

    // --- Misc ---
    public final ModConfigSpec.BooleanValue polarBearIncreasesHealth;
    public final ModConfigSpec.BooleanValue foxIncreasesHealth;
    public final ModConfigSpec.BooleanValue wolfIncreasesHealth;
    public final ModConfigSpec.ConfigValue<List<? extends String>> wolfExtraModsSupportedForVariants;

    public final ModConfigSpec.BooleanValue bearBabyCustomModel;
    public final ModConfigSpec.BooleanValue crocodileBabyCustomModel;
//    public final ModConfigSpec.BooleanValue eagleBabyCustomModel;
//    public final ModConfigSpec.BooleanValue sharkBabyCustomModel;

    public final ModConfigSpec.BooleanValue cassowaryBabyCustomModel;
    public final ModConfigSpec.BooleanValue walrusBabyCustomModel;
    public final ModConfigSpec.BooleanValue lionBabyCustomModel;
    public final ModConfigSpec.BooleanValue snakeBabyCustomModel;
    public final ModConfigSpec.BooleanValue deerBabyCustomModel;

    // --- Dolphin ---
    public final ModConfigSpec.BooleanValue enableDolphinColdSpawn;
    public final ModConfigSpec.IntValue dolphinColdSpawnWeight;
    public final ModConfigSpec.IntValue dolphinColdMinGroup;
    public final ModConfigSpec.IntValue dolphinColdMaxGroup;
    public final ModConfigSpec.ConfigValue<List<? extends String>> dolphinColdExtraBiomes;

    // --- Rabbit ---
    public final ModConfigSpec.BooleanValue enableRabbitBadlandsSpawn;
    public final ModConfigSpec.IntValue rabbitBadlandsSpawnWeight;
    public final ModConfigSpec.IntValue rabbitBadlandsMinGroup;
    public final ModConfigSpec.IntValue rabbitBadlandsMaxGroup;
    public final ModConfigSpec.ConfigValue<List<? extends String>> rabbitBadlandsExtraBiomes;

    // --- Special Config ---
    public final ModConfigSpec.ConfigValue<List<? extends List<? extends String>>> eggData;
    public final ModConfigSpec.ConfigValue<List<? extends String>> extraPlaceableEggs;

    // --- World Features ---
    public final ModConfigSpec.BooleanValue riverReedsSpawnInWorld;
    public final ModConfigSpec.IntValue riverReedsPatchRarity;
    public final ModConfigSpec.IntValue riverReedsPatchSpreadXZ;
    public final ModConfigSpec.IntValue riverReedsPatchSpreadY;
    public final ModConfigSpec.ConfigValue<List<? extends String>> riverReedsExtraBiomes;

    public final ModConfigSpec.BooleanValue cattailsSpawnInWorld;
    public final ModConfigSpec.DoubleValue cattailsAlongsideRiverReedsProbability;
    public final ModConfigSpec.IntValue cattailsPatchRarity;
    public final ModConfigSpec.IntValue cattailsPatchSpreadXZ;
    public final ModConfigSpec.IntValue cattailsPatchSpreadY;
    public final ModConfigSpec.ConfigValue<List<? extends String>> cattailsExtraBiomes;

    public final ModConfigSpec.BooleanValue seaShellsSpawnInWorld;
    public final ModConfigSpec.IntValue seaShellsPatchRarity;
    public final ModConfigSpec.IntValue seaShellsPatchTries;
    public final ModConfigSpec.IntValue seaShellsPatchSpreadXZ;
    public final ModConfigSpec.IntValue seaShellsPatchSpreadY;
    public final ModConfigSpec.ConfigValue<List<? extends String>> seaShellsExtraBiomes;

    public final ModConfigSpec.BooleanValue thornyAcaciaSpawnInWorld;
    public final ModConfigSpec.DoubleValue thornyAcaciaProbabilityOfReplacing;
    public final ModConfigSpec.ConfigValue<List<? extends String>> thornyAcaciaExtraBiomes;

    public final ModConfigSpec.BooleanValue hollowOakLogSpawnInWorld;
    public final ModConfigSpec.BooleanValue hollowSpruceLogSpawnInWorld;
    public final ModConfigSpec.BooleanValue hollowBirchLogSpawnInWorld;
    public final ModConfigSpec.BooleanValue hollowJungleLogSpawnInWorld;
    public final ModConfigSpec.BooleanValue hollowAcaciaLogSpawnInWorld;
    public final ModConfigSpec.BooleanValue hollowDarkOakLogSpawnInWorld;
    public final ModConfigSpec.BooleanValue hollowMangroveLogSpawnInWorld;
    public final ModConfigSpec.ConfigValue<List<? extends Double>> hollowLogEmptyProbability;
    public final ModConfigSpec.ConfigValue<List<? extends Double>> hollowLogSnakeProbability;
    public final ModConfigSpec.ConfigValue<List<? extends String>> hollowTreesExtraBiomes;

    public final ModConfigSpec.BooleanValue eagleNestSpawnInWorld;
    public final ModConfigSpec.IntValue eagleNestRarity;
    public final ModConfigSpec.IntValue eagleNestTries;
    public final ModConfigSpec.IntValue eagleNestSpreadXZ;
    public final ModConfigSpec.IntValue eagleNestSpreadY;
    public final ModConfigSpec.ConfigValue<List<? extends String>> eagleNestExtraBiomes;

    public final ModConfigSpec.BooleanValue cassowaryNestSpawnInWorld;
    public final ModConfigSpec.IntValue cassowaryNestRarity;
    public final ModConfigSpec.IntValue cassowaryNestTries;
    public final ModConfigSpec.IntValue cassowaryNestSpreadXZ;
    public final ModConfigSpec.IntValue cassowaryNestSpreadY;
    public final ModConfigSpec.ConfigValue<List<? extends String>> cassowaryNestExtraBiomes;

    @SuppressWarnings("deprecation")
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
                foxModelChange = builder.define("foxModelChange", true);
                rabbitModelChange = builder.define("rabbitModelChange", true);
                wolfModelChange = builder.define("wolfModelChange", true);
                dolphinModelChange = builder.define("dolphinModelChange", true);
            }
            builder.pop();

            // --- Misc Changes ---
            builder.push("misc");
            {
                builder.comment("Multiple random options");
                polarBearIncreasesHealth = builder.define("polarBearIncreasesHealth", true);
                wolfIncreasesHealth = builder.define("wolfIncreasesHealth", true);
                foxIncreasesHealth = builder.define("foxIncreasesHealth", true);
                builder.comment("""
                            Extra mods with supported wolf variants. For custom resourcepacks
                            You must write the mod namespace here, after that Primal will read the custom texture files (e.g. wolfExtraModsSupportedForVariants = ["atmospheric", "environmental"] )
                            The new custom texture files must be located on "resources/assets/primal/textures/entity/wolf/[mod namespace]/[variant name].png"
                            You also need to add the glowing eye texture on "resources/assets/primal/textures/entity/wolf/[mod namespace]/[variant name]_eyes.png"
                            This will only work with correctly registered variants, no with new entities that use the wolf renderer
                            Default = []""");
                wolfExtraModsSupportedForVariants = builder.defineList("wolfExtraModsSupportedForVariants",
                        List.of(),
                        o -> o instanceof String s && s.contains(":")
                );

                builder.push("babyCustomModels");
                {
                    builder.comment("""
                            If for some reason you don't want the cute babies custom models, you can disable them here, and they will use the adult model with a big head.
                            You need to restart the game after changing these options.
                            Default for all=true""");
                    bearBabyCustomModel = builder.define("bearBabyCustomModel", true);
                    crocodileBabyCustomModel = builder.define("crocodileBabyCustomModel", true);
                    cassowaryBabyCustomModel = builder.define("cassowaryBabyCustomModel", true);
                    walrusBabyCustomModel = builder.define("walrusBabyCustomModel", true);
                    lionBabyCustomModel = builder.define("lionBabyCustomModel", true);
                    snakeBabyCustomModel = builder.define("snakeBabyCustomModel", true);
                    deerBabyCustomModel = builder.define("deerBabyCustomModel", true);
                }
                builder.pop();
            }
            builder.pop();

            builder.comment("""
                    Vanilla Mobs New Spawn - tweak natural spawn""");
            // --- Dolphin ---
            builder.push("dolphin");
            {
                builder.push("cold");
                {
                    builder.comment("""
                            Enable or disable cold dolphins spawning
                            Default=true""");
                    enableDolphinColdSpawn = builder.define("enableDolphinColdSpawn", true);

                    builder.comment("""
                            Spawn weight for cold dolphins (higher = more common)
                            Default=2""");
                    dolphinColdSpawnWeight = builder.defineInRange("dolphinColdSpawnWeight", 2, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Minimum group size for cold dolphins, it MUST be lesser than max group size
                            Default=1""");
                    dolphinColdMinGroup = builder.defineInRange("dolphinColdMinGroup", 1, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Maximum group size for cold dolphins, it MUST be greater than min group size
                            Default=2""");
                    dolphinColdMaxGroup = builder.defineInRange("dolphinColdMaxGroup", 2, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Extra biomes (separated from biome tag) where cold dolphins can spawn.
                            Must be written as full biome IDs (e.g. "minecraft:plains", "nomansland:maple_forest")
                            You can use biome tags too, just put a # before (e.g. "#minecraft:is_forest", "#nomansland:is_river")
                            Default = []""");
                    dolphinColdExtraBiomes = builder.defineList("dolphinColdExtraBiomes",
                            List.of(),
                            o -> o instanceof String s && s.contains(":")
                    );
                }
                builder.pop();
            }
            builder.pop();

            // --- Rabbit ---
            builder.push("rabbit");
            {
                builder.push("badlands");
                {
                    builder.comment("""
                            Enable or disable badlands rabbits spawning
                            Default=true""");
                    enableRabbitBadlandsSpawn = builder.define("enableRabbitBadlandsSpawn", true);

                    builder.comment("""
                            Spawn weight for badlands rabbits (higher = more common)
                            Default=5""");
                    rabbitBadlandsSpawnWeight = builder.defineInRange("rabbitBadlandsSpawnWeight", 5, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Minimum group size for badlands rabbits, it MUST be lesser than max group size
                            Default=1""");
                    rabbitBadlandsMinGroup = builder.defineInRange("rabbitBadlandsMinGroup", 1, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Maximum group size for badlands rabbits, it MUST be greater than min group size
                            Default=2""");
                    rabbitBadlandsMaxGroup = builder.defineInRange("rabbitBadlandsMaxGroup", 3, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Extra biomes (separated from biome tag) where badlands rabbits can spawn.
                            Must be written as full biome IDs (e.g. "minecraft:plains", "nomansland:maple_forest")
                            You can use biome tags too, just put a # before (e.g. "#minecraft:is_forest", "#nomansland:is_river")
                            Default = []""");
                    rabbitBadlandsExtraBiomes = builder.defineList("rabbitBadlandsExtraBiomes",
                            List.of(),
                            o -> o instanceof String s && s.contains(":")
                    );
                }
                builder.pop();
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
                            Default=15""");
                    bearSingleSpawnWeight = builder.defineInRange("bearSingleSpawnWeight", 15, 1, Integer.MAX_VALUE);

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
                            Default=2""");
                    sharkSingleSpawnWeight = builder.defineInRange("sharkSingleSpawnWeight", 2, 1, Integer.MAX_VALUE);

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

            // --- Walrus ---
            builder.push("walrus");
            {
                builder.push("coast");
                {
                    builder.comment("""
                            Enable or disable walrus spawning on coasts
                            Default=true""");
                    enableWalrusCoastSpawn = builder.define("enableWalrusCoastSpawn", true);

                    builder.comment("""
                            Spawn weight for walruses on coasts (higher = more common)
                            Default=3""");
                    walrusCoastSpawnWeight = builder.defineInRange("walrusCoastSpawnWeight", 3, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Minimum group size for walruses on coasts, it MUST be lesser than max group size
                            Default=3""");
                    walrusCoastMinGroup = builder.defineInRange("walrusCoastMinGroup", 3, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Maximum group size for walruses on coasts, it MUST be greater than min group size
                            Default=5""");
                    walrusCoastMaxGroup = builder.defineInRange("walrusCoastMaxGroup", 5, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Extra biomes (separated from biome tag) where walruses on coasts can spawn.
                            Must be written as full biome IDs (e.g. "minecraft:plains", "nomansland:maple_forest")
                            You can use biome tags too, just put a # before (e.g. "#minecraft:is_forest", "#nomansland:is_river")
                            Default = []""");
                    walrusCoastExtraBiomes = builder.defineList("walrusCoastExtraBiomes",
                            List.of(),
                            o -> o instanceof String s && s.contains(":")
                    );
                }
                builder.pop();

                builder.push("ocean");
                {
                    builder.comment("""
                            Enable or disable walrus spawning on oceans (Walruses only spawn on the ground)
                            Default=true""");
                    enableWalrusOceanSpawn = builder.define("enableWalrusOceanSpawn", true);

                    builder.comment("""
                            Spawn weight for walruses on oceans (Walruses only spawn on the ground) (higher = more common)
                            Default=1""");
                    walrusOceanSpawnWeight = builder.defineInRange("walrusOceanSpawnWeight", 1, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Minimum group size for walruses on oceans (Walruses only spawn on the ground), it MUST be lesser than max group size
                            Default=1""");
                    walrusOceanMinGroup = builder.defineInRange("walrusOceanMinGroup", 2, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Maximum group size for walruses on oceans (Walruses only spawn on the ground), it MUST be greater than min group size
                            Default=3""");
                    walrusOceanMaxGroup = builder.defineInRange("walrusOceanMaxGroup", 3, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Extra biomes (separated from biome tag) where walruses on oceans can spawn (Walruses only spawn on the ground).
                            Must be written as full biome IDs (e.g. "minecraft:plains", "nomansland:maple_forest")
                            You can use biome tags too, just put a # before (e.g. "#minecraft:is_forest", "#nomansland:is_river")
                            Default = []""");
                    walrusOceanExtraBiomes = builder.defineList("walrusOceanExtraBiomes",
                            List.of(),
                            o -> o instanceof String s && s.contains(":")
                    );
                }
                builder.pop();
            }
            builder.pop();

            // --- Lion ---
            builder.push("lion");
            {
                builder.push("savanna");
                {
                    builder.comment("""
                            Enable or disable lion spawning on savanna
                            Default=true""");
                    enableLionSavannaSpawn = builder.define("enableLionSavannaSpawn", true);

                    builder.comment("""
                            Spawn weight for lions (higher = more common)
                            Default=5""");
                    lionSavannaSpawnWeight = builder.defineInRange("lionSavannaSpawnWeight", 5, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Minimum group size for lions, it MUST be lesser than max group size
                            Default=2""");
                    lionSavannaMinGroup = builder.defineInRange("lionSavannaMinGroup", 2, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Maximum group size for lions, it MUST be greater than min group size
                            Default=5""");
                    lionSavannaMaxGroup = builder.defineInRange("lionSavannaMaxGroup", 5, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Extra biomes (separated from biome tag) where lions can spawn.
                            Must be written as full biome IDs (e.g. "minecraft:plains", "nomansland:maple_forest")
                            You can use biome tags too, just put a # before (e.g. "#minecraft:is_forest", "#nomansland:is_river")
                            Default = []""");
                    lionSavannaExtraBiomes = builder.defineList("lionSavannaExtraBiomes",
                            List.of(),
                            o -> o instanceof String s && s.contains(":")
                    );
                }
                builder.pop();

                builder.push("snowy");
                {
                    builder.comment("""
                            Enable or disable cave lions spawning on snowy biomes
                            Default=true""");
                    enableLionSnowySpawn = builder.define("enableLionSnowySpawn", true);

                    builder.comment("""
                            Spawn weight for cave lions (higher = more common)
                            Default=1""");
                    lionSnowySpawnWeight = builder.defineInRange("lionSnowySpawnWeight", 1, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Minimum group size for cave lions, it MUST be lesser than max group size
                            Default=2""");
                    lionSnowyMinGroup = builder.defineInRange("lionSnowyMinGroup", 2, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Maximum group size for cave lions, it MUST be greater than min group size
                            Default=3""");
                    lionSnowyMaxGroup = builder.defineInRange("lionSnowyMaxGroup", 3, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Extra biomes (separated from biome tag) where cave lions can spawn.
                            Must be written as full biome IDs (e.g. "minecraft:plains", "nomansland:maple_forest")
                            You can use biome tags too, just put a # before (e.g. "#minecraft:is_forest", "#nomansland:is_river")
                            Default = []""");
                    lionSnowyExtraBiomes = builder.defineList("lionSnowyExtraBiomes",
                            List.of(),
                            o -> o instanceof String s && s.contains(":")
                    );
                }
                builder.pop();
            }
            builder.pop();

            // --- Snake ---
            builder.push("snake");
            {
                builder.push("overworld");
                {
                    builder.comment("""
                            Enable or disable snake spawning (the spawn from hollow logs is in the world section)
                            Default=true""");
                    enableSnakeSpawn = builder.define("enableSnakeSpawn", true);

                    builder.comment("""
                            Spawn weight for snakes (higher = more common)
                            Default=2""");
                    snakeSpawnWeight = builder.defineInRange("snakeSpawnWeight", 2, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Minimum group size for snakes, it MUST be lesser than max group size
                            Default=1""");
                    snakeMinGroup = builder.defineInRange("snakeMinGroup", 1, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Maximum group size for snakes, it MUST be greater than min group size
                            Default=1""");
                    snakeMaxGroup = builder.defineInRange("snakeMaxGroup", 1, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Extra biomes (separated from biome tag) where snakes can spawn.
                            Must be written as full biome IDs (e.g. "minecraft:plains", "nomansland:maple_forest")
                            You can use biome tags too, just put a # before (e.g. "#minecraft:is_forest", "#nomansland:is_river")
                            Default = []""");
                    snakeExtraBiomes = builder.defineList("snakeExtraBiomes",
                            List.of(),
                            o -> o instanceof String s && s.contains(":")
                    );
                }
                builder.pop();
            }
            builder.pop();

            // --- Deer ---
            builder.push("deer");
            {
                builder.push("forest");
                {
                    builder.comment("""
                            Enable or disable deer spawning on forests
                            Default=true""");
                    enableDeerForestSpawn = builder.define("enableDeerForestSpawn", true);

                    builder.comment("""
                            Spawn weight for deer on forests (higher = more common)
                            Default=12""");
                    deerForestSpawnWeight = builder.defineInRange("deerForestSpawnWeight", 12, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Minimum group size for deer on forests, it MUST be lesser than max group size
                            Default=3""");
                    deerForestMinGroup = builder.defineInRange("deerForestMinGroup", 3, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Maximum group size for deer on forests, it MUST be greater than min group size
                            Default=6""");
                    deerForestMaxGroup = builder.defineInRange("deerForestMaxGroup", 6, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Extra biomes (separated from biome tag) where deer on forests can spawn.
                            Must be written as full biome IDs (e.g. "minecraft:plains", "nomansland:maple_forest")
                            You can use biome tags too, just put a # before (e.g. "#minecraft:is_forest", "#nomansland:is_river")
                            Default = []""");
                    deerForestExtraBiomes = builder.defineList("deerForestExtraBiomes",
                            List.of(),
                            o -> o instanceof String s && s.contains(":")
                    );
                }
                builder.pop();

                builder.push("snowy");
                {
                    builder.comment("""
                            Enable or disable deer spawning on snowy biomes
                            Default=true""");
                    enableDeerSnowySpawn = builder.define("enableDeerSnowySpawn", true);

                    builder.comment("""
                            Spawn weight for deer on snowy biomes (higher = more common)
                            Default=3""");
                    deerSnowySpawnWeight = builder.defineInRange("deerSnowySpawnWeight", 3, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Minimum group size for deer on snowy biomes, it MUST be lesser than max group size
                            Default=2""");
                    deerSnowyMinGroup = builder.defineInRange("deerSnowyMinGroup", 2, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Maximum group size for deer on snowy biomes, it MUST be greater than min group size
                            Default=4""");
                    deerSnowyMaxGroup = builder.defineInRange("deerSnowyMaxGroup", 4, 1, Integer.MAX_VALUE);

                    builder.comment("""
                            Extra biomes (separated from biome tag) where deer on snowy biomes can spawn.
                            Must be written as full biome IDs (e.g. "minecraft:plains", "nomansland:maple_forest")
                            You can use biome tags too, just put a # before (e.g. "#minecraft:is_forest", "#nomansland:is_river")
                            Default = []""");
                    deerSnowyExtraBiomes = builder.defineList("deerSnowyExtraBiomes",
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
                        Enable or disable river reeds spawning, they spawn alongside some cattails
                        Default=true""");
                riverReedsSpawnInWorld = builder.define("riverReedsSpawnInWorld", true);

                builder.comment("""
                        Patch rarity for river reeds, they spawn alongside some cattails
                        Default=2""");
                riverReedsPatchRarity = builder.defineInRange("riverReedsPatchRarity", 2, 0, Integer.MAX_VALUE);

                builder.comment("""
                        Patch horizontal spread (XZ) for river reeds, they spawn alongside some cattails
                        Default=8""");
                riverReedsPatchSpreadXZ = builder.defineInRange("riverReedsPatchSpreadXZ", 8, 0, Integer.MAX_VALUE);

                builder.comment("""
                        Patch vertical spread (Y) for river reeds, they spawn alongside some cattails
                        Default=5""");
                riverReedsPatchSpreadY = builder.defineInRange("riverReedsPatchSpreadY", 5, 0, Integer.MAX_VALUE);

                builder.comment("""
                        Extra biomes (separated from biome tag) where river reeds can spawn, they spawn alongside some cattails.
                        Must be written as full biome IDs (e.g. "minecraft:plains", "nomansland:maple_forest")
                        You can use biome tags too, just put a # before (e.g. "#minecraft:is_forest", "#nomansland:is_river")
                        Default = []""");
                riverReedsExtraBiomes = builder.defineList("riverReedsExtraBiomes",
                        List.of(),
                        o -> o instanceof String s && s.contains(":")
                );
            }
            builder.pop();

            // --- Cattails ---
            builder.push("cattails");
            {
                builder.comment("""
                        Enable or disable cattails spawning
                        Default=true""");
                cattailsSpawnInWorld = builder.define("cattailsSpawnInWorld", true);

                builder.comment("""
                        Cattails can generate alongside river reeds, this is to decrease them or disable the replacing
                        This is separated from the normal spawning on swamps
                        Put 0 to disable the replacing. It is on percentage (1 means 100%, 0.3 means 30%)
                        Default=0.33""");
                cattailsAlongsideRiverReedsProbability = builder.defineInRange("cattailsAlongsideRiverReedsProbability", 0.3, 0, 1);

                builder.comment("""
                        Patch rarity for cattails
                        Default=2""");
                cattailsPatchRarity = builder.defineInRange("cattailsPatchRarity", 2, 0, Integer.MAX_VALUE);

                builder.comment("""
                        Patch horizontal spread (XZ) for cattails
                        Default=8""");
                cattailsPatchSpreadXZ = builder.defineInRange("cattailsPatchSpreadXZ", 8, 0, Integer.MAX_VALUE);

                builder.comment("""
                        Patch vertical spread (Y) for cattails
                        Default=5""");
                cattailsPatchSpreadY = builder.defineInRange("cattailsPatchSpreadY", 5, 0, Integer.MAX_VALUE);

                builder.comment("""
                        Extra biomes (separated from biome tag) where cattails can spawn.
                        Must be written as full biome IDs (e.g. "minecraft:plains", "nomansland:maple_forest")
                        You can use biome tags too, just put a # before (e.g. "#minecraft:is_forest", "#nomansland:is_river")
                        Default = []""");
                cattailsExtraBiomes = builder.defineList("cattailsExtraBiomes",
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

            // --- Thorny Acacia ---
            builder.push("thorny_acacia");
            {
                builder.comment("""
                        Enable or disable thorny acacia trees spawning
                        Default=true""");
                thornyAcaciaSpawnInWorld = builder.define("thornyAcaciaSpawnInWorld", true);

                builder.comment("""
                        Probability of a thorny acacia replacing the generation of an acacia tree
                        Lower values make the tree rarer. It is on percentage (1 means 100%, 0.3 means 30%)
                        Minimum = 0.0
                        Default = 0.25""");
                thornyAcaciaProbabilityOfReplacing = builder.defineInRange("thornyAcaciaProbabilityOfReplacing", 0.25, 0, 1d);

                builder.comment("""
                        Extra biomes (separated from biome tag) where thorny acacia trees can spawn.
                        Thorny acacia trees replace ONLY acacia trees.
                        Must be written as full biome IDs (e.g. "minecraft:plains", "nomansland:maple_forest")
                        You can use biome tags too, just put a # before (e.g. "#minecraft:is_forest", "#nomansland:is_river")
                        Default = []""");
                thornyAcaciaExtraBiomes = builder.defineList("thornyAcaciaExtraBiomes",
                        List.of(),
                        o -> o instanceof String s && s.contains(":")
                );
            }
            builder.pop();

            // --- Hollow Logs ---
            builder.push("hollow_logs");
            {
                builder.comment("""
                        Enable or disable hollow oak logs spawning.
                        Default=true""");
                hollowOakLogSpawnInWorld = builder.define("hollowOakLogSpawnInWorld", true);

                builder.comment("""
                        Enable or disable hollow spruce logs spawning.
                        Default=true""");
                hollowSpruceLogSpawnInWorld = builder.define("hollowSpruceLogSpawnInWorld", true);

                builder.comment("""
                        Enable or disable hollow birch logs spawning.
                        Default=true""");
                hollowBirchLogSpawnInWorld = builder.define("hollowBirchLogSpawnInWorld", true);

                builder.comment("""
                        Enable or disable hollow jungle logs spawning.
                        Default=true""");
                hollowJungleLogSpawnInWorld = builder.define("hollowJungleLogSpawnInWorld", true);

                builder.comment("""
                        Enable or disable hollow acacia logs spawning.
                        Default=true""");
                hollowAcaciaLogSpawnInWorld = builder.define("hollowAcaciaLogSpawnInWorld", true);

                builder.comment("""
                        Enable or disable hollow dark oak logs spawning.
                        Default=true""");
                hollowDarkOakLogSpawnInWorld = builder.define("hollowDarkOakLogSpawnInWorld", true);

                builder.comment("""
                        Enable or disable hollow mangrove logs spawning.
                        Default=true""");
                hollowMangroveLogSpawnInWorld = builder.define("hollowMangroveLogSpawnInWorld", true);

                builder.comment("""
                        Probability of an empty hollow log spawning, each entry is a different tree.
                        Lower values make the hollow log rarer. It is on percentage (1 means 100%, 0.3 means 30%, 0.002 means 0.2%)
                        Minimum = 0.00
                        Default = [0.004, 0.002, 0.001, 0.01, 0.002, 0.15, 0.002]
                        The list goes: Oak, Spruce, Birch, Jungle, Acacia, Dark Oak and Mangrove.""");
                hollowLogEmptyProbability = builder.defineList("hollowLogEmptyProbability",
                        List.of(0.004, 0.002, 0.001, 0.08, 0.002, 0.15, 0.002),
                        o-> o instanceof Double);

                builder.comment("""
                        Probability of a hollow log with snakes inside spawning, each entry is a different tree.
                        Lower values make the hollow log rarer. It is on percentage (1 means 100%, 0.3 means 30%, 0.002 means 0.2%)
                        For reference, a bee nest on vanilla has 0.2% of spawning on a tree on a normal forest.
                        Minimum = 0.00
                        Default = [0.003, 0.002, 0.002, 0.002, 0.004, 0.002, 0.002]
                        The list goes: Oak, Spruce, Birch, Jungle, Acacia, Dark Oak and Mangrove.""");
                hollowLogSnakeProbability = builder.defineList("hollowLogSnakeProbability",
                        List.of(0.002, 0.002, 0.002, 0.002, 0.004, 0.002, 0.002),
                        o-> o instanceof Double);

                builder.comment("""
                        Extra biomes (separated from the biome tag) where hollow logs can spawn.
                        Hollow logs can replace any log with air in front of the log hole generated within a tree,
                        as long as they are inside the proper biome and the hollow version of the log actually exists.
                        Must be written as full biome IDs (e.g. "minecraft:plains", "nomansland:maple_forest")
                        You can use biome tags too, just put a # before (e.g. "#minecraft:is_forest", "#nomansland:is_river")
                        Default = []""");
                hollowTreesExtraBiomes = builder.defineList("hollowTreesExtraBiomes",
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


            // --- Cassowary Nests ---
            builder.push("cassowary_nest");
            {
                builder.comment("""
                        Enable or disable cassowary nest spawning
                        Default=true""");
                cassowaryNestSpawnInWorld = builder.define("cassowaryNestSpawnInWorld", true);

                builder.comment("""
                        Rarity of cassowary nests (higher = rarer)
                        Note: Cassowary nests can only generate if there is air on all sides of the origin, which makes them rarer in areas with a lot of obstructions.
                        Default=15""");
                cassowaryNestRarity = builder.defineInRange("cassowaryNestRarity", 15, 0, Integer.MAX_VALUE);

                builder.comment("""
                        Number of tries per patch for cassowary nests
                        Default=30""");
                cassowaryNestTries = builder.defineInRange("cassowaryNestTries", 30, 0, Integer.MAX_VALUE);

                builder.comment("""
                        Patch horizontal spread (XZ) for cassowary nests
                        Default=3""");
                cassowaryNestSpreadXZ = builder.defineInRange("cassowaryNestSpreadXZ", 3, 0, Integer.MAX_VALUE);

                builder.comment("""
                        Patch vertical spread (Y) for cassowary nests
                        Default=1""");
                cassowaryNestSpreadY = builder.defineInRange("cassowaryNestSpreadY", 1, 0, Integer.MAX_VALUE);

                builder.comment("""
                        Extra biomes (separated from biome tag) where cassowary nests can spawn.
                        Must be written as full biome IDs (e.g. "minecraft:plains", "nomansland:maple_forest")
                        You can use biome tags too, just put a # before (e.g. "#minecraft:is_forest", "#nomansland:is_river")
                        Default = []""");
                cassowaryNestExtraBiomes = builder.defineList("cassowaryNestExtraBiomes",
                        List.of(),
                        o -> o instanceof String s && s.contains(":")
                );
            }
            builder.pop();
        }
        builder.pop();

    }
}
