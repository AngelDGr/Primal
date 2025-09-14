package org.primal.entity.ai.sensors.bear;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.primal.registry.Primal_Tags;
import org.primal.entity.animal.BearEntity;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.NearestLivingEntitySensor;
import net.minecraft.world.entity.ai.sensing.Sensor;

public final class BearAttackEntitySensor extends NearestLivingEntitySensor<BearEntity> {

    @Override
    public @NotNull Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.copyOf(Iterables.concat(super.requires(), List.of(MemoryModuleType.NEAREST_ATTACKABLE)));
    }

    @Override
    protected void doTick(@NotNull ServerLevel level, @NotNull BearEntity bear) {
        super.doTick(level, bear);

        //Tame logic
        if(bear.isTame()){
            bear.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).stream().flatMap(Collection::stream)
                    .filter(target ->
                            Sensor.isEntityAttackable(bear, target)
                                    && bear.getOwner()!=null
                                    //To defend owner or attack targets when following
                                    && (bear.getOwner().getLastHurtByMob()==target || (bear.getOwner().getLastHurtMob()==target && bear.isFollowing()))
                                    //To not attack other owned bear
                                    && !(target instanceof BearEntity bear2 && bear2.getOwner()!=null && bear2.getOwner()==bear.getOwner())
                    )
                    .findFirst()
                    .ifPresentOrElse(ent -> bear.getBrain().setMemory(MemoryModuleType.NEAREST_ATTACKABLE, ent),
                            () -> bear.getBrain().eraseMemory(MemoryModuleType.NEAREST_ATTACKABLE));
        }
        //Wild logic
        else {
            bear.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).stream().flatMap(Collection::stream)
                    .filter(
                            target ->
                                    Sensor.isEntityAttackable(bear, target)
                                            //To attack prey
                                            && target.getType().is(Primal_Tags.BEAR_HUNTABLE)
                                            //To not attack enemies near campfires
                                            && !(target.level() instanceof ServerLevel serverLevel && BearRepellentSensor.findNearestRepellent(serverLevel, target).isPresent()))
                    .findFirst()
                    .ifPresentOrElse(ent -> bear.getBrain().setMemory(MemoryModuleType.NEAREST_ATTACKABLE, ent),
                            () -> bear.getBrain().eraseMemory(MemoryModuleType.NEAREST_ATTACKABLE));
        }
    }
}
