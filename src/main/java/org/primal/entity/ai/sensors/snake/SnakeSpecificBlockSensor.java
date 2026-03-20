package org.primal.entity.ai.sensors.snake;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;
import org.primal.block_entity.NestBlockEntity;
import org.primal.entity.ai.sensors.generic.NearestSpecificBlockSensor;
import org.primal.entity.animal.SnakeEntity;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.registry.Primal_Tags;
import org.primal.util.mob_types.PrimalTamable;

import java.util.Optional;

public class SnakeSpecificBlockSensor extends NearestSpecificBlockSensor<SnakeEntity> {

    public SnakeSpecificBlockSensor(int widthDetection, int heightDetection) {
        super(PrimalTamable::isWandering, Primal_Tags.Block.HOLLOW_LOGS, widthDetection, heightDetection);
    }

    @Override
    protected void doTick(@NotNull ServerLevel level, @NotNull SnakeEntity snake) {
        super.doTick(level, snake);

        //Egg Detection, only when wild
        if(!snake.isTame())
            snake.getBrain()
                    .setMemory(Primal_MemoryModuleTypes.NEAREST_EDIBLE_EGG.get(), findNearestEdibleEgg(level, snake));
    }

    public Optional<BlockPos> findNearestEdibleEgg(ServerLevel level, SnakeEntity snake) {

        return BlockPos.findClosestMatch(snake.blockPosition(), this.widthDetection, this.heightDetection, pos-> {

            if(level.getBlockEntity(pos) instanceof NestBlockEntity nest){
                return nest.getEgg().is(Primal_Tags.Item.SNAKE_EDIBLE_EGGS);
            }

            return false;
        });
    }
}
