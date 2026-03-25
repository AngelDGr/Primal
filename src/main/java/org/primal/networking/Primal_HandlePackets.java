package org.primal.networking;

import net.minecraftforge.network.NetworkEvent;
import org.primal.entity.animal.WalrusEntity;
import org.primal.networking.packets.WalrusJumpPacket;

import java.util.function.Supplier;

public class Primal_HandlePackets {

    public static class OnClient {

    }

    public static class OnServer {
        public static void handleWalrusJumpPacket(final WalrusJumpPacket data, final Supplier<NetworkEvent.Context> context) {

            context.get().enqueueWork(() -> {

                var sender = context.get().getSender();
                if(sender==null) return;

                var entity = sender.level().getEntity(data.entityId());

                if(entity instanceof WalrusEntity walrus){
                    if(walrus.isInWater()){
                        walrus.doWhirlwind(data.playerJumpPendingScale());
                        walrus.setWhirlwindDuration(20);
                    } else {
                        walrus.stopTriggeredAnimation("base_controller", "ground_pound");
                        walrus.triggerAnim("base_controller", "ground_pound");
                        walrus.setSlamDuration(10);
                    }

                    walrus.setJumpCooldown(40);
                    walrus.setJumpScale(data.playerJumpPendingScale());
                }
            });

            context.get().setPacketHandled(true);
        }
    }
}