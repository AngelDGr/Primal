package org.primal.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.client.model.entity.SnakeModel;
import org.primal.client.renderer.entity.layer.snake.SnakePaintLayer;
import org.primal.client.renderer.entity.layer.snake.SnakeShedLayer;
import org.primal.entity.animal.SnakeEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

@OnlyIn(Dist.CLIENT)
public class SnakeRenderer extends GeoEntityRenderer<SnakeEntity> {
    public SnakeRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SnakeModel());
        this.addRenderLayer(new SnakePaintLayer(this));
        this.addRenderLayer(new SnakeShedLayer(this));
        shadowRadius=0f;
    }

    @Override
    protected float getShadowRadius(@NotNull SnakeEntity entity) {
        float f = super.getShadowRadius(entity);
        return entity.isBaby() ? f * 0.25F : f;
    }

    @Override
    protected void applyRotations(SnakeEntity animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
//        if(animatable.isInWater() && animatable.isSlithering())
//            Primal_Util.Visuals.bodyFullRotations(animatable, partialTick, poseStack);
//        else
            super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick, nativeScale);
    }

    public<T extends Player> void renderOnNeck(
            T owner,
            SnakeEntity snake,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            float partialTick
    ) {
        poseStack.pushPose();

        poseStack.translate(
                0.0,
                (23.75f/16f) + (owner.isCrouching()? 0.2f: 0),
                0.0);

        BakedGeoModel model = getGeoModel().getBakedModel(getGeoModel().getModelResource(snake, this));
        var renderType = getRenderType(snake, getTextureLocation(snake), bufferSource, partialTick);

        if(renderType==null) return;

        var buffer = bufferSource.getBuffer(renderType);
        poseStack.mulPose(Axis.YP.rotationDegrees(180));
        poseStack.mulPose(Axis.ZP.rotationDegrees(180));

        // Lock body/head rotation
        snake.yBodyRot = 0;
        snake.setYRot(0);
        snake.yHeadRot = snake.getYRot();
        snake.yHeadRotO = snake.getYRot();
        snake.yBodyRotO = snake.getYRot();
        snake.tickCount=owner.tickCount;
        snake.setPose(Pose.SNIFFING);

        if(owner.swinging && !snake.isBaby())
            snake.triggerAnim("attack", "bite_wrapped");

        int renderColor = getRenderColor(snake, partialTick, packedLight).argbInt();
        int packedOverlay = getPackedOverlay(snake, 0, partialTick);

        super.actuallyRender(
                poseStack,
                snake,
                model,
                renderType,
                bufferSource,
                buffer,
                false,
                partialTick,
                packedLight,
                packedOverlay,
                renderColor
        );

        for (GeoRenderLayer<SnakeEntity> renderLayer : getRenderLayers())
            renderLayer.render(poseStack, snake, model, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);

        poseStack.popPose();
    }

    @Override
    public void scaleModelForRender(float widthScale, float heightScale, PoseStack poseStack, SnakeEntity animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
        if(!Primal_Main.COMMON_CONFIG.snakeBabyCustomModel.get()){
            var bone = model.getBone("head");
            float headScale = animatable.isBaby() ? 2.f : 1.f;
            bone.ifPresent(geoBone ->
                    geoBone.updateScale(headScale, headScale, headScale));
            if (animatable.isBaby()) {
                widthScale = heightScale = .4f;
            }
        }

        super.scaleModelForRender(widthScale, heightScale, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
    }

    @Override
    public float getMotionAnimThreshold(SnakeEntity animatable) {
        return 0.0015f;
    }
}
