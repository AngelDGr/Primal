package org.primal.entity.ai.controls.look;

import net.minecraft.world.entity.ai.control.LookControl;
import org.primal.entity.animal.EagleEntity;

public class EagleLookControl extends LookControl {

    private final EagleEntity eagle;

    public EagleLookControl(EagleEntity mob) {
        super(mob);
        this.eagle=mob;
    }

    protected boolean resetXRotOnTick() {
        return !eagle.isFlying() || eagle.isBaby();
    }
}