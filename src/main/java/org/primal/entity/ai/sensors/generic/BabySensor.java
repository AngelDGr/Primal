package org.primal.entity.ai.sensors.generic;

import com.google.common.collect.ImmutableSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.Sensor;
import org.jetbrains.annotations.NotNull;
import org.primal.registry.Primal_MemoryModuleTypes;

import java.util.Optional;
import java.util.Set;

public class BabySensor extends Sensor<AgeableMob> {
    @Override
    public @NotNull Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(Primal_MemoryModuleTypes.NEAREST_VISIBLE_BABY.get(), MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
    }

    protected void doTick(@NotNull ServerLevel level, AgeableMob entity) {
        entity.getBrain()
            .getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)
            .ifPresent(nearestVisibleLivingEntities -> this.setNearestVisibleBaby(entity, nearestVisibleLivingEntities));
    }

    private void setNearestVisibleBaby(AgeableMob mob, NearestVisibleLivingEntities nearbyEntities) {
        Optional<AgeableMob> optional = nearbyEntities.findClosest(livingEntity ->
                        (livingEntity.getType() == mob.getType()) && livingEntity.isBaby())
                .map(AgeableMob.class::cast);
        mob.getBrain().setMemory(Primal_MemoryModuleTypes.NEAREST_VISIBLE_BABY.get(), optional);
    }
}
