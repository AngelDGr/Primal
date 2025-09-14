package org.primal.entity.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import org.primal.entity.ai.behavior.eagle.*;
import org.primal.entity.ai.behavior.generic.*;
import org.primal.entity.animal.EagleEntity;
import org.primal.registry.*;
import org.primal.util.MiscUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public class EagleAi {

    private static final ImmutableList<SensorType<? extends Sensor<? super EagleEntity>>> SENSOR_TYPES = ImmutableList.of(
            SensorType.NEAREST_LIVING_ENTITIES,
            SensorType.HURT_BY,
            Primal_Sensors.NEAREST_BABY.get(),
            SensorType.NEAREST_PLAYERS,
            SensorType.NEAREST_ADULT,
            Primal_Sensors.EAGLE_ATTACK_SENSOR.get(),
            Primal_Sensors.EAGLE_TEMPTATIONS_SENSOR.get(),
            Primal_Sensors.EAGLE_SCARE_SENSOR.get(),
            Primal_Sensors.EAGLE_HOSTILE_SENSOR.get(),
            Primal_Sensors.NEAREST_NEST.get());

    private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
            MemoryModuleType.NEAREST_LIVING_ENTITIES,
            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.WALK_TARGET,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.PATH,
            MemoryModuleType.ATTACK_TARGET,
            MemoryModuleType.ATTACK_COOLING_DOWN,
            MemoryModuleType.NEAREST_ATTACKABLE,
            MemoryModuleType.ROAR_TARGET,

            MemoryModuleType.HURT_BY,
            MemoryModuleType.HURT_BY_ENTITY,
            MemoryModuleType.BREED_TARGET,

            MemoryModuleType.IS_PANICKING,
            MemoryModuleType.TEMPTATION_COOLDOWN_TICKS,
            MemoryModuleType.IS_TEMPTED,
            MemoryModuleType.TEMPTING_PLAYER,

            MemoryModuleType.AVOID_TARGET,
            MemoryModuleType.HAS_HUNTING_COOLDOWN,

            MemoryModuleType.NEAREST_REPELLENT,
            MemoryModuleType.IS_PREGNANT,
            MemoryModuleType.PACIFIED,

            MemoryModuleType.ADMIRING_ITEM,

            MemoryModuleType.NEAREST_HOSTILE,
            Primal_MemoryModuleTypes.CHIRP_COOLDOWN.get(),

            Primal_MemoryModuleTypes.NEAREST_VISIBLE_BABY.get(),
            Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get(),
            Primal_MemoryModuleTypes.IS_STUNNED.get(),
            Primal_MemoryModuleTypes.IS_SNATCHING.get(),
            Primal_MemoryModuleTypes.AMOUNT_ATTACKED.get(),
            Primal_MemoryModuleTypes.NEAREST_SCARED.get(),

            MemoryModuleType.HOME,
            MemoryModuleType.RAM_COOLDOWN_TICKS,

            Primal_MemoryModuleTypes.ATTACKED_LIST.get(),
            Primal_MemoryModuleTypes.LAST_ATTACK_TARGET.get()
    );

    private static final UniformInt ADULT_FOLLOW_RANGE = UniformInt.of(5, 16);

    public static Predicate<ItemStack> getTemptations() {
        return EagleEntity::isMatingFood;
    }

    public static void initMemories(EagleEntity eagle, RandomSource random) {
    }

    public static Brain.Provider<EagleEntity> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    public static Brain<?> makeBrain(Brain<EagleEntity> brain) {
        initCoreActivity(brain);
        initIdleActivity(brain);
        initFightActivity(brain);
        initSnatchActivity(brain);
        initLayEggActivity(brain);
        initRetreatActivity(brain);
        initNestedActivity(brain);
        initFollowActivity(brain);
        initSitActivity(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    private static void initCoreActivity(Brain<EagleEntity> brain) {
        brain.addActivity(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new ConditionalSwim(0.8F, eagle-> eagle.isBaby() || eagle.getBrain().isActive(Primal_Activities.SIT.get())),
                        new LookAtTargetSink(45, 90),
                        new MoveToTargetSink()
                )
        );
    }

    private static void initIdleActivity(Brain<EagleEntity> brain) {
        brain.addActivity(
                Activity.IDLE,
                10,
                ImmutableList.of(
                        StartAttacking.create(EagleAi::findNearestValidAttackTarget),
                        new EagleRemoveHome(),
                        new EagleSearchHome(),
                        new BirdReturnHome(1600, 25, true),
                        new AnimalMakeLove(Primal_Entities.EAGLE.get()),
                        SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60)),
                        new FollowTemptation(livingEntity -> 1.0F, livingEntity -> livingEntity.isBaby() ? 2.5 : 3.5),
                        new AnimalWanderFromScared(5, 8, 5, 10),
                        createIdleMovementBehaviors()
                )
        );
    }

    private static void initFightActivity(Brain<EagleEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.FIGHT,
                10,
                ImmutableList.of(
                        SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F),
                        SetEntityLookTarget.create(50),
                        new EagleStartAttack(10),
                        StopAttackingIfTargetInvalid.create(),
                        EraseMemoryIf.create(eagle -> {
                            Optional<List<UUID>> attackedList = eagle.getBrain().getMemory(Primal_MemoryModuleTypes.ATTACKED_LIST.get());

                            Optional<UUID> lastUuid = attackedList.map(List::getLast);

                            Optional<LivingEntity> lastEntity =
                                    lastUuid.isPresent() && !eagle.level().isClientSide && ((ServerLevel)eagle.level()).getEntity(lastUuid.get()) instanceof LivingEntity living ?
                                            Optional.of(living):
                                            Optional.empty();

                            if(lastEntity.isPresent() && !eagle.getBrain().isMemoryValue(MemoryModuleType.ATTACK_TARGET, lastEntity.get())){
                                return true;
                            }

                            return eagle.isInLove() || !eagle.getPassengers().isEmpty();
                        }, MemoryModuleType.ATTACK_TARGET)),
                MemoryModuleType.ATTACK_TARGET);
    }

    private static void initSnatchActivity(Brain<EagleEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Primal_Activities.SNATCH.get(),
                10,
                ImmutableList.of(new EagleSnatch(60)),
                Primal_MemoryModuleTypes.IS_SNATCHING.get());
    }

    private static void initFollowActivity(Brain<EagleEntity> brain) {
        brain.addActivity(
                Primal_Activities.FOLLOW.get(),
                10,
                ImmutableList.of(
                        StartAttacking.create(EagleAi::findNearestValidAttackTarget),
                        new EagleRemoveHome(),
                        new EagleSearchHome(),
                        new EagleDetectHostile(),
                        new AnimalMakeLove(Primal_Entities.EAGLE.get()),
                        new FollowOwner(),
                        new FollowTemptation(livingEntity -> 1.0F, livingEntity -> livingEntity.isBaby() ? 2.5 : 3.5),
                        new RandomLookAround(UniformInt.of(150, 250), 30.0F, 0.0F, 0.0F),

                        createIdleMovementBehaviors()
                )
        );
    }

    private static void initSitActivity(Brain<EagleEntity> brain) {
        brain.addActivity(
                Primal_Activities.SIT.get(),
                ImmutableList.of(
                        Pair.of(0, new AnimalSitting())
                )
        );
    }

    private static final UniformInt RETREAT_DURATION = TimeUtil.rangeOfSeconds(5, 20);
    private static void initRetreatActivity(Brain<EagleEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.AVOID,
                10,
                ImmutableList.of(
                        new BirdAvoidWhileAscending(),
                        SetWalkTargetAwayFrom.entity(MemoryModuleType.AVOID_TARGET, 1.0F, 20, false)
                ),
                MemoryModuleType.AVOID_TARGET
        );
    }

    private static void initNestedActivity(Brain<EagleEntity> brain) {
        brain.addActivity(
                Primal_Activities.NESTED.get(),
                10,
                ImmutableList.of(
                        new EagleRemoveHome(),
                        new EagleSearchHome(),
                        SetEntityLookTargetSometimes.create(EntityType.PLAYER, 3.0F, UniformInt.of(30, 60)),
                        SetEntityLookTargetSometimes.create(Primal_Entities.EAGLE.get(), 16.0F, UniformInt.of(30, 60)),
                        new RandomLookAround(UniformInt.of(150, 250), 30.0F, 0.0F, 0.0F),
                        new BirdReturnHome(1, 1, false),
                        createIdleMovementBehaviors()
                )
        );
    }

    public static void wasHurtBy(EagleEntity eagle, LivingEntity target) {
        Brain<EagleEntity> brain = eagle.getBrain();
        brain.eraseMemory(MemoryModuleType.PACIFIED);
        brain.eraseMemory(MemoryModuleType.BREED_TARGET);
        if (eagle.isBaby()) {
            retreatFromNearestTarget(eagle, target);
            for(EagleEntity nearEagle: getNearestAdultEagles(eagle)){
                nearEagle.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, target);
            }

        } else {
            eagle.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, target);
        }
    }

    private static List<EagleEntity> getNearestAdultEagles(EagleEntity eagle) {
        return eagle.level().getEntitiesOfClass(EagleEntity.class, eagle.getBoundingBox().inflate(30,5,30))
                .stream().filter(crocodile1 -> !crocodile1.isBaby()).toList();
    }

    private static void retreatFromNearestTarget(EagleEntity eagle, LivingEntity target) {
        Brain<EagleEntity> brain = eagle.getBrain();
        LivingEntity avoidTarget = BehaviorUtils.getNearestTarget(eagle, brain.getMemory(MemoryModuleType.AVOID_TARGET), target);
        avoidTarget = BehaviorUtils.getNearestTarget(eagle, brain.getMemory(MemoryModuleType.ATTACK_TARGET), avoidTarget);
        setAvoidTarget(eagle, avoidTarget);
    }

    private static void setAvoidTarget(EagleEntity eagle, LivingEntity target) {
        eagle.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
        eagle.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        eagle.getBrain().setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, target, RETREAT_DURATION.sample(eagle.level().random));
    }

    private static void initLayEggActivity(Brain<EagleEntity> brain) {
        brain.addActivityWithConditions(
                Activity.LAY_SPAWN,
                ImmutableList.of(
                        Pair.of(0, new AnimalGoesToNest()),
                        Pair.of(1, TryLayEggOnLandOrNest.create(Primal_Blocks.EAGLE_EGG.get(), MiscUtil.EGGS_2, 2, 1)),
                        Pair.of(2, TryFindLand.create(16, 1)),
                        Pair.of(
                                3,
                                new RunOne<>(
                                        ImmutableList.of(
                                                Pair.of(RandomStroll.stroll(1.0F), 2),
                                                Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), 1),
                                                Pair.of(BehaviorBuilder.triggerIf(Entity::onGround), 1)
                                        )
                                )
                        )
                ),
                ImmutableSet.of(
                        Pair.of(MemoryModuleType.IS_PREGNANT, MemoryStatus.VALUE_PRESENT)
                )
        );
    }

    private static RunOne<EagleEntity> createIdleMovementBehaviors() {
        return new RunOne<>(
                ImmutableList.of(
                        Pair.of(BehaviorBuilder.triggerIf(Predicate.not(eagle -> eagle.isAggressive() || (eagle.isBaby() && eagle.getBrain().hasMemoryValue(MemoryModuleType.HOME))), RandomStroll.stroll(1f)), 1),
                        Pair.of(new DoNothing(30, 60), 1)));
    }

    public static void updateActivity(EagleEntity eagle) {
        Brain<EagleEntity> brain = eagle.getBrain();

        if(eagle.isSitting()){
            eagle.getBrain().setActiveActivityIfPossible(Primal_Activities.SIT.get());
        }
        else if(eagle.isFollowing()){
            eagle.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.AVOID, Primal_Activities.SNATCH.get(), Activity.FIGHT, Activity.LAY_SPAWN, Primal_Activities.FOLLOW.get()));
        } else {
            if(eagle.isBaby())
                brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.AVOID, Primal_Activities.NESTED.get()));
            else
                brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.AVOID, Primal_Activities.SNATCH.get(), Activity.FIGHT, Activity.LAY_SPAWN, Activity.IDLE));
        }

        eagle.setAggressive(brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET) || brain.hasMemoryValue(Primal_MemoryModuleTypes.IS_SNATCHING.get()));

        handleEagleQueue(brain, eagle);

        //This put the value
        if (brain.getMemory(MemoryModuleType.ATTACK_TARGET).isPresent()) {
            brain.getMemory(MemoryModuleType.ATTACK_TARGET).get().primal$setEagleAttacking(eagle.getUUID());
            brain.setMemoryWithExpiry(Primal_MemoryModuleTypes.LAST_ATTACK_TARGET.get(), brain.getMemory(MemoryModuleType.ATTACK_TARGET).get(), 80L);
        }

        //This should remove the value
        if(brain.getMemory(Primal_MemoryModuleTypes.LAST_ATTACK_TARGET.get()).isPresent()
                && brain.getMemory(MemoryModuleType.ATTACK_TARGET).isPresent()
                //If is not the same, it means that changed target
                && !brain.isMemoryValue(Primal_MemoryModuleTypes.LAST_ATTACK_TARGET.get(), brain.getMemory(MemoryModuleType.ATTACK_TARGET).get())
                && MiscUtil.isSameEagleAttacking(brain.getMemory(Primal_MemoryModuleTypes.LAST_ATTACK_TARGET.get()).get(), eagle)){

            brain.getMemory(Primal_MemoryModuleTypes.LAST_ATTACK_TARGET.get()).get().primal$setEagleAttacking(null);
        }
    }

    private static void handleEagleQueue(Brain<EagleEntity> brain, EagleEntity eagle){
        //This adds to the queue
        if(eagle.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_ATTACKABLE)){

            List<UUID> attackedList= brain.hasMemoryValue(Primal_MemoryModuleTypes.ATTACKED_LIST.get())? brain.getMemory(Primal_MemoryModuleTypes.ATTACKED_LIST.get()).get() : Lists.newArrayList();

            //This put the nearest attackable in the last place
            if(!attackedList.contains(brain.getMemory(MemoryModuleType.NEAREST_ATTACKABLE).get().getUUID())){
                attackedList.addFirst(brain.getMemory(MemoryModuleType.NEAREST_ATTACKABLE).get().getUUID());
            }

            //This ensures that the list never gets too long, if it goes for more than 2, it removes the last one
            if(attackedList.size()>2){
                attackedList.removeLast();
            }

            brain.setMemory(Primal_MemoryModuleTypes.ATTACKED_LIST.get(), attackedList);
        }

        //This remove from the queue
        if(brain.hasMemoryValue(Primal_MemoryModuleTypes.ATTACKED_LIST.get())){
            List<UUID> attackedList = new ArrayList<>(
                    brain.getMemory(Primal_MemoryModuleTypes.ATTACKED_LIST.get()).get()
            );

            attackedList.removeIf(uuid -> (((ServerLevel) eagle.level()).getEntity(uuid) instanceof LivingEntity target && target.isDeadOrDying()) || ((ServerLevel) eagle.level()).getEntity(uuid) == null);

            if(attackedList.isEmpty()){
                brain.eraseMemory(Primal_MemoryModuleTypes.ATTACKED_LIST.get());
            } else {
                brain.setMemory(Primal_MemoryModuleTypes.ATTACKED_LIST.get(), attackedList);
            }
        }
    }

    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(EagleEntity eagle) {

        if(BehaviorUtils.isBreeding(eagle) || eagle.isBaby() || eagle.level().isClientSide){
            return Optional.empty();
        }

        if(eagle.getBrain().hasMemoryValue(Primal_MemoryModuleTypes.ATTACKED_LIST.get())){
            List<UUID> attackedList = eagle.getBrain().getMemory(Primal_MemoryModuleTypes.ATTACKED_LIST.get()).get();

            if(((ServerLevel) eagle.level()).getEntity(attackedList.getLast()) instanceof LivingEntity target){
                return Optional.of(target);
            }
        }

        return Optional.empty();
    }
}
