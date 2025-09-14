package org.primal.client.model.entity;

import net.minecraft.resources.ResourceLocation;
import org.primal.Primal_Main;
import org.primal.entity.animal.EagleEntity;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class EagleModel extends DefaultedEntityGeoModel<EagleEntity> {
    public EagleModel() {
        super(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "eagle"), true);
    }

    @Override
    public ResourceLocation getAnimationResource(EagleEntity eagle) {
        if(eagle.isBaby()){
            return buildFormattedAnimationPath(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "eagle_chick"));
        }

        return super.getAnimationResource(eagle);
    }

    @Override
    public ResourceLocation getModelResource(EagleEntity eagle) {
        if(eagle.isBaby()){
            return buildFormattedModelPath(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "eagle_chick"));
        }

        return super.getModelResource(eagle);
    }

    @Override
    public ResourceLocation getTextureResource(EagleEntity eagle) {
        if(eagle.isBaby()){
            return buildFormattedTexturePath(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "eagle/chick"));
        }

        return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/eagle/"+eagle.getVariant().getSerializedName()+".png");
    }

    @Override
    public void setCustomAnimations(EagleEntity eagle, long instanceId, AnimationState<EagleEntity> animationState) {
        final AnimationController<GeoAnimatable> controller = eagle.getAnimatableInstanceCache().getManagerForId(eagle.getId()).getAnimationControllers().get("base_controller");

        if(eagle.onGround() || eagle.isBaby())
            super.setCustomAnimations(eagle, instanceId, animationState);
    }
}
