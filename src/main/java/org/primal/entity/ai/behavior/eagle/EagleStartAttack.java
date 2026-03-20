package org.primal.entity.ai.behavior.eagle;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.EagleEntity;
import org.primal.entity.animal.SnakeEntity;
import org.primal.registry.Primal_Advancements;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.util.Primal_Util;

import java.util.List;

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
        var target = eagle.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
        if(target.isEmpty()) return false;

        var nearestVisibleLivingEntities = eagle.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);

        return nearestVisibleLivingEntities.filter(visibleLivingEntities -> !isHoldingUsableProjectileWeapon(eagle)
                && eagle.isWithinMeleeAttackRange(target.get())
                && visibleLivingEntities.contains(target.get())
                && eagle.getPassengers().isEmpty()).isPresent();

    }

    @Override
    protected void start(@NotNull ServerLevel level, @NotNull EagleEntity eagle, long gameTime) {
        if(eagle.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isEmpty()) return;

        LivingEntity target = eagle.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();

        eagle.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
        eagle.swing(InteractionHand.MAIN_HAND);

        float speed = (float) eagle.getDeltaMovement().length();
        boolean alreadyHurt=false;

        var xRotWrapped= Mth.wrapDegrees(eagle.getXRot());
        boolean swoop = xRotWrapped>40 && !eagle.isVehicle();
        //Damages the entity if the eagle goes too fast
        if(speed>=0.35 && swoop){
            float scaled = speed * speed;
            float damage = 1.5f + scaled * 6f;
            target.hurt(level.damageSources().mobAttack(eagle), damage);
            //To avoid too much damage
            alreadyHurt=true;
        }

        //Picks up an entity if it's not too big
        if(eagle.canPickUpEntity(target)) {
            //To alert target
            if(eagle.getRandom().nextIntBetweenInclusive(0, 1)==0 && !alreadyHurt) target.hurt(level.damageSources().mobAttack(eagle), 0);

            target.startRiding(eagle);

            //Triggers advancement
            if(target instanceof SnakeEntity && eagle.getVariant().equals(EagleEntity.Variant.GOLDEN)){
                List<ServerPlayer> playersList= eagle.level().getEntitiesOfClass(ServerPlayer.class, eagle.getBoundingBox().inflate(8));

                if(!playersList.isEmpty())
                    playersList.forEach(player -> Primal_Advancements.EAGLE_ATTACKS_SNAKE.get().trigger(player));
            }

            eagle.setHealthWhenStartRiding(eagle.getHealth());
            eagle.getBrain().setMemory(Primal_MemoryModuleTypes.IS_GRABBING.get(), true);

            stop(level, eagle, gameTime);
        } else {
            if(!alreadyHurt)
                eagle.doHurtTarget(target);

            if(!eagle.isBaby()){
                Primal_Util.addToAttackCount(eagle);

                if(eagle.getBrain().getMemory(Primal_MemoryModuleTypes.AMOUNT_ATTACKED.get()).isPresent()
                        && eagle.getBrain().getMemory(Primal_MemoryModuleTypes.AMOUNT_ATTACKED.get()).get()>3){

                    eagle.getBrain().setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, target, level.getRandom().nextIntBetweenInclusive(20, 80));

                    eagle.getBrain().setMemory(Primal_MemoryModuleTypes.AMOUNT_ATTACKED.get(), 0);
                }
            }

            stop(level, eagle, gameTime);
        }
    }

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull EagleEntity eagle, long gameTime) {}

    @Override
    protected void stop(@NotNull ServerLevel level, EagleEntity eagle, long gameTime) {
        if(!eagle.isBaby())
            eagle.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_COOLING_DOWN, true, this.cooldownBetweenAttacks);
    }

    private static boolean isHoldingUsableProjectileWeapon(Mob mob) {
        return mob.isHolding(stack -> {
            Item item = stack.getItem();
            return item instanceof ProjectileWeaponItem && mob.canFireProjectileWeapon((ProjectileWeaponItem)item);
        });
    }
}
