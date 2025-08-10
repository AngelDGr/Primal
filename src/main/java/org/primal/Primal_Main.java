package org.primal;

import org.primal.datagen.ModDatagen;
import org.primal.entity.Primal_Entities;
import org.primal.entity.ai.sensors.ModSensors;
import org.primal.entity.ai.memory.ModMemoryModuleTypes;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Primal_Main.MODID)
public class Primal_Main {
    public static final String MODID = "primal";
    
    public Primal_Main(IEventBus modEventBus) {
        Primal_Entities.bootstrap(modEventBus);
        ModSensors.bootstrap(modEventBus);
        ModDatagen.bootstrap(modEventBus);
        ModMemoryModuleTypes.bootstrap(modEventBus);
    }
}
