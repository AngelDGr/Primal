package org.primal.client.renderer.defaulted;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import org.primal.Primal_Main;
import org.primal.util.mob_types.ReplacedEntityNewVariantHolder;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoReplacedEntityRenderer;

public abstract class DefaultedReplacedEntityWithNewVariantsRenderer<E extends Entity, T extends GeoAnimatable, M extends StringRepresentable> extends GeoReplacedEntityRenderer<E, T> {

    private final String assetSubpath;
    private final boolean hasBaby;
    private final boolean babyHasVariants;

    public DefaultedReplacedEntityWithNewVariantsRenderer(EntityRendererProvider.Context renderManager, GeoModel<T> model, T animatable, String assetSubpath, boolean hasBaby, boolean babyHasVariants) {
        super(renderManager, model, animatable);
        this.assetSubpath=assetSubpath;
        this.hasBaby=hasBaby;
        this.babyHasVariants=babyHasVariants;
    }

    /** No custom baby **/
    public DefaultedReplacedEntityWithNewVariantsRenderer(EntityRendererProvider.Context renderManager, GeoModel<T> model, T animatable, String assetSubpath) {
        this(renderManager, model, animatable, assetSubpath, false, false);
    }

    /**
     * IMPORTANT: DO A MIXIN IMPLEMENTING THIS INTERFACE INTO THE ORIGINAL ENTITY CLASS, OTHERWISE IT WON'T BE ABLE TO CAST IT
     * **/
    @SuppressWarnings("unchecked")
    protected ReplacedEntityNewVariantHolder<M> getVariantHolder(){
        return (ReplacedEntityNewVariantHolder<M>) this.currentEntity;
    }

    protected boolean isBaby(){
        return this.currentEntity instanceof AgeableMob ageableMob && ageableMob.isBaby() && hasBaby;
    }

    @Override
    public ResourceLocation getTextureLocation(T animatable) {
        var variantHolder = getVariantHolder();
        return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/" +this.assetSubpath + "/"
                +(isBaby()? "baby/": "") + ((babyHasVariants && isBaby() || !isBaby())? variantHolder.primal$getVariant().getSerializedName(): this.assetSubpath)+".png");
    }
}
