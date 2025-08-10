package org.primal.registry;

import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.primal.Primal_Registries;

public final class Primal_MemoryModuleTypes {

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<BlockPos>> NEAREST_BEEHIVE = Primal_Registries.MEMORY_MODULE_TYPES.register("nearest_beehive", () -> new MemoryModuleType<>(Optional.empty()));

    public static void init() {}
}
