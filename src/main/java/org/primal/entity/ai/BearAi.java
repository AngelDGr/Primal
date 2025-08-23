package org.primal.entity.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import net.minecraft.core.BlockPos;
import net.minecraft.util.TimeUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.item.ItemStack;
import org.primal.entity.ai.behavior.bear.*;
import org.primal.entity.ai.behavior.generic.FollowOwner;
import org.primal.registry.Primal_Activities;
import org.primal.registry.Primal_Entities;
import org.primal.registry.Primal_Sensors;
import org.primal.entity.animal.BearEntity;

import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;

public final class BearAi {
    private static final ImmutableList<SensorType<? extends Sensor<? super BearEntity>>> SENSOR_TYPES = ImmutableList.of(
            SensorType.NEAREST_LIVING_ENTITIES,
            SensorType.HURT_BY,
            Primal_Sensors.NEAREST_ADULT_BEAR.get(),
            Primal_Sensors.NEAREST_BABY.get(),
            SensorType.NEAREST_PLAYERS,
            Primal_Sensors.BEAR_ATTACK_SENSOR.get(),
            Primal_Sensors.BEAR_NEAREST_BEEHIVE_SENSOR.get(),
            Primal_Sensors.BEAR_NEAREST_SWEET_BERRY_BUSH_SENSOR.get(),
            Primal_Sensors.BEAR_TEMPTATIONS_SENSOR.get(),
            Primal_Sensors.BEAR_REPELLENT_SENSOR.get());

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

