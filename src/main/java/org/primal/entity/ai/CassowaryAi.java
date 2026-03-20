package org.primal.entity.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.entity.monster.piglin.StopAdmiringIfItemTooFarAway;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import org.primal.entity.ai.behavior.cassowary.CassowaryLungeAttack;
import org.primal.entity.ai.behavior.cassowary.CassowaryPickFruit;
import org.primal.entity.ai.behavior.generic.*;
import org.primal.entity.ai.behavior.generic.home.AnimalGoesToBlock;
import org.primal.entity.ai.behavior.generic.home.AnimalRemoveHome;
import org.primal.entity.ai.behavior.generic.home.AnimalSearchHome;
import org.primal.entity.animal.*;
import org.primal.registry.*;
import org.primal.util.Primal_Util;

import java.util.Optional;
import java.util.function.Predicate;

public class CassowaryAi {

    //──────────────────────────────────── Init ────────────────────────────────────
    private static final ImmutableList<SensorType<? extends Sensor<? super CassowaryEntity>>> SENSOR_TYPES = ImmutableList.of(
            Primal_Sensors.GENERIC_ATTACK_SENSOR.get(),
            SensorType.HURT_BY,
            Primal_Sensors.NEAREST_BABY.get(),
            SensorType.NEAREST_PLAYERS,
            SensorType.NEAREST_ADULT,
            Primal_Sensors.CASSOWARY_TEMPTATIONS_SENSOR.get(),
            Primal_Sensors.CASSOWARY_NEAREST_EGG.get(),
            Primal_Sensors.NEAREST_NEST.get(),
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
            Primal_MemoryModuleTypes.LUNGE_COOLDOWN.get(),

            Primal_MemoryModuleTypes.WAS_IDLE_ANIMATION.get(),
            Primal_MemoryModuleTypes.WAS_TOWARDS_IMPORTANT_BLOCK.get(),

            Primal_MemoryModuleTypes.HURT_RECENTLY.get(),

            MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM,
            MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS,
            MemoryModuleType.ADMIRING_ITEM
    );

    public static Predicate<ItemStack> getTemptations() {
        return CassowaryEntity::isMatingFood;
    }

    public static void initMemories(CassowaryEntity cassowary, RandomSource random) {
    }

