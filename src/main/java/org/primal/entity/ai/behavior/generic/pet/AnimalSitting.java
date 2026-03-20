package org.primal.entity.ai.behavior.generic.pet;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import org.jetbrains.annotations.NotNull;
import org.primal.registry.Primal_Activities;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.util.Primal_Util;

public class AnimalSitting extends Behavior<TamableAnimal> {

    private final Pair<String, String> startAnimation;
    private final Pair<String, String> stopAnimation;

    public AnimalSitting() {
        this(null, null);
    }

    public AnimalSitting(String animationName) {
        this(Pair.of("base_controller", animationName+"_start"), Pair.of("base_controller", animationName+"_end"));
    }

    public AnimalSitting(Pair<String, String> startAnimation, Pair<String, String> stopAnimation) {
        super(ImmutableMap.of(), Integer.MAX_VALUE);
        this.startAnimation =startAnimation;
        this.stopAnimation=stopAnimation;
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
        Primal_Util.Visuals.emitAnimation(startAnimation, pet);
        pet.setPose(Pose.SITTING);
        pet.getBrain().setMemoryWithExpiry(Primal_MemoryModuleTypes.WAS_IDLE_ANIMATION.get(), true, 100);
    }

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull TamableAnimal pet, long gameTime) {
        this.stopMoving(pet);
        pet.setPose(Pose.SITTING);
    }

    @Override
    protected void stop(@NotNull ServerLevel level, @NotNull TamableAnimal pet, long gameTime) {
        Primal_Util.Visuals.emitAnimation(stopAnimation, pet);
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
