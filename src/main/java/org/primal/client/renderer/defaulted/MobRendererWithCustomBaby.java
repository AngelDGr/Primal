package org.primal.client.renderer.defaulted;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.VariantHolder;
import org.jetbrains.annotations.NotNull;

public abstract class MobRendererWithCustomBaby<T extends Mob, M extends EntityModel<T>> extends MobRenderer<T, M> {
    private final M adultModel;
    private final M babyModel;
    protected final ResourceLocation assetSubpath;
    protected final boolean customBabyModelActive;

    public MobRendererWithCustomBaby(EntityRendererProvider.Context context, ResourceLocation assetSubpath, M adultModel, M babyModel, float shadow, boolean customBabyModelActive) {
        super(context, adultModel, shadow);
        this.assetSubpath = assetSubpath;
        this.adultModel = adultModel;
        this.babyModel = babyModel;
        this.customBabyModelActive = customBabyModelActive;
    }

    public M getModel(T mob) {
        return mob.isBaby() && customBabyModelActive ? this.babyModel : this.adultModel;
    }

    @Override
    public void render(@NotNull T entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        this.model = this.getModel(entity);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    public static abstract class WithVariants<T extends Mob & VariantHolder<S>, M extends EntityModel<T>, S extends StringRepresentable> extends MobRendererWithCustomBaby<T, M> {
        private final boolean babyHasVariants;

        public WithVariants(EntityRendererProvider.Context context, ResourceLocation assetSubpath, M adultModel, M babyModel, float shadow, boolean babyHasVariants, boolean customBabyModelActive) {
            super(context, assetSubpath, adultModel, babyModel, shadow, customBabyModelActive);
            this.babyHasVariants = babyHasVariants;
        }

        @Override
        public @NotNull ResourceLocation getTextureLocation(@NotNull T entity) {
            return buildFormattedTexturePath(assetSubpath, entity);
        }

        public ResourceLocation buildFormattedTexturePath(ResourceLocation basePath, T entity) {
            return entity.isBaby() && customBabyModelActive ? buildBabyTexturePath(basePath, entity): buildAdultTexturePath(basePath, entity);
        }

        protected ResourceLocation buildBabyTexturePath(ResourceLocation basePath, T entity) {
            String babyTextureName = babyHasVariants? entity.getVariant().getSerializedName(): basePath.getPath();
            return basePath.withPath("textures/entity/" + basePath.getPath()+"/" + "baby/" + babyTextureName+  ".png");
        }

        protected ResourceLocation buildAdultTexturePath(ResourceLocation basePath, T entity) {
            return basePath.withPath("textures/entity/" + basePath.getPath()+"/" + entity.getVariant().getSerializedName()+  ".png");
        }
    }
}
