package org.primal.entity.ai.behavior.deer;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.DeerEntity;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.util.Primal_Util;

import java.util.function.Predicate;

public class DeerRegrowAntler extends Behavior<DeerEntity> {
    private long endTimestamp=10;
    private final Predicate<DeerEntity> canStart;
    private final Predicate<DeerEntity> canStillUse;
    private final int duration;
    private final UniformInt cooldown;

    public DeerRegrowAntler(int duration, UniformInt cooldown) {
        super(ImmutableMap.of(
                MemoryModuleType.AVOID_TARGET,
                MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.ATTACK_TARGET,
                MemoryStatus.VALUE_ABSENT,
                Primal_MemoryModuleTypes.WAS_TRIGGER_ANIMATION.get(),
                MemoryStatus.VALUE_ABSENT), duration);
        this.duration=duration;
        this.canStart=m->m.getBlockStateOn().is(Blocks.GRASS_BLOCK)
                && (!m.hasRightAntler() || !m.hasLeftAntler())
                && m.hasNaturalAntlers()
                && !Primal_Util.isMoving(m);
        this.canStillUse=this.canStart;
        this.cooldown=cooldown;
    }

    public static DeerRegrowAntler create(UniformInt cooldown) {
        return new DeerRegrowAntler(41, cooldown);
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull DeerEntity mob) {
        return canStart.test(mob);
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, @NotNull DeerEntity entity, long gameTime) {
        return canStillUse.test(entity) && hasRequiredMemories(entity);
    }

    @Override
    protected void start(@NotNull ServerLevel level, @NotNull DeerEntity mob, long gameTime) {
        mob.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        mob.getNavigation().stop();
        mob.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
        mob.level().broadcastEntityEvent(mob, DeerEntity.EAT);

        this.endTimestamp = gameTime + (long) duration;
        time=0;
    }

    private int time=0;
    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull DeerEntity mob, long gameTime) {
        mob.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        mob.getNavigation().stop();
        mob.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
        time++;

        if(time>=35){
            BlockPos blockpos1 = mob.getOnPos();
            if (mob.level().getBlockState(blockpos1).is(Blocks.GRASS_BLOCK)) {
                if (net.neoforged.neoforge.event.EventHooks.canEntityGrief(mob.level(), mob)) {
                    mob.level().levelEvent(2001, blockpos1, Block.getId(Blocks.GRASS_BLOCK.defaultBlockState()));
                    mob.level().setBlock(blockpos1, Blocks.DIRT.defaultBlockState(), 2);
                }

                mob.ate();
            }

            //Regrow antlers
            mob.setAntlers(true, true);
            time=0;
        }
    }

    @Override
    protected void stop(@NotNull ServerLevel level, @NotNull DeerEntity mob, long gameTime) {
        mob.getBrain().setMemoryWithExpiry(Primal_MemoryModuleTypes.WAS_TRIGGER_ANIMATION.get(), true, mob.level().getRandom().nextIntBetweenInclusive(cooldown.getMinValue(), cooldown.getMaxValue()));
        time=0;
    }

    @Override
    protected boolean timedOut(long gameTime) {
        return gameTime > this.endTimestamp;
    }
}