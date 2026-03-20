package org.primal.entity.ai.behavior.generic.home;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class AnimalReturnHome<T extends LivingEntity> extends Behavior<T> {
    private final int startDistance;
    private final int stopDistance;
    private final boolean hasOffset;
    private final Predicate<LivingEntity> canUse;

    public AnimalReturnHome(int startDistanceBlocks, int stopDistanceBlocks, boolean hasOffset) {
        this(startDistanceBlocks, stopDistanceBlocks, hasOffset, m->true);
    }

    public AnimalReturnHome(int startDistanceBlocks, int stopDistanceBlocks, boolean hasOffset, Predicate<LivingEntity> canUse) {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET,
                MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.HOME,
                MemoryStatus.VALUE_PRESENT
        ));
        this.startDistance = startDistanceBlocks*startDistanceBlocks;
        this.stopDistance = stopDistanceBlocks*stopDistanceBlocks;
        this.hasOffset=hasOffset;
        this.canUse = canUse;
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull LivingEntity animal) {
        if(animal.getBrain().getMemory(MemoryModuleType.HOME).isEmpty()){
            return false;
        }

        GlobalPos pos = animal.getBrain().getMemory(MemoryModuleType.HOME).get();
        return animal.getOnPos().distSqr(pos.pos())>= startDistance && canUse.test(animal);
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, @NotNull T animal, long gameTime) {
        if(animal.getBrain().getMemory(MemoryModuleType.HOME).isEmpty()){
            return false;
        }

        GlobalPos pos = animal.getBrain().getMemory(MemoryModuleType.HOME).get();

        return animal.getOnPos().distSqr(pos.pos())> stopDistance;
    }

    @Override
    protected void start(@NotNull ServerLevel level, @NotNull T animal, long gameTime) {
        if(animal.getBrain().getMemory(MemoryModuleType.HOME).isEmpty()){
            stop(level, animal, gameTime);
            return;
        }

        GlobalPos pos = animal.getBrain().getMemory(MemoryModuleType.HOME).get();
        BlockPos finalPos= pos.pos();

        if(hasOffset)
            finalPos= pos.pos().offset(
                animal.getRandom().nextInt(0, 5),
                animal.getRandom().nextInt(0, 5),
                animal.getRandom().nextInt(0, 5));

        BehaviorUtils.setWalkAndLookTargetMemories(animal, finalPos, 1.0f, 0);
    }
}
