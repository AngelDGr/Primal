package org.primal.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.primal.registry.Primal_Entities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin {

    @Shadow public abstract Block getBlock();

    @Shadow protected abstract BlockState asState();

    @Inject(method = "isValidSpawn", at = @At("HEAD"), cancellable = true)
    private void primal$walrusValidSpawnOnIce(BlockGetter level, BlockPos pos, EntityType<?> entityType, CallbackInfoReturnable<Boolean> cir){
        if(entityType.equals(Primal_Entities.WALRUS.get()) && this.asState().is(Blocks.ICE)){
            cir.setReturnValue(true);
        }
    }
}
