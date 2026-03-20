package org.primal.client.model.block;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.primal.Primal_Main;
import org.primal.block_entity.ChompTrapBlockEntity;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

public class ChompTrapModel extends DefaultedBlockGeoModel<ChompTrapBlockEntity> {

    public ChompTrapModel() {
        super(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "chomp_trap"));
    }

    @Override
    public ResourceLocation getTextureResource(ChompTrapBlockEntity animatable) {
        return buildFormattedTexturePath(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "chomp_trap"), animatable);
    }

    @Override
    public @Nullable RenderType getRenderType(ChompTrapBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityCutoutNoCull(getTextureResource(animatable));
    }

    public ResourceLocation buildFormattedTexturePath(ResourceLocation basePath, ChompTrapBlockEntity animatable) {
        return basePath.withPath("textures/" + subtype() + "/" + basePath.getPath()+"/" + animatable.colorType + ".png");
    }
}
