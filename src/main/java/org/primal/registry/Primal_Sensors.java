package org.primal.registry;

import org.primal.Primal_Registries;
import org.primal.entity.ai.sensors.bear.BearAttackEntitySensor;
import org.primal.entity.ai.sensors.bear.BearNearestBeehiveSensor;

import net.minecraft.world.entity.ai.sensing.SensorType;
import net.neoforged.neoforge.registries.DeferredHolder;

public final class Primal_Sensors {

    public static DeferredHolder<SensorType<?>, SensorType<BearAttackEntitySensor>> BEAR_ATTACK_SENSOR =
            Primal_Registries.SENSOR_TYPES.register("bear_attack_entity_sensor",
                    () -> new SensorType<>(BearAttackEntitySensor::new));

    public static DeferredHolder<SensorType<?>, SensorType<BearNearestBeehiveSensor>> BEAR_NEAREST_BEEHIVE_SENSOR =
            Primal_Registries.SENSOR_TYPES.register("bear_nearest_beehive_sensor",
                    () -> new SensorType<>(BearNearestBeehiveSensor::new));

    public static void init() {}
}
