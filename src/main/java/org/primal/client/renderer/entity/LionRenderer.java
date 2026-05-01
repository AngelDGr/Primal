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
import org.primal.client.model.entity.LionModel;
import org.primal.client.renderer.entity.layer.lion.LionEyesLayer;
import org.primal.client.renderer.entity.layer.lion.LionPaintLayer;
import org.primal.entity.animal.LionEntity;

@OnlyIn(Dist.CLIENT)
public class LionRenderer extends MobRendererWithCustomBaby.WithVariants<LionEntity, LionModel<LionEntity>, LionEntity.Variant> {
    public LionRenderer(EntityRendererProvider.Context context) {
        super(context,
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "lion"),
                new LionModel.Adult<>(context.bakeLayer(LionModel.Adult.LAYER_LOCATION)),
                new LionModel.Baby<>(context.bakeLayer(LionModel.Baby.LAYER_LOCATION)),
                0.75f,
                true, Primal_Main.COMMON_CONFIG.lionBabyCustomModel.get());
        this.addLayer(new LionEyesLayer<>(this));
        this.addLayer(new LionPaintLayer<>(this));
    }

    @Override
    protected ResourceLocation buildBabyTexturePath(ResourceLocation basePath, LionEntity entity) {
        //Special lion
        if (entity.isSigma()) return basePath.withPath("textures/entity/lion/baby/sigma.png");

        return super.buildBabyTexturePath(basePath, entity);
    }

    @Override
    protected ResourceLocation buildAdultTexturePath(ResourceLocation basePath, LionEntity entity) {
        //Special lion
        if (entity.isSigma())
            return basePath.withPath("textures/entity/lion/sigma"+ (entity.isManeless() || entity.isBaby()? "_maneless": "") +".png");

        //Maneless Lion
        if(entity.isManeless() || entity.isBaby()) {
            return basePath.withPath("textures/entity/" + basePath.getPath()+"/" + entity.getVariant().getSerializedName()+  "_maneless.png");
        }

        return super.buildAdultTexturePath(basePath, entity);
    }

    @Override
    public void render(@NotNull LionEntity entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        if(entity.isBaby()) this.shadowRadius *= 0.25F;
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}