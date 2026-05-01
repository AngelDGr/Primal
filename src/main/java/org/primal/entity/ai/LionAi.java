package org.primal.entity.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import org.primal.advancements.criterion.Primal_CustomCriterion;
import org.primal.entity.ai.behavior.generic.*;
import org.primal.entity.ai.behavior.generic.pet.AnimalSitting;
import org.primal.entity.ai.behavior.generic.pet.FollowOwner;
import org.primal.entity.ai.behavior.generic.roar.AnimalRoar;
import org.primal.entity.ai.behavior.generic.roar.SetRoarTarget;
import org.primal.entity.ai.behavior.generic.stalk.SetWalkTargetFromStalkTargetIfTargetOutOfReach;
import org.primal.entity.ai.behavior.lion.AnimalRelaxOnSleepingOwner;
import org.primal.entity.ai.behavior.lion.LionLungeAttack;
import org.primal.entity.ai.behavior.lion.LionMaul;
import org.primal.entity.ai.behavior.lion.LionStartAttack;
import org.primal.entity.animal.LionEntity;
import org.primal.registry.*;
import org.primal.util.Primal_Util;

import java.util.Optional;
import java.util.function.Predicate;

public class LionAi {
    //──────────────────────────────────── Init ────────────────────────────────────
    private static final ImmutableList<SensorType<? extends Sensor<? super LionEntity>>> SENSOR_TYPES = ImmutableList.of(
            Primal_Sensors.LION_ENTITY_SENSOR.get(),
            SensorType.HURT_BY,
            Primal_Sensors.NEAREST_BABY.get(),
            SensorType.NEAREST_PLAYERS,
            SensorType.NEAREST_ADULT,
            Primal_Sensors.LION_TEMPTATIONS_SENSOR.get(),
            Primal_Sensors.LION_NEAREST_TO_LAY.get(),
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

            MemoryModuleType.IS_PANICKING,
            MemoryModuleType.TEMPTATION_COOLDOWN_TICKS,
            MemoryModuleType.IS_TEMPTED,
            MemoryModuleType.TEMPTING_PLAYER,

            MemoryModuleType.AVOID_TARGET,
            MemoryModuleType.NEAREST_VISIBLE_ADULT,

            MemoryModuleType.NEAREST_HOSTILE,

            Primal_MemoryModuleTypes.NEAREST_VISIBLE_BABY.get(),
            Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get(),
            Primal_MemoryModuleTypes.WAS_TOWARDS_IMPORTANT_BLOCK.get(),

            Primal_MemoryModuleTypes.LAST_ATTACK_TARGET.get(),

            Primal_MemoryModuleTypes.WAS_IDLE_ANIMATION.get(),

            Primal_MemoryModuleTypes.STALK_TARGET.get(),
            Primal_MemoryModuleTypes.STALK_COOLDOWN.get(),

            MemoryModuleType.HOME,
            Primal_MemoryModuleTypes.NEAREST_LEADER.get(),
            Primal_MemoryModuleTypes.NEAREST_PACK_MEMBER.get(),
            Primal_MemoryModuleTypes.GO_TO_PACK_MEMBER_COOLDOWN.get(),

            Primal_MemoryModuleTypes.LUNGE_COOLDOWN.get(),

            Primal_MemoryModuleTypes.IS_STUNNED.get(),
            Primal_MemoryModuleTypes.IS_GRABBING.get(),

            MemoryModuleType.PACIFIED,
            Primal_MemoryModuleTypes.CAUTIOUS_LIST.get(),
            Primal_MemoryModuleTypes.NEAREST_ATTACKABLE_CAUTIOUS.get()
    );

    public static Predicate<ItemStack> getTemptations() {
        return LionEntity::isMatingFood;
    }

    private static final Primal_CustomCriterion NAP_ADVANCEMENT = Primal_Advancements.LION_NAP;
    public static void initMemories(LionEntity lion, RandomSource random) {
//        GlobalPos globalpos = GlobalPos.of(lion.level().dimension(), lion.blockPosition());
//        lion.getBrain().setMemory(MemoryModuleType.HOME, globalpos);
    }

