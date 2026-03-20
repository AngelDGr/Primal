package org.primal.client.model.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.primal.Primal_Main;
import org.primal.client.animation.entity.SnakeAnimations;
import org.primal.client.model.defaulted.DefaultedEntityWithVariantsWithBabyGeoModel;
import org.primal.entity.animal.SnakeEntity;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.data.EntityModelData;

public class SnakeModel extends DefaultedEntityWithVariantsWithBabyGeoModel<SnakeEntity, SnakeEntity.Variant> {
    public SnakeModel() {
        super(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "snake"), true, true, Primal_Main.COMMON_CONFIG.snakeBabyCustomModel.get());
    }

    @Override
    public void setCustomAnimations(SnakeEntity snake, long instanceId, AnimationState<SnakeEntity> animationState) {
        final AnimationController<GeoAnimatable> controller = snake.getAnimatableInstanceCache().getManagerForId(snake.getId()).getAnimationControllers().get("base_controller");

        GeoBone head = getAnimationProcessor().getBone("head");

        //Avoids applying head rotations
        if (controller.isPlayingTriggeredAnimation() || controller.getCurrentRawAnimation()== SnakeAnimations.SIT || controller.getCurrentRawAnimation()== SnakeAnimations.WRAPPED) return;

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            float offsetRad = -107.5f * Mth.DEG_TO_RAD;
            float yaw = entityData.netHeadYaw() * Mth.DEG_TO_RAD;

            head.setRotX(((entityData.headPitch() + (snake.isSlithering()? 0: -107.5f)) * Mth.DEG_TO_RAD));

            if(snake.isSlithering()) head.setRotY(yaw);
            else head.setRotY(Mth.cos(offsetRad) * yaw);

            if (snake.isSlithering()) head.setRotZ(0);
            else head.setRotZ(Mth.sin(offsetRad) * yaw);
        }
    }
}
