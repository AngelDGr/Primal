package org.primal.entity.ai.sensors.bear;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.NearestLivingEntitySensor;
import net.minecraft.world.entity.ai.sensing.Sensor;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.BearEntity;
import org.primal.util.Primal_Util;

import java.util.List;
import java.util.Set;

public final class BearEntitySensor extends NearestLivingEntitySensor<BearEntity> {

    @Override
    public @NotNull Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.copyOf(Iterables.concat(super.requires(), List.of(MemoryModuleType.NEAREST_ATTACKABLE)));
    }

    @Override
    protected void doTick(@NotNull ServerLevel level, @NotNull BearEntity bear) {
        super.doTick(level, bear);

        //Tame logic
        if(bear.isTame())
            Primal_Util.Ai.setNearestAttackableOnSensor(bear, target ->
                    Sensor.isEntityAttackable(bear, target)
                            && bear.getOwner()!=null
                            //To defend owner or attack targets when following
                            && (bear.getOwner().getLastHurtByMob()==target || (bear.getOwner().getLastHurtMob()==target && bear.isFollowing()))
                            //To not attack other owned variantFromBiome
                            && !(target instanceof TamableAnimal pet2 && pet2.getOwner()!=null && pet2.getOwner()==bear.getOwner()));
        //Wild logic
        else
            Primal_Util.Ai.setNearestAttackableOnSensor(bear);
    }
}
