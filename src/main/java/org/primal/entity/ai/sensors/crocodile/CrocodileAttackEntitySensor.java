package org.primal.entity.ai.sensors.crocodile;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.NearestLivingEntitySensor;
import net.minecraft.world.entity.ai.sensing.Sensor;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.CrocodileEntity;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public final class CrocodileAttackEntitySensor extends NearestLivingEntitySensor<CrocodileEntity> {

    @Override
    public @NotNull Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.copyOf(Iterables.concat(super.requires(), List.of(MemoryModuleType.NEAREST_ATTACKABLE)));
    }

    @Override
    protected void doTick(@NotNull ServerLevel level, @NotNull CrocodileEntity crocodile) {
        super.doTick(level, crocodile);

        crocodile.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).stream().flatMap(Collection::stream)
                .filter(
                        target ->
                                Sensor.isEntityAttackable(crocodile, target) && crocodile.canAttack(target)
                )
                .findFirst()
                .ifPresentOrElse(ent -> crocodile.getBrain().setMemory(MemoryModuleType.NEAREST_ATTACKABLE, ent),
                        () -> crocodile.getBrain().eraseMemory(MemoryModuleType.NEAREST_ATTACKABLE));
    }
}
