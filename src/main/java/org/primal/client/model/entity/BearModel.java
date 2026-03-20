package org.primal.client.model.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.primal.Primal_Main;
import org.primal.client.animation.entity.BearAnimations;
import org.primal.client.model.defaulted.DefaultedEntityWithVariantsWithBabyGeoModel;
import org.primal.entity.animal.BearEntity;
import org.primal.util.Primal_Util;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;

public class BearModel extends DefaultedEntityWithVariantsWithBabyGeoModel<BearEntity, BearEntity.Variant> {
    public BearModel() {
        super(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "bear"), true, true, Primal_Main.COMMON_CONFIG.bearBabyCustomModel.get());
    }

    @Override
    public ResourceLocation getModelResource(BearEntity animatable) {
        return animatable.isBaby() && Primal_Main.COMMON_CONFIG.bearBabyCustomModel.get()?
                super.getModelResource(animatable):
                ResourceLocation.fromNamespaceAndPath(
                        Primal_Main.MOD_ID,
                        "geo/entity/"+ (animatable.getVariant().equals(BearEntity.Variant.GROLAR)? "grolar": "bear") +".geo.json");
    }

    @Override
    public void setCustomAnimations(BearEntity bear, long instanceId, AnimationState<BearEntity> animationState) {
        final AnimationController<GeoAnimatable> controller = bear.getAnimatableInstanceCache().getManagerForId(bear.getId()).getAnimationControllers().get("base_controller");

        //Avoids applying head rotations
        if (controller.isPlayingTriggeredAnimation()
                || controller.getCurrentRawAnimation() == BearAnimations.BEG
                || controller.getCurrentRawAnimation() == BearAnimations.SLEEP) return;

        //Fix a weird flashing when tries to sleep
        if(bear.isBearSleeping() && this.getBone("head").isPresent()){
            this.getBone("head").get().setRotY(Mth.DEG_TO_RAD * (bear.isBaby()? -20f: 17.5f));
            return;
        }

        Primal_Util.Visuals.headRotationsSwimming(this.getAnimationProcessor(), bear, animationState);
    }
}
