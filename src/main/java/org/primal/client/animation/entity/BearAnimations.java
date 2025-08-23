package org.primal.client.animation.entity;

import net.minecraft.util.Mth;
import org.primal.entity.animal.BearEntity;

import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

public final class BearAnimations {
    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.grizzly_bear.idle");
    public static final RawAnimation IDLE_ALT = RawAnimation.begin().thenPlay("animation.grizzly_bear.idle_break_one");
    public static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.grizzly_bear.walk");
    public static final RawAnimation SWIM = RawAnimation.begin().thenLoop("animation.grizzly_bear.swim");
    public static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.grizzly_bear.run");
    public static final RawAnimation ROAR = RawAnimation.begin().thenPlay("animation.grizzly_bear.roar");
    public static final RawAnimation ATTACK = RawAnimation.begin().thenPlay("animation.grizzly_bear.attack");
    public static final RawAnimation ATTACK_ALT = RawAnimation.begin().thenPlay("animation.grizzly_bear.attack_flipped");
    public static final RawAnimation BEG = RawAnimation.begin().thenPlay("animation.grizzly_bear.beg_start").thenLoop("animation.grizzly_bear.beg");
    public static final RawAnimation SLEEP = RawAnimation.begin().thenLoop("animation.grizzly_bear.sleep");

    public static AnimationController<BearEntity> mainController(BearEntity animatable) {
        return new AnimationController<>(animatable, state -> {
            state.setControllerSpeed(1);
            state.getController().transitionLength(0);

            if (state.getController().isPlayingTriggeredAnimation()) {
                state.getController().transitionLength(5);
                return PlayState.CONTINUE;
            }

            switch (animatable.getPose()) {
                case SNIFFING:
                    if (state.getController().getCurrentAnimation() != null && state.getController().getCurrentAnimation().animation().name().equals(BEG.getAnimationStages().getFirst().animationName())) {
                        state.getController().transitionLength(5);
                    }
                    return state.setAndContinue(BEG);
                case CROAKING:
                    return state.setAndContinue(SLEEP);
                default:
                    break;
            }

            if (state.isMoving()) {
                //Swim
                if(animatable.isInWater()){
                    float healthFactor = Mth.clamp(animatable.getHealth() / animatable.getMaxHealth(), 0.1f, 1f);
                    state.setControllerSpeed(healthFactor);
                    return state.setAndContinue(SWIM);
                }
                //Run
                else if (animatable.isSprinting()) {
                    state.setControllerSpeed(state.getLimbSwingAmount() * (animatable.isBaby() ? 15 : 1f));
                    return state.setAndContinue(RUN);
                }
                //Walk
                else {
                    state.setControllerSpeed(state.getLimbSwingAmount() * (animatable.isBaby() ? 15 : 3));

                    return state.setAndContinue(WALK);
                }
            }

            state.setControllerSpeed(animatable.isInWater() && !state.isMoving()? 0.3f: 1f);
            state.getController().transitionLength(20);
            return  state.setAndContinue(animatable.isInWater()? SWIM: IDLE);
        });
    }
}
