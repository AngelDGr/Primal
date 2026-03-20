package org.primal.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.level.Level;
import org.primal.util.mob_types.AttackVillagers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IronGolem.class)
public abstract class IronGolemMixin extends AbstractGolem implements NeutralMob {
    protected IronGolemMixin(final EntityType<? extends AbstractGolem> entityType, final Level world) {
        super(entityType, world);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void primal$addAttackAnimals(CallbackInfo ci){
        this.goalSelector.addGoal(
                3,
                new NearestAttackableTargetGoal<>(this,
                        Mob.class,
                        true,
                        mob ->
                                mob instanceof AttackVillagers attackVillagers && attackVillagers.isTargetingVillager()
                )
        );
    }

}

