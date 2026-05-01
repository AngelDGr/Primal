package org.primal.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.primal.injection.SetNeckEntity;
import org.primal.registry.Primal_Entities;
import org.primal.util.mob_types.EntityOnNeckRenderer;

@OnlyIn(Dist.CLIENT)
public class EntityOnNeckLayer<T extends Player> extends RenderLayer<T, PlayerModel<T>> {

    public EntityOnNeckLayer(RenderLayerParent<T, PlayerModel<T>> renderer) {
        super(renderer);
    }

    @Override
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
    @SuppressWarnings({"unchecked", "rawtypes"})
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

                    Minecraft mc = Minecraft.getInstance();
                    EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();
                    EntityRenderer<?> renderer = dispatcher.getRenderer(entity);

                    //Renders the entity
                    if (renderer instanceof EntityOnNeckRenderer rendererOnNeck){
                        entity.load(tag);

                        rendererOnNeck.renderOnNeck(
                                livingEntity,
                                entity,
                                poseStack,
                                buffer,
                                packedLight,
                                partialTick
                        );
                    }
                });
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void registerOnPlayer(EntityRenderDispatcher renderManager) {
        for (EntityRenderer<? extends Player> renderer : renderManager.getSkinMap().values())
            if(renderer instanceof PlayerRenderer playerRenderer){
                EntityOnNeckLayer<?> layer = new EntityOnNeckLayer<>(playerRenderer);
                playerRenderer.addLayer((EntityOnNeckLayer) layer);
            }
    }
}