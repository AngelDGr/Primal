package org.primal.registry;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.primal.Primal_Main;
import org.primal.Primal_Registries;
import org.primal.block.SeashellsBlock;
import org.primal.worldgen.EagleNestFeature;
import org.primal.worldgen.RiverReedsFeature;
import org.primal.worldgen.RandomPatchCustomConfig;
import org.primal.worldgen.SeashellsFeature;

import java.util.function.Supplier;

public class Primal_WorldGen {
    public static final ResourceKey<PlacedFeature> RIVER_REEDS_PLACED = registerPlacedFeatureKey("river_reeds");
    public static ResourceKey<ConfiguredFeature<?, ?>> RIVER_REEDS_CONFIGURED = registerKey("river_reeds_patch");

    public static final ResourceKey<PlacedFeature> SEASHELLS_PLACED = registerPlacedFeatureKey("seashells");
    public static ResourceKey<ConfiguredFeature<?, ?>> SEASHELLS_CONFIGURED = registerKey("seashells_patch");

    public static final ResourceKey<PlacedFeature> EAGLES_NEST_SPAWN = registerPlacedFeatureKey("eagle_nest");
    public static ResourceKey<ConfiguredFeature<?, ?>> EAGLES_NEST_SPAWN_CONFIGURED = registerKey("eagle_nest_spawn");


    //New Features
    public static DeferredHolder<Feature<?>, RiverReedsFeature> RIVER_REEDS_FEATURE =
            registerFeature("river_reeds", ()-> new RiverReedsFeature(RandomPatchCustomConfig.CODEC));

    public static final DeferredHolder<Feature<?>, SeashellsFeature> SEASHELLS =
            registerFeature("seashells", ()-> new SeashellsFeature(SimpleBlockConfiguration.CODEC));

    public static final DeferredHolder<Feature<?>, EagleNestFeature> EAGLE_NEST =
            registerFeature("eagle_nest", ()-> new EagleNestFeature(RandomPatchCustomConfig.CODEC));

    public static void init() {}

    public static void boostrapPlacedFeature(final BootstrapContext<PlacedFeature> context) {
        final HolderGetter<ConfiguredFeature<?, ?>> registryEntryLookup = context.lookup(Registries.CONFIGURED_FEATURE);

        PlacementUtils.register(context, RIVER_REEDS_PLACED, registryEntryLookup.getOrThrow(RIVER_REEDS_CONFIGURED),
                RarityFilter.onAverageOnceEvery(2), InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP, BiomeFilter.biome());

        PlacementUtils.register(context, SEASHELLS_PLACED, registryEntryLookup.getOrThrow(SEASHELLS_CONFIGURED),
                RarityFilter.onAverageOnceEvery(5), InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_TOP_SOLID, BiomeFilter.biome());

        PlacementUtils.register(context, EAGLES_NEST_SPAWN, registryEntryLookup.getOrThrow(EAGLES_NEST_SPAWN_CONFIGURED),
                RarityFilter.onAverageOnceEvery(8), InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
    }

    public static void boostrapConfiguredFeature(final BootstrapContext<ConfiguredFeature<?, ?>> context) {
        registerFeature(context,
                RIVER_REEDS_CONFIGURED,
                RIVER_REEDS_FEATURE.get(),
                new RandomPatchCustomConfig(384,6,6));

        registerFeature(
                context,
                SEASHELLS_CONFIGURED,
                Feature.FLOWER,
                createSeashells(96,6,2)
        );

        registerFeature(context,
                EAGLES_NEST_SPAWN_CONFIGURED,
                EAGLE_NEST.get(),
                new RandomPatchCustomConfig(12,1,0));
    }

    @SuppressWarnings("all")
    private static RandomPatchConfiguration createSeashells(int tries, int xzSpread, int ySpread){
        SimpleWeightedRandomList.Builder<BlockState> builder = SimpleWeightedRandomList.builder();

        for (int i = 1; i <= 4; i++) {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                builder.add(
                        Primal_Blocks.SEASHELLS.get().defaultBlockState().setValue(SeashellsBlock.AMOUNT, i).setValue(SeashellsBlock.FACING, direction), 1
                );
            }
        }

        return new RandomPatchConfiguration(
                tries, xzSpread, ySpread,
                PlacementUtils.filtered(SEASHELLS.get(), new SimpleBlockConfiguration(new WeightedStateProvider(builder)), BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE)
        );
    }


    private static <C extends FeatureConfiguration, F extends Feature<C>> DeferredHolder<Feature<?>, F> registerFeature(final String name, final Supplier<F> feature) {
        return Primal_Registries.FEATURES.register(name,  feature);
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void registerFeature(final BootstrapContext<ConfiguredFeature<?, ?>> context, final ResourceKey<ConfiguredFeature<?, ?>> key, final F feature, final FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(final String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, name));
    }

    public static ResourceKey<PlacedFeature> registerPlacedFeatureKey(final String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, name));
    }
}
