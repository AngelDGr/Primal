package org.primal.client.model.entity;

import net.minecraft.resources.ResourceLocation;
import org.primal.Primal_Main;
import org.primal.client.model.defaulted.DefaultedEntityWithVariantsWithBabyGeoModel;
import org.primal.entity.animal.CassowaryEntity;

public class CassowaryModel extends DefaultedEntityWithVariantsWithBabyGeoModel<CassowaryEntity, CassowaryEntity.Variant> {
    public CassowaryModel() {
        super(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "cassowary"), true, false, Primal_Main.COMMON_CONFIG.cassowaryBabyCustomModel.get());
    }
}
