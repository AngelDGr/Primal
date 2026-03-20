package org.primal.entity.ai.behavior.generic;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.Swim;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class ConditionalSwim<T extends Mob> extends Behavior<T> {
    private final float chance;
    private final Predicate<T> canSwim;

    public ConditionalSwim(float chance, Predicate<T> canSwim) {
        super(ImmutableMap.of());
        this.chance = chance;
        this.canSwim=canSwim;
    }

    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull T owner) {
        return Swim.shouldSwim(owner) && canSwim.test(owner);
    }

    protected boolean canStillUse(@NotNull ServerLevel level, @NotNull T entity, long gameTime) {
        return this.checkExtraStartConditions(level, entity);
    }

    protected void tick(@NotNull ServerLevel level, T owner, long gameTime) {
        if (owner.getRandom().nextFloat() < this.chance) {
            owner.getJumpControl().jump();
        }
    }
}
