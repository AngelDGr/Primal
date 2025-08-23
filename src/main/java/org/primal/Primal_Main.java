package org.primal;

import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import org.primal.entity.animal.BearEntity;
import org.primal.entity.animal.SharkEntity;
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
        Primal_Activities.init(); Primal_Registries.ACTIVITIES.register(modEventBus);

        //Entities
        Primal_Entities.init(); Primal_Registries.ENTITIES.register(modEventBus);

        //Blocks
        Primal_Blocks.init(); Primal_Registries.BLOCKS.register(modEventBus);

        //Items
        Primal_Items.initItems(); Primal_Registries.ITEMS.register(modEventBus);
        Primal_Items.initGroups(); Primal_Registries.CREATIVE_MODE_TABS.register(modEventBus);

        //Advancements
        Primal_Advancements.initCriteria(); Primal_Registries.CRITERIA.register(modEventBus);
        Primal_Advancements.initEntitySubPredicates(); Primal_Registries.ENTITY_SUB_PREDICATE_TYPES.register(modEventBus);
    }

    @SubscribeEvent
    public static void registerAttribute(EntityAttributeCreationEvent event) {
        event.put(Primal_Entities.BEAR.get(), BearEntity.createAttributes().build());
        event.put(Primal_Entities.SHARK.get(), SharkEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(final RegisterSpawnPlacementsEvent event){
        //Bear
        event.register(Primal_Entities.BEAR.get(), SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                BearEntity::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        //Shark
        event.register(Primal_Entities.SHARK.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                SharkEntity::checkSurfaceWaterAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }
}
