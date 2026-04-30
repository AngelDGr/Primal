package org.primal.entity.ai.behavior.generic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.primal.block.NestBlock;
import org.primal.registry.Primal_Blocks;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.registry.Primal_Sounds;

import java.util.Optional;

public class TryLayEggOnLandOrNest {
    public static BehaviorControl<LivingEntity> create(Block spawnBlock){
        return create(spawnBlock, null,1, 1);
    }

    public static BehaviorControl<LivingEntity> create(Block spawnBlock, @Nullable IntegerProperty eggAmountProperty, int maxEggs, int minEggs) {
        return BehaviorBuilder.create(
                instance -> instance.group(
                                instance.absent(MemoryModuleType.ATTACK_TARGET),
                                instance.present(MemoryModuleType.WALK_TARGET),
                                instance.present(MemoryModuleType.IS_PREGNANT)
                        )
                        .apply(
                                instance,
                                (livingEntityMemoryAccessor, walkTargetMemoryAccessor, pregnantMemoryAccessor) -> (level, entity, l) -> {

                                    // If it has a nest nearby, prioritizes the nest
                                    boolean needANest = entity.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get()).isPresent()
                                            && level.getBlockState(entity.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get()).get()).is(Primal_Blocks.NEST_BLOCK)
                                            && !level.getBlockState(entity.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get()).get()).getValue(NestBlock.HAS_EGG);

                                    if (!entity.isInWater() && entity.onGround()) {

                                        BlockPos blockPosBelow = entity.blockPosition().below();

                                        boolean foundANest = false;
                                        Optional<BlockPos> blockPosAboveAroundOptional = Optional.empty();

                                        //Check directly below first
                                        if (level.getBlockState(blockPosBelow).is(Primal_Blocks.NEST_BLOCK)
                                                && !level.getBlockState(blockPosBelow).getValue(NestBlock.HAS_EGG)) {
                                            foundANest = true;
                                            blockPosAboveAroundOptional = Optional.of(blockPosBelow);
                                        } else {
                                            //Otherwise check around
                                            for (Direction direction : Direction.Plane.HORIZONTAL) {
                                                BlockPos blockPosAround = blockPosBelow.relative(direction);

                                                if (!level.getBlockState(blockPosAround).isAir() && level.getFluidState(blockPosAround).isEmpty()) {
                                                    BlockPos blockPosAboveAround = blockPosAround.above();

                                                    // If it's a nest without an egg
                                                    if (level.getBlockState(blockPosAboveAround).is(Primal_Blocks.NEST_BLOCK)
                                                            && !level.getBlockState(blockPosAboveAround).getValue(NestBlock.HAS_EGG)) {
                                                        foundANest = true;
                                                        blockPosAboveAroundOptional = Optional.of(blockPosAboveAround);
                                                        break; // stop searching, place egg immediately
                                                    }
                                                    // If it is an air block and no nest is required
                                                    else if (level.getBlockState(blockPosAboveAround).isAir() && !needANest) {
                                                        blockPosAboveAroundOptional = Optional.of(blockPosAboveAround);
                                                    }
                                                }
                                            }
                                        }

                                        if (blockPosAboveAroundOptional.isPresent()) {
                                            if (foundANest) {
                                                // Place inside nest
                                                ItemStack eggStack = new ItemStack(spawnBlock.asItem(),
                                                        level.getRandom().nextIntBetweenInclusive(minEggs, maxEggs));

                                                NestBlock.getBlockEntity(level, blockPosAboveAroundOptional.get()).setEgg(eggStack);
                                                BlockState blockstate = level.getBlockState(blockPosAboveAroundOptional.get()).setValue(NestBlock.HAS_EGG, true);
                                                level.setBlock(blockPosAboveAroundOptional.get(), blockstate, 3);
                                                level.gameEvent(GameEvent.BLOCK_CHANGE, blockPosAboveAroundOptional.get(), GameEvent.Context.of(entity, blockstate));

                                            } else {
                                                // Place block egg
                                                BlockState blockstate = spawnBlock.defaultBlockState();
                                                if (eggAmountProperty != null) {
                                                    blockstate = blockstate.setValue(eggAmountProperty,
                                                            level.getRandom().nextIntBetweenInclusive(minEggs, maxEggs));
                                                }
                                                level.setBlock(blockPosAboveAroundOptional.get(), blockstate, 3);
                                                level.gameEvent(GameEvent.BLOCK_PLACE, blockPosAboveAroundOptional.get(),
                                                        GameEvent.Context.of(entity, blockstate));
                                            }

                                            // Play sound + reset pregnant
                                            level.playSound(null, entity, Primal_Sounds.LAYS_EGG.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                                            pregnantMemoryAccessor.erase();
                                            return true;
                                        }
                                    }

                                    return false;
                                }
                        )
        );
    }

}