    public static Brain.Provider<LionEntity> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    public static Brain<?> makeBrain(Brain<LionEntity> brain) {
        initCoreActivity(brain);
        initIdleActivity(brain);
        initFightActivity(brain);
        initRetreatActivity(brain);
        initRoarActivity(brain);
        initStalkingActivity(brain);
        initMaulActivity(brain);
        initFollowActivity(brain);
        initSitActivity(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    //──────────────────────────────────── Activities ────────────────────────────────────
    private static void initCoreActivity(Brain<LionEntity> brain) {
        brain.addActivity(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new Swim(0.8F),
                        new LookAtTargetSink(45, 90),
                        new MoveToTargetSink(),
                        EraseMemoryIf.create(lion ->
                                        lion.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isPresent()
                                                && !lion.canAttack(lion.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get()),
                                MemoryModuleType.ATTACK_TARGET),
                        SetLookTarget.fromRoarTarget()
                )
        );
    }

    private static final UniformInt ADULT_FOLLOW_RANGE = UniformInt.of(7, 16);
    private static void initIdleActivity(Brain<LionEntity> brain) {
        brain.addActivity(
                Activity.IDLE,
                10,
                ImmutableList.of(
                        new AnimalMakeLove(Primal_Entities.LION.get(), 1.0f),
                        SetRoarTarget.create(LionAi::canRoar),
                        StartAttacking.create(LionAi::findNearestValidAttackTargetDuringIdle),
                        new RunOne<>(
                                ImmutableList.of(
                                        Pair.of(new FollowTemptation(livingEntity -> 1.0F, livingEntity -> livingEntity.isBaby() ? 2.5 : 3.5), 4),
                                        Pair.of(BehaviorBuilder.triggerIf(Predicate.not(LionEntity::refuseToMove),
                                                BabyFollowAdult.create(ADULT_FOLLOW_RANGE, 1.0F)), 1)
                                )
                        ),
                        SetWalkTargetFromPackMemberSometimes.create(
                                1f, 16, 5,
                                UniformInt.of(Primal_Util.toTicks(8), Primal_Util.toTicks(30)),
                                Predicate.not(LionEntity::isBaby).and(LionEntity::isWandering)),
                        SetEntityLookTargetSometimes.create(6.0F, UniformInt.of(30, 60), target -> EntityType.PLAYER.equals(target.getType()) && !target.isPassenger()),
                        AnimalRelaxOnSleepingOwner.create(1.0f, Primal_LootTables.LION_MORNING_GIFT, 0.85f, NAP_ADVANCEMENT),
                        GoesToImportantBlockSometimes.create(9, 0, mob -> mob.level().getRandom().nextFloat()<0.15, 200, 100),
                        createIdleBehaviors()
                )
        );
    }

    private static RunOne<LionEntity> createIdleBehaviors() {
        return new RunOne<>(
                ImmutableList.of(
                        Pair.of(BehaviorBuilder.triggerIf(Predicate.not(LionEntity::refuseToMove), RandomStroll.stroll(1f)), 2),
                        //For maneless lions
                        Pair.of(IdlePoseAnimationBehavior.create(
                                        "Laying",
                                        80, 200,
                                        l ->
                                                l.isManeless() && canLay(l),
                                        300),
                                2),
                        //For maned lions
                        Pair.of(IdlePoseAnimationBehavior.create(
                                        "Laying",
                                        200, 500,
                                        l ->
                                                !l.isManeless() && canLay(l),
                                        150),
                                2),
                        Pair.of(StrollToPoi.create(MemoryModuleType.HOME, 1.0F, 2, 100), 2),
                        Pair.of(StrollAroundPoi.create(MemoryModuleType.HOME, 1.0F, 16), 2),
                        Pair.of(new DoNothing(30, 60), 1)));
    }

    private static boolean canLay(LionEntity l){
        return IdlePoseAnimationBehavior.basicCanStart(l)
                && !l.isInWater()
                && l.onGround()
                && !l.isStalking()
                && !l.isAggressive()
                && l.getBrain().getMemory(MemoryModuleType.TEMPTING_PLAYER).isEmpty()
                && l.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get()).isEmpty()
                && !l.isFollowing()
                && l.getBrain().getMemory(MemoryModuleType.ROAR_TARGET).isEmpty();
    }

