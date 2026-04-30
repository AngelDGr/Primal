package org.primal.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import org.jetbrains.annotations.NotNull;
import org.primal.block.NestBlock;
import org.primal.block_entity.NestBlockEntity;
import org.primal.entity.animal.CassowaryEntity;
import org.primal.registry.*;
import org.primal.util.Primal_Util;

public class CassowaryNestFeature extends Feature<RandomPatchCustomConfig> {
    public CassowaryNestFeature(final Codec<RandomPatchCustomConfig> configCodec) {
        super(configCodec);
    }


    @Override
    public boolean place(final @NotNull FeaturePlaceContext<RandomPatchCustomConfig> context) {
        RandomPatchCustomConfig randomPatchConfiguration = context.config();
        RandomSource randomsource = context.random();
        BlockPos origin = context.origin();
        WorldGenLevel level = context.level();

        //This avoids generating unless enough space (cross shape + 1 air above)
        if(notEnoughFreeSpace(level, origin)) return false;

        int i = 0;
        BlockPos.MutableBlockPos desiredPosition = context.origin().mutable();
        int xzSpread = randomPatchConfiguration.xzSpread() + 1;
        int ySpread = randomPatchConfiguration.ySpread() + 1;

        BlockState defaultStateNest = Primal_Blocks.NEST_BLOCK.get().defaultBlockState();

        boolean adultGenerated = false;
        boolean mainNestGenerated= false;

        int eggNumber = 0;
        int cassowaryNumber = 0;

        BlockPos nestOrigin = null;
        //So all the cassowaries are from the same variant
        Holder<Biome> holder = level.getBiome(origin);

        for (int l = 0; l < randomPatchConfiguration.tries(); l++) {

            if(mainNestGenerated)
                desiredPosition.setWithOffset(
                            origin,
                            randomsource.nextInt(xzSpread) - randomsource.nextInt(xzSpread),
                            randomsource.nextInt(ySpread) - randomsource.nextInt(ySpread),
                            randomsource.nextInt(xzSpread) - randomsource.nextInt(xzSpread)
                    );

            //75% probability of replacing a dirt block with suspicious gravel
            if (level.getBlockState(desiredPosition.below()).is(BlockTags.DIRT) && level.getRandom().nextIntBetweenInclusive(0, 3) > 0) {
                level.setBlock(desiredPosition.below(), Blocks.SUSPICIOUS_GRAVEL.defaultBlockState(), Block.UPDATE_ALL);

                BlockEntity be = level.getBlockEntity(desiredPosition.below());
                if (be instanceof BrushableBlockEntity brushable) brushable.setLootTable(Primal_LootTables.CASSOWARY_NEST_LOOT, level.getRandom().nextLong());
            }

            if(canGenerateHere(level, desiredPosition)){

                BlockState finalState = defaultStateNest;

                //Generates nest center and other nests as long as they have a nearby nest
                if(!mainNestGenerated || hasNestAround(level, desiredPosition)){
                    if(level.setBlock(desiredPosition, defaultStateNest, Block.UPDATE_ALL)){
                        //To put eggs with a 10% of probability, at max 2 eggs, always put at least 1 egg
                        if((!mainNestGenerated || level.getRandom().nextInt(10) == 0) && eggNumber < 2){
                            ItemStack eggStack = new ItemStack(Primal_Items.CASSOWARY_EGG.get(), 1);

                            if (level.getBlockEntity(desiredPosition) instanceof NestBlockEntity nestEntity) {
                                nestEntity.setEgg(eggStack);
                                finalState = finalState.setValue(NestBlock.HAS_EGG, true);
                                level.setBlock(desiredPosition, finalState, Block.UPDATE_ALL);
                                eggNumber++;
                            }

                            //Generates main cross
                            if(!mainNestGenerated){
                                nestOrigin=desiredPosition;
                                for (Direction dir : Direction.Plane.HORIZONTAL)
                                    if (canGenerateHere(level, nestOrigin.relative(dir)))
                                        level.setBlock(nestOrigin.relative(dir), Primal_Util.Generation.updateNest(level, nestOrigin.relative(dir), defaultStateNest), Block.UPDATE_ALL);

                                mainNestGenerated=true;
                            }
                        }


                        //To put always at least a cassowary, after the first one it has a 33% probability of generating another, at max 3 (1 adult + 2 babies)
                        //Only on nests without an egg, except the first one
                        if ((!adultGenerated || (level.getRandom().nextIntBetweenInclusive(0, 2) == 0 && !finalState.getValue(NestBlock.HAS_EGG)))
                                && cassowaryNumber < 3) {
                            CassowaryEntity cassowary = Primal_Entities.CASSOWARY.get().create(level.getLevel());

                            if (cassowary != null) {
                                if (adultGenerated) cassowary.setAge(-24000);

                                cassowary.setVariantFromBiome(cassowary, holder);

                                cassowary.moveTo(desiredPosition.getX(), desiredPosition.getY() + 1, desiredPosition.getZ(),
                                        0.0F, 0.0F);

                                level.addFreshEntity(cassowary);
                                adultGenerated = true;
                                cassowaryNumber++;
                            }
                        }

                        level.setBlock(
                                desiredPosition,
                                Primal_Util.Generation.updateNest(level, desiredPosition, finalState),
                                Block.UPDATE_CLIENTS
                        );

                        i++;
                    }
                }
            }
        }

        return i > 0;
    }

    private boolean hasNestAround(WorldGenLevel level, BlockPos desiredPosition){
        return level.getBlockState(desiredPosition.east()).is(Primal_Blocks.NEST_BLOCK);
    }

    private boolean notEnoughFreeSpace(WorldGenLevel level, BlockPos origin){
        return !isAirOrReplaceable(level, origin.east())
                || !isAirOrReplaceable(level, origin.west())
                || !isAirOrReplaceable(level, origin.north())
                || !isAirOrReplaceable(level, origin.south())
                || !isAirOrReplaceable(level, origin.above());
    }

    private static boolean isValidGround(WorldGenLevel level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        return !below.is(BlockTags.LOGS) && !below.is(BlockTags.LEAVES) && (below.is(Primal_Tags.Block.CASSOWARY_NEST_CAN_SPAWN_ON));
    }

    public boolean canGenerateHere(WorldGenLevel level, BlockPos pos){
        return (isAirOrReplaceable(level, pos)) && level.getBlockState(pos.below()).isCollisionShapeFullBlock(level, pos) && isValidGround(level, pos);
    }

    public boolean isAirOrReplaceable(WorldGenLevel level, BlockPos pos){
        return level.getBlockState(pos).isAir() || level.getBlockState(pos).canBeReplaced();
    }
}
