package org.primal.entity.ai.behavior.generic;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class ConditionalDoNothing<T extends LivingEntity> implements BehaviorControl<T> {
    private final int minDuration;
    private final int maxDuration;
    private Behavior.Status status = Behavior.Status.STOPPED;
    private long endTimestamp;
    private final Predicate<T> canUse;

    public ConditionalDoNothing(int minDuration, int maxDuration, Predicate<T> canUse) {
        this.minDuration = minDuration;
        this.maxDuration = maxDuration;
        this.canUse=canUse;
    }

    @Override
    public Behavior.@NotNull Status getStatus() {
        return this.status;
    }

    @Override
    public final boolean tryStart(@NotNull ServerLevel level, @NotNull T entity, long gameTime) {
        if(!this.canUse.test(entity)) return false;

        this.status = Behavior.Status.RUNNING;
        int i = this.minDuration + level.getRandom().nextInt(this.maxDuration + 1 - this.minDuration);
        this.endTimestamp = gameTime + (long)i;
        return true;
    }

    @Override
    public final void tickOrStop(@NotNull ServerLevel level, @NotNull T entity, long gameTime) {
        if (gameTime > this.endTimestamp || !this.canUse.test(entity)) {
            this.doStop(level, entity, gameTime);
        }
    }

    @Override
    public final void doStop(@NotNull ServerLevel level, @NotNull T entity, long gameTime) {
        this.status = Behavior.Status.STOPPED;
    }

    @Override
    public @NotNull String debugString() {
        return this.getClass().getSimpleName();
    }
}
