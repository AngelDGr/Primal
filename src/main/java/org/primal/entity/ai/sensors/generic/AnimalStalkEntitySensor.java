package org.primal.entity.ai.sensors.generic;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.NearestLivingEntitySensor;
import net.minecraft.world.entity.ai.sensing.Sensor;
import org.jetbrains.annotations.NotNull;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.util.mob_types.DetectsFartherAway;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class AnimalStalkEntitySensor<T extends LivingEntity> extends NearestLivingEntitySensor<T> {

    private final double closestDistanceToStalk;
    private final int cooldownTicks;
    private final Predicate<T> canStalk;

    protected AnimalStalkEntitySensor(double closestDistanceToStalk, Predicate<T> canStalk, int cooldownTicks){
        this.closestDistanceToStalk = closestDistanceToStalk;
        this.canStalk=canStalk;
        this.cooldownTicks=cooldownTicks;
    }

    @Override
    public @NotNull Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.copyOf(Iterables.concat(super.requires(), List.of(Primal_MemoryModuleTypes.STALK_TARGET.get(), Primal_MemoryModuleTypes.STALK_COOLDOWN.get())));
    }

    @Override
    protected void doTick(@NotNull ServerLevel level, @NotNull T animal) {
        super.doTick(level, animal);

        var hasCooldown= animal.getBrain().getMemory(Primal_MemoryModuleTypes.STALK_COOLDOWN.get());
        var hasStalkTarget= animal.getBrain().getMemory(Primal_MemoryModuleTypes.STALK_TARGET.get());

        if(canStalk.test(animal)){
            animal.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).stream().flatMap(Collection::stream)
                    .filter(
                            target -> {
                                if(animal instanceof DetectsFartherAway detectsFartherAway) {
                                    return detectsFartherAway.isEntityAttackable(animal, target)
                                            && animal.canAttack(target)
                                            && animal.distanceTo(target) > closestDistanceToStalk
                                            //If it doesn't have cooldown or is the same
                                            && (hasCooldown.isEmpty() || (hasStalkTarget.isPresent() && hasStalkTarget.get()==target));
                                } else {
                                    return Sensor.isEntityAttackable(animal, target)
                                            && animal.canAttack(target)
                                            && animal.distanceTo(target) > closestDistanceToStalk
                                            //If it doesn't have cooldown or is the same
                                            && (hasCooldown.isEmpty() || (hasStalkTarget.isPresent() && hasStalkTarget.get()==target));
                                }
                            }
                    )
                    .findFirst()
                    .ifPresentOrElse(ent -> {
                                //Puts the cooldown
                                animal.getBrain().setMemoryWithExpiry(Primal_MemoryModuleTypes.STALK_COOLDOWN.get(), true, cooldownTicks);
                                //Puts the cooldown
                                animal.getBrain().setMemory(Primal_MemoryModuleTypes.STALK_TARGET.get(), ent);
                            },
                            () -> animal.getBrain().eraseMemory(Primal_MemoryModuleTypes.STALK_TARGET.get()));
        }

    }

    @Override
    protected int radiusXZ() {
        return 48;
    }
}
