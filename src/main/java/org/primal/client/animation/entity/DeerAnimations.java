package org.primal.client.animation.entity;

import org.primal.entity.animal.DeerEntity;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

public class DeerAnimations {

    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.deer.idle");
    public static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.deer.walk");
    public static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.deer.run");
    public static final RawAnimation JUMP = RawAnimation.begin().thenLoop("animation.deer.jump");

    public static final RawAnimation LOOK = RawAnimation.begin().thenPlay("animation.deer.look");
    public static final RawAnimation EAT = RawAnimation.begin().thenPlay("animation.deer.eat");

    public static AnimationController<DeerEntity> mainController(DeerEntity animatable) {
        return new AnimationController<>(animatable, state -> {
            state.getController().transitionLength(8);
            state.setControllerSpeed(1f);

            //──────────────────────────────────── Triggered ────────────────────────────────────
            {
                if (state.getController().isPlayingTriggeredAnimation()) {
                    return PlayState.CONTINUE;
                }
            }

            //────────────────────────────────────    Pose   ────────────────────────────────────
            {
                if(animatable.isJumping()){
                    return state.setAndContinue(JUMP);
                }
            }

            //────────────────────────────────────  Movement ────────────────────────────────────
            {
                if (state.isMoving()) {
                    if (animatable.isSprinting()) {
                        state.setControllerSpeed(state.getLimbSwingAmount() * (animatable.isBaby() ? 1.5f : 1.5f));
                        return state.setAndContinue(RUN);
                    } else {
                        state.setControllerSpeed(state.getLimbSwingAmount() * (animatable.isBaby() ? 6f : 3.0f));
                        return state.setAndContinue(WALK);
                    }
                }
            }

            //────────────────────────────────────    Idle   ────────────────────────────────────
            state.setControllerSpeed(animatable.isInWater()? 0.3f: 1.0f);
            return state.setAndContinue(animatable.isInWater()? WALK: IDLE);
        }).receiveTriggeredAnimations()
                .triggerableAnim("eat", DeerAnimations.EAT)
                .triggerableAnim("look", DeerAnimations.LOOK);
    }
}
