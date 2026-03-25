package org.primal.entity.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.piglin.StopAdmiringIfItemTooFarAway;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.primal.entity.ai.behavior.generic.*;
import org.primal.entity.ai.behavior.generic.hide_on_log.AnimalEntersLog;
import org.primal.entity.ai.behavior.generic.home.AnimalGoesToBlock;
import org.primal.entity.ai.behavior.generic.home.AnimalRemoveHome;
import org.primal.entity.ai.behavior.generic.home.AnimalSearchHome;
import org.primal.entity.ai.behavior.generic.pet.AnimalSitting;
import org.primal.entity.ai.behavior.generic.pet.FollowOwner;
import org.primal.entity.ai.behavior.generic.stalk.SetWalkTargetFromStalkTargetIfTargetOutOfReach;
import org.primal.entity.ai.behavior.snake.SnakeEatEgg;
import org.primal.entity.ai.behavior.snake.SnakeGoesToNestAndRemovesEdibleEgg;
import org.primal.entity.animal.SnakeEntity;
import org.primal.registry.*;
import org.primal.util.Primal_Util;

import java.util.Optional;
import java.util.function.Predicate;

@SuppressWarnings("deprecation")
public final class SnakeAi {
    private static final ImmutableList<SensorType<? extends Sensor<? super SnakeEntity>>> SENSOR_TYPES = ImmutableList.of(
            Primal_Sensors.SNAKE_ENTITY_SENSOR.get(),
            SensorType.HURT_BY,
            SensorType.NEAREST_ADULT,
            Primal_Sensors.NEAREST_BABY.get(),
            SensorType.NEAREST_PLAYERS,
            Primal_Sensors.SNAKE_BLOCK_SENSOR.get(),
            Primal_Sensors.SNAKE_TEMPTATIONS_SENSOR.get(),
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
            MemoryModuleType.ROAR_SOUND_COOLDOWN,

            MemoryModuleType.HURT_BY,
            MemoryModuleType.HURT_BY_ENTITY,
            MemoryModuleType.BREED_TARGET,
            MemoryModuleType.HAS_HUNTING_COOLDOWN,
            Primal_MemoryModuleTypes.LAST_ATTACK_TARGET.get(),

            MemoryModuleType.IS_PANICKING,
            MemoryModuleType.TEMPTATION_COOLDOWN_TICKS,
            MemoryModuleType.IS_TEMPTED,
            MemoryModuleType.TEMPTING_PLAYER,
            MemoryModuleType.IS_PREGNANT,

            MemoryModuleType.AVOID_TARGET,

            MemoryModuleType.NEAREST_REPELLENT,

            Primal_MemoryModuleTypes.STALK_TARGET.get(),
            Primal_MemoryModuleTypes.STALK_COOLDOWN.get(),

            Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get(),
            Primal_MemoryModuleTypes.WAS_TOWARDS_IMPORTANT_BLOCK.get(),
            Primal_MemoryModuleTypes.MUSIC_BLOCK.get(),

            Primal_MemoryModuleTypes.CAUTIOUS_LIST.get(),
            Primal_MemoryModuleTypes.NEAREST_CAUTIOUS.get(),
            Primal_MemoryModuleTypes.NEAREST_ATTACKABLE_CAUTIOUS.get(),

            MemoryModuleType.DANCING,

            MemoryModuleType.HOME,
            Primal_MemoryModuleTypes.MATE_VARIANT.get(),
            Primal_MemoryModuleTypes.HOLLOW_LOG_ENTER_COOLDOWN.get(),

            MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM,
            MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS,
            MemoryModuleType.ADMIRING_ITEM,

            Primal_MemoryModuleTypes.HURT_RECENTLY.get(),
            Primal_MemoryModuleTypes.NEAREST_EDIBLE_EGG.get()
    );

    private static final UniformInt ADULT_FOLLOW_RANGE = UniformInt.of(5, 16);

    public static Ingredient getTemptations() {
        return Ingredient.of(Items.FERMENTED_SPIDER_EYE);
    }

    @SuppressWarnings("unused")
    public static void initMemories(SnakeEntity SnakeEntity, RandomSource random) {
    }

