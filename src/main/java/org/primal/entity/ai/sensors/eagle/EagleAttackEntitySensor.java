package org.primal.entity.ai.sensors.eagle;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.NearestLivingEntitySensor;
import net.minecraft.world.entity.ai.sensing.Sensor;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.EagleEntity;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public final class EagleAttackEntitySensor extends NearestLivingEntitySensor<EagleEntity> {

    @Override
    public @NotNull Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.copyOf(Iterables.concat(super.requires(), List.of(MemoryModuleType.NEAREST_ATTACKABLE)));
    }

    @Override
    protected void doTick(@NotNull ServerLevel level, @NotNull EagleEntity eagle) {
        super.doTick(level, eagle);
        
        //Tame logic
        if(eagle.isTame()){
            eagle.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).stream().flatMap(Collection::stream)
                    .filter(target ->
                            {
                                if(eagle.isBaby())
                                    return Sensor.isEntityAttackable(eagle, target)
                                            && eagle.getOwner()!=null
                                            //To defend owner or attack targets when following
                                            && (eagle.getOwner().getLastHurtByMob()==target || (eagle.getOwner().getLastHurtMob()==target && eagle.isFollowing()))
                                            //To not attack other owned eagle
                                            && !(target instanceof EagleEntity eagle2 && eagle2.getOwner()!=null && eagle2.getOwner()==eagle.getOwner())
                                            && eagle.canBabyAttack(target);
                                else
                                    return Sensor.isEntityAttackable(eagle, target)
                                        && eagle.getOwner()!=null
                                        //To defend owner or attack targets when following
                                        && (eagle.getOwner().getLastHurtByMob()==target || (eagle.getOwner().getLastHurtMob()==target && eagle.isFollowing()))
                                        //To not attack other owned eagle
                                        && !(target instanceof EagleEntity eagle2 && eagle2.getOwner()!=null && eagle2.getOwner()==eagle.getOwner());
                            }

                    )
                    .findFirst()
                    .ifPresentOrElse(ent -> eagle.getBrain().setMemory(MemoryModuleType.NEAREST_ATTACKABLE, ent),
                            () -> eagle.getBrain().eraseMemory(MemoryModuleType.NEAREST_ATTACKABLE));
        }
        //Wild logic
        else {
            eagle.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).stream().flatMap(Collection::stream)
                    .filter(
                            target ->
                            {
                                if(eagle.isBaby())
                                    return Sensor.isEntityAttackable(eagle, target) && eagle.canAttack(target) && eagle.canBabyAttack(target) && eagle.distanceToSqr(target)<9;
                                else
                                    return Sensor.isEntityAttackable(eagle, target) && eagle.canAttack(target);
                            }

                    )
                    .findFirst()
                    .ifPresentOrElse(ent -> eagle.getBrain().setMemory(MemoryModuleType.NEAREST_ATTACKABLE, ent),
                            () -> eagle.getBrain().eraseMemory(MemoryModuleType.NEAREST_ATTACKABLE));    
        }
    }

    protected int radiusXZ() {
        return 48;
    }

    protected int radiusY() {
        return 48;
    }
}
