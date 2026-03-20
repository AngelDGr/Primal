package org.primal.entity.ai.sensors.generic;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.NearestLivingEntitySensor;
import org.jetbrains.annotations.NotNull;
import org.primal.util.Primal_Util;

import java.util.List;
import java.util.Set;

public class GenericAttackEntitySensor extends NearestLivingEntitySensor<LivingEntity> {

    @Override
    public @NotNull Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.copyOf(Iterables.concat(super.requires(), List.of(MemoryModuleType.NEAREST_ATTACKABLE)));
    }

    @Override
    protected void doTick(@NotNull ServerLevel level, @NotNull LivingEntity animal) {
        super.doTick(level, animal);
        Primal_Util.Ai.setNearestAttackableOnSensor(animal);
    }
}
