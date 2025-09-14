package org.primal.client.animation.entity;

import org.primal.block.NestBlock;
import org.primal.entity.animal.EagleEntity;
import org.primal.registry.Primal_Blocks;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

public class EagleAnimations {
    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.eagle.idle");
    public static final RawAnimation FLY = RawAnimation.begin().thenLoop("animation.eagle.fly");
    public static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.eagle.walk");

    public static final RawAnimation SIT = RawAnimation.begin().thenLoop("animation.eagle.sit");
    public static final RawAnimation IDLE_BREAK_ONE = RawAnimation.begin().thenLoop("animation.eagle.idle_break_one");

    public static final RawAnimation GLIDE = RawAnimation.begin().thenLoop("animation.eagle.glide");
    public static final RawAnimation SWOOP = RawAnimation.begin().thenLoop("animation.eagle.swoop");

    public static final RawAnimation IDLE_BABY = RawAnimation.begin().thenLoop("animation.eagle_chick.idle");
    public static final RawAnimation WALK_BABY = RawAnimation.begin().thenLoop("animation.eagle_chick.walk");
    public static final RawAnimation SIT_BABY  = RawAnimation.begin().thenLoop("animation.eagle_chick.sit");

    public static AnimationController<EagleEntity> mainController(EagleEntity eagle) {
        return new AnimationController<>(eagle, state -> {
            state.setControllerSpeed(1);
            switch (eagle.getPose()) {
                case SITTING:
                    return state.setAndContinue(eagle.isBaby()? SIT_BABY: SIT);
                default:
                    break;
            }

            if(eagle.isBaby()){
                state.getController().transitionLength(0);

                if (state.getController().isPlayingTriggeredAnimation()) {
                    state.getController().transitionLength(0);
                    state.setControllerSpeed(1f);
                    return PlayState.CONTINUE;
                }


                if (state.isMoving()) {
                    state.setControllerSpeed(state.getLimbSwingAmount() * 4);
                    return state.setAndContinue(WALK_BABY);
                }
                //To sit on empty nest when untamed
                else if(
                        eagle.level().getBlockState(eagle.getOnPos()).is(Primal_Blocks.NEST_BLOCK.get())
                        && !eagle.level().getBlockState(eagle.getOnPos()).getValue(NestBlock.HAS_EGG)
                ) {
                    state.setControllerSpeed(1);
                    return state.setAndContinue(SIT_BABY);
                }

                state.getController().transitionLength(2);
                state.setControllerSpeed(1f);
                return state.setAndContinue(IDLE_BABY);
            }

            //Adult
            state.getController().transitionLength(0);

            if (state.getController().isPlayingTriggeredAnimation()) {
                state.getController().transitionLength(0);
                state.setControllerSpeed(1f);
                return PlayState.CONTINUE;
            }

            if (state.isMoving()) {
                double speed = eagle.getDeltaMovement().length();

                state.setControllerSpeed(state.getLimbSwingAmount() * 1f);

                return state.setAndContinue(!eagle.onGround() && eagle.getXRot() > 50? SWOOP : eagle.onGround()? WALK: speed>0.3? GLIDE: FLY);
            }

            state.getController().transitionLength(2);
            state.setControllerSpeed(1f);
            return state.setAndContinue(eagle.onGround()? IDLE: FLY);
        });
    }
}
