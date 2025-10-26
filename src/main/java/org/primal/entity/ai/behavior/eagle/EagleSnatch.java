package org.primal.entity.ai.behavior.eagle;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.EagleEntity;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.registry.Primal_Tags;

import java.util.List;
import java.util.Objects;

public class EagleSnatch extends Behavior<EagleEntity> {

    private long endTimestamp=10;

    private final int defaultDuration;

    public EagleSnatch(int defaultDuration) {
        super(ImmutableMap.of(
                        Primal_MemoryModuleTypes.IS_SNATCHING.get(), MemoryStatus.VALUE_PRESENT,
                        Primal_MemoryModuleTypes.IS_STUNNED.get(), MemoryStatus.VALUE_ABSENT),
                defaultDuration);

        this.defaultDuration = defaultDuration;
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, EagleEntity eagle) {
        return !eagle.getPassengers().isEmpty();
    }

    @Override
    protected void start(@NotNull ServerLevel level, @NotNull EagleEntity eagle, long gameTime) {
        Vec3 lookVec = eagle.getLookAngle().normalize();

        int finalDuration = this.defaultDuration;

        double strengthAddition=0;
        if(eagle.hasEffect(MobEffects.DAMAGE_BOOST)){
            int amplifier = Objects.requireNonNull(eagle.getEffect(MobEffects.DAMAGE_BOOST)).getAmplifier();
            //4 blocks extra for each strength level
            strengthAddition=(4*(amplifier+1));

            //2s extra for each strength level
            finalDuration=finalDuration+(2*(amplifier+1));
        }

        double verticalAddition = 20 + strengthAddition;
        this.endTimestamp = gameTime + (long)finalDuration;

        double forward = 10.0;
        // Calculate target position
        Vec3 targetPos =
                eagle.position()
                        .add(
                                lookVec.x * forward,
                                lookVec.y * forward + verticalAddition,
                                lookVec.z * forward
                        );

        //If it is not following you, tries to retrieve the animal to the baby if is from eagle_huntable
        if(!eagle.isFollowing() &&  eagle.getFirstPassenger()!=null && eagle.getFirstPassenger().getType().is(Primal_Tags.Entity.EAGLE_HUNTABLE)){
            List<EagleEntity> babyEagles=
                    level.getEntitiesOfClass(EagleEntity.class, eagle.getBoundingBox().inflate(10))
                            .stream().filter(babyEagle-> babyEagle.isBaby() && !babyEagle.isFollowing()).toList();

            if(!babyEagles.isEmpty()){
                BlockPos babyPos = babyEagles.get(level.random.nextInt(babyEagles.size())).getOnPos();

                targetPos= new Vec3(babyPos.getX(), babyPos.getY()+5, babyPos.getZ());
            }

        }

        BlockPos flyTo = BlockPos.containing(targetPos);

        eagle.stopMoving();

        eagle.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(flyTo));
        eagle.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(flyTo, 1.2f, 0));
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, @NotNull EagleEntity eagle, long gameTime) {
        return checkExtraStartConditions(level, eagle) && this.hasRequiredMemories(eagle);
    }

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull EagleEntity eagle, long gameTime) {
        LivingEntity target = !eagle.getPassengers().isEmpty() && eagle.getPassengers().getFirst() instanceof LivingEntity living? living: null;

        //If it doesn't have target
        if(target==null){
            stop(level, eagle, gameTime);
        }

        //To stop if reach the objective
        if(eagle.getBrain().getMemory(MemoryModuleType.WALK_TARGET).isPresent()){
            var pos =eagle.getBrain().getMemory(MemoryModuleType.WALK_TARGET).get();

            if(pos.getTarget().currentBlockPosition().distManhattan(eagle.getOnPos())<2.0f){
                stop(level, eagle, gameTime);
            }
        }
    }

    @Override
    protected void stop(@NotNull ServerLevel level, @NotNull EagleEntity eagle, long gameTime) {

        if(!eagle.getPassengers().isEmpty()){
            var passenger= eagle.getPassengers().getFirst();

            //To attack again if the dropped entity stills alive
            if(passenger instanceof LivingEntity livingEntity && livingEntity.getHealth()>0){
                eagle.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, livingEntity);
            }

            eagle.ejectPassengers();
        }
        eagle.setPose(Pose.STANDING);
        eagle.getBrain().eraseMemory(Primal_MemoryModuleTypes.IS_SNATCHING.get());
        eagle.getBrain().setMemoryWithExpiry(Primal_MemoryModuleTypes.IS_STUNNED.get(), true, 20);
        eagle.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_COOLING_DOWN, true, 10);
    }

    @Override
    protected boolean timedOut(long gameTime) {
        return gameTime > this.endTimestamp;
    }
}
