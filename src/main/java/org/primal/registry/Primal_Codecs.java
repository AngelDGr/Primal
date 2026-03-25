package org.primal.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.List;
import java.util.Map;

public class Primal_Codecs {

    public static final Codec<Map<String, Long>> CAUTIOUS_LIST = Codec.unboundedMap(Codec.STRING, Codec.LONG);

    public static final Codec<List<Direction.Axis>> AXIS_LIST = Codec.list(Direction.Axis.CODEC);

    public static final Codec<Map<TagKey<Biome>, Integer>> BIOME_TAG_INTEGER_MAP = Codec.unboundedMap(TagKey.codec(Registries.BIOME), Codec.INT);
}
