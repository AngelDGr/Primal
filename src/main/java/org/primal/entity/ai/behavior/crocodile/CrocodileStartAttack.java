package org.primal.entity.ai.behavior.crocodile;

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
import org.primal.entity.animal.CrocodileEntity;
import org.primal.registry.Primal_MemoryModuleTypes;

public class CrocodileStartAttack extends Behavior<CrocodileEntity> {
    private final int cooldownBetweenAttacks;
    public CrocodileStartAttack(int cooldownBetweenAttacks) {
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
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull CrocodileEntity crocodile) {

        LivingEntity target = crocodile.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
        NearestVisibleLivingEntities nearestVisibleLivingEntities = crocodile.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).get();
        return !isHoldingUsableProjectileWeapon(crocodile)
                && crocodile.isWithinMeleeAttackRange(target)
                && nearestVisibleLivingEntities.contains(target)
                && crocodile.getPassengers().isEmpty();
    }

    @Override
    protected void start(@NotNull ServerLevel level, @NotNull CrocodileEntity crocodile, long gameTime) {
        LivingEntity target = crocodile.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();

        crocodile.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
        crocodile.swing(InteractionHand.MAIN_HAND);

        //Picks up an entity if it's not too big
        if(CrocodileEntity.canPickUpEntity(target, crocodile)){
            target.startRiding(crocodile);
            crocodile.setHealthWhenStartRiding(crocodile.getHealth());
            crocodile.setPose(Pose.SPIN_ATTACK);
            crocodile.getBrain().setMemory(Primal_MemoryModuleTypes.IS_THRASHING.get(), true);

            stop(level, crocodile, gameTime);
        } else {
            crocodile.doHurtTarget(target);

            stop(level, crocodile, gameTime);
        }
    }

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull CrocodileEntity crocodile, long gameTime) {}

    @Override
    protected void stop(@NotNull ServerLevel level, CrocodileEntity crocodile, long gameTime) {
        crocodile.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_COOLING_DOWN, true, this.cooldownBetweenAttacks);
    }

    private static boolean isHoldingUsableProjectileWeapon(Mob mob) {
        return mob.isHolding(stack -> {
            Item item = stack.getItem();
            return item instanceof ProjectileWeaponItem && mob.canFireProjectileWeapon((ProjectileWeaponItem)item);
        });
    }
}
