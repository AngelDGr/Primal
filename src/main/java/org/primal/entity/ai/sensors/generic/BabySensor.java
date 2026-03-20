package org.primal.entity.ai.sensors.generic;

import com.google.common.collect.ImmutableSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import org.jetbrains.annotations.NotNull;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.util.Primal_Util;

import java.util.Set;

public class BabySensor extends Sensor<AgeableMob> {
    @Override
    public @NotNull Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(Primal_MemoryModuleTypes.NEAREST_VISIBLE_BABY.get(), MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
    }

    protected void doTick(@NotNull ServerLevel level, @NotNull AgeableMob entity) {
        Primal_Util.Ai.setMemoryFromVisibleEntity(entity, livingEntity ->
                (livingEntity.getType() == entity.getType()) && livingEntity.isBaby(),
                Primal_MemoryModuleTypes.NEAREST_VISIBLE_BABY.get());
    }
}
