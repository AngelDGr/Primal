package org.primal.client.renderer.replaced;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.client.model.entity.replaced.WolfModel;
import org.primal.client.renderer.entity.layer.wolf.WolfArmorLayer;
import org.primal.client.renderer.entity.layer.wolf.WolfCollarLayer;
import org.primal.client.renderer.entity.layer.wolf.WolfEyesLayer;

import java.util.List;

public class WolfRenderer extends MobRenderer<Wolf, WolfModel<Wolf>> {

    public WolfRenderer(EntityRendererProvider.Context context) {
        super(context, new WolfModel<>(context.bakeLayer(WolfModel.LAYER_LOCATION)), 0.5f);
        this.addLayer(new WolfCollarLayer<>(this));
        this.addLayer(new WolfEyesLayer<>(this));
        this.addLayer(new WolfArmorLayer<>(this));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(Wolf wolf) {
        CompoundTag mainTag = new CompoundTag();
        wolf.saveWithoutId(mainTag);
        String variantTag= mainTag.getString("variant");

        if(BuiltInRegistries.ENTITY_TYPE.getKey(wolf.getType()).getNamespace().equals("pet_cemetery")) {
            if (BuiltInRegistries.ENTITY_TYPE.getKey(wolf.getType()).getPath().equals("skeleton_wolf"))
                return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/wolf/pet_cemetery/skeleton.png");

            if (BuiltInRegistries.ENTITY_TYPE.getKey(wolf.getType()).getPath().equals("zombie_wolf"))
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
