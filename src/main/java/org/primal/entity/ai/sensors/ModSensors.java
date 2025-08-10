package org.primal.entity.ai.sensors;

import org.primal.Primal_Main;
import org.primal.entity.ai.sensors.bear.BearAttackEntitySensor;
import org.primal.entity.ai.sensors.bear.BearNearestBeehiveSensor;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModSensors {
    public static DeferredRegister<SensorType<?>> SENSOR_TYPES = DeferredRegister.create(Registries.SENSOR_TYPE, Primal_Main.MODID);

    public static DeferredHolder<SensorType<?>, SensorType<BearAttackEntitySensor>> BEAR_ATTACK_SENSOR = SENSOR_TYPES.register("bear_attack_entity_sensor", () -> new SensorType<BearAttackEntitySensor>(BearAttackEntitySensor::new));
    public static DeferredHolder<SensorType<?>, SensorType<BearNearestBeehiveSensor>> BEAR_NEAREST_BEEHIVE_SENSOR = SENSOR_TYPES.register("bear_nearest_beehive_sensor", () -> new SensorType<BearNearestBeehiveSensor>(BearNearestBeehiveSensor::new));

    public static void bootstrap(IEventBus bus) {
        SENSOR_TYPES.register(bus);
    }
}
