package org.primal.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import org.jetbrains.annotations.NotNull;
import org.primal.block.SeashellsBlock;

public class SeashellsFeature extends Feature<SimpleBlockConfiguration> {
    public SeashellsFeature(Codec<SimpleBlockConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<SimpleBlockConfiguration> context) {
        SimpleBlockConfiguration simpleblockconfiguration = context.config();
        WorldGenLevel level = context.level();
        BlockPos blockpos = context.origin();
        BlockState blockstate = simpleblockconfiguration.toPlace().getState(context.random(), blockpos);
        if (blockstate.canSurvive(level, blockpos)) {

            if(level.getBlockState(blockpos).is(Blocks.WATER))
                blockstate= blockstate.setValue(SeashellsBlock.WATERLOGGED, true);

            level.setBlock(blockpos, blockstate, 2);


            return true;
        } else {
            return false;
        }
    }
}
