package org.primal.mixin;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import org.primal.Primal_Main;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.AnimationController;

@Mixin(AgeableMob.class)
public abstract class AgeableMobMixin extends PathfinderMob {

    @Shadow @Final private static EntityDataAccessor<Boolean> DATA_BABY_ID;

    protected AgeableMobMixin(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    AgeableMob p$THIS = (AgeableMob)(Object)this;

    @Inject(method = "onSyncedDataUpdated", at = @At("HEAD"))
    private void primal$updateAnimationsWhenGrowingUp(EntityDataAccessor<?> key, CallbackInfo ci){
        ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(this.getType());
        if(DATA_BABY_ID.equals(key) && p$THIS instanceof GeoEntity geo && id.getNamespace().equals(Primal_Main.MOD_ID)){
            if (geo.getAnimatableInstanceCache()!=null) {
                var manager = geo.getAnimatableInstanceCache().getManagerForId(this.getId());
                manager.getAnimationControllers().values().forEach(AnimationController::forceAnimationReset);
            }
        }
    }
}
