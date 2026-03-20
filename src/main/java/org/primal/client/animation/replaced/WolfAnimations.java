package org.primal.client.animation.replaced;

import net.minecraft.world.entity.animal.Wolf;
import org.primal.entity.replaced.WolfReplaced;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

public class WolfAnimations {
    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.wolf.idle");
    public static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.wolf.walk");
    public static final RawAnimation SIT = RawAnimation.begin().thenLoop("animation.wolf.sit");
    public static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.wolf.run");
    public static final RawAnimation WET = RawAnimation.begin().thenPlay("animation.wolf.wet");

    public static AnimationController<WolfReplaced> mainController(WolfReplaced animatable) {
        return new AnimationController<>(animatable, state -> {
            state.getController().transitionLength(2);
            state.setControllerSpeed(1);
            Wolf wolf= animatable.getEntityFromState(state);
            double speed = wolf.getDeltaMovement().length();

            //──────────────────────────────────── Triggered ────────────────────────────────────
            {
                if (state.getController().isPlayingTriggeredAnimation()) {
                    return PlayState.CONTINUE;
                }
            }

            //────────────────────────────────────    Pose   ────────────────────────────────────
            {
                if (wolf.isInSittingPose()) {
                    state.getController().transitionLength(2);
                    state.setControllerSpeed(0.6f);
                    if (wolf.getOwner() != null && !wolf.getOwner().isSpectator()) {
                        float distance = wolf.distanceTo(wolf.getOwner());

                        // Tunables
                        float minSpeed = 0.6f;
                        float maxSpeed = 2.0f;
                        float maxDistance = 15.0f; // distance at which speed is minimal

                        // Normalize distance (0 = close, 1 = far)
                        float t = Math.min(distance / maxDistance, 1.0f);

                        // Invert so closer = faster
                        float tailSpeed = maxSpeed - (t * (maxSpeed - minSpeed));

                        state.setControllerSpeed(tailSpeed);
                    }
                    return state.setAndContinue(SIT);
                }
            }

            //────────────────────────────────────  Movement ────────────────────────────────────
            {
                if (state.isMoving()) {
                    //Run
                    if ((wolf.isAggressive() || speed > 0.18) && !wolf.isInWater()) {
                        state.setControllerSpeed(state.getLimbSwingAmount() * (wolf.isBaby() ? 2 : 1.5f));
                        return state.setAndContinue(RUN);
                    }
                    //Walk
                    else {
                        state.setControllerSpeed(state.getLimbSwingAmount() * (wolf.isBaby() ? 8 : 3));
                        return state.setAndContinue(WALK);
                    }
                }
            }

            //────────────────────────────────────    Idle   ────────────────────────────────────
            state.getController().transitionLength(4);
            state.setControllerSpeed(wolf.isInWater()? 0.3f: 1f);
            return state.setAndContinue(wolf.isInWater()? WALK : IDLE);
        }).receiveTriggeredAnimations()
                .triggerableAnim("wet", WolfAnimations.WET);
    }

}
