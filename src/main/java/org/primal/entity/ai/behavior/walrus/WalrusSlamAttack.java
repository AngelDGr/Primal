package org.primal.entity.ai.behavior.walrus;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.WalrusEntity;

public class WalrusSlamAttack extends Behavior<WalrusEntity> {

    private final int cooldownBetweenAttacks;

    public WalrusSlamAttack(int cooldownBetweenAttacks) {
        super(ImmutableMap.of(
                MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED,
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_ABSENT
        ), 10);
        this.cooldownBetweenAttacks = cooldownBetweenAttacks;
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull WalrusEntity walrus) {
        Brain<?> brain = walrus.getBrain();
        LivingEntity target = brain.getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
        if (target == null) return false;

        return isWithinSlamAttackRange(walrus, target) && !walrus.isUnderWater() && walrus.onGround();
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, @NotNull WalrusEntity walrus, long gameTime) {
        return hasRequiredMemories(walrus);
    }

    @Override
    protected void start(@NotNull ServerLevel level, WalrusEntity walrus, long gameTime) {
        Brain<?> brain = walrus.getBrain();
        LivingEntity target = brain.getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
        if (target == null) return;

        walrus.stopTriggeredAnim("base_controller", "ground_pound");
        walrus.triggerAnim("base_controller", "ground_pound");

        brain.setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
        walrus.getNavigation().stop();
        walrus.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
    }

    @Override
    protected void tick(@NotNull ServerLevel level, WalrusEntity entity, long gameTime) {
        entity.getNavigation().stop();
        entity.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
    }

    @Override
    protected void stop(@NotNull ServerLevel level, WalrusEntity entity, long gameTime) {
        Brain<?> brain = entity.getBrain();

        entity.doSlamAttackDamage(0);
        entity.stopTriggeredAnim("base_controller", "ground_pound");
        brain.setMemoryWithExpiry(MemoryModuleType.ATTACK_COOLING_DOWN, true, cooldownBetweenAttacks);
    }

    public boolean isWithinSlamAttackRange(WalrusEntity walrus, LivingEntity target) {
        return walrus.getSlamAttackBoundingBox().intersects(target.getHitbox());
    }
}
