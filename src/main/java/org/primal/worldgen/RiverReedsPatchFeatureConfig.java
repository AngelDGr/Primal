package org.primal.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record RiverReedsPatchFeatureConfig(int tries, int xzSpread, int ySpread) implements FeatureConfiguration {

    public static final Codec<RiverReedsPatchFeatureConfig> CODEC = RecordCodecBuilder.create(
            config -> config.group(
                            ExtraCodecs.POSITIVE_INT.fieldOf("tries").orElse(128).forGetter(RiverReedsPatchFeatureConfig::tries),
                            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("xz_spread").orElse(7).forGetter(RiverReedsPatchFeatureConfig::xzSpread),
                            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("y_spread").orElse(3).forGetter(RiverReedsPatchFeatureConfig::ySpread)
                    )
                    .apply(config, RiverReedsPatchFeatureConfig::new)
    );
}
