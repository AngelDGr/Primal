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
                .add(Primal_Items.WARM_SEASHELLS, new Compostable(0.3f), false)
                .add(Primal_Items.COLD_SEASHELLS, new Compostable(0.3f), false)
                .add(Primal_Items.TEMPERATE_SEASHELLS, new Compostable(0.3f), false)
                .add(Primal_Items.SHORT_RIVER_REEDS, new Compostable(0.3f), false)
                .add(Primal_Items.RIVER_REEDS, new Compostable(0.5f), false)
                .add(Primal_Items.CATTAILS, new Compostable(0.5f), false)
                .add(Primal_Items.THORNY_ACACIA_LEAVES, new Compostable(0.3f), false)
                .add(Primal_Items.EXPLOSEED, new Compostable(0.65f), false)
                .add(Primal_Items.STRAW_BLOCK, new Compostable(0.85f), false)
                .add(Primal_Items.DRIED_STRAW_BLOCK, new Compostable(0.85f), false)
                .add(Primal_Items.WEAVED_STRAW, new Compostable(0.85f), false)
                .add(Primal_Items.WEAVED_STRAW_STAIRS, new Compostable(0.35f), false)
                .add(Primal_Items.WEAVED_STRAW_SLAB, new Compostable(0.35f), false)
                .add(Primal_Items.STRAW_BASKET, new Compostable(0.85f), false);

        builder(NeoForgeDataMaps.FURNACE_FUELS)
                .add(Primal_Items.NEST, new FurnaceFuel(100), false)
                .add(Primal_Items.STRAW_BLOCK, new FurnaceFuel(800), false)
                .add(Primal_Items.DRIED_STRAW_BLOCK, new FurnaceFuel(1600), false)
                .add(Primal_Items.WEAVED_STRAW, new FurnaceFuel(600), false)
                .add(Primal_Items.WEAVED_STRAW_STAIRS, new FurnaceFuel(300), false)
                .add(Primal_Items.WEAVED_STRAW_SLAB, new FurnaceFuel(300), false)
                .add(Primal_Items.STRAW_BASKET, new FurnaceFuel(600), false);
    }
}
