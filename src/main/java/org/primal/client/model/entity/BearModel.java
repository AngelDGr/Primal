package org.primal.client.model.entity;

import net.minecraft.util.Mth;
import org.primal.Primal_Main;
import org.primal.client.animation.entity.BearAnimations;
import org.primal.entity.animal.BearEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class BearModel extends DefaultedEntityGeoModel<BearEntity> {
    public BearModel() {
        super(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "bear"), true);
    }

    @Override
    public ResourceLocation getTextureResource(BearEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/bear/main_"+animatable.getVariant().getSerializedName()+".png");
    }

    @Override
    public void setCustomAnimations(BearEntity bear, long instanceId, AnimationState<BearEntity> animationState) {
        final AnimationController<GeoAnimatable> controller = bear.getAnimatableInstanceCache().getManagerForId(bear.getId()).getAnimationControllers().get("base_controller");

        if (bear.isBaby() && this.getBone("head").isPresent()) {
            this.getBone("head").get().setPosY(5);
        }

        //Avoids applying head rotations
        if (controller.isPlayingTriggeredAnimation() || controller.getCurrentRawAnimation() == BearAnimations.BEG) {
            return;
        }

        //Fix a weird flashing when tries to sleep
        if(bear.isBearSleeping() && this.getBone("head").isPresent()){
            this.getBone("head").get().setRotY(Mth.DEG_TO_RAD * 17.5f);
            return;
        }

        super.setCustomAnimations(bear, instanceId, animationState);
    }
}
