package org.primal.client.renderer.entity.layer.crocodile;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Client;
import org.primal.client.model.entity.CrocodileModel;
import org.primal.entity.animal.CrocodileEntity;

public class CrocodilePreyLayer<T extends CrocodileEntity, M extends CrocodileModel<T>> extends RenderLayer<T, M> {

    public CrocodilePreyLayer(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, T entity, float pLimbSwing, float pLimbSwingAmount, float partialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (entity.isVehicle()) {
            for (Entity passenger : entity.getPassengers()) {
                if (Primal_Client.isFirstPersonPlayer(passenger)) {
                    continue;
                }

                Primal_Client.BLOCKED_RENDER_ENTITIES.remove(passenger.getUUID());

                poseStack.pushPose();

                this.getParentModel().translateToMouth(poseStack);

//                poseStack.translate(0.0F, -0.5F, 0.5F);

                poseStack.scale(-1.0F, -1.0F, 1.0F);

                renderPassenger(passenger, partialTick, poseStack, bufferSource, packedLight);

                poseStack.popPose();

                Primal_Client.BLOCKED_RENDER_ENTITIES.add(passenger.getUUID());
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