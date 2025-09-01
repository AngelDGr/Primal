package org.primal.datagen.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
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
                .add(Primal_Items.SEASHELLS, new Compostable(0.3f), false);
    }
}
