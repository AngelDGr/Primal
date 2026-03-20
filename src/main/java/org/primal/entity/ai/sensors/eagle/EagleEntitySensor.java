package org.primal.entity.ai.sensors.eagle;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.NearestLivingEntitySensor;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.EagleEntity;
import org.primal.util.Primal_Util;

import java.util.List;
import java.util.Set;

public final class EagleEntitySensor extends NearestLivingEntitySensor<EagleEntity> {

    @Override
    public @NotNull Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.copyOf(Iterables.concat(super.requires(), List.of(MemoryModuleType.NEAREST_ATTACKABLE)));
    }

    @Override
    protected void doTick(@NotNull ServerLevel level, @NotNull EagleEntity eagle) {
        super.doTick(level, eagle);
        
        //Tame logic
        if(eagle.isTame())
            Primal_Util.Ai.setNearestAttackableOnSensor(eagle, target ->
            {
                if(eagle.isBaby())
                    return eagle.isEntityAttackable(eagle, target)
                            && eagle.getOwner()!=null
                            //To no set attackable underwater
                            && !eagle.isUnderWater()
                            //To defend owner or attack targets when following or continue attacking/defend itself
                            && (eagle.getBrain().isMemoryValue(MemoryModuleType.ATTACK_TARGET, target) || (eagle.getOwner().getLastHurtByMob()==target || (eagle.getOwner().getLastHurtMob()==target && eagle.isFollowing())))
                            //To not attack other owned eagle
                            && !(target instanceof TamableAnimal pet2 && pet2.getOwner()!=null && pet2.getOwner()==eagle.getOwner())
                            && eagle.canBabyAttack(target);
                else
                    return eagle.isEntityAttackable(eagle, target)
                            && eagle.getOwner()!=null
                            //To no set attackable underwater
                            && !eagle.isUnderWater()
                            //To defend owner or attack targets when following or continue attacking/defend itself
                            && (eagle.getBrain().isMemoryValue(MemoryModuleType.ATTACK_TARGET, target) || (eagle.getOwner().getLastHurtByMob()==target || (eagle.getOwner().getLastHurtMob()==target && eagle.isFollowing())))
                            //To not attack other owned eagle
                            && !(target instanceof TamableAnimal pet2 && pet2.getOwner()!=null && pet2.getOwner()==eagle.getOwner());
            });
        //Wild logic
        else
            Primal_Util.Ai.setNearestAttackableOnSensor(eagle, target ->
            {
                if(eagle.isBaby())
                    return eagle.isEntityAttackable(eagle, target) && eagle.canAttack(target) && eagle.canBabyAttack(target) && eagle.distanceToSqr(target)<9;
                else
                    return eagle.isEntityAttackable(eagle, target) && eagle.canAttack(target);
            });
    }

    protected int radiusXZ() {
        return 48;
    }

    protected int radiusY() {
        return 48;
    }
}
