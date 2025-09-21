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
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.primal.block.RiverReeds;
import org.primal.block.properties.TripleBlockHalf;
import org.primal.registry.Primal_Blocks;

import java.util.List;

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
        var isNearWater= BlockPredicate.anyOf(
                BlockPredicate.matchesFluids(new BlockPos(1, -1, 0), Fluids.WATER, Fluids.FLOWING_WATER),
                BlockPredicate.matchesFluids(new BlockPos(-1, -1, 0), Fluids.WATER, Fluids.FLOWING_WATER),
                BlockPredicate.matchesFluids(new BlockPos(0, -1, 1), Fluids.WATER, Fluids.FLOWING_WATER),
                BlockPredicate.matchesFluids(new BlockPos(0, -1, -1), Fluids.WATER, Fluids.FLOWING_WATER)
        );

        //This avoids generating them too low
        boolean isTooBelowSeaLevel=origin.getY() < level.getSeaLevel()-3;

        if(!isNearWater.test(level, origin) || isTooBelowSeaLevel)
            return false;

        int i = 0;
        BlockPos.MutableBlockPos desiredPosition = new BlockPos.MutableBlockPos();
        int lateralDistance = randomPatchConfiguration.xzSpread() + 1;
        int verticalDistance = randomPatchConfiguration.ySpread() + 1;

        BlockState defaultStateShortReeds= Primal_Blocks.SHORT_RIVER_REEDS.get().defaultBlockState();
        BlockState defaultStateLongReeds= Primal_Blocks.RIVER_REEDS.get().defaultBlockState();

        // Create noise generator (best to keep this as a field, not per-call!)
        PerlinSimplexNoise reedsNoise = new PerlinSimplexNoise(randomsource, List.of(0));

        for (int dx = -lateralDistance; dx <= lateralDistance; dx++) {
            for (int dz = -lateralDistance; dz <= lateralDistance; dz++) {
                for (int dy = -verticalDistance; dy <= verticalDistance; dy++) {

                    // Sample noise at world position (scaled to avoid blocky look)
                    double noiseValue = reedsNoise.getValue(
                            (origin.getX() + dx) * 0.1, // scale factor controls patch size
                            (origin.getZ() + dz) * 0.1,
                            false
                    );

                    // Threshold for generation (higher -> sparser patches)
                    if (noiseValue > 0.1) {

                        desiredPosition = desiredPosition.setWithOffset(origin, dx, dy, dz);
                        if (defaultStateLongReeds.canSurvive(level, desiredPosition) && canGenerateHere(level, desiredPosition)) {
                            boolean isUnderwater=level.getBlockState(desiredPosition).is(Blocks.WATER);
                            boolean isUnderwaterAbove=level.getBlockState(desiredPosition.above()).is(Blocks.WATER);
                            boolean isUnderwaterAboveAbove=level.getBlockState(desiredPosition.above().above()).is(Blocks.WATER);
                            //33% probability of generating short instead of tall
                            boolean generateShort=level.getRandom().nextInt(0,3)==0;

                            if(!generateShort) {
                                //40% probability of being a plant with flowers -> 40/100 -> 20/50 -> 10/25 - >2/5
                                int age=level.getRandom().nextInt(1, 5)>=4? 1: 0;
                                //33%% probability of being a double instead of triple
                                boolean generateDouble=level.getRandom().nextInt(0, 3)==0;

                                if(isUnderwater && !generateDouble
                                        //Checks the tree upper blocks to not destroy other blocks
                                        && canGenerateHere(level, desiredPosition) && canGenerateHere(level, desiredPosition.above()) && canGenerateHere(level, desiredPosition.above().above())) {
                                    //Avoids generating if all underwater
                                    if(!(isUnderwater && isUnderwaterAbove && isUnderwaterAboveAbove))
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
                                    //Avoids generating if all underwater
                                    if(!(isUnderwater && isUnderwaterAbove))
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

                                        else {
                                            if(level.setBlock(desiredPosition, defaultStateShortReeds.setValue(RiverReeds.WATERLOGGED, isUnderwater), 2)){
                                                i++;
                                            }
                                        }
                                }
                            }
                        }
                    }
                }
            }
        }


