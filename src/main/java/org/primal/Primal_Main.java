package org.primal;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import org.jetbrains.annotations.NotNull;
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
    
    public Primal_Main(IEventBus modEventBus) {
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
        public static void customizeOverlay(RenderGuiLayerEvent.@NotNull Pre event) {
            if (VanillaGuiLayers.VEHICLE_HEALTH == event.getName()) {
                assert Minecraft.getInstance().player != null;
                if(MiscUtil.isRidingUnfriendly(Minecraft.getInstance().player))
                    event.setCanceled(true);
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
