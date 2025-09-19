package org.primal.datagen;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.primal.Primal_Main;
import org.primal.datagen.providers.*;
import org.primal.registry.Primal_BannerPatterns;
import org.primal.registry.Primal_DamageTypes;
import org.primal.registry.Primal_WorldGen;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = Primal_Main.MOD_ID)
public final class Primal_DataGenerator {

    private static final RegistrySetBuilder BUILDER =
            new RegistrySetBuilder()
//                    .add(Registries.DAMAGE_TYPE, Primal_DamageTypes::boostrapDamageTypes)
//                    .add(Registries.BANNER_PATTERN, Primal_BannerPatterns::bootstrap)

                    .add(Registries.PLACED_FEATURE, Primal_WorldGen::boostrapPlacedFeature)
                    .add(Registries.CONFIGURED_FEATURE, Primal_WorldGen::boostrapConfiguredFeature)

                    .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, Primal_BiomeModifiersGenerator::bootstrap);

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        //Client
        {
            //Block Model
            generator.addProvider(event.includeClient(), new Primal_BlockModelGenerator(output, existingFileHelper));

            //Block States
            generator.addProvider(event.includeClient(), new Primal_BlockStateGenerator(output, existingFileHelper));

            //Item Model
            generator.addProvider(event.includeClient(), new Primal_ItemModelGenerator(output, existingFileHelper));

            //Sounds
            generator.addProvider(event.includeClient(), new Primal_SoundsJsonGenerator(output, existingFileHelper));

            //Language File
            generator.addProvider(event.includeClient(), new Primal_LanguageFileGenerator(output));
        }


        //Server
        {
            //World Gen - Banners
            generator.addProvider(true, new DatapackBuiltinEntriesProvider(output, lookupProvider, BUILDER, Set.of("minecraft", Primal_Main.MOD_ID)));

            //Entity Tags
            generator.addProvider(event.includeServer(), new Primal_EntityTagGenerator(output, lookupProvider, existingFileHelper));
            //Block Tags
            final var blockTagGenerator = generator.addProvider(event.includeServer(), new Primal_BlockTagsGenerator(output, lookupProvider, existingFileHelper));
            //Biome Tags
            generator.addProvider(event.includeServer(), new Primal_BiomeTagGenerator(output, lookupProvider, existingFileHelper));
            //Damage Types Tags
            generator.addProvider(event.includeServer(), new Primal_DamageTypesTagGenerator(output, lookupProvider, existingFileHelper));
            //Item Tags
            generator.addProvider(event.includeServer(), new Primal_ItemTagsGenerator(output, lookupProvider, blockTagGenerator.contentsGetter(), existingFileHelper));
            //Banner Pattern Tags
            generator.addProvider(event.includeServer(), new Primal_BannerPatternTagsGenerator(output, lookupProvider, existingFileHelper));


            //Recipes
            generator.addProvider(event.includeServer(), new Primal_RecipesGenerator(output, lookupProvider));

            //Loot Tables
            generator.addProvider(event.includeServer(), createLootTableProviders(output, lookupProvider));

            //Advancements
            generator.addProvider(event.includeServer(), new Primal_AdvancementsGenerator(output, lookupProvider, existingFileHelper));





            //NeoForge Datamaps
            generator.addProvider(event.includeServer(), new Primal_DataMapGenerator(output, lookupProvider));
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
