package org.primal.entity.ai.behavior.deer;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.DeerEntity;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.registry.Primal_Sounds;

public class DeerHeadbutt extends Behavior<DeerEntity> {
    private final UniformInt cooldownBetweenAttacks;
    public DeerHeadbutt(UniformInt cooldownBetweenAttacks) {
        super(ImmutableMap.of(
                MemoryModuleType.LOOK_TARGET,
                MemoryStatus.REGISTERED,
                Primal_MemoryModuleTypes.LUNGE_COOLDOWN.get(),
                MemoryStatus.VALUE_ABSENT,
                Primal_MemoryModuleTypes.NEAREST_PLAY_MOB.get(),
                MemoryStatus.VALUE_PRESENT)
        );

        this.cooldownBetweenAttacks=cooldownBetweenAttacks;
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull DeerEntity deer) {
        var nearestVisibleLivingEntities = deer.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
        var target = deer.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_PLAY_MOB.get());
        if(target.isEmpty() || nearestVisibleLivingEntities.isEmpty())
            return false;

        return deer.isWithinMeleeAttackRange(target.get())
                && nearestVisibleLivingEntities.get().contains(target.get());
    }

    @Override
    protected void start(@NotNull ServerLevel level, @NotNull DeerEntity deer, long gameTime) {
        var otherDeer = deer.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_PLAY_MOB.get());
        if(otherDeer.isEmpty())
            return;

//            deer.doHurtTarget(otherDeer.get());
        if (otherDeer.get() instanceof LivingEntity living) {
            float strength = (float) deer.getAttributeValue(Attributes.ATTACK_KNOCKBACK);

            if (strength > 0.0F) {
                living.knockback(
                        strength * 0.5F,
                        Mth.sin(deer.getYRot() * ((float)Math.PI / 180F)),
                        -Mth.cos(deer.getYRot() * ((float)Math.PI / 180F))
                );

                deer.setDeltaMovement(deer.getDeltaMovement().multiply(0.6, 1.0, 0.6));
            }
        }
        stop(level, deer, gameTime);
    }

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull DeerEntity deer, long gameTime) {}

    @Override
    protected void stop(@NotNull ServerLevel level, DeerEntity deer, long gameTime) {
        deer.getBrain().setMemoryWithExpiry(Primal_MemoryModuleTypes.LUNGE_COOLDOWN.get(), true, this.cooldownBetweenAttacks.sample(deer.getRandom()));
        deer.getBrain().eraseMemory(Primal_MemoryModuleTypes.NEAREST_PLAY_MOB.get());
        deer.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
        deer.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);

        ItemStack antlerStack = new ItemStack(DeerEntity.Variant.ANTLERS.get(deer.getVariant()), 1);

        deer.playSound(Primal_Sounds.DEER_RAM_IMPACT.get());

        boolean breaks=false;
        //15% probability of loosing any antler
        if(deer.getRandom().nextFloat()< 0.15){
            deer.setRightAntler(false);
            BehaviorUtils.throwItem(deer, antlerStack, deer.position().add(0, 1, 0));
            breaks=true;
        }
        if(deer.getRandom().nextFloat()< 0.15){
            deer.setLeftAntler(false);
            BehaviorUtils.throwItem(deer, antlerStack, deer.position().add(0, 1, 0));
            breaks=true;
        }

        if(breaks){
            deer.playSound(Primal_Sounds.DEER_ANTLER_BREAK.get());
            deer.level().broadcastEntityEvent(deer, (byte)36);
        }
    }
}
