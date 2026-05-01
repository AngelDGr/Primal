package org.primal.client.renderer.replaced;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.client.model.entity.replaced.FoxModel;
import org.primal.util.Primal_Util;

public class FoxRenderer extends MobRenderer<Fox, FoxModel<Fox>> {

    public FoxRenderer(EntityRendererProvider.Context context) {
        super(context, new FoxModel<>(context.bakeLayer(FoxModel.LAYER_LOCATION)), 0.3F);

        //Item on hand renderer
        this.addLayer(new RenderLayer<>(this) {
                          @Override
                          public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, @NotNull Fox livingEntity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
                              boolean isBaby = livingEntity.isBaby();
                              poseStack.pushPose();

                              ModelPart foxy = this.getParentModel().foxy;
                              ModelPart body = this.getParentModel().body_group;
                              ModelPart head = this.getParentModel().head;
                              ModelPart mouth = this.getParentModel().mouth;

                              if (isBaby) {
                                  poseStack.scale(0.5f, 0.5f, 0.5f);
                                  poseStack.translate(0.0F, 24f / 16.0F, 0.0F);
                              }

                              foxy.translateAndRotate(poseStack);
                              body.translateAndRotate(poseStack);
                              head.translateAndRotate(poseStack);
                              mouth.translateAndRotate(poseStack);

                              poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));

                              ItemStack itemstack = livingEntity.getItemBySlot(EquipmentSlot.MAINHAND);
                              context.getItemInHandRenderer().renderItem(livingEntity, itemstack, ItemDisplayContext.GROUND, false, poseStack, bufferSource, packedLight);
                              poseStack.popPose();
                          }
        }
        );
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Fox fox) {
        CompoundTag mainTag = new CompoundTag();
        fox.saveWithoutId(mainTag);

        var variant = Primal_Util.Visuals.getNomanslandVariant(mainTag, "fox", (fox.isSleeping() ? "_sleep" : ""), "cream", "forest");
        if (variant != null) return variant;

        return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/fox/"+fox.getVariant().getSerializedName()+(fox.isSleeping()?"_sleep":"")+ ".png");
    }
}