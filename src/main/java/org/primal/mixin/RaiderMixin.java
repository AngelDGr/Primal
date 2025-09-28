package org.primal.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import org.primal.entity.animal.EagleEntity;
import org.primal.registry.Primal_Advancements;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Raider.class)
public abstract class RaiderMixin extends PatrollingMonster {

    @Shadow public abstract boolean isCaptain();

    protected RaiderMixin(EntityType<? extends PatrollingMonster> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "die", at = @At(value = "HEAD"))
    private void primal$triggerAdvancement(DamageSource cause, CallbackInfo ci){
        //Triggers advancement if dies by a direct eagle attack, or if it has a nearby tamed following eagle when dying for fall damage, made this way so it counts the fall damage
        if(isCaptain()){
            List<EagleEntity> nearEagles =
                    this.level().getEntitiesOfClass(
                            EagleEntity.class,
                            this.getBoundingBox().inflate(30),
                            eagle -> eagle.isTame() && eagle.isFollowing());

            if(!nearEagles.isEmpty()){
                for (EagleEntity eagle: nearEagles){
                    if(eagle.getOwner()!=null && eagle.getOwner() instanceof ServerPlayer serverPlayer)
                        primal$triggerAdvancementKillCaptain(serverPlayer);
                }
            }
        }
    }

    @Unique
    private void primal$triggerAdvancementKillCaptain(ServerPlayer serverPlayer){
        Primal_Advancements.KILL_CAPTAIN.get().trigger(serverPlayer);
    }
}
