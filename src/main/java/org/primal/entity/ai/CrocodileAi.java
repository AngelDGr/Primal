package org.primal.entity.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
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
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import org.primal.entity.ai.behavior.crocodile.*;
import org.primal.entity.ai.behavior.generic.TryFindWaterSurface;
import org.primal.entity.ai.behavior.generic.TryLayEggOnLandOrNest;
import org.primal.entity.animal.CrocodileEntity;
import org.primal.registry.*;
import org.primal.util.MiscUtil;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class CrocodileAi {

    private static final ImmutableList<SensorType<? extends Sensor<? super CrocodileEntity>>> SENSOR_TYPES = ImmutableList.of(
            SensorType.NEAREST_LIVING_ENTITIES,
            SensorType.HURT_BY,
            Primal_Sensors.NEAREST_BABY.get(),
            SensorType.NEAREST_PLAYERS,
            SensorType.NEAREST_ADULT,
            Primal_Sensors.CROCODILE_ATTACK_SENSOR.get(),
            Primal_Sensors.CROCODILE_TEMPTATIONS_SENSOR.get(),
            Primal_Sensors.CROCODILE_NEAREST_EGG.get(),
            Primal_Sensors.CROCODILE_NEAREST_REED.get());

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

            Primal_MemoryModuleTypes.WAS_BASKING.get(),

            Primal_MemoryModuleTypes.NEAREST_VISIBLE_BABY.get(),
            Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get(),
            MemoryModuleType.ADMIRING_ITEM,
            Primal_MemoryModuleTypes.IS_THRASHING.get(),
            Primal_MemoryModuleTypes.IS_EXPLODING.get(),
            Primal_MemoryModuleTypes.IS_STUNNED.get()
    );

    private static final UniformInt ADULT_FOLLOW_RANGE = UniformInt.of(5, 16);

    public static Predicate<ItemStack> getTemptations() {
        return CrocodileEntity::isMatingFood;
    }

    public static void initMemories(CrocodileEntity CrocodileEntity, RandomSource random) {
    }

    public static Brain.Provider<CrocodileEntity> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    public static Brain<?> makeBrain(Brain<CrocodileEntity> brain) {
        initCoreActivity(brain);
        initIdleActivity(brain);
        initFightActivity(brain);
        initThrashActivity(brain);
        initExplosionActivity(brain);
        initRetreatActivity(brain);
        initLayEggActivity(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    private static void initCoreActivity(Brain<CrocodileEntity> brain) {
        brain.addActivity(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new LookAtTargetSink(45, 90),
                        new MoveToTargetSink()
                )
        );
    }

    private static void initIdleActivity(Brain<CrocodileEntity> brain) {
        brain.addActivity(
                Activity.IDLE,
                10,
                ImmutableList.of(
                        StartAttacking.create(CrocodileAi::findNearestValidAttackTarget),
                        new AnimalMakeLove(Primal_Entities.CROCODILE.get()),
                        TryFindWaterSurface.create(16, 1),
                        new CrocodileGoesToCompass(),
                        new CrocodileGoesToEgg(),
                        SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60)),
                        new RunOne<>(
                                ImmutableList.of(
                                        Pair.of(new FollowTemptation(livingEntity -> 1.0F, livingEntity -> livingEntity.isBaby() ? 2.5 : 3.5), 4),
                                        Pair.of(BehaviorBuilder.triggerIf(crocodile -> !crocodile.isAggressive() || crocodile.getAirSupply()<3500,
                                                BabyFollowAdult.create(ADULT_FOLLOW_RANGE, 1.0F)), 1)
                                )
                        ),
                        new CrocodileGoesToReed(),
                        createIdleMovementBehaviors()
                )
        );
    }

    private static void initFightActivity(Brain<CrocodileEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.FIGHT,
                10,
                ImmutableList.of(
                        SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F),
                        new CrocodileStartAttack(20),
                        SetEntityLookTarget.create(50),
                        StopAttackingIfTargetInvalid.create(),
                        EraseMemoryIf.create(Animal::isInLove, MemoryModuleType.ATTACK_TARGET)),
                MemoryModuleType.ATTACK_TARGET);
    }

    private static final UniformInt RETREAT_DURATION = TimeUtil.rangeOfSeconds(5, 20);
    public static void wasHurtBy(CrocodileEntity crocodile, LivingEntity target) {
        Brain<CrocodileEntity> brain = crocodile.getBrain();
        brain.eraseMemory(MemoryModuleType.PACIFIED);
        brain.eraseMemory(MemoryModuleType.BREED_TARGET);
        if (crocodile.isBaby()) {
            retreatFromNearestTarget(crocodile, target);
            for(CrocodileEntity nearCrocodile: getNearestAdultCrocodiles(crocodile)){
                nearCrocodile.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, target);
            }

        } else {
            crocodile.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, target);
        }
    }

    private static List<CrocodileEntity> getNearestAdultCrocodiles(CrocodileEntity crocodile) {

        return crocodile.level().getEntitiesOfClass(CrocodileEntity.class, crocodile.getBoundingBox().inflate(30,5,30))
                .stream().filter(crocodile1 -> !crocodile1.isBaby()).toList();
    }

    private static void retreatFromNearestTarget(CrocodileEntity crocodile, LivingEntity target) {
        Brain<CrocodileEntity> brain = crocodile.getBrain();
        LivingEntity avoidTarget = BehaviorUtils.getNearestTarget(crocodile, brain.getMemory(MemoryModuleType.AVOID_TARGET), target);
        avoidTarget = BehaviorUtils.getNearestTarget(crocodile, brain.getMemory(MemoryModuleType.ATTACK_TARGET), avoidTarget);
        setAvoidTarget(crocodile, avoidTarget);
    }

    private static void setAvoidTarget(CrocodileEntity crocodile, LivingEntity target) {
        crocodile.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
        crocodile.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        crocodile.getBrain().setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, target, RETREAT_DURATION.sample(crocodile.level().random));
    }

    private static void initThrashActivity(Brain<CrocodileEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Primal_Activities.THRASH.get(),
                10,
                ImmutableList.of(new CrocodileThrash(100)),
                Primal_MemoryModuleTypes.IS_THRASHING.get());
    }

    private static void initExplosionActivity(Brain<CrocodileEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Primal_Activities.EXPLODING.get(),
                10,
                ImmutableList.of(new CrocodileExploding(40)),
                Primal_MemoryModuleTypes.IS_EXPLODING.get());
    }

    private static void initRetreatActivity(Brain<CrocodileEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.AVOID,
                10,
                ImmutableList.of(
                        SetWalkTargetAwayFrom.entity(MemoryModuleType.AVOID_TARGET, 1.2F, 20, false),
                        createIdleMovementBehaviors(),
                        SetEntityLookTargetSometimes.create(8.0F, UniformInt.of(30, 60)),
                        EraseMemoryIf.create(crocodile -> !crocodile.isBaby(), MemoryModuleType.AVOID_TARGET)
                ),
                MemoryModuleType.AVOID_TARGET
        );
    }

    private static void initLayEggActivity(Brain<CrocodileEntity> brain) {
        brain.addActivityWithConditions(
                Activity.LAY_SPAWN,
                ImmutableList.of(
                        Pair.of(1, TryLayEggOnLandOrNest.create(Primal_Blocks.CROCODILE_EGG.get(), MiscUtil.EGGS_3, 3, 1)),
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

    private static RunOne<CrocodileEntity> createIdleMovementBehaviors() {
        return new RunOne<>(
                ImmutableList.of(
                        Pair.of(new CrocodileBasking(20), 1),

                        Pair.of(BehaviorBuilder.triggerIf(Predicate.not(
                                crocodile -> crocodile.isAggressive() || (crocodile.isUnderWater() && crocodile.getAirSupply()>3500)),
                                RandomStroll.stroll(1f)), 1),

                        Pair.of(new DoNothing(30, 60), 1)));
    }

    public static void updateActivity(CrocodileEntity crocodile) {
        Brain<CrocodileEntity> brain = crocodile.getBrain();

        if(crocodile.isBaby())
            brain.setActiveActivityToFirstValid(ImmutableList.of(Primal_Activities.EXPLODING.get(), Activity.AVOID, Activity.IDLE));
        else
            brain.setActiveActivityToFirstValid(ImmutableList.of(Primal_Activities.EXPLODING.get(), Primal_Activities.THRASH.get(), Activity.FIGHT, Activity.LAY_SPAWN, Activity.IDLE));

        crocodile.setAggressive(brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET) || brain.hasMemoryValue(Primal_MemoryModuleTypes.IS_THRASHING.get()));
    }

    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(CrocodileEntity crocodile) {
        return BehaviorUtils.isBreeding(crocodile) || crocodile.isBaby() || crocodile.isPacified() ? Optional.empty() : crocodile.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE);
    }
}
