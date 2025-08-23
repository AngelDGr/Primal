package org.primal;

import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.EntitySubPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Primal_Registries {

    public static DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES = DeferredRegister.create(Registries.MEMORY_MODULE_TYPE, Primal_Main.MOD_ID);

    public static DeferredRegister<SensorType<?>> SENSOR_TYPES = DeferredRegister.create(Registries.SENSOR_TYPE, Primal_Main.MOD_ID);

    public static DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, Primal_Main.MOD_ID);

    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Primal_Main.MOD_ID);

    public static DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Primal_Main.MOD_ID);

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, Primal_Main.MOD_ID);

    public static final DeferredRegister<MapCodec<? extends EntitySubPredicate>> ENTITY_SUB_PREDICATE_TYPES = DeferredRegister.create(Registries.ENTITY_SUB_PREDICATE_TYPE, Primal_Main.MOD_ID);

    public static DeferredRegister<Activity> ACTIVITIES = DeferredRegister.create(Registries.ACTIVITY, Primal_Main.MOD_ID);

    public static DeferredRegister<CriterionTrigger<?>> CRITERIA = DeferredRegister.create(Registries.TRIGGER_TYPE, Primal_Main.MOD_ID);

}
