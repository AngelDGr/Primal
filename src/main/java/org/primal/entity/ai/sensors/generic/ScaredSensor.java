package org.primal.entity.ai.sensors.generic;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.NearestLivingEntitySensor;
import org.jetbrains.annotations.NotNull;
import org.primal.registry.Primal_MemoryModuleTypes;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public final class ScaredSensor<T extends LivingEntity> extends NearestLivingEntitySensor<T> {

    private final Predicate<LivingEntity> canScare;
    private final Predicate<T> isScared;

    public ScaredSensor(Predicate<LivingEntity> canScare, Predicate<T> isScared) {
        this.canScare = canScare;
        this.isScared=isScared;
    }

    public ScaredSensor(Predicate<LivingEntity> canScare) {
        this(canScare, entity -> true);
    }

    @Override
    public @NotNull Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.copyOf(Iterables.concat(super.requires(), List.of(Primal_MemoryModuleTypes.NEAREST_SCARED.get())));
    }

    @Override
    protected void doTick(@NotNull ServerLevel level, @NotNull T animal) {
        super.doTick(level, animal);

        //To not be scared of it own kind
        Class<?> entityClass = animal.getClass();

        animal.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).stream().flatMap(Collection::stream)
                .filter(
                        target -> !target.isShiftKeyDown() && !target.isSpectator() && canScare.test(target) && !entityClass.isInstance(target) && isScared.test(animal)
                )
                .findFirst()
                .ifPresentOrElse(ent -> animal.getBrain().setMemory(Primal_MemoryModuleTypes.NEAREST_SCARED.get(), ent),
                        () -> animal.getBrain().eraseMemory(Primal_MemoryModuleTypes.NEAREST_SCARED.get()));
    }
}
