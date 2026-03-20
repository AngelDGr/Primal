package org.primal.entity.ai.controls.look;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;
import org.primal.entity.animal.DeerEntity;
import org.primal.registry.Primal_MemoryModuleTypes;

public class DeerLookControl extends LookControl {
    private final DeerEntity deer;

    public DeerLookControl(Mob mob) {
        super(mob);
        deer = (DeerEntity) mob;
    }


    protected boolean resetXRotOnTick() {
        return !deer.isJumping() && deer.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_PLAY_MOB.get()).isEmpty();
    }
}
