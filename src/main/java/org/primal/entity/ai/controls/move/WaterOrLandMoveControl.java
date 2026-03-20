package org.primal.entity.ai.controls.move;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;

import java.util.function.Predicate;

public class WaterOrLandMoveControl<T extends Mob> extends MoveControl {
    protected final T amphibian;
    protected final int maxTurnX;
    protected final int maxTurnY;
    protected final float inWaterSpeedModifier;
    protected final float outsideWaterSpeedModifier;
    protected final boolean applyGravity;
    protected final Predicate<T> canSwim;

    public WaterOrLandMoveControl(T amphibian, int maxTurnX, int maxTurnY, float inWaterSpeedModifier, float outsideWaterSpeedModifier, boolean applyGravity) {
        this(amphibian, maxTurnX, maxTurnY, inWaterSpeedModifier, outsideWaterSpeedModifier, applyGravity, m->true);
    }

    public WaterOrLandMoveControl(T amphibian, int maxTurnX, int maxTurnY, float inWaterSpeedModifier, float outsideWaterSpeedModifier, boolean applyGravity, Predicate<T> canSwim) {
        super(amphibian);
        this.amphibian = amphibian;
        this.maxTurnX=maxTurnX;
        this.maxTurnY=maxTurnY;
        this.inWaterSpeedModifier=inWaterSpeedModifier;
        this.outsideWaterSpeedModifier=outsideWaterSpeedModifier;
        this.applyGravity=applyGravity;
        this.canSwim=canSwim;
    }

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
                    this.mob.setSpeed(f1 * this.inWaterSpeedModifier);
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

    protected static float getTurningSpeedFactor(float degreesToTurn) {
        return 1.0F - Mth.clamp((degreesToTurn - 10.0F) / 10.0F, 0.0F, 1.0F);
    }

    @Override
    public void tick() {
        //It's swimming, so applies the water control
        if(amphibian.isInWater() && canSwim.test(amphibian)) {
            handleUnderwaterMovement();
        }
        //It's not in water
        else {
            //It's on land, so return the normal MoveControl
            super.tick();
        }
    }
}
