package org.primal.registry;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.AcaciaFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraft.world.level.levelgen.feature.treedecorators.AlterGroundDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.ForkingTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.primal.Primal_Main;
import org.primal.Primal_Registries;
import org.primal.worldgen.*;
import org.primal.worldgen.blockstate_provider.HollowLogBlockProvider;
import org.primal.worldgen.feature.AlterLeavesDecorator;
import org.primal.worldgen.feature.AlterTrunkDecorator;
import org.primal.worldgen.feature.HollowLogTreeDecorator;

import java.util.function.Supplier;

public class Primal_WorldGen {
    //New Features
    public static DeferredHolder<Feature<?>, RiverReedsFeature> RIVER_REEDS_FEATURE =
            registerFeature("river_reeds", ()-> new RiverReedsFeature(RandomPatchCustomConfig.CODEC));

    public static DeferredHolder<Feature<?>, CattailFeature> CATTAILS_FEATURE =
            registerFeature("cattails", ()-> new CattailFeature(RandomPatchCustomConfig.CODEC));

    public static final DeferredHolder<Feature<?>, SeashellsFeature> SEASHELLS =
            registerFeature("seashells", ()-> new SeashellsFeature(SimpleBlockConfiguration.CODEC));

    public static final DeferredHolder<Feature<?>, EagleNestFeature> EAGLE_NEST =
            registerFeature("eagle_nest", ()-> new EagleNestFeature(RandomPatchCustomConfig.CODEC));

    public static final DeferredHolder<Feature<?>, CassowaryNestFeature> CASSOWARY_NEST =
            registerFeature("cassowary_nest", ()-> new CassowaryNestFeature(RandomPatchCustomConfig.CODEC));

    public static void init() {}

    private static <C extends FeatureConfiguration, F extends Feature<C>> DeferredHolder<Feature<?>, F> registerFeature(final String name, final Supplier<F> feature) {
        return Primal_Registries.FEATURES.register(name,  feature);
    }

    public static class ConfiguredFeatures {

        public static final ResourceKey<ConfiguredFeature<?, ?>> THORNY_ACACIA = createKey("thorny_acacia");

        public static void boostrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {

            FeatureUtils.register(
                    context,
                    THORNY_ACACIA,
                    Feature.TREE,
                    new TreeConfiguration.TreeConfigurationBuilder(
                            BlockStateProvider.simple(Primal_Blocks.THORNY_ACACIA_LOG.get()),
                            new ForkingTrunkPlacer(5, 2, 2),
                            BlockStateProvider.simple(Primal_Blocks.THORNY_ACACIA_LEAVES.get()),
                            new AcaciaFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)),
                            new TwoLayersFeatureSize(1, 0, 2)
                    ).decorators(ImmutableList.of(new AlterGroundDecorator(BlockStateProvider.simple(Blocks.COARSE_DIRT))))
                            .ignoreVines().build()
            );
        }

        public static ResourceKey<ConfiguredFeature<?, ?>> createKey(String string) {
            return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, string));
        }
    }

    public static class PlacedFeatures {

        public static final ResourceKey<PlacedFeature> THORNY_ACACIA = createKey("thorny_acacia");

        public static void boostrap(BootstrapContext<PlacedFeature> context) {
            final HolderGetter<ConfiguredFeature<?, ?>> registryEntryLookup = context.lookup(Registries.CONFIGURED_FEATURE);

            PlacementUtils.register(context, THORNY_ACACIA, registryEntryLookup.getOrThrow(ConfiguredFeatures.THORNY_ACACIA),
                    PlacementUtils.filteredByBlockSurvival(Blocks.ACACIA_SAPLING));
        }

        public static ResourceKey<PlacedFeature> createKey(String string) {
            return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, string));
        }
    }

    public static class TreeDecorators {

        public static DeferredHolder<TreeDecoratorType<?>, TreeDecoratorType<?>> ALTERNATE_TRUNK;
        public static DeferredHolder<TreeDecoratorType<?>, TreeDecoratorType<?>> ALTERNATE_LEAVES;
        public static DeferredHolder<TreeDecoratorType<?>, TreeDecoratorType<?>> HOLLOW_LOG;

        public static void init() {
            ALTERNATE_TRUNK = register("alter_trunk", AlterTrunkDecorator.CODEC);
            ALTERNATE_LEAVES = register("alter_leaves", AlterLeavesDecorator.CODEC);
            HOLLOW_LOG = register("hollow_log", HollowLogTreeDecorator.CODEC);
        }

        private static<P extends TreeDecorator> DeferredHolder<TreeDecoratorType<?>, TreeDecoratorType<?>> register(final String name, final MapCodec<P> codec) {
            return Primal_Registries.TREE_DECORATORS.register(name,  ()-> new TreeDecoratorType<>(codec));
        }

    }

    public static class BlockStateProviders {

        public static DeferredHolder<BlockStateProviderType<?>, BlockStateProviderType<?>> HOLLOW_LOG_ROTATED;

        public static void init() {
            HOLLOW_LOG_ROTATED = register("hollow_log_rotated", HollowLogBlockProvider.CODEC);
        }

        private static<P extends BlockStateProvider> DeferredHolder<BlockStateProviderType<?>, BlockStateProviderType<?>> register(final String name, final MapCodec<P> codec) {
            return Primal_Registries.BLOCK_STATE_PROVIDERS.register(name, ()-> new BlockStateProviderType<>(codec));
        }
    }
}