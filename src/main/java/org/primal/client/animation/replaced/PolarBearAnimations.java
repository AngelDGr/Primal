package org.primal.client.animation.replaced;

import net.minecraft.world.entity.animal.PolarBear;
import org.primal.entity.replaced.PolarBearReplaced;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.core.animation.RawAnimation;

public class PolarBearAnimations {
    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.polar_bear.idle");
    public static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.polar_bear.walk");
    public static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.polar_bear.run");
    public static final RawAnimation SWIM = RawAnimation.begin().thenLoop("animation.polar_bear.swim");

    public static final RawAnimation ATTACK = RawAnimation.begin().thenPlay("animation.polar_bear.attack");
    public static final RawAnimation ATTACK_SWIM = RawAnimation.begin().thenPlay("animation.polar_bear.attack_swim");

    public static AnimationController<PolarBearReplaced> mainController(PolarBearReplaced animatable) {
        return new AnimationController<>(animatable, state -> {
            state.getController().transitionLength(2);
            state.setControllerSpeed(1);
            PolarBear polarBear= animatable.getEntityFromState(state);

            //──────────────────────────────────── Triggered ────────────────────────────────────
            {
                if (state.getController().isPlayingTriggeredAnimation()) {
                    return PlayState.CONTINUE;
                }
            }

            //────────────────────────────────────  Movement ────────────────────────────────────
            {
                if (state.isMoving()) {
                    //Swim
                    if (polarBear.isInWater()) {
                        state.setControllerSpeed(state.getLimbSwingAmount() * 2f);
                        return state.setAndContinue(SWIM);
                    }
                    //Run
                    else if (polarBear.isAggressive()) {
                        state.setControllerSpeed(state.getLimbSwingAmount() * (polarBear.isBaby() ? 2 : 1f));
                        return state.setAndContinue(RUN);
                    }
                    //Walk
                    else {
                        state.setControllerSpeed(state.getLimbSwingAmount() * (polarBear.isBaby() ? 5 : 2));
                        return state.setAndContinue(WALK);
                    }
                }
            }

            //────────────────────────────────────    Idle   ────────────────────────────────────
            state.setControllerSpeed(polarBear.isInWater() && !state.isMoving()? 0.5f: 1f);
            return state.setAndContinue(polarBear.isInWater()? SWIM: IDLE);
        }).receiveTriggeredAnimations();
    }

    public static AnimationController<PolarBearReplaced> attackController(PolarBearReplaced animatable) {
        return new AnimationController<>(animatable, "attack", state -> PlayState.STOP)
                .triggerableAnim("attack", PolarBearAnimations.ATTACK)
                .triggerableAnim("attack_swim", PolarBearAnimations.ATTACK_SWIM);
    }
}
