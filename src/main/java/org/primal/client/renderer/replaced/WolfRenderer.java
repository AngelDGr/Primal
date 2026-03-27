package org.primal.client.renderer.replaced;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.nbt.CompoundTag;
import org.primal.Primal_Main;
import org.primal.client.model.replaced.WolfModel;
import org.primal.client.renderer.entity.layer.wolf.WolfArmorLayer;
import org.primal.client.renderer.entity.layer.wolf.WolfCollarLayer;
import org.primal.client.renderer.entity.layer.wolf.WolfEyesLayer;
import org.primal.entity.replaced.WolfReplaced;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoReplacedEntityRenderer;

import java.util.List;

public class WolfRenderer extends GeoReplacedEntityRenderer<Wolf, WolfReplaced> {

    public WolfRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new WolfModel(), new WolfReplaced());
        addRenderLayer(new WolfCollarLayer(this));
        addRenderLayer(new WolfEyesLayer(this));
        addRenderLayer(new WolfArmorLayer(this));
        shadowRadius=0.5F;
    }

    @Override
    public void scaleModelForRender(float widthScale, float heightScale, PoseStack poseStack, WolfReplaced animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
        var bone = model.getBone("head");

        float headScale = this.currentEntity.isBaby() ? 2.f : 1.f;
        bone.ifPresent(geoBone ->
                geoBone.updateScale(headScale, headScale, headScale));
        if (this.currentEntity.isBaby()) {
            widthScale = heightScale = .5f;
        }
        super.scaleModelForRender(widthScale, heightScale, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
    }

    @Override
    public ResourceLocation getTextureLocation(WolfReplaced animatable) {
        Wolf wolf = this.currentEntity;
        CompoundTag mainTag = new CompoundTag();
        wolf.saveWithoutId(mainTag);
        String variantTag= mainTag.getString("variant");

        if(BuiltInRegistries.ENTITY_TYPE.getKey(this.currentEntity.getType()).getNamespace().equals("pet_cemetery")) {
            if (BuiltInRegistries.ENTITY_TYPE.getKey(this.currentEntity.getType()).getPath().equals("skeleton_wolf"))
                return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/wolf/pet_cemetery/skeleton.png");

            if (BuiltInRegistries.ENTITY_TYPE.getKey(this.currentEntity.getType()).getPath().equals("zombie_wolf"))
                return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/wolf/pet_cemetery/zombie.png");
        }

        if(mainTag.getBoolean("IsCuredBewereager")){
            return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/wolf/species/bewereager.png");
        }

        return convertWolfVariantToName(variantTag);
    }

    public static List<String> SLIM_VARIANTS = List.of(
            "rusty",
            "stripped",
            "spotted",
            "maned",
            "bush");

    public static ResourceLocation convertWolfVariantToName(String variantName) {
        ResourceLocation nameRegistered = ResourceLocation.tryParse(variantName);
        if(!variantName.isEmpty() && nameRegistered!=null && isModSupported(nameRegistered.getNamespace())){
            String sublocation = nameRegistered.getNamespace().equals("minecraft")? "" : nameRegistered.getNamespace()+"/";
            return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/wolf/" +sublocation+ nameRegistered.getPath()+".png");
        }

        //Default
        return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/wolf/pale.png");
    }

    public static boolean isModSupported(String namespace){
        return namespace.equals("minecraft") || namespace.equals("atmospheric") || namespace.equals("environmental") || namespace.equals("nomansland") || namespace.equals("autumnity");
    }

    public static String addSlimSuffix(Wolf wolf){
        CompoundTag mainTag = new CompoundTag();
        wolf.saveWithoutId(mainTag);
        String variantTag= mainTag.getString("variant");

        ResourceLocation nameRegistered = ResourceLocation.tryParse(variantTag);
        return nameRegistered!=null && WolfRenderer.SLIM_VARIANTS.contains(nameRegistered.getPath())? "_slim": "";
    }
}
