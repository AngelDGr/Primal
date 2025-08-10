package org.primal;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import org.primal.entity.animal.BearEntity;
import org.primal.registry.*;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@EventBusSubscriber(modid = Primal_Main.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
@Mod(Primal_Main.MOD_ID)
public class Primal_Main {
    public static final String MOD_ID = "primal";
    
    public Primal_Main(IEventBus modEventBus) {
        //AI
        Primal_Sensors.init(); Primal_Registries.SENSOR_TYPES.register(modEventBus);
        Primal_MemoryModuleTypes.init(); Primal_Registries.MEMORY_MODULE_TYPES.register(modEventBus);

        //Entities
        Primal_Entities.init(); Primal_Registries.ENTITIES.register(modEventBus);

        //Blocks
        Primal_Blocks.init(); Primal_Registries.BLOCKS.register(modEventBus);

        //Items
        Primal_Items.initItems(); Primal_Registries.ITEMS.register(modEventBus);
        Primal_Items.initGroups(); Primal_Registries.CREATIVE_MODE_TABS.register(modEventBus);
    }

    @SubscribeEvent
    public static void registerAttribute(EntityAttributeCreationEvent event) {
        event.put(Primal_Entities.BEAR.get(), BearEntity.createAttributes().build());
    }
}
