package org.primal.entity.ai.controls;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class CrocodilePathNavigation extends PathNavigation {
    public CrocodilePathNavigation(Mob mob, Level level) {
        super(mob, level);
    }

    @Override
    protected @NotNull PathFinder createPathFinder(int maxVisitedNodes) {
        this.nodeEvaluator = new AmphibiousNodeEvaluator(false);
        this.nodeEvaluator.setCanPassDoors(true);
        return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
    }

    @Override
    protected boolean canUpdatePath() {
        return true;
    }

    //Changing it from 0.5 to 0.0 it seems to fix the weird rotations
    @Override
    protected @NotNull Vec3 getTempMobPos() {
        return new Vec3(this.mob.getX(), this.mob.getY(), this.mob.getZ());
    }

    @Override
    protected double getGroundY(Vec3 vec3) {
        return vec3.y;
    }

    @Override
    protected boolean canMoveDirectly(@NotNull Vec3 vecFrom, @NotNull Vec3 vecTo) {
        return this.mob.isInLiquid() && isClearForMovementBetween(this.mob, vecFrom, vecTo, false);
    }

    @Override
    public boolean isStableDestination(BlockPos blockPos) {
        return !this.level.getBlockState(blockPos.below()).isAir();
    }

    @Override
    public void setCanFloat(boolean canFloat) {
    }
}
