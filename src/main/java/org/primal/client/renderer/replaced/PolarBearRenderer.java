package org.primal.client.renderer.replaced;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.PolarBear;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.client.model.entity.replaced.PolarBearModel;

@OnlyIn(Dist.CLIENT)
public class PolarBearRenderer extends MobRenderer<PolarBear, PolarBearModel<PolarBear>> {

    public PolarBearRenderer(EntityRendererProvider.Context context) {
        super(context, new PolarBearModel<>(context.bakeLayer(PolarBearModel.LAYER_LOCATION)), 1.0F);
    }

    @Override
    protected float getShadowRadius(@NotNull PolarBear entity) {
        float f = super.getShadowRadius(entity);
        return entity.isBaby() ? f * 0.35F : f;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull PolarBear entity) {
        return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/polar_bear.png");
    }
}
