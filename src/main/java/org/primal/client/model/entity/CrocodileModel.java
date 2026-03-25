package org.primal.client.model.entity;

import net.minecraft.resources.ResourceLocation;
import org.primal.Primal_Main;
import org.primal.client.animation.entity.CrocodileAnimations;
import org.primal.client.model.defaulted.DefaultedEntityWithVariantsWithBabyGeoModel;
import org.primal.entity.animal.CrocodileEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;

public class CrocodileModel extends DefaultedEntityWithVariantsWithBabyGeoModel<CrocodileEntity, CrocodileEntity.Variant> {
    public CrocodileModel() {
        super(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "crocodile"), true, true, Primal_Main.COMMON_CONFIG.crocodileBabyCustomModel.get());
    }

    @Override
    public void setCustomAnimations(CrocodileEntity crocodile, long instanceId, AnimationState<CrocodileEntity> animationState) {
        final AnimationController<GeoAnimatable> controller = crocodile.getAnimatableInstanceCache().getManagerForId(crocodile.getId()).getAnimationControllers().get("base_controller");

        //Avoids applying head rotations
        if (controller.isPlayingTriggeredAnimation()
                || controller.getCurrentRawAnimation() == CrocodileAnimations.SWIM_IDLE
                || controller.getCurrentRawAnimation() == CrocodileAnimations.TRASH
                || controller.getCurrentRawAnimation() == CrocodileAnimations.TRASH_UNDERWATER

                || controller.getCurrentRawAnimation() == CrocodileAnimations.BASKING_END
                || controller.getCurrentRawAnimation() == CrocodileAnimations.BASKING
                || controller.getCurrentRawAnimation() == CrocodileAnimations.BASKING_START) {
            return;
        }

        if(!crocodile.isInWater())
            super.setCustomAnimations(crocodile, instanceId, animationState);
    }
}
