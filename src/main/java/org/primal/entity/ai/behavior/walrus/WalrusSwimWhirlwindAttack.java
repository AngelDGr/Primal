package org.primal.entity.ai.behavior.walrus;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.WalrusEntity;
import org.primal.registry.Primal_MemoryModuleTypes;

public class WalrusSwimWhirlwindAttack extends Behavior<WalrusEntity> {

    public WalrusSwimWhirlwindAttack() {
        super(ImmutableMap.of(
                MemoryModuleType.LOOK_TARGET,
                MemoryStatus.REGISTERED,
                MemoryModuleType.ATTACK_TARGET,
                MemoryStatus.VALUE_PRESENT,
                Primal_MemoryModuleTypes.LUNGE_COOLDOWN.get(),
                MemoryStatus.VALUE_ABSENT), 20);
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull WalrusEntity walrus) {
        var target = walrus.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
        var nearestVisibleLivingEntities = walrus.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
        return target.isPresent() && nearestVisibleLivingEntities.isPresent()
                && nearestVisibleLivingEntities.get().contains(target.get())
                && walrus.isInWater()
                && walrus.isSeeingTarget(target.get());
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, @NotNull WalrusEntity entity, long gameTime) {
        return !entity.horizontalCollision && entity.getAutoSpinAttackTicks()>0;
    }

    @Override
    protected void start(@NotNull ServerLevel level, WalrusEntity walrus, long gameTime) {
        LivingEntity target = walrus.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
        walrus.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
        walrus.doWhirlwind(-1f);
    }

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull WalrusEntity walrus, long gameTime) {}

    @Override
    protected void stop(@NotNull ServerLevel level, WalrusEntity walrus, long gameTime) {
        walrus.stopWhirlwind();
    }
}
