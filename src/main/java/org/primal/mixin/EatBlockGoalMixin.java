package org.primal.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.EatBlockGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import org.primal.registry.Primal_Blocks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EatBlockGoal.class)
public abstract class EatBlockGoalMixin extends Goal {

    @Shadow @Final private Level level;

    @Shadow @Final private Mob mob;

    @Shadow private int eatAnimationTick;

    @Inject(method = "canUse", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    private void primal$canEatStraw(CallbackInfoReturnable<Boolean> cir) {
        BlockPos blockpos = this.mob.blockPosition();
        cir.setReturnValue(cir.getReturnValue() || this.level.getBlockState(blockpos.below()).is(Primal_Blocks.STRAW_BALE.get()));
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void primal$cantShiftIfHostileMount(CallbackInfo ci) {
        if (this.eatAnimationTick == this.adjustedTickDelay(4)) {
            BlockPos blockpos = this.mob.blockPosition();
            BlockPos blockpos1 = blockpos.below();

            if (this.level.getBlockState(blockpos1).is(Primal_Blocks.STRAW_BALE.get())) {
                this.level.playSound(null, blockpos1 , SoundType.GRASS.getBreakSound(), SoundSource.BLOCKS, 1, 1);
                this.mob.ate();
            }
        }
    }
}
