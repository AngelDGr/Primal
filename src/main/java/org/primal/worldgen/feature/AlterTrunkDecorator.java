package org.primal.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import org.jetbrains.annotations.NotNull;
import org.primal.registry.Primal_WorldGen;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AlterTrunkDecorator extends TreeDecorator {
    public static final Codec<AlterTrunkDecorator> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    Codec.floatRange(0.0F, 1.0F).fieldOf("probability")
                            .forGetter((alterTrunkDecorator) -> alterTrunkDecorator.probability),
                    BlockStateProvider.CODEC.fieldOf("block_provider")
                            .forGetter((alterTrunkDecorator) -> alterTrunkDecorator.blockProvider),
                    Codec.BOOL.fieldOf("need_air_exposition")
                            .forGetter((alterTrunkDecorator) -> alterTrunkDecorator.needAirExposition),
                    BlockStateProvider.CODEC.fieldOf("to_replace")
                            .forGetter((alterTrunkDecorator) -> alterTrunkDecorator.toReplace),
                    Codec.INT.fieldOf("max_amount")
                            .forGetter((alterTrunkDecorator) -> alterTrunkDecorator.maxAmount),
                    Codec.BOOL.fieldOf("rotates")
                            .forGetter((alterTrunkDecorator) -> alterTrunkDecorator.rotates)
            ).apply(instance, AlterTrunkDecorator::new));

    private final float probability;
    protected final BlockStateProvider blockProvider;
    private final boolean needAirExposition;
    private final int maxAmount;
    private final BlockStateProvider toReplace;
    private final boolean rotates;

    public AlterTrunkDecorator(float probability, BlockStateProvider blockProvider, boolean needAirExposition, BlockStateProvider toReplace, int maxAmount) {
        this(probability, blockProvider, needAirExposition, toReplace, maxAmount, false);
    }

    public AlterTrunkDecorator(float probability, BlockStateProvider blockProvider, boolean needAirExposition, BlockStateProvider toReplace, int maxAmount, boolean rotates) {
        this.probability=probability;
        this.blockProvider=blockProvider;
        this.needAirExposition=needAirExposition;
        this.toReplace = toReplace;
        this.maxAmount=maxAmount;
        this.rotates=rotates;
    }

    @Override
    protected @NotNull TreeDecoratorType<?> type() {
        return Primal_WorldGen.TreeDecorators.ALTERNATE_TRUNK.get();
    }

    @Override
    public void place(@NotNull Context context) {
        RandomSource randomsource = context.random();
        List<BlockPos> logPosList = context.logs();
        AtomicInteger amountPlaced = new AtomicInteger();

        logPosList.forEach((pos) -> {
            var logState= ((WorldGenLevel)(context.level())).getBlockState(pos);

            var canReplace = context.level().isStateAtPosition(pos, p-> p.getBlock().equals(toReplace.getState(randomsource, pos).getBlock()));
            if(!(randomsource.nextFloat()>=this.probability)){
                //Air exposition on false or all around is air
                if((!this.needAirExposition) || (context.isAir(pos.east()) && context.isAir(pos.west()) && context.isAir(pos.north()) && context.isAir(pos.south()))
                        && canReplace){
                    //If it is not has placed the max amount
                    if(amountPlaced.get()<maxAmount){
                        var toPlace = this.blockProvider.getState(randomsource, pos);
                        //Rotates it accordingly
                        if(rotates && logState.hasProperty(RotatedPillarBlock.AXIS) && toPlace.hasProperty(RotatedPillarBlock.AXIS))
                            toPlace= toPlace.setValue(RotatedPillarBlock.AXIS, logState.getValue(RotatedPillarBlock.AXIS));

                        context.setBlock(pos, toPlace);
                        amountPlaced.getAndIncrement();
                    }
                }
            }
        });
    }
}