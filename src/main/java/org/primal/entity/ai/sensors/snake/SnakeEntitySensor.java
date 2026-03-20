package org.primal.entity.ai.sensors.snake;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.ai.sensors.generic.AnimalStalkEntitySensor;
import org.primal.entity.animal.SnakeEntity;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.util.Primal_Util;

import java.util.*;

public final class SnakeEntitySensor extends AnimalStalkEntitySensor<SnakeEntity> {

    public SnakeEntitySensor() {
        super(8, s->true, 600);
    }

    @Override
    public @NotNull Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.copyOf(Iterables.concat(super.requires(), List.of(MemoryModuleType.NEAREST_ATTACKABLE)));
    }

    @Override
    protected void doTick(@NotNull ServerLevel level, @NotNull SnakeEntity snake) {
        super.doTick(level, snake);

        if(snake.isTame())
            Primal_Util.Ai.setNearestAttackableOnSensor(snake,
                    target ->
                            snake.getOwner()!=null
                                    && snake.isEntityAttackable(snake, target)
                                    //To defend owner or attack targets when following or continue attacking/defend itself
                                    && (snake.getBrain().isMemoryValue(MemoryModuleType.ATTACK_TARGET, target) || (snake.getOwner().getLastHurtByMob()==target || (snake.getOwner().getLastHurtMob()==target && snake.isFollowing())))
                                    //To not attack other owned variantFromBiome
                                    && !(target instanceof TamableAnimal pet2 && pet2.getOwner()!=null && pet2.getOwner()==snake.getOwner())
                                    //Snake babies don't attack
                                    && !snake.isBaby()
            );
        else
            Primal_Util.Ai.setNearestAttackableOnSensor(snake);

        nearbyMobCautiousDetection(snake);
    }

    public static boolean isEntityAttackable(LivingEntity attacker, @NotNull LivingEntity target) {
        return attacker.getBrain().isMemoryValue(MemoryModuleType.ATTACK_TARGET, target)
                ? Primal_Util.Ai.getTargetConditions(6, true).test(attacker, target)
                : Primal_Util.Ai.getTargetConditions(6, false).test(attacker, target);
    }

    @Override
    protected int radiusXZ() {
        return 36;
    }

    public static void nearbyMobCautiousDetection(SnakeEntity snake){
        long gameTime = snake.level().getGameTime();
        long requiredTimeNormal = 100; // ticks before aggression at normal (5 seconds)
        long requiredTimeSneaking = 200; // ticks before aggression at sneaking (10 seconds)

        Brain<?> brain = snake.getBrain();

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
                nearby.map(n -> n.findAll(snake::canBeCautious))
                        .orElse(List.of());

        // Track currently valid entities (to clean up later)
        Set<String> stillClose = new HashSet<>();

        //Puts the nearest cautious
        if (cautious.iterator().hasNext()) {
            brain.setMemory(Primal_MemoryModuleTypes.NEAREST_CAUTIOUS.get(), cautious.iterator().next());
        }

        for (LivingEntity target : cautious) {

            UUID id = target.getUUID();
            stillClose.add(id.toString());
            // First time seen
            cautiousMap.putIfAbsent(id.toString(), gameTime);

            long firstSeen = cautiousMap.get(id.toString());
            long elapsed = gameTime - firstSeen;

            if(elapsed >= (target.isCrouching() ? requiredTimeSneaking: requiredTimeNormal)){
                brain.setMemory(Primal_MemoryModuleTypes.NEAREST_ATTACKABLE_CAUTIOUS.get(), target);
            }
        }

        // Remove entities that are no longer close
        cautiousMap.keySet().removeIf(uuid -> !stillClose.contains(uuid));

        //Sets memory
        brain.setMemory(Primal_MemoryModuleTypes.CAUTIOUS_LIST.get(), cautiousMap);

        //Removes memory if empty
        if((cautiousMap.isEmpty() || brain.getMemory(MemoryModuleType.DANCING).isPresent()) && brain.getMemory(Primal_MemoryModuleTypes.CAUTIOUS_LIST.get()).isPresent()){
            brain.eraseMemory(Primal_MemoryModuleTypes.NEAREST_CAUTIOUS.get());
            brain.eraseMemory(Primal_MemoryModuleTypes.CAUTIOUS_LIST.get());
        }

        //Removes the memory
        if(brain.getMemory(Primal_MemoryModuleTypes.NEAREST_ATTACKABLE_CAUTIOUS.get()).isPresent()){
            if(!snake.canBeCautious(brain.getMemory(Primal_MemoryModuleTypes.NEAREST_ATTACKABLE_CAUTIOUS.get()).get())){
                brain.eraseMemory(Primal_MemoryModuleTypes.NEAREST_ATTACKABLE_CAUTIOUS.get());
            }
        }
    }
}
