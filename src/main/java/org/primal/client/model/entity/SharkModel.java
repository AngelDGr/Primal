package org.primal.client.model.entity;

import net.minecraft.resources.ResourceLocation;
import org.primal.Primal_Main;
import org.primal.entity.animal.SharkEntity;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class SharkModel extends DefaultedEntityGeoModel<SharkEntity> {
    public SharkModel() {
        super(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "shark"), false);
    }

    @Override
    public ResourceLocation getTextureResource(SharkEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/shark/"+animatable.getVariant().getSerializedName()+".png");
    }

    @Override
    public void setCustomAnimations(SharkEntity bear, long instanceId, AnimationState<SharkEntity> animationState) {
    }
}
