package org.primal.client.renderer.block_entity;

import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.primal.Primal_Main;
import org.primal.block_entity.ChompTrapBlockEntity;
import org.primal.client.model.block.ChompTrapModel;
import org.primal.client.renderer.defaulted.BlockEntityRendererWithAnimations;

public class ChompTrapRenderer extends BlockEntityRendererWithAnimations<ChompTrapBlockEntity> {
    public ChompTrapRenderer(BlockEntityRendererProvider.Context context) {
        super(new ChompTrapModel(context.bakeLayer(ChompTrapModel.LAYER_LOCATION)), ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "chomp_trap"));
    }

    @Override
    public ResourceLocation buildFormattedTexturePath(ResourceLocation basePath, ChompTrapBlockEntity animatable) {
        return basePath.withPath("textures/block/chomp_trap/" + animatable.colorType + ".png");
    }
}