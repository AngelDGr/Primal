package org.primal.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.MaceItem;
import org.primal.registry.Primal_Effects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MaceItem.class)
public class MaceItemMixin {

    @ModifyVariable(method = "getAttackDamageBonus", at = @At("STORE"), ordinal = 4)
    private float primal$modifyMaceDamageIfHeavy(float value, @Local(argsOnly = true) DamageSource damageSource){
        if(damageSource.getDirectEntity() instanceof LivingEntity player){
            if(player.hasEffect(Primal_Effects.HEAVINESS)){
                return value*2;
            }
        }


        return value;
    }
}