    public static Brain.Provider<CassowaryEntity> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    public static Brain<?> makeBrain(Brain<CassowaryEntity> brain) {
        initCoreActivity(brain);
        initIdleActivity(brain);
        initFightActivity(brain);
        initAdmireItemActivity(brain);
        initRetreatActivity(brain);
        initEscapeActivity(brain);
        initLayEggActivity(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    //──────────────────────────────────── Activities ────────────────────────────────────
    private static void initCoreActivity(Brain<CassowaryEntity> brain) {
        brain.addActivity(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new Swim(0.8F),
                        new LookAtTargetSink(45, 90),
                        new MoveToTargetSink(),
                        EraseMemoryIf.create(cassowary ->
                                        cassowary.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isPresent()
                                                && !cassowary.canAttack(cassowary.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get()),
                                MemoryModuleType.ATTACK_TARGET)
                )
        );
    }

    private static final UniformInt ADULT_FOLLOW_RANGE = UniformInt.of(3, 16);
    private static void initIdleActivity(Brain<CassowaryEntity> brain) {
        brain.addActivity(
                Activity.IDLE,
                10,
                ImmutableList.of(
                        StartAttacking.create(CassowaryAi::findNearestValidAttackTarget),
                        AnimalRemoveHome.basicNest(30),
                        AnimalSearchHome.fromNest(),
                        GoesToImportantBlockSometimes.create(7, 2, mob -> !mob.isBaby(), 100, 60),
//                        new AnimalReturnHome<>(7, 2, false),
                        new AnimalMakeLove(Primal_Entities.CASSOWARY.get()),
                        new RunOne<>(
                                ImmutableList.of(
                                        Pair.of(new FollowTemptation(livingEntity -> 1.0F, livingEntity -> livingEntity.isBaby() ? 2.5 : 3.5), 4),
                                        Pair.of(BehaviorBuilder.triggerIf(Predicate.not(CassowaryEntity::refuseToMove),
                                                BabyFollowAdult.create(ADULT_FOLLOW_RANGE, 1.0F)), 1)
                                )
                        ),
                        SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60)),

                        createIdleBehaviors()
                )
        );
    }

    private static RunOne<CassowaryEntity> createIdleBehaviors() {
        return new RunOne<>(
                ImmutableList.of(
                        Pair.of(IdlePoseAnimationBehavior.create(Pose.SITTING, 100, 500,
                                        m-> IdlePoseAnimationBehavior.basicCanStart(m) && !m.isInWater()
                                                && !m.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM), 60),
                                1),
                        Pair.of(BehaviorBuilder.triggerIf(Predicate.not(CassowaryEntity::refuseToMove), RandomStroll.stroll(0.9F)), 4),
                        Pair.of(StrollToPoi.create(MemoryModuleType.HOME, 1.0F, 2, 20), 4),
                        Pair.of(StrollAroundPoi.create(MemoryModuleType.HOME, 1.0F, 8), 4),
                        Pair.of(new RandomLookAround(UniformInt.of(150, 250), 30.0F, 0.0F, 0.0F),
                                8),
                        Pair.of(new DoNothing(40, 80),
                                10)
                )
        );
    }


    private static void initFightActivity(Brain<CassowaryEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.FIGHT,
                10,
                ImmutableList.of(
                        SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F),
                        new CassowaryLungeAttack(20),
                        MeleeAttack.create(20),
                        SetEntityLookTarget.create(50),
                        StopAttackingIfTargetInvalid.create(),
                        EraseMemoryIf.create(Animal::isInLove, MemoryModuleType.ATTACK_TARGET)),
                MemoryModuleType.ATTACK_TARGET);
    }

    public static final int DISTANCE_TO_SEE_ADMIRE_ITEM=10;
    private static void initAdmireItemActivity(Brain<CassowaryEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.ADMIRE_ITEM,
                10,
                ImmutableList.of(
                        CassowaryPickFruit.create(),
                        GoToWantedItem.create(Predicate.not(CassowaryEntity::refuseToMove),
                                1.2F, true, DISTANCE_TO_SEE_ADMIRE_ITEM),
                        StopAdmiringIfItemTooFarAway.create(DISTANCE_TO_SEE_ADMIRE_ITEM)),
                MemoryModuleType.ADMIRING_ITEM);
    }

    private static void initRetreatActivity(Brain<CassowaryEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.AVOID,
                10,
                ImmutableList.of(
                        SetWalkTargetAwayFrom.entity(MemoryModuleType.AVOID_TARGET, 1.5F, 20, false)
                ),
                MemoryModuleType.AVOID_TARGET
        );
    }

    private static void initEscapeActivity(Brain<CassowaryEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Primal_Activities.ESCAPE.get(),
                10,
                ImmutableList.of(
                        SetWalkTargetAwayFrom.pos(MemoryModuleType.NEAREST_REPELLENT, 1.5F, 80, false)
                ),
                MemoryModuleType.NEAREST_REPELLENT
        );
    }

    private static void initLayEggActivity(Brain<CassowaryEntity> brain) {
        brain.addActivityWithConditions(
                Activity.LAY_SPAWN,
                ImmutableList.of(
                        Pair.of(0, AnimalGoesToBlock.toNest()),
                        Pair.of(1, TryLayEggOnLandOrNest.create(Primal_Blocks.CASSOWARY_EGG.get())),
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

    //──────────────────────────────────── Misc ────────────────────────────────────
    public static void updateActivity(CassowaryEntity cassowary) {
        Brain<CassowaryEntity> brain = cassowary.getBrain();

        //Set to admire items
        var hasWant = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM);
        if(hasWant.isPresent() && cassowary.distanceTo(hasWant.get())<DISTANCE_TO_SEE_ADMIRE_ITEM && hasWant.get().onGround() && !Primal_Util.Ai.wasHurtRecently(cassowary)) brain.setMemory(MemoryModuleType.ADMIRING_ITEM, true);
        else brain.eraseMemory(MemoryModuleType.ADMIRING_ITEM);

        //Runs from other cassowary if it has less than 30% health
        var visibleAdult = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT);
        if(!cassowary.isBaby() && visibleAdult.isPresent() && cassowary.getMaxHealth()*0.30>=cassowary.getHealth()) brain.setMemoryWithExpiry(MemoryModuleType.NEAREST_REPELLENT, visibleAdult.get().getOnPos(), 600);

        if(cassowary.isBaby())
            brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.ADMIRE_ITEM, Activity.AVOID, Activity.IDLE));
        else
            brain.setActiveActivityToFirstValid(ImmutableList.of(Primal_Activities.ESCAPE.get(), Activity.ADMIRE_ITEM, Activity.FIGHT, Activity.LAY_SPAWN, Activity.IDLE));

        cassowary.setAggressive(brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
    }

    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(CassowaryEntity cassowary) {
        return BehaviorUtils.isBreeding(cassowary) || cassowary.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM) || cassowary.isBaby()? Optional.empty() : cassowary.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE);
    }
}