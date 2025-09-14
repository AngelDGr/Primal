package org.primal.client.animation.entity;

import net.minecraft.world.entity.Pose;
import org.primal.entity.animal.CrocodileEntity;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

public class CrocodileAnimations {
    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.crocodile.idle");
    public static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.crocodile.walk");
    public static final RawAnimation BASKING_START = RawAnimation.begin().thenPlay("animation.crocodile.basking_start");
    public static final RawAnimation BASKING = RawAnimation.begin().thenLoop("animation.crocodile.basking");
    public static final RawAnimation VOMITS = RawAnimation.begin().thenPlay("animation.crocodile.vomits");
    public static final RawAnimation BASKING_END = RawAnimation.begin().thenPlay("animation.crocodile.basking_end");
    public static final RawAnimation SWIM = RawAnimation.begin().thenLoop("animation.crocodile.swim");
    public static final RawAnimation SWIM_IDLE = RawAnimation.begin().thenLoop("animation.crocodile.swim_idle");
    public static final RawAnimation ATTACK = RawAnimation.begin().thenPlay("animation.crocodile.attack");
    public static final RawAnimation TRASH = RawAnimation.begin().thenLoop("animation.crocodile.thrash");
    public static final RawAnimation ATTACK_UNDERWATER = RawAnimation.begin().thenPlay("animation.crocodile.attack_underwater");
    public static final RawAnimation TRASH_UNDERWATER = RawAnimation.begin().thenLoop("animation.crocodile.thrash_underwater");
    public static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.crocodile.run");

    public static AnimationController<CrocodileEntity> mainController(CrocodileEntity animatable) {
        return new AnimationController<>(animatable, state -> {
            state.getController().transitionLength(0);

            if (state.getController().isPlayingTriggeredAnimation()) {
                state.getController().transitionLength(0);
                state.setControllerSpeed(1f);
                return PlayState.CONTINUE;
            }

            if(animatable.getPose() == Pose.SPIN_ATTACK){
                state.setControllerSpeed(1f);
                return state.setAndContinue(animatable.isInWater()? TRASH_UNDERWATER: TRASH);
            }

            if (state.isMoving()) {
                if (animatable.isInWater()) {
                    state.setControllerSpeed(state.getLimbSwingAmount() * (animatable.isBaby() ? 8 : 4f));
                    return state.setAndContinue(SWIM);
                } else if(animatable.isSprinting()){
                    state.setControllerSpeed(state.getLimbSwingAmount() * (animatable.isBaby() ? 4 : 2.f));
                    return state.setAndContinue(RUN);
                } else {
                    state.setControllerSpeed(state.getLimbSwingAmount() * (animatable.isBaby() ? 6 : 5f));
                    return state.setAndContinue(WALK);
                }
            }

            state.getController().transitionLength(2);
            state.setControllerSpeed(1f);
            return state.setAndContinue(animatable.isUnderWater()? SWIM : animatable.isInWater()? SWIM_IDLE: IDLE);
        });
    }
}
