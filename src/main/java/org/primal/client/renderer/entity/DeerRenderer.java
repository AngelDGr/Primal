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
import org.primal.client.model.entity.DeerModel;
import org.primal.entity.animal.DeerEntity;

@OnlyIn(Dist.CLIENT)
public class DeerRenderer extends MobRendererWithCustomBaby.WithVariants<DeerEntity, DeerModel<DeerEntity>, DeerEntity.Variant> {
    public DeerRenderer(EntityRendererProvider.Context context) {
        super(context,
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "deer"),
                new DeerModel.Adult<>(context.bakeLayer(DeerModel.Adult.LAYER_LOCATION)),
                new DeerModel.Baby<>(context.bakeLayer(DeerModel.Baby.LAYER_LOCATION)),
                0.65f,
                true, Primal_Main.COMMON_CONFIG.deerBabyCustomModel.get());
    }

    @Override
    public void render(@NotNull DeerEntity entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        if (entity.isBaby()) this.shadowRadius *= 0.25F;
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public boolean isShaking(@NotNull DeerEntity animatable) {
        return super.isShaking(animatable) || animatable.level().isThundering();
    }
}