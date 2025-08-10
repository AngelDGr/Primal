package org.primal.client.model.entity;

import org.primal.Primal_Main;
import org.primal.client.animation.entity.BearAnimations;
import org.primal.entity.animal.Bear;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class BearModel extends DefaultedEntityGeoModel<Bear> {
    public BearModel() {
        super(ResourceLocation.fromNamespaceAndPath(Primal_Main.MODID, "bear"), true);
    }

    @Override
    public ResourceLocation getTextureResource(Bear animatable) {
        return ResourceLocation.fromNamespaceAndPath(Primal_Main.MODID, String.format("textures/entity/bear/grizzly_bear_texture%s.png", animatable.getVariant() == Bear.Variant.ASIATIC ? "_warm" : ""));
    }

    @Override
    public void setCustomAnimations(Bear animatable, long instanceId, AnimationState<Bear> animationState) {
        final AnimationController controller = animatable.getAnimatableInstanceCache().getManagerForId(animatable.getId()).getAnimationControllers().get("base_controller");
        if (animatable.isBaby()) {
            this.getBone("head").get().setPosY(5);
        }
        if (controller.isPlayingTriggeredAnimation() || controller.getCurrentRawAnimation() == BearAnimations.BEG || controller.getCurrentRawAnimation() == BearAnimations.SLEEP) {
            animatable.setYHeadRot(0);
            animatable.setXRot(0);
            return;
        }
        super.setCustomAnimations(animatable, instanceId, animationState);
    }
}
