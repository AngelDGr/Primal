package org.primal.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.client.renderer.defaulted.MobRendererWithCustomBaby;
import org.primal.client.model.entity.CrocodileModel;
import org.primal.client.renderer.entity.layer.crocodile.CrocodilePreyLayer;
import org.primal.entity.animal.CrocodileEntity;

public class CrocodileRenderer extends MobRendererWithCustomBaby.WithVariants<CrocodileEntity, CrocodileModel<CrocodileEntity>, CrocodileEntity.Variant> {
    public CrocodileRenderer(EntityRendererProvider.Context context) {
        super(context,
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "crocodile"),
                new CrocodileModel.Adult<>(context.bakeLayer(CrocodileModel.Adult.LAYER_LOCATION)),
                new CrocodileModel.Baby<>(context.bakeLayer(CrocodileModel.Baby.LAYER_LOCATION)),
                0.9f,
                true, Primal_Main.COMMON_CONFIG.crocodileBabyCustomModel.get());

        this.addLayer(new RenderLayer<>(this) {
            @Override
            public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, @NotNull CrocodileEntity entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {

                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.eyes(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID,
                        "textures/entity/crocodile/"+ (entity.isBaby() && Primal_Main.COMMON_CONFIG.crocodileBabyCustomModel.get()? "baby/": "") +entity.getVariant().getSerializedName()+"_eyes.png")));

                this.getParentModel().renderToBuffer(poseStack, vertexconsumer, 15728640, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            }
        });
        this.addLayer(new CrocodilePreyLayer<>(this));
    }

    @Override
    public void render(@NotNull CrocodileEntity entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        if (entity.isBaby()) this.shadowRadius *= 0.3F;
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}