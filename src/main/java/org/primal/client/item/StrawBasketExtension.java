package org.primal.client.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.*;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
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

        private StrawBasketBlockEntity strawBasket; // no init here

        public StrawBasketItemRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet entityModelSet) {
            super(blockEntityRenderDispatcher, entityModelSet);
        }

        private StrawBasketBlockEntity getOrCreate() {
            if (strawBasket == null) {
                strawBasket = new StrawBasketBlockEntity(
                        Primal_BlockEntities.STRAW_BASKET.get(),
                        BlockPos.ZERO,
                        Primal_Blocks.STRAW_BASKET.get().defaultBlockState()
                );
            }
            return strawBasket;
        }

        @Override
        public void renderByItem(@NotNull ItemStack stack, @NotNull ItemDisplayContext displayContext, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
            BlockEntityRenderDispatcher blockEntityRenderDispatcher =
                    Minecraft.getInstance().getBlockEntityRenderDispatcher();

            if(this.getOrCreate()!=null)
                blockEntityRenderDispatcher.renderItem(this.getOrCreate(), poseStack, buffer, packedLight, packedOverlay);
        }
    }
}
