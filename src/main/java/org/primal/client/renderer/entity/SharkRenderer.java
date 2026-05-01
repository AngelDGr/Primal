package org.primal.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.client.model.entity.SharkModel;
import org.primal.client.renderer.entity.layer.SharkConduitEyesLayer;
import org.primal.entity.animal.SharkEntity;

public class SharkRenderer extends MobRenderer<SharkEntity, SharkModel<SharkEntity>> {
    public SharkRenderer(EntityRendererProvider.Context context) {
        super(context, new SharkModel<>(context.bakeLayer(SharkModel.LAYER_LOCATION)), 0);
        this.addLayer(new SharkConduitEyesLayer<>(this));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull SharkEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/shark/"+animatable.getVariant().getSerializedName()+".png");
    }
}
