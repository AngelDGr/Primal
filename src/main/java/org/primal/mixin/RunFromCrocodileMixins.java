package org.primal.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.level.Level;
import org.primal.entity.animal.CrocodileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")
public class RunFromCrocodileMixins {

    @Mixin(Pillager.class)
    public abstract static class PillagerMixin extends AbstractIllager implements CrossbowAttackMob, InventoryCarrier {
        protected PillagerMixin(EntityType<? extends AbstractIllager> entityType, Level level) {
            super(entityType, level);
        }

        @Inject(method = "registerGoals", at = @At(value = "HEAD"))
        private void primal$runFromCrocodile(CallbackInfo ci){

            this.goalSelector.addGoal(2,
                    new AvoidEntityGoal<>(
                            this,
                            CrocodileEntity.class,
                            avoidable-> avoidable instanceof CrocodileEntity crocodile && crocodile.hasClock(),
                            12.0F, 1.0, 1.2,
                            living-> true));

        }
    }

    @Mixin(Vindicator.class)
    public abstract static class VindicatorMixin extends AbstractIllager {
        protected VindicatorMixin(EntityType<? extends AbstractIllager> entityType, Level level) {
            super(entityType, level);
        }

        @Inject(method = "registerGoals", at = @At(value = "HEAD"))
        private void primal$runFromCrocodile(CallbackInfo ci){

            this.goalSelector.addGoal(2,
                    new AvoidEntityGoal<>(
                            this,
                            CrocodileEntity.class,
                            avoidable-> avoidable instanceof CrocodileEntity crocodile && crocodile.hasClock(),
                            12.0F, 1.0, 1.2,
                            living-> true));

        }
    }


}
