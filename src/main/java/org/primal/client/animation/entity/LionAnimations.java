package org.primal.client.animation.entity;

import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import org.primal.entity.animal.LionEntity;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.core.animation.RawAnimation;

public class LionAnimations {

    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.lion.idle");
    public static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.lion.walk");
    public static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.lion.run");
    public static final RawAnimation STALK = RawAnimation.begin().thenLoop("animation.lion.stalk");
    public static final RawAnimation STALK_WALK = RawAnimation.begin().thenLoop("animation.lion.stalk_walk");

    public static final RawAnimation MAUL = RawAnimation.begin().thenLoop("animation.lion.maul");
    public static final RawAnimation EAT = RawAnimation.begin().thenLoop("animation.lion.eat");
    public static final RawAnimation POUNCE = RawAnimation.begin().thenPlay("animation.lion.pounce");

    public static final RawAnimation LAY_START = RawAnimation.begin().thenPlay("animation.lion.lay_start");
    public static final RawAnimation LAY = RawAnimation.begin().thenLoop("animation.lion.lay");
    public static final RawAnimation LAY_END = RawAnimation.begin().thenPlay("animation.lion.lay_end");

    public static final RawAnimation ROAR = RawAnimation.begin().thenPlay("animation.lion.roar");

    public static AnimationController<LionEntity> mainController(LionEntity animatable) {
        return new AnimationController<>(animatable, state -> {
            state.getController().transitionLength(animatable.tickCount==0? 0: 8);
            state.setControllerSpeed(1f);

            //──────────────────────────────────── Triggered ────────────────────────────────────
            {
                if (state.getController().isPlayingTriggeredAnimation()) {
                    state.setControllerSpeed(speedsUpEndAnimation(animatable, state) ? 3f : 1f);

                    //Pounce is faster
                    if (state.getController().getTriggeredAnimation() != null && state.getController().getTriggeredAnimation().equals(POUNCE))
                        state.setControllerSpeed(2f);

                    return PlayState.CONTINUE;
                }
            }

            //────────────────────────────────────    Pose   ────────────────────────────────────
            {
                switch (animatable.getPose()) {
                    case SPIN_ATTACK:
                        return state.setAndContinue(MAUL);
                    case SITTING, DIGGING:
                        state.getController().transitionLength(0);
                        return state.setAndContinue(LAY);
                }
            }

            //────────────────────────────────────  Movement ────────────────────────────────────
            {
                if (state.isMoving()) {
                    if (animatable.isCrouching()) {
                        state.setControllerSpeed(state.getLimbSwingAmount() * (animatable.isBaby() ? 3f : 2f));
                        return state.setAndContinue(STALK_WALK);
                    } else if (animatable.isSprinting()) {
                        state.setControllerSpeed(state.getLimbSwingAmount() * (animatable.isBaby() ? 3f : 1.5f));
                        return state.setAndContinue(RUN);
                    } else {
                        state.setControllerSpeed(state.getLimbSwingAmount() * (animatable.isBaby() ? 6f : 2.25f));
                        return state.setAndContinue(WALK);
                    }
                }
            }

            //────────────────────────────────────    Idle   ────────────────────────────────────
            state.setControllerSpeed(animatable.isInWater()? 0.3f: 1.0f);
            return state.setAndContinue(animatable.isInWater()? WALK: animatable.isCrouching()? STALK: IDLE);
        }).receiveTriggeredAnimations()
                .triggerableAnim("roar", LionAnimations.ROAR)
                .triggerableAnim("pounce", LionAnimations.POUNCE)
                .triggerableAnim("lay_start", LionAnimations.LAY_START)
                .triggerableAnim("lay_end", LionAnimations.LAY_END);
    }

    private static boolean speedsUpEndAnimation(LionEntity animatable, AnimationState<LionEntity> state){
        return state.getController().getTriggeredAnimation() !=null && state.getController().getTriggeredAnimation().equals(LAY_END)
                && (animatable.isAggressive()
                || animatable.getBrain().getMemory(MemoryModuleType.AVOID_TARGET).isPresent()
                || animatable.getBrain().getMemory(MemoryModuleType.ROAR_TARGET).isPresent()
                || animatable.isVehicle()
                || animatable.isStalking());
    }
}
