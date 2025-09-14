package org.primal.entity.ai.behavior.generic;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import org.jetbrains.annotations.NotNull;
import org.primal.registry.Primal_Activities;

import java.util.function.Predicate;

public class ConditionalSwim extends Behavior<Mob> {
    private final float chance;
    private final Predicate<Mob> canSwim;

    public ConditionalSwim(float chance, Predicate<Mob> canSwim) {
        super(ImmutableMap.of());
        this.chance = chance;
        this.canSwim=canSwim;
    }

    public static boolean shouldSwim(Mob mob, Predicate<Mob> canSwim) {
        return canSwim.test(mob) && mob.isInWater() && mob.getFluidHeight(FluidTags.WATER) > mob.getFluidJumpThreshold()
                || mob.isInLava()
                || mob.isInFluidType((fluidType, height) -> mob.canSwimInFluidType(fluidType) && height > mob.getFluidJumpThreshold());
    }

    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull Mob owner) {
        return shouldSwim(owner, this.canSwim);
    }

    protected boolean canStillUse(@NotNull ServerLevel level, @NotNull Mob entity, long gameTime) {
        return this.checkExtraStartConditions(level, entity);
    }

    protected void tick(@NotNull ServerLevel level, Mob owner, long gameTime) {
        if (owner.getRandom().nextFloat() < this.chance) {
            owner.getJumpControl().jump();
        }
    }
}
