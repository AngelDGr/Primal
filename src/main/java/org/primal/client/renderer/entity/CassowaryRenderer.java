package org.primal.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.client.renderer.defaulted.MobRendererWithCustomBaby;
import org.primal.client.model.entity.CassowaryModel;
import org.primal.entity.animal.CassowaryEntity;

@OnlyIn(Dist.CLIENT)
public class CassowaryRenderer extends MobRendererWithCustomBaby.WithVariants<CassowaryEntity, CassowaryModel<CassowaryEntity>, CassowaryEntity.Variant> {
    public CassowaryRenderer(EntityRendererProvider.Context context) {
        super(context,
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "cassowary"),
                new CassowaryModel.Adult<>(context.bakeLayer(CassowaryModel.Adult.LAYER_LOCATION)),
                new CassowaryModel.Baby<>(context.bakeLayer(CassowaryModel.Baby.LAYER_LOCATION)),
                0.35f,
                false, Primal_Main.COMMON_CONFIG.cassowaryBabyCustomModel.get());
    }

    @Override
    public void render(@NotNull CassowaryEntity entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        if (entity.isBaby()) this.shadowRadius *= 0.5F;
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}