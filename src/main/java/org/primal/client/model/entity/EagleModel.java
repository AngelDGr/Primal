package org.primal.client.model.entity;

import net.minecraft.resources.ResourceLocation;
import org.primal.Primal_Main;
import org.primal.client.animation.entity.EagleAnimations;
import org.primal.client.model.defaulted.DefaultedEntityWithVariantsWithBabyGeoModel;
import org.primal.entity.animal.EagleEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;

public class EagleModel extends DefaultedEntityWithVariantsWithBabyGeoModel<EagleEntity, EagleEntity.Variant> {
    public EagleModel() {
        super(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "eagle"), true, false, true);
    }

    @Override
    public void setCustomAnimations(EagleEntity eagle, long instanceId, AnimationState<EagleEntity> animationState) {
        boolean applyFullRotations = !eagle.onGround() && !eagle.isBaby();
        final AnimationController<GeoAnimatable> controller = eagle.getAnimatableInstanceCache().getManagerForId(eagle.getId()).getAnimationControllers().get("base_controller");

        CoreGeoBone leftIdleFeathers = getAnimationProcessor().getBone("left_idle_feathers");
        CoreGeoBone rightIdleFeathers = getAnimationProcessor().getBone("right_idle_feathers");

        CoreGeoBone leftFlightFeathers = getAnimationProcessor().getBone("left_flight_feathers");
        CoreGeoBone rightFlightFeathers = getAnimationProcessor().getBone("right_flight_feathers");

        if(leftIdleFeathers != null && rightIdleFeathers!=null && leftFlightFeathers!=null && rightFlightFeathers!=null){
            if(controller.getCurrentRawAnimation()!=null){
                boolean flying = (controller.getCurrentRawAnimation().equals(EagleAnimations.FLY)
                        || controller.getCurrentRawAnimation().equals(EagleAnimations.SWOOP)
                        || controller.getCurrentRawAnimation().equals(EagleAnimations.GLIDE));

                leftIdleFeathers .setHidden(flying);
                rightIdleFeathers.setHidden(flying);

                leftFlightFeathers .setHidden(!leftIdleFeathers. isHidden());
                rightFlightFeathers.setHidden(!rightIdleFeathers.isHidden());

                //Fix for field book
                if(eagle.tickCount==0){
                    leftFlightFeathers .setHidden(true);
                    rightFlightFeathers.setHidden(true);

                    leftIdleFeathers .setHidden(false);
                    rightIdleFeathers.setHidden(false);
                }


            }
        }

        if(!applyFullRotations)
            super.setCustomAnimations(eagle, instanceId, animationState);
    }
}
