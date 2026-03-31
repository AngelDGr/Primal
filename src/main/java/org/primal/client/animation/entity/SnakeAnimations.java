package org.primal.client.animation.entity;

import org.primal.entity.animal.SnakeEntity;
import org.primal.util.Primal_Util;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.core.animation.RawAnimation;

public final class SnakeAnimations {
    public static final RawAnimation IDLE = RawAnimation.begin().thenPlay("animation.snake.idle");
    public static final RawAnimation IDLE_CAUTIOUS = RawAnimation.begin().thenPlay("animation.snake.idle_cautious");

    public static final RawAnimation SLITHER_START = RawAnimation.begin().thenPlay("animation.snake.slither_start");
    public static final RawAnimation SLITHER_END = RawAnimation.begin().thenPlay("animation.snake.slither_end");

    public static final RawAnimation BITE_SLITHERING = RawAnimation.begin().thenPlay("animation.snake.bite_slithering");
    public static final RawAnimation BITE_STANDING = RawAnimation.begin().thenPlay("animation.snake.bite_standing");
    public static final RawAnimation BITE_WRAPPED = RawAnimation.begin().thenPlay("animation.snake.bite_wrapped");
    public static final RawAnimation TONGUE_FLICK = RawAnimation.begin().thenPlay("animation.snake.tongue_flick");

    public static final RawAnimation SLITHER = RawAnimation.begin().thenPlay("animation.snake.slither");
    public static final RawAnimation SLITHER_STANDING = RawAnimation.begin().thenPlay("animation.snake.slither_standing");
    public static final RawAnimation SLITHER_CAUTIOUS = RawAnimation.begin().thenPlay("animation.snake.slither_cautious");

    public static final RawAnimation EAT = RawAnimation.begin().thenPlay("animation.snake.eat");
    public static final RawAnimation SIT = RawAnimation.begin().thenLoop("animation.snake.sit");

    public static final RawAnimation WRAPPED = RawAnimation.begin().thenPlay("animation.snake.wrapped");

    public static final RawAnimation DANCE = RawAnimation.begin().thenPlay("animation.snake.dance");

    public static AnimationController<SnakeEntity> mainController(SnakeEntity animatable) {
        return new AnimationController<>(animatable, state -> {
            if(animatable.hasFieldGuideState()){
                state.getController().transitionLength(0);
                return state.setAndContinue(IDLE);
            }

            Primal_Util.Visuals.resetControllerAfterAttack(animatable, state, BITE_STANDING, BITE_SLITHERING);
            state.getController().transitionLength(animatable.tickCount==0? 0: 6);
            state.setControllerSpeed(1f);

            //──────────────────────────────────── Triggered ────────────────────────────────────
            {
                if (state.getController().isPlayingTriggeredAnimation()) {
                    state.getController().transitionLength(1);
                    state.setControllerSpeed(2.0f);

                    return PlayState.CONTINUE;
                }
            }

            //────────────────────────────────────    Pose   ────────────────────────────────────
            {
                switch (animatable.getPose()) {
                    case SITTING:
                        return state.setAndContinue(SIT);
                    case SNIFFING:
                        state.getController().transitionLength(0);
                        return state.setAndContinue(WRAPPED);
                }
            }

            //────────────────────────────────────  Movement ────────────────────────────────────
            {
                if (state.isMoving()) {
                    if(animatable.isSlithering()){
                        state.getController().transitionLength(3);
                        state.setControllerSpeed(state.getLimbSwingAmount() * (animatable.isBaby() ? 4f : animatable.isInWater()? 8: 4));
                        return state.setAndContinue(SLITHER);
                    } else {
                        state.getController().transitionLength(3);
                        state.setControllerSpeed(state.getLimbSwingAmount() * (animatable.isBaby() ? 4f : animatable.isInWater()? 8: 4.5f));
                        return state.setAndContinue(animatable.isAggressive() || animatable.isCautious()? SLITHER_CAUTIOUS: SLITHER_STANDING);
                    }
                } else if(animatable.isDancing()){
                    return state.setAndContinue(DANCE);
                }
            }

            //────────────────────────────────────    Idle   ────────────────────────────────────
            state.setControllerSpeed(animatable.isInWater()? 0.3f: 1f);
            return state.setAndContinue(animatable.isSlithering()? SLITHER: animatable.isInWater()? SLITHER_STANDING: animatable.isAggressive() || animatable.isCautious()? IDLE_CAUTIOUS: IDLE);
        }).receiveTriggeredAnimations()
                .triggerableAnim("slither_start", SnakeAnimations.SLITHER_START)
                .triggerableAnim("slither_end", SnakeAnimations.SLITHER_END);
    }

    public static AnimationController<SnakeEntity> attackController(SnakeEntity animatable) {
        return new AnimationController<>(animatable, "attack", state -> {
            state.getController().transitionLength(1);
            return PlayState.CONTINUE;
        }).receiveTriggeredAnimations()
                .triggerableAnim("bite_standing", SnakeAnimations.BITE_STANDING)
                .triggerableAnim("bite_slithering", SnakeAnimations.BITE_SLITHERING)
                .triggerableAnim("bite_wrapped", SnakeAnimations.BITE_WRAPPED);
    }

    public static AnimationController<SnakeEntity> miscController(SnakeEntity animatable) {
        return new AnimationController<>(animatable, "misc", state -> {
            state.getController().transitionLength(1);
            return PlayState.CONTINUE;
        }).receiveTriggeredAnimations()
                .triggerableAnim("flick", SnakeAnimations.TONGUE_FLICK)
                .triggerableAnim("eat", SnakeAnimations.EAT);
    }
}
