package org.primal.client.animation.entity;

import org.primal.entity.animal.SharkEntity;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;

public class SharkAnimations {

    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.shark.idle");
    public static final RawAnimation SWIM = RawAnimation.begin().thenLoop("animation.shark.swim");
    public static final RawAnimation SHAKE = RawAnimation.begin().thenLoop("animation.shark.shake");
    public static final RawAnimation ATTACK = RawAnimation.begin().thenPlay("animation.grizzly_bear.attack");
    public static final RawAnimation ATTACK_ALT = RawAnimation.begin().thenPlay("animation.grizzly_bear.attack_flipped");
    public static final RawAnimation BEACHED = RawAnimation.begin().thenLoop("animation.shark.beached");

    public static AnimationController<SharkEntity> mainController(SharkEntity animatable) {
        return new AnimationController<>(animatable, state -> {

            state.getController().transitionLength(0);

            if (state.isMoving()) {
                if (animatable.isInWater()) {
                    state.setControllerSpeed(state.getLimbSwingAmount() * (animatable.isBaby() ? 15 : 4f));
                    return state.setAndContinue(SWIM);
                } else {
                    state.setControllerSpeed(1.5f);
                    return state.setAndContinue(SHAKE);
                }
            }

            state.getController().transitionLength(2);
            state.setControllerSpeed(!animatable.isInWater() && animatable.shouldBeBeached()? 1f: animatable.isInWater()? 0.6f: 1f);
            return  state.setAndContinue(animatable.isInWater()? IDLE: !animatable.isInWater() && !animatable.shouldBeBeached()? SHAKE: BEACHED);
        });
    }
}
