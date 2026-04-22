package org.primal.client.renderer.entity.layer.wolf;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Crackiness;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.component.DyedItemColor;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.client.model.entity.replaced.WolfModel;
import org.primal.client.renderer.replaced.WolfRenderer;

public class WolfArmorLayer<T extends Wolf, M extends WolfModel<T>> extends RenderLayer<T, M> {

    public WolfArmorLayer(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, @NotNull T livingEntity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!livingEntity.hasArmor()) return;

        RenderType mainArmorRenderType = RenderType.entityCutoutNoCull(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/wolf/armor"+ WolfRenderer.addSlimSuffix(livingEntity.getVariant())+ ".png"));
        RenderType tintArmorRenderType = RenderType.entityCutoutNoCull(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/wolf/armor_tint"+ WolfRenderer.addSlimSuffix(livingEntity.getVariant())+ ".png"));

        int armorColor = 0;
        if(livingEntity.getBodyArmorItem().is(ItemTags.DYEABLE))
            armorColor = DyedItemColor.getOrDefault(livingEntity.getBodyArmorItem(), 0);

        //Base
        this.getParentModel().renderToBuffer(poseStack, bufferSource.getBuffer(mainArmorRenderType), packedLight, OverlayTexture.NO_OVERLAY);

        //Armor tint
        if(armorColor != 0)
            this.getParentModel().renderToBuffer(poseStack, bufferSource.getBuffer(tintArmorRenderType), packedLight, OverlayTexture.NO_OVERLAY, armorColor);

        //Cracks
        Crackiness.Level cracks = Crackiness.WOLF_ARMOR.byDamage(livingEntity.getBodyArmorItem());
        if(cracks!=Crackiness.Level.NONE){
            RenderType crackType = RenderType.entityTranslucent(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID,
                    "textures/entity/wolf/armor"+ WolfRenderer.addSlimSuffix(livingEntity.getVariant())+ "_" + getSuffix(cracks) + ".png"));
            this.getParentModel().renderToBuffer(poseStack, bufferSource.getBuffer(crackType), packedLight, OverlayTexture.NO_OVERLAY);
        }
    }

    private String getSuffix(Crackiness.Level cracks){
        return switch (cracks){
            case NONE -> "";
            case LOW -> "cracks_low";
            case MEDIUM -> "cracks_medium";
            case HIGH -> "cracks_high";
        };
    }
}
