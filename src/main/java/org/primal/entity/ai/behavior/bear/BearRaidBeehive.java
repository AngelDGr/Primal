package org.primal.entity.ai.behavior.bear;

import net.minecraft.world.InteractionHand;
import org.jetbrains.annotations.NotNull;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.entity.animal.BearEntity;

import com.google.common.collect.ImmutableMap;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity.BeeReleaseStatus;
import net.minecraft.world.level.block.state.BlockState;
import org.primal.util.Primal_Util;

public class BearRaidBeehive extends Behavior<BearEntity> {

    public BearRaidBeehive() {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET,
                MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.ROAR_TARGET,
                MemoryStatus.VALUE_ABSENT,
                Primal_MemoryModuleTypes.NEAREST_BEEHIVE.get(),
                MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, BearEntity entity, long gameTime) {
        return entity.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_BEEHIVE.get()).isPresent();
    }

    @Override
    protected void start(@NotNull ServerLevel level, BearEntity entity, long gameTime) {
        var beehive = entity.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_BEEHIVE.get());
        beehive.ifPresent(blockPos ->
                entity.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(blockPos, 1.f, 0)));
    }

    @Override
    protected void tick(@NotNull ServerLevel level, BearEntity owner, long gameTime) {
        BlockPos nearestBeehive = owner.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_BEEHIVE.get()).orElse(null);
        if (nearestBeehive != null && owner.blockPosition().distManhattan(nearestBeehive) <= 4.5f) {

            BlockState state = level.getBlockState(nearestBeehive);
            if (!state.is(BlockTags.BEEHIVES)) {
                return;
            }
            BeehiveBlockEntity beehiveBlockEntity = ((BeehiveBlockEntity) level.getBlockEntity(nearestBeehive));
            if (beehiveBlockEntity == null) {
                return;
            }
            for (Entity entity : beehiveBlockEntity.releaseAllOccupants(state, BeeReleaseStatus.EMERGENCY)) {
                ((Bee)entity).setTarget(owner);
            }
            level.destroyBlock(nearestBeehive, false, owner);
            owner.swing(InteractionHand.MAIN_HAND);

            owner.setHoneyCounter(Primal_Util.toTicks(150));
        }
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, BearEntity owner) {
        if (owner.isBaby() || owner.getHoneyCounter() > 0)
            return false;
        BlockPos blockPosBeehive = owner.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_BEEHIVE.get()).orElse(null);
        return blockPosBeehive != null && level.getBlockEntity(blockPosBeehive) instanceof BeehiveBlockEntity && BeehiveBlockEntity.getHoneyLevel(level.getBlockState(blockPosBeehive)) >= 5;
    }

}
