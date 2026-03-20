package org.primal.entity.ai.controls.navigation;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.FlyNodeEvaluator;
import net.minecraft.world.level.pathfinder.PathFinder;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.EagleEntity;

public class EaglePathNavigation extends FlyingPathNavigation {
    public EaglePathNavigation(Mob mob, Level level) {
        super(mob, level);
    }

    @Override
    protected @NotNull PathFinder createPathFinder(int maxVisitedNodes) {
        this.nodeEvaluator = new EagleNodeEvaluator();
        this.nodeEvaluator.setCanPassDoors(true);
        this.nodeEvaluator.setCanOpenDoors(true);
        this.nodeEvaluator.setCanFloat(true);

        return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
    }

    public static class EagleNodeEvaluator extends FlyNodeEvaluator {

        @Override
        public boolean canFloat() {
            if(this.mob==null)
                return super.canFloat();

            return super.canFloat() && ((EagleEntity) this.mob).isAffectedByFluids();
        }
    }
}
