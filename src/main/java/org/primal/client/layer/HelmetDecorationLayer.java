package org.primal.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Registries;
import org.primal.item.HelmetDecoration;
import org.primal.item.component.HelmetDecorationComponent;
import org.primal.mixin.client.EntityRenderDispatcherAccessor;
import org.primal.registry.Primal_Items;
import org.primal.util.Primal_Util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class HelmetDecorationLayer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

    private final Map<HelmetDecoration<?>, HumanoidModel<?>> modelMap = new HashMap<>();

    public HelmetDecorationLayer(final RenderLayerParent<T, M> featureContext, final EntityRendererProvider.Context rendererContext) {
        super(featureContext);
        for (var helmetDecorationEntry : Primal_Registries.HELMET_DECORATIONS_REGISTRY.entrySet()){
            if(!helmetDecorationEntry.getValue().getAsItem().equals(ItemStack.EMPTY.getItem()))
                modelMap.put(helmetDecorationEntry.getValue(), helmetDecorationEntry.getValue().createHelmetDecorationModel(rendererContext));
        }
    }

    @Override
    public void render(@NotNull final PoseStack matrices, @NotNull final MultiBufferSource vertexConsumers, final int light,
                       @NotNull T humanoid,
                       final float limbAngle, final float limbDistance, final float tickDelta, final float animationProgress, final float headYaw, final float headPitch) {
        var helmet = humanoid.getItemBySlot(EquipmentSlot.HEAD);
        var decorations= helmet.get(Primal_Items.Components.HELMET_DECORATION);

        if(helmet.isEmpty() || decorations==null) return;

        displayHeadDecoration(decorations, matrices, vertexConsumers, light);
    }

    private void displayHeadDecoration(HelmetDecorationComponent component, PoseStack matrices, @NotNull final MultiBufferSource vertexConsumers, final int light){
        var modelRight = modelMap.get(component.right());
        var modelLeft  = modelMap.get(component.left());

        //Right
        if(modelRight != null){
            var headPart = modelRight.head;
            headPart.getChild("right").visible=true;
            headPart.getChild("left").visible=false;

            final VertexConsumer textureBuffer =
                    vertexConsumers.getBuffer(
                            RenderType.entityCutoutNoCull(
                                    Primal_Util.Visuals.getHelmetDecorationTexture(
                                            Objects.requireNonNull(Primal_Registries.HELMET_DECORATIONS_REGISTRY.getKey(component.right()))
                                    )
                            )
                    );
            headPart.copyFrom(this.getParentModel().head);
            headPart.render(matrices, textureBuffer, light, OverlayTexture.NO_OVERLAY, -1);
        }

        //Left
        if(modelLeft != null){
            var headPart = modelLeft.head;
            headPart.getChild("right").visible=false;
            headPart.getChild("left").visible=true;

            final VertexConsumer textureBuffer =
                    vertexConsumers.getBuffer(
                            RenderType.entityCutoutNoCull(
                                    Primal_Util.Visuals.getHelmetDecorationTexture(
                                            Objects.requireNonNull(Primal_Registries.HELMET_DECORATIONS_REGISTRY.getKey(component.left()))
                                    )
                            )
                    );
            headPart.copyFrom(this.getParentModel().head);
            headPart.render(matrices, textureBuffer, light, OverlayTexture.NO_OVERLAY, -1);
        }
    }



    public static void registerOnAll(EntityRenderDispatcher renderManager, EntityRendererProvider.Context rendererContext) {
        for (EntityRenderer<? extends Player> renderer : renderManager.getSkinMap().values())
            if(renderer instanceof PlayerRenderer playerRenderer)
                registerOnPlayer(playerRenderer, rendererContext);

        for (EntityRenderer<?> renderer : ((EntityRenderDispatcherAccessor) renderManager).primal$getRenderers().values()){
            if(renderer instanceof HumanoidMobRenderer<?,?> humanoidMobRenderer)
                registerOnMob(humanoidMobRenderer, rendererContext);
            if(renderer instanceof ArmorStandRenderer armorStandRenderer)
                registerOnArmorStand(armorStandRenderer, rendererContext);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void registerOnPlayer(PlayerRenderer playerRenderer, EntityRendererProvider.Context rendererContext) {
        HelmetDecorationLayer<?, ?> layer = new HelmetDecorationLayer<>(playerRenderer, rendererContext);
        playerRenderer.addLayer((HelmetDecorationLayer) layer);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void registerOnMob(HumanoidMobRenderer<?, ?> entityRenderer, EntityRendererProvider.Context rendererContext) {
        HelmetDecorationLayer<?, ?> layer = new HelmetDecorationLayer<>(entityRenderer, rendererContext);
        entityRenderer.addLayer((HelmetDecorationLayer) layer);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void registerOnArmorStand(ArmorStandRenderer armorStandRenderer, EntityRendererProvider.Context rendererContext) {
        HelmetDecorationLayer<?, ?> layer = new HelmetDecorationLayer<>(armorStandRenderer, rendererContext);
        armorStandRenderer.addLayer((HelmetDecorationLayer) layer);
    }
}
