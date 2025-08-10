package org.primal;

import org.primal.datagen.ModDatagen;
import org.primal.entity.ModEntities;
import org.primal.entity.ai.sensors.ModSensors;
import org.primal.entity.ai.memory.ModMemoryModuleTypes;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Primal_Main.MODID)
public class Primal_Main {
    public static final String MODID = "primal";
    private static final Logger LOGGER = LogUtils.getLogger();
    
    public Primal_Main(IEventBus modEventBus) {
        ModEntities.bootstrap(modEventBus);
        ModSensors.bootstrap(modEventBus);
        ModDatagen.bootstrap(modEventBus);
        ModMemoryModuleTypes.bootstrap(modEventBus);
    }
}
