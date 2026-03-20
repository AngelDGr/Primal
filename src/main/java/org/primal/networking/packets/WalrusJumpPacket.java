package org.primal.networking.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;

public record WalrusJumpPacket(int entityId, float playerJumpPendingScale) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<WalrusJumpPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "walrus_jump"));

    public static final StreamCodec<ByteBuf, WalrusJumpPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT,
                    WalrusJumpPacket::entityId,
                    ByteBufCodecs.FLOAT,
                    WalrusJumpPacket::playerJumpPendingScale,
                    WalrusJumpPacket::new
            );

    @Override
    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
