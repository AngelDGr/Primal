package org.primal.client.renderer.entity.layer.wolf;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
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
//        Wolf wolf = renderer.getCurrentEntity();

        if (!hasBackportArmor(livingEntity)) return;

        RenderType mainArmorRenderType = RenderType.entityCutoutNoCull(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/wolf/armor"+ WolfRenderer.addSlimSuffix(livingEntity)+ ".png"));
        RenderType tintArmorRenderType = RenderType.entityCutoutNoCull(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/wolf/armor_tint"+ WolfRenderer.addSlimSuffix(livingEntity)+ ".png"));

        int armorColor = 0;

        if(livingEntity.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof DyeableLeatherItem item)
            armorColor = item.getColor(livingEntity.getItemBySlot(EquipmentSlot.CHEST));

        //Base
        this.getParentModel().renderToBuffer(poseStack, bufferSource.getBuffer(mainArmorRenderType), packedLight, OverlayTexture.NO_OVERLAY,
                1, 1, 1, 1
        );

        //Armor tint
        if (armorColor != 10511680) {
            float red = (float)(armorColor >> 16 & 255) / 255.0F;
            float green = (float)(armorColor >> 8 & 255) / 255.0F;
            float blue = (float)(armorColor & 255) / 255.0F;
            this.getParentModel().renderToBuffer(poseStack, bufferSource.getBuffer(tintArmorRenderType), packedLight, OverlayTexture.NO_OVERLAY,
                    red, green, blue, 1
            );
        }

        //Cracks
        Crackiness cracks = Crackiness.byDamage(livingEntity.getItemBySlot(EquipmentSlot.CHEST));
        if(cracks!=Crackiness.NONE){
            RenderType crackType = RenderType.entityTranslucent(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID,
                    "textures/entity/wolf/armor"+ WolfRenderer.addSlimSuffix(livingEntity)+ "_" + cracks.getId() + ".png"));
            this.getParentModel().renderToBuffer(poseStack, bufferSource.getBuffer(crackType), packedLight, OverlayTexture.NO_OVERLAY,
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
