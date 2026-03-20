package org.primal.entity.ai.controls.move;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.primal.entity.animal.DeerEntity;

public class DeerMoveControl extends MoveControl {
    public DeerMoveControl(Mob mob) {
        super(mob);
    }

    @Override
    public void tick() {
        if (this.operation == MoveControl.Operation.STRAFE) {
            float f = (float)this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
            float f1 = (float)this.speedModifier * f;
            float f2 = this.strafeForwards;
            float f3 = this.strafeRight;
            float f4 = Mth.sqrt(f2 * f2 + f3 * f3);
            if (f4 < 1.0F) {
                f4 = 1.0F;
            }

            f4 = f1 / f4;
            f2 *= f4;
            f3 *= f4;
            float f5 = Mth.sin(this.mob.getYRot() * (float) (Math.PI / 180.0));
            float f6 = Mth.cos(this.mob.getYRot() * (float) (Math.PI / 180.0));
            float f7 = f2 * f6 - f3 * f5;
            float f8 = f3 * f6 + f2 * f5;
            if (!this.isWalkable(f7, f8)) {
                this.strafeForwards = 1.0F;
                this.strafeRight = 0.0F;
            }

            this.mob.setSpeed(f1);
            this.mob.setZza(this.strafeForwards);
            this.mob.setXxa(this.strafeRight);
            this.operation = MoveControl.Operation.WAIT;
        } else if (this.operation == MoveControl.Operation.MOVE_TO) {
            this.operation = MoveControl.Operation.WAIT;
            double d0 = this.wantedX - this.mob.getX();
            double d1 = this.wantedZ - this.mob.getZ();
            double d2 = this.wantedY - this.mob.getY();
            double d3 = d0 * d0 + d2 * d2 + d1 * d1;
            if (d3 < 2.5000003E-7F) {
                this.mob.setZza(0.0F);
                return;
            }

            float f9 = (float)(Mth.atan2(d1, d0) * 180.0F / (float)Math.PI) - 90.0F;
            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), f9, 90.0F));
            this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));

            BlockPos targetPos = mob.getNavigation().getTargetPos();
            if (targetPos != null && this.mob.isSprinting()) {

                Direction dir = this.mob.getDirection();
                int maxCheck = 3; // how far ahead detect fences

                BlockPos fencePos = null;

                for (int i = 1; i <= maxCheck; i++) {
                    BlockPos pos = this.mob.blockPosition().relative(dir, i);
                    BlockState state = this.mob.level().getBlockState(pos);

                    BlockState aboveState = this.mob.level().getBlockState(pos.above());
                    BlockState aboveAboveState = this.mob.level().getBlockState(pos.above().above());
                    BlockState aboveAboveAboveState = this.mob.level().getBlockState(pos.above().above().above());
                    BlockState aboveAboveAboveAboveState = this.mob.level().getBlockState(pos.above().above().above().above());

                    boolean isOneFenceWithSpaceAbove = state.is(BlockTags.FENCES) && aboveState.isAir() && aboveAboveState.isAir() && aboveAboveAboveState.isAir();
                    boolean isTwoBlocksWithAirAbove =
                            !state.isAir()
                            && !aboveState.isAir()
                            && aboveAboveState.isAir() && aboveAboveAboveState.isAir()
                            && aboveAboveAboveAboveState.isAir();

                    //It has to have air above it
                    if (isOneFenceWithSpaceAbove || isTwoBlocksWithAirAbove) {
                        fencePos = pos;
                        break; // first fence in path
                    }
                }

                if (fencePos != null) {
                    Vec3 mobPos = this.mob.position();
                    Vec3 fenceCenter = Vec3.atCenterOf(fencePos);

                    double mobToFence = mobPos.distanceToSqr(fenceCenter);
                    double mobToTarget = mobPos.distanceToSqr(
                            new Vec3(targetPos.getX(), targetPos.getY(), targetPos.getZ())
                    );

                    if (mobToTarget > mobToFence) {
                        this.mob.getJumpControl().jump();
                        ((DeerEntity)this.mob).setIsJumping(true);
                        this.operation = MoveControl.Operation.JUMPING;
                    }
                }
            }

        } else if (this.operation == MoveControl.Operation.JUMPING) {
            this.mob.setSpeed((float)(this.speedModifier*1.8 * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
            if (this.mob.onGround()) {
                this.mob.getNavigation().stop(); // clear path
                this.operation = MoveControl.Operation.WAIT;
                ((DeerEntity)(this.mob)).setIsJumping(false);
            }
        } else {
            this.mob.setZza(0.0F);
        }
    }

    private boolean isWalkable(float relativeX, float relativeZ) {
        PathNavigation pathnavigation = this.mob.getNavigation();
        if (pathnavigation != null) {
            NodeEvaluator nodeevaluator = pathnavigation.getNodeEvaluator();
            if (nodeevaluator != null
                    && nodeevaluator.getPathType(
                    this.mob, BlockPos.containing(this.mob.getX() + (double)relativeX, this.mob.getBlockY(), this.mob.getZ() + (double)relativeZ)
            )
                    != PathType.WALKABLE) {
                return false;
            }
        }

        return true;
    }
}
