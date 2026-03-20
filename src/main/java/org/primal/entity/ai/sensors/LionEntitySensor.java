package org.primal.entity.ai.sensors;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.ai.sensors.generic.AnimalStalkEntitySensor;
import org.primal.entity.ai.sensors.generic.PackAnimalSensor;
import org.primal.entity.animal.LionEntity;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.util.Primal_Util;

import java.util.*;

public final class LionEntitySensor extends AnimalStalkEntitySensor<LionEntity> {

    public LionEntitySensor(){
        super(16, lion ->
                (lion.isManeless() || Primal_Util.Ai.getNearestMobs(lion, LionEntity.class, LionEntity::isManeless).isEmpty())
                        //Only if it doesn't have home or is close enough to home
                && (lion.getBrain().getMemory(MemoryModuleType.HOME).isEmpty() || lion.getOnPos().distSqr(lion.getBrain().getMemory(MemoryModuleType.HOME).get().pos()) < 48*48)
                && !lion.isBaby()
                && lion.isWandering(),
                Primal_Util.toTicks(120));
    }

    @Override
    public @NotNull Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.copyOf(Iterables.concat(super.requires(), List.of(MemoryModuleType.NEAREST_ATTACKABLE)));
    }

    @Override
    protected void doTick(@NotNull ServerLevel level, @NotNull LionEntity lion) {
        super.doTick(level, lion);

        if(lion.isTame())
            Primal_Util.Ai.setNearestAttackableOnSensor(lion, target ->
            {
                if(lion.isBaby())
                    return lion.getOwner()!=null
                            && lion.isEntityAttackable(lion, target)
                            //To defend owner or attack targets when following or continue attacking/defend itself
                            && (lion.getBrain().isMemoryValue(MemoryModuleType.ATTACK_TARGET, target) || (lion.getOwner().getLastHurtByMob()==target || (lion.getOwner().getLastHurtMob()==target && lion.isFollowing())))
                            //To not attack other owned variantFromBiome
                            && !(target instanceof TamableAnimal pet2 && pet2.getOwner()!=null && pet2.getOwner()==lion.getOwner())
                            && lion.canBabyAttack(target);
                else {
                    return
                            lion.getOwner()!=null
                                    && lion.isEntityAttackable(lion, target)
                                    //To defend owner or attack targets when following or continue attacking/defend itself
                                    && (lion.getBrain().isMemoryValue(MemoryModuleType.ATTACK_TARGET, target) || (lion.getOwner().getLastHurtByMob()==target || (lion.getOwner().getLastHurtMob()==target && lion.isFollowing())))
                                    //To not attack other owned variantFromBiome
                                    && !(target instanceof TamableAnimal pet2 && pet2.getOwner()!=null && pet2.getOwner()==lion.getOwner());
                }
            });
        //Wild logic
        else
            Primal_Util.Ai.setNearestAttackableOnSensor(lion);

        //Pack animal
        PackAnimalSensor.setLeaderMemory(lion);
        PackAnimalSensor.setMemoryNearestPackMemberForPackAnimal(lion);

        nearbyMobCautiousDetection(lion);
    }

    public static void nearbyMobCautiousDetection(LionEntity lion){
        long gameTime = lion.level().getGameTime();
        long requiredTimeNormal = 100; // ticks before aggression at normal (5 seconds)
        long requiredTimeSneaking = 200; // ticks before aggression at sneaking (10 seconds)
        long requiredTimeHoldingMeat = 400; // ticks before aggression while holding meat (20 seconds)

        Brain<?> brain = lion.getBrain();

        //Get or create cautious map
        Map<String, Long> cautiousMap =  new HashMap<>(brain.getMemory(Primal_MemoryModuleTypes.CAUTIOUS_LIST.get())
                .orElseGet(() -> {
                    Map<String, Long> map = new HashMap<>();
                    brain.setMemory(Primal_MemoryModuleTypes.CAUTIOUS_LIST.get(), map);
                    return map;
                }));

        // Get nearby entities
        Optional<NearestVisibleLivingEntities> nearby =
                brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);

        //Filters the entities to avoid iterating with unnecessary ones
        Iterable<LivingEntity> cautious =
                nearby.map(n -> n.findAll(lion::canBeCautious))
                        .orElse(List.of());

        // Track currently valid entities (to clean up later)
        Set<String> stillClose = new HashSet<>();

        for (LivingEntity target : cautious) {
            UUID id = target.getUUID();
            stillClose.add(id.toString());
            // First time seen
            cautiousMap.putIfAbsent(id.toString(), gameTime);

            long firstSeen = cautiousMap.get(id.toString());
            long elapsed = gameTime - firstSeen;

            if(elapsed >= (target.getMainHandItem().is(ItemTags.MEAT)? requiredTimeHoldingMeat: target.isCrouching() ? requiredTimeSneaking: requiredTimeNormal)){
                brain.setMemory(Primal_MemoryModuleTypes.NEAREST_ATTACKABLE_CAUTIOUS.get(), target);
            }
        }

        // Remove entities that are no longer close
        cautiousMap.keySet().removeIf(uuid -> !stillClose.contains(uuid));

        //Sets memory
        brain.setMemory(Primal_MemoryModuleTypes.CAUTIOUS_LIST.get(), cautiousMap);

        //Removes memory if empty
        if(cautiousMap.isEmpty() && brain.getMemory(Primal_MemoryModuleTypes.CAUTIOUS_LIST.get()).isPresent())
            brain.eraseMemory(Primal_MemoryModuleTypes.CAUTIOUS_LIST.get());

        //Removes the memory
        if(brain.getMemory(Primal_MemoryModuleTypes.NEAREST_ATTACKABLE_CAUTIOUS.get()).isPresent()){
            if(!lion.canBeCautious(brain.getMemory(Primal_MemoryModuleTypes.NEAREST_ATTACKABLE_CAUTIOUS.get()).get())){
                brain.eraseMemory(Primal_MemoryModuleTypes.NEAREST_ATTACKABLE_CAUTIOUS.get());
            }
        }
    }

    @Override
    protected int radiusXZ() {
        return 36;
    }
}
