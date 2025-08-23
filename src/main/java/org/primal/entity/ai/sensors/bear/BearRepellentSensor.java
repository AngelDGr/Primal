package org.primal.entity.ai.sensors.bear;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import org.jetbrains.annotations.NotNull;
import org.primal.registry.Primal_Tags;

import java.util.Optional;
import java.util.Set;

public class BearRepellentSensor extends Sensor<LivingEntity> {
    @Override
    public @NotNull Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_REPELLENT);
    }

    protected void doTick(@NotNull ServerLevel level, LivingEntity entity) {

        entity.getBrain()
                .setMemory(MemoryModuleType.NEAREST_REPELLENT, findNearestRepellent(level, entity));
    }

    public static Optional<BlockPos> findNearestRepellent(ServerLevel level, LivingEntity bear) {
        return BlockPos.findClosestMatch(bear.blockPosition(), 8, 4, pos -> level.getBlockState(pos).is(Primal_Tags.BEAR_REPELLENTS));
    }
}
