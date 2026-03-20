package org.primal.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.primal.Primal_Registries;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Boolean>> IS_EXPLODING =
            Primal_Registries.MEMORY_MODULE_TYPES.register("crocodile_exploding", () -> new MemoryModuleType<>(Optional.of(Codec.BOOL)));

    //Eagle
    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Boolean>> CHIRP_COOLDOWN =
            Primal_Registries.MEMORY_MODULE_TYPES.register("eagle_chirp_cooldown", () -> new MemoryModuleType<>(Optional.empty()));

    //Snake
    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<BlockPos>> NEAREST_EDIBLE_EGG =
            Primal_Registries.MEMORY_MODULE_TYPES.register("snake_nearest_edible_egg", () -> new MemoryModuleType<>(Optional.of(BlockPos.CODEC)));

    //Generic
    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<LivingEntity>> NEAREST_VISIBLE_BABY =
            Primal_Registries.MEMORY_MODULE_TYPES.register("nearest_visible_baby", () -> new MemoryModuleType<>(Optional.empty()));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Boolean>> IS_STUNNED =
            Primal_Registries.MEMORY_MODULE_TYPES.register("animal_stunned", () -> new MemoryModuleType<>(Optional.empty()));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<LivingEntity>> LAST_ATTACK_TARGET =
            Primal_Registries.MEMORY_MODULE_TYPES.register("last_attack_target", () -> new MemoryModuleType<>(Optional.empty()));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<BlockPos>> NEAREST_IMPORTANT_BLOCK =
            Primal_Registries.MEMORY_MODULE_TYPES.register("nearest_important_block", () -> new MemoryModuleType<>(Optional.of(BlockPos.CODEC)));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<LivingEntity>> NEAREST_SCARED =
            Primal_Registries.MEMORY_MODULE_TYPES.register("nearest_scared", () -> new MemoryModuleType<>(Optional.empty()));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Integer>> AMOUNT_ATTACKED =
            Primal_Registries.MEMORY_MODULE_TYPES.register("amount_attacked", () -> new MemoryModuleType<>(Optional.empty()));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Boolean>> LUNGE_COOLDOWN =
            Primal_Registries.MEMORY_MODULE_TYPES.register("lunge_cooldown", () -> new MemoryModuleType<>(Optional.of(Codec.BOOL)));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Boolean>> WAS_IDLE_ANIMATION =
            Primal_Registries.MEMORY_MODULE_TYPES.register("was_idle_animation", () -> new MemoryModuleType<>(Optional.of(Codec.BOOL)));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Boolean>> WAS_TRIGGER_ANIMATION =
            Primal_Registries.MEMORY_MODULE_TYPES.register("was_trigger_animation", () -> new MemoryModuleType<>(Optional.of(Codec.BOOL)));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Boolean>> WAS_TOWARDS_IMPORTANT_BLOCK =
            Primal_Registries.MEMORY_MODULE_TYPES.register("was_towards_important_block", () -> new MemoryModuleType<>(Optional.empty()));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Boolean>> HURT_RECENTLY =
            Primal_Registries.MEMORY_MODULE_TYPES.register("hurt_recently", () -> new MemoryModuleType<>(Optional.empty()));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Boolean>> HAS_INSTRUMENT =
            Primal_Registries.MEMORY_MODULE_TYPES.register("has_instrument", () -> new MemoryModuleType<>(Optional.empty()));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<LivingEntity>> STALK_TARGET =
            Primal_Registries.MEMORY_MODULE_TYPES.register("stalk_target", () -> new MemoryModuleType<>(Optional.empty()));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Boolean>> STALK_COOLDOWN =
            Primal_Registries.MEMORY_MODULE_TYPES.register("stalk_cooldown", () -> new MemoryModuleType<>(Optional.of(Codec.BOOL)));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<LivingEntity>> NEAREST_LEADER =
            Primal_Registries.MEMORY_MODULE_TYPES.register("nearest_leader", () -> new MemoryModuleType<>(Optional.empty()));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<LivingEntity>> NEAREST_PACK_MEMBER =
            Primal_Registries.MEMORY_MODULE_TYPES.register("nearest_pack_member", () -> new MemoryModuleType<>(Optional.empty()));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Boolean>> GO_TO_PACK_MEMBER_COOLDOWN =
            Primal_Registries.MEMORY_MODULE_TYPES.register("go_to_pack_member_cooldown", () -> new MemoryModuleType<>(Optional.of(Codec.BOOL)));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Map<String, Long>>> CAUTIOUS_LIST =
            Primal_Registries.MEMORY_MODULE_TYPES.register("cautious_targets_list", () -> new MemoryModuleType<>(Optional.of(Primal_Codecs.CAUTIOUS_LIST)));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<LivingEntity>> NEAREST_CAUTIOUS =
            Primal_Registries.MEMORY_MODULE_TYPES.register("nearest_cautious", () -> new MemoryModuleType<>(Optional.empty()));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<LivingEntity>> NEAREST_ATTACKABLE_CAUTIOUS =
            Primal_Registries.MEMORY_MODULE_TYPES.register("nearest_attackable_cautious", () -> new MemoryModuleType<>(Optional.empty()));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Boolean>> IS_GRABBING =
            Primal_Registries.MEMORY_MODULE_TYPES.register("animal_grab", () -> new MemoryModuleType<>(Optional.empty()));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<BlockPos>> MUSIC_BLOCK =
            Primal_Registries.MEMORY_MODULE_TYPES.register("nearest_music_block", () -> new MemoryModuleType<>(Optional.empty()));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Integer>> REST_NEEDED =
            Primal_Registries.MEMORY_MODULE_TYPES.register("rest_needed", () -> new MemoryModuleType<>(Optional.of(Codec.INT)));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Integer>> RESTED_TIME =
            Primal_Registries.MEMORY_MODULE_TYPES.register("rested_time", () -> new MemoryModuleType<>(Optional.of(Codec.INT)));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<BlockPos>> LANDING_POS =
            Primal_Registries.MEMORY_MODULE_TYPES.register("landing_pos", () -> new MemoryModuleType<>(Optional.of(BlockPos.CODEC)));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Boolean>> HOLLOW_LOG_ENTER_COOLDOWN =
            Primal_Registries.MEMORY_MODULE_TYPES.register("hollow_log_enter_cooldown", () -> new MemoryModuleType<>(Optional.of(Codec.BOOL)));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Integer>> MATE_VARIANT =
            Primal_Registries.MEMORY_MODULE_TYPES.register("mate_variant", () -> new MemoryModuleType<>(Optional.of(Codec.INT)));

    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<LivingEntity>> NEAREST_PLAY_MOB =
            Primal_Registries.MEMORY_MODULE_TYPES.register("nearest_play_mob", () -> new MemoryModuleType<>(Optional.empty()));


    //Note: Need codec to being able to store it inside the nbt
    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<List<UUID>>> ATTACKED_LIST =
            Primal_Registries.MEMORY_MODULE_TYPES.register("mobs_attacked", () -> new MemoryModuleType<>(Optional.of(Codec.list(UUIDUtil.CODEC))));

    public static void init() {}
}
