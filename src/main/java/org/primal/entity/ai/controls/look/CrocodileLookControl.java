package org.primal.entity.ai.controls.look;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;

import java.util.function.Predicate;

public class CrocodileLookControl<T extends Mob> extends LookControl {
    private final int maxYRotFromCenter;
    private final T amphibious;
    private final Predicate<T> canSwim;
    public CrocodileLookControl(T mob, int maxYRotFromCenter) {
        this(mob, maxYRotFromCenter, m->true);
    }

    public CrocodileLookControl(T mob, int maxYRotFromCenter, Predicate<T> canSwim) {
        super(mob);
        this.maxYRotFromCenter = maxYRotFromCenter;
        this.amphibious = mob;
        this.canSwim=canSwim;
    }

    protected boolean resetXRotOnTick() {
        return !this.mob.isUnderWater();
    }

    @Override
    public void tick() {
        if (amphibious.isInWater() && canSwim.test(amphibious)){

            if (this.resetXRotOnTick()) this.mob.setXRot(0.0F);

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
            if (f < (float)(-this.maxYRotFromCenter)) {
                this.mob.yBodyRot -= 4.0F;
            } else if (f > (float)this.maxYRotFromCenter) {
                this.mob.yBodyRot += 4.0F;
            }
        }
        else{
            super.tick();
        }
    }
}
