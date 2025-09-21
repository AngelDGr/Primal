package org.primal.client.model.entity;

import net.minecraft.resources.ResourceLocation;
import org.primal.Primal_Main;
import org.primal.client.animation.entity.CrocodileAnimations;
import org.primal.entity.animal.CrocodileEntity;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class CrocodileModel extends DefaultedEntityGeoModel<CrocodileEntity> {
    public CrocodileModel() {
        super(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "crocodile"), true);
    }

    @Override
    public ResourceLocation getTextureResource(CrocodileEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/crocodile/"+animatable.getVariant().getSerializedName()+".png");
    }

    @Override
    public void setCustomAnimations(CrocodileEntity crocodile, long instanceId, AnimationState<CrocodileEntity> animationState) {
        final AnimationController<GeoAnimatable> controller = crocodile.getAnimatableInstanceCache().getManagerForId(crocodile.getId()).getAnimationControllers().get("base_controller");

        //Avoids applying head rotations
        if (controller.isPlayingTriggeredAnimation()
                || controller.getCurrentRawAnimation() == CrocodileAnimations.SWIM_IDLE
                || controller.getCurrentRawAnimation() == CrocodileAnimations.TRASH
                || controller.getCurrentRawAnimation() == CrocodileAnimations.TRASH_UNDERWATER
                || controller.getCurrentRawAnimation() == CrocodileAnimations.BASKING) {
            return;
        }

        if(!crocodile.isUnderWater())
            super.setCustomAnimations(crocodile, instanceId, animationState);
    }
}
