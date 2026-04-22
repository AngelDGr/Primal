package org.primal.client.model.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class PrimalBlockEntityModel<T extends BlockEntity> extends Model {
    private final ModelPart root;

    public PrimalBlockEntityModel(Function<ResourceLocation, RenderType> renderType, ModelPart root) {
        super(renderType);
        this.root = root;
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.render(poseStack, buffer, packedLight, packedOverlay, color);
    }

    public void render(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.root.render(poseStack, buffer, packedLight, packedOverlay, color);
    }

    public void setupAnim(@NotNull T entity, float ageInTicks) {

    }
}
