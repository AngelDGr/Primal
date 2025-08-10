package org.primal.client.animation.entity;

import org.primal.entity.animal.Bear;

import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.Animation.LoopType;
import software.bernie.geckolib.animation.AnimationController.State;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.constant.dataticket.DataTicket;

public final class BearAnimations {
    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.grizzly_bear.idle");
    public static final RawAnimation IDLE_ALT = RawAnimation.begin().thenPlay("animation.grizzly_bear.idle_break_one");
    public static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.grizzly_bear.walk");
    public static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.grizzly_bear.run");
    public static final RawAnimation ROAR = RawAnimation.begin().thenPlay("animation.grizzly_bear.roar");
    public static final RawAnimation ATTACK = RawAnimation.begin().thenPlay("animation.grizzly_bear.attack");
    public static final RawAnimation ATTACK_ALT = RawAnimation.begin().thenPlay("animation.grizzly_bear.attack_flipped");
    public static final RawAnimation BEG = RawAnimation.begin().thenPlay("animation.grizzly_bear.beg_start").thenLoop("animation.grizzly_bear.beg");
    public static final RawAnimation SLEEP = RawAnimation.begin().thenLoop("animation.grizzly_bear.sleep");

    public static AnimationController<Bear> mainController(Bear animatable) {
        return new AnimationController<>(animatable, state -> {
            state.setControllerSpeed(1);
            state.getController().transitionLength(0);

            if (state.getController().isPlayingTriggeredAnimation()) {
                state.getController().transitionLength(5);
                return PlayState.CONTINUE;
            }

            switch (animatable.getPose()) {
                case SNIFFING:
                    if (state.getController().getCurrentAnimation() != null && state.getController().getCurrentAnimation().animation().name() == BEG.getAnimationStages().get(0).animationName()) {
                        state.getController().transitionLength(5);
                    }
                    return state.setAndContinue(BEG);
                case CROAKING:
                    return state.setAndContinue(SLEEP);
                default:
                    break;
            }

            if (state.isMoving()) {
                if (animatable.isSprinting()) {
                    return state.setAndContinue(RUN);
                } else {
                    state.setControllerSpeed(state.getLimbSwingAmount() * (animatable.isBaby() ? 15 : 3));
                    return state.setAndContinue(WALK);
                }
            }

            state.setControllerSpeed(1);
            state.getController().transitionLength(20);
            return state.setAndContinue(IDLE);
        });
    }
}
