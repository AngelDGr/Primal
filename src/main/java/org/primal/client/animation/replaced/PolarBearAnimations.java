package org.primal.client.animation.replaced;

import net.minecraft.world.entity.animal.PolarBear;
import org.primal.entity.replaced.PolarBearReplaced;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

public class PolarBearAnimations {
    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.polar_bear.idle");
    public static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.polar_bear.walk");
    public static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.polar_bear.run");
    public static final RawAnimation SWIM = RawAnimation.begin().thenLoop("animation.polar_bear.swim");

    public static final RawAnimation ATTACK = RawAnimation.begin().thenPlay("animation.polar_bear.attack");

    public static AnimationController<PolarBearReplaced> mainController(PolarBearReplaced animatable) {
        return new AnimationController<>(animatable, state -> {
            PolarBear polarBear= animatable.getEntityFromState(state);

            state.setControllerSpeed(1);
            state.getController().transitionLength(0);

            if (state.getController().isPlayingTriggeredAnimation()) {
                state.getController().transitionLength(5);
                return PlayState.CONTINUE;
            }

            if (state.isMoving()) {
                //Swim
                if(polarBear.isInWater()){
                    state.setControllerSpeed(1.0f);
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

            state.setControllerSpeed(polarBear.isInWater() && !state.isMoving()? 0.5f: 1f);
            state.getController().transitionLength(20);
            return  state.setAndContinue(polarBear.isInWater()? SWIM: IDLE);

//            return state.setAndContinue(IDLE);
        });
    }
}
