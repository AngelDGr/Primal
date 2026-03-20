package org.primal.entity.ai.behavior.generic;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.animal.Animal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.registry.Primal_MemoryModuleTypes;
import com.mojang.datafixers.util.Pair;
import org.primal.util.Primal_Util;

import java.util.function.Predicate;

public class IdlePoseAnimationBehavior<T extends Mob> extends Behavior<T> {
    private long endTimestamp=10;
    @Nullable
    private final Pair<String, String> animationStart;
    @Nullable
    private final Pair<String, String> animationEnd;
    private final Pose pose;
    private final int minDuration;
    private final int maxDuration;
    private final Predicate<T> canStart;
    private final Predicate<T> canStillUse;
    private final int cooldown;


    public IdlePoseAnimationBehavior(@Nullable Pair<String, String> animationStart,
                                     @Nullable Pair<String, String> animationEnd,
                                     Pose pose,
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
        this.animationStart=animationStart;
        this.animationEnd = animationEnd;
        this.pose=pose;
        this.minDuration=minDuration;
        this.maxDuration=maxDuration;
        this.canStart=canStart;
        this.canStillUse=canStillUse;
        this.cooldown=cooldown;
    }

    public static<M extends Mob> IdlePoseAnimationBehavior<M> create(Pose pose, int minDuration, int maxDuration, Predicate<M> canStart, int cooldown){
        return new IdlePoseAnimationBehavior<>(null, null, pose, minDuration, maxDuration, canStart, canStart, cooldown);
    }

    public static<M extends Mob> IdlePoseAnimationBehavior<M> create(String animation, Pose pose, int minDuration, int maxDuration, Predicate<M> canStart, int cooldown){
        return new IdlePoseAnimationBehavior<>(Pair.of("base_controller", animation+"_start"), Pair.of("base_controller", animation+"_end"), pose, minDuration, maxDuration, canStart, canStart, cooldown);
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
        return canStillUse.test(entity) && hasRequiredMemories(entity);
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

        Primal_Util.Visuals.emitAnimation(animationStart, mob);
        mob.setPose(pose);

        this.endTimestamp = gameTime + (long)finalDuration;
    }

    @Override
    protected void stop(@NotNull ServerLevel level, @NotNull T mob, long gameTime) {
        Primal_Util.Visuals.emitAnimation(animationEnd, mob);
        mob.setPose(Pose.STANDING);
        mob.getBrain().setMemoryWithExpiry(Primal_MemoryModuleTypes.WAS_IDLE_ANIMATION.get(), true, cooldown);
    }

    @Override
    protected boolean timedOut(long gameTime) {
        return gameTime > this.endTimestamp;
    }
}
