package org.primal.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import org.jetbrains.annotations.NotNull;
import org.primal.block.HollowLogBlock;
import org.primal.block_entity.HollowLogBlockEntity;
import org.primal.registry.Primal_BlockEntities;
import org.primal.registry.Primal_Codecs;
import org.primal.registry.Primal_WorldGen;
import org.primal.worldgen.blockstate_provider.HollowLogBlockProvider;

import java.util.List;
import java.util.Map;
import java.util.OptionalInt;

public class HollowLogTreeDecorator extends TreeDecorator {
    public static final Codec<HollowLogTreeDecorator> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("entity_to_spawn")
                            .forGetter((alterTrunkDecorator) -> alterTrunkDecorator.entityToSpawn),
                    ResourceLocation.CODEC.fieldOf("entity_idle_sound")
                            .forGetter((alterTrunkDecorator) -> alterTrunkDecorator.entityIdleSound),
                    HollowLogBlockProvider.CODEC.fieldOf("hollow_log_block")
                            .forGetter((alterTrunkDecorator) -> alterTrunkDecorator.blockProvider),
                    Primal_Codecs.BIOME_TAG_INTEGER_MAP.fieldOf("variant_map")
                            .forGetter((alterTrunkDecorator) -> alterTrunkDecorator.variantMap),
                    UniformInt.CODEC.fieldOf("animal_amount")
                            .forGetter((alterTrunkDecorator) -> alterTrunkDecorator.animalAmount),
                    Codec.floatRange(0.0F, 1.0F).fieldOf("subsequent_probability")
                            .forGetter((alterTrunkDecorator) -> alterTrunkDecorator.subsequentProbability),
                    Codec.INT.fieldOf("max_amount")
                            .forGetter((alterTrunkDecorator) -> alterTrunkDecorator.maxAmount),
                    Codec.INT.fieldOf("min_y_position")
                            .forGetter((alterTrunkDecorator) -> alterTrunkDecorator.minYPosition),
                    Codec.INT.fieldOf("max_y_position")
                            .forGetter((alterTrunkDecorator) -> alterTrunkDecorator.maxYPosition)
            ).apply(instance, HollowLogTreeDecorator::new));

    private final ResourceLocation entityToSpawn;
    private final ResourceLocation entityIdleSound;
    protected final HollowLogBlockProvider blockProvider;
    private final Map<TagKey<Biome>, Integer> variantMap;
    private final UniformInt animalAmount;

    private final float subsequentProbability;
    private final int maxAmount;
    private final int minYPosition;
    private final int maxYPosition;

    public HollowLogTreeDecorator(
            ResourceLocation entityToSpawn,
            ResourceLocation entityIdleSound,
            HollowLogBlockProvider hollowLog,
            Map<TagKey<Biome>, Integer> variantMap,
            UniformInt animalAmount,
            float subsequentProbability,
            int maxAmount,
            int minYPosition,
            int maxYPosition){
        this.entityToSpawn=entityToSpawn;
        this.entityIdleSound = entityIdleSound;
        this.blockProvider= hollowLog;
        this.variantMap=variantMap;
        this.animalAmount=animalAmount;
        this.subsequentProbability = subsequentProbability;
        this.maxAmount=maxAmount;
        this.minYPosition=minYPosition;
        this.maxYPosition=maxYPosition;
    }

    public HollowLogTreeDecorator(ResourceLocation entityToSpawn,
                                  ResourceLocation entityIdleSound,
                                  Block hollowLog,
                                  Map<TagKey<Biome>, Integer> variantMap,
                                  UniformInt animalAmount,
                                  float subsequentProbability,
                                  int maxAmount,
                                  int minYPosition,
                                  int maxYPosition){
        this(entityToSpawn, entityIdleSound, new HollowLogBlockProvider(hollowLog), variantMap, animalAmount, subsequentProbability, maxAmount, minYPosition, maxYPosition);
    }

    @Override
    protected @NotNull TreeDecoratorType<?> type() {
        return Primal_WorldGen.TreeDecorators.HOLLOW_LOG.get();
    }

    @Override
    public void place(@NotNull Context context) {
        RandomSource randomsource = context.random();
        List<BlockPos> logPosList = context.logs();
        int amountPlaced = 0;

        OptionalInt minY = logPosList.stream()
                .filter(blockPos -> context.level().isStateAtPosition(blockPos, state -> state.is(BlockTags.LOGS)))
                .mapToInt(BlockPos::getY)
                .min();

        if (minY.isEmpty()) {
            return; // fallback
        }

        int baseY = minY.getAsInt();

        for (BlockPos pos : logPosList) {
            int relativeHeight = pos.getY() - baseY;

            if (relativeHeight < this.minYPosition || relativeHeight > this.maxYPosition) continue;

            //The subsequent probability only affects if there's more than 1 log
            if (randomsource.nextFloat() <= this.subsequentProbability || amountPlaced==0) {
                var state = this.blockProvider.getState(randomsource, pos);

                //If the direction is facing is air, so the hole is out
                if(context.isAir(pos.relative(state.getValue(HollowLogBlock.FACING)))){
                    //If it is not has placed the max amount
                    if (amountPlaced < maxAmount) {
                        context.setBlock(pos, state);

                        if(!entityToSpawn.getPath().isEmpty())
                            context.level().getBlockEntity(pos, Primal_BlockEntities.HOLLOW_LOG.get()).ifPresent(
                                    hollowLogBlockEntity -> {
                                        WorldGenLevel level = (WorldGenLevel) context.level();
                                        Holder<Biome> biomeHolder = level.getBiome(pos);

                                        int j = randomsource.nextIntBetweenInclusive(this.animalAmount.getMinValue(), this.animalAmount.getMaxValue());

                                        for (int k = 0; k < j; k++) {
                                            hollowLogBlockEntity.storeAnimal( HollowLogBlockEntity.AnimalData.create(
                                                    entityToSpawn,
                                                    entityIdleSound,
                                                    variantMap,
                                                    biomeHolder)
                                            );
                                        }
                                    });

                        amountPlaced++;
                    }
                }
            }
        }
    }
}
