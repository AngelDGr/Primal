package org.primal.entity.ai.behavior.lion;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.LionEntity;
import org.primal.registry.Primal_MemoryModuleTypes;

public class LionMaul extends Behavior<LionEntity> {

    public LionMaul(int maxDuration) {
        super(ImmutableMap.of(
                Primal_MemoryModuleTypes.IS_GRABBING.get(), MemoryStatus.VALUE_PRESENT,
                Primal_MemoryModuleTypes.IS_STUNNED.get(), MemoryStatus.VALUE_ABSENT),
                maxDuration);
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, LionEntity lion) {
        return !lion.getPassengers().isEmpty();
    }

    @Override
    protected void start(@NotNull ServerLevel level, @NotNull LionEntity lion, long gameTime) {
        lion.setMauling(true);
        lion.setPose(Pose.SPIN_ATTACK);
        stopMoving(lion);
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, @NotNull LionEntity lion, long gameTime) {
        return checkExtraStartConditions(level, lion) && this.hasRequiredMemories(lion);
    }

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull LionEntity lion, long gameTime) {
        LivingEntity target = !lion.getPassengers().isEmpty() && lion.getPassengers().get(0) instanceof LivingEntity living? living: null;
        stopMoving(lion);
        //If it doesn't have target
        if(target==null){
            stop(level, lion, gameTime);
            return;
        }

        //Particles
        if(lion.isInWater() && !lion.isUnderWater())
            lion.level().broadcastEntityEvent(lion, (byte)80);

        //It hurt the picked entity each 2 ticks
        if(gameTime%2==0){
            target.hurt(lion.level().damageSources().mobAttack(lion), 3);
        }
    }

    @Override
    protected void stop(@NotNull ServerLevel level, @NotNull LionEntity lion, long gameTime) {
        stopMauling(lion);
    }

    public static void stopMauling(LionEntity lion){
        if(!lion.getPassengers().isEmpty()) lion.ejectPassengers();
        lion.setPose(Pose.STANDING);
        lion.getBrain().eraseMemory(Primal_MemoryModuleTypes.IS_GRABBING.get());
        lion.getBrain().setMemoryWithExpiry(Primal_MemoryModuleTypes.IS_STUNNED.get(), true, 60L);
        lion.setMauling(false);
    }

    public void stopMoving(LionEntity lion){
        lion.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        lion.getNavigation().stop();
        lion.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
    }
}
