package org.primal;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.*;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.primal.biome_modifiers.features.*;
import org.primal.biome_modifiers.mobs.*;
import org.primal.block.ChompTrapBlock;
import org.primal.block_entity.ChompTrapBlockEntity;
import org.primal.datagen.providers.Primal_DataMapGenerator;
import org.primal.entity.animal.*;
import org.primal.item.ConchShellItem;
import org.primal.item.HelmetDecorationType;
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

@Mod.EventBusSubscriber(modid = Primal_Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@Mod(Primal_Main.MOD_ID)
public class Primal_Main {
    public static final String MOD_ID = "primal";
    public static final Primal_Config COMMON_CONFIG;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        Pair<Primal_Config, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder()
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

    public Primal_Main(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        context.registerConfig(ModConfig.Type.COMMON, COMMON_SPEC);

        //AI
        Primal_Sensors.init(); Primal_Registries.SENSOR_TYPES.register(modEventBus);
        Primal_MemoryModuleTypes.init(); Primal_Registries.MEMORY_MODULE_TYPES.register(modEventBus);
        Primal_Activities.init(); Primal_Registries.ACTIVITIES.register(modEventBus);

        Primal_EntityAttributes.init(); Primal_Registries.ATTRIBUTES.register(modEventBus);

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
        Primal_Items.Components.init(); Primal_Registries.LOOT_FUNCTIONS.register(modEventBus);

        //Advancements
        Primal_Advancements.initCriteria();
        Primal_Advancements.initEntitySubPredicates();

        //World Gen
        Primal_WorldGen.init(); Primal_Registries.FEATURES.register(modEventBus);
        Primal_WorldGen.TreeDecorators.init(); Primal_Registries.TREE_DECORATORS.register(modEventBus);
        Primal_WorldGen.BlockStateProviders.init(); Primal_Registries.BLOCK_STATE_PROVIDERS.register(modEventBus);

        Primal_VillagerCustomTrades.init();

        Primal_BannerPatterns.init(); Primal_Registries.BANNER_PATTERNS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        //Biome Modifiers
        createBiomeModifiers(modEventBus);

        //Particles
        Primal_Particles.init(); Primal_Registries.PARTICLES.register(modEventBus);

        registerNetworking();
    }

    public static void createBiomeModifiers(IEventBus modEventBus) {
        final DeferredRegister<Codec<? extends BiomeModifier>> biomeModifiers =
                DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Primal_Main.MOD_ID);
        biomeModifiers.register(modEventBus);


        //Mobs
        {
            biomeModifiers.register("bear_single_spawn", BearSingle_BiomeModifier::makeCodec);
            biomeModifiers.register("bear_group_spawn", BearGroup_BiomeModifier::makeCodec);
            biomeModifiers.register("crocodile_normal_spawn", CrocodileNormal_BiomeModifier::makeCodec);
            biomeModifiers.register("crocodile_warm_spawn", CrocodileWarm_BiomeModifier::makeCodec);
            biomeModifiers.register("shark_single_spawn", SharkSingle_BiomeModifier::makeCodec);
            biomeModifiers.register("shark_group_spawn", SharkGroup_BiomeModifier::makeCodec);
            biomeModifiers.register("walrus_spawn", WalrusNormal_BiomeModifier::makeCodec);
            biomeModifiers.register("walrus_ocean_spawn", WalrusOcean_BiomeModifier::makeCodec);
            biomeModifiers.register("lion_savanna_spawn", LionSavanna_BiomeModifier::makeCodec);
            biomeModifiers.register("lion_snowy_spawn", LionSnowy_BiomeModifier::makeCodec);
            biomeModifiers.register("snake_spawn", Snake_BiomeModifier::makeCodec);
            biomeModifiers.register("deer_forest_spawn", DeerForest_BiomeModifier::makeCodec);
            biomeModifiers.register("deer_snowy_spawn", DeerSnowy_BiomeModifier::makeCodec);
            biomeModifiers.register("cold_dolphin_spawn", DolphinCold_BiomeModifier::makeCodec);
            biomeModifiers.register("badlands_rabbit_spawn", RabbitBadlands_BiomeModifier::makeCodec);
        }

        //Features
        {
            biomeModifiers.register("river_reeds_patch_spawn", RiverReeds_BiomeModifier::makeCodec);
            biomeModifiers.register("cattails_patch_spawn", Cattails_BiomeModifier::makeCodec);
            biomeModifiers.register("seashells_patch_spawn", Seashells_BiomeModifier::makeCodec);
            biomeModifiers.register("eagle_nest_spawn", EagleNest_BiomeModifier::makeCodec);
            biomeModifiers.register("cassowary_nest_spawn", CassowaryNest_BiomeModifier::makeCodec);
        }
    }

    @SubscribeEvent
    public static void registerCommonEvent(final FMLCommonSetupEvent event){
        setFlammables();
        ConfigCache.load();

        Primal_DataMapGenerator.setSimilarToDataMaps();
        Primal_MainGameBus.registerBrewingRecipes();
        Primal_Main.setStrippables(event);
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
    @Mod.EventBusSubscriber(modid = Primal_Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
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

        public static void registerBrewingRecipes() {

            // Will add brewing recipes for all container potions (e.g. potion, splash potion, lingering potion)
//            builder.addMix(
//                    Potions.AWKWARD,
//
//                    Primal_Items.SHARK_TOOTH.get(),
//
//                    Primal_Potions.HEAVINESS
//            );

            BrewingRecipeRegistry.addRecipe(
                    Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD)),
                    Ingredient.of(Primal_Items.EXPLOSEED.get()),
                    PotionUtils.setPotion(new ItemStack(Items.POTION), Primal_Potions.THORNED.get())
            );

            BrewingRecipeRegistry.addRecipe(
                    Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Primal_Potions.THORNED.get())),
                    Ingredient.of(Items.GLOWSTONE_DUST),
                    PotionUtils.setPotion(new ItemStack(Items.POTION), Primal_Potions.STRONG_THORNED.get())
            );

            BrewingRecipeRegistry.addRecipe(
                    Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Primal_Potions.THORNED.get())),
                    Ingredient.of(Items.REDSTONE),
                    PotionUtils.setPotion(new ItemStack(Items.POTION), Primal_Potions.LONG_THORNED.get())
            );
        }

        @SubscribeEvent
        public static void modifyLootTables(LootTableLoadEvent event) {

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
        }

        @SubscribeEvent
        public static void anvilUpdateEvent(AnvilUpdateEvent event){
            if(event.getLeft().is(Primal_Tags.Item.HELMET_ATTACHMENTS) &&
                    (event.getRight().getItem() instanceof ArmorItem armorItem && armorItem.getType().equals(ArmorItem.Type.HELMET))){
                var helmet = event.getRight().copy();
                HelmetDecorationComponent.of(helmet, event.getLeft(), true);

                event.setOutput(helmet);
                event.setCost(1);
                event.setMaterialCost(1);
            }

            if(event.getRight().is(Primal_Tags.Item.HELMET_ATTACHMENTS) &&
                    (event.getLeft().getItem() instanceof ArmorItem armorItem && armorItem.getType().equals(ArmorItem.Type.HELMET))){
                var helmet = event.getLeft().copy();
                HelmetDecorationComponent.of(helmet, event.getRight(), false);

                event.setOutput(helmet);
                event.setCost(1);
                event.setMaterialCost(1);
            }
        }

        @SubscribeEvent
        public static void anvilRemoveEvent(AnvilRepairEvent event){
            var decorations = Primal_Util.OneTwentyEquivalent.Components.get(event.getOutput(), Primal_Items.Components.HELMET_DECORATION);

            if(event.getEntity() instanceof ServerPlayer serverPlayer && decorations!=null){
                //Triggers advancement
                Primal_Advancements.ADD_HELMET_DECORATION.trigger(serverPlayer);

                //Triggers advancement
                if(decorations.right().equals(HelmetDecorationType.GOAT) && decorations.left().equals(HelmetDecorationType.GOAT)){
                    Primal_Advancements.ADD_HELMET_HORNS.trigger(serverPlayer);
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

        @SubscribeEvent
        public static void onServerTick(TickEvent.ServerTickEvent event) {
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
    public static void registerSpawnPlacements(final @NotNull SpawnPlacementRegisterEvent event){
        //For natural generation
        //Bear
        event.register(Primal_Entities.BEAR.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                BearEntity::checkAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);

        //Shark
        event.register(Primal_Entities.SHARK.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                SharkEntity::sharkSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);

        //Crocodile
        event.register(Primal_Entities.CROCODILE.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                CrocodileEntity::checkCrocodileSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);

        //Eagle
        event.register(Primal_Entities.EAGLE.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                EagleEntity::checkAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);

        //Cassowary
        event.register(Primal_Entities.CASSOWARY.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                CassowaryEntity::checkAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);

        //Walrus
        event.register(Primal_Entities.WALRUS.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                WalrusEntity::checkWalrusSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);

        //Lion
        event.register(Primal_Entities.LION.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                LionEntity::checkLionSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);

        //Snake
        event.register(Primal_Entities.SNAKE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                SnakeEntity::checkSnakeSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);

        //Deer
        event.register(Primal_Entities.DEER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                DeerEntity::checkDeerSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
    }

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void registerNetworking() {
        int i=0;

        Primal_Main.INSTANCE.registerMessage(
                i++,
                WalrusJumpPacket.class,
                WalrusJumpPacket::write,
                WalrusJumpPacket::read,
                Primal_HandlePackets.OnServer::handleWalrusJumpPacket
        );
    }
}
