package org.primal.entity.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.primal.entity.ai.behavior.generic.IdlePoseAnimationBehavior;
import org.primal.entity.ai.behavior.generic.TryFindWaterSurface;
import org.primal.entity.ai.behavior.walrus.WalrusPlayInstrument;
import org.primal.entity.ai.behavior.walrus.WalrusSlamAttack;
import org.primal.entity.ai.behavior.walrus.WalrusSwimWhirlwindAttack;
import org.primal.entity.animal.WalrusEntity;
import org.primal.registry.Primal_Activities;
import org.primal.registry.Primal_Entities;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.registry.Primal_Sensors;
import org.primal.util.Primal_Util;

import java.util.Optional;
import java.util.function.Predicate;

public class WalrusAi {

    //──────────────────────────────────── Init ────────────────────────────────────
    private static final ImmutableList<SensorType<? extends Sensor<? super WalrusEntity>>> SENSOR_TYPES = ImmutableList.of(
            Primal_Sensors.WALRUS_ENTITY_SENSOR.get(),
            SensorType.HURT_BY,
            Primal_Sensors.NEAREST_BABY.get(),
            SensorType.NEAREST_PLAYERS,
            SensorType.NEAREST_ADULT,
            Primal_Sensors.WALRUS_TEMPTATIONS_SENSOR.get(),
            Primal_Sensors.WALRUS_HOSTILE_SENSOR.get(),
            SensorType.NEAREST_ITEMS);

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
            MemoryModuleType.GAZE_COOLDOWN_TICKS,

            MemoryModuleType.HURT_BY,
            MemoryModuleType.HURT_BY_ENTITY,
            MemoryModuleType.BREED_TARGET,

            MemoryModuleType.IS_PANICKING,
            MemoryModuleType.TEMPTATION_COOLDOWN_TICKS,
            MemoryModuleType.IS_TEMPTED,
            MemoryModuleType.TEMPTING_PLAYER,

            MemoryModuleType.AVOID_TARGET,
            MemoryModuleType.NEAREST_VISIBLE_ADULT,

            MemoryModuleType.NEAREST_HOSTILE,

            Primal_MemoryModuleTypes.NEAREST_VISIBLE_BABY.get(),
            Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get(),

            Primal_MemoryModuleTypes.LAST_ATTACK_TARGET.get(),
            Primal_MemoryModuleTypes.LUNGE_COOLDOWN.get(),

