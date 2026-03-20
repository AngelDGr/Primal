package org.primal.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import org.primal.Primal_Main;

public class Primal_LootTables {

    public static ResourceKey<LootTable> CASSOWARY_PROCESSED_SEEDS = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID,"gameplay/cassowary_processed_seeds"));
    public static ResourceKey<LootTable> CASSOWARY_NEST_LOOT = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID,"gameplay/cassowary_nest_loot"));

    public static ResourceKey<LootTable> LION_MORNING_GIFT = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID,"gameplay/lion_morning_gift"));
}
