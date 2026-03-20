package org.primal.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.primal.injection.BedBlockHasDreamcatcher;
import org.primal.injection.SetNeckEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player{

    public ServerPlayerMixin(Level level, BlockPos pos, float yRot, GameProfile gameProfile) {
        super(level, pos, yRot, gameProfile);
    }

    @Inject(method = "restoreFrom", at = @At("TAIL"))
    private void primal$restoreEntityOnNeck(ServerPlayer that, boolean keepEverything, CallbackInfo ci){
        ((SetNeckEntity)(this)).setEntityOnNeck(((SetNeckEntity)(that)).primal$getNeckEntity());
    }
    @ModifyArg(method = "lambda$startSleepInBed$13",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;"
            ),
            index = 1
    )
    private AABB primal$reduceHostileDetectorWithDreamcatcher(AABB original, @Local(argsOnly = true) BlockPos at) {
        if(this.level().getBlockEntity(at) instanceof BedBlockHasDreamcatcher hasDreamcatcher){
            if(hasDreamcatcher.primal$hasDreamcatcher()){
                return original.deflate(
                        original.getXsize() * 0.25,
                        original.getYsize() * 0.25,
                        original.getZsize() * 0.25);
            }
        }

        return original;
    }
}
