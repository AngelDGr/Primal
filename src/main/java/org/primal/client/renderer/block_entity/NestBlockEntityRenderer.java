package org.primal.client.renderer.block_entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;
import org.primal.block.NestBlock;
import org.primal.block_entity.NestBlockEntity;

import java.util.Optional;

public class NestBlockEntityRenderer implements BlockEntityRenderer<NestBlockEntity> {
    private final BlockRenderDispatcher blockRenderer;

    public NestBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderer= context.getBlockRenderDispatcher();
    }

    @Override
    public void render(@NotNull NestBlockEntity nestBlockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Level level = nestBlockEntity.getLevel();
        if (level != null) {
            if(!nestBlockEntity.getBlockState().getValue(NestBlock.HAS_EGG)){
                return;
            }

            ItemStack eggStack= nestBlockEntity.getEgg();

            if(eggStack.getItem() instanceof BlockItem blockItem){
                Block eggBlock = blockItem.getBlock();
                BlockState eggState =eggBlock.defaultBlockState();



                Optional<Property<?>> eggProperty=
                        eggBlock.defaultBlockState().getProperties().stream().filter(
                                property ->
                                        property.getName().equals("eggs") && property instanceof IntegerProperty)
                        .findFirst();

                Optional<Property<?>> hatchProperty=
                        eggBlock.defaultBlockState().getProperties().stream().filter(
                                        property ->
                                                property.getName().equals("hatch") && property instanceof IntegerProperty)
                                .findFirst();

                if(eggProperty.isPresent()){
                    eggState= eggState.setValue((IntegerProperty) eggProperty.get(), eggStack.getCount());
                }

                if(hatchProperty.isPresent()){
                    eggState= eggState.setValue((IntegerProperty) hatchProperty.get(), nestBlockEntity.getBlockState().getValue(NestBlock.HATCH));
                }

                BakedModel eggModel = blockRenderer.getBlockModel(eggState);

                if (eggModel != null) {
                    poseStack.pushPose();
                    poseStack.translate(0.0F, 4f/16f, 0.0F);


                    blockRenderer.renderSingleBlock(eggState, poseStack, bufferSource, packedLight, packedOverlay);
                    poseStack.popPose();
                }
            }
        }
    }
}
