package org.primal.entity.ai.sensors.eagle;

import com.google.common.collect.ImmutableSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.EagleEntity;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public final class EagleNearestHostile extends Sensor<EagleEntity> {

    @Override
    public @NotNull Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_HOSTILE);
    }

    @Override
    protected void doTick(@NotNull ServerLevel level, @NotNull EagleEntity eagle) {

        eagle.getBrain().setMemory(MemoryModuleType.NEAREST_HOSTILE, this.getNearestHostile(eagle));
    }

    private Optional<LivingEntity> getNearestHostile(LivingEntity eagle) {
        Level level = eagle.level();

        List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, eagle.getBoundingBox().inflate(20.0), mob -> mob instanceof Monster);

        if (list.isEmpty()) {
            return Optional.empty();
        } else {
            return list.stream()
                    .min(Comparator.comparingDouble(mob -> mob.distanceToSqr(eagle)));
        }
    }

}
