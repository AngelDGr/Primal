package org.primal.client.animation.replaced;

import net.minecraft.world.entity.animal.Fox;
import org.primal.entity.replaced.FoxReplaced;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

public class FoxAnimations {
    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.fox.idle");
    public static final RawAnimation SNEAK = RawAnimation.begin().thenLoop("animation.fox.sneak");
    public static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.fox.walk");
    public static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.fox.run");
    public static final RawAnimation SIT = RawAnimation.begin().thenLoop("animation.fox.sit");
    public static final RawAnimation SLEEP = RawAnimation.begin().thenLoop("animation.fox.sleep");
    public static final RawAnimation WIGGLE = RawAnimation.begin().thenLoop("animation.fox.wiggle");

    public static final RawAnimation POUNCE = RawAnimation.begin().thenLoop("animation.fox.pounce");
    public static final RawAnimation STUCK = RawAnimation.begin().thenLoop("animation.fox.stuck");
    public static final RawAnimation UNSTUCK = RawAnimation.begin().thenPlay("animation.fox.unstuck");

    public static AnimationController<FoxReplaced> mainController(FoxReplaced animatable) {
        return new AnimationController<>(animatable, state -> {
            state.getController().transitionLength(2);
            state.setControllerSpeed(1);
            Fox fox= animatable.getEntityFromState(state);
            double speed = fox.getDeltaMovement().length();

            //──────────────────────────────────── Triggered ────────────────────────────────────
            {
                if (state.getController().isPlayingTriggeredAnimation()) {
                    state.getController().transitionLength(0);
                    return PlayState.CONTINUE;
                }
            }

            //────────────────────────────────────    Pose   ────────────────────────────────────
            {
                if (fox.isFaceplanted()) {
                    return state.setAndContinue(STUCK);
                } else if (fox.isSitting()) {
                    return state.setAndContinue(SIT);
                } else if (fox.isPouncing() && !fox.onGround()) {
                    return state.setAndContinue(POUNCE);
                } else if (fox.isSleeping()) {
                    return state.setAndContinue(SLEEP);
                }
            }

            //────────────────────────────────────  Movement ────────────────────────────────────
            {
                if (state.isMoving()) {
                    if (fox.isCrouching() || fox.isInterested()) {
                        state.getController().transitionLength(5);
                        state.setControllerSpeed(state.getLimbSwingAmount() * (fox.isBaby() ? 2 : 1f));
                        return state.setAndContinue(SNEAK);
                    } else if ((fox.isAggressive() || speed > 0.18) && !fox.isInWater()) {
                        state.getController().transitionLength(2);
                        state.setControllerSpeed(state.getLimbSwingAmount() * (fox.isBaby() ? 2 : 1f));
                        return state.setAndContinue(RUN);
                    }
                    else {
                        state.getController().transitionLength(2);
                        state.setControllerSpeed(state.getLimbSwingAmount() * (fox.isBaby() ? 8 : 5));
                        return state.setAndContinue(WALK);
                    }
                }
            }

            //────────────────────────────────────    Idle   ────────────────────────────────────
            state.setControllerSpeed(fox.isInWater()? 0.3f: 1f);
            return state.setAndContinue(fox.isInWater()? WALK :IDLE);
        }).receiveTriggeredAnimations()
                .triggerableAnim("unstuck", FoxAnimations.UNSTUCK);
    }
}
