package org.primal.client.renderer.entity.layer.wolf;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.client.model.entity.replaced.WolfModel;
import org.primal.client.renderer.replaced.WolfRenderer;

public class WolfEyesLayer<T extends Wolf, M extends WolfModel<T>> extends RenderLayer<T, M> {

    public WolfEyesLayer(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, @NotNull T livingEntity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        CompoundTag mainTag = new CompoundTag();
        livingEntity.saveWithoutId(mainTag);

        //Default glowing eyes
        RenderType eyesrenderType = RenderType.entityTranslucentEmissive(convertWolfVariantToName(livingEntity));

        if(mainTag.getBoolean("IsCuredBewereager")){
            eyesrenderType = RenderType.entityTranslucentEmissive(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/wolf/species/bewereager_eyes.png"));
        }

        //Angry glowing eyes
        if(livingEntity.isAngry() && !livingEntity.isTame())
            eyesrenderType = RenderType.entityTranslucentEmissive(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/wolf/angry.png"));


        this.getParentModel().renderToBuffer(poseStack, bufferSource.getBuffer(eyesrenderType), packedLight, OverlayTexture.NO_OVERLAY);
    }

    public ResourceLocation convertWolfVariantToName(T wolf) {
        var wolfVariantHolder = wolf.getVariant();
        if(BuiltInRegistries.ENTITY_TYPE.getKey(wolf.getType()).getNamespace().equals("pet_cemetery")){
            if(BuiltInRegistries.ENTITY_TYPE.getKey(wolf.getType()).getPath().equals("skeleton_wolf"))
                return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/wolf/pet_cemetery/skeleton_eyes.png");

            if(BuiltInRegistries.ENTITY_TYPE.getKey(wolf.getType()).getPath().equals("zombie_wolf"))
                return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/wolf/pet_cemetery/zombie_eyes.png");
        }

        ResourceLocation nameRegistered = ResourceLocation.tryParse(wolfVariantHolder.getRegisteredName());
        if(nameRegistered!=null && WolfRenderer.isModSupported(nameRegistered.getNamespace())){
            String sublocation = nameRegistered.getNamespace().equals("minecraft")? "" : nameRegistered.getNamespace()+"/";
            return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/wolf/" +sublocation+ nameRegistered.getPath()+"_eyes.png");
        }

        //Default
        return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/wolf/pale_eyes.png");
    }
}
