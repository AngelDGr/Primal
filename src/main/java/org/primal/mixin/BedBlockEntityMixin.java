package org.primal.mixin;

import net.minecraft.world.level.block.entity.BedBlockEntity;
import org.primal.injection.BedBlockHasDreamcatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BedBlockEntity.class)
public class BedBlockEntityMixin implements BedBlockHasDreamcatcher {

    @Unique
    private boolean primal$hasDreamcatcher;

    @Override
    public boolean primal$hasDreamcatcher() {
        return primal$hasDreamcatcher;
    }

    @Override
    public void primal$setDreamcatcher(boolean hasDreamcatcher) {
        this.primal$hasDreamcatcher = hasDreamcatcher;
    }
}
