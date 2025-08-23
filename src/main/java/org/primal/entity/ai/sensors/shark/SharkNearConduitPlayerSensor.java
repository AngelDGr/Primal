package org.primal.entity.ai.sensors.shark;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.NearestLivingEntitySensor;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.SharkEntity;
import org.primal.registry.Primal_MemoryModuleTypes;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public final class SharkNearConduitPlayerSensor extends NearestLivingEntitySensor<SharkEntity> {

    @Override
    public @NotNull Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.copyOf(Iterables.concat(super.requires(), List.of(Primal_MemoryModuleTypes.NEAREST_CONDUIT_PLAYER.get())));
    }

    @Override
    protected void doTick(@NotNull ServerLevel level, @NotNull SharkEntity shark) {
        super.doTick(level, shark);

        shark.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).stream().flatMap(Collection::stream)
                .filter(
                        target -> target.hasEffect(MobEffects.CONDUIT_POWER)
                )
                .findFirst()
                .ifPresentOrElse(ent -> shark.getBrain().setMemory(Primal_MemoryModuleTypes.NEAREST_CONDUIT_PLAYER.get(), ent),
                        () -> shark.getBrain().eraseMemory(Primal_MemoryModuleTypes.NEAREST_CONDUIT_PLAYER.get()));
    }
}
