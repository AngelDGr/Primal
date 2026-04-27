package org.primal.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.client.model.entity.WalrusModel;
import org.primal.client.renderer.defaulted.MobRendererWithCustomBaby;
import org.primal.client.renderer.entity.layer.walrus.WalrusRiderLayer;
import org.primal.client.renderer.entity.layer.walrus.WalrusRiptideLayer;
import org.primal.client.renderer.entity.layer.walrus.WalrusSaddleLayer;
import org.primal.entity.animal.WalrusEntity;

@OnlyIn(Dist.CLIENT)
public class WalrusRenderer extends MobRendererWithCustomBaby.WithVariants<WalrusEntity, WalrusModel<WalrusEntity>, WalrusEntity.Variant> {

    public WalrusRenderer(EntityRendererProvider.Context context) {
        super(context,
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "walrus"),
                new WalrusModel.Adult<>(context.bakeLayer(WalrusModel.Adult.LAYER_LOCATION)),
                new WalrusModel.Baby<>(context.bakeLayer(WalrusModel.Baby.LAYER_LOCATION)),
                1.0f,
                false, Primal_Main.COMMON_CONFIG.walrusBabyCustomModel.get());
        this.addLayer(new WalrusRiptideLayer<>(this));
        this.addLayer(new WalrusSaddleLayer<>(this));
        this.addLayer(new WalrusRiderLayer<>(this));

        //Item on hand renderer
        this.addLayer(new RenderLayer<>(this) {
            @Override
            public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, @NotNull WalrusEntity walrus, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
                if(!walrus.hasPose(Pose.ROARING) || walrus.isBaby()) return;

                poseStack.pushPose();

                var model = (WalrusModel.Adult<?>) this.getParentModel();

                ModelPart walrusBody = model.walrus;
                ModelPart rightArm = model.right_arm;
                ModelPart rightHand = model.right_hand;
                ModelPart rightItem = model.right_item;
                poseStack.translate(0, -0.5, -0.15);

                walrusBody.translateAndRotate(poseStack);
                rightArm.translateAndRotate(poseStack);
                rightHand.translateAndRotate(poseStack);
                rightItem.translateAndRotate(poseStack);

                poseStack.translate(0.2f, 0, 0.2);

                poseStack.mulPose(Axis.ZN.rotationDegrees(-90));
                poseStack.mulPose(Axis.YN.rotationDegrees(-35));
                poseStack.mulPose(Axis.XN.rotationDegrees(-2));

                float f = 1.75F;
                poseStack.scale(f, f, f);

                ItemStack itemstack = walrus.getItemBySlot(EquipmentSlot.MAINHAND);
                context.getItemInHandRenderer().renderItem(walrus, itemstack, ItemDisplayContext.GROUND, false, poseStack, bufferSource, packedLight);

                poseStack.popPose();
            }
        });
    }

    @Override
    protected float getShadowRadius(@NotNull WalrusEntity entity) {
        float f = super.getShadowRadius(entity);
        return entity.isBaby() ? f * 0.5F : f;
    }
}