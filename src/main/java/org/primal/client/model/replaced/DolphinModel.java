package org.primal.client.model.replaced;

import net.minecraft.resources.ResourceLocation;
import org.primal.Primal_Main;
import org.primal.entity.replaced.DolphinReplaced;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class DolphinModel extends DefaultedEntityGeoModel<DolphinReplaced> {
    public DolphinModel() {
        super(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "dolphin"), false);
    }
}
