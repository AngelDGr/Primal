package org.primal.client.model.replaced;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Rabbit;
import org.primal.Primal_Main;
import org.primal.entity.replaced.RabbitReplaced;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class RabbitModel extends DefaultedEntityGeoModel<RabbitReplaced> {
    public RabbitModel() {
        super(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "rabbit"), true);
    }

    @Override
    public void setCustomAnimations(RabbitReplaced animatable, long instanceId, AnimationState<RabbitReplaced> animationState) {
        Rabbit polarBear = animatable.getEntityFromState(animationState);

        final AnimationController<GeoAnimatable> controller = animatable.getAnimatableInstanceCache().getManagerForId(polarBear.getId()).getAnimationControllers().get("base_controller");

        //Avoids applying head rotations
        if (controller.isPlayingTriggeredAnimation())
            return;

        super.setCustomAnimations(animatable, instanceId, animationState);
    }
}
