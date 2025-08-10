package org.primal.datagen;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.primal.Primal_Main;
import org.primal.datagen.providers.*;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = Primal_Main.MOD_ID)
public final class Primal_DataGenerator {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        //Client
        {
            //Item Model
            generator.addProvider(event.includeClient(), new Primal_ItemModelGenerator(output, existingFileHelper));

            //Sounds
            generator.addProvider(event.includeClient(), new Primal_SoundsJsonGenerator(output, existingFileHelper));

            //Language File
            generator.addProvider(event.includeClient(), new Primal_LanguageFileGenerator(output));
        }


        //Server
        {
            //Entity Tags
            generator.addProvider(event.includeServer(), new Primal_EntityTagGenerator(output, lookupProvider, existingFileHelper));

            //Recipes
            generator.addProvider(event.includeServer(), new Primal_RecipesGenerator(output, lookupProvider));

            //Loot Tables
            generator.addProvider(event.includeServer(), createLootTableProviders(output, lookupProvider));

            //Advancements
            generator.addProvider(event.includeServer(), new Primal_AdvancementsGenerator(output, lookupProvider, existingFileHelper));
        }
    }

    public static LootTableProvider createLootTableProviders(final PackOutput output, final CompletableFuture<HolderLookup.Provider> lookupProvider) {
        return new LootTableProvider(output, Set.of(),
                List.of(
                        new LootTableProvider.SubProviderEntry(Primal_LootTablesBlocksGenerator::new, LootContextParamSets.BLOCK),
                        new LootTableProvider.SubProviderEntry(Primal_LootTablesEntitiesGenerator::new, LootContextParamSets.ENTITY)),
                lookupProvider
        );
    }
}
