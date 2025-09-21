package org.primal.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import org.primal.registry.Primal_Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin {

    @ModifyArg(method = "makeParticles",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addAlwaysVisibleParticle(Lnet/minecraft/core/particles/ParticleOptions;ZDDDDDD)V"),
            index = 2)
    private static double primal$makeParticlesAroundStrawX(double x, @Local SimpleParticleType simpleparticletype,
                                                           @Local(argsOnly = true) BlockPos pos,
                                                           @Local(argsOnly = true) Level level,
                                                           @Local(argsOnly = true, ordinal = 0) boolean isSignalFire) {
        boolean hasStrawBlock = level.getBlockState(pos.below()).getBlock() == Primal_Blocks.STRAW_BALE.get() ||
                //If it has signal fire also works with a block below-below
                (level.getBlockState(pos.below().below()).getBlock() == Primal_Blocks.STRAW_BALE.get() && isSignalFire);
        RandomSource random = level.getRandom();

        if (hasStrawBlock) {
            // radius around the center of the campfire
            double radius = 5.0D; // how far from center
            double angle = random.nextDouble() * Math.PI * 2.0; // random circle
            double distance = radius * random.nextDouble(); // random distance inside radius

            return pos.getX() + 0.5D + Math.cos(angle) * distance;
        }

        return x;
    }

    @ModifyArg(method = "makeParticles",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addAlwaysVisibleParticle(Lnet/minecraft/core/particles/ParticleOptions;ZDDDDDD)V"),
            index = 4)
    private static double primal$makeParticlesAroundStrawZ(double z, @Local SimpleParticleType simpleparticletype,
                                                           @Local(argsOnly = true) BlockPos pos,
                                                           @Local(argsOnly = true) Level level,
                                                           @Local(argsOnly = true, ordinal = 0) boolean isSignalFire) {
        boolean hasStrawBlock = level.getBlockState(pos.below()).getBlock() == Primal_Blocks.STRAW_BALE.get() ||
                //If it has signal fire also works with a block below-below
                (level.getBlockState(pos.below().below()).getBlock() == Primal_Blocks.STRAW_BALE.get() && isSignalFire);
        RandomSource random = level.getRandom();

        if (hasStrawBlock) {
            // radius around the center of the campfire
            double radius = 5.0D; // how far from center
            double angle = random.nextDouble() * Math.PI * 2.0; // random circle
            double distance = radius * random.nextDouble(); // random distance inside radius

            return pos.getZ() + 0.5D + Math.sin(angle) * distance;
        }

        return z;
    }
}
