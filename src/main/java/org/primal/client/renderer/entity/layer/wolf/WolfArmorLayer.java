package org.primal.client.renderer.entity.layer.wolf;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.primal.Primal_Main;
import org.primal.client.renderer.replaced.WolfRenderer;
import org.primal.entity.replaced.WolfReplaced;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoReplacedEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class WolfArmorLayer extends GeoRenderLayer<WolfReplaced> {

    private final GeoReplacedEntityRenderer<Wolf, WolfReplaced> renderer;
    public WolfArmorLayer(GeoReplacedEntityRenderer<Wolf, WolfReplaced> entityRendererIn) {
        super(entityRendererIn);
        this.renderer=entityRendererIn;
    }

    @Override
    public void render(PoseStack poseStack, WolfReplaced animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        Wolf wolf = renderer.getCurrentEntity();

        if (!hasBackportArmor(wolf)) return;

        RenderType mainArmorRenderType = RenderType.entityCutoutNoCull(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/wolf/armor"+ WolfRenderer.addSlimSuffix(wolf)+ ".png"));
        RenderType tintArmorRenderType = RenderType.entityCutoutNoCull(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/wolf/armor_tint"+ WolfRenderer.addSlimSuffix(wolf)+ ".png"));

        int armorColor = 0;

        if(wolf.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof DyeableLeatherItem item)
            armorColor = item.getColor(wolf.getItemBySlot(EquipmentSlot.CHEST));

        //Base
        this.getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, mainArmorRenderType, bufferSource.getBuffer(mainArmorRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                1, 1, 1, 1
        );

        //Armor tint
        if (armorColor != 10511680) {
            float red = (float)(armorColor >> 16 & 255) / 255.0F;
            float green = (float)(armorColor >> 8 & 255) / 255.0F;
            float blue = (float)(armorColor & 255) / 255.0F;
            this.getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, tintArmorRenderType, bufferSource.getBuffer(tintArmorRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                    red, green, blue, 1
            );
        }

        //Cracks
        Crackiness cracks = Crackiness.byDamage(wolf.getItemBySlot(EquipmentSlot.CHEST));
        if(cracks!=Crackiness.NONE){
            RenderType crackType = RenderType.entityTranslucent(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID,
                    "textures/entity/wolf/armor"+ WolfRenderer.addSlimSuffix(wolf)+ "_" + cracks.getId() + ".png"));
            this.getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, crackType, bufferSource.getBuffer(crackType), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                    1, 1, 1, 1
            );
        }
    }

    //Compat for backport
    private static boolean hasBackportArmor(Wolf wolf){
        var itemStack = wolf.getItemBySlot(EquipmentSlot.CHEST);
        var itemName= BuiltInRegistries.ITEM.getKey(itemStack.getItem());

        return !itemStack.isEmpty() && itemName.getPath().equals("wolf_armor") && itemName.getNamespace().equals("minecraft");
    }

    private enum Crackiness {
        NONE(""),
        LOW("cracks_low"),
        MEDIUM("cracks_medium"),
        HIGH("cracks_high");

        private final String id;

        Crackiness(String id) {
            this.id=id;
        }

        public static Crackiness byFraction(float fraction) {
            float fractionLow= 0.95F;
            float fractionMedium= 0.69F;
            float fractionHigh=0.32F;

            if (fraction < fractionHigh) {
                return Crackiness.HIGH;
            } else if (fraction < fractionMedium) {
                return Crackiness.MEDIUM;
            } else {
                return fraction < fractionLow ? Crackiness.LOW : Crackiness.NONE;
            }
        }

        public static Crackiness byDamage(ItemStack stack) {
            return !stack.isDamageableItem() ? Crackiness.NONE : byDamage(stack.getDamageValue(), stack.getMaxDamage());
        }

        public static Crackiness byDamage(int damage, int durability) {
            return byFraction((float)(durability - damage) / (float)durability);
        }

        public String getId() {
            return id;
        }
    }
}
