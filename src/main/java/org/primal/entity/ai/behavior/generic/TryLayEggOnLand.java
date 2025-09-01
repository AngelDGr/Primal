package org.primal.entity.ai.behavior.generic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

public class TryLayEggOnLand {
    public static BehaviorControl<LivingEntity> create(Block spawnBlock, @Nullable SoundEvent soundEggLay){
        return create(spawnBlock, soundEggLay, null,0, 0);
    }

    public static BehaviorControl<LivingEntity> create(Block spawnBlock, @Nullable SoundEvent soundEggLay, @Nullable IntegerProperty eggAmountProperty, int maxEggs, int minEggs) {
        return BehaviorBuilder.create(
            instance -> instance.group(
                        instance.absent(MemoryModuleType.ATTACK_TARGET),
                        instance.present(MemoryModuleType.WALK_TARGET),
                        instance.present(MemoryModuleType.IS_PREGNANT)
                    )
                    .apply(
                        instance,
                        (livingEntityMemoryAccessor, walkTargetMemoryAccessor, pregnantMemoryAccessor) -> (level, entity, l) -> {
                                if (!entity.isInWater() && entity.onGround()) {
                                    BlockPos blockpos = entity.blockPosition().below();

                                    for (Direction direction : Direction.Plane.HORIZONTAL) {
                                        BlockPos blockPos1 = blockpos.relative(direction);
                                        if (!level.getBlockState(blockPos1).isAir() && level.getFluidState(blockPos1).isEmpty()) {
                                            BlockPos blockPos2 = blockPos1.above();

                                            if (level.getBlockState(blockPos2).isAir()) {

                                                BlockState blockstate = spawnBlock.defaultBlockState();

                                                if(eggAmountProperty!=null){
                                                    blockstate=blockstate.setValue(eggAmountProperty, level.getRandom().nextIntBetweenInclusive(minEggs,maxEggs));
                                                }

                                                level.setBlock(blockPos2, blockstate, 3);
                                                level.gameEvent(GameEvent.BLOCK_PLACE, blockPos2, GameEvent.Context.of(entity, blockstate));

                                                if(soundEggLay!=null){
                                                    level.playSound(null, entity, soundEggLay, SoundSource.BLOCKS, 1.0F, 1.0F);
                                                }

                                                pregnantMemoryAccessor.erase();
                                                return true;
                                            }
                                        }
                                    }

                                    return true;
                                } else {
                                    return false;
                                }
                            }
                    )
        );
    }
}
