package org.primal.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.primal.util.block_types.Snowloggable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin extends Level implements WorldGenLevel {
    protected ServerLevelMixin(WritableLevelData levelData, ResourceKey<Level> dimension, RegistryAccess registryAccess, Holder<DimensionType> dimensionTypeRegistration, Supplier<ProfilerFiller> profiler, boolean isClientSide, boolean isDebug, long biomeZoomSeed, int maxChainedNeighborUpdates) {
        super(levelData, dimension, registryAccess, dimensionTypeRegistration, profiler, isClientSide, isDebug, biomeZoomSeed, maxChainedNeighborUpdates);
    }

    @Inject(method = "tickChunk",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;",
                    ordinal = 1))
    private void primal$increaseSnowNaturallyOnBlock(LevelChunk levelChunk, int i, CallbackInfo ci, @Local(ordinal = 0) BlockPos blockpos){
        BlockState blockstate = this.getBlockState(blockpos);

        if(blockstate.getBlock() instanceof Snowloggable
                && !blockstate.getValue(Snowloggable.SNOWY) &&
                //Can't be snowlogged if is already waterlogged
                (!blockstate.hasProperty(BlockStateProperties.WATERLOGGED) || !blockstate.getValue(BlockStateProperties.WATERLOGGED))){
            BlockState blockState1 = blockstate.setValue(Snowloggable.SNOWY, true);
            Block.pushEntitiesUp(blockstate, blockState1, this, blockpos);
            this.setBlockAndUpdate(blockpos, blockState1);
        }
    }

    @WrapOperation(
            method = "tickChunk",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z",
                    ordinal = 2)
    )
    private boolean primal$avoidReplacingSnowloggable(ServerLevel instance, BlockPos blockPos, BlockState state, Operation<Boolean> original) {
        BlockState blockstate = this.getBlockState(blockPos);
        if (!(blockstate.getBlock() instanceof Snowloggable)) {
            return original.call(instance, blockPos, state);
        } else {
            return false;
        }
    }
}
