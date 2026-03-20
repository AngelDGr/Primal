package org.primal.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import org.jetbrains.annotations.NotNull;
import org.primal.registry.Primal_WorldGen;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AlterTrunkDecorator extends TreeDecorator {
    public static final MapCodec<AlterTrunkDecorator> CODEC = RecordCodecBuilder.mapCodec((instance) ->
            instance.group(
                    Codec.floatRange(0.0F, 1.0F).fieldOf("probability")
                            .forGetter((alterTrunkDecorator) -> alterTrunkDecorator.probability),
                            BlockStateProvider.CODEC.fieldOf("block_provider")
                                    .forGetter((alterTrunkDecorator) -> alterTrunkDecorator.blockProvider),
                            Codec.BOOL.fieldOf("need_air_exposition")
                                    .forGetter((alterTrunkDecorator) -> alterTrunkDecorator.needAirExposition),
                            Codec.INT.fieldOf("max_amount")
                                    .forGetter((alterTrunkDecorator) -> alterTrunkDecorator.maxAmount)
                    ).apply(instance, AlterTrunkDecorator::new));

    private final float probability;
    protected final BlockStateProvider blockProvider;
    private final boolean needAirExposition;
    private final int maxAmount;

    public AlterTrunkDecorator(float probability, BlockStateProvider blockProvider, boolean needAirExposition, int maxAmount){
        this.probability=probability;
        this.blockProvider=blockProvider;
        this.needAirExposition=needAirExposition;
        this.maxAmount=maxAmount;
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
            if(!(randomsource.nextFloat()>=this.probability)){
                //Air exposition on false or all around is air
                if((!this.needAirExposition) || (context.isAir(pos.east()) && context.isAir(pos.west()) && context.isAir(pos.north()) && context.isAir(pos.south())) ){
                    //If it is not has placed the max amount
                    if(amountPlaced.get()<maxAmount){
                        context.setBlock(pos, this.blockProvider.getState(randomsource, pos));
                        amountPlaced.getAndIncrement();
                    }
                }
            }
        });
    }
}
