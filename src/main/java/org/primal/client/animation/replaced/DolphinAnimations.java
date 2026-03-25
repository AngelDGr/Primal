package org.primal.client.animation.replaced;

import net.minecraft.world.entity.animal.Dolphin;
import org.primal.entity.replaced.DolphinReplaced;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

public class DolphinAnimations {
    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.dolphin.idle");
    public static final RawAnimation SWIM = RawAnimation.begin().thenLoop("animation.dolphin.swim");

    @SuppressWarnings("all")
    public static AnimationController<DolphinReplaced> mainController(DolphinReplaced animatable) {
        return new AnimationController<>(animatable, state -> {
            state.getController().transitionLength(10);
            state.setControllerSpeed(1);
            Dolphin dolphin= animatable.getEntityFromState(state);

            //────────────────────────────────────  Movement ────────────────────────────────────
            {
                if (state.isMoving()) {
                    if (dolphin.isInWater()) {
                        state.setControllerSpeed(state.getLimbSwingAmount() * (dolphin.isBaby() ? 15 : 4f));
                        return state.setAndContinue(SWIM);
                    } else {
                        state.setControllerSpeed(1.5f);
                        return state.setAndContinue(SWIM);
                    }
                }
            }

            //────────────────────────────────────    Idle   ────────────────────────────────────
            state.setControllerSpeed(dolphin.isInWater()? 0.6f: 1f);
            return state.setAndContinue(dolphin.isInWater()? IDLE: SWIM);
        }).receiveTriggeredAnimations();
    }
}
