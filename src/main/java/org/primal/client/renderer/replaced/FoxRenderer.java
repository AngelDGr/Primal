package org.primal.client.renderer.replaced;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.primal.Primal_Main;
import org.primal.client.model.replaced.FoxModel;
import org.primal.entity.replaced.FoxReplaced;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoReplacedEntityRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

public class FoxRenderer extends GeoReplacedEntityRenderer<Fox, FoxReplaced> {

    private static final String ITEM = "mouth";
    protected ItemStack mainHandItem;

    public FoxRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FoxModel(), new FoxReplaced());

        //Item on hand renderer
        addRenderLayer(new BlockAndItemGeoLayer<>(this) {
            @Nullable
            @Override
            protected ItemStack getStackForBone(GeoBone bone, FoxReplaced animatable) {
                if(bone.getName().equals(ITEM))
                    return FoxRenderer.this.mainHandItem;

                return null;
            }

            @Override
            protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack, FoxReplaced animatable) {
                if(bone.getName().equals(ITEM))
                    return ItemDisplayContext.GROUND;

                return ItemDisplayContext.NONE;
            }

            // Do some quick render modifications depending on what the item is
            @Override
            protected void renderStackForBone(PoseStack poseStack, GeoBone bone, ItemStack stack, FoxReplaced dwarf,
                                              MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay) {
                if (stack == FoxRenderer.this.mainHandItem) {
                    poseStack.mulPose(Axis.XN.rotationDegrees(-90f));
                    poseStack.mulPose(Axis.YN.rotationDegrees(180f));

                    float f = 0.75F;
                    poseStack.scale(f, f, f);
                }

                super.renderStackForBone(poseStack, bone, stack, dwarf, bufferSource, partialTick, packedLight, packedOverlay);
            }
        });

        shadowRadius=0.4F;
    }

    @Override
    public ResourceLocation getTextureLocation(FoxReplaced animatable) {
        Fox fox = this.currentEntity;

        return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/fox/"+fox.getVariant().getSerializedName()+(fox.isSleeping()?"_sleep":"")+ ".png");
    }

    @Override
    public void preRender(PoseStack poseStack, FoxReplaced animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);

        this.mainHandItem = this.currentEntity.getMainHandItem();
    }

    @Override
    protected void applyRotations(FoxReplaced animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick, nativeScale);
        if ((this.currentEntity.isPouncing()) && !this.currentEntity.onGround()) {
            float f = -Mth.lerp(partialTick, this.currentEntity.xRotO, this.currentEntity.getXRot());
            poseStack.mulPose(Axis.XP.rotationDegrees(f));
        }
    }

    @Override
    public void scaleModelForRender(float widthScale, float heightScale, PoseStack poseStack, FoxReplaced animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
        GeoBone bone = model.getBone("head").get();
        float headScale = this.currentEntity.isBaby() ? 2.f : 1.f;
        bone.updateScale(headScale, headScale, headScale);
        if (this.currentEntity.isBaby()) {
            widthScale = heightScale = .5f;
        }
        super.scaleModelForRender(widthScale, heightScale, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
    }

    @Override
    public float getMotionAnimThreshold(FoxReplaced animatable) {
        return this.currentEntity.isInWater()? 0.0015f: 0.0015f;
    }
}
