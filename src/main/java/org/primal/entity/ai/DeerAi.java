package org.primal.entity.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import org.primal.entity.ai.behavior.deer.DeerHeadbutt;
import org.primal.entity.ai.behavior.deer.DeerRegrowAntler;
import org.primal.entity.ai.behavior.generic.*;
import org.primal.entity.animal.DeerEntity;
import org.primal.registry.Primal_Activities;
import org.primal.registry.Primal_Entities;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.registry.Primal_Sensors;
import org.primal.util.Primal_Util;

import java.util.function.Predicate;

@SuppressWarnings("deprecation")
public class DeerAi {

    //──────────────────────────────────── Init ────────────────────────────────────
    private static final ImmutableList<SensorType<? extends Sensor<? super DeerEntity>>> SENSOR_TYPES = ImmutableList.of(
            SensorType.HURT_BY,
            Primal_Sensors.NEAREST_BABY.get(),
            Primal_Sensors.DEER_TEMPTATIONS_SENSOR.get(),
            Primal_Sensors.DEER_ENTITY_SENSOR.get(),
            SensorType.NEAREST_PLAYERS,
            SensorType.NEAREST_ADULT,
            SensorType.NEAREST_ITEMS);

    private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
            MemoryModuleType.NEAREST_LIVING_ENTITIES,
            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.GAZE_COOLDOWN_TICKS,
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
            MemoryModuleType.NEAREST_VISIBLE_ADULT,

            MemoryModuleType.NEAREST_REPELLENT,
            MemoryModuleType.IS_PREGNANT,
            MemoryModuleType.PACIFIED,

            MemoryModuleType.NEAREST_HOSTILE,

            Primal_MemoryModuleTypes.NEAREST_VISIBLE_BABY.get(),
            Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get(),

            MemoryModuleType.HOME,

            Primal_MemoryModuleTypes.WAS_IDLE_ANIMATION.get(),
            Primal_MemoryModuleTypes.WAS_TRIGGER_ANIMATION.get(),

            Primal_MemoryModuleTypes.HURT_RECENTLY.get(),

            MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM,
            MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS,
            MemoryModuleType.ADMIRING_ITEM,

            Primal_MemoryModuleTypes.NEAREST_SCARED.get(),
            MemoryModuleType.SNIFF_COOLDOWN,
            Primal_MemoryModuleTypes.NEAREST_CAUTIOUS.get(),
            Primal_MemoryModuleTypes.NEAREST_PACK_MEMBER.get(),
            Primal_MemoryModuleTypes.GO_TO_PACK_MEMBER_COOLDOWN.get(),

            Primal_MemoryModuleTypes.NEAREST_PLAY_MOB.get(),
            Primal_MemoryModuleTypes.LUNGE_COOLDOWN.get()
    );

    public static Predicate<ItemStack> getTemptations() {
        return DeerEntity::isMatingFood;
    }

    public static void initMemories(DeerEntity deer, RandomSource random) {
    }

    public static Brain.Provider<DeerEntity> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    public static Brain<?> makeBrain(Brain<DeerEntity> brain) {
        initCoreActivity(brain);
        initIdleActivity(brain);
        initRetreatActivity(brain);
        initEscapeActivity(brain);
        initCautiousActivity(brain);
        initPlayActivity(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    //──────────────────────────────────── Activities ────────────────────────────────────
    private static void initCoreActivity(Brain<DeerEntity> brain) {
        brain.addActivity(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new Swim(0.8F),
                        new LookAtTargetSink(45, 90),
                        new MoveToTargetSink()
                )
        );
    }

    private static final UniformInt ADULT_FOLLOW_RANGE = UniformInt.of(2, 19);
    private static void initIdleActivity(Brain<DeerEntity> brain) {
        brain.addActivity(
                Activity.IDLE,
                10,
                ImmutableList.of(
                        new AnimalMakeLove(Primal_Entities.DEER.get()),
                        new RunOne<>(
                                ImmutableList.of(
                                        Pair.of(new FollowTemptation(livingEntity -> 1.0F, livingEntity -> livingEntity.isBaby() ? 2.5 : 3.5), 4),
                                        Pair.of(BehaviorBuilder.triggerIf(Predicate.not(DeerEntity::refuseToMove),
                                                BabyFollowAdult.create(ADULT_FOLLOW_RANGE, 1.0F)), 1)
                                )
                        ),
                        SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60)),
                        SetWalkTargetFromPackMemberSometimes.create(
                                1f, 12, 3,
                                UniformInt.of(Primal_Util.toTicks(5), Primal_Util.toTicks(20)),
                                Predicate.not(DeerEntity::isBaby)),
                        createIdleBehaviors()
                )
        );
    }

    private static RunOne<DeerEntity> createIdleBehaviors() {
        return new RunOne<>(
                ImmutableList.of(
                        Pair.of(new GateBehavior<>(
                                        ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
                                        ImmutableSet.of(),
                                        GateBehavior.OrderPolicy.ORDERED,
                                        GateBehavior.RunningPolicy.TRY_ALL,
                                        ImmutableList.of(
                                                Pair.of(DeerRegrowAntler.create(TimeUtil.rangeOfSeconds(2, 5)), 1),
                                                Pair.of(StopAndTriggerAnimation.create(DeerEntity.EAT,
                                                        41,
                                                        m->m.getBlockStateOn().is(Blocks.GRASS_BLOCK)
                                                                && !Primal_Util.isMoving(m)
                                                                && m.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_PLAY_MOB.get()).isEmpty(),
                                                        TimeUtil.rangeOfSeconds(3, 10)), 1)
                                        )),
                                3),
                        Pair.of(BehaviorBuilder.triggerIf(Predicate.not(DeerEntity::refuseToMove), RandomStroll.stroll(0.9F)), 5),
                        Pair.of(StopAndTriggerAnimation.create(DeerEntity.LOOK,
                                41,
                                m->!Primal_Util.isMoving(m)
                                        && m.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_PLAY_MOB.get()).isEmpty(),
                                TimeUtil.rangeOfSeconds(8, 15)), 1),
                        Pair.of(new RandomLookAround(UniformInt.of(150, 250), 30.0F, 0.0F, 0.0F),
                                3),
                        Pair.of(new DoNothing(40, 80),
                                1)
                )
        );
    }

    public static final UniformInt RETREAT_DURATION = TimeUtil.rangeOfSeconds(15, 30);
    private static void initRetreatActivity(Brain<DeerEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.AVOID,
                10,
                ImmutableList.of(
                        SetWalkTargetAwayFrom.entity(MemoryModuleType.AVOID_TARGET, 1.3F, 30, false),
                        RandomStroll.stroll(0.9F),
                        new RandomLookAround(UniformInt.of(150, 250), 30.0F, 0.0F, 0.0F)
                ),
                MemoryModuleType.AVOID_TARGET
        );
    }

    private static void initEscapeActivity(Brain<DeerEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Primal_Activities.ESCAPE.get(),
                10,
                ImmutableList.of(
                        SetWalkTargetAwayFrom.pos(MemoryModuleType.NEAREST_REPELLENT, 1.0F, 80, false)
                ),
                MemoryModuleType.NEAREST_REPELLENT
        );
    }

    private static void initCautiousActivity(Brain<DeerEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(Primal_Activities.CAUTIOUS.get(), 10,
                ImmutableList.of(
                        SetLookTarget.fromCautiousTarget(),
                        StopMoving.create()
                ),
                Primal_MemoryModuleTypes.NEAREST_CAUTIOUS.get());
    }

    private static void initPlayActivity(Brain<DeerEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Primal_Activities.PLAY.get(),
                10,
                ImmutableList.of(
                        SetLookTarget.fromPlayTarget(-80),
                        SetWalkTargetFromPlayTargetIfTargetOutOfReach.create(1.2f),
                        new DeerHeadbutt(TimeUtil.rangeOfSeconds(35, 75))
                ),
                Primal_MemoryModuleTypes.NEAREST_PLAY_MOB.get()
        );
    }

    //──────────────────────────────────── Misc ────────────────────────────────────
    public static void updateActivity(DeerEntity deer) {
        Brain<DeerEntity> brain = deer.getBrain();

        if(deer.isBaby())
            brain.setActiveActivityToFirstValid(ImmutableList.of(Primal_Activities.ESCAPE.get(), Activity.AVOID, Activity.IDLE));
        else
            brain.setActiveActivityToFirstValid(ImmutableList.of(Primal_Activities.ESCAPE.get(), Activity.AVOID, Primal_Activities.CAUTIOUS.get(), Primal_Activities.PLAY.get(), Activity.IDLE));
    }
}