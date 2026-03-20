package org.primal.entity.ai.behavior.generic.roar;

import javax.annotation.Nonnull;

import net.minecraft.util.Unit;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableMap;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.primal.util.mob_types.AnimalRoars;
import org.primal.util.Primal_Util;
import software.bernie.geckolib.animatable.GeoEntity;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class AnimalRoar<T extends Mob & AnimalRoars> extends Behavior<T> {

    private final float roarVolume;
    private final Consumer<T> atStop;
    private final int roarCooldown;

    public AnimalRoar(float roarVolume, Consumer<T> atStop, int roarCooldown) {
        super(ImmutableMap.of(
                MemoryModuleType.ROAR_TARGET,
                MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.ATTACK_TARGET,
                MemoryStatus.VALUE_ABSENT),
                45);
        this.roarVolume=roarVolume;
        this.atStop=atStop;
        this.roarCooldown=roarCooldown;
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, @NotNull T mob, long p_217590_) {
        return true;
    }

    @Override
    protected void start(@NotNull ServerLevel level, @Nonnull T mob, long gameTime) {
        stopMoving(mob);

        Optional<LivingEntity> target = mob.getBrain().getMemory(MemoryModuleType.ROAR_TARGET);
        target.ifPresent(entity -> BehaviorUtils.lookAtEntity(mob, entity));

        //It automatically starts attacking if it has cooldown
        if(mob.getBrain().hasMemoryValue(MemoryModuleType.ROAR_SOUND_COOLDOWN)){
            stop(level, mob, gameTime);
        } else {
            mob.setPose(Pose.ROARING);

            if(mob.getRoarSound()!=null) mob.playSound(mob.getRoarSound(), roarVolume, 1);

            if(mob instanceof GeoEntity geo)
                geo.triggerAnim("base_controller", "roar"+(mob.isInWater() && mob.hasCustomWaterRoar()? "_swim": "") );
        }
    }

    @Override
    protected void tick(@NotNull ServerLevel level, T mob, long gameTime) {
        mob.setPose(Pose.ROARING);
    }

    @Override
    protected void stop(@NotNull ServerLevel level, T mob, long gameTime) {
        if (mob.hasPose(Pose.ROARING)) mob.setPose(Pose.STANDING);

        atStop.accept(mob);

        mob.getBrain().eraseMemory(MemoryModuleType.ROAR_TARGET);

        if(!mob.getBrain().hasMemoryValue(MemoryModuleType.ROAR_SOUND_COOLDOWN)) mob.getBrain().setMemoryWithExpiry(MemoryModuleType.ROAR_SOUND_COOLDOWN, Unit.INSTANCE, roarCooldown);
    }

    private void stopMoving(T mob){
        mob.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        mob.getNavigation().stop();
        mob.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
    }

    public static<T extends Mob & AnimalRoars> void setAttackTarget(T mob){
        mob.getBrain().getMemory(MemoryModuleType.ROAR_TARGET).ifPresent(target -> setAttackTarget(mob, target));
    }

    public static<T extends Mob & AnimalRoars> void setAttackTargetAndNearby(T mob, Class<T> entityClass, Predicate<T> filter){
        var mobList = Primal_Util.Ai.getNearestMobs(mob, entityClass, filter, 30, 5);

        var roarTarget =  mob.getBrain().getMemory(MemoryModuleType.ROAR_TARGET);
        if(roarTarget.isEmpty()) return;

        setAttackTarget(mob, roarTarget.get());
        for(T nearbyMob : mobList) setAttackTarget(nearbyMob, roarTarget.get());
    }

    private static<T extends Mob & AnimalRoars> void setAttackTarget(T mob, LivingEntity attackTarget) {
        mob.getBrain().eraseMemory(MemoryModuleType.ROAR_TARGET);
        mob.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, attackTarget);
        mob.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
    }
}
