package org.primal;

import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.EntitySubPredicate;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.primal.item.HelmetDecoration;

public class Primal_Registries {

    public static DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES = DeferredRegister.create(Registries.MEMORY_MODULE_TYPE, Primal_Main.MOD_ID);

    public static DeferredRegister<SensorType<?>> SENSOR_TYPES = DeferredRegister.create(Registries.SENSOR_TYPE, Primal_Main.MOD_ID);

    public static DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, Primal_Main.MOD_ID);

    public static DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, Primal_Main.MOD_ID);

    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Primal_Main.MOD_ID);

    public static DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Primal_Main.MOD_ID);

    public static DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Primal_Main.MOD_ID);

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, Primal_Main.MOD_ID);

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Primal_Main.MOD_ID);

    public static DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, Primal_Main.MOD_ID);

    public static DeferredRegister<Potion> POTIONS = DeferredRegister.create(Registries.POTION, Primal_Main.MOD_ID);

    public static final DeferredRegister<MapCodec<? extends EntitySubPredicate>> ENTITY_SUB_PREDICATE_TYPES = DeferredRegister.create(Registries.ENTITY_SUB_PREDICATE_TYPE, Primal_Main.MOD_ID);

    public static DeferredRegister<Activity> ACTIVITIES = DeferredRegister.create(Registries.ACTIVITY, Primal_Main.MOD_ID);

    public static DeferredRegister<CriterionTrigger<?>> CRITERIA = DeferredRegister.create(Registries.TRIGGER_TYPE, Primal_Main.MOD_ID);

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, Primal_Main.MOD_ID);

    public final static DeferredRegister<MapCodec<? extends BiomeModifier>> BIOME_MODIFIERS = DeferredRegister.create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Primal_Main.MOD_ID);

    public static DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(Registries.PARTICLE_TYPE, Primal_Main.MOD_ID);

    public static DeferredRegister<TreeDecoratorType<?>> TREE_DECORATORS = DeferredRegister.create(Registries.TREE_DECORATOR_TYPE, Primal_Main.MOD_ID);

    public static DeferredRegister<BlockStateProviderType<?>> BLOCK_STATE_PROVIDERS = DeferredRegister.create(Registries.BLOCK_STATE_PROVIDER_TYPE, Primal_Main.MOD_ID);


    public static final ResourceKey<Registry<HelmetDecoration<?>>> PRIMAL_HELMET_DECORATIONS = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "helmet_decorations"));
    public static final Registry<HelmetDecoration<?>> HELMET_DECORATIONS_REGISTRY = new RegistryBuilder<>(PRIMAL_HELMET_DECORATIONS)
            .sync(true)
            .defaultKey(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "none"))
            // Build the registry.
            .create();

    public static final DeferredRegister<HelmetDecoration<?>> HELMET_DECORATIONS = DeferredRegister.create(PRIMAL_HELMET_DECORATIONS, Primal_Main.MOD_ID);
}
