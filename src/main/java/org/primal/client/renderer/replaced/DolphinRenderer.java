package org.primal.client.renderer.replaced;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.VariantHolder;
import net.minecraft.world.entity.animal.Dolphin;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.client.model.entity.replaced.DolphinModel;
import org.primal.entity.replaced.DolphinReplaced;
import org.primal.util.Primal_Util;

public class DolphinRenderer extends MobRenderer<Dolphin, DolphinModel<Dolphin>> {

    public DolphinRenderer(EntityRendererProvider.Context context) {
        super(context, new DolphinModel<>(context.bakeLayer(DolphinModel.LAYER_LOCATION)), 0.7f);
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Dolphin dolphin) {
        CompoundTag mainTag = new CompoundTag();
        dolphin.saveWithoutId(mainTag);
        var variant = Primal_Util.Visuals.getNomanslandVariant(mainTag, "dolphin", "", "river");
        if (variant != null) return variant;

        var variantHolder = ((VariantHolder<DolphinReplaced.Variant>)(dolphin)).getVariant();
        return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/dolphin/" + variantHolder.getSerializedName() +".png");
    }
}
