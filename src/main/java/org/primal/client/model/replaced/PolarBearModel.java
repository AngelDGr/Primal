package org.primal.client.model.replaced;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.PolarBear;
import org.primal.Primal_Main;
import org.primal.client.animation.replaced.PolarBearAnimations;
import org.primal.entity.replaced.PolarBearReplaced;
import org.primal.util.Primal_Util;
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
        if(polarBear==null) return;

        final AnimationController<GeoAnimatable> controller = animatable.getAnimatableInstanceCache().getManagerForId(polarBear.getId()).getAnimationControllers().get("base_controller");
        final AnimationController<GeoAnimatable> controller_attacker = animatable.getAnimatableInstanceCache().getManagerForId(polarBear.getId()).getAnimationControllers().get("attack");

        //Avoids applying head rotations
        if (controller.isPlayingTriggeredAnimation()
                || controller_attacker.getCurrentRawAnimation() == PolarBearAnimations.ATTACK
                || polarBear.isStanding())
            return;

        Primal_Util.Visuals.headRotationsSwimming(this.getAnimationProcessor(), polarBear, animationState);
    }
}
