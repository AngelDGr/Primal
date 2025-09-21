package org.primal;

import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.GrassColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import org.primal.client.renderer.block_entity.NestBlockEntityRenderer;
import org.primal.client.renderer.entity.BearRenderer;
import org.primal.client.renderer.entity.CrocodileRenderer;
import org.primal.client.renderer.entity.EagleRenderer;
import org.primal.client.renderer.entity.SharkRenderer;
import org.primal.client.renderer.replaced.PolarBearRenderer;
import org.primal.registry.Primal_BlockEntities;
import org.primal.registry.Primal_Blocks;
import org.primal.registry.Primal_Entities;
import org.primal.registry.Primal_Items;

@EventBusSubscriber(modid = Primal_Main.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
@Mod(value = Primal_Main.MOD_ID, dist = Dist.CLIENT)
public class Primal_Client {

    @SubscribeEvent
    public static void registerClientEvent(final FMLClientSetupEvent event){
        registerBlockRenderers();
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(Primal_Entities.BEAR.get(),      BearRenderer::new);
        event.registerEntityRenderer(Primal_Entities.SHARK.get(),     SharkRenderer::new);
        event.registerEntityRenderer(Primal_Entities.CROCODILE.get(), CrocodileRenderer::new);
        event.registerEntityRenderer(Primal_Entities.EAGLE.get(),     EagleRenderer::new);

        //Replaced
        event.registerEntityRenderer(EntityType.POLAR_BEAR,      PolarBearRenderer::new);

        event.registerBlockEntityRenderer(Primal_BlockEntities.NEST_BLOCK_ENTITY.get(), NestBlockEntityRenderer::new);
    }

    @SuppressWarnings("deprecation")
    public static void registerBlockRenderers(){
        //Blocks
        ItemBlockRenderTypes.setRenderLayer(Primal_Blocks.NEST_BLOCK.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(Primal_Blocks.SHARK_TOOTH.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(Primal_Blocks.RIVER_REEDS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(Primal_Blocks.SHORT_RIVER_REEDS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(Primal_Blocks.SEASHELLS.get(), RenderType.cutout());
    }

    @SubscribeEvent
    public static void registerColorBlocks(final RegisterColorHandlersEvent.Block event){

        event.register((state, world, pos, tintIndex) -> {
                    if (world == null || pos == null) {
                        return GrassColor.getDefaultColor();
                    }
                    return BiomeColors.getAverageGrassColor(world, pos);}, Primal_Blocks.SHORT_RIVER_REEDS.get(), Primal_Blocks.RIVER_REEDS.get());
    }

    @SubscribeEvent
    public static void registerColorItems(final RegisterColorHandlersEvent.Item event){
        event.register((stack, tintIndex) -> 0x91bd59, Primal_Items.SHORT_RIVER_REEDS.get());

        event.register((stack, tintIndex) -> tintIndex==0? 0x91bd59: -1, Primal_Items.RIVER_REEDS.get());
    }
}
