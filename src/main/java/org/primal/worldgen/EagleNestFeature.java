package org.primal.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import org.jetbrains.annotations.NotNull;
import org.primal.block.NestBlock;
import org.primal.block_entity.NestBlockEntity;
import org.primal.entity.animal.EagleEntity;
import org.primal.registry.Primal_Blocks;
import org.primal.registry.Primal_Entities;
import org.primal.registry.Primal_Items;
import org.primal.util.Primal_Util;

public class EagleNestFeature extends Feature<RandomPatchCustomConfig> {
    public EagleNestFeature(final Codec<RandomPatchCustomConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(final @NotNull FeaturePlaceContext<RandomPatchCustomConfig> context) {
        RandomPatchCustomConfig randomPatchConfiguration = context.config();
        RandomSource randomsource = context.random();
        BlockPos origin = context.origin();
        WorldGenLevel level = context.level();

        //This avoids generating them too low
        boolean isTooLowAltitude=origin.getY() < level.getSeaLevel()+20;

        if(isTooLowAltitude)
            return false;

        int i = 0;
        BlockPos.MutableBlockPos desiredPosition = new BlockPos.MutableBlockPos();
        int xzSpread = randomPatchConfiguration.xzSpread() + 1;
        int ySpread = randomPatchConfiguration.ySpread() + 1;

        BlockState defaultStateNest = Primal_Blocks.NEST_BLOCK.get().defaultBlockState();

        //So all the eagles are from the same variant
        Holder<Biome> holder = level.getBiome(origin);

        boolean adultGenerated=false;

        for (int l = 0; l < randomPatchConfiguration.tries(); l++) {

            desiredPosition.setWithOffset(
                    origin,
                    randomsource.nextInt(xzSpread) - randomsource.nextInt(xzSpread),
                    randomsource.nextInt(ySpread)  - randomsource.nextInt(ySpread),
                    randomsource.nextInt(xzSpread) - randomsource.nextInt(xzSpread)
            );

            if (canGenerateHere(level, desiredPosition)) {

                if (level.setBlock(desiredPosition, defaultStateNest, Block.UPDATE_ALL)) {

                    BlockState finalState=defaultStateNest;

                    //Update connections when generating the nest
                    finalState= Primal_Util.Generation.updateNest(level, desiredPosition, finalState);

                    //To put eggs with a 33% of probability
                    if (level.getRandom().nextInt(3) == 0) {

                        ItemStack eggStack = new ItemStack(Primal_Items.EAGLE_EGG.get(), level.getRandom().nextIntBetweenInclusive(1, 2));
                        BlockEntity be = level.getBlockEntity(desiredPosition);

                        if (be instanceof NestBlockEntity nestEntity) {
                            nestEntity.setEgg(eggStack, null);
                            finalState = finalState.setValue(NestBlock.HAS_EGG, true);
                        }
                    }

                    level.setBlock(desiredPosition, finalState, Block.UPDATE_ALL);

                    //To put always at least an eagle, after the first one it has a 50% probability of generating another
                    if(!adultGenerated || (adultGenerated && level.getRandom().nextIntBetweenInclusive(0, 1)==0)){

                        EagleEntity eagle = Primal_Entities.EAGLE.get().create(level.getLevel());

                        if (eagle != null) {
                            //25% of probability of being a baby instead of an adult, only after generating an adult and only on nest without eggs
                            if(adultGenerated && level.getRandom().nextIntBetweenInclusive(0, 4)==0 && !finalState.getValue(NestBlock.HAS_EGG))
                                eagle.setAge(-24000);

                            eagle.setVariantFromBiome(eagle, holder);

                            eagle.moveTo(desiredPosition.getX(), desiredPosition.getY()+1, desiredPosition.getZ(), 0.0F, 0.0F);
                            level.addFreshEntity(eagle);
                            adultGenerated=true;
                        }
                    }


                    i++;
                }

            }
        }

        return i > 0;
    }


    public boolean canGenerateHere(WorldGenLevel level, BlockPos pos){
        return (level.getBlockState(pos).isAir() || level.getBlockState(pos).canBeReplaced()) && level.getBlockState(pos.below()).isCollisionShapeFullBlock(level, pos);
    }
}
