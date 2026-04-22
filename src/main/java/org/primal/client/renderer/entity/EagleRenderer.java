package org.primal.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.client.renderer.defaulted.MobRendererWithCustomBaby;
import org.primal.client.model.entity.EagleModel;
import org.primal.client.renderer.entity.layer.EagleCollarLayer;
import org.primal.entity.animal.EagleEntity;

@OnlyIn(Dist.CLIENT)
public final class EagleRenderer extends MobRendererWithCustomBaby.WithVariants<EagleEntity, EagleModel<EagleEntity>, EagleEntity.Variant> {
    public EagleRenderer(EntityRendererProvider.Context context) {
        super(context,
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "eagle"),
                new EagleModel.Adult<>(context.bakeLayer(EagleModel.Adult.LAYER_LOCATION)),
                new EagleModel.Baby<>(context.bakeLayer(EagleModel.Baby.LAYER_LOCATION)),
                0.5f,
                false, true);

        this.addLayer(new EagleCollarLayer<>(this));
    }

    @Override
    protected float getShadowRadius(@NotNull EagleEntity entity) {
        float f = super.getShadowRadius(entity);
        return entity.isBaby() ? f * 0.3F : f;
    }
}