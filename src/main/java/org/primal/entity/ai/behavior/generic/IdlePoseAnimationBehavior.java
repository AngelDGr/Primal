package org.primal.entity.ai.behavior.generic;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.animal.Animal;
import org.jetbrains.annotations.NotNull;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.util.mob_types.MobWithTransitionablePoseAnimations;

import java.util.function.Predicate;

public class IdlePoseAnimationBehavior<T extends Mob> extends Behavior<T> {
    private long endTimestamp=10;
    private final String animationName;
    private final int minDuration;
    private final int maxDuration;
    private final Predicate<T> canStart;
    private final Predicate<T> canStillUse;
    private final int cooldown;

    public IdlePoseAnimationBehavior(String animationName,
                                     int minDuration, int maxDuration,
                                     Predicate<T> canStart,
                                     Predicate<T> canStillUse,
                                     int cooldown) {
        super(ImmutableMap.of(
                MemoryModuleType.AVOID_TARGET,
                MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.ATTACK_TARGET,
                MemoryStatus.VALUE_ABSENT,
                Primal_MemoryModuleTypes.WAS_IDLE_ANIMATION.get(),
                MemoryStatus.VALUE_ABSENT), Integer.MAX_VALUE);
        this.animationName = animationName;
        this.minDuration=minDuration;
        this.maxDuration=maxDuration;
        this.canStart=canStart;
        this.canStillUse=canStillUse;
        this.cooldown=cooldown;
    }

    public static<M extends Mob> IdlePoseAnimationBehavior<M> create(String animation, int minDuration, int maxDuration, Predicate<M> canStart, int cooldown){
        return new IdlePoseAnimationBehavior<>(animation, minDuration, maxDuration, canStart, canStart, cooldown);
    }

    public static<M extends Mob> boolean basicCanStart(M mob){
        return basicCanStart().test(mob);
    }

    public static<M extends Mob> Predicate<M> basicCanStart(){
        return mob -> !mob.isVehicle() && (!(mob instanceof Animal animal) || !animal.isInLove()) && !mob.isLeashed();
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull T mob) {
        return canStart.test(mob);
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, @NotNull T entity, long gameTime) {
        return gameTime <= this.endTimestamp && canStillUse.test(entity) && hasRequiredMemories(entity);
    }

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull T mob, long gameTime) {
        mob.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        mob.getNavigation().stop();
        mob.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
    }

    @Override
    protected void start(@NotNull ServerLevel level, @NotNull T mob, long gameTime) {
        int finalDuration = level.getRandom().nextIntBetweenInclusive(minDuration, maxDuration);

        mob.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        mob.getNavigation().stop();
        mob.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);

        if(mob instanceof MobWithTransitionablePoseAnimations transitionablePoseAnimations)
            transitionablePoseAnimations.startAnimation(this.animationName);

        this.endTimestamp = gameTime + (long)finalDuration;
    }

    @Override
    protected void stop(@NotNull ServerLevel level, @NotNull T mob, long gameTime) {
        if(mob instanceof MobWithTransitionablePoseAnimations transitionablePoseAnimations){
            transitionablePoseAnimations.stopAnimation(this.animationName);
        }
        mob.getBrain().setMemoryWithExpiry(Primal_MemoryModuleTypes.WAS_IDLE_ANIMATION.get(), true, cooldown);
    }

    @Override
    protected boolean timedOut(long gameTime) {
        return gameTime > this.endTimestamp;
    }
}
