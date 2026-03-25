package org.primal.client.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.primal.item.ConchShellItem;
import org.primal.registry.Primal_Items;
import org.primal.util.Primal_Util;

public class ConchShellClientExtension implements IClientItemExtensions {

    @Override
    public boolean applyForgeHandTransform(
            @NotNull PoseStack poseStack,
            @NotNull LocalPlayer player,
            @NotNull HumanoidArm arm,
            @NotNull ItemStack stack,
            float partialTick,
            float equipProcess,
            float swingProcess) {

        if(!Primal_Util.OneTwentyEquivalent.Components.has(stack, Primal_Items.Components.CONCH_SHELL)) return false;

        float scaleRate = -1f *(player.getUseItemRemainingTicks() - ConchShellItem.MAX_DURATION) / ConchShellItem.RELEASE_TIME;
        if (scaleRate > 1.0F) scaleRate = 1.0F;

        if(player.getUseItemRemainingTicks()==0) return false;

        poseStack.scale(1.0F, 1.0F, 1.0F + (scaleRate * 0.5F));

        return false;
    }
}
