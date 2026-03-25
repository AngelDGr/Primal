package org.primal.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.primal.registry.Primal_Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity implements Targeting {

    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    Mob p$THIS = (Mob)(Object)this;

    @ModifyReturnValue(method = "createNavigation", at = @At("RETURN"))
    protected PathNavigation primal$createNavigationForPolarBear(PathNavigation original) {
        if(this.getType().equals(EntityType.POLAR_BEAR))
            return new AmphibiousPathNavigation(p$THIS, p$THIS.level());

        return original;
    }

    @ModifyReturnValue(method = "getMaxHeadXRot", at = @At("RETURN"))
    protected int primal$limitHeadXRotForPolarBear(int original) {
        if(this.getType().equals(EntityType.POLAR_BEAR))
            return this.isInWater()? 10: original;

        return original;
    }

    @ModifyReturnValue(method = "getMaxHeadYRot", at = @At("RETURN"))
    protected int primal$limitHeadYRotForPolarBear(int original) {
        if(this.getType().equals(EntityType.POLAR_BEAR))
            return this.isInWater()? 20: original;

        return original;
    }

    @ModifyReturnValue(method = "checkAndHandleImportantInteractions", at = @At("RETURN"))
    protected InteractionResult primal$handleConchShellInteraction(InteractionResult original, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.is(Primal_Items.WARM_CONCH_SHELL.get()) || itemstack.is(Primal_Items.TEMPERATE_CONCH_SHELL.get()) || itemstack.is(Primal_Items.COLD_CONCH_SHELL.get())) {
            InteractionResult interactionresult = itemstack.interactLivingEntity(player, this, hand);
            if (interactionresult.consumesAction()) {
                return interactionresult;
            }
        }
        return original;
    }
}
