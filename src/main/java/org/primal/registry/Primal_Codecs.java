package org.primal.registry;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Primal_Codecs {

    public static final StreamCodec<ByteBuf, UUID> UUID =
            StreamCodec.of(
                    (buf, uuid) -> {
                        buf.writeLong(uuid.getMostSignificantBits());
                        buf.writeLong(uuid.getLeastSignificantBits());
                    },
                    buf -> new UUID(buf.readLong(), buf.readLong())
            );

    public static final Codec<Map<String, Long>> CAUTIOUS_LIST = Codec.unboundedMap(Codec.STRING, Codec.LONG);

    public static final StreamCodec<FriendlyByteBuf, ResourceLocation> RESOURCE_LOCATION =
            StreamCodec.of(
                    FriendlyByteBuf::writeResourceLocation,
                    FriendlyByteBuf::readResourceLocation
            );

    public static final Codec<List<Direction.Axis>> AXIS_LIST = Codec.list(Direction.Axis.CODEC);

    public static final Codec<Map<TagKey<Biome>, Integer>> BIOME_TAG_INTEGER_MAP = Codec.unboundedMap(TagKey.codec(Registries.BIOME), Codec.INT);
}
