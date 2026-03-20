package org.primal.entity.ai.behavior.crocodile;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.CrocodileEntity;
import org.primal.registry.Primal_MemoryModuleTypes;

public class CrocodileThrash extends Behavior<CrocodileEntity> {

    public CrocodileThrash(int maxDuration) {
        super(ImmutableMap.of(
                Primal_MemoryModuleTypes.IS_GRABBING.get(), MemoryStatus.VALUE_PRESENT,
                Primal_MemoryModuleTypes.IS_STUNNED.get(), MemoryStatus.VALUE_ABSENT),
                maxDuration);
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, CrocodileEntity crocodile) {
        return !crocodile.getPassengers().isEmpty();
    }

    @Override
    protected void start(@NotNull ServerLevel level, @NotNull CrocodileEntity crocodile, long gameTime) {
        crocodile.setThrashing(true);
        crocodile.setPose(Pose.SPIN_ATTACK);
        if (!crocodile.isSilent())
            crocodile.level().broadcastEntityEvent(crocodile, CrocodileEntity.CROCODILE_THRASHING);

        crocodile.stopMoving();
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, @NotNull CrocodileEntity crocodile, long gameTime) {
        return checkExtraStartConditions(level, crocodile) && this.hasRequiredMemories(crocodile);
    }

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull CrocodileEntity crocodile, long gameTime) {
        LivingEntity target = !crocodile.getPassengers().isEmpty() && crocodile.getPassengers().getFirst() instanceof LivingEntity living? living: null;
        crocodile.stopMoving();
        //If it doesn't have target
        if(target==null){
            stop(level, crocodile, gameTime);
            return;
        }

        //Particles
        if(crocodile.isInWater() && !crocodile.isUnderWater())
            crocodile.level().broadcastEntityEvent(crocodile, (byte)80);

        //It hurt the picked entity each 2 ticks
        if(gameTime%2==0){
            target.hurt(crocodile.level().damageSources().mobAttack(crocodile), 2);
        }
    }

    @Override
    protected void stop(@NotNull ServerLevel level, @NotNull CrocodileEntity crocodile, long gameTime) {
        stopThrashing(crocodile);
    }

    public static void stopThrashing(CrocodileEntity crocodile){
        if(!crocodile.getPassengers().isEmpty()) crocodile.ejectPassengers();
        crocodile.setPose(Pose.STANDING);
        crocodile.getBrain().eraseMemory(Primal_MemoryModuleTypes.IS_GRABBING.get());
        crocodile.getBrain().setMemoryWithExpiry(Primal_MemoryModuleTypes.IS_STUNNED.get(), true, 60L);
        crocodile.setThrashing(false);
    }
}
