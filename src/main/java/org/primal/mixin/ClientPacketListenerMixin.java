package org.primal.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.world.entity.Entity;
import org.primal.util.HostileMount;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    @Shadow private ClientLevel level;

    @WrapWithCondition(
            method = "handleSetEntityPassengersPacket",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;setOverlayMessage(Lnet/minecraft/network/chat/Component;Z)V")
    )    private boolean mtg$disableDismountToast(Gui instance, Component component, boolean animateColor, @Local(argsOnly = true) ClientboundSetPassengersPacket packet){
        Entity entity = this.level.getEntity(packet.getVehicle());


        return !(entity instanceof HostileMount);
    }

    @WrapWithCondition(
            method = "handleSetEntityPassengersPacket",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/GameNarrator;sayNow(Lnet/minecraft/network/chat/Component;)V")
    )    private boolean mtg$disableDismountNarrator(GameNarrator instance, Component message, @Local(argsOnly = true) ClientboundSetPassengersPacket packet){
        Entity entity = this.level.getEntity(packet.getVehicle());


        return !(entity instanceof HostileMount);
    }
}
