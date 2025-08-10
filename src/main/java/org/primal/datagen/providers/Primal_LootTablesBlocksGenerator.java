package org.primal.datagen.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;

import java.util.Map;
import java.util.Set;

public class Primal_LootTablesBlocksGenerator extends BlockLootSubProvider {

    public Primal_LootTablesBlocksGenerator(final HolderLookup.Provider lookupProvider) {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, lookupProvider);
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return BuiltInRegistries.BLOCK.entrySet().stream()
                .filter(e -> e.getKey().location().getNamespace().equals(Primal_Main.MOD_ID)).map(Map.Entry::getValue).toList();
    }

    @Override
    public void generate() {
        final HolderLookup.RegistryLookup<Enchantment> enchantmentRegistryLookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

    }
}
