package org.primal.entity.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;
import org.primal.entity.ai.behavior.generic.TriesReturnToWater;
import org.primal.entity.ai.behavior.shark.SharkFollowConduitPlayer;
import org.primal.entity.ai.behavior.shark.SharkGoesToConduit;
import org.primal.entity.ai.behavior.shark.SharkJumpOutWater;
import org.primal.entity.animal.SharkEntity;
import org.primal.registry.Primal_Activities;
import org.primal.registry.Primal_Advancements;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.registry.Primal_Sensors;

import java.util.Optional;

public class SharkAi {

    protected static final ImmutableList<SensorType<? extends Sensor<? super SharkEntity>>> SENSOR_TYPES = ImmutableList.of(
            Primal_Sensors.GENERIC_ATTACK_SENSOR.get(),
            SensorType.NEAREST_PLAYERS,
            SensorType.HURT_BY,
            SensorType.FROG_TEMPTATIONS,
            Primal_Sensors.SHARK_NEAR_CONDUIT_PLAYER.get(),
            Primal_Sensors.SHARK_NEAREST_CONDUIT.get()
    );
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.WALK_TARGET,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.PATH,
            MemoryModuleType.ATTACK_TARGET,
            MemoryModuleType.ATTACK_COOLING_DOWN,
            MemoryModuleType.NEAREST_ATTACKABLE,

            MemoryModuleType.HURT_BY,
            MemoryModuleType.HURT_BY_ENTITY,
            MemoryModuleType.BREED_TARGET,

            MemoryModuleType.IS_PANICKING,
            MemoryModuleType.TEMPTATION_COOLDOWN_TICKS,
            MemoryModuleType.IS_TEMPTED,
            MemoryModuleType.TEMPTING_PLAYER,

            MemoryModuleType.AVOID_TARGET,
            MemoryModuleType.HAS_HUNTING_COOLDOWN,
            Primal_MemoryModuleTypes.NEAREST_CONDUIT_PLAYER.get(),
            Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get(),
            Primal_MemoryModuleTypes.WAS_TOWARDS_IMPORTANT_BLOCK.get(),
            MemoryModuleType.ADMIRING_ITEM,
            Primal_MemoryModuleTypes.LAST_ATTACK_TARGET.get()
    );

    private static final UniformInt ADULT_FOLLOW_RANGE = UniformInt.of(5, 16);

