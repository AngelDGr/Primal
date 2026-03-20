package org.primal.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.primal.client.renderer.entity.SnakeRenderer;
import org.primal.entity.animal.SnakeEntity;
import org.primal.injection.SetNeckEntity;
import org.primal.registry.Primal_Entities;

@OnlyIn(Dist.CLIENT)
public class EntityOnNeckLayer<T extends Player> extends RenderLayer<T, PlayerModel<T>> {
    private final SnakeRenderer snakeRenderer;

    public EntityOnNeckLayer(RenderLayerParent<T, PlayerModel<T>> renderer, EntityRendererProvider.Context context) {
        super(renderer);
        this.snakeRenderer = new SnakeRenderer(context);
    }

    public void render(
            @NotNull PoseStack poseStack,
            @NotNull MultiBufferSource buffer,
            int packedLight,
            @NotNull T livingEntity,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        this.render(poseStack, buffer, packedLight, livingEntity, partialTicks);
    }

    Entity entity=null;
    private void render(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            T livingEntity,
            float partialTick
    ) {
        CompoundTag tag = ((SetNeckEntity) livingEntity).primal$getNeckEntity();

        EntityType.byString(tag.getString("id"))
                .filter(type -> type == Primal_Entities.SNAKE.get())
                .ifPresent(type -> {
                    //Starts the entity
                    if(entity==null)
                        this.entity = type.create(livingEntity.level());

                    //Renders the entity
                    if (entity instanceof SnakeEntity snake){
                        snake.load(tag);

                        this.snakeRenderer.renderOnNeck(
                                livingEntity,
                                snake,
                                poseStack,
                                buffer,
                                packedLight,
                                partialTick
                        );
                    }
                });
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void registerOnPlayer(EntityRenderDispatcher renderManager, EntityRendererProvider.Context rendererContext) {
        for (EntityRenderer<? extends Player> renderer : renderManager.getSkinMap().values())
            if(renderer instanceof PlayerRenderer playerRenderer){
                EntityOnNeckLayer<?> layer = new EntityOnNeckLayer<>(playerRenderer, rendererContext);
                playerRenderer.addLayer((EntityOnNeckLayer) layer);
            }
    }
}