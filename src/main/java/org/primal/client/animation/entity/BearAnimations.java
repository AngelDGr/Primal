package org.primal.client.animation.entity;

import net.minecraft.util.Mth;
import org.primal.entity.animal.BearEntity;

import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

public final class BearAnimations {
    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.bear.idle");
    public static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.bear.walk");
    public static final RawAnimation SWIM = RawAnimation.begin().thenLoop("animation.bear.swim");
    public static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.bear.run");
    public static final RawAnimation ROAR = RawAnimation.begin().thenPlay("animation.bear.roar");
    public static final RawAnimation ROAR_SWIM = RawAnimation.begin().thenPlay("animation.bear.roar_swim");
    public static final RawAnimation ATTACK = RawAnimation.begin().thenPlay("animation.bear.attack");
    public static final RawAnimation ATTACK_ALT = RawAnimation.begin().thenPlay("animation.bear.attack_flipped");

    public static final RawAnimation BEG = RawAnimation.begin().thenPlay("animation.bear.beg_start").thenLoop("animation.bear.beg");
    public static final RawAnimation BEG_END = RawAnimation.begin().thenPlay("animation.bear.beg_end");

    public static final RawAnimation SLEEP_START = RawAnimation.begin().thenPlay("animation.bear.sleep_start");
    public static final RawAnimation SLEEP = RawAnimation.begin().thenLoop("animation.bear.sleep");
    public static final RawAnimation SLEEP_END = RawAnimation.begin().thenPlay("animation.bear.sleep_end");

    public static final RawAnimation DANCE = RawAnimation.begin().thenPlay("animation.bear.dance");

    public static AnimationController<BearEntity> mainController(BearEntity animatable) {
        return new AnimationController<>(animatable, state -> {
            state.getController().transitionLength(animatable.tickCount==0? 0: 2);
            state.setControllerSpeed(1f);

            //──────────────────────────────────── Triggered ────────────────────────────────────
            {
                if (state.getController().isPlayingTriggeredAnimation()) {
                    return PlayState.CONTINUE;
                }
            }

            //────────────────────────────────────    Pose   ────────────────────────────────────
            {
                switch (animatable.getPose()) {
                    case SNIFFING:
                        state.getController().transitionLength(0);
                        return state.setAndContinue(BEG);
                    case CROAKING:
                        return state.setAndContinue(SLEEP);
                }
            }

            //────────────────────────────────────  Movement ────────────────────────────────────
            {
                if (state.isMoving()) {
                    //Swim
                    if (animatable.isInWater()) {
                        float speed = (float) animatable.getDeltaMovement().length();
                        float healthFactor = Mth.clamp((speed * 8f) + (animatable.getHealth() / animatable.getMaxHealth()), 0.1f, 10f);
                        state.setControllerSpeed(healthFactor);
                        return state.setAndContinue(SWIM);
                    }
                    //Run
                    else if (animatable.isSprinting()) {
                        state.setControllerSpeed(state.getLimbSwingAmount() * (animatable.isBaby() ? 1.5f : 1f));
                        return state.setAndContinue(RUN);
                    }
                    //Walk
                    else {
                        state.setControllerSpeed(state.getLimbSwingAmount() * (animatable.isBaby() ? 4f : 3));
                        return state.setAndContinue(WALK);
                    }
                }
            }

            //────────────────────────────────────    Idle   ────────────────────────────────────
            state.setControllerSpeed(animatable.isInWater()? 0.3f: 1f);
            return  state.setAndContinue(animatable.isInWater()? SWIM: IDLE);
        }).receiveTriggeredAnimations()
                .triggerableAnim("roar", BearAnimations.ROAR)
                .triggerableAnim("roar_swim", BearAnimations.ROAR_SWIM)
                .triggerableAnim("beg_end", BearAnimations.BEG_END)
                .triggerableAnim("sleep_start", BearAnimations.SLEEP_START)
                .triggerableAnim("sleep_end", BearAnimations.SLEEP_END);
    }

    public static AnimationController<BearEntity> attackController(BearEntity animatable) {
        return new AnimationController<>(animatable, "attack", state -> PlayState.STOP)
                .triggerableAnim("attack", BearAnimations.ATTACK)
                .triggerableAnim("attack2", BearAnimations.ATTACK_ALT);
    }
}
