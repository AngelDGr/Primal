package org.primal.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.primal.entity.animal.LionEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Creeper.class)
public abstract class CreeperMixin extends Monster implements PowerableMob {
    protected CreeperMixin(EntityType<? extends Monster> entityType, Level level) {super(entityType, level);}

    @Inject(method = "registerGoals", at = @At("HEAD"))
    private void primal$addNewCreeperGoals(final CallbackInfo ci){
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, LionEntity.class, 6.0F, 1.0, 1.2));
    }
}
