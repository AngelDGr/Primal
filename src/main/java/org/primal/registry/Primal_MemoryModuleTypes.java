package org.primal.registry;

import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.primal.Primal_Registries;

public final class Primal_MemoryModuleTypes {

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<BlockPos>> NEAREST_BEEHIVE =
            Primal_Registries.MEMORY_MODULE_TYPES.register("nearest_beehive", () -> new MemoryModuleType<>(Optional.empty()));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<BlockPos>> NEAREST_SWEET_BERRY_BUSH =
            Primal_Registries.MEMORY_MODULE_TYPES.register("nearest_sweet_berry_bush", () -> new MemoryModuleType<>(Optional.empty()));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<AgeableMob>> NEAREST_VISIBLE_BABY =
            Primal_Registries.MEMORY_MODULE_TYPES.register("nearest_visible_baby", () -> new MemoryModuleType<>(Optional.empty()));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<LivingEntity>> NEAREST_CONDUIT_PLAYER =
            Primal_Registries.MEMORY_MODULE_TYPES.register("nearest_conduit_player", () -> new MemoryModuleType<>(Optional.empty()));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<BlockPos>> NEAREST_IMPORTANT_BLOCK =
            Primal_Registries.MEMORY_MODULE_TYPES.register("nearest_important_block", () -> new MemoryModuleType<>(Optional.empty()));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<LivingEntity>> LAST_ATTACK_TARGET =
            Primal_Registries.MEMORY_MODULE_TYPES.register("last_attack_target", () -> new MemoryModuleType<>(Optional.empty()));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Boolean>> IS_THRASHING =
            Primal_Registries.MEMORY_MODULE_TYPES.register("crocodile_thrashing", () -> new MemoryModuleType<>(Optional.empty()));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Boolean>> IS_STUNNED =
            Primal_Registries.MEMORY_MODULE_TYPES.register("crocodile_stunned", () -> new MemoryModuleType<>(Optional.empty()));

    public static void init() {}
}
