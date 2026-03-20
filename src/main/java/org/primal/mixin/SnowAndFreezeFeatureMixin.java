package org.primal.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.SnowAndFreezeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.primal.util.block_types.Snowloggable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SnowAndFreezeFeature.class)
public abstract class SnowAndFreezeFeatureMixin extends Feature<NoneFeatureConfiguration> {
    public SnowAndFreezeFeatureMixin(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @ModifyExpressionValue(
            method = "place",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/biome/Biome;shouldSnow(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z",
                    ordinal = 0)
    )
    private boolean primal$avoidReplacingSnowloggable(boolean original, @Local WorldGenLevel level, @Local(ordinal = 0) BlockPos.MutableBlockPos blockPos) {
        BlockState blockstate = level.getBlockState(blockPos);

        return original && !(blockstate.getBlock() instanceof Snowloggable);
    }
}
