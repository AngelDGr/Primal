package org.primal.client.renderer.entity.layer.wolf;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;
import org.jetbrains.annotations.Nullable;
import org.primal.Primal_Main;
import org.primal.client.renderer.replaced.WolfRenderer;
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

        CompoundTag mainTag = new CompoundTag();
        wolf.saveWithoutId(mainTag);
        String variantTag= mainTag.getString("variant");

        //Default glowing eyes
        RenderType eyesrenderType = RenderType.entityTranslucentEmissive(convertWolfVariantToName(variantTag));

        //Angry glowing eyes
        if(wolf.isAngry() && !wolf.isTame())
            eyesrenderType = RenderType.entityTranslucentEmissive(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/wolf/angry.png"));


        this.getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, eyesrenderType, bufferSource.getBuffer(eyesrenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY, 1,1,1,1);
    }

    public ResourceLocation convertWolfVariantToName(String variantName) {

        if(BuiltInRegistries.ENTITY_TYPE.getKey(this.renderer.getCurrentEntity().getType()).getNamespace().equals("pet_cemetery")){
            if(BuiltInRegistries.ENTITY_TYPE.getKey(this.renderer.getCurrentEntity().getType()).getPath().equals("skeleton_wolf"))
                return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/wolf/pet_cemetery/skeleton_eyes.png");

            if(BuiltInRegistries.ENTITY_TYPE.getKey(this.renderer.getCurrentEntity().getType()).getPath().equals("zombie_wolf"))
                return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/wolf/pet_cemetery/zombie_eyes.png");
        }

        ResourceLocation nameRegistered = ResourceLocation.tryParse(variantName);
        if(!variantName.isEmpty() && nameRegistered!=null && WolfRenderer.isModSupported(nameRegistered.getNamespace())){
            String sublocation = nameRegistered.getNamespace().equals("minecraft")? "" : nameRegistered.getNamespace()+"/";
            return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/wolf/" +sublocation+ nameRegistered.getPath()+"_eyes.png");
        }

        //Default
        return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/wolf/pale_eyes.png");
    }
}
