package org.primal.client.renderer.block_entity;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.block_entity.StrawBasketBlockEntity;

public class StrawBasketRenderer extends ChestRenderer<StrawBasketBlockEntity> {
    public StrawBasketRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    public static final Material STRAW_BASKET_LOCATION_LEFT = chestMaterial("straw_basket/left");
    public static final Material STRAW_BASKET_LOCATION_RIGHT = chestMaterial("straw_basket/right");
    public static final Material STRAW_BASKET_LOCATION = chestMaterial("straw_basket/normal");

    @Override
    protected @NotNull Material getMaterial(@NotNull StrawBasketBlockEntity blockEntity, @NotNull ChestType chestType) {
        return chooseMaterial(chestType, STRAW_BASKET_LOCATION, STRAW_BASKET_LOCATION_LEFT, STRAW_BASKET_LOCATION_RIGHT);
    }

    public static Material chooseMaterial(ChestType chestType, Material doubleMaterial, Material leftMaterial, Material rightMaterial) {
        return switch (chestType) {
            case LEFT -> leftMaterial;
            case RIGHT -> rightMaterial;
            default -> doubleMaterial;
        };
    }

    private static Material chestMaterial(String chestName) {
        return new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/" + chestName));
    }
}
