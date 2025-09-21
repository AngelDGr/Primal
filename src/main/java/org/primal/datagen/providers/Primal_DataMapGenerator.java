package org.primal.datagen.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import org.jetbrains.annotations.NotNull;
import org.primal.registry.Primal_Items;

import java.util.concurrent.CompletableFuture;

public class Primal_DataMapGenerator extends DataMapProvider {
    public Primal_DataMapGenerator(final PackOutput packOutput, final CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.@NotNull Provider provider) {

        builder(NeoForgeDataMaps.COMPOSTABLES)
                .add(Primal_Items.SEASHELLS, new Compostable(0.3f), false)
                .add(Primal_Items.SHORT_RIVER_REEDS, new Compostable(0.3f), false)
                .add(Primal_Items.RIVER_REEDS, new Compostable(0.5f), false);

        builder(NeoForgeDataMaps.FURNACE_FUELS)
                .add(Primal_Items.NEST, new FurnaceFuel(100), false)
                .add(Primal_Items.STRAW_BLOCK, new FurnaceFuel(800), false);
    }
}
