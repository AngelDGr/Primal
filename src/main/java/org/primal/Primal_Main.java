package org.primal;

import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.primal.biome_modifiers.features.EagleNest_BiomeModifier;
import org.primal.biome_modifiers.features.RiverReeds_BiomeModifier;
import org.primal.biome_modifiers.features.Seashells_BiomeModifier;
import org.primal.biome_modifiers.mobs.*;
import org.primal.entity.animal.BearEntity;
import org.primal.entity.animal.CrocodileEntity;
import org.primal.entity.animal.EagleEntity;
import org.primal.entity.animal.SharkEntity;
import org.primal.registry.*;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.primal.util.MiscUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@EventBusSubscriber(modid = Primal_Main.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
@Mod(Primal_Main.MOD_ID)
public class Primal_Main {
    public static final String MOD_ID = "primal";
    public static final Primal_Config COMMON_CONFIG;
    public static final ModConfigSpec COMMON_SPEC;

    static {
        Pair<Primal_Config, ModConfigSpec> pair = new ModConfigSpec.Builder()
                .configure(Primal_Config::new);
        COMMON_CONFIG = pair.getLeft();
        COMMON_SPEC = pair.getRight();
    }

    public static class ConfigCache {
        public static boolean foxModelChange;
        public static boolean polarBearModelChange;

        public static void load() {
            foxModelChange = Primal_Main.COMMON_CONFIG.foxModelChange.get();
            polarBearModelChange = Primal_Main.COMMON_CONFIG.polarBearModelChange.get();
        }
    }

    public Primal_Main(IEventBus modEventBus) {
        ModList.get().getModContainerById(Primal_Main.MOD_ID).get().registerConfig(ModConfig.Type.COMMON, COMMON_SPEC);

        //AI
        Primal_Sensors.init(); Primal_Registries.SENSOR_TYPES.register(modEventBus);
        Primal_MemoryModuleTypes.init(); Primal_Registries.MEMORY_MODULE_TYPES.register(modEventBus);
        Primal_Activities.init(); Primal_Registries.ACTIVITIES.register(modEventBus);

        //Effects
        Primal_Effects.init(); Primal_Registries.MOB_EFFECTS.register(modEventBus);
        //Potions
        Primal_Potions.init(); Primal_Registries.POTIONS.register(modEventBus);

        //Entities
        Primal_Entities.init(); Primal_Registries.ENTITIES.register(modEventBus);

        //Sounds
        Primal_Sounds.init(); Primal_Registries.SOUNDS.register(modEventBus);

        //Blocks
        Primal_Blocks.init(); Primal_Registries.BLOCKS.register(modEventBus);
        Primal_BlockEntities.init(); Primal_Registries.BLOCK_ENTITIES.register(modEventBus);

        //Items
        Primal_Items.initItems(); Primal_Registries.ITEMS.register(modEventBus);
        Primal_Items.initGroups(); Primal_Registries.CREATIVE_MODE_TABS.register(modEventBus);

        //Advancements
        Primal_Advancements.initCriteria(); Primal_Registries.CRITERIA.register(modEventBus);
        Primal_Advancements.initEntitySubPredicates(); Primal_Registries.ENTITY_SUB_PREDICATE_TYPES.register(modEventBus);

        //World Gen
        Primal_WorldGen.init(); Primal_Registries.FEATURES.register(modEventBus);

        Primal_VillagerCustomTrades.init();

        //Biome Modifiers
        createBiomeModifiers(); Primal_Registries.BIOME_MODIFIERS.register(modEventBus);
    }

    public static void createBiomeModifiers() {
        //Mobs
        {
            BearSingle_BiomeModifier.register();
            BearGroup_BiomeModifier.register();
            CrocodileNormal_BiomeModifier.register();
            CrocodileWarm_BiomeModifier.register();
            SharkSingle_BiomeModifier.register();
            SharkGroup_BiomeModifier.register();
        }

        //Features
        {
            RiverReeds_BiomeModifier.register();
            Seashells_BiomeModifier.register();
            EagleNest_BiomeModifier.register();
        }
    }

    @SubscribeEvent
    public static void registerCommonEvent(final FMLCommonSetupEvent event){
        setFlammables();

        ConfigCache.load();
    }

    public static void setFlammables() {
        FireBlock fireblock = (FireBlock) Blocks.FIRE;
        fireblock.setFlammable(Primal_Blocks.SHORT_RIVER_REEDS.get(), 30, 60);
        fireblock.setFlammable(Primal_Blocks.RIVER_REEDS.get(), 30, 60);
        fireblock.setFlammable(Primal_Blocks.NEST_BLOCK.get(), 30, 60);

        fireblock.setFlammable(Primal_Blocks.STRAW_BALE.get(), 100, 200);
    }

    @SuppressWarnings("unused")
    @EventBusSubscriber(modid = Primal_Main.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
    private static class Primal_MainGameBus {

        @SubscribeEvent
        public static void captureDropsForCrocodile(@NotNull LivingDropsEvent event) {
            //If killed by a crocodile
            if(event.getSource().getEntity()!=null &&event.getSource().getEntity() instanceof CrocodileEntity crocodile && !(event.getEntity() instanceof Player)){
                Collection<ItemStack> stacks=new ArrayList<>();
                Collection<ItemEntity> stacksEaten=new ArrayList<>();
                for (ItemEntity itemEntity : event.getDrops()){
                    //If it can actually fit on the crocodile
                    if(crocodile.canEatItem(itemEntity.getItem())){
                        //Adds the item to the stacks that will eat
                        stacks.add(itemEntity.getItem());

                        //Add the entity to removal list
                        stacksEaten.add(itemEntity);
                    }
                }

                for(ItemEntity itemEntity: stacksEaten)
                    event.getDrops().remove(itemEntity);

                crocodile.addItemsToInventory(stacks);
            }
        }

        @SubscribeEvent
        public static void registerBrewingRecipes(RegisterBrewingRecipesEvent event) {
            PotionBrewing.Builder builder = event.getBuilder();

            // Will add brewing recipes for all container potions (e.g. potion, splash potion, lingering potion)
            builder.addMix(
                    Potions.AWKWARD,

                    Primal_Items.SHARK_TOOTH.get(),

                    Primal_Potions.HEAVINESS
            );
        }

        @SubscribeEvent
        public static void modifyLootTables(LootTableLoadEvent event) {

            //Heaviness potion to trial chambers
            if(event.getName().equals(BuiltInLootTables.SPAWNER_TRIAL_ITEMS_TO_DROP_WHEN_OMINOUS.location())){

                var originalTable= event.getTable();

                var originalPool= originalTable.getPool("pool0");

                if(originalPool!=null){

                    LootPoolEntryContainer heaviness_potion =
                            LootItem.lootTableItem(Items.LINGERING_POTION)
                                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                                    .apply(SetPotionFunction.setPotion(Primal_Potions.HEAVINESS)).build();

                    MiscUtil.extendLootPool(originalPool, List.of(heaviness_potion));
                }
            }

            //1-3 Shark tooth in the small chest
            if(event.getName().equals(BuiltInLootTables.UNDERWATER_RUIN_SMALL.location())){

                var originalTable= event.getTable();

                var originalPool= originalTable.getPool("pool1");

                if(originalPool!=null){

                    LootPoolEntryContainer shark_tooth =
                            LootItem.lootTableItem(Primal_Items.SHARK_TOOTH.get())
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f)))
                                    .setWeight(2)
                                    .build();

                    MiscUtil.extendLootPool(originalPool, List.of(shark_tooth));
                }
            }
        }
    }


    @SubscribeEvent
    public static void addToCreativeTabs(@NotNull BuildCreativeModeTabContentsEvent event) {

        if(event.getTabKey().equals(CreativeModeTabs.FOOD_AND_DRINKS)){

            MiscUtil.insertItemsAfter(event,
                    Items.ENCHANTED_GOLDEN_APPLE.getDefaultInstance(),

                    Primal_Items.APPLE_FRITTER.get().getDefaultInstance(),
                    Primal_Items.GOLDEN_APPLE_FRITTER.get().getDefaultInstance(),
                    Primal_Items.ENCHANTED_GOLDEN_APPLE_FRITTER.get().getDefaultInstance());
        }

        if(event.getTabKey().equals(CreativeModeTabs.NATURAL_BLOCKS)){
            MiscUtil.insertItemsAfter(
                    event,
                    Items.SHORT_GRASS.getDefaultInstance(),
                    Primal_Items.SHORT_RIVER_REEDS.get().getDefaultInstance()
            );

            MiscUtil.insertItemsAfter(
                    event,
                    Items.TALL_GRASS.getDefaultInstance(),
                    Primal_Items.RIVER_REEDS.get().getDefaultInstance()
            );

            MiscUtil.insertItemsAfter(
                    event,
                    Items.FROGSPAWN.getDefaultInstance(),
                    Primal_Items.NEST.get().getDefaultInstance()
            );

            MiscUtil.insertItemsAfter(
                    event,
                    Items.TURTLE_EGG.getDefaultInstance(),
                    Primal_Items.CROCODILE_EGG.get().getDefaultInstance(),
                    Primal_Items.EAGLE_EGG.get().getDefaultInstance()
            );

            MiscUtil.insertItemsAfter(
                    event,
                    Items.LILY_PAD.getDefaultInstance(),
                    Primal_Items.SEASHELLS.get().getDefaultInstance()
            );

            MiscUtil.insertItemsAfter(
                    event,
                    Items.HAY_BLOCK.getDefaultInstance(),
                    Primal_Items.STRAW_BLOCK.get().getDefaultInstance()
            );
        }

        if(event.getTabKey().equals(CreativeModeTabs.INGREDIENTS)){
            MiscUtil.insertItemsAfter(
                    event,
                    Items.TURTLE_SCUTE.getDefaultInstance(),
                    Primal_Items.CROCODILE_SCUTE.get().getDefaultInstance()
            );

            MiscUtil.insertItemsAfter(
                    event,
                    Items.PRISMARINE_CRYSTALS.getDefaultInstance(),
                    Primal_Items.SHARK_TOOTH.get().getDefaultInstance()
            );

            MiscUtil.insertItemsAfter(
                    event,
                    Items.FLOWER_BANNER_PATTERN.getDefaultInstance(),
                    Primal_Items.PAW_BANNER_PATTERN.get().getDefaultInstance(),
                    Primal_Items.JAWS_BANNER_PATTERN.get().getDefaultInstance(),
                    Primal_Items.MARSH_BANNER_PATTERN.get().getDefaultInstance(),
                    Primal_Items.EYRIE_BANNER_PATTERN.get().getDefaultInstance()
            );
        }

        if(event.getTabKey().equals(CreativeModeTabs.BUILDING_BLOCKS)){
            MiscUtil.insertItemsAfter(
                    event,
                    Items.DARK_PRISMARINE_SLAB.getDefaultInstance(),
                    Primal_Items.CROCODILE_SCUTE_BLOCK.get().getDefaultInstance(),
                    Primal_Items.CROCODILE_SCUTE_STAIRS.get().getDefaultInstance(),
                    Primal_Items.CROCODILE_SCUTE_SLAB.get().getDefaultInstance(),
                    Primal_Items.CHISELED_CROCODILE_SCUTE.get().getDefaultInstance(),
                    Primal_Items.CROCODILE_SCUTE_SHINGLE.get().getDefaultInstance(),

                    Primal_Items.ARID_CROCODILE_SCUTE_BLOCK.get().getDefaultInstance(),
                    Primal_Items.ARID_CROCODILE_SCUTE_STAIRS.get().getDefaultInstance(),
                    Primal_Items.ARID_CROCODILE_SCUTE_SLAB.get().getDefaultInstance(),
                    Primal_Items.ARID_CHISELED_CROCODILE_SCUTE.get().getDefaultInstance(),
                    Primal_Items.ARID_CROCODILE_SCUTE_SHINGLE.get().getDefaultInstance(),

                    Primal_Items.HUMID_CROCODILE_SCUTE_BLOCK.get().getDefaultInstance(),
                    Primal_Items.HUMID_CROCODILE_SCUTE_STAIRS.get().getDefaultInstance(),
                    Primal_Items.HUMID_CROCODILE_SCUTE_SLAB.get().getDefaultInstance(),
                    Primal_Items.HUMID_CHISELED_CROCODILE_SCUTE.get().getDefaultInstance(),
                    Primal_Items.HUMID_CROCODILE_SCUTE_SHINGLE.get().getDefaultInstance()
            );
        }

        if(event.getTabKey().equals(CreativeModeTabs.SPAWN_EGGS)){
            //Bat -> Bear -> Bee
            event.insertAfter(Items.BAT_SPAWN_EGG.getDefaultInstance(),
                    Primal_Items.BEAR_SPAWN_EGG.get().getDefaultInstance(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            //Creeper -> Crocodile
            event.insertAfter(Items.CREEPER_SPAWN_EGG.getDefaultInstance(),
                    Primal_Items.CROCODILE_SPAWN_EGG.get().getDefaultInstance(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            //Drowned -> Eagle -> Elder Guardian
            event.insertAfter(Items.DROWNED_SPAWN_EGG.getDefaultInstance(),
                    Primal_Items.EAGLE_SPAWN_EGG.get().getDefaultInstance(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            //Salmon -> Shark -> Sheep
            event.insertAfter(Items.SALMON_SPAWN_EGG.getDefaultInstance(),
                    Primal_Items.SHARK_SPAWN_EGG.get().getDefaultInstance(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }

    @SubscribeEvent
    public static void registerAttribute(@NotNull EntityAttributeCreationEvent event) {
        event.put(Primal_Entities.BEAR.get(), BearEntity.createAttributes().build());
        event.put(Primal_Entities.SHARK.get(), SharkEntity.createAttributes().build());
        event.put(Primal_Entities.CROCODILE.get(), CrocodileEntity.createAttributes().build());
        event.put(Primal_Entities.EAGLE.get(), EagleEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(final @NotNull RegisterSpawnPlacementsEvent event){
        //Bear
        event.register(Primal_Entities.BEAR.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                BearEntity::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        //Shark
        event.register(Primal_Entities.SHARK.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                SharkEntity::checkSurfaceWaterAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        //Crocodile
        event.register(Primal_Entities.CROCODILE.get(), SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                CrocodileEntity::checkCrocodileSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        //Eagle
        event.register(Primal_Entities.EAGLE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                BearEntity::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }
}
