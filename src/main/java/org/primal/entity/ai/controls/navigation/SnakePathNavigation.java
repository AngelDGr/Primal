package org.primal.entity.ai.controls.navigation;

import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.primal.entity.animal.SnakeEntity;

public class SnakePathNavigation extends AmphibiousPathNavigation {
    private final SnakeEntity snake;
    public SnakePathNavigation(SnakeEntity snake, Level level) {
        super(snake, level);
        this.snake=snake;
    }

    @Override
    protected double getGroundY(Vec3 vec) {
        return vec.y + (snake.isSlithering() && snake.isInWater()? 0.75: 0);
    }

    @Override
    public void setCanFloat(boolean canSwim) {
        this.nodeEvaluator.setCanFloat(canSwim);
    }
}
