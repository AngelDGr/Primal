package org.primal.client.model.entity;

import net.minecraft.resources.ResourceLocation;
import org.primal.Primal_Main;
import org.primal.client.model.defaulted.DefaultedEntityWithVariantsWithBabyGeoModel;
import org.primal.entity.animal.DeerEntity;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;

public class DeerModel extends DefaultedEntityWithVariantsWithBabyGeoModel<DeerEntity, DeerEntity.Variant> {
    public DeerModel() {
        super(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "deer"), true, true, Primal_Main.COMMON_CONFIG.deerBabyCustomModel.get());
    }

    @Override
    public void setCustomAnimations(DeerEntity deer, long instanceId, AnimationState<DeerEntity> animationState) {
        final AnimationController<GeoAnimatable> controller = deer.getAnimatableInstanceCache().getManagerForId(deer.getId()).getAnimationControllers().get("base_controller");

        GeoBone rightAntler = getAnimationProcessor().getBone("right_antler");
        GeoBone leftAntler = getAnimationProcessor().getBone("left_antler");

        if(rightAntler!=null && leftAntler!=null){
            rightAntler.setHidden(!deer.hasRightAntler() || deer.isBaby());
            leftAntler.setHidden(!deer.hasLeftAntler() || deer.isBaby());
        }

        //Avoids applying head rotations
        if (controller.isPlayingTriggeredAnimation()) return;

        super.setCustomAnimations(deer, instanceId, animationState);
    }
}
