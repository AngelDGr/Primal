package org.primal.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
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
}