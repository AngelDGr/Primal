package org.primal.entity.ai.controls;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import org.primal.util.MiscUtil;

import java.util.function.Predicate;

public class SmoothRotationControl extends BodyRotationControl {
    private final Mob mob;
    private final float maxChangeRate;
    private final Predicate<Mob> condition;
    private float lastStableYHeadRot;
    private int headStableTime;

    public SmoothRotationControl(Mob mob, float maxChangeRate, Predicate<Mob> condition) {
        super(mob);
        this.mob=mob;
        this.maxChangeRate = maxChangeRate;
        this.condition=condition;
    }

    @Override
    public void clientTick() {
        if (condition.test(this.mob)) {
//            this.mob.yBodyRot = MiscUtil.smoothAngle(this.mob.yBodyRot, this.mob.yHeadRot, maxChangeRate);
//            this.mob.setYRot(MiscUtil.smoothAngle(this.mob.getYRot(), this.mob.yHeadRot, maxChangeRate));

            if (this.isMoving()) {
                this.mob.yBodyRot = MiscUtil.smoothAngle(this.mob.yBodyRot, this.mob.getYRot(), maxChangeRate);
                this.mob.yHeadRot = MiscUtil.smoothAngle(this.mob.yHeadRot, Mth.rotateIfNecessary(this.mob.yHeadRot, this.mob.yBodyRot, (float)this.mob.getMaxHeadYRot()), maxChangeRate);

                this.lastStableYHeadRot = this.mob.yHeadRot;
                this.headStableTime = 0;
            } else {
                if (this.notCarryingMobPassengers()) {
                    if (Math.abs(this.mob.yHeadRot - this.lastStableYHeadRot) > 15.0F) {
                        this.headStableTime = 0;
                        this.lastStableYHeadRot = this.mob.yHeadRot;
                        this.mob.yBodyRot = MiscUtil.smoothAngle(this.mob.yBodyRot, Mth.rotateIfNecessary(this.mob.yBodyRot, this.mob.yHeadRot, (float)this.mob.getMaxHeadYRot()), maxChangeRate);
                    } else {
                        this.headStableTime++;
                        if (this.headStableTime > 10) {
                            this.rotateHeadTowardsFront();
                        }
                    }
                }
            }
        }
        else super.clientTick();
    }

    private void rotateHeadIfNecessary() {
        this.mob.yHeadRot = Mth.rotateIfNecessary(this.mob.yHeadRot, this.mob.yBodyRot, (float)this.mob.getMaxHeadYRot());
    }

    private void rotateBodyIfNecessary() {
        this.mob.yBodyRot = Mth.rotateIfNecessary(this.mob.yBodyRot, this.mob.yHeadRot, (float)this.mob.getMaxHeadYRot());
    }

    private void rotateHeadTowardsFront() {
        int i = this.headStableTime - 10;
        float f = Mth.clamp((float)i / 10.0F, 0.0F, 1.0F);
        float f1 = (float)this.mob.getMaxHeadYRot() * (1.0F - f);
        this.mob.yBodyRot = MiscUtil.smoothAngle(this.mob.yBodyRot, Mth.rotateIfNecessary(this.mob.yBodyRot, this.mob.yHeadRot, f1), maxChangeRate);
    }

    private boolean notCarryingMobPassengers() {
        return !(this.mob.getFirstPassenger() instanceof Mob);
    }

    private boolean isMoving() {
        double d0 = this.mob.getX() - this.mob.xo;
        double d1 = this.mob.getZ() - this.mob.zo;
        return d0 * d0 + d1 * d1 > 2.5000003E-7F;
    }
}
