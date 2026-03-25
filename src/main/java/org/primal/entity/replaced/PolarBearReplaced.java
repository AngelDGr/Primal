package org.primal.entity.replaced;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.PolarBear;
import org.jetbrains.annotations.Nullable;
import org.primal.client.animation.replaced.PolarBearAnimations;
import software.bernie.geckolib.animatable.GeoReplacedEntity;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.util.GeckoLibUtil;

public class PolarBearReplaced implements GeoReplacedEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public PolarBearReplaced(){
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(
                PolarBearAnimations.mainController(this),
                PolarBearAnimations.attackController(this));
    }

    @Override
    public EntityType<?> getReplacingEntityType() {
        return EntityType.POLAR_BEAR;
    }

    @Nullable
    public PolarBear getEntityFromState(AnimationState<PolarBearReplaced> state) {
        Entity entity = state.getData(DataTickets.ENTITY);
        if (!(entity instanceof PolarBear polarBear)) return null;
        return polarBear;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
