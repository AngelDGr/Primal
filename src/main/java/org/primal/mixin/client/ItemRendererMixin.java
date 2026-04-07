package org.primal.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.primal.Primal_Registries;
import org.primal.item.HelmetDecoration;
import org.primal.registry.Primal_HelmetDecorations;
import org.primal.registry.Primal_Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Shadow
    public abstract void renderModelLists(BakedModel model, ItemStack stack, int combinedLight, int combinedOverlay, PoseStack poseStack, VertexConsumer buffer);

    @Shadow
    public abstract void render(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, BakedModel p_model);

    @Inject(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/BakedModel;getRenderPasses(Lnet/minecraft/world/item/ItemStack;Z)Ljava/util/List;")
    )
    private void primal$addHelmetDecorationOverlay(ItemStack itemStack, ItemDisplayContext displayContext,
                                                 boolean leftHand, PoseStack poseStack,
                                                 MultiBufferSource bufferSource, int combinedLight,
                                                 int combinedOverlay, BakedModel p_model, CallbackInfo ci,
                                                 @Local(ordinal = 1) boolean flag1){

        var component = itemStack.get(Primal_Items.Components.HELMET_DECORATION);
        if (component == null) return;

        poseStack.pushPose();

        primal$renderHelmetDecoration(component.left(), "_l", poseStack, bufferSource, combinedLight, combinedOverlay, flag1);
        primal$renderHelmetDecoration(component.right(), "_r", poseStack, bufferSource, combinedLight, combinedOverlay, flag1);

        poseStack.popPose();

    }

    @Unique
    private void primal$renderHelmetDecoration(HelmetDecoration<?> type, String suffix,
                                               PoseStack poseStack, MultiBufferSource bufferSource,
                                               int light, int overlay, boolean flag) {

        if (type == Primal_HelmetDecorations.NONE.get()) return;
        var decorKey= Primal_Registries.HELMET_DECORATIONS_REGISTRY.getKey(type);
        if (decorKey == null) return;

        var location = ModelResourceLocation.standalone(
                ResourceLocation.fromNamespaceAndPath(
                        decorKey.getNamespace(),
                        "helmet_decoration/" + decorKey.getPath() + suffix
                )
        );

        BakedModel model = Minecraft.getInstance().getModelManager().getModel(location);

        for (var pass : model.getRenderPasses(ItemStack.EMPTY, flag)) {
            for (var renderType : pass.getRenderTypes(ItemStack.EMPTY, flag)) {

                VertexConsumer consumer = bufferSource.getBuffer(renderType);
                this.renderModelLists(model, ItemStack.EMPTY, light, overlay, poseStack, consumer);
            }
        }
    }
}
