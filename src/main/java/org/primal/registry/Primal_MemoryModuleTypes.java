package org.primal.registry;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.primal.Primal_Registries;

public final class Primal_MemoryModuleTypes {

    //Bear
    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<BlockPos>> NEAREST_BEEHIVE =
            Primal_Registries.MEMORY_MODULE_TYPES.register("nearest_beehive", () -> new MemoryModuleType<>(Optional.empty()));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<BlockPos>> NEAREST_SWEET_BERRY_BUSH =
            Primal_Registries.MEMORY_MODULE_TYPES.register("nearest_sweet_berry_bush", () -> new MemoryModuleType<>(Optional.empty()));

    //Shark
    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<LivingEntity>> NEAREST_CONDUIT_PLAYER =
            Primal_Registries.MEMORY_MODULE_TYPES.register("nearest_conduit_player", () -> new MemoryModuleType<>(Optional.empty()));


    //Crocodile
    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Boolean>> IS_THRASHING =
            Primal_Registries.MEMORY_MODULE_TYPES.register("crocodile_thrashing", () -> new MemoryModuleType<>(Optional.empty()));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Boolean>> WAS_BASKING =
            Primal_Registries.MEMORY_MODULE_TYPES.register("crocodile_was_basking", () -> new MemoryModuleType<>(Optional.empty()));

    //Eagle
    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Boolean>> IS_SNATCHING =
            Primal_Registries.MEMORY_MODULE_TYPES.register("eagle_snatching", () -> new MemoryModuleType<>(Optional.empty()));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Boolean>> CHIRP_COOLDOWN =
            Primal_Registries.MEMORY_MODULE_TYPES.register("eagle_chirp_cooldown", () -> new MemoryModuleType<>(Optional.empty()));

    //Generic
    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<AgeableMob>> NEAREST_VISIBLE_BABY =
            Primal_Registries.MEMORY_MODULE_TYPES.register("nearest_visible_baby", () -> new MemoryModuleType<>(Optional.empty()));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Boolean>> IS_STUNNED =
            Primal_Registries.MEMORY_MODULE_TYPES.register("animal_stunned", () -> new MemoryModuleType<>(Optional.empty()));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<LivingEntity>> LAST_ATTACK_TARGET =
            Primal_Registries.MEMORY_MODULE_TYPES.register("last_attack_target", () -> new MemoryModuleType<>(Optional.empty()));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<BlockPos>> NEAREST_IMPORTANT_BLOCK =
            Primal_Registries.MEMORY_MODULE_TYPES.register("nearest_important_block", () -> new MemoryModuleType<>(Optional.empty()));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<LivingEntity>> NEAREST_SCARED =
            Primal_Registries.MEMORY_MODULE_TYPES.register("nearest_scared", () -> new MemoryModuleType<>(Optional.empty()));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Integer>> AMOUNT_ATTACKED =
            Primal_Registries.MEMORY_MODULE_TYPES.register("amount_attacked", () -> new MemoryModuleType<>(Optional.empty()));

    //Note: Need codec to being able to store it inside the nbt
    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<List<UUID>>> ATTACKED_LIST =
            Primal_Registries.MEMORY_MODULE_TYPES.register("mobs_attacked", () -> new MemoryModuleType<>(Optional.of(Codec.list(UUIDUtil.CODEC))));

    public static void init() {}
}