            MemoryModuleType.NEAREST_REPELLENT
    );

    private static final UniformInt ADULT_FOLLOW_RANGE = UniformInt.of(5, 16);

    public static Predicate<ItemStack> getTemptations() {
        return BearEntity::isMatingFood;
    }

    private static void initMemories(BearEntity BearEntity, RandomSource random) {
    }

    public static Brain.Provider<BearEntity> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    public static Brain<?> makeBrain(Brain<BearEntity> brain) {
        initCoreActivity(brain);
        initIdleActivity(brain);
        initRoarActivity(brain);
        initFightActivity(brain);
        initRetreatActivity(brain);
        initFollowActivity(brain);
        initSitActivity(brain);
        initJockeyActivity(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    private static void initCoreActivity(Brain<BearEntity> brain) {
        brain.addActivity(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new Swim(0.8F),
                        SetBearLookRoarTarget.create(),
                        new LookAtTargetSink(45, 90),
                        new MoveToTargetSink()));
    }

    private static void initRoarActivity(Brain<BearEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(Activity.ROAR, 10, ImmutableList.of(new BearRoar()), MemoryModuleType.ROAR_TARGET);
    }

    private static void initIdleActivity(Brain<BearEntity> brain) {
        brain.addActivity(
                Activity.IDLE,
                10,
                ImmutableList.of(
                        new BearDefeated(),

                        BecomePassiveIfMemoryPresent.create(MemoryModuleType.NEAREST_REPELLENT, 200),

                        SetWalkTargetAwayFrom.pos(MemoryModuleType.NEAREST_REPELLENT, 1.5F, 8, true),

                        new BearSleep(),

                        new AnimalMakeLove(Primal_Entities.BEAR.get()),
                        new AnimalMakeLove(EntityType.POLAR_BEAR),

                        new Beg(),

                        SetBearRoarTarget.create(),

                        new BearRaidBeehive(),

                        new BearRaidSweetBerryBush(),

                        new RunOne<>(
                                ImmutableList.of(
                                        Pair.of(new FollowTemptation(livingEntity -> 1.0F, livingEntity -> livingEntity.isBaby() ? 2.5 : 3.5), 4),
                                        Pair.of(BehaviorBuilder.triggerIf(Predicate.not(BearEntity::refuseToMove),
                                                BabyFollowAdult.create(ADULT_FOLLOW_RANGE, 1.0F)), 1)
                                )
                        ),

                        SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60)),

                        new RandomLookAround(UniformInt.of(150, 250), 30.0F, 0.0F, 0.0F),

                        createIdleMovementBehaviors()
                )
        );
    }

    private static void initFollowActivity(Brain<BearEntity> brain) {
        brain.addActivity(
                Primal_Activities.FOLLOW.get(),
                10,
                ImmutableList.of(
                        new BearDefeated(),

                        new AnimalMakeLove(Primal_Entities.BEAR.get()),
                        new AnimalMakeLove(EntityType.POLAR_BEAR),

                        new Beg(),

                        SetBearRoarTarget.create(),

                        new FollowOwner(pet -> pet instanceof BearEntity bear && !bear.bearCollapses(),
                                4
                        ),

                        SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60)),

                        new RandomLookAround(UniformInt.of(150, 250), 30.0F, 0.0F, 0.0F),

                        createIdleMovementBehaviors()
                )
        );
    }

    private static void initSitActivity(Brain<BearEntity> brain) {
        brain.addActivity(
                Primal_Activities.SIT.get(),
                ImmutableList.of(
                        Pair.of(0, new BearSitting())
                )
        );
    }

    private static void initFightActivity(Brain<BearEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.FIGHT,
                10,
                ImmutableList.of(
                        BecomePassiveIfMemoryPresent.create(MemoryModuleType.NEAREST_REPELLENT, 200),
                        SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F),
                        MeleeAttack.create(20),
                        SetEntityLookTarget.create(50),
                        StopAttackingIfTargetInvalid.create(),
                        EraseMemoryIf.create(bear-> bear.isInLove() || bear.bearCollapses() , MemoryModuleType.ATTACK_TARGET)),
                MemoryModuleType.ATTACK_TARGET);
    }

    private static void initJockeyActivity(Brain<BearEntity> brain) {
        brain.addActivity(
                Primal_Activities.JOCKEY.get(),
                10,
                ImmutableList.of(
                        new BearDefeated(),
                        new RandomLookAround(UniformInt.of(150, 250), 30.0F, 0.0F, 0.0F)
                )
        );
    }

    private static void initRetreatActivity(Brain<BearEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.AVOID,
                10,
                ImmutableList.of(
                        SetWalkTargetAwayFrom.entity(MemoryModuleType.AVOID_TARGET, 1.2F, 20, false),
                        createIdleMovementBehaviors(),
                        SetEntityLookTargetSometimes.create(8.0F, UniformInt.of(30, 60)),
                        EraseMemoryIf.create(bear -> !bear.isBaby(), MemoryModuleType.AVOID_TARGET)
                ),
                MemoryModuleType.AVOID_TARGET
        );
    }

    public static void wasHurtBy(BearEntity bear, LivingEntity target) {
        Brain<BearEntity> brain = bear.getBrain();
        brain.eraseMemory(MemoryModuleType.BREED_TARGET);
        if (bear.isBaby()) {
            retreatFromNearestTarget(bear, target);
            for(BearEntity nearBear: getNearestAdultBears(bear)){
                nearBear.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, target);
            }
        } else {
            bear.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, target);
        }
    }

    private static final UniformInt RETREAT_DURATION = TimeUtil.rangeOfSeconds(5, 20);
    private static void retreatFromNearestTarget(BearEntity bear, LivingEntity target) {
        Brain<BearEntity> brain = bear.getBrain();
        LivingEntity avoidTarget = BehaviorUtils.getNearestTarget(bear, brain.getMemory(MemoryModuleType.AVOID_TARGET), target);
        avoidTarget = BehaviorUtils.getNearestTarget(bear, brain.getMemory(MemoryModuleType.ATTACK_TARGET), avoidTarget);
        setAvoidTarget(bear, avoidTarget);
    }

    private static void setAvoidTarget(BearEntity bear, LivingEntity target) {
        bear.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
        bear.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        bear.getBrain().setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, target, RETREAT_DURATION.sample(bear.level().random));
    }

    private static List<BearEntity> getNearestAdultBears(BearEntity bear) {
        return bear.level().getEntitiesOfClass(BearEntity.class, bear.getBoundingBox().inflate(20,5,20)).stream().filter(bear1 -> !bear1.isTame() && !bear1.isBaby()).toList();
    }

    private static RunOne<BearEntity> createIdleMovementBehaviors() {
        return new RunOne<>(
                ImmutableList.of(
                        Pair.of(BehaviorBuilder.triggerIf(Predicate.not(BearEntity::refuseToMove), RandomStroll.stroll(1f)), 1),
                        Pair.of(new DoNothing(30, 60), 1)));
    }

    public static void updateActivity(BearEntity bear) {

        if(bear.isSitting()){
            bear.getBrain().setActiveActivityIfPossible(Primal_Activities.SIT.get());
        }
        else if(bear.isFollowing()){
            bear.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.ROAR, Activity.FIGHT, Primal_Activities.FOLLOW.get()));
            bear.setAggressive(bear.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
        }
        else if(bear.isBaby()){
            if(bear.isBearJockey())
                bear.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Primal_Activities.JOCKEY.get()));
            else
                bear.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.AVOID, Activity.IDLE));
        }
        else {
            bear.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.ROAR, Activity.FIGHT, Activity.IDLE));
            bear.setAggressive(bear.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
        }
    }

    public static boolean isPosNearNearestRepellent(BearEntity bear, BlockPos pos) {
        Optional<BlockPos> optional = bear.getBrain().getMemory(MemoryModuleType.NEAREST_REPELLENT);
        return optional.isPresent() && optional.get().closerThan(pos, 8.0);
    }
}
