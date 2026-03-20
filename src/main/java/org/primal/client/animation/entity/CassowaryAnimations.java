package org.primal.client.animation.entity;

import net.minecraft.world.entity.Pose;
import org.primal.entity.animal.CassowaryEntity;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

public class CassowaryAnimations {

    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.cassowary.idle");
    public static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.cassowary.walk");
    public static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.cassowary.run");
    public static final RawAnimation ATTACK = RawAnimation.begin().thenPlay("animation.cassowary.attack");
    public static final RawAnimation FRUIT_PICK = RawAnimation.begin().thenPlay("animation.cassowary.fruit_pick");

    public static final RawAnimation SIT = RawAnimation.begin().thenLoop("animation.cassowary.sit");

    public static AnimationController<CassowaryEntity> mainController(CassowaryEntity animatable) {
        return new AnimationController<>(animatable, state -> {
            state.getController().transitionLength(animatable.tickCount==0? 0: 2);
            state.setControllerSpeed(1f);

            //──────────────────────────────────── Triggered ────────────────────────────────────
            {
                if (state.getController().isPlayingTriggeredAnimation()) {
                    state.setControllerSpeed(1f);
                    return PlayState.CONTINUE;
                }
            }

            //────────────────────────────────────    Pose   ────────────────────────────────────
            {
                if (animatable.getPose() == Pose.SITTING) {
                    state.setControllerSpeed(1f);
                    return state.setAndContinue(SIT);
                }
            }

            //────────────────────────────────────  Movement ────────────────────────────────────
            {
                if (state.isMoving()) {
                    if (animatable.isInWater()) {
                        state.setControllerSpeed(state.getLimbSwingAmount() * (animatable.isBaby() ? 8f : 0.5f));
                        return state.setAndContinue(WALK);
                    } else if (animatable.isSprinting()) {
                        state.setControllerSpeed(state.getLimbSwingAmount() * (animatable.isBaby() ? 4f : 1.0f));
                        return state.setAndContinue(RUN);
                    } else {
                        state.setControllerSpeed(state.getLimbSwingAmount() * (animatable.isBaby() ? 6f : 2f));
                        return state.setAndContinue(WALK);
                    }
                }
            }

            //────────────────────────────────────    Idle   ────────────────────────────────────
            state.setControllerSpeed(animatable.isInWater()? 0.3f: 1f);
            return state.setAndContinue(animatable.isInWater()? WALK: IDLE);
        }).receiveTriggeredAnimations()
                .triggerableAnim("attack", ATTACK)
                .triggerableAnim("pick_fruit", FRUIT_PICK);
    }
}
