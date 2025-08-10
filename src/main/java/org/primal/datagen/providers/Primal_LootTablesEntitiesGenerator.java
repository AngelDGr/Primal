package org.primal.datagen.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.registry.Primal_Entities;

import java.util.Map;
import java.util.stream.Stream;

public class Primal_LootTablesEntitiesGenerator extends EntityLootSubProvider {

    public Primal_LootTablesEntitiesGenerator(final HolderLookup.Provider lookupProvider) {
        super(FeatureFlags.DEFAULT_FLAGS, lookupProvider);
    }

    @Override
    protected @NotNull Stream<EntityType<?>> getKnownEntityTypes() {
        return BuiltInRegistries.ENTITY_TYPE.entrySet().stream()
                .filter(e -> e.getKey().location().getNamespace().equals(Primal_Main.MOD_ID)).map(Map.Entry::getValue);
    }

    //TODO: Add drops
    @Override
    public void generate() {


        this.add(Primal_Entities.BEAR.get(), LootTable.lootTable());

    }
}