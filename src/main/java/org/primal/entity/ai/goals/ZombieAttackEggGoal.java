package org.primal.entity.ai.goals;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RemoveBlockGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.primal.registry.Primal_Sounds;

public class ZombieAttackEggGoal extends RemoveBlockGoal {
    public ZombieAttackEggGoal(Block block, PathfinderMob mob, double speedModifier, int verticalSearchRange) {
        super(block, mob, speedModifier, verticalSearchRange);
    }

    @Override
    public void playDestroyProgressSound(LevelAccessor level, @NotNull BlockPos pos) {
        level.playSound(null, pos, Primal_Sounds.ZOMBIE_DESTROY_GENERIC_EGG.get(), SoundSource.HOSTILE, 0.5F, 0.9F + this.mob.getRandom().nextFloat() * 0.2F);
    }

    @Override
    public void playBreakSound(Level level, @NotNull BlockPos pos) {
        level.playSound(null, pos, Primal_Sounds.EGG_BREAK.get(), SoundSource.BLOCKS, 0.7F, 0.9F + level.random.nextFloat() * 0.2F);
    }

    @Override
    public double acceptedDistance() {
        return 1.14;
    }
}