package org.primal.client.model.entity;

import net.minecraft.resources.ResourceLocation;
import org.primal.Primal_Main;
import org.primal.client.animation.entity.WalrusAnimations;
import org.primal.client.model.defaulted.DefaultedEntityWithVariantsWithBabyGeoModel;
import org.primal.entity.animal.WalrusEntity;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;

public class WalrusModel extends DefaultedEntityWithVariantsWithBabyGeoModel<WalrusEntity, WalrusEntity.Variant> {
    public WalrusModel() {
        super(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "walrus"), true, false, Primal_Main.COMMON_CONFIG.walrusBabyCustomModel.get());
    }

    @Override
    public void setCustomAnimations(WalrusEntity walrus, long instanceId, AnimationState<WalrusEntity> animationState) {
        final AnimationController<GeoAnimatable> controller = walrus.getAnimatableInstanceCache().getManagerForId(walrus.getId()).getAnimationControllers().get("base_controller");

        //Avoids applying head rotations
        if (controller.isPlayingTriggeredAnimation()
                || controller.getCurrentRawAnimation() == WalrusAnimations.LAY_START
                || controller.getCurrentRawAnimation() == WalrusAnimations.LAY
                || controller.getCurrentRawAnimation() == WalrusAnimations.LAY_END
                || controller.getCurrentRawAnimation() == WalrusAnimations.SWIM_ATTACK
                || controller.getCurrentRawAnimation() == WalrusAnimations.PLAY) return;

        if(!walrus.isInWater())
            super.setCustomAnimations(walrus, instanceId, animationState);
    }
}
