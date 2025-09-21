package org.primal.client.model.replaced;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.PolarBear;
import org.primal.Primal_Main;
import org.primal.entity.replaced.PolarBearReplaced;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class PolarBearModel extends DefaultedEntityGeoModel<PolarBearReplaced> {
    public PolarBearModel() {
        super(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "polar_bear"), true);
    }

    @Override
    public void setCustomAnimations(PolarBearReplaced animatable, long instanceId, AnimationState<PolarBearReplaced> animationState) {
        PolarBear polarBear = animatable.getEntityFromState(animationState);

        final AnimationController<GeoAnimatable> controller = animatable.getAnimatableInstanceCache().getManagerForId(polarBear.getId()).getAnimationControllers().get("base_controller");

        //Avoids applying head rotations
        if (controller.isPlayingTriggeredAnimation())
            return;

        super.setCustomAnimations(animatable, instanceId, animationState);
    }
}
