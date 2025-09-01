package org.primal.entity.ai.controls;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;

public class WaterOrLandMoveControl extends MoveControl {
    private final Mob amphibian;
    private final int maxTurnX;
    private final int maxTurnY;
    private final float inWaterSpeedModifier;
    private final float outsideWaterSpeedModifier;
    private final boolean applyGravity;

    public WaterOrLandMoveControl(Mob amphibian, int maxTurnX, int maxTurnY, float inWaterSpeedModifier, float outsideWaterSpeedModifier, boolean applyGravity) {
        super(amphibian);
        this.amphibian = amphibian;
        this.maxTurnX=maxTurnX;
        this.maxTurnY=maxTurnY;
        this.inWaterSpeedModifier=inWaterSpeedModifier;
        this.outsideWaterSpeedModifier=outsideWaterSpeedModifier;
        this.applyGravity=applyGravity;
    }

    public void handleUnderwaterMovement() {
        // Only apply buoyancy if not trying to move downward
        if (this.applyGravity && this.mob.isInWater() && this.mob.yya >= 0.0F) {
            this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0.0, 0.005, 0.0));
        }

        if (this.operation == Operation.MOVE_TO && !this.mob.getNavigation().isDone()) {
            double d = this.wantedX - this.mob.getX();
            double e = this.wantedY - this.mob.getY();
            double f = this.wantedZ - this.mob.getZ();
            double g = d * d + e * e + f * f;
            if (g < 2.5000003E-7F) {
                this.mob.setZza(0.0F);
            } else {
                float h = (float)(Mth.atan2(f, d) * 180.0F / (float)Math.PI) - 90.0F;
                this.mob.setYRot(this.rotlerp(this.mob.getYRot(), h, (float)this.maxTurnY));
                this.mob.yBodyRot = this.mob.getYRot();
                this.mob.yHeadRot = this.mob.getYRot();
                float i = (float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
                if (this.mob.isInWater()) {
                    this.mob.setSpeed(i * this.inWaterSpeedModifier);
                    double j = Math.sqrt(d * d + f * f);
                    if (Math.abs(e) > 1.0E-5F || Math.abs(j) > 1.0E-5F) {
                        float k = -((float)(Mth.atan2(e, j) * 180.0F / (float)Math.PI));
                        k = Mth.clamp(Mth.wrapDegrees(k), (float)(-this.maxTurnX), (float)this.maxTurnX);
                        this.mob.setXRot(this.rotlerp(this.mob.getXRot(), k, 5.0F));
                    }

                    float k = Mth.cos(this.mob.getXRot() * (float) (Math.PI / 180.0));
                    float l = Mth.sin(this.mob.getXRot() * (float) (Math.PI / 180.0));
                    this.mob.zza = k * i;
                    this.mob.yya = -l * i;
                } else {
                    float m = Math.abs(Mth.wrapDegrees(this.mob.getYRot() - h));
                    float n = getTurningSpeedFactor(m);
                    this.mob.setSpeed(i * this.outsideWaterSpeedModifier * n);
                }
            }
        } else {
            this.mob.setSpeed(0.0F);
            this.mob.setXxa(0.0F);
            this.mob.setYya(0.0F);
            this.mob.setZza(0.0F);
        }
    }

    private static float getTurningSpeedFactor(float f) {
        return 1.0F - Mth.clamp((f - 10.0F) / 50.0F, 0.0F, 1.0F);
    }


    @Override
    public void tick() {
        //It's swimming, so applies the water control
        if(amphibian.isInWater()) {
            handleUnderwaterMovement();
        }
        //It's not underwater
        else{
            //It's on land, so return the normal MoveControl
            super.tick();
        }
    }
}
