package org.primal.entity.ai.controls.move;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import org.primal.entity.animal.CrocodileEntity;

public class CrocodileMoveControl extends WaterOrLandMoveControl<CrocodileEntity> {
    public CrocodileMoveControl(CrocodileEntity amphibian, int maxTurnX, int maxTurnY, float inWaterSpeedModifier, float outsideWaterSpeedModifier, boolean applyGravity) {
        super(amphibian, maxTurnX, maxTurnY, inWaterSpeedModifier, outsideWaterSpeedModifier, applyGravity);
    }

    @Override
    public void handleUnderwaterMovement() {
        if (this.operation == MoveControl.Operation.JUMPING)
            this.operation = MoveControl.Operation.WAIT;

        if (this.applyGravity && this.mob.isInWater()) {
            this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0.0, 0.005, 0.0));
        }

        if (this.operation == MoveControl.Operation.MOVE_TO && !this.mob.getNavigation().isDone()) {
            double d0 = this.wantedX - this.mob.getX();
            double d1 = this.wantedY - this.mob.getY();
            double d2 = this.wantedZ - this.mob.getZ();
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;
            if (d3 < 2.5000003E-7F) {
                this.mob.setZza(0.0F);
            } else {
                float f = (float)(Mth.atan2(d2, d0) * 180.0F / (float)Math.PI) - 90.0F;
                this.mob.setYRot(this.rotlerp(this.mob.getYRot(), f, (float)this.maxTurnY));
                this.mob.yBodyRot = this.mob.getYRot();
                this.mob.yHeadRot = this.mob.getYRot();
                float f1 = (float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
                if (this.mob.isInWater()) {
                    this.mob.setSpeed(f1 * this.inWaterSpeedModifier * getSurfaceFactor());
                    double d4 = Math.sqrt(d0 * d0 + d2 * d2);
                    if (Math.abs(d1) > 1.0E-5F || Math.abs(d4) > 1.0E-5F) {
                        float f3 = -((float)(Mth.atan2(d1, d4) * 180.0F / (float)Math.PI));
                        f3 = Mth.clamp(Mth.wrapDegrees(f3), (float)(-this.maxTurnX), (float)this.maxTurnX);
                        this.mob.setXRot(this.rotlerp(this.mob.getXRot(), f3, 5.0F));
                    }

                    float f6 = Mth.cos(this.mob.getXRot() * (float) (Math.PI / 180.0));
                    float f4 = Mth.sin(this.mob.getXRot() * (float) (Math.PI / 180.0));
                    this.mob.zza = f6 * f1;
                    this.mob.yya = -f4 * f1;
                } else {
                    float f5 = Math.abs(Mth.wrapDegrees(this.mob.getYRot() - f));
                    float f2 = getTurningSpeedFactor(f5);
                    this.mob.setSpeed(f1 * this.outsideWaterSpeedModifier * f2);
                }
            }
        } else {
            this.mob.setSpeed(0.0F);
            this.mob.setXxa(0.0F);
            this.mob.setYya(0.0F);
            this.mob.setZza(0.0F);
        }
    }

    private float getSurfaceFactor() {
        double distanceToSurface = getDistanceToWaterSurface();

        // Only apply when close to surface and looking upward
        float surfaceFactor = 1.0f;

        if (distanceToSurface < 5.0 && Mth.wrapDegrees(this.mob.getXRot()) < -20 && this.mob.getTarget() == null) {

            // 0 at surface, 1 at 5 blocks deep
            float depthRatio = (float) Mth.clamp(distanceToSurface / 5.0, 0.0, 1.0);

            // Minimum 0.10 speed at surface, smoothly increasing with depth
            surfaceFactor = 0.15f + (0.85f * depthRatio);
            surfaceFactor=Mth.clamp(surfaceFactor, 0.15f, 1.0f);
        }

        return surfaceFactor;
    }

    private double getDistanceToWaterSurface() {
        BlockPos.MutableBlockPos pos = this.mob.blockPosition().mutable();
        int maxCheck = 16; // safety cap to avoid excessive scanning

        for (int i = 0; i < maxCheck; i++) {
            pos.move(Direction.UP);

            if (!this.mob.level().getFluidState(pos).is(FluidTags.WATER)) {
                // Found first non-water block → surface is just below
                double surfaceY = pos.getY();
                return surfaceY - this.mob.getY();
            }
        }

        return Double.MAX_VALUE; // very deep or ocean column
    }
}
