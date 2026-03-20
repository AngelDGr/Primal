package org.primal.mixin;

import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.level.Level;
import org.objectweb.asm.Opcodes;
import org.primal.entity.animal.LionEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Phantom.class)
public abstract class PhantomMixin extends FlyingMob implements Enemy {

    protected PhantomMixin(EntityType<? extends FlyingMob> entityType, Level level) {super(entityType, level);}

    @SuppressWarnings("unused")
    @Mixin(targets = "net.minecraft.world.entity.monster.Phantom$PhantomSweepAttackGoal")
    public abstract static class PhantomSweepAttackGoal {

        @Shadow private boolean isScaredOfCat;

        @Final @Shadow Phantom this$0;

        @Inject(
                method = "canContinueToUse",
                at = @At(
                        value = "FIELD",
                        target = "Lnet/minecraft/world/entity/monster/Phantom$PhantomSweepAttackGoal;isScaredOfCat:Z",
                        opcode = Opcodes.PUTFIELD,
                        shift = At.Shift.AFTER
                )
        )
        private void primal$scareOfPrimalCatsToo(CallbackInfoReturnable<Boolean> cir) {
            List<LionEntity> list = this$0.level()
                    .getEntitiesOfClass(LionEntity.class, this$0.getBoundingBox().inflate(16.0), EntitySelector.ENTITY_STILL_ALIVE);

            this.isScaredOfCat |= !list.isEmpty();
        }
    }

}
