package org.primal.entity.ai.sensors.generic;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.sensing.NearestLivingEntitySensor;
import org.jetbrains.annotations.NotNull;

public class OwnerSensor<T extends TamableAnimal> extends NearestLivingEntitySensor<T> {
//    @Override
//    public @NotNull Set<MemoryModuleType<?>> requires() {
//        return ImmutableSet.copyOf(Iterables.concat(super.requires(), List.of(Primal_MemoryModuleTypes.OWNER.get())));
//    }

    @Override
    protected void doTick(@NotNull ServerLevel level, @NotNull T pet) {
        super.doTick(level, pet);
//
//        if (pet.getOwner() != null) {
//            pet.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).stream().flatMap(Collection::stream)
//                    .filter(target -> pet.getOwner() == target)
//                    .findFirst()
//                    .ifPresentOrElse(ent -> pet.getBrain().setMemory(Primal_MemoryModuleTypes.OWNER.get(), ent),
//                            () -> pet.getBrain().eraseMemory(Primal_MemoryModuleTypes.OWNER.get()));
//        }
//
//        if (pet.getOwner() != null) {
//
//            pet.getBrain().
//
//            pet.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).stream().flatMap(Collection::stream)
//                    .filter(target -> pet.getOwner() == target)
//                    .findFirst()
//                    .ifPresentOrElse(
//                            ent -> pet.getBrain().setMemory(Primal_MemoryModuleTypes.OWNER.get(), ent),
//                            () -> pet.getBrain().eraseMemory(Primal_MemoryModuleTypes.OWNER.get()));
//        }
    }

    @Override
    protected int radiusXZ() {
        return super.radiusXZ();
    }

    @Override
    protected int radiusY() {
        return super.radiusY();
    }
}
