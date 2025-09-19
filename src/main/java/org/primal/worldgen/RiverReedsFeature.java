package org.primal.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.primal.block.RiverReeds;
import org.primal.block.properties.TripleBlockHalf;
import org.primal.registry.Primal_Blocks;

public class RiverReedsFeature extends Feature<RandomPatchCustomConfig> {
    public RiverReedsFeature(final Codec<RandomPatchCustomConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(final @NotNull FeaturePlaceContext<RandomPatchCustomConfig> context) {
        RandomPatchCustomConfig randomPatchConfiguration = context.config();
        RandomSource randomsource = context.random();
        BlockPos origin = context.origin();
        WorldGenLevel level = context.level();
        //This avoids generate them far from water
        var predicate= BlockPredicate.anyOf(
                BlockPredicate.matchesFluids(new BlockPos(1, -1, 0), Fluids.WATER, Fluids.FLOWING_WATER),
                BlockPredicate.matchesFluids(new BlockPos(-1, -1, 0), Fluids.WATER, Fluids.FLOWING_WATER),
                BlockPredicate.matchesFluids(new BlockPos(0, -1, 1), Fluids.WATER, Fluids.FLOWING_WATER),
                BlockPredicate.matchesFluids(new BlockPos(0, -1, -1), Fluids.WATER, Fluids.FLOWING_WATER)
        );
        //This avoids generating them too low
        boolean isTooBelowSeaLevel=origin.getY() < level.getSeaLevel()-3;


        if(!predicate.test(level, origin) || isTooBelowSeaLevel)
            return false;

        int i = 0;
        BlockPos.MutableBlockPos desiredPosition = new BlockPos.MutableBlockPos();
        int j = randomPatchConfiguration.xzSpread() + 1;
        int k = randomPatchConfiguration.ySpread() + 1;

        BlockState defaultStateShortReeds= Primal_Blocks.SHORT_RIVER_REEDS.get().defaultBlockState();
        BlockState defaultStateLongReeds= Primal_Blocks.RIVER_REEDS.get().defaultBlockState();

        for (int l = 0; l < randomPatchConfiguration.tries(); l++) {
            desiredPosition.setWithOffset(
                    origin,
                    randomsource.nextInt(j) - randomsource.nextInt(j),
                    randomsource.nextInt(k) - randomsource.nextInt(k),
                    randomsource.nextInt(j) - randomsource.nextInt(j)
            );
            if (canGenerateHere(level, desiredPosition)
                    //To check if it's a survivable soil
                    && defaultStateShortReeds.canSurvive(level, desiredPosition)
                    && defaultStateLongReeds.canSurvive(level, desiredPosition)) {

                boolean isUnderwater=level.getBlockState(desiredPosition).is(Blocks.WATER);
                boolean isUnderwaterAbove=level.getBlockState(desiredPosition.above()).is(Blocks.WATER);
                boolean isUnderwaterAboveAbove=level.getBlockState(desiredPosition.above().above()).is(Blocks.WATER);
                //33% probability of generating short instead of tall
                boolean generateShort=level.getRandom().nextInt(0,3)==0;

                if(defaultStateLongReeds.canSurvive(level, desiredPosition) && !generateShort) {
                    //50% probability of being a plant without flowers
                    int age=level.getRandom().nextInt(0, 2)==0? 0: 1;
                    //33%% probability of being a double instead of triple
                    boolean generateDouble=level.getRandom().nextInt(0, 3)==0;

                    if(isUnderwater && !generateDouble
                    //Checks the tree upper blocks to not destroy other blocks
                            && canGenerateHere(level, desiredPosition) && canGenerateHere(level, desiredPosition.above()) && canGenerateHere(level, desiredPosition.above().above())) {
                        if(level.setBlock(desiredPosition,
                                defaultStateLongReeds
                                        .setValue(RiverReeds.HALF, TripleBlockHalf.LOWER)
                                        .setValue(RiverReeds.AGE, age)
                                        .setValue(RiverReeds.WATERLOGGED, isUnderwater), 2) &&

                                level.setBlock(desiredPosition.above(),
                                        defaultStateLongReeds
                                                .setValue(RiverReeds.HALF, TripleBlockHalf.MIDDLE)
                                                .setValue(RiverReeds.AGE, age)
                                                .setValue(RiverReeds.WATERLOGGED, isUnderwaterAbove), 2) &&

                                level.setBlock(desiredPosition.above().above(),
                                        defaultStateLongReeds
                                                .setValue(RiverReeds.HALF, TripleBlockHalf.UPPER)
                                                .setValue(RiverReeds.AGE, age)
                                                .setValue(RiverReeds.WATERLOGGED, isUnderwaterAboveAbove), 2)) {

                            i++;
                        }
                    } else if(canGenerateHere(level, desiredPosition) && canGenerateHere(level, desiredPosition.above())) {
                        if(level.setBlock(desiredPosition,
                                defaultStateLongReeds
                                        .setValue(RiverReeds.HALF, TripleBlockHalf.LOWER)
                                        .setValue(RiverReeds.AGE, age)
                                        .setValue(RiverReeds.WATERLOGGED, isUnderwater), 2) &&

                                level.setBlock(desiredPosition.above(),
                                        defaultStateLongReeds
                                                .setValue(RiverReeds.HALF, TripleBlockHalf.UPPER)
                                                .setValue(RiverReeds.AGE, age)
                                                .setValue(RiverReeds.WATERLOGGED, isUnderwaterAbove), 2)) {

                            i++;
                        }

                        else if(defaultStateShortReeds.canSurvive(level, desiredPosition)) {
                            if(level.setBlock(desiredPosition, defaultStateShortReeds.setValue(RiverReeds.WATERLOGGED, isUnderwater), 2)){
                                i++;
                            }
                        }
                    }



                }
            }
        }

        return i > 0;
    }


    public boolean canGenerateHere(WorldGenLevel level, BlockPos pos){
        return level.getBlockState(pos).isAir() || level.getBlockState(pos).is(Blocks.WATER);
    }
}
