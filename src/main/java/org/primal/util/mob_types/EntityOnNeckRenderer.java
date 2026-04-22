package org.primal.util.mob_types;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public interface EntityOnNeckRenderer<M extends Entity> {

    <T extends Player> void renderOnNeck(
            T owner,
            M snake,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            float partialTick
    );
}
