package org.primal.entity.ai.behavior.lion;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.LionEntity;
import org.primal.registry.Primal_MemoryModuleTypes;

public class LionLungeAttack extends Behavior<LionEntity> {
    private final int cooldownBetweenLunges;
    public LionLungeAttack(int cooldownBetweenLunges) {
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
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull LionEntity lion) {
        LivingEntity target = lion.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
        NearestVisibleLivingEntities nearestVisibleLivingEntities = lion.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).get();
        return lion.isWithinLungeAttackRange(target)
                && nearestVisibleLivingEntities.contains(target)
                && lion.isSeeingTarget(target)
                && lion.onGround();
    }

    @Override
    protected void start(@NotNull ServerLevel level, LionEntity lion, long gameTime) {
        LivingEntity target = lion.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();

        lion.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));

        Vec3 lookVec = lion.getLookAngle();

        float lungeSpeed    = 0.9f;
        float verticalBoost = 0.25f;

        lion.setDeltaMovement(
                lookVec.x * lungeSpeed,
                verticalBoost,
                lookVec.z * lungeSpeed
        );

        if(lion.getLungeSound()!=null)
            lion.playSound(lion.getLungeSound(), 1.0f, 1.0f);

        lion.triggerAnim("base_controller", "pounce");

        stop(level, lion, gameTime);
    }

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull LionEntity lion, long gameTime) {}

    @Override
    protected void stop(@NotNull ServerLevel level, LionEntity lion, long gameTime) {
        lion.getBrain().setMemoryWithExpiry(Primal_MemoryModuleTypes.LUNGE_COOLDOWN.get(), true, this.cooldownBetweenLunges);
    }
}
