package org.primal.client.renderer.entity.layer.lion;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.client.model.entity.LionModel;
import org.primal.entity.animal.LionEntity;

public class LionEyesLayer<T extends LionEntity, M extends LionModel<T>> extends RenderLayer<T, M> {

    public LionEyesLayer(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, @NotNull T lion, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if(lion.isBaby() && Primal_Main.COMMON_CONFIG.lionBabyCustomModel.get()) return;

        //Default glowing eyes
        RenderType eyesrenderType =
                RenderType.entityTranslucentEmissive(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/lion/"+ lion.getVariant().getSerializedName() +"_eyes.png"));

        if(lion.isSigma()){
            eyesrenderType = RenderType.entityTranslucentEmissive(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/lion/sigma_eyes.png"));
        }

        //Angry glowing eyes
        if(lion.isAngry() && !lion.isTame())
            eyesrenderType = RenderType.entityTranslucentEmissive(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/lion/angry.png"));

        this.getParentModel().renderToBuffer(poseStack, bufferSource.getBuffer(eyesrenderType), packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    }
}