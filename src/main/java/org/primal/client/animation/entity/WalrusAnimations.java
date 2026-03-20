package org.primal.client.animation.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.primal.entity.animal.WalrusEntity;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

public class WalrusAnimations {

    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.walrus.idle");
    public static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.walrus.walk");
    public static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.walrus.run");
    public static final RawAnimation SWIM_IDLE = RawAnimation.begin().thenLoop("animation.walrus.swim_idle");
    public static final RawAnimation SWIM = RawAnimation.begin().thenLoop("animation.walrus.swim");

    public static final RawAnimation LAY_START = RawAnimation.begin().thenPlay("animation.walrus.lay_start");
    public static final RawAnimation LAY = RawAnimation.begin().thenLoop("animation.walrus.lay");
    public static final RawAnimation LAY_END = RawAnimation.begin().thenPlay("animation.walrus.lay_end");

    public static final RawAnimation GROUND_POUND = RawAnimation.begin().thenPlay("animation.walrus.ground_pound");
    public static final RawAnimation SWIM_ATTACK = RawAnimation.begin().thenLoop("animation.walrus.swim_attack");

    public static final RawAnimation PLAY = RawAnimation.begin().thenLoop("animation.walrus.play");

    public static AnimationController<WalrusEntity> mainController(WalrusEntity animatable) {
        return new AnimationController<>(animatable, state -> {
            state.getController().transitionLength(animatable.tickCount==0? 0: 2);
            state.setControllerSpeed(1f);

            //──────────────────────────────────── Triggered ────────────────────────────────────
            {
                if (state.getController().isPlayingTriggeredAnimation()) {
                    if(animatable.hasInstrument()) return PlayState.STOP;
                    state.setControllerSpeed(speedsUpEndAnimation(animatable, state) ? 3f : 1f);
                    return PlayState.CONTINUE;
                }
            }

            //────────────────────────────────────    Pose   ────────────────────────────────────
            {
                switch (animatable.getPose()){
                    case CROAKING:
                        return state.setAndContinue(LAY);
                    case SPIN_ATTACK:
                        state.getController().transitionLength(0);
                        return state.setAndContinue(SWIM_ATTACK);
                    case ROARING:
                        state.getController().transitionLength(0);
                        return state.setAndContinue(PLAY);
                }
            }

            //────────────────────────────────────  Movement ────────────────────────────────────
            {
                if (state.isMoving()) {
                    if (animatable.isInWater()) {
                        state.setControllerSpeed(state.getLimbSwingAmount() * (animatable.isBaby() ? 3f : 2.5f));
                        return state.setAndContinue(SWIM);
                    } else if (animatable.isSprinting()) {
                        state.setControllerSpeed(state.getLimbSwingAmount() * (animatable.isBaby() ? 3f : 1.5f));
                        return state.setAndContinue(RUN);
                    } else {
                        state.setControllerSpeed(state.getLimbSwingAmount() * (animatable.isBaby() ? 6f : 3.5f));
                        return state.setAndContinue(WALK);
                    }
                }
            }

            //────────────────────────────────────    Idle   ────────────────────────────────────
            state.setControllerSpeed(1f);
            return state.setAndContinue(animatable.isInWater()? SWIM_IDLE: IDLE);
        }).receiveTriggeredAnimations()
                .triggerableAnim("ground_pound", WalrusAnimations.GROUND_POUND)
                .triggerableAnim("lay_start", WalrusAnimations.LAY_START)
                .triggerableAnim("lay_end", WalrusAnimations.LAY_END)
                .setCustomInstructionKeyframeHandler(c-> {
                    if (!"groundParticles;".equals(c.getKeyframeData().getInstructions())) return;

                    BlockPos groundPos = animatable.getOnPos();
                    BlockState groundState = animatable.level().getBlockState(groundPos);
                    if (groundState.isAir() || groundState.liquid()) return;

                    spawnGroundPoundParticles(animatable.level(), animatable, groundState);
                });
    }

    private static void spawnGroundPoundParticles(
            Level level,
            WalrusEntity walrus,
            BlockState groundState
    ) {
        Vec3 center = walrus.position();

        ParticleOptions particle = new BlockParticleOption(
                ParticleTypes.BLOCK,
                groundState
        );

        int points = 80;
        double radius = 1.5;
        double y = 0.05;

        for (int i = 0; i < points; i++) {
            double angle = (Math.PI * 2 * i) / points;

            double px = center.x + Math.cos(angle) * radius;
            double pz = center.z + Math.sin(angle) * radius;

            level.addParticle(
                    particle,
                    px,
                    center.y + y,
                    pz,
                    0.0,
                    0.15,
                    0.0
            );
        }
    }

    private static boolean speedsUpEndAnimation(WalrusEntity animatable, AnimationState<WalrusEntity> state){
        return state.getController().getTriggeredAnimation() !=null && state.getController().getTriggeredAnimation().equals(LAY_END)
                && (animatable.isAggressive() || animatable.getBrain().getMemory(MemoryModuleType.AVOID_TARGET).isPresent() || animatable.isVehicle());
    }
}