    private static void initFightActivity(Brain<LionEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.FIGHT,
                10,
                ImmutableList.of(
                        SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F),
                        new LionLungeAttack(20),
                        new LionStartAttack(20),
                        SetEntityLookTarget.create(50),
                        StopAttackingIfTargetInvalid.create()),
                MemoryModuleType.ATTACK_TARGET);
    }

    private static void initRetreatActivity(Brain<LionEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.AVOID,
                10,
                ImmutableList.of(
                        SetWalkTargetAwayFrom.entity(MemoryModuleType.AVOID_TARGET, 1.3F, 30, false)
                ),
                MemoryModuleType.AVOID_TARGET
        );
    }

    private static void initRoarActivity(Brain<LionEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(Activity.ROAR, 10,
                ImmutableList.of(
                        StopMoving.create(),
                        new AnimalRoar<>(6f,
                                l-> AnimalRoar.setAttackTargetAndNearby(l, LionEntity.class,
                                        ll->{
                                            if(l.isTame()){
                                                //Tamed lions only command if is adult and maneless and has the same owner
                                                return ll!=l && ll.getOwner()==l.getOwner() && (!ll.isBaby() && ll.isManeless());
                                            } else {
                                                //Wild only command adults, maneless untamed lions
                                                return ll!=l && !ll.isTame() && (!ll.isBaby() && ll.isManeless());
                                            }
                                                }),
                                100)
                ),
                MemoryModuleType.ROAR_TARGET);
    }

    private static void initStalkingActivity(Brain<LionEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(Primal_Activities.STALK.get(), 10,
                ImmutableList.of(
                        StartAttacking.create(LionAi::findNearestValidAttackTarget),
                        SetWalkTargetFromStalkTargetIfTargetOutOfReach.create(1.0F),
                        SetEntityLookTarget.create(50),
                        EraseMemoryIf.create(Animal::isInLove, Primal_MemoryModuleTypes.STALK_TARGET.get())
                ),
                Primal_MemoryModuleTypes.STALK_TARGET.get());
    }

    private static void initMaulActivity(Brain<LionEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Primal_Activities.GRAB.get(),
                10,
                ImmutableList.of(
                        StopMoving.create(),
                        new LionMaul(40)),
                Primal_MemoryModuleTypes.IS_GRABBING.get());
    }

    private static void initFollowActivity(Brain<LionEntity> brain) {
        brain.addActivity(
                Primal_Activities.FOLLOW.get(),
                10,
                ImmutableList.of(
                        SetRoarTarget.create(LionAi::canRoar),
                        StartAttacking.create(LionAi::findNearestValidAttackTargetDuringIdle),
                        new AnimalMakeLove(Primal_Entities.LION.get(), 1.0f),
                        new FollowOwner<>(),
                        SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60)),
                        new RandomLookAround(UniformInt.of(150, 250), 30.0F, 0.0F, 0.0F),
                        AnimalRelaxOnSleepingOwner.create(1.0f, Primal_LootTables.LION_MORNING_GIFT, 0.85f, NAP_ADVANCEMENT),
                        createIdleBehaviors()
                )
        );
    }

    private static void initSitActivity(Brain<LionEntity> brain) {
        brain.addActivity(
                Primal_Activities.SIT.get(),
                ImmutableList.of(
                        Pair.of(0, new AnimalSitting<>("Laying", LionEntity::isLayingPose))
                )
        );
    }

    //──────────────────────────────────── Misc ────────────────────────────────────
    public static void updateActivity(LionEntity lion) {
        Brain<LionEntity> brain = lion.getBrain();

        //Is angry per 30s
        if(brain.getMemory(MemoryModuleType.ATTACK_TARGET).isPresent()) brain.setMemoryWithExpiry(Primal_MemoryModuleTypes.LAST_ATTACK_TARGET.get(), brain.getMemory(MemoryModuleType.ATTACK_TARGET).get(), 600);

        if(!brain.isActive(Primal_Activities.STALK.get()) && brain.getMemory(Primal_MemoryModuleTypes.STALK_TARGET.get()).isEmpty() && lion.hasPose(Pose.CROUCHING))
            lion.setPose(Pose.STANDING);

        if(lion.isSitting()){
            lion.getBrain().setActiveActivityIfPossible(Primal_Activities.SIT.get());
        }
        else if(lion.isFollowing()){
            lion.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.AVOID, Primal_Activities.GRAB.get(), Activity.ROAR, Activity.FIGHT, Primal_Activities.FOLLOW.get()));
        } else {
            if (lion.isBaby())
                brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.AVOID, Activity.IDLE));
            else
                brain.setActiveActivityToFirstValid(ImmutableList.of(Primal_Activities.GRAB.get(), Activity.ROAR, Activity.FIGHT, Primal_Activities.STALK.get(), Activity.IDLE));
        }

        lion.setAggressive(brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET) || brain.hasMemoryValue(Primal_MemoryModuleTypes.IS_GRABBING.get()));

    }

    private static boolean canRoar(LionEntity lion) {
        return lion.canBeLeader();
    }

    private static Optional<? extends LivingEntity> findNearestValidAttackTargetDuringIdle(LionEntity lion) {
               //Don't attack while breeding
        return BehaviorUtils.isBreeding(lion)
                //Baby don't attack if it is not a following pet
                || (lion.isBaby() && !lion.isFollowing())
                //Maned lions use setRoarTarget instead
                || (!lion.isManeless() && !lion.isBaby())
                //If is a lion with leader, as long as not is following and doesn't have a target or grudge
                || (lion.hasLeader() && !lion.isFollowing() &&
                (lion.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isEmpty() && lion.getBrain().getMemory(Primal_MemoryModuleTypes.LAST_ATTACK_TARGET.get()).isEmpty()))?
                Optional.empty()
                : lion.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE);
    }

    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(LionEntity lion) {
        return BehaviorUtils.isBreeding(lion) || lion.isBaby() ? Optional.empty() : lion.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE);
    }
}