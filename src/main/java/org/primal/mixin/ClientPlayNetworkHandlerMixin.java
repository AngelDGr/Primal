package org.primal.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.network.Connection;
import net.minecraft.network.TickablePacketListener;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.CrocodileEntity;
import org.primal.sounds.CrocodileSplashesSoundInstance;
import org.primal.sounds.CrocodileThrashingSoundInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPlayNetworkHandlerMixin extends ClientCommonPacketListenerImpl implements TickablePacketListener, ClientGamePacketListener {
    @Shadow private ClientLevel level;

    public ClientPlayNetworkHandlerMixin(final Minecraft client, final Connection connection, final CommonListenerCookie connectionState) {
        super(client, connection, connectionState);
    }

    @Inject(method = "handleEntityEvent", at = @At("TAIL"), cancellable = true)
    private void injectGhoulSound(@NotNull final ClientboundEntityEventPacket packet, final CallbackInfo ci) {
        final Entity entity = packet.getEntity(this.level);
        if (entity instanceof CrocodileEntity crocodile) {
            if (packet.getEventId() == CrocodileEntity.CROCODILE_THRASHING) {
                this.minecraft.getSoundManager().play(new CrocodileThrashingSoundInstance(crocodile));
                this.minecraft.getSoundManager().play(new CrocodileSplashesSoundInstance(crocodile));
                ci.cancel();
            }
        }
    }
}
