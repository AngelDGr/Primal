package org.primal.client.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.primal.block_entity.StrawBasketBlockEntity;
import org.primal.registry.Primal_BlockEntities;
import org.primal.registry.Primal_Blocks;

public class StrawBasketExtension implements IClientItemExtensions {

    private final BlockEntityWithoutLevelRenderer renderer = new StrawBasketItemRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());

    @Override
    public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return renderer;
    }

    public static class StrawBasketItemRenderer extends BlockEntityWithoutLevelRenderer {

        private final StrawBasketBlockEntity strawBasket = new StrawBasketBlockEntity(Primal_BlockEntities.STRAW_BASKET.get(), BlockPos.ZERO, Primal_Blocks.STRAW_BASKET.get().defaultBlockState());
        private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;

        public StrawBasketItemRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet entityModelSet) {
            super(blockEntityRenderDispatcher, entityModelSet);
            this.blockEntityRenderDispatcher = blockEntityRenderDispatcher;
        }

        @Override
        public void renderByItem(@NotNull ItemStack stack, @NotNull ItemDisplayContext displayContext, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
            this.blockEntityRenderDispatcher.renderItem(this.strawBasket, poseStack, buffer, packedLight, packedOverlay);
        }
    }
}
