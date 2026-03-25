package org.primal.client.model.entity;

import net.minecraft.resources.ResourceLocation;
import org.primal.Primal_Main;
import org.primal.client.model.defaulted.DefaultedEntityWithVariantsWithBabyGeoModel;
import org.primal.entity.animal.DeerEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;

public class DeerModel extends DefaultedEntityWithVariantsWithBabyGeoModel<DeerEntity, DeerEntity.Variant> {
    public DeerModel() {
        super(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "deer"), true, true, Primal_Main.COMMON_CONFIG.deerBabyCustomModel.get());
    }

    @Override
    public void setCustomAnimations(DeerEntity deer, long instanceId, AnimationState<DeerEntity> animationState) {
        final AnimationController<GeoAnimatable> controller = deer.getAnimatableInstanceCache().getManagerForId(deer.getId()).getAnimationControllers().get("base_controller");

        CoreGeoBone rightAntler = getAnimationProcessor().getBone("right_antler");
        CoreGeoBone leftAntler = getAnimationProcessor().getBone("left_antler");

        if(rightAntler!=null && leftAntler!=null){
            rightAntler.setHidden(!deer.hasRightAntler() || deer.isBaby());
            leftAntler.setHidden(!deer.hasLeftAntler() || deer.isBaby());
        }

        //Avoids applying head rotations
        if (controller.isPlayingTriggeredAnimation()) return;

        super.setCustomAnimations(deer, instanceId, animationState);
    }
}
