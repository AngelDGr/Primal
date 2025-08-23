package org.primal.entity.ai.sensors.bear;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.AdultSensor;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class BearNearestAdultSensor extends AdultSensor {

    protected void doTick(@NotNull ServerLevel level, AgeableMob entity) {
        entity.getBrain()
            .getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)
            .ifPresent(nearestVisibleLivingEntities -> this.setNearestVisibleAdult(entity, nearestVisibleLivingEntities));
    }

    protected void setNearestVisibleAdult(AgeableMob mob, NearestVisibleLivingEntities nearbyEntities) {
        Optional<AgeableMob> optional = nearbyEntities.findClosest(livingEntity ->
                        (livingEntity.getType() == mob.getType() || livingEntity.getType() == EntityType.POLAR_BEAR) && !livingEntity.isBaby())
                .map(AgeableMob.class::cast);
        mob.getBrain().setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT, optional);
    }
}