    public static Brain.Provider<SnakeEntity> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    public static Brain<?> makeBrain(Brain<SnakeEntity> brain) {
        initCoreActivity(brain);
        initIdleActivity(brain);
        initFightActivity(brain);
        initLayEggActivity(brain);
        initRetreatActivity(brain);
        initStalkingActivity(brain);
        initCautiousActivity(brain);
        initDanceActivity(brain);
        initAdmireItemActivity(brain);
        initFollowActivity(brain);
        initSitActivity(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    private static void initCoreActivity(Brain<SnakeEntity> brain) {
        brain.addActivity(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new LookAtTargetSink(45, 90),
                        new MoveToTargetSink(),
                        TryFindWaterSurface.create(16, 1, 0.90f)));
    }


    private static void initIdleActivity(Brain<SnakeEntity> brain) {
        brain.addActivity(
                Activity.IDLE,
                10,
                ImmutableList.of(
                        StartAttacking.create(SnakeAi::findNearestValidAttackTarget),
                        new AnimalRemoveHome<>(Primal_Tags.Block.HOLLOW_LOGS, s-> AnimalRemoveHome.isFar(s, 40)),
                        AnimalSearchHome.fromHollowLog(),
                        AnimalGoesToBlock.toHollowLog(s -> s.level().isRaining() ||
                                //Between 6am and 2pm
                                (Primal_Util.getDaytime(s.level()) >= 0 && Primal_Util.getDaytime(s.level()) < 8000)),
                        SnakeGoesToNestAndRemovesEdibleEgg.create(),
                        GoesToImportantBlockSometimes.create(25, 0,
                                mob -> mob.getBrain().getMemory(Primal_MemoryModuleTypes.HOLLOW_LOG_ENTER_COOLDOWN.get()).isEmpty(),
                                Primal_Util.toTicks(15), Primal_Util.toTicks(30)),
                        new AnimalEntersLog<>(UniformInt.of(Primal_Util.toTicks(5), Primal_Util.toTicks(15)), true),
                        new AnimalMakeLove(Primal_Entities.SNAKE.get(), 1.0f),
                        new RunOne<>(
                                ImmutableList.of(
                                        Pair.of(new FollowTemptation(livingEntity -> 1.0F, livingEntity -> livingEntity.isBaby() ? 2.5 : 3.5), 4),
                                        Pair.of(BehaviorBuilder.triggerIf(Predicate.not(SnakeEntity::refuseToMove),
                                                BabyFollowAdult.create(ADULT_FOLLOW_RANGE, 1.0F)), 1)
                                )
                        ),
                        SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60)),
                        createIdleBehaviors()
                )
        );
    }

    private static void initStalkingActivity(Brain<SnakeEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(Primal_Activities.STALK.get(), 10,
                ImmutableList.of(
                        StartAttacking.create(SnakeAi::findNearestValidAttackTarget),
                        SetWalkTargetFromStalkTargetIfTargetOutOfReach.create(1.0F),
                        SetEntityLookTarget.create(50),
                        EraseMemoryIf.create(Animal::isInLove, Primal_MemoryModuleTypes.STALK_TARGET.get())
                ),
                Primal_MemoryModuleTypes.STALK_TARGET.get());
    }

    private static void initCautiousActivity(Brain<SnakeEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(Primal_Activities.CAUTIOUS.get(), 10,
                ImmutableList.of(
                        StartAttacking.create(SnakeAi::findNearestValidAttackTarget),
                        SetLookTarget.fromCautiousTarget(),
                        StopMoving.create()
                ),
                Primal_MemoryModuleTypes.NEAREST_CAUTIOUS.get());
    }

    private static void initDanceActivity(Brain<SnakeEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(Primal_Activities.DANCING.get(), 10,
                ImmutableList.of(
                        StartAttacking.create(SnakeAi::findNearestValidAttackTarget),
                        SetLookTarget.fromMusicBlock(),
                        StopMoving.create()
                ),
                MemoryModuleType.DANCING);
    }

    public static final int DISTANCE_TO_SEE_ADMIRE_ITEM=10;
    private static void initAdmireItemActivity(Brain<SnakeEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.ADMIRE_ITEM,
                10,
                ImmutableList.of(
                        SnakeEatEgg.create(),
                        GoToWantedItem.create(Predicate.not(SnakeEntity::refuseToMove),
                                1.2F, true, DISTANCE_TO_SEE_ADMIRE_ITEM),
                        StopAdmiringIfItemTooFarAway.create(DISTANCE_TO_SEE_ADMIRE_ITEM)),
                MemoryModuleType.ADMIRING_ITEM);
    }

    private static void initFollowActivity(Brain<SnakeEntity> brain) {
        brain.addActivity(
                Primal_Activities.FOLLOW.get(),
                10,
                ImmutableList.of(
                        StartAttacking.create(SnakeAi::findNearestValidAttackTarget),
                        new AnimalMakeLove(Primal_Entities.SNAKE.get(), 1.0f),
                        new FollowOwner<>(),
                        SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60)),
                        createIdleBehaviors()
                )
        );
    }

    private static void initSitActivity(Brain<SnakeEntity> brain) {
        brain.addActivity(
                Primal_Activities.SIT.get(),
                ImmutableList.of(
                        Pair.of(0, new AnimalSitting())
                )
        );
    }

    private static void initLayEggActivity(Brain<SnakeEntity> brain) {
        brain.addActivityWithConditions(
                Activity.LAY_SPAWN,
                ImmutableList.of(
                        Pair.of(0, AnimalGoesToBlock.toHollowLog()),
                        Pair.of(1, new AnimalEntersLog<>(UniformInt.of(Primal_Util.toTicks(10), Primal_Util.toTicks(30)), false)),
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

    private static void initFightActivity(Brain<SnakeEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.FIGHT,
                10,
                ImmutableList.of(
                        BecomePassiveIfMemoryPresent.create(MemoryModuleType.NEAREST_REPELLENT, 200),
                        SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F),
                        MeleeAttack.create(20),
                        SetEntityLookTarget.create(50),
                        StopAttackingIfTargetInvalid.create(),
                        EraseMemoryIf.create(b-> b.isInLove() || Primal_Util.Ai.lessThanMinAirSlow(b), MemoryModuleType.ATTACK_TARGET)),
                MemoryModuleType.ATTACK_TARGET);
    }

    private static void initRetreatActivity(Brain<SnakeEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.AVOID,
                10,
                ImmutableList.of(
                        SetWalkTargetAwayFrom.entity(MemoryModuleType.AVOID_TARGET, 1.2F, 20, false),
                        createIdleBehaviors(),
                        SetEntityLookTargetSometimes.create(8.0F, UniformInt.of(30, 60)),
                        EraseMemoryIf.create(bear -> !bear.isBaby(), MemoryModuleType.AVOID_TARGET)
                ),
                MemoryModuleType.AVOID_TARGET
        );
    }

    private static RunOne<SnakeEntity> createIdleBehaviors() {
        return new RunOne<>(
                ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
                ImmutableList.of(
                        Pair.of(stroll(1.0F), 10),
                        Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), 8),
                        Pair.of(new DoNothing(30, 60), 1)
                )
        );
    }

    public static OneShot<PathfinderMob> stroll(float speedModifier) {
        return RandomStroll.strollFlyOrSwim(
                speedModifier, mob -> LandRandomPos.getPos(mob, 5, 2), mob -> true
        );
    }

    public static void updateActivity(SnakeEntity snake) {
        var brain = snake.getBrain();

        //Is angry per 20s
        if(brain.getMemory(MemoryModuleType.ATTACK_TARGET).isPresent())
            brain.setMemoryWithExpiry(Primal_MemoryModuleTypes.LAST_ATTACK_TARGET.get(), brain.getMemory(MemoryModuleType.ATTACK_TARGET).get(), 400);

        //Set to admire items
        var hasWant = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM);
        if(hasWant.isPresent()
                && snake.distanceTo(hasWant.get())<DISTANCE_TO_SEE_ADMIRE_ITEM
                && hasWant.get().onGround()
                && !Primal_Util.Ai.wasHurtRecently(snake)
                && Primal_Util.Ai.canReachPos(snake, hasWant.get().blockPosition()))
            brain.setMemory(MemoryModuleType.ADMIRING_ITEM, true);
        else
            brain.eraseMemory(MemoryModuleType.ADMIRING_ITEM);

        //Removes the grudge if starts dancing
        if(brain.getMemory(Primal_MemoryModuleTypes.LAST_ATTACK_TARGET.get()).isPresent() && brain.getMemory(MemoryModuleType.DANCING).isPresent())
            brain.eraseMemory(Primal_MemoryModuleTypes.LAST_ATTACK_TARGET.get());

        if(snake.isSitting()){
            brain.setActiveActivityToFirstValid(ImmutableList.of(Primal_Activities.DANCING.get(), Primal_Activities.SIT.get()));
        }
        else if(snake.isFollowing()){
            brain.setActiveActivityToFirstValid(ImmutableList.of(Primal_Activities.DANCING.get(), Activity.FIGHT, Activity.LAY_SPAWN, Primal_Activities.FOLLOW.get()));
        }
        else if(snake.isBaby()){
            brain.setActiveActivityToFirstValid(ImmutableList.of(Primal_Activities.DANCING.get(), Activity.AVOID, Activity.IDLE));
        }
        else {
            brain.setActiveActivityToFirstValid(ImmutableList.of(Primal_Activities.DANCING.get(), Activity.FIGHT, Primal_Activities.CAUTIOUS.get(), Primal_Activities.STALK.get(), Activity.LAY_SPAWN, Activity.ADMIRE_ITEM, Activity.IDLE));
        }

        snake.setAggressive(brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
    }

    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(SnakeEntity snake) {
        return BehaviorUtils.isBreeding(snake) ||  snake.isBaby()? Optional.empty() : snake.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE);
    }
}