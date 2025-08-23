package org.primal.entity.ai.sensors.shark;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.NearestLivingEntitySensor;
import net.minecraft.world.entity.ai.sensing.Sensor;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.SharkEntity;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public final class SharkAttackEntitySensor extends NearestLivingEntitySensor<SharkEntity> {

    @Override
    public @NotNull Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.copyOf(Iterables.concat(super.requires(), List.of(MemoryModuleType.NEAREST_ATTACKABLE)));
    }

    @Override
    protected void doTick(@NotNull ServerLevel level, @NotNull SharkEntity shark) {
        super.doTick(level, shark);

        shark.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).stream().flatMap(Collection::stream)
                .filter(
                        target ->
                                Sensor.isEntityAttackable(shark, target) && shark.canAttack(target)
                )
                .findFirst()
                .ifPresentOrElse(ent -> shark.getBrain().setMemory(MemoryModuleType.NEAREST_ATTACKABLE, ent),
                        () -> shark.getBrain().eraseMemory(MemoryModuleType.NEAREST_ATTACKABLE));
    }
}
