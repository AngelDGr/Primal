package org.primal.registry;

import net.minecraft.world.entity.ai.sensing.TemptingSensor;
import org.primal.Primal_Registries;
import org.primal.entity.ai.BearAi;
import org.primal.entity.ai.CrocodileAi;
import org.primal.entity.ai.sensors.bear.*;

import net.minecraft.world.entity.ai.sensing.SensorType;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.primal.entity.ai.sensors.crocodile.CrocodileAttackEntitySensor;
import org.primal.entity.ai.sensors.generic.BabySensor;
import org.primal.entity.ai.sensors.generic.NearestSpecificBlockSensor;
import org.primal.entity.ai.sensors.shark.SharkAttackEntitySensor;
import org.primal.entity.ai.sensors.shark.SharkNearConduitPlayerSensor;

public final class Primal_Sensors {

    //Bear
    public static DeferredHolder<SensorType<?>, SensorType<BearAttackEntitySensor>> BEAR_ATTACK_SENSOR =
            Primal_Registries.SENSOR_TYPES.register("bear_attack_entity_sensor",
                    () -> new SensorType<>(BearAttackEntitySensor::new));

    public static DeferredHolder<SensorType<?>, SensorType<BearNearestBeehiveSensor>> BEAR_NEAREST_BEEHIVE_SENSOR =
            Primal_Registries.SENSOR_TYPES.register("bear_nearest_beehive_sensor",
                    () -> new SensorType<>(BearNearestBeehiveSensor::new));

    public static DeferredHolder<SensorType<?>, SensorType<BearNearestSweetBerryBushSensor>> BEAR_NEAREST_SWEET_BERRY_BUSH_SENSOR =
            Primal_Registries.SENSOR_TYPES.register("bear_nearest_sweet_berry_bush_sensor",
                    () -> new SensorType<>(BearNearestSweetBerryBushSensor::new));

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
    public static DeferredHolder<SensorType<?>, SensorType<SharkAttackEntitySensor>> SHARK_ATTACK_SENSOR =
            Primal_Registries.SENSOR_TYPES.register("shark_attack_entity_sensor",
                    () -> new SensorType<>(SharkAttackEntitySensor::new));

    public static DeferredHolder<SensorType<?>, SensorType<SharkNearConduitPlayerSensor>> SHARK_NEAR_CONDUIT_PLAYER =
            Primal_Registries.SENSOR_TYPES.register("shark_near_conduit_player",
                    () -> new SensorType<>(SharkNearConduitPlayerSensor::new));

    public static DeferredHolder<SensorType<?>, SensorType<NearestSpecificBlockSensor>> SHARK_NEAREST_CONDUIT =
            Primal_Registries.SENSOR_TYPES.register("shark_near_conduit",
                    () -> new SensorType<>(()-> new NearestSpecificBlockSensor(Primal_Tags.SHARK_ATTRACTORS, 24, 24)));


    //Crocodile
    public static DeferredHolder<SensorType<?>, SensorType<CrocodileAttackEntitySensor>> CROCODILE_ATTACK_SENSOR =
            Primal_Registries.SENSOR_TYPES.register("crocodile_attack_entity_sensor",
                    () -> new SensorType<>(CrocodileAttackEntitySensor::new));

    public static final DeferredHolder<SensorType<?>, SensorType<TemptingSensor>> CROCODILE_TEMPTATIONS_SENSOR =
            Primal_Registries.SENSOR_TYPES.register("crocodile_temptations",
                    () -> new SensorType<>(() -> new TemptingSensor(CrocodileAi.getTemptations())));

    public static DeferredHolder<SensorType<?>, SensorType<NearestSpecificBlockSensor>> CROCODILE_NEAREST_EGG =
            Primal_Registries.SENSOR_TYPES.register("crocodile_near_egg",
                    () -> new SensorType<>(()-> new NearestSpecificBlockSensor(Primal_Blocks.CROCODILE_EGG.get(), 24, 3)));

    public static DeferredHolder<SensorType<?>, SensorType<NearestSpecificBlockSensor>> CROCODILE_NEAREST_REED =
            Primal_Registries.SENSOR_TYPES.register("crocodile_near_reed",
                    () -> new SensorType<>(()-> new NearestSpecificBlockSensor(Primal_Tags.CROCODILE_ATTRACTORS, 24, 3)));


    //Generic
    public static DeferredHolder<SensorType<?>, SensorType<BabySensor>> NEAREST_BABY =
            Primal_Registries.SENSOR_TYPES.register("nearest_baby",
                    () -> new SensorType<>(BabySensor::new));
    public static void init() {}
}
