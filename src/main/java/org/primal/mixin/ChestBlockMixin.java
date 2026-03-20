package org.primal.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.phys.AABB;
import org.primal.entity.animal.LionEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(ChestBlock.class)
public class ChestBlockMixin {

    @ModifyReturnValue(method = "isCatSittingOnChest", at = @At("RETURN"))
    private static boolean primal$lionStopChest(boolean original, @Local(argsOnly = true) LevelAccessor level, @Local(argsOnly = true) BlockPos pos) {

        boolean isLionSittingOn=false;
        List<LionEntity> list = level.getEntitiesOfClass(
                LionEntity.class,
                new AABB(
                        pos.getX(),
                        pos.getY() + 1,
                        pos.getZ(),
                        pos.getX() + 1,
                        pos.getY() + 2,
                        pos.getZ() + 1
                )
        );
        if (!list.isEmpty()) {
            for (LionEntity lion : list) {
                if (lion.hasPose(Pose.SLIDING) || lion.hasPose(Pose.SITTING)) {
                    isLionSittingOn=true;
                }
            }
        }

        return original || isLionSittingOn;
    }
}
