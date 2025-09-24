package org.primal.client.model.replaced;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Fox;
import org.primal.Primal_Main;
import org.primal.client.animation.replaced.FoxAnimations;
import org.primal.entity.replaced.FoxReplaced;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class FoxModel extends DefaultedEntityGeoModel<FoxReplaced> {
    public FoxModel() {
        super(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "fox"), true);
    }

    @Override
    public void setCustomAnimations(FoxReplaced animatable, long instanceId, AnimationState<FoxReplaced> animationState) {
        Fox fox = animatable.getEntityFromState(animationState);

        final AnimationController<GeoAnimatable> controller = animatable.getAnimatableInstanceCache().getManagerForId(fox.getId()).getAnimationControllers().get("base_controller");

        //Disables tongue if it has something on the mouth
        GeoBone bone = getAnimationProcessor().getBone("tongue");
        if(bone!=null){
            bone.setHidden(!fox.getMainHandItem().isEmpty());
        }

        //Avoids applying head rotations
        if (controller.isPlayingTriggeredAnimation() || controller.getCurrentRawAnimation()== FoxAnimations.SIT)
            return;

        super.setCustomAnimations(animatable, instanceId, animationState);
    }
}
