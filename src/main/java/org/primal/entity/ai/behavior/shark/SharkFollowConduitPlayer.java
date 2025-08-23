package org.primal.entity.ai.behavior.shark;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.SharkEntity;
import org.primal.registry.Primal_Advancements;
import org.primal.registry.Primal_MemoryModuleTypes;

public class SharkFollowConduitPlayer extends Behavior<SharkEntity> {
    public SharkFollowConduitPlayer() {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET,
                MemoryStatus.VALUE_ABSENT,
                Primal_MemoryModuleTypes.NEAREST_CONDUIT_PLAYER.get(),
                MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.ADMIRING_ITEM,
                MemoryStatus.VALUE_ABSENT)
        );
    }

    @Override
    protected void start(@NotNull ServerLevel level, SharkEntity shark, long gameTime) {
        shark.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        shark.getBrain().setMemory(MemoryModuleType.WALK_TARGET,
                new WalkTarget(shark.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_CONDUIT_PLAYER.get()).get(),
                        1.0f, 6));

        //Trigger advancement
        if(shark.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_CONDUIT_PLAYER.get()).isPresent()
                && shark.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_CONDUIT_PLAYER.get()).get() instanceof ServerPlayer player){
            Primal_Advancements.SWIM_WITH_SHARK.get().trigger(player);
        }
    }

    @Override
    protected void stop(@NotNull ServerLevel level, SharkEntity shark, long gameTime) {
        shark.heal(2);
        shark.getBrain().setMemoryWithExpiry(MemoryModuleType.ADMIRING_ITEM, true, 100L);
    }
}
