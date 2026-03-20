package org.primal.client.animation.entity;

import net.minecraft.world.entity.Pose;
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

    public static final RawAnimation GLIDE = RawAnimation.begin().thenLoop("animation.eagle.glide");
    public static final RawAnimation SWOOP = RawAnimation.begin().thenLoop("animation.eagle.swoop");

    public static AnimationController<EagleEntity> mainController(EagleEntity animatable) {
        return new AnimationController<>(animatable, state -> {
            state.getController().transitionLength(2);
            state.setControllerSpeed(1f);

            //──────────────────────────────────── Triggered ────────────────────────────────────
            {
                if (state.getController().isPlayingTriggeredAnimation()) {
                    return PlayState.CONTINUE;
                }
            }

            //────────────────────────────────────    Pose   ────────────────────────────────────
            {
                if (animatable.getPose() == Pose.SITTING && (animatable.onGround() || animatable.isInWater())) {
                    return state.setAndContinue(SIT);
                }
            }

            //────────────────────────────────────  Movement ────────────────────────────────────
            {
                if (state.isMoving()) {
                    double speed = animatable.getDeltaMovement().length();
                    boolean canSwoop = !animatable.onGround() && animatable.getXRot() > 50 && speed>0.35 && animatable.isAggressive();
                    state.setControllerSpeed(state.getLimbSwingAmount() * (animatable.isBaby() ? 4f : 1f));
                    return state.setAndContinue(animatable.isBaby()? WALK: canSwoop? SWOOP : animatable.onGround()? WALK: speed>0.50? GLIDE: FLY);
                }
            }

            //────────────────────────────────────    Idle   ────────────────────────────────────
            state.setControllerSpeed(animatable.isBaby() && animatable.isInWater()? 0.3f: 1.0f);
            return state.setAndContinue(animatable.isBaby() && animatable.isInWater()? WALK: isBabyOnNest(animatable)? SIT: animatable.onGround()? IDLE: FLY);
        }).receiveTriggeredAnimations();
    }

    private static boolean isBabyOnNest(EagleEntity eagle){
        return eagle.isBaby() &&
                eagle.level().getBlockState(eagle.getOnPos()).is(Primal_Blocks.NEST_BLOCK.get())
                && !eagle.level().getBlockState(eagle.getOnPos()).getValue(NestBlock.HAS_EGG);
    }
}
