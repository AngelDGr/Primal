package org.primal.client.renderer.entity.layer.wolf;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Crackiness;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.component.DyedItemColor;
import org.jetbrains.annotations.Nullable;
import org.primal.Primal_Main;
import org.primal.client.renderer.replaced.WolfRenderer;
import org.primal.entity.replaced.WolfReplaced;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoReplacedEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.Color;

public class WolfArmorLayer extends GeoRenderLayer<WolfReplaced> {

    private final GeoReplacedEntityRenderer<Wolf, WolfReplaced> renderer;
    public WolfArmorLayer(GeoReplacedEntityRenderer<Wolf, WolfReplaced> entityRendererIn) {
        super(entityRendererIn);
        this.renderer=entityRendererIn;
    }

    @Override
    public void render(PoseStack poseStack, WolfReplaced animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        Wolf wolf = renderer.getCurrentEntity();

        if (!wolf.hasArmor()) return;

        RenderType mainArmorRenderType = RenderType.entityCutoutNoCull(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/wolf/armor"+ WolfRenderer.addSlimSuffix(wolf.getVariant())+ ".png"));
        RenderType tintArmorRenderType = RenderType.entityCutoutNoCull(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/wolf/armor_tint"+ WolfRenderer.addSlimSuffix(wolf.getVariant())+ ".png"));

        int armorColor = 0;
        if(wolf.getBodyArmorItem().is(ItemTags.DYEABLE))
            armorColor = DyedItemColor.getOrDefault(wolf.getBodyArmorItem(), 0);

        //Base
        this.getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, mainArmorRenderType, bufferSource.getBuffer(mainArmorRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY, Color.WHITE.argbInt());

        //Armor tint
        if(armorColor != 0)
            this.getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, tintArmorRenderType, bufferSource.getBuffer(tintArmorRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY, armorColor);

        //Cracks
        Crackiness.Level cracks = Crackiness.WOLF_ARMOR.byDamage(wolf.getBodyArmorItem());
        if(cracks!=Crackiness.Level.NONE){
            RenderType crackType = RenderType.entityTranslucent(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID,
                    "textures/entity/wolf/armor"+ WolfRenderer.addSlimSuffix(wolf.getVariant())+ "_" + getSuffix(cracks) + ".png"));
            this.getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, crackType, bufferSource.getBuffer(crackType), partialTick, packedLight, OverlayTexture.NO_OVERLAY, Color.WHITE.argbInt());
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
