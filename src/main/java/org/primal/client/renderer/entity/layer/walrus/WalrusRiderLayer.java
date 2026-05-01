package org.primal.client.renderer.entity.layer.walrus;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Client;
import org.primal.entity.animal.WalrusEntity;
import org.primal.client.model.entity.WalrusModel;

public class WalrusRiderLayer<T extends WalrusEntity, M extends WalrusModel<T>> extends RenderLayer<T, M> {

    public WalrusRiderLayer(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, T entity, float pLimbSwing, float pLimbSwingAmount, float partialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (entity.isVehicle()) {
            for (Entity passenger : entity.getPassengers()) {
                if (Primal_Client.Primal_ClientMainGameBus.isFirstPersonPlayer(passenger)) {
                    continue;
                }

                Primal_Client.Primal_ClientMainGameBus.BLOCKED_RENDER_ENTITIES.remove(passenger.getUUID());

                poseStack.pushPose();

                this.getParentModel().translateToSaddle(poseStack);

                poseStack.translate(0.0F, -0.5F, 0.5F);

                poseStack.scale(-1.0F, -1.0F, 1.0F);

                if (passenger instanceof LivingEntity livingPassenger) {
                    float bodyRot = net.minecraft.util.Mth.rotLerp(partialTick, livingPassenger.yBodyRotO, livingPassenger.yBodyRot);
                    poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(bodyRot - 180.0F));

                    if(passenger instanceof Player player){
                        player.yBodyRot=entity.yBodyRot;
                        player.yBodyRotO=entity.yBodyRotO;
                    }
                }

                renderPassenger(passenger, partialTick, poseStack, bufferSource, packedLight);

                poseStack.popPose();

                Primal_Client.Primal_ClientMainGameBus.BLOCKED_RENDER_ENTITIES.add(passenger.getUUID());
            }
        }
    }

    @SuppressWarnings("ConstantValue")
    private void renderPassenger(Entity passenger, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        EntityRenderer<? super Entity> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(passenger);
        if (renderer != null) {
            renderer.render(passenger, 0.0F, partialTicks, poseStack, bufferSource, packedLight);
        }
    }
}