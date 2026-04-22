package org.primal.entity.ai.behavior.generic.pet;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.registry.Primal_Activities;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.util.Primal_Util;
import org.primal.util.mob_types.MobWithTransitionablePoseAnimations;

public class AnimalSitting extends Behavior<TamableAnimal> {

    @Nullable
    private final String animationName;

    public AnimalSitting() {
        this(null);
    }

    public AnimalSitting(@Nullable String animationName) {
        super(ImmutableMap.of(), Integer.MAX_VALUE);
        this.animationName = animationName;
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
        if(this.animationName != null && pet instanceof MobWithTransitionablePoseAnimations animatable)
            animatable.startAnimation(this.animationName);
        else
            pet.setPose(Pose.SITTING);
        pet.getBrain().setMemoryWithExpiry(Primal_MemoryModuleTypes.WAS_IDLE_ANIMATION.get(), true, 100);
    }

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull TamableAnimal pet, long gameTime) {
        this.stopMoving(pet);
    }

    @Override
    protected void stop(@NotNull ServerLevel level, @NotNull TamableAnimal pet, long gameTime) {
        if(this.animationName != null && pet instanceof MobWithTransitionablePoseAnimations animatable)
            animatable.stopAnimation(this.animationName);
        else
            pet.setPose(Pose.STANDING);
        //To avoid an idle immediately after standing up
        pet.getBrain().setMemoryWithExpiry(Primal_MemoryModuleTypes.WAS_IDLE_ANIMATION.get(), true, 100);
    }

    public void stopMoving(TamableAnimal pet){
        pet.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        pet.getNavigation().stop();
        pet.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
    }
}
