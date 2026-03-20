package org.primal.entity.ai.behavior.lion;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.LionEntity;
import org.primal.registry.Primal_MemoryModuleTypes;

public class LionStartAttack extends Behavior<LionEntity> {
    private final int cooldownBetweenAttacks;
    public LionStartAttack(int cooldownBetweenAttacks) {
        super(ImmutableMap.of(
                MemoryModuleType.LOOK_TARGET,
                MemoryStatus.REGISTERED,
                MemoryModuleType.ATTACK_TARGET,
                MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.ATTACK_COOLING_DOWN,
                MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.NEAREST_ATTACKABLE,
                MemoryStatus.VALUE_PRESENT)
        );

        this.cooldownBetweenAttacks=cooldownBetweenAttacks;
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull LionEntity lion) {

        LivingEntity target = lion.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
        NearestVisibleLivingEntities nearestVisibleLivingEntities = lion.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).get();
        return !isHoldingUsableProjectileWeapon(lion)
                && lion.isWithinMeleeAttackRange(target)
                && nearestVisibleLivingEntities.contains(target)
                && lion.getPassengers().isEmpty();
    }

    @Override
    protected void start(@NotNull ServerLevel level, @NotNull LionEntity lion, long gameTime) {
        LivingEntity target = lion.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
        lion.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
        lion.swing(InteractionHand.MAIN_HAND);
        //Picks up an entity if it's not too big
        if(LionEntity.canPickUpEntity(target, lion)){
            target.startRiding(lion);
            lion.setHealthWhenStartRiding(lion.getHealth());
            lion.setPose(Pose.SPIN_ATTACK);
            lion.getBrain().setMemory(Primal_MemoryModuleTypes.IS_GRABBING.get(), true);

            lion.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
            lion.getNavigation().stop();
            lion.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);

            stop(level, lion, gameTime);
        } else {
            lion.doHurtTarget(target);

            stop(level, lion, gameTime);
        }
    }

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull LionEntity lion, long gameTime) {}

    @Override
    protected void stop(@NotNull ServerLevel level, LionEntity lion, long gameTime) {
        lion.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_COOLING_DOWN, true, this.cooldownBetweenAttacks);
    }

    private static boolean isHoldingUsableProjectileWeapon(Mob mob) {
        return mob.isHolding(stack -> {
            Item item = stack.getItem();
            return item instanceof ProjectileWeaponItem && mob.canFireProjectileWeapon((ProjectileWeaponItem)item);
        });
    }
}
