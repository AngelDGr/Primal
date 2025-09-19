package org.primal.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.VillagerDataHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.primal.registry.Primal_Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(ZombieVillager.class)
public abstract class ZombieVillagerMixin extends Zombie implements VillagerDataHolder {
    @Shadow protected abstract void startConverting(@Nullable UUID conversionStarter, int villagerConversionTime);

    public ZombieVillagerMixin(EntityType<? extends Zombie> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void primal$curesVillagerAppleFritter(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir){
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.is(Primal_Items.GOLDEN_APPLE_FRITTER.get())) {
            if (this.hasEffect(MobEffects.WEAKNESS)) {
                itemstack.consume(1, player);
                if (!this.level().isClientSide) {
                    //Vanilla -> 3.0-5.0 minutes (3600-6000)
                    //Fritter -> 1.5-2.5 minutes (1800-3000)
                    this.startConverting(player.getUUID(), this.random.nextInt(1201) + 1800);
                }

                cir.setReturnValue(InteractionResult.SUCCESS);
            } else {
                cir.setReturnValue(InteractionResult.CONSUME);
            }
        }
    }
}
