package org.primal.client.renderer.entity.layer.snake;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.client.model.entity.SnakeModel;
import org.primal.entity.animal.SnakeEntity;

public class SnakeShedLayer<T extends SnakeEntity, M extends SnakeModel<T>> extends RenderLayer<T, M> {
    public SnakeShedLayer(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, @NotNull T entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entity.isShedding() || entity.isInvisible()) return;

        RenderType collarRenderType = RenderType.entityTranslucentCull(
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID,
                        "textures/entity/snake/"+(entity.isBaby() && Primal_Main.COMMON_CONFIG.snakeBabyCustomModel.get()? "baby/": "")+ "shed" + ".png"));

        this.getParentModel().renderToBuffer(poseStack, bufferSource.getBuffer(collarRenderType), packedLight, OverlayTexture.NO_OVERLAY);
    }
}
