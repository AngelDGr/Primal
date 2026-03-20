package org.primal.entity.ai.sensors;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.ai.DeerAi;
import org.primal.entity.ai.sensors.generic.PackAnimalSensor;
import org.primal.entity.animal.DeerEntity;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.util.Primal_Util;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public final class DeerEntitySensor extends PackAnimalSensor<DeerEntity> {

    public DeerEntitySensor() {
        super(false);
    }

    @Override
    public @NotNull Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.copyOf(Iterables.concat(super.requires(), List.of()));
    }

    @Override
    protected void doTick(@NotNull ServerLevel level, @NotNull DeerEntity deer) {
        super.doTick(level, deer);

        //Look directly
        Primal_Util.Ai.setMemoryFromVisibleEntity(deer, deer::canBeCautious, Primal_MemoryModuleTypes.NEAREST_CAUTIOUS.get());

        //Starts running
        deer.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).stream().flatMap(Collection::stream)
                .filter(t-> deer.canBeScared(t) && canSeeAvoid(deer, t))
                .findFirst()
                .ifPresentOrElse(ent -> {
                            if(deer.getBrain().getMemory(MemoryModuleType.AVOID_TARGET).isEmpty())
                                deer.setAlertOthersTime(Primal_Util.toTicks(5));

                            deer.getBrain().setMemory(MemoryModuleType.AVOID_TARGET, ent);
                        },
                        () -> deer.getBrain().eraseMemory(MemoryModuleType.AVOID_TARGET));

        //Headbutting
        Primal_Util.Ai.setMemoryFromVisibleEntity(deer, deer::canHeadbutt, Primal_MemoryModuleTypes.NEAREST_PLAY_MOB.get());

        this.getCautiousFromAnotherDeer(deer);
    }

    private boolean canSeeAvoid(@NotNull DeerEntity deer, LivingEntity target){
        return deer.getBrain().isMemoryValue(MemoryModuleType.AVOID_TARGET, target)
                ? TargetingConditions.forNonCombat().range(radiusXZ()).ignoreInvisibilityTesting().ignoreLineOfSight().test(deer, target)
                : Primal_Util.Ai.getTargetConditions(radiusXZ(), false).test(deer, target);
    }

    private void getCautiousFromAnotherDeer(@NotNull DeerEntity deer) {

        //To avoid setting another target if it already has one and avoid infinite setting
        if(deer.getBrain().getMemory(MemoryModuleType.AVOID_TARGET).isPresent()
                || deer.getBrain().getMemory(MemoryModuleType.NEAREST_REPELLENT).isPresent()
                || deer.getBrain().getMemory(MemoryModuleType.SNIFF_COOLDOWN).isPresent()){

            //Resets the cooldown for alerting
            if(deer.getBrain().getMemory(MemoryModuleType.AVOID_TARGET).isEmpty() && deer.getBrain().getMemory(MemoryModuleType.SNIFF_COOLDOWN).isPresent())
                deer.getBrain().eraseMemory(MemoryModuleType.SNIFF_COOLDOWN);

            return;
        }

        deer.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).ifPresent(
                nearestVisibleLivingEntities -> {
                    Stream<LivingEntity> nearbyDeers = nearestVisibleLivingEntities.find(t->
                            t instanceof DeerEntity otherDeer
                                    && otherDeer.getAlertOthersTime()>0
                                    && (otherDeer.getBrain().getMemory(MemoryModuleType.AVOID_TARGET).isPresent()
                                    || otherDeer.getBrain().getMemory(MemoryModuleType.NEAREST_REPELLENT).isPresent()));

                    nearbyDeers.forEach(
                      otherDeer-> {
                          var otherAvoidTarget = otherDeer.getBrain().getMemory(MemoryModuleType.AVOID_TARGET);
                          var otherNearestRepellent = otherDeer.getBrain().getMemory(MemoryModuleType.NEAREST_REPELLENT);

                          if(otherAvoidTarget.isPresent()){
                              int duration = DeerAi.RETREAT_DURATION.sample(deer.getRandom());

                              deer.getBrain().setMemoryWithExpiry(
                                      MemoryModuleType.AVOID_TARGET,
                                      otherAvoidTarget.get(),
                                      duration);

                              //An extra second to avoid being scared forever
                              deer.getBrain().setMemoryWithExpiry(
                                      MemoryModuleType.SNIFF_COOLDOWN,
                                      Unit.INSTANCE,
                                      duration+10);
                          } else if(otherNearestRepellent.isPresent()){
                              int duration = DeerAi.RETREAT_DURATION.sample(deer.getRandom());

                              deer.getBrain().setMemoryWithExpiry(
                                      MemoryModuleType.NEAREST_REPELLENT,
                                      otherNearestRepellent.get(),
                                      duration);

                              //An extra second to avoid being scared forever
                              deer.getBrain().setMemoryWithExpiry(
                                      MemoryModuleType.SNIFF_COOLDOWN,
                                      Unit.INSTANCE,
                                      duration+10);
                          }
                      }
                    );
                }
        );
    }

    @Override
    protected int radiusXZ() {
        return 24;
    }
}
