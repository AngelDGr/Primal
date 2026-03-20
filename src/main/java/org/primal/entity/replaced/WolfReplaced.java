package org.primal.entity.replaced;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Wolf;
import org.jetbrains.annotations.Nullable;
import org.primal.client.animation.replaced.WolfAnimations;
import software.bernie.geckolib.animatable.GeoReplacedEntity;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.util.GeckoLibUtil;

public class WolfReplaced implements GeoReplacedEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public WolfReplaced(){
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public EntityType<Wolf> getReplacingEntityType() {
        return EntityType.WOLF;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(WolfAnimations.mainController(this));
    }

    @Nullable
    public Wolf getEntityFromState(AnimationState<WolfReplaced> state) {
        Entity entity = state.getData(DataTickets.ENTITY);
        if (!(entity instanceof Wolf wolf)) return null;
        return wolf;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
