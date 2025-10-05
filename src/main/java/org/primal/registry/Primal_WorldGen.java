package org.primal.registry;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.primal.Primal_Registries;
import org.primal.worldgen.EagleNestFeature;
import org.primal.worldgen.RiverReedsFeature;
import org.primal.worldgen.RandomPatchCustomConfig;
import org.primal.worldgen.SeashellsFeature;

import java.util.function.Supplier;

public class Primal_WorldGen {
    //New Features
    public static DeferredHolder<Feature<?>, RiverReedsFeature> RIVER_REEDS_FEATURE =
            registerFeature("river_reeds", ()-> new RiverReedsFeature(RandomPatchCustomConfig.CODEC));

    public static final DeferredHolder<Feature<?>, SeashellsFeature> SEASHELLS =
            registerFeature("seashells", ()-> new SeashellsFeature(SimpleBlockConfiguration.CODEC));

    public static final DeferredHolder<Feature<?>, EagleNestFeature> EAGLE_NEST =
            registerFeature("eagle_nest", ()-> new EagleNestFeature(RandomPatchCustomConfig.CODEC));

    public static void init() {}

    private static <C extends FeatureConfiguration, F extends Feature<C>> DeferredHolder<Feature<?>, F> registerFeature(final String name, final Supplier<F> feature) {
        return Primal_Registries.FEATURES.register(name,  feature);
    }
}
