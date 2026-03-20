package org.primal.networking;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.primal.entity.animal.WalrusEntity;
import org.primal.networking.packets.WalrusJumpPacket;

public class Primal_HandlePackets {

    public static class OnClient {

    }

    public static class OnServer {
        public static void handleWalrusJumpPacket(final WalrusJumpPacket data, final IPayloadContext context) {

            var entity = context.player().level().getEntity(data.entityId());

            if(entity instanceof WalrusEntity walrus){
                if(walrus.isInWater()){
                    walrus.doWhirlwind(data.playerJumpPendingScale());
                    walrus.setWhirlwindDuration(20);
                } else {
                    walrus.stopTriggeredAnim("base_controller", "ground_pound");
                    walrus.triggerAnim("base_controller", "ground_pound");
                    walrus.setSlamDuration(10);
                }

                walrus.setJumpCooldown(40);
                walrus.setJumpScale(data.playerJumpPendingScale());
            }


        }
    }
}