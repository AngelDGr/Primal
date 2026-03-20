package org.primal.entity.ai.goals;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;

import java.util.EnumSet;
import java.util.function.Predicate;

public class TryFindWaterSurfaceGoal<E extends Mob> extends Goal {
    protected final E mob;
    private final int range;
    private final float speedModifier;
    private final Predicate<E> whenStart;
    private Path path;

    public TryFindWaterSurfaceGoal(E mob, int range, float speedModifier, Predicate<E> whenStart) {
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        this.mob = mob;
        this.range=range;
        this.speedModifier=speedModifier;
        this.whenStart=whenStart;
    }

    @Override
    public boolean canUse() {
        if (mob.isUnderWater() && mob.isInWater() && whenStart.test(mob)) {

            Path path = null;
            BlockPos blockpos = mob.blockPosition();
            ServerLevel serverLevel = (ServerLevel) mob.level();
            for (BlockPos blockPos1 : BlockPos.withinManhattan(blockpos, range, range, range)) {
                if (blockPos1.getX() != blockpos.getX() || blockPos1.getZ() != blockpos.getZ()) {
                    BlockState blockState = serverLevel.getBlockState(blockPos1);
                    if (blockState.is(Blocks.WATER)
                            && serverLevel.getFluidState(blockPos1).is(FluidTags.WATER)
                            && serverLevel.getBlockState(blockPos1.above()).isAir()) {

                        this.mob.lookAt(EntityAnchorArgument.Anchor.EYES, blockPos1.above().getCenter());
                        path = this.mob.getNavigation().createPath(blockPos1, 0);
                        break;
                    }
                }
            }

            if (path != null) {
                this.path = path;
                return true;
            }

        }

        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return whenStart.test(this.mob);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void start() {
        this.mob.getNavigation().moveTo(this.path, this.speedModifier);
    }

    @Override
    public void stop() {
        this.mob.getNavigation().stop();
    }
}