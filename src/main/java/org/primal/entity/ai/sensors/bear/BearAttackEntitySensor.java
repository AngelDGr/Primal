package org.primal.entity.ai.sensors.bear;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.primal.datagen.ModEntityTypeTags;
import org.primal.entity.animal.Bear;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.NearestLivingEntitySensor;
import net.minecraft.world.entity.ai.sensing.Sensor;

public final class BearAttackEntitySensor extends NearestLivingEntitySensor<Bear> {

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.copyOf(Iterables.concat(super.requires(), List.of(MemoryModuleType.NEAREST_ATTACKABLE)));
    }

    @Override
    protected void doTick(ServerLevel level, Bear entity) {
        super.doTick(level, entity);
        entity.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).stream().flatMap(Collection::stream)
                .filter(ent -> Sensor.isEntityAttackable(entity, ent) && ent.getType().is(ModEntityTypeTags.BEAR_HUNTABLE)).findFirst()
                .ifPresentOrElse(ent -> entity.getBrain().setMemory(MemoryModuleType.NEAREST_ATTACKABLE, ent),
                        () -> entity.getBrain().eraseMemory(MemoryModuleType.NEAREST_ATTACKABLE));
    }
}
