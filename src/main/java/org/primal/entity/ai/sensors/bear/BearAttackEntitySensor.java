package org.primal.entity.ai.sensors.bear;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.primal.registry.Primal_Tags;
import org.primal.entity.animal.BearEntity;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.NearestLivingEntitySensor;
import net.minecraft.world.entity.ai.sensing.Sensor;

public final class BearAttackEntitySensor extends NearestLivingEntitySensor<BearEntity> {

    @Override
    public @NotNull Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.copyOf(Iterables.concat(super.requires(), List.of(MemoryModuleType.NEAREST_ATTACKABLE)));
    }

    @Override
    protected void doTick(@NotNull ServerLevel level, @NotNull BearEntity entity) {
        super.doTick(level, entity);
        entity.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).stream().flatMap(Collection::stream)
                .filter(ent -> Sensor.isEntityAttackable(entity, ent) && ent.getType().is(Primal_Tags.BEAR_HUNTABLE)).findFirst()
                .ifPresentOrElse(ent -> entity.getBrain().setMemory(MemoryModuleType.NEAREST_ATTACKABLE, ent),
                        () -> entity.getBrain().eraseMemory(MemoryModuleType.NEAREST_ATTACKABLE));
    }
}
