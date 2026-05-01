package org.primal.entity.ai.behavior.generic;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.jetbrains.annotations.NotNull;
import org.primal.registry.Primal_MemoryModuleTypes;

import java.util.function.Predicate;

public class StopAndTriggerAnimation<T extends Mob> extends Behavior<T> {
    private long endTimestamp=10;
    private final Predicate<T> canStart;
    private final Predicate<T> canStillUse;
    private final int duration;
    private final UniformInt cooldown;
    private final byte animationByte;

    public StopAndTriggerAnimation(int duration, Predicate<T> canStart, Predicate<T> canStillUse, UniformInt cooldown, byte animationByte) {
        super(ImmutableMap.of(
                MemoryModuleType.AVOID_TARGET,
                MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.ATTACK_TARGET,
                MemoryStatus.VALUE_ABSENT,
                Primal_MemoryModuleTypes.WAS_TRIGGER_ANIMATION.get(),
                MemoryStatus.VALUE_ABSENT), duration);
        this.duration=duration;
        this.canStart=canStart;
        this.canStillUse=canStillUse;
        this.cooldown=cooldown;
        this.animationByte=animationByte;
    }

    public static<T extends Mob> StopAndTriggerAnimation<T> create(byte animationByte, int duration, Predicate<T> canStart, UniformInt cooldown){
        return new StopAndTriggerAnimation<>(duration, canStart, canStart, cooldown, animationByte);
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

        mob.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        mob.getNavigation().stop();
        mob.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
        if(this.animationByte!=(byte) -1){
            mob.level().broadcastEntityEvent(mob, animationByte);
        }

        this.endTimestamp = gameTime + (long) duration;
    }

    @Override
    protected void stop(@NotNull ServerLevel level, @NotNull T mob, long gameTime) {
        mob.getBrain().setMemoryWithExpiry(Primal_MemoryModuleTypes.WAS_TRIGGER_ANIMATION.get(), true, mob.level().getRandom().nextIntBetweenInclusive(cooldown.getMinValue(), cooldown.getMaxValue()));
    }

    @Override
    protected boolean timedOut(long gameTime) {
        return gameTime > this.endTimestamp;
    }
}