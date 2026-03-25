package org.primal.client.model.defaulted;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.VariantHolder;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

/**
    For an entity that has variants and custom baby model
 */
public abstract class DefaultedEntityWithVariantsWithBabyGeoModel<T extends AgeableMob & GeoAnimatable & VariantHolder<M>, M extends StringRepresentable> extends DefaultedEntityGeoModel<T> {
    private final ResourceLocation assetSubpath;
    private final boolean babyHasVariants;
    private final boolean customBabyModelActive;

    public DefaultedEntityWithVariantsWithBabyGeoModel(ResourceLocation assetSubpath, boolean turnsHead, boolean babyHasVariants, boolean customBabyModelActive) {
        super(assetSubpath, turnsHead);
        this.assetSubpath=assetSubpath;
        this.babyHasVariants=babyHasVariants;
        this.customBabyModelActive =customBabyModelActive;
    }

    @Override
    public ResourceLocation getModelResource(T animatable) {
        return buildFormattedModelPath(assetSubpath, animatable);
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return buildFormattedTexturePath(assetSubpath, animatable);
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return buildFormattedAnimationPath(assetSubpath, animatable);
    }

    public ResourceLocation buildFormattedModelPath(ResourceLocation basePath, T animatable) {
        return basePath.withPath("geo/" + subtype() + "/" + (animatable.isBaby() && customBabyModelActive ? "baby/": "") + basePath.getPath()  + ".geo.json");
    }

    public ResourceLocation buildFormattedAnimationPath(ResourceLocation basePath, T animatable) {
        return basePath.withPath("animations/" + subtype() + "/" + (animatable.isBaby() && customBabyModelActive ? "baby/": "") + basePath.getPath() + ".animation.json");
    }

    public ResourceLocation buildFormattedTexturePath(ResourceLocation basePath, T animatable) {
        String babyTextureName = babyHasVariants? animatable.getVariant().getSerializedName(): basePath.getPath();
        ResourceLocation babyPath = basePath.withPath("textures/" + subtype() + "/" + basePath.getPath()+"/" + "baby/" + babyTextureName+  ".png");
        ResourceLocation adultPath = basePath.withPath("textures/" + subtype() + "/" + basePath.getPath()+"/" + animatable.getVariant().getSerializedName()+  ".png");

        return animatable.isBaby() && customBabyModelActive ? babyPath: adultPath;
    }
}
