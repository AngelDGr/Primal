package org.primal.entity.ai.behavior.eagle;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.EagleEntity;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.util.MiscUtil;

public class EagleStartAttack extends Behavior<EagleEntity> {

    private final int cooldownBetweenAttacks;
    public EagleStartAttack(int cooldownBetweenAttacks) {
        super(ImmutableMap.of(
                MemoryModuleType.LOOK_TARGET,
                MemoryStatus.REGISTERED,
                MemoryModuleType.ATTACK_TARGET,
                MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.ATTACK_COOLING_DOWN,
                MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
                MemoryStatus.VALUE_PRESENT,

                MemoryModuleType.AVOID_TARGET,
                MemoryStatus.VALUE_ABSENT,

                Primal_MemoryModuleTypes.AMOUNT_ATTACKED.get(),
                MemoryStatus.REGISTERED
        ));
        
        this.cooldownBetweenAttacks=cooldownBetweenAttacks;
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull EagleEntity eagle) {
        LivingEntity target = eagle.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
        NearestVisibleLivingEntities nearestVisibleLivingEntities = eagle.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).get();
        
        return !isHoldingUsableProjectileWeapon(eagle)
                && eagle.isWithinMeleeAttackRange(target)
                && nearestVisibleLivingEntities.contains(target)
                && eagle.getPassengers().isEmpty();
    }

    @Override
    protected void start(@NotNull ServerLevel level, @NotNull EagleEntity eagle, long gameTime) {
        LivingEntity target = eagle.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();

        eagle.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
        eagle.swing(InteractionHand.MAIN_HAND);

        double speed = eagle.getDeltaMovement().length();



        //Damages the entity if the eagle goes too fast
        if(speed>0.5){
            target.hurt(level.damageSources().mobAttack(eagle), 1);
        }

        //Picks up an entity if it's not too big
        if(EagleEntity.canPickUpEntity(target, eagle)) {

            target.startRiding(eagle);

            eagle.setHealthWhenStartRiding(eagle.getHealth());
            eagle.getBrain().setMemory(Primal_MemoryModuleTypes.IS_SNATCHING.get(), true);

            stop(level, eagle, gameTime);
        } else {
            eagle.doHurtTarget(target);

            MiscUtil.addToAttackCount(eagle);

            if(eagle.getBrain().getMemory(Primal_MemoryModuleTypes.AMOUNT_ATTACKED.get()).isPresent()
                    && eagle.getBrain().getMemory(Primal_MemoryModuleTypes.AMOUNT_ATTACKED.get()).get()>3){

                eagle.getBrain().setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, target, level.getRandom().nextIntBetweenInclusive(60, 100));

                eagle.getBrain().setMemory(Primal_MemoryModuleTypes.AMOUNT_ATTACKED.get(), 0);
            }

            stop(level, eagle, gameTime);
        }
    }

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull EagleEntity eagle, long gameTime) {}

    @Override
    protected void stop(@NotNull ServerLevel level, EagleEntity eagle, long gameTime) {
        eagle.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_COOLING_DOWN, true, this.cooldownBetweenAttacks);
    }

    private static boolean isHoldingUsableProjectileWeapon(Mob mob) {
        return mob.isHolding(stack -> {
            Item item = stack.getItem();
            return item instanceof ProjectileWeaponItem && mob.canFireProjectileWeapon((ProjectileWeaponItem)item);
        });
    }
}
