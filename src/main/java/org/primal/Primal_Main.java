package org.primal;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.neoforged.neoforge.event.GrindstoneEvent;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.AnvilRepairEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.primal.biome_modifiers.features.*;
import org.primal.biome_modifiers.mobs.*;
import org.primal.block.ChompTrapBlock;
import org.primal.block_entity.ChompTrapBlockEntity;
import org.primal.entity.animal.*;
import org.primal.item.ConchShellItem;
import org.primal.item.SnakeItem;
import org.primal.item.component.HelmetDecorationComponent;
import org.primal.networking.DelayedTasks;
import org.primal.networking.Primal_HandlePackets;
import org.primal.networking.packets.WalrusJumpPacket;
import org.primal.registry.*;
import org.primal.server_data.ConchShellsData;
import org.primal.util.Primal_Util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("removal")
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
        public static boolean rabbitModelChange;
        public static boolean wolfModelChange;
        public static boolean dolphinModelChange;

        public static void load() {
            foxModelChange = Primal_Main.COMMON_CONFIG.foxModelChange.get();
            polarBearModelChange = Primal_Main.COMMON_CONFIG.polarBearModelChange.get();
            rabbitModelChange = Primal_Main.COMMON_CONFIG.rabbitModelChange.get();
            wolfModelChange = Primal_Main.COMMON_CONFIG.wolfModelChange.get();
            dolphinModelChange = Primal_Main.COMMON_CONFIG.dolphinModelChange.get();
        }
    }

    public Primal_Main(IEventBus modEventBus) {
        ModList.get().getModContainerById(Primal_Main.MOD_ID)
                .ifPresent(
                        modContainer ->
                                modContainer.registerConfig(ModConfig.Type.COMMON, COMMON_SPEC)
                );

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
        Primal_Items.Components.init(); Primal_Registries.DATA_COMPONENTS.register(modEventBus);

        //Advancements
        Primal_Advancements.initCriteria(); Primal_Registries.CRITERIA.register(modEventBus);
        Primal_Advancements.initEntitySubPredicates(); Primal_Registries.ENTITY_SUB_PREDICATE_TYPES.register(modEventBus);

        //World Gen
        Primal_WorldGen.init(); Primal_Registries.FEATURES.register(modEventBus);
        Primal_WorldGen.TreeDecorators.init(); Primal_Registries.TREE_DECORATORS.register(modEventBus);
        Primal_WorldGen.BlockStateProviders.init(); Primal_Registries.BLOCK_STATE_PROVIDERS.register(modEventBus);

        Primal_VillagerCustomTrades.init();

        //Biome Modifiers
        createBiomeModifiers(); Primal_Registries.BIOME_MODIFIERS.register(modEventBus);

        //Particles
        Primal_Particles.init(); Primal_Registries.PARTICLES.register(modEventBus);

        //Helmet Decorations
        Primal_HelmetDecorations.init(); Primal_Registries.HELMET_DECORATIONS.register(modEventBus);
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
            WalrusNormal_BiomeModifier.register();
            WalrusOcean_BiomeModifier.register();
            LionSavanna_BiomeModifier.register();
            LionSnowy_BiomeModifier.register();
            Snake_BiomeModifier.register();
            DeerForest_BiomeModifier.register();
            DeerSnowy_BiomeModifier.register();
            DolphinCold_BiomeModifier.register();
            RabbitBadlands_BiomeModifier.register();
        }

        //Features
        {
            RiverReeds_BiomeModifier.register();
            Cattails_BiomeModifier.register();
            Seashells_BiomeModifier.register();
            EagleNest_BiomeModifier.register();
            CassowaryNest_BiomeModifier.register();
        }
    }

    @SubscribeEvent
    public static void registerCommonEvent(final FMLCommonSetupEvent event){
        ConfigCache.load();
        Primal_Main.setFlammables();
        Primal_Main.setStrippables(event);
    }

    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        event.register(Primal_Registries.HELMET_DECORATIONS_REGISTRY);
    }

    public static void setStrippables(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            AxeItem.STRIPPABLES = new ImmutableMap.Builder<Block, Block>()
                    .putAll(AxeItem.STRIPPABLES)

                    .put(Primal_Blocks.THORNY_ACACIA_LOG.get(), Blocks.STRIPPED_ACACIA_LOG)
                    .put(Primal_Blocks.THORNY_ACACIA_WOOD.get(), Blocks.STRIPPED_ACACIA_WOOD)

                    .build();
        });
    }

    public static void setFlammables() {
        FireBlock fireblock = (FireBlock) Blocks.FIRE;
        fireblock.setFlammable(Primal_Blocks.SHORT_RIVER_REEDS.get(), 30, 60);
        fireblock.setFlammable(Primal_Blocks.RIVER_REEDS.get(), 30, 60);
        fireblock.setFlammable(Primal_Blocks.CATTAILS.get(), 30, 60);
        fireblock.setFlammable(Primal_Blocks.NEST_BLOCK.get(), 30, 60);

        fireblock.setFlammable(Primal_Blocks.STRAW_BALE.get(), 70, 100);
        fireblock.setFlammable(Primal_Blocks.DRIED_STRAW_BALE.get(), 80, 120);
        fireblock.setFlammable(Primal_Blocks.WEAVED_STRAW.get(), 80, 120);
        fireblock.setFlammable(Primal_Blocks.STRAW_BASKET.get(), 80, 120);
    }

    @SuppressWarnings("unused")
    @EventBusSubscriber(modid = Primal_Main.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
    private static class Primal_MainGameBus {

        @SubscribeEvent
        public static void dropsEvent(@NotNull LivingDropsEvent event) {
            //Chomp
            if(event.getSource().is(Primal_DamageTypes.CHOMP_TRAP)){
                if(event.getSource().getSourcePosition()==null) return;
                var pos = event.getSource().getSourcePosition();

                if(event.getEntity().level().getBlockState(BlockPos.containing(pos)).getBlock() instanceof ChompTrapBlock){
                    captureDropsForChompTrap(event.getEntity().level(), BlockPos.containing(pos), event.getDrops());
                }
            }

            //If killed by a crocodile
            if(event.getSource().getEntity()!=null &&event.getSource().getEntity() instanceof CrocodileEntity crocodile){
                captureDropsForCrocodile(crocodile, event.getDrops());
            }
        }

        private static void captureDropsForChompTrap(Level level, BlockPos pos, Collection<ItemEntity> drops){
            Collection<ItemStack> stacks=new ArrayList<>();
            Collection<ItemEntity> stacksEaten=new ArrayList<>();
            if(!(level.getBlockEntity(pos) instanceof ChompTrapBlockEntity chompTrapBlockEntity)) return;

            for (ItemEntity itemEntity : drops){
//                if(chompTrapBlockEntity.canFit(itemEntity.getItem())){
                    //Adds the item to the stacks that will eat
                    stacks.add(itemEntity.getItem());

                    //Add the entity to removal list
                    stacksEaten.add(itemEntity);
//                }
            }

            for(ItemEntity itemEntity: stacksEaten)
                drops.remove(itemEntity);

            stacks.forEach(chompTrapBlockEntity::insertItem);
        }

        private static void captureDropsForCrocodile(CrocodileEntity crocodile, Collection<ItemEntity> drops){
            Collection<ItemStack> stacks=new ArrayList<>();
            Collection<ItemEntity> stacksEaten=new ArrayList<>();
            for (ItemEntity itemEntity : drops){
                //If it can actually fit on the crocodile
                if(crocodile.canEatItem(itemEntity.getItem())){
                    //Adds the item to the stacks that will eat
                    stacks.add(itemEntity.getItem());

                    //Add the entity to removal list
                    stacksEaten.add(itemEntity);
                }
            }

            for(ItemEntity itemEntity: stacksEaten)
                drops.remove(itemEntity);

            crocodile.addItemsToInventory(stacks);
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

            builder.addMix(
                    Potions.AWKWARD,
                    Primal_Items.EXPLOSEED.get(),
                    Primal_Potions.THORNED
            );

            builder.addMix(
                    Primal_Potions.THORNED,
                    Items.GLOWSTONE_DUST,
                    Primal_Potions.STRONG_THORNED
            );

            builder.addMix(
                    Primal_Potions.THORNED,
                    Items.REDSTONE,
                    Primal_Potions.LONG_THORNED
            );
        }

        @SubscribeEvent
        public static void modifyLootTables(LootTableLoadEvent event) {

            //Heaviness potion to trial chambers
            if(Primal_Util.isLootTable(event, BuiltInLootTables.SPAWNER_TRIAL_ITEMS_TO_DROP_WHEN_OMINOUS)){

                var originalTable= event.getTable();

                var originalPool= originalTable.getPool("pool0");

                if(originalPool!=null){

                    LootPoolEntryContainer heaviness_potion =
                            LootItem.lootTableItem(Items.LINGERING_POTION)
                                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                                    .apply(SetPotionFunction.setPotion(Primal_Potions.HEAVINESS)).build();

                    Primal_Util.extendLootPool(originalPool, List.of(heaviness_potion));
                }
            }

            if(Primal_Util.isLootTable(event, BuiltInLootTables.UNDERWATER_RUIN_SMALL)){

                var originalTable= event.getTable();

//                var firstPool= originalTable.getPool("pool0");
                var secondPool= originalTable.getPool("pool1");

                //Add snakes to pool
                event.getTable().addPool(SnakeItem.getMarineSnakePool(0.20f));

                //1-3 Shark tooth in the small chest
                if(secondPool!=null){

                    LootPoolEntryContainer shark_tooth =
                            LootItem.lootTableItem(Primal_Items.SHARK_TOOTH.get())
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f)))
                                    .setWeight(2)
                                    .build();

                    Primal_Util.extendLootPool(secondPool, List.of(shark_tooth));
                }
            }

            SnakeItem.addMarineSnakeToLootTable(event, BuiltInLootTables.UNDERWATER_RUIN_BIG, 0.10f);

            SnakeItem.addSnakeToLootTable(event, BuiltInLootTables.SIMPLE_DUNGEON, 0.05f);

            SnakeItem.addSnakeToLootTable(event, BuiltInLootTables.ABANDONED_MINESHAFT, 0.20f);

            SnakeItem.addSnakeToLootTable(event, BuiltInLootTables.STRONGHOLD_LIBRARY, 0.10f);
            SnakeItem.addSnakeToLootTable(event, BuiltInLootTables.STRONGHOLD_CROSSING, 0.15f);
            SnakeItem.addSnakeToLootTable(event, BuiltInLootTables.STRONGHOLD_CORRIDOR, 0.15f);

            SnakeItem.addSnakeToLootTable(event, BuiltInLootTables.DESERT_PYRAMID, 0.25f);
            SnakeItem.addSnakeToLootTable(event, BuiltInLootTables.JUNGLE_TEMPLE, 0.25f);

            SnakeItem.addMarineSnakeToLootTable(event, BuiltInLootTables.SHIPWRECK_MAP, 0.10f);
            SnakeItem.addMarineSnakeToLootTable(event, BuiltInLootTables.SHIPWRECK_SUPPLY, 0.15f);
            SnakeItem.addMarineSnakeToLootTable(event, BuiltInLootTables.SHIPWRECK_TREASURE, 0.05f);

            SnakeItem.addSnakeToLootTable(event, BuiltInLootTables.RUINED_PORTAL, 0.15f);

            SnakeItem.addSnakeToLootTable(event, BuiltInLootTables.TRIAL_CHAMBERS_SUPPLY, 0.15f);
            SnakeItem.addSnakeToLootTable(event, BuiltInLootTables.TRIAL_CHAMBERS_CORRIDOR, 0.10f);
            if(Primal_Util.isLootTable(event, BuiltInLootTables.TRIAL_CHAMBERS_CORRIDOR_POT)){
                var originalTable= event.getTable();
                var pool = originalTable.getPool("main");

                if(pool!=null) Primal_Util.extendLootPool(pool, List.of(SnakeItem.getSnakeItemEntry().setWeight(10).build()));
            }
            SnakeItem.addSnakeToLootTable(event, BuiltInLootTables.TRIAL_CHAMBERS_INTERSECTION, 0.10f);
            SnakeItem.addSnakeToLootTable(event, BuiltInLootTables.TRIAL_CHAMBERS_INTERSECTION_BARREL, 0.10f);
            SnakeItem.addSnakeToLootTable(event, BuiltInLootTables.TRIAL_CHAMBERS_ENTRANCE, 0.05f);
        }

        @SubscribeEvent
        public static void anvilUpdateEvent(AnvilUpdateEvent event){
            if(event.getLeft().is(Primal_Tags.Item.HELMET_ATTACHMENTS) && event.getRight().is(ItemTags.HEAD_ARMOR)){
                var helmet = event.getRight().copy();
                HelmetDecorationComponent.of(helmet, event.getLeft(), true);

                event.setOutput(helmet);
                event.setCost(1);
                event.setMaterialCost(1);
            }

            if(event.getRight().is(Primal_Tags.Item.HELMET_ATTACHMENTS) && event.getLeft().is(ItemTags.HEAD_ARMOR)){
                var helmet = event.getLeft().copy();
                HelmetDecorationComponent.of(helmet, event.getRight(), false);

                event.setOutput(helmet);
                event.setCost(1);
                event.setMaterialCost(1);
            }
        }

        @SubscribeEvent
        public static void anvilRemoveEvent(AnvilRepairEvent event){
            var decorations = event.getOutput().get(Primal_Items.Components.HELMET_DECORATION);

            if(event.getEntity() instanceof ServerPlayer serverPlayer && decorations!=null){
                //Triggers advancement
                Primal_Advancements.ADD_HELMET_DECORATION.get().trigger(serverPlayer);

                //Triggers advancement
                if(decorations.right().equals(Primal_HelmetDecorations.GOAT.get()) && decorations.left().equals(Primal_HelmetDecorations.GOAT.get())){
                    Primal_Advancements.ADD_HELMET_HORNS.get().trigger(serverPlayer);
                }
            }
        }
    }

    @SubscribeEvent
    public static void registerAttribute(@NotNull EntityAttributeCreationEvent event) {
        event.put(Primal_Entities.BEAR.get(), BearEntity.createAttributes().build());
        event.put(Primal_Entities.SHARK.get(), SharkEntity.createAttributes().build());
        event.put(Primal_Entities.CROCODILE.get(), CrocodileEntity.createAttributes().build());
        event.put(Primal_Entities.EAGLE.get(), EagleEntity.createAttributes().build());
        event.put(Primal_Entities.CASSOWARY.get(), CassowaryEntity.createAttributes().build());
        event.put(Primal_Entities.WALRUS.get(), WalrusEntity.createAttributes().build());
        event.put(Primal_Entities.LION.get(), LionEntity.createAttributes().build());
        event.put(Primal_Entities.SNAKE.get(), SnakeEntity.createAttributes().build());
        event.put(Primal_Entities.DEER.get(), DeerEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(final @NotNull RegisterSpawnPlacementsEvent event){
        //For natural generation
        //Bear
        event.register(Primal_Entities.BEAR.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                BearEntity::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        //Shark
        event.register(Primal_Entities.SHARK.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                SharkEntity::sharkSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        //Crocodile
        event.register(Primal_Entities.CROCODILE.get(), SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                CrocodileEntity::checkCrocodileSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        //Eagle
        event.register(Primal_Entities.EAGLE.get(), SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                EagleEntity::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        //Cassowary
        event.register(Primal_Entities.CASSOWARY.get(), SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                CassowaryEntity::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        //Walrus
        event.register(Primal_Entities.WALRUS.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                WalrusEntity::checkWalrusSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        //Lion
        event.register(Primal_Entities.LION.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                LionEntity::checkLionSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        //Snake
        event.register(Primal_Entities.SNAKE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                SnakeEntity::checkSnakeSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        //Deer
        event.register(Primal_Entities.DEER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                DeerEntity::checkDeerSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }

    @SubscribeEvent
    public static void registerNetworking(final RegisterPayloadHandlersEvent event) {
        // Sets the current network version
        final PayloadRegistrar mainThread = event.registrar("1");

        mainThread.playToServer(
                WalrusJumpPacket.TYPE,
                WalrusJumpPacket.STREAM_CODEC,
                Primal_HandlePackets.OnServer::handleWalrusJumpPacket
        );
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        ServerLevel serverLevel = event.getServer().overworld();
        ConchShellsData conchShellsData = ConchShellsData.get(serverLevel);

        List<Entity> toRemove = new ArrayList<>();

        for (UUID uuid: conchShellsData.getPets().keySet()){
            var dimension = conchShellsData.getDimension(uuid);
            var dimensionLevel = event.getServer().getLevel(dimension);
            if(dimensionLevel==null) continue;

            var pet = dimensionLevel.getEntity(uuid);
            if(pet==null) continue;

            conchShellsData.addPet(pet);

            if (!pet.isAlive()) toRemove.add(pet);
        }

        for (Entity entity : toRemove) conchShellsData.removePet(entity);

        DelayedTasks.tick();
    }

    @SubscribeEvent
    public static void entityJoinLevel(EntityJoinLevelEvent event) {}

    @SubscribeEvent
    public static void entityLeavesLevel(EntityLeaveLevelEvent event) {

        //If variantFromBiome dies
        if(event.getEntity().getRemovalReason() !=null && event.getEntity().getRemovalReason().equals(Entity.RemovalReason.KILLED)){

            if(event.getLevel().getServer()==null) return;
            MinecraftServer server = event.getLevel().getServer();

            ServerLevel serverLevel = server.overworld();
            ConchShellsData conchShellsData = ConchShellsData.get(serverLevel);

            if(conchShellsData.hasPet(event.getEntity())){
                conchShellsData.removePet(event.getEntity());
            }
        }
    }

    @SubscribeEvent
    public static void onGrindstonePlace(GrindstoneEvent.OnPlaceItem event) {
        ItemStack outputStack = ItemStack.EMPTY;

        if(event.getTopItem().getItem() instanceof ConchShellItem && event.getBottomItem().isEmpty())
            outputStack = ConchShellItem.removePet(event.getTopItem().copy());

        if(event.getBottomItem().getItem() instanceof ConchShellItem && event.getTopItem().isEmpty())
            outputStack = ConchShellItem.removePet(event.getBottomItem().copy());

        event.setOutput(outputStack);
    }
}
