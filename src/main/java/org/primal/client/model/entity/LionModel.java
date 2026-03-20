package org.primal.client.model.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Pose;
import org.primal.Primal_Main;
import org.primal.client.animation.entity.LionAnimations;
import org.primal.client.model.defaulted.DefaultedEntityWithVariantsWithBabyGeoModel;
import org.primal.entity.animal.LionEntity;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.data.EntityModelData;

public class LionModel extends DefaultedEntityWithVariantsWithBabyGeoModel<LionEntity, LionEntity.Variant> {
    public LionModel() {
        super(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "lion"), true, true, Primal_Main.COMMON_CONFIG.lionBabyCustomModel.get());
    }

    @Override
    public ResourceLocation buildFormattedTexturePath(ResourceLocation basePath, LionEntity lion) {
        if((!lion.isBaby() || !Primal_Main.COMMON_CONFIG.lionBabyCustomModel.get()))
            return basePath.withPath("textures/" + subtype() + "/" + basePath.getPath()+"/" + lion.getVariant().getSerializedName()+ (lion.isManeless() || lion.isBaby()? "_maneless" : "")+  ".png");

        return super.buildFormattedTexturePath(basePath, lion);
    }

    @Override
    public void setCustomAnimations(LionEntity lion, long instanceId, AnimationState<LionEntity> animationState) {
        final AnimationController<GeoAnimatable> controller = lion.getAnimatableInstanceCache().getManagerForId(lion.getId()).getAnimationControllers().get("base_controller");

        //Avoids applying head rotations
        if (controller.isPlayingTriggeredAnimation() || controller.getCurrentRawAnimation()==LionAnimations.LAY) return;

        //Fix a weird flashing when tries to lay
        if((lion.hasPose(Pose.SITTING) || lion.hasPose(Pose.SLIDING)) && this.getBone("head").isPresent()){
            this.getBone("head").get().setRotY(Mth.DEG_TO_RAD * 35f);
            this.getBone("head").get().setRotZ(Mth.DEG_TO_RAD * 90f);
            return;
        }

        if(lion.hasPose(Pose.CROUCHING)){
            GeoBone head = getAnimationProcessor().getBone("head");
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            if (head != null && entityData!=null) {
                head.setRotX((entityData.headPitch() * Mth.DEG_TO_RAD) + (10 * Mth.DEG_TO_RAD));
                head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
            }

            return;
        }

        super.setCustomAnimations(lion, instanceId, animationState);
    }
}
