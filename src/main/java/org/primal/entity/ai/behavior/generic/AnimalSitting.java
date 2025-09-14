package org.primal.entity.ai.behavior.generic;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import org.jetbrains.annotations.NotNull;
import org.primal.registry.Primal_Activities;

public class AnimalSitting extends Behavior<TamableAnimal> {
    public AnimalSitting() {
        super(ImmutableMap.of(), Integer.MAX_VALUE);
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull TamableAnimal pet) {
        return this.canStillUse(level, pet, 0);
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, @NotNull TamableAnimal pet, long gameTime) {
        return pet.getBrain().isActive(Primal_Activities.SIT.get());
    }

    @Override
    protected void start(@NotNull ServerLevel level, @NotNull TamableAnimal pet, long gameTime) {
        this.stopMoving(pet);

        pet.setPose(Pose.SITTING);
    }

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull TamableAnimal pet, long gameTime) {
        this.stopMoving(pet);
    }

    @Override
    protected void stop(@NotNull ServerLevel level, TamableAnimal pet, long gameTime) {
        pet.setPose(Pose.STANDING);
    }

    public void stopMoving(TamableAnimal pet){
        pet.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        pet.getNavigation().stop();
        pet.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
    }
}
