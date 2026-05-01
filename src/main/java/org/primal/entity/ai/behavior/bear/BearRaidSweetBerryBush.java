package org.primal.entity.ai.behavior.bear;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.BearEntity;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.util.Primal_Util;

public class BearRaidSweetBerryBush extends Behavior<BearEntity> {

    //Determines what memories need to activate this behavior
    public BearRaidSweetBerryBush() {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET,
                MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.ROAR_TARGET,
                MemoryStatus.VALUE_ABSENT,
                Primal_MemoryModuleTypes.NEAREST_BEEHIVE.get(),
                MemoryStatus.VALUE_ABSENT,
                Primal_MemoryModuleTypes.NEAREST_SWEET_BERRY_BUSH.get(),
                MemoryStatus.VALUE_PRESENT)
        );
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, BearEntity entity, long gameTime) {
        return entity.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_SWEET_BERRY_BUSH.get()).isPresent() && checkExtraStartConditions(level, entity);
    }

    @Override
    protected void start(@NotNull ServerLevel level, BearEntity entity, long gameTime) {
        var bush = entity.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_SWEET_BERRY_BUSH.get());
        bush.ifPresent(blockPos -> entity.getBrain().setMemory(MemoryModuleType.WALK_TARGET,
                new WalkTarget(blockPos,
                        1.0f, 0)));
    }

    @Override
    protected void tick(@NotNull ServerLevel level, BearEntity bear, long gameTime) {
        //Gets the position of the bush
        BlockPos nearestBush = bear.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_SWEET_BERRY_BUSH.get()).orElse(null);

        if (nearestBush != null && bear.blockPosition().distManhattan(nearestBush) <= 3.0f) {
            BlockState originalState = level.getBlockState(nearestBush);
            //If it isn't a sweet berry and not has age, just cancels the behavior (This shouldn't ever happen)
            if (!originalState.is(Blocks.SWEET_BERRY_BUSH) || (originalState.hasProperty(SweetBerryBushBlock.AGE) && originalState.getValue(SweetBerryBushBlock.AGE)==0)) {
                stop(level, bear, gameTime);
                return;
            }

            //Sounds of picking the bush berries
            bear.playSound(SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, 1.0F, 1.0F);

            level.destroyBlock(nearestBush, false, bear);
            level.setBlock(nearestBush, originalState.setValue(SweetBerryBushBlock.AGE, 0), 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, nearestBush, GameEvent.Context.of(bear));

            bear.swing(InteractionHand.MAIN_HAND);
            bear.setBerriesCounter(Primal_Util.toTicks(45));
        }
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, BearEntity owner) {
        return owner.getHoneyCounter() <= 0 && owner.getBerriesCounter()<=0;
    }
}