//
//        for (int l = 0; l < randomPatchConfiguration.tries(); l++) {
//            desiredPosition = desiredPosition.setWithOffset(
//                    origin,
//                    randomsource.nextInt(lateralDistance) - randomsource.nextInt(lateralDistance),
//                    randomsource.nextInt(verticalDistance) - randomsource.nextInt(verticalDistance),
//                    randomsource.nextInt(lateralDistance) - randomsource.nextInt(lateralDistance)
//            );
//
//            if (defaultStateLongReeds.canSurvive(level, desiredPosition) && canGenerateHere(level, desiredPosition)) {
//
//                boolean isUnderwater=level.getBlockState(desiredPosition).is(Blocks.WATER);
//                boolean isUnderwaterAbove=level.getBlockState(desiredPosition.above()).is(Blocks.WATER);
//                boolean isUnderwaterAboveAbove=level.getBlockState(desiredPosition.above().above()).is(Blocks.WATER);
//                //33% probability of generating short instead of tall
//                boolean generateShort=level.getRandom().nextInt(0,3)==0;
//
//                if(!generateShort) {
//                    //40% probability of being a plant with flowers -> 40/100 -> 20/50 -> 10/25 - >2/5
//                    int age=level.getRandom().nextInt(1, 5)>=4? 1: 0;
//                    //33%% probability of being a double instead of triple
//                    boolean generateDouble=level.getRandom().nextInt(0, 3)==0;
//
//                    if(isUnderwater && !generateDouble
//                    //Checks the tree upper blocks to not destroy other blocks
//                            && canGenerateHere(level, desiredPosition) && canGenerateHere(level, desiredPosition.above()) && canGenerateHere(level, desiredPosition.above().above())) {
//                        //Avoids generating if all underwater
//                        if(!(isUnderwater && isUnderwaterAbove && isUnderwaterAboveAbove))
//                            if(level.setBlock(desiredPosition,
//                                    defaultStateLongReeds
//                                            .setValue(RiverReeds.HALF, TripleBlockHalf.LOWER)
//                                            .setValue(RiverReeds.AGE, age)
//                                            .setValue(RiverReeds.WATERLOGGED, isUnderwater), 2) &&
//
//                                    level.setBlock(desiredPosition.above(),
//                                            defaultStateLongReeds
//                                                    .setValue(RiverReeds.HALF, TripleBlockHalf.MIDDLE)
//                                                    .setValue(RiverReeds.AGE, age)
//                                                    .setValue(RiverReeds.WATERLOGGED, isUnderwaterAbove), 2) &&
//
//                                    level.setBlock(desiredPosition.above().above(),
//                                            defaultStateLongReeds
//                                                    .setValue(RiverReeds.HALF, TripleBlockHalf.UPPER)
//                                                    .setValue(RiverReeds.AGE, age)
//                                                    .setValue(RiverReeds.WATERLOGGED, isUnderwaterAboveAbove), 2)) {
//
//                                i++;
//                            }
//
//                    } else if(canGenerateHere(level, desiredPosition) && canGenerateHere(level, desiredPosition.above())) {
//                        //Avoids generating if all underwater
//                        if(!(isUnderwater && isUnderwaterAbove))
//                            if(level.setBlock(desiredPosition,
//                                    defaultStateLongReeds
//                                            .setValue(RiverReeds.HALF, TripleBlockHalf.LOWER)
//                                            .setValue(RiverReeds.AGE, age)
//                                            .setValue(RiverReeds.WATERLOGGED, isUnderwater), 2) &&
//
//                                    level.setBlock(desiredPosition.above(),
//                                            defaultStateLongReeds
//                                                    .setValue(RiverReeds.HALF, TripleBlockHalf.UPPER)
//                                                    .setValue(RiverReeds.AGE, age)
//                                                    .setValue(RiverReeds.WATERLOGGED, isUnderwaterAbove), 2)) {
//
//                                i++;
//                            }
//
//                        else {
//                                if(level.setBlock(desiredPosition, defaultStateShortReeds.setValue(RiverReeds.WATERLOGGED, isUnderwater), 2)){
//                                    i++;
//                                }
//                        }
//                    }
//                }
//            }
//        }

        return i > 0;
    }


    public boolean canGenerateHere(WorldGenLevel level, BlockPos pos){
        return level.getBlockState(pos).isAir() || level.getBlockState(pos).is(Blocks.WATER);
    }
}
