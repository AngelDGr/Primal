package org.primal.networking.packets;

import net.minecraft.network.FriendlyByteBuf;

public record WalrusJumpPacket(int entityId, float playerJumpPendingScale) {

    public static void write(WalrusJumpPacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.entityId);
        buffer.writeFloat(packet.playerJumpPendingScale);
    }

    public static WalrusJumpPacket read(FriendlyByteBuf buffer) {
        return new WalrusJumpPacket(buffer.readInt(), buffer.readFloat());
    }
}
