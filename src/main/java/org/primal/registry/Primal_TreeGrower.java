package org.primal.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Primal_TreeGrower {

    public static final AbstractTreeGrower THORNY_ACACIA =
            new AbstractTreeGrower() {
                @Override
                protected @Nullable ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(@NotNull RandomSource randomSource, boolean b) {
                    return Primal_WorldGen.ConfiguredFeatures.THORNY_ACACIA;
                }
            };
}
