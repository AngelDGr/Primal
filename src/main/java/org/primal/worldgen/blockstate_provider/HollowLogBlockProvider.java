package org.primal.worldgen.blockstate_provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import org.jetbrains.annotations.NotNull;
import org.primal.block.HollowLogBlock;
import org.primal.registry.Primal_Codecs;
import org.primal.registry.Primal_WorldGen;

import java.util.List;

public class HollowLogBlockProvider extends BlockStateProvider {
    public static final Codec<HollowLogBlockProvider> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    BlockState.CODEC.fieldOf("state")
                            .forGetter((hollowRotated)-> hollowRotated.block.defaultBlockState()),
                    Primal_Codecs.AXIS_LIST.fieldOf("axis_to_rotate")
                            .forGetter((hollowRotated)-> hollowRotated.axisToRotate)
            ).apply(instance, (state, canRotateAxis) -> new HollowLogBlockProvider(state.getBlock(), canRotateAxis)));

    private final Block block;
    private final List<Direction.Axis> axisToRotate;

    public HollowLogBlockProvider(Block block) {
        this(block, List.of());
    }

    public HollowLogBlockProvider(Block block, List<Direction.Axis> axisToRotate) {
        this.block = block;
        this.axisToRotate = axisToRotate;
    }

    public Block getBlock() {
        return block;
    }

    @Override
    protected @NotNull BlockStateProviderType<?> type() {
        return Primal_WorldGen.BlockStateProviders.HOLLOW_LOG_ROTATED.get();
    }

    @Override
    public @NotNull BlockState getState(@NotNull RandomSource random, @NotNull BlockPos pos) {
        var finalState= this.block.defaultBlockState();

        List<Direction> rotations = List.of(
                Direction.NORTH,
                Direction.EAST,
                Direction.SOUTH,
                Direction.WEST
        );
        Direction randomDirection = rotations.get(random.nextInt(rotations.size()));

        finalState= finalState.setValue(HollowLogBlock.FACING, randomDirection);

        if(!axisToRotate.isEmpty()){
            var axis= axisToRotate.get(random.nextInt(axisToRotate.size()));
            finalState= finalState.setValue(HollowLogBlock.AXIS, axis);
        }

        return finalState;
    }
}