package org.primal.entity.ai.behavior.generic;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class ConditionalLookAround<T extends LivingEntity> extends Behavior<T> {
    private final IntProvider interval;
    private final float maxYaw;
    private final float minPitch;
    private final float pitchRange;
    private final Predicate<T> canUse;

    public ConditionalLookAround(IntProvider interval, float maxYaw, float minPitch, float maxPitch, Predicate<T> canUse) {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.GAZE_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT));
        if (minPitch > maxPitch) {
            throw new IllegalArgumentException("Minimum pitch is larger than maximum pitch! " + minPitch + " > " + maxPitch);
        } else {
            this.interval = interval;
            this.maxYaw = maxYaw;
            this.minPitch = minPitch;
            this.pitchRange = maxPitch - minPitch;
            this.canUse=canUse;
        }
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull T owner) {
        return super.checkExtraStartConditions(level, owner) && canUse.test(owner);
    }

    @Override
    protected void start(@NotNull ServerLevel level, T entity, long gameTime) {
        RandomSource randomsource = entity.getRandom();
        float f = Mth.clamp(randomsource.nextFloat() * this.pitchRange + this.minPitch, -90.0F, 90.0F);
        float f1 = Mth.wrapDegrees(entity.getYRot() + 2.0F * randomsource.nextFloat() * this.maxYaw - this.maxYaw);
        Vec3 vec3 = Vec3.directionFromRotation(f, f1);
        entity.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(entity.getEyePosition().add(vec3)));
        entity.getBrain().setMemory(MemoryModuleType.GAZE_COOLDOWN_TICKS, this.interval.sample(randomsource));
    }
}
