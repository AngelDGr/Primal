package org.primal.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record RandomPatchCustomConfig(int tries, int xzSpread, int ySpread) implements FeatureConfiguration {

    public static final Codec<RandomPatchCustomConfig> CODEC = RecordCodecBuilder.create(
            config -> config.group(
                            ExtraCodecs.POSITIVE_INT.fieldOf("tries").orElse(128).forGetter(RandomPatchCustomConfig::tries),
                            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("xz_spread").orElse(7).forGetter(RandomPatchCustomConfig::xzSpread),
                            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("y_spread").orElse(3).forGetter(RandomPatchCustomConfig::ySpread)
                    )
                    .apply(config, RandomPatchCustomConfig::new)
    );
}
