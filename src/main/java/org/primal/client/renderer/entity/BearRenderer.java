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
import org.primal.client.model.entity.BearModel;
import org.primal.client.renderer.entity.layer.bear.*;
import org.primal.entity.animal.BearEntity;

@OnlyIn(Dist.CLIENT)
public final class BearRenderer extends MobRendererWithCustomBaby.WithVariants<BearEntity, BearModel<BearEntity>, BearEntity.Variant> {
    private final BearModel.Adult.Grolar<BearEntity> grolar;

    public BearRenderer(EntityRendererProvider.Context context) {
        super(context,
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "bear"),
                new BearModel.Adult<>(context.bakeLayer(BearModel.Adult.LAYER_LOCATION)),
                new BearModel.Baby<>(context.bakeLayer(BearModel.Baby.LAYER_LOCATION)),
                1.0f,
                true, Primal_Main.COMMON_CONFIG.bearBabyCustomModel.get());

        this.addLayer(new BearCollarLayer<>(this));
        this.addLayer(new BearBarrelsLayer<>(this));
        this.addLayer(new BearBerriesLayer<>(this));
        this.addLayer(new BearHoneyLayer<>(this));
        this.addLayer(new BearSleepLayer<>(this));

        shadowRadius=1.0F;

        this.grolar=new BearModel.Adult.Grolar<>(context.bakeLayer(BearModel.Adult.Grolar.LAYER_LOCATION));
    }

    @Override
    public BearModel<BearEntity> getModel(BearEntity mob) {
        return mob.getVariant().equals(BearEntity.Variant.GROLAR) && (!mob.isBaby() || !Primal_Main.COMMON_CONFIG.bearBabyCustomModel.get())?
            this.grolar:
                super.getModel(mob);
    }

    @Override
    public void render(@NotNull BearEntity entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        if (entity.isBaby()) this.shadowRadius *= 0.35F;
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}