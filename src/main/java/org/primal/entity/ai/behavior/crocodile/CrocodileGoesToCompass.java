package org.primal.entity.ai.behavior.crocodile;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.CrocodileEntity;

public class CrocodileGoesToCompass extends Behavior<CrocodileEntity> {

    public CrocodileGoesToCompass() {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET,
                MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.ADMIRING_ITEM,
                MemoryStatus.VALUE_ABSENT)
        );
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, CrocodileEntity crocodile) {
        return crocodile.getCompassPos().isPresent()
        && crocodile.getCompassPos().get().pos().distManhattan(crocodile.getOnPos())>5;
    }

    @Override
    protected void start(@NotNull ServerLevel level, CrocodileEntity crocodile, long gameTime) {
        var pos = crocodile.getCompassPos();

        if(pos.isEmpty()){
            stop(level, crocodile, gameTime);
            return;
        }

        crocodile.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        crocodile.getBrain().setMemory(MemoryModuleType.WALK_TARGET,
                new WalkTarget(pos.get().pos(), 1.0f, 3));
    }

    @Override
    protected void stop(@NotNull ServerLevel level, CrocodileEntity crocodile, long gameTime) {
        crocodile.getBrain().setMemoryWithExpiry(MemoryModuleType.ADMIRING_ITEM, true,  level.getRandom().nextIntBetweenInclusive(0, 100)+ 200L);
    }
}
