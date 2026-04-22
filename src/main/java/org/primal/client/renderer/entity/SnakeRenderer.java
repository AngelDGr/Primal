package org.primal.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.primal.Primal_Main;
import org.primal.client.renderer.defaulted.MobRendererWithCustomBaby;
import org.primal.client.model.entity.SnakeModel;
import org.primal.client.renderer.entity.layer.snake.SnakePaintLayer;
import org.primal.client.renderer.entity.layer.snake.SnakeShedLayer;
import org.primal.entity.animal.SnakeEntity;
import org.primal.util.mob_types.EntityOnNeckRenderer;

@OnlyIn(Dist.CLIENT)
public class SnakeRenderer extends MobRendererWithCustomBaby.WithVariants<SnakeEntity, SnakeModel<SnakeEntity>, SnakeEntity.Variant> implements EntityOnNeckRenderer<SnakeEntity> {
    public SnakeRenderer(EntityRendererProvider.Context context) {
        super(context,
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "snake"),
                new SnakeModel.Adult<>(context.bakeLayer(SnakeModel.Adult.LAYER_LOCATION)),
                new SnakeModel.Baby<>(context.bakeLayer(SnakeModel.Baby.LAYER_LOCATION)),
                0f,
                true, Primal_Main.COMMON_CONFIG.snakeBabyCustomModel.get());
        this.addLayer(new SnakePaintLayer<>(this));
        this.addLayer(new SnakeShedLayer<>(this));
    }

    @Override
    public<T extends Player> void renderOnNeck(
            T owner,
            SnakeEntity snake,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            float partialTick
    ) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(180));
        poseStack.mulPose(Axis.ZP.rotationDegrees(180));

        // Lock body/head rotation
        snake.yBodyRot = 0;
        snake.setYRot(0);
        snake.yHeadRot = snake.getYRot();
        snake.yHeadRotO = snake.getYRot();
        snake.yBodyRotO = snake.getYRot();
        snake.tickCount=owner.tickCount;

        RandomSource random = snake.getRandom();

        boolean shouldFlick =
                random.nextFloat() < 0.0005f // ~0.005% chance per tick
                        || snake.flickAnimationState.getAccumulatedTime() < 500;

        snake.flickAnimationState.animateWhen(shouldFlick, snake.tickCount);
        snake.wrappedAnimationState.animateWhen(true, snake.tickCount);
        snake.attackAnimationState.animateWhen(owner.swinging && !snake.isBaby(), snake.tickCount);

        this.render(snake, 0, partialTick, poseStack, bufferSource, packedLight);

        poseStack.popPose();
    }
}