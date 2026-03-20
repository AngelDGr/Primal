package org.primal.client.model.replaced;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;
import org.primal.Primal_Main;
import org.primal.entity.replaced.WolfReplaced;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class WolfModel extends DefaultedEntityGeoModel<WolfReplaced> {
    public WolfModel() {
        super(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "wolf"), true);
    }

    @Override
    public void setCustomAnimations(WolfReplaced animatable, long instanceId, AnimationState<WolfReplaced> animationState) {
        Wolf wolf = animatable.getEntityFromState(animationState);
        //Avoids applying head rotations
//        if (controller.isPlayingTriggeredAnimation()) return;

        GeoBone tail = getAnimationProcessor().getBone("tail");
        //Health rising from health, when not sitting and only tamed
        if(!wolf.isInSittingPose() && wolf.isTame()){
            double healthPercentage = wolf.getHealth()/wolf.getMaxHealth();

            //Max -80° -> 1.0
            //Min 20° -> 0.0
            // Clamp just in case
            healthPercentage = Math.max(0.0, Math.min(1.0, healthPercentage));

            // 0.0 -> 20°, 1.0 -> -80°
            double angleDeg = 20.0 + (-80.0 - 20.0) * healthPercentage;

            tail.setRotX(tail.getRotX ()+ (float) Math.toRadians(angleDeg));
        }


        super.setCustomAnimations(animatable, instanceId, animationState);
        GeoBone head = getAnimationProcessor().getBone("head");
        if(wolf.isInSittingPose())
            head.setRotX((float) (head.getRotX() + Math.toRadians(-30)));
    }
}
