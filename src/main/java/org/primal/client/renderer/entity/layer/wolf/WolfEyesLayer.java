package org.primal.client.renderer.entity.layer.wolf;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;
import org.jetbrains.annotations.Nullable;
import org.primal.Primal_Main;
import org.primal.entity.replaced.WolfReplaced;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoReplacedEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class WolfEyesLayer extends GeoRenderLayer<WolfReplaced> {

    private final GeoReplacedEntityRenderer<Wolf, WolfReplaced> renderer;
    public WolfEyesLayer(GeoReplacedEntityRenderer<Wolf, WolfReplaced> entityRendererIn) {
        super(entityRendererIn);
        this.renderer=entityRendererIn;
    }

    @Override
    public void render(PoseStack poseStack, WolfReplaced animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        Wolf wolf = renderer.getCurrentEntity();

        //Default glowing eyes
//        RenderType eyesrenderType = RenderType.entityTranslucentEmissive(convertWolfVariantToName(wolf.getVariant()));
        RenderType eyesrenderType = RenderType.entityTranslucentEmissive(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/wolf/pale_eyes.png"));

        //Angry glowing eyes
        if(wolf.isAngry() && !wolf.isTame())
            eyesrenderType = RenderType.entityTranslucentEmissive(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/wolf/angry.png"));


        this.getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, eyesrenderType, bufferSource.getBuffer(eyesrenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY, 1,1,1,1);
    }

//    public ResourceLocation convertWolfVariantToName(Holder<WolfVariant> wolfVariantHolder) {
//
//        ResourceLocation nameRegistered = ResourceLocation.tryParse(wolfVariantHolder.getRegisteredName());
//        if(nameRegistered!=null && WolfRenderer.isModSupported(nameRegistered.getNamespace())){
//            String sublocation = nameRegistered.getNamespace().equals("minecraft")? "" : nameRegistered.getNamespace()+"/";
//            return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/wolf/" +sublocation+ nameRegistered.getPath()+"_eyes.png");
//        }
//
//        //Default
//        return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/wolf/pale_eyes.png");
//    }
}
