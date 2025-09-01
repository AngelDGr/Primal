package org.primal.entity.ai.controls;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;

public class WaterOrLandLookControl extends LookControl {
    private final int yawAdjustThreshold;
    private final Mob amphibious;
    public WaterOrLandLookControl(Mob mob, int yawAdjustThreshold) {
        super(mob);
        this.yawAdjustThreshold = yawAdjustThreshold;
        this.amphibious = mob;
    }

    @Override
    public void tick() {
        if (amphibious.isInWater()){
            if (this.lookAtCooldown > 0) {
                this.lookAtCooldown--;
                this.getYRotD().ifPresent(float_ -> this.mob.yHeadRot = this.rotateTowards(this.mob.yHeadRot, float_ + 20.0F, this.yMaxRotSpeed));
                this.getXRotD().ifPresent(float_ -> this.mob.setXRot(this.rotateTowards(this.mob.getXRot(), float_ + 10.0F, this.xMaxRotAngle)));
            } else {
                if (this.mob.getNavigation().isDone()) {
                    this.mob.setXRot(this.rotateTowards(this.mob.getXRot(), 0.0F, 5.0F));
                }

                this.mob.yHeadRot = this.rotateTowards(this.mob.yHeadRot, this.mob.yBodyRot, this.yMaxRotSpeed);
            }

            float f = Mth.wrapDegrees(this.mob.yHeadRot - this.mob.yBodyRot);
            if (f < (float)(-this.yawAdjustThreshold)) {
                this.mob.yBodyRot -= 4.0F;
            } else if (f > (float)this.yawAdjustThreshold) {
                this.mob.yBodyRot += 4.0F;
            }
        }
        else{
            super.tick();
        }
    }
}