//    public static Predicate<ItemStack> getTemptations() {
//        return SharkEntity::isMatingFood;
//    }

    private static void initMemories(SharkEntity SharkEntity, RandomSource random) {
    }

    public static Brain.Provider<SharkEntity> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    public static Brain<?> makeBrain(Brain<SharkEntity> brain) {
        initCoreActivity(brain);
        initIdleActivity(brain);
        initFightActivity(brain);
        initRetreatActivity(brain);
        initBeachedActivity(brain);
        initJockeyActivity(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    private static void initCoreActivity(Brain<SharkEntity> brain) {
        brain.addActivity(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new LookAtTargetSink(45, 90),
                        new MoveToTargetSink(),
                        new TriesReturnToWater(),
                        EraseMemoryIf.create(shark ->
                                shark.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isPresent()
                                && !shark.canAttack(shark.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get()),
                                MemoryModuleType.ATTACK_TARGET)
                )
        );
    }

    private static void initIdleActivity(Brain<SharkEntity> brain) {
        brain.addActivity(
                Activity.IDLE,
                10,
                ImmutableList.of(
                        StartAttacking.create(SharkAi::findNearestValidAttackTarget),
                        new SharkFollowConduitPlayer(),
                        new SharkGoesToConduit(),
                        SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60)),
                        new FollowTemptation(p_218740_ -> 1.25F),
                        createIdleMovementBehaviors()
                )
        );
    }

    private static void initBeachedActivity(Brain<SharkEntity> brain) {
        brain.addActivity(
                Primal_Activities.BEACHED.get(),
                10,
                ImmutableList.of(
                        createIdleMovementBehaviors()
                )
        );
    }

    private static void initFightActivity(Brain<SharkEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.FIGHT,
                10,
                ImmutableList.of(
                        new SharkJumpOutWater(),
                        SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F),
                        MeleeAttack.create(20),
                        SetEntityLookTarget.create(50),
                        StopAttackingIfTargetInvalid.create()),
                MemoryModuleType.ATTACK_TARGET);
    }

    private static void initRetreatActivity(Brain<SharkEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.AVOID,
                10,
                ImmutableList.of(
                        SetWalkTargetAwayFrom.entity(MemoryModuleType.AVOID_TARGET, 1.0F, 10, false),
                        createIdleMovementBehaviors(),
                        SetEntityLookTargetSometimes.create(8.0F, UniformInt.of(30, 60))
                ),
                MemoryModuleType.AVOID_TARGET
        );
    }

    private static void initJockeyActivity(Brain<SharkEntity> brain) {
        brain.addActivity(
                Primal_Activities.JOCKEY.get(),
                10,
                ImmutableList.of(
                        new RandomLookAround(UniformInt.of(150, 250), 30.0F, 0.0F, 0.0F)
                )
        );
    }

    private static RunOne<SharkEntity> createIdleMovementBehaviors() {
        return new RunOne<>(
                ImmutableList.of(
                        Pair.of(RandomStroll.swim(1f), 1)));
    }

    public static void wasHurtBy(SharkEntity bear, LivingEntity target) {
        Brain<SharkEntity> brain = bear.getBrain();
        brain.eraseMemory(MemoryModuleType.BREED_TARGET);
        if (bear.isBaby()) {
            retreatFromNearestTarget(bear, target);
        } else {
            bear.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, target);
        }
    }

    private static final UniformInt RETREAT_DURATION = TimeUtil.rangeOfSeconds(5, 20);
    private static void retreatFromNearestTarget(SharkEntity bear, LivingEntity target) {
        Brain<SharkEntity> brain = bear.getBrain();
        LivingEntity avoidTarget = BehaviorUtils.getNearestTarget(bear, brain.getMemory(MemoryModuleType.AVOID_TARGET), target);
        avoidTarget = BehaviorUtils.getNearestTarget(bear, brain.getMemory(MemoryModuleType.ATTACK_TARGET), avoidTarget);
        setAvoidTarget(bear, avoidTarget);
    }

    private static void setAvoidTarget(SharkEntity bear, LivingEntity target) {
        bear.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
        bear.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        bear.getBrain().setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, target, RETREAT_DURATION.sample(bear.level().random));
    }

    public static void updateActivity(SharkEntity shark) {
        Brain<SharkEntity> brain = shark.getBrain();

        if(shark.isBaby()){
            brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.AVOID, Activity.IDLE));
        }
        else if(shark.shouldBeBeached()) {
            brain.setActiveActivityToFirstValid(ImmutableList.of(Primal_Activities.BEACHED.get()));
        } else {

            if(shark.isSharkJockey())
                brain.setActiveActivityToFirstValid(ImmutableList.of(Primal_Activities.JOCKEY.get()));
            else
                brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.AVOID, Activity.FIGHT, Activity.IDLE));

            shark.setAggressive(brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
        }

        //Trigger advancement
        triggerSurviveSharkAdvancement(brain, shark);
    }

    private static void triggerSurviveSharkAdvancement(Brain<SharkEntity> brain, SharkEntity shark){
        if (brain.getMemory(MemoryModuleType.ATTACK_TARGET).isPresent()) {
            brain.setMemoryWithExpiry(Primal_MemoryModuleTypes.LAST_ATTACK_TARGET.get(), brain.getMemory(MemoryModuleType.ATTACK_TARGET).get(), 200L);
        }

        if(brain.getMemory(Primal_MemoryModuleTypes.LAST_ATTACK_TARGET.get()).isPresent()){
            LivingEntity last_target= brain.getMemory(Primal_MemoryModuleTypes.LAST_ATTACK_TARGET.get()).get();

            if(last_target instanceof ServerPlayer player && !shark.canAttack(player) && player.getHealth()>=player.getMaxHealth() && player.getLastHurtByMob() instanceof SharkEntity){
                Primal_Advancements.SURVIVE_SHARK.trigger(player);
            }
        }
    }

    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(SharkEntity shark) {
        return BehaviorUtils.isBreeding(shark) ? Optional.empty() : shark.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE) ;
    }
}
