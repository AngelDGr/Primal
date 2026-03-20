package org.primal.entity.ai.sensors.generic;

import com.google.common.collect.ImmutableSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.NearestLivingEntitySensor;
import org.jetbrains.annotations.NotNull;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.util.Primal_Util;
import org.primal.util.mob_types.IsPackAnimal;

import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;

public class PackAnimalSensor<T extends LivingEntity> extends NearestLivingEntitySensor<T> {
    private final boolean canHaveLeader;
    protected PackAnimalSensor(boolean canHaveLeader) {
        this.canHaveLeader = canHaveLeader;
    }

    @Override
    public @NotNull Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
    }

    @Override
    protected void doTick(@NotNull ServerLevel level, @NotNull T entity) {
        super.doTick(level, entity);
        if(canHaveLeader) setLeaderMemory(entity);

        //Pack animal
        setMemoryNearestPackMemberForPackAnimal(entity);
    }

    public static boolean isLeaderTargetable(LivingEntity livingEntity, LivingEntity target) {
        return livingEntity.getBrain().isMemoryValue(Primal_MemoryModuleTypes.NEAREST_LEADER.get(), target)
                ? Primal_Util.Ai.getTargetConditions(36, true).test(livingEntity, target)
                : Primal_Util.Ai.getTargetConditions(36, false).test(livingEntity, target);
    }

    public static<T extends LivingEntity> void setLeaderMemory(T entity){
        entity.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).stream().flatMap(Collection::stream)
                .filter(
                        target ->
                                isLeaderTargetable(entity, target)
                                        && (target.getType() == entity.getType()) && target instanceof IsPackAnimal<?> packAnimal && packAnimal.isLeader()
                )
                .findFirst()
                .ifPresentOrElse(ent -> entity.getBrain().setMemory(Primal_MemoryModuleTypes.NEAREST_LEADER.get(), ent),
                        () -> entity.getBrain().eraseMemory(Primal_MemoryModuleTypes.NEAREST_LEADER.get()));
    }

    public static void setMemoryNearestPackMemberForPackAnimal(LivingEntity mob, Predicate<LivingEntity> filter){
        Primal_Util.Ai.setMemoryFromVisibleEntity(mob, filter, Primal_MemoryModuleTypes.NEAREST_PACK_MEMBER.get());
    }

    public static void setMemoryNearestPackMemberForPackAnimal(LivingEntity mob){
        //Detects other mobs. Adult only detect other adults, babies detect adults and babies (so alone babies remain close to each other)
        setMemoryNearestPackMemberForPackAnimal(mob, e-> mob.getType().equals(e.getType()) && (!e.isBaby() || mob.isBaby()));
    }
}
