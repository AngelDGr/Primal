package org.primal.util.mob_types;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface MobWithTransitionablePoseAnimations {

    private Entity self() {
        return (Entity) this;
    }

    Map<String, TransitionablePoseAnimation> transitionableAnimations();

    default void resetPoses(){
        for(Map.Entry<String, TransitionablePoseAnimation> entry : transitionableAnimations().entrySet())
            resetLastPoseChangeTickToFullStand(self(), self().level().getGameTime(), entry.getValue().lastPoseTick, entry.getValue().stopTransitionLength);
    }

    default void addAdditionalSaveDataTransitionablePoseAnimations(@NotNull CompoundTag compound) {
        for(Map.Entry<String, TransitionablePoseAnimation> entry : transitionableAnimations().entrySet())
            addAdditionalSaveData(compound, entry.getKey(), self().getEntityData().get(entry.getValue().lastPoseTick()));
    }

    default void readAdditionalSaveDataTransitionablePoseAnimations(@NotNull CompoundTag compound) {
        for(Map.Entry<String, TransitionablePoseAnimation> entry : transitionableAnimations().entrySet())
            readAdditionalSaveData(compound, entry.getKey(), self(), entry.getValue().pose(), entry.getValue().lastPoseTick());
    }

    default void transitionablePoseAnimationsSetupAnimationStates(){
        for(Map.Entry<String, TransitionablePoseAnimation> entry : transitionableAnimations().entrySet())
            entry.getValue().setupAnimationStates(self());
    }

    private void addAdditionalSaveData(@NotNull CompoundTag compound, String name, long lastPoseTick) {
        compound.putLong("LastPoseTick" + name, lastPoseTick);
    }

    private void readAdditionalSaveData(@NotNull CompoundTag compound, String name, Entity entity, Pose pose, EntityDataAccessor<Long> tickValue) {
        long l = compound.getLong("LastPoseTick" + name);
        if (l < 0L) {
            entity.setPose(pose);
        }
        resetLastPoseChangeTick(entity, l, tickValue);
    }

    default void startAnimation(String animationName){
        var animation = this.transitionableAnimations().get(animationName);
        if(animation != null){
            if(!animation.conditionToTrigger){
                self().setPose(animation.pose());
                self().gameEvent(GameEvent.ENTITY_ACTION);
                self().getEntityData().set(animation.lastPoseTick, -self().level().getGameTime());
            }
        }
    }

    default void stopAnimation(String animationName){
        var animation = this.transitionableAnimations().get(animationName);
        if(animation != null){
            if(animation.conditionToTrigger){
                self().setPose(Pose.STANDING);
                self().gameEvent(GameEvent.ENTITY_ACTION);
                self().getEntityData().set(animation.lastPoseTick, self().level().getGameTime());
            }
        }
    }

    static void resetLastPoseChangeTickToFullStand(Entity entity, long lastPoseChangedTick, EntityDataAccessor<Long> tickValue, long endAnimationDuration) {
        resetLastPoseChangeTick(entity, Math.max(0L, lastPoseChangedTick - endAnimationDuration - 1L), tickValue);
    }

    static void resetLastPoseChangeTick(Entity entity, long lastPoseChangeTick, EntityDataAccessor<Long> tickValue){
        entity.getEntityData().set(tickValue, lastPoseChangeTick);
    }

    default boolean isOnPoseTransition(){
        boolean result=false;
        for(Map.Entry<String, TransitionablePoseAnimation> entry : transitionableAnimations().entrySet())
            if(entry.getValue().isInStartPoseTransition(self()) || entry.getValue().isInStopPoseTransition(self()))
                result=true;
        return result;
    }

    default boolean isOnPoseAnimation(){
        boolean result=false;
        for(Map.Entry<String, TransitionablePoseAnimation> entry : transitionableAnimations().entrySet())
            if(self().getPose().equals(entry.getValue().pose()))
                result=true;

        return result;
    }

    record TransitionablePoseAnimation(Pose pose,
                                       boolean conditionToTrigger,
                                       EntityDataAccessor<Long> lastPoseTick,
                                       AnimationState startAnimState, AnimationState animState, AnimationState stopAnimState,
                                       long startTransitionLength, long stopTransitionLength){

        public TransitionablePoseAnimation(boolean conditionToTrigger,
                                           Pose pose,
                                           EntityDataAccessor<Long> lastPoseTick,
                                           AnimationState startAnimState, AnimationState animState, AnimationState stopAnimState,
                                           long transitionLength) {
            this(pose, conditionToTrigger, lastPoseTick, startAnimState, animState, stopAnimState, transitionLength, transitionLength);
        }

        public void setupAnimationStates(Entity entity) {
            var poseTime = getPoseTime(entity, lastPoseTick);
            if (conditionToTrigger) {
                stopAnimState.stop();
                if (isStartingAnim(poseTime, startTransitionLength)) {
                    startAnimState.startIfStopped(entity.tickCount);
                    animState.stop();
                } else {
                    startAnimState.stop();
                    animState.startIfStopped(entity.tickCount);
                }
            } else {
                startAnimState.stop();
                animState.stop();
                stopAnimState.animateWhen(isInPoseTransition(poseTime, stopTransitionLength) && poseTime >= 0L, entity.tickCount);
            }
        }

        public boolean isInStopPoseTransition(Entity entity){
            long i = this.getPoseTime(entity, this.lastPoseTick);
            return i < stopTransitionLength;
        }

        public boolean isInStartPoseTransition(Entity entity){
            long i = this.getPoseTime(entity, this.lastPoseTick);
            return i < startTransitionLength;
        }

        public boolean isStartingAnim(long poseTime, long transitionLength) {
            return conditionToTrigger && poseTime < transitionLength && poseTime >= 0L;
        }

        public boolean isInPoseTransition(long poseTime, long transitionLength) {
            return poseTime < (transitionLength);
        }

        public long getPoseTime(Entity entity, EntityDataAccessor<Long> longEntityDataAccessor) {
            return entity.level().getGameTime() - Math.abs(entity.getEntityData().get(longEntityDataAccessor));
        }
    }

}