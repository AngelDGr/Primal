package org.primal.entity.ai.memory;

import java.util.Optional;

import org.primal.Primal_Main;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModMemoryModuleTypes {

    public static DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES = DeferredRegister.create(Registries.MEMORY_MODULE_TYPE, Primal_Main.MODID);
    
    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<BlockPos>> NEAREST_BEEHIVE = MEMORY_MODULE_TYPES.register("nearest_beehive", () -> new MemoryModuleType<>(Optional.empty()));

    public static void bootstrap(IEventBus bus) {
        MEMORY_MODULE_TYPES.register(bus);
    }
}
