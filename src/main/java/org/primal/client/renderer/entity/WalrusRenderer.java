package org.primal.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.Primal_Main;
import org.primal.client.model.entity.WalrusModel;
import org.primal.client.renderer.entity.layer.walrus.WalrusRiptideLayer;
import org.primal.client.renderer.entity.layer.walrus.WalrusSaddleLayer;
import org.primal.entity.animal.WalrusEntity;
import org.primal.util.Primal_Util;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

@OnlyIn(Dist.CLIENT)
public class WalrusRenderer extends GeoEntityRenderer<WalrusEntity> {

    private static final String LEFT_HAND = "left_item";
    private static final String RIGHT_HAND = "right_item";

    protected ItemStack mainHandItem;
    protected ItemStack offHandItem;
    public WalrusRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new WalrusModel());
        this.addRenderLayer(new WalrusRiptideLayer(this));
        this.addRenderLayer(new WalrusSaddleLayer(this));

        //Item on hand renderer
        addRenderLayer(new BlockAndItemGeoLayer<>(this) {
            @Nullable
            @Override
            protected ItemStack getStackForBone(GeoBone bone, WalrusEntity animatable) {

                if(bone.getName().equals(LEFT_HAND))
                    return animatable.isLeftHanded()? WalrusRenderer.this.mainHandItem: WalrusRenderer.this.offHandItem;

                if(bone.getName().equals(RIGHT_HAND))
                    return animatable.isLeftHanded()? WalrusRenderer.this.offHandItem:  WalrusRenderer.this.mainHandItem;

                return null;
            }

            @Override
            protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack, WalrusEntity animatable) {
                if(bone.getName().equals(LEFT_HAND) || bone.getName().equals(RIGHT_HAND))
                    return ItemDisplayContext.GROUND;

                return ItemDisplayContext.NONE;
            }

            // Do some quick render modifications depending on what the item is
            @Override
            protected void renderStackForBone(PoseStack poseStack, GeoBone bone, ItemStack stack, WalrusEntity walrus,
                                              MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay) {
                if(!walrus.hasPose(Pose.ROARING)) return;

                if (stack == WalrusRenderer.this.mainHandItem) {
                    float f = 1.75F;
                    if(walrus.isLeftHanded())
                        poseStack.translate(-0.2f, 0.0, 0.2);
                    else
                        poseStack.translate(0.2f, 0, 0.2);
                    poseStack.scale(f, f, f);


                    if(walrus.isLeftHanded()){
                        poseStack.mulPose(Axis.ZP.rotationDegrees(85));
                        poseStack.mulPose(Axis.YP.rotationDegrees((-35)+180));
                        poseStack.mulPose(Axis.XN.rotationDegrees(5));
                    } else {
                        poseStack.mulPose(Axis.ZN.rotationDegrees(85));
                        poseStack.mulPose(Axis.YN.rotationDegrees(-35));
                        poseStack.mulPose(Axis.XN.rotationDegrees(-5));
                    }
                }

                super.renderStackForBone(poseStack, bone, stack, walrus, bufferSource, partialTick, packedLight, packedOverlay);
            }
        });

        shadowRadius=1.00F;
    }

    @Override
    public void preRender(PoseStack poseStack, WalrusEntity animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);

        this.mainHandItem = animatable.getMainHandItem();
        this.offHandItem = animatable.getOffhandItem();
    }

    @Override
    public void render(WalrusEntity entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        if (entity.isBaby()) this.shadowRadius *= 0.5F;
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    protected void applyRotations(WalrusEntity animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick) {
        if(animatable.isInWater() && !animatable.isVehicle()) {
            Primal_Util.Visuals.bodyFullRotations(animatable, partialTick, poseStack);
        } else
            super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick);
    }

    @Override
    public void scaleModelForRender(float widthScale, float heightScale, PoseStack poseStack, WalrusEntity animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
        if(!Primal_Main.COMMON_CONFIG.walrusBabyCustomModel.get()){
            var bone = model.getBone("head");
            float headScale = animatable.isBaby() ? 1.5f : 1.f;
            bone.ifPresent(geoBone ->
                    geoBone.updateScale(headScale, headScale, headScale));
            if (animatable.isBaby()) {
                widthScale = heightScale = .25f;
            }
        }

        super.scaleModelForRender(widthScale, heightScale, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
    }
}