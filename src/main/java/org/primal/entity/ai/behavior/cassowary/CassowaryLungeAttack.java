package org.primal.entity.ai.behavior.cassowary;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.CassowaryEntity;
import org.primal.registry.Primal_MemoryModuleTypes;

public class CassowaryLungeAttack extends Behavior<CassowaryEntity> {
    private final int cooldownBetweenLunges;
    public CassowaryLungeAttack(int cooldownBetweenLunges) {
        super(ImmutableMap.of(
                MemoryModuleType.LOOK_TARGET,
                MemoryStatus.REGISTERED,
                MemoryModuleType.ATTACK_TARGET,
                MemoryStatus.VALUE_PRESENT,
                Primal_MemoryModuleTypes.LUNGE_COOLDOWN.get(),
                MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.NEAREST_ATTACKABLE,
                MemoryStatus.VALUE_PRESENT)
        );
        this.cooldownBetweenLunges = cooldownBetweenLunges;
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull CassowaryEntity cassowary) {
        var target = cassowary.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
        var nearestVisibleLivingEntities = cassowary.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
        return target.isPresent() && nearestVisibleLivingEntities.isPresent() &&
                cassowary.isWithinLungeAttackRange(target.get())
                && nearestVisibleLivingEntities.get().contains(target.get())
                && cassowary.isSeeingTarget(target.get())
                && cassowary.onGround();
    }

    @Override
    protected void start(@NotNull ServerLevel level, CassowaryEntity cassowary, long gameTime) {
        var target = cassowary.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
        if(target.isEmpty()){
            stop(level, cassowary, gameTime);
            return;
        }

        cassowary.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target.get(), true));

        Vec3 lookVec = cassowary.getLookAngle();

        float lungeSpeed    = 0.9f;
        float verticalBoost = 0.25f;

        cassowary.setDeltaMovement(
                lookVec.x * lungeSpeed,
                verticalBoost,
                lookVec.z * lungeSpeed
        );

        if(cassowary.getLungeSound()!=null)
            cassowary.playSound(cassowary.getLungeSound(), 1.0f, 1.0f);

        cassowary.startAnimation("Attacking");

        stop(level, cassowary, gameTime);
    }

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull CassowaryEntity cassowary, long gameTime) {}

    @Override
    protected void stop(@NotNull ServerLevel level, CassowaryEntity cassowary, long gameTime) {
        cassowary.getBrain().setMemoryWithExpiry(Primal_MemoryModuleTypes.LUNGE_COOLDOWN.get(), true, this.cooldownBetweenLunges);
    }
}
