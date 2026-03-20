package org.primal.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import org.primal.util.mob_types.DetectsFartherAway;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.function.Predicate;

@Mixin(NearestVisibleLivingEntities.class)
public class NearestVisibleLivingEntitiesMixin {

    @ModifyVariable(method = "<init>(Lnet/minecraft/world/entity/LivingEntity;Ljava/util/List;)V", at = @At("STORE"))
    private Predicate<LivingEntity> primal$extraLineOfSight(Predicate<LivingEntity> predicate, @Local(argsOnly = true) LivingEntity owner) {
        if(owner instanceof DetectsFartherAway detectsFartherAway)
            return target -> detectsFartherAway.isEntityTargetable(owner, target);

        return predicate;
    }
}
