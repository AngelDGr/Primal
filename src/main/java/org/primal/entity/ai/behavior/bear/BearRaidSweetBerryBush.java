package org.primal.entity.ai.behavior.bear;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.BearEntity;
import org.primal.registry.Primal_MemoryModuleTypes;

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
        return entity.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_SWEET_BERRY_BUSH.get()).isPresent();
    }

    @Override
    protected void start(@NotNull ServerLevel level, BearEntity entity, long gameTime) {
        entity.getBrain().setMemory(MemoryModuleType.WALK_TARGET,
                new WalkTarget(entity.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_SWEET_BERRY_BUSH.get()).get(),
                        1.0f, 2));
    }

    @Override
    protected void tick(@NotNull ServerLevel level, BearEntity bear, long gameTime) {
        //Gets the position of the bush
        BlockPos nearestBush = bear.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_SWEET_BERRY_BUSH.get()).orElse(null);

        if (nearestBush != null && bear.blockPosition().distManhattan(nearestBush) <= 3.0f) {
            BlockState originalState = level.getBlockState(nearestBush);
            //If it isn't a sweet berry and not has age, just cancels the behaviour (This shouldn't ever happen)
            if (!originalState.is(Blocks.SWEET_BERRY_BUSH) || !originalState.hasProperty(SweetBerryBushBlock.AGE)) {
                return;
            }

            //xTODO: Maybe change this
            //Just decreases the age of the bush, and set it to 1 (No berries)
//            BlockState newState = originalState.setValue(SweetBerryBushBlock.AGE, 1);
//            level.setBlock(nearestBush, newState, 2);
//            level.gameEvent(GameEvent.BLOCK_CHANGE, nearestBush, GameEvent.Context.of(bear, originalState));

            //DESTROY BUSH
            level.destroyBlock(nearestBush, false, bear);

            bear.triggerAnim("attack", "attack");

            //Sounds of picking the bush berries
            bear.playSound(SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, 1.0F, 1.0F);
        }
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, BearEntity owner) {
        //To not eat berries while full of honey or being a baby
        if (owner.isBaby() || owner.getHoneyCounter() > 0)
            return false;
        BlockPos nearestBushPos = owner.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_SWEET_BERRY_BUSH.get()).orElse(null);
        if (!(level.getBlockState(nearestBushPos).is(Blocks.SWEET_BERRY_BUSH) && level.getBlockState(nearestBushPos).hasProperty(SweetBerryBushBlock.AGE) && level.getBlockState(nearestBushPos).getValue(SweetBerryBushBlock.AGE)>=2))
            return false;
        return true;
    }
}
