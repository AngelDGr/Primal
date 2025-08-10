package org.primal;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import org.primal.client.renderer.entity.bear.BearRenderer;
import org.primal.registry.Primal_Entities;

@EventBusSubscriber(modid = Primal_Main.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
@Mod(value = Primal_Main.MOD_ID, dist = Dist.CLIENT)
public class Primal_Client {

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(Primal_Entities.BEAR.get(), BearRenderer::new);
    }

}
