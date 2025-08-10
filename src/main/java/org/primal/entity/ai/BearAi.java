package org.primal.entity.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import org.primal.entity.ModEntities;
import org.primal.entity.ai.behavior.Beg;
import org.primal.entity.ai.behavior.bear.BearRaidBeehive;
import org.primal.entity.ai.behavior.bear.BearSleep;
import org.primal.entity.ai.behavior.bear.BearStartAttacking;
import org.primal.entity.ai.sensors.ModSensors;
import org.primal.entity.animal.Bear;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.AnimalMakeLove;
import net.minecraft.world.entity.ai.behavior.AnimalPanic;
import net.minecraft.world.entity.ai.behavior.BabyFollowAdult;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
import net.minecraft.world.entity.ai.behavior.DoNothing;
import net.minecraft.world.entity.ai.behavior.EraseMemoryIf;
import net.minecraft.world.entity.ai.behavior.FollowTemptation;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.ai.behavior.MeleeAttack;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.ai.behavior.RandomLookAround;
import net.minecraft.world.entity.ai.behavior.RandomStroll;
import net.minecraft.world.entity.ai.behavior.RunOne;
import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
import net.minecraft.world.entity.ai.behavior.StartAttacking;
import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
import net.minecraft.world.entity.ai.behavior.Swim;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.behavior.warden.SetRoarTarget;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.camel.CamelAi;
import net.minecraft.world.entity.monster.hoglin.HoglinAi;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.crafting.Ingredient;

public final class BearAi {
    private static final ImmutableList<SensorType<? extends Sensor<? super Bear>>> SENSOR_TYPES = ImmutableList.of(
            SensorType.NEAREST_LIVING_ENTITIES,
            SensorType.HURT_BY,
            SensorType.NEAREST_ADULT,
            SensorType.NEAREST_PLAYERS,
            ModSensors.BEAR_ATTACK_SENSOR.get(),
            ModSensors.BEAR_NEAREST_BEEHIVE_SENSOR.get());
    private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
            MemoryModuleType.HURT_BY,
            MemoryModuleType.HURT_BY_ENTITY,
            MemoryModuleType.NEAREST_ATTACKABLE,
            MemoryModuleType.ATTACK_TARGET,
            MemoryModuleType.ATTACK_COOLING_DOWN,
            MemoryModuleType.WALK_TARGET,
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.PATH,
            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
            MemoryModuleType.BREED_TARGET,
            MemoryModuleType.NEAREST_VISIBLE_ADULT,
            MemoryModuleType.ROAR_TARGET);
    private static final UniformInt ADULT_FOLLOW_RANGE = UniformInt.of(5, 16);

    protected static void initMemories(Bear Bear, RandomSource random) {
    }

    public static Brain.Provider<Bear> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    public static Brain<?> makeBrain(Brain<Bear> brain) {
        initCoreActivity(brain);
        initIdleActivity(brain);
        initFightActivity(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    private static void initCoreActivity(Brain<Bear> brain) {
        brain.addActivity(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new Swim(0.8F),
                        new LookAtTargetSink(45, 90),
                        new MoveToTargetSink()));
    }

    private static void initIdleActivity(Brain<Bear> brain) {
        brain.addActivity(
                Activity.IDLE,
                10,
                ImmutableList.of(
                        new BearSleep(),
                        new AnimalMakeLove(ModEntities.BEAR.get(), 0.6F, 10),
                        new Beg(),
                        new BearStartAttacking(Predicate.not(Bear::isBaby),
                                bear -> bear.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE)),
                        new BearRaidBeehive(),
                        BabyFollowAdult.create(ADULT_FOLLOW_RANGE, 1F),
                        SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F,
                                UniformInt.of(30, 60)),
                        new RandomLookAround(UniformInt.of(150, 250), 30.0F, 0.0F,
                                0.0F),
                        new RunOne<>(
                                ImmutableMap.of(MemoryModuleType.WALK_TARGET,
                                        MemoryStatus.VALUE_ABSENT),
                                ImmutableList.of(
                                        Pair.of(BehaviorBuilder.triggerIf(
                                                Predicate.not(Bear::refuseToMove),
                                                RandomStroll.stroll(
                                                        1f)),
                                                1),
                                        Pair.of(new DoNothing(30, 60), 1)))));
    }

    private static void initFightActivity(Brain<Bear> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.FIGHT,
                10,
                ImmutableList.of(
                        SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.5F),
                        MeleeAttack.create(10),
                        SetEntityLookTarget.create(10),
                        StopAttackingIfTargetInvalid.create(),
                        EraseMemoryIf.create(Bear::isInLove, MemoryModuleType.ATTACK_TARGET)),
                MemoryModuleType.ATTACK_TARGET);
    }

    public static void updateActivity(Bear bear) {
        bear.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
        bear.setAggressive(bear.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
    }
}
