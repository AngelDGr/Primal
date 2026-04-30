package org.primal.registry;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.sensing.TemptingSensor;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import org.primal.Primal_Registries;

import org.primal.entity.ai.*;
import org.primal.entity.ai.sensors.*;
import org.primal.entity.ai.sensors.crocodile.CrocodileNearestImportantBlockSensor;
import org.primal.entity.ai.sensors.generic.*;
import org.primal.entity.ai.sensors.bear.*;
import org.primal.entity.ai.sensors.eagle.*;
import org.primal.entity.ai.sensors.snake.*;
import org.primal.entity.animal.*;

import net.minecraft.world.entity.ai.sensing.SensorType;
import net.neoforged.neoforge.registries.DeferredHolder;

public final class Primal_Sensors {

    //Bear
    public static DeferredHolder<SensorType<?>, SensorType<BearEntitySensor>> BEAR_ENTITY_SENSOR =
            Primal_Registries.SENSOR_TYPES.register("bear_entity_sensor",
                    () -> new SensorType<>(BearEntitySensor::new));

    public static DeferredHolder<SensorType<?>, SensorType<BearNearestImportantBlock>> BEAR_NEAREST_RAIDABLE_BLOCK_SENSOR =
            Primal_Registries.SENSOR_TYPES.register("bear_nearest_raidable_block_sensor",
                    () -> new SensorType<>(BearNearestImportantBlock::new));

    public static final DeferredHolder<SensorType<?>, SensorType<TemptingSensor>> BEAR_TEMPTATIONS_SENSOR =
            Primal_Registries.SENSOR_TYPES.register("bear_temptations",
                    () -> new SensorType<>(() -> new TemptingSensor(BearAi.getTemptations())));

    public static final DeferredHolder<SensorType<?>, SensorType<BearNearestAdultSensor>> NEAREST_ADULT_BEAR =
            Primal_Registries.SENSOR_TYPES.register("nearest_adult_bear",
                    () -> new SensorType<>(BearNearestAdultSensor::new));

    public static final DeferredHolder<SensorType<?>, SensorType<BearRepellentSensor>> BEAR_REPELLENT_SENSOR =
            Primal_Registries.SENSOR_TYPES.register("bear_repellent",
                    () -> new SensorType<>(BearRepellentSensor::new));

    //Shark
    public static DeferredHolder<SensorType<?>, SensorType<SharkNearConduitPlayerSensor>> SHARK_NEAR_CONDUIT_PLAYER =
            Primal_Registries.SENSOR_TYPES.register("shark_near_conduit_player",
                    () -> new SensorType<>(SharkNearConduitPlayerSensor::new));

    public static DeferredHolder<SensorType<?>, SensorType<NearestSpecificBlockSensor<SharkEntity>>> SHARK_NEAREST_CONDUIT =
            Primal_Registries.SENSOR_TYPES.register("shark_near_conduit",
                    () -> new SensorType<>(()-> new NearestSpecificBlockSensor<>(Primal_Tags.Block.SHARK_ATTRACTORS, 24, 24)));

    //Crocodile
    public static final DeferredHolder<SensorType<?>, SensorType<TemptingSensor>> CROCODILE_TEMPTATIONS_SENSOR =
            Primal_Registries.SENSOR_TYPES.register("crocodile_temptations",
                    () -> new SensorType<>(() -> new TemptingSensor(CrocodileAi.getTemptations())));

    public static DeferredHolder<SensorType<?>, SensorType<CrocodileNearestImportantBlockSensor>> CROCODILE_NEAREST_IMPORTANT_BLOCK =
            Primal_Registries.SENSOR_TYPES.register("crocodile_nearest_important_block",
                    () -> new SensorType<>(CrocodileNearestImportantBlockSensor::new));

    //Eagle
    public static DeferredHolder<SensorType<?>, SensorType<EagleEntitySensor>> EAGLE_ENTITY_SENSOR =
            Primal_Registries.SENSOR_TYPES.register("eagle_entity_sensor",
                    () -> new SensorType<>(EagleEntitySensor::new));

    public static final DeferredHolder<SensorType<?>, SensorType<TemptingSensor>> EAGLE_TEMPTATIONS_SENSOR =
            Primal_Registries.SENSOR_TYPES.register("eagle_temptations",
                    () -> new SensorType<>(() -> new TemptingSensor(EagleAi.getTemptations())));

    public static final DeferredHolder<SensorType<?>, SensorType<ScaredSensor<EagleEntity>>> EAGLE_SCARE_SENSOR =
            Primal_Registries.SENSOR_TYPES.register("eagle_scare_sensor",
                    () -> new SensorType<>(() -> new ScaredSensor<>(
                            //Not scared from Animals
                            target -> !(target instanceof Animal) && target.isSprinting(),
                            //Not scared if its tamed
                            eagle -> !eagle.isTame())));

    public static DeferredHolder<SensorType<?>, SensorType<EagleNearestHostile>> EAGLE_HOSTILE_SENSOR =
            Primal_Registries.SENSOR_TYPES.register("eagle_nearest_hostile_sensor",
                    () -> new SensorType<>(EagleNearestHostile::new));

    //Cassowary
    public static final DeferredHolder<SensorType<?>, SensorType<TemptingSensor>> CASSOWARY_TEMPTATIONS_SENSOR =
            Primal_Registries.SENSOR_TYPES.register("cassowary_temptations",
                    () -> new SensorType<>(() -> new TemptingSensor(CassowaryAi.getTemptations())));

    public static DeferredHolder<SensorType<?>, SensorType<NearestSpecificBlockSensor<CassowaryEntity>>> CASSOWARY_NEAREST_EGG =
            Primal_Registries.SENSOR_TYPES.register("cassowary_near_egg",
                    () -> new SensorType<>(()-> new NearestSpecificBlockSensor<>(Primal_Blocks.CASSOWARY_EGG.get(), 24, 3)));

