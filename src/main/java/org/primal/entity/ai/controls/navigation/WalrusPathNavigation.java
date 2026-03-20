package org.primal.entity.ai.controls.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.PathfindingContext;
import org.jetbrains.annotations.NotNull;
import org.primal.registry.Primal_Tags;

public class WalrusPathNavigation extends AmphibiousPathNavigation {

    public WalrusPathNavigation(Mob mob, Level level) {
        super(mob, level);
    }

    @Override
    protected @NotNull PathFinder createPathFinder(int maxVisitedNodes) {
        this.nodeEvaluator = new WalrusNodeEvaluator(false);
        this.nodeEvaluator.setCanPassDoors(true);
        return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
    }

    static class WalrusNodeEvaluator extends AmphibiousNodeEvaluator {
        private final BlockPos.MutableBlockPos belowPos = new BlockPos.MutableBlockPos();

        public WalrusNodeEvaluator(boolean prefersShallowSwimming) {
            super(prefersShallowSwimming);
        }

        @Override
        public @NotNull PathType getPathType(PathfindingContext context, int x, int y, int z) {
            this.belowPos.set(x, y, z);
            BlockState blockstate = context.getBlockState(this.belowPos);
            return blockstate.is(Primal_Tags.Block.WALRUS_SPAWN_ON) ? PathType.OPEN : super.getPathType(context, x, y, z);
        }
    }
}
