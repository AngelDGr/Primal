package org.primal.client.animation.replaced;

import net.minecraft.world.entity.animal.Rabbit;
import org.primal.entity.replaced.RabbitReplaced;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.core.animation.RawAnimation;

public class RabbitAnimations {
    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.rabbit.idle");
    public static final RawAnimation HOP = RawAnimation.begin().thenLoop("animation.rabbit.hop");
    public static final RawAnimation REAR = RawAnimation.begin().thenPlay("animation.rabbit.rear");

    public static AnimationController<RabbitReplaced> mainController(RabbitReplaced animatable) {
        return new AnimationController<>(animatable, state -> {
            state.getController().transitionLength(2);
            state.setControllerSpeed(1);
            Rabbit rabbit= animatable.getEntityFromState(state);
            double speed = rabbit.getDeltaMovement().length();

            //──────────────────────────────────── Triggered ────────────────────────────────────
            {
                if (state.getController().isPlayingTriggeredAnimation()) {
                    return PlayState.CONTINUE;
                }
            }

            //────────────────────────────────────  Movement ────────────────────────────────────
            {
                if (rabbit.getJumpCompletion(state.getPartialTick()) > 0) {
                    state.getController().transitionLength(2);
                    state.setControllerSpeed(1f);
                    return state.setAndContinue(HOP);
                }
            }

            //────────────────────────────────────    Idle   ────────────────────────────────────
            state.setControllerSpeed(rabbit.isInWater()? 0.3f: 1f);
            return state.setAndContinue(IDLE);
        }).receiveTriggeredAnimations()
                .triggerableAnim("rear", RabbitAnimations.REAR);
    }

}