            Primal_MemoryModuleTypes.WAS_IDLE_ANIMATION.get(),
            Primal_MemoryModuleTypes.HAS_INSTRUMENT.get()
    );

    public static Ingredient getTemptations() {
        return Ingredient.of(Items.COD_BUCKET);
    }

    public static void initMemories(WalrusEntity walrus, RandomSource random) {
    }

    public static Brain.Provider<WalrusEntity> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    public static Brain<?> makeBrain(Brain<WalrusEntity> brain) {
        initCoreActivity(brain);
        initIdleActivity(brain);
        initFightActivity(brain);
        initRetreatActivity(brain);
        initMountActivity(brain);
        initPlayActivity(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    //──────────────────────────────────── Activities ────────────────────────────────────
    private static void initMountActivity(Brain<WalrusEntity> brain) {
        brain.addActivity(
                Primal_Activities.JOCKEY.get(),
                10,
                ImmutableList.of()
        );
    }

    private static void initCoreActivity(Brain<WalrusEntity> brain) {
        brain.addActivity(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new LookAtTargetSink(45, 90),
                        new MoveToTargetSink(),
                        TryFindWaterSurface.create(32, 1, 0.10f),
                        EraseMemoryIf.create(walrus ->
                                        walrus.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isPresent()
                                                && !walrus.canAttack(walrus.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get()),
                                MemoryModuleType.ATTACK_TARGET)
                )
        );
    }

    private static final UniformInt ADULT_FOLLOW_RANGE = UniformInt.of(3, 16);
    private static void initIdleActivity(Brain<WalrusEntity> brain) {
        brain.addActivity(
                Activity.IDLE,
                10,
                ImmutableList.of(
                        CopyMemoryWithExpiry.create(
                                walrus -> walrus.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_HOSTILE),
                                MemoryModuleType.NEAREST_HOSTILE, MemoryModuleType.AVOID_TARGET, TimeUtil.rangeOfSeconds(5, 7)
                        ),
                        StartAttacking.create(WalrusAi::findNearestValidAttackTarget),
                        new AnimalMakeLove(Primal_Entities.WALRUS.get(), 1.0f),
                        new RunOne<>(
                                ImmutableList.of(
                                        Pair.of(new FollowTemptation(livingEntity -> 1.0F, livingEntity -> livingEntity.isBaby() ? 2.5 : 3.5), 4),
                                        Pair.of(BehaviorBuilder.triggerIf(Predicate.not(WalrusEntity::refuseToMove),
                                                BabyFollowAdult.create(ADULT_FOLLOW_RANGE, 1.0F)), 1)
                                )
                        ),

                        SetEntityLookTargetSometimes.create(6.0F, UniformInt.of(30, 60), target -> EntityType.PLAYER.equals(target.getType()) && !target.isPassenger()),

                        createIdleBehaviors()
                )
        );
    }

    private static RunOne<WalrusEntity> createIdleBehaviors() {
        return new RunOne<>(
                ImmutableList.of(
                        Pair.of(new GateBehavior<>(
                                        ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
                                        ImmutableSet.of(),
                                        GateBehavior.OrderPolicy.ORDERED,
                                        GateBehavior.RunningPolicy.TRY_ALL,
                                        ImmutableList.of(
                                                Pair.of(Primal_Util.Ai.swimButMobIsReallyFuckingFat(0.9f), 1),
                                                Pair.of(RandomStroll.stroll(0.9F, true), 1),
                                                Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), 1),
                                                Pair.of(BehaviorBuilder.triggerIf(Entity::isInWaterOrBubble), 10)
                                        )),
                                1),
                        Pair.of(IdlePoseAnimationBehavior.create(
                                        "lay",
                                        Pose.CROAKING, 100, 400,
                                        mob -> IdlePoseAnimationBehavior.basicCanStart(mob) && !mob.isInWater() && mob.onGround() && !mob.hasInstrument(),
                                        200),
                                1),
                        Pair.of(new DoNothing(40, 80),
                                1)
                )
        );
    }

    private static void initFightActivity(Brain<WalrusEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.FIGHT,
                10,
                ImmutableList.of(
                        SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F),
                        new WalrusSwimWhirlwindAttack(),
                        new WalrusSlamAttack(20),
                        SetEntityLookTarget.create(50),
                        StopAttackingIfTargetInvalid.create(),
                        EraseMemoryIf.create(Animal::isInLove, MemoryModuleType.ATTACK_TARGET)),
                MemoryModuleType.ATTACK_TARGET);
    }

    private static void initRetreatActivity(Brain<WalrusEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.AVOID,
                10,
                ImmutableList.of(
                        SetWalkTargetAwayFrom.entity(MemoryModuleType.AVOID_TARGET, 1.3F, 30, false)
                ),
                MemoryModuleType.AVOID_TARGET
        );
    }

    private static void initPlayActivity(Brain<WalrusEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Primal_Activities.PLAY.get(),
                10,
                ImmutableList.of(new WalrusPlayInstrument()),
                Primal_MemoryModuleTypes.HAS_INSTRUMENT.get()
        );
    }

    //──────────────────────────────────── Misc ────────────────────────────────────
    public static void updateActivity(WalrusEntity walrus) {
        //Is angry per 10s
        Brain<WalrusEntity> brain = walrus.getBrain();
        if(brain.getMemory(MemoryModuleType.ATTACK_TARGET).isPresent() && !brain.getMemory(MemoryModuleType.ATTACK_TARGET).get().getType().equals(Primal_Entities.WALRUS.get()))
            brain.setMemoryWithExpiry(Primal_MemoryModuleTypes.LAST_ATTACK_TARGET.get(), brain.getMemory(MemoryModuleType.ATTACK_TARGET).get(), 200);

        //Mount behavior
        if(walrus.hasControllingPassenger())
            brain.setActiveActivityToFirstValid(ImmutableList.of(Primal_Activities.JOCKEY.get()));
        //Normal behavior
        else {
            if(walrus.isBaby())
                brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.AVOID, Activity.IDLE));
            else
                brain.setActiveActivityToFirstValid(ImmutableList.of(Primal_Activities.PLAY.get(), Activity.FIGHT, Activity.AVOID, Activity.IDLE));
        }

        walrus.setAggressive(brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
    }

    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(WalrusEntity walrus) {
        return BehaviorUtils.isBreeding(walrus) || walrus.isBaby()? Optional.empty() : walrus.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE);
    }

    public static final ImmutableMap<EntityType<?>, Float> ACCEPTABLE_DISTANCE_FROM_HOSTILES = ImmutableMap.<EntityType<?>, Float>builder()
            .put(EntityType.POLAR_BEAR, 12.0F)
            .build();
}