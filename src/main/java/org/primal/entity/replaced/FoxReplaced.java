package org.primal.entity.replaced;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Fox;
import org.jetbrains.annotations.Nullable;
import org.primal.client.animation.replaced.FoxAnimations;
import software.bernie.geckolib.animatable.GeoReplacedEntity;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.util.GeckoLibUtil;

public class FoxReplaced implements GeoReplacedEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public FoxReplaced(){
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public EntityType<Fox> getReplacingEntityType() {
        return EntityType.FOX;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(FoxAnimations.mainController(this));
    }

    @Nullable
    public Fox getEntityFromState(AnimationState<FoxReplaced> state) {
        Entity entity = state.getData(DataTickets.ENTITY);
        if (!(entity instanceof Fox polarBear)) return null;
        return polarBear;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
