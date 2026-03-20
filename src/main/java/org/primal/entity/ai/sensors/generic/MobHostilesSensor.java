package org.primal.entity.ai.sensors.generic;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.NearestVisibleLivingEntitySensor;
import org.jetbrains.annotations.NotNull;

public class MobHostilesSensor extends NearestVisibleLivingEntitySensor {

    private final ImmutableMap<EntityType<?>, Float> hostilesList;

    public MobHostilesSensor(@NotNull ImmutableMap<EntityType<?>, Float> hostilesList){
        super();
        this.hostilesList = hostilesList;
    }

    @Override
    protected boolean isMatchingEntity(@NotNull LivingEntity attacker, @NotNull LivingEntity target) {
        return this.isHostile(target) && this.isClose(attacker, target);
    }

    private boolean isClose(LivingEntity attacker, LivingEntity target) {
        Float minDistance = hostilesList.get(target.getType());
        if (minDistance == null) return false;

        return target.distanceToSqr(attacker) <= (double)(minDistance * minDistance);
    }

    @Override
    protected @NotNull MemoryModuleType<LivingEntity> getMemory() {
        return MemoryModuleType.NEAREST_HOSTILE;
    }

    private boolean isHostile(LivingEntity entity) {
        return hostilesList.containsKey(entity.getType());
    }
}