    //Walrus
    public static DeferredHolder<SensorType<?>, SensorType<GenericAttackEntitySensor>> WALRUS_ENTITY_SENSOR =
            Primal_Registries.SENSOR_TYPES.register("walrus_entity_sensor",
                    () -> new SensorType<>(()->
                            new GenericAttackEntitySensor(){
                                protected int radiusXZ() {
                                    return 48;
                                }
                            }));

    public static final DeferredHolder<SensorType<?>, SensorType<TemptingSensor>> WALRUS_TEMPTATIONS_SENSOR =
            Primal_Registries.SENSOR_TYPES.register("walrus_temptations",
                    () -> new SensorType<>(() -> new TemptingSensor(WalrusAi.getTemptations())));

    public static DeferredHolder<SensorType<?>, SensorType<MobHostilesSensor>> WALRUS_HOSTILE_SENSOR =
            Primal_Registries.SENSOR_TYPES.register("walrus_hostile_sensor",
                    () -> new SensorType<>(()-> new MobHostilesSensor(WalrusAi.ACCEPTABLE_DISTANCE_FROM_HOSTILES)));

    //Lion
    public static DeferredHolder<SensorType<?>, SensorType<LionEntitySensor>> LION_ENTITY_SENSOR =
            Primal_Registries.SENSOR_TYPES.register("lion_entity_sensor",
                    () -> new SensorType<>(LionEntitySensor::new));

    public static final DeferredHolder<SensorType<?>, SensorType<TemptingSensor>> LION_TEMPTATIONS_SENSOR =
            Primal_Registries.SENSOR_TYPES.register("lion_temptations",
                    () -> new SensorType<>(() -> new TemptingSensor(LionAi.getTemptations())));

    public static DeferredHolder<SensorType<?>, SensorType<NearestSpecificBlockSensor<LionEntity>>> LION_NEAREST_TO_LAY =
            Primal_Registries.SENSOR_TYPES.register("lion_near_lay",
                    () -> new SensorType<>(()-> new NearestSpecificBlockSensor<>(l -> l.isTame() && l.isWandering(), (entity, pos) -> {
                        BlockState blockstate = entity.level().getBlockState(pos);
                        if (blockstate.is(Blocks.CHEST)) {
                            return ChestBlockEntity.getOpenCount(entity.level(), pos) < 1;
                        } else {
                            return blockstate.is(Blocks.FURNACE) && blockstate.getValue(FurnaceBlock.LIT) ||
                                    blockstate.is(
                                            BlockTags.BEDS, base -> base.getOptionalValue(BedBlock.PART)
                                                    .map(bedPart -> bedPart != BedPart.HEAD).orElse(true)
                                    );
                        }
                    }, 24, 3)));

    //Snake
    public static DeferredHolder<SensorType<?>, SensorType<SnakeEntitySensor>> SNAKE_ENTITY_SENSOR =
            Primal_Registries.SENSOR_TYPES.register("snake_entity_sensor",
                    () -> new SensorType<>(SnakeEntitySensor::new));

    public static final DeferredHolder<SensorType<?>, SensorType<TemptingSensor>> SNAKE_TEMPTATIONS_SENSOR =
            Primal_Registries.SENSOR_TYPES.register("snake_temptations",
                    () -> new SensorType<>(() -> new TemptingSensor(SnakeAi.getTemptations())));

    public static DeferredHolder<SensorType<?>, SensorType<SnakeSpecificBlockSensor>> SNAKE_BLOCK_SENSOR =
            Primal_Registries.SENSOR_TYPES.register("snake_block_detection",
                    () -> new SensorType<>(()-> new SnakeSpecificBlockSensor(24, 3)));

    //Deer
    public static final DeferredHolder<SensorType<?>, SensorType<TemptingSensor>> DEER_TEMPTATIONS_SENSOR =
            Primal_Registries.SENSOR_TYPES.register("deer_temptations",
                    () -> new SensorType<>(() -> new TemptingSensor(DeerAi.getTemptations())));

    public static DeferredHolder<SensorType<?>, SensorType<DeerEntitySensor>> DEER_ENTITY_SENSOR =
            Primal_Registries.SENSOR_TYPES.register("deer_entity_sensor",
                    () -> new SensorType<>(DeerEntitySensor::new));

    //Generic
    public static DeferredHolder<SensorType<?>, SensorType<BabySensor>> NEAREST_BABY =
            Primal_Registries.SENSOR_TYPES.register("nearest_baby",
                    () -> new SensorType<>(BabySensor::new));

    public static DeferredHolder<SensorType<?>, SensorType<NearestSpecificBlockSensor<LivingEntity>>> NEAREST_NEST =
            Primal_Registries.SENSOR_TYPES.register("nearest_nest",
                    () -> new SensorType<>(()-> new NearestSpecificBlockSensor<>(Primal_Blocks.NEST_BLOCK.get(), 24, 5)));

    public static DeferredHolder<SensorType<?>, SensorType<NearestSpecificBlockSensor<LivingEntity>>> NEAREST_HOLLOW_LOG =
            Primal_Registries.SENSOR_TYPES.register("nearest_hollow_log",
                    () -> new SensorType<>(()-> new NearestSpecificBlockSensor<>(Primal_Tags.Block.HOLLOW_LOGS, 24, 8)));

    public static DeferredHolder<SensorType<?>, SensorType<GenericAttackEntitySensor>> GENERIC_ATTACK_SENSOR =
            Primal_Registries.SENSOR_TYPES.register("generic_attack_entity_sensor",
                    () -> new SensorType<>(GenericAttackEntitySensor::new));

    public static void init() {}
}
