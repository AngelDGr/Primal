package org.primal.client.renderer.replaced;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Dolphin;
import org.jetbrains.annotations.NotNull;
import org.primal.client.model.replaced.DolphinModel;
import org.primal.client.renderer.defaulted.DefaultedReplacedEntityWithNewVariantsRenderer;
import org.primal.entity.replaced.DolphinReplaced;
import org.primal.util.Primal_Util;
import software.bernie.geckolib.cache.object.BakedGeoModel;

public class DolphinRenderer extends DefaultedReplacedEntityWithNewVariantsRenderer<Dolphin, DolphinReplaced, DolphinReplaced.Variant> {

    public DolphinRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DolphinModel(), new DolphinReplaced(), "dolphin");
        shadowRadius=0.7F;
    }

    @Override
    protected float getShadowRadius(@NotNull Dolphin entity) {
        float f = super.getShadowRadius(entity);
        return entity.isBaby() ? f * 0.35F : f;
    }

    @Override
    public void scaleModelForRender(float widthScale, float heightScale, PoseStack poseStack, DolphinReplaced animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
        var bone = model.getBone("head");
        float headScale = this.currentEntity.isBaby() ? 2.f : 1.f;
        bone.ifPresent(geoBone ->
                geoBone.updateScale(headScale, headScale, headScale));
        if (this.currentEntity.isBaby()) {
            widthScale = heightScale = .5f;
        }
        super.scaleModelForRender(widthScale, heightScale, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
    }

    @Override
    protected void applyRotations(DolphinReplaced animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        Primal_Util.Visuals.bodyFullRotations(this.currentEntity, partialTick, poseStack);
    }

    @Override
    public ResourceLocation getTextureLocation(DolphinReplaced animatable) {
        Dolphin dolphin = this.currentEntity;
        CompoundTag mainTag = new CompoundTag();
        dolphin.saveWithoutId(mainTag);
        var variant = Primal_Util.Visuals.getNomanslandVariant(mainTag, "dolphin", "", "river");
        if (variant != null) return variant;

        return super.getTextureLocation(animatable);
    }
}
