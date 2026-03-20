package org.primal.client.renderer.replaced;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Rabbit;
import org.primal.Primal_Main;
import org.primal.client.model.replaced.RabbitModel;
import org.primal.entity.replaced.RabbitReplaced;
import org.primal.util.mob_types.ReplacedEntityNewVariantHolder;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoReplacedEntityRenderer;

public class RabbitRenderer extends GeoReplacedEntityRenderer<Rabbit, RabbitReplaced> {

    public RabbitRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new RabbitModel(), new RabbitReplaced());
        shadowRadius=0.3F;
    }

    @Override
    public void scaleModelForRender(float widthScale, float heightScale, PoseStack poseStack, RabbitReplaced animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
        var bone = model.getBone("head");

        float headScale = this.currentEntity.isBaby() ? 2.f : 1.f;
        bone.ifPresent(geoBone ->
                geoBone.updateScale(headScale, headScale, headScale));
        if (this.currentEntity.isBaby()) {
            widthScale = heightScale = .5f;
        }
        super.scaleModelForRender(widthScale, heightScale, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
    }

    @SuppressWarnings("unchecked")
    protected ReplacedEntityNewVariantHolder<RabbitReplaced.PrimalVariant> getVariantHolder(){
        return (ReplacedEntityNewVariantHolder<RabbitReplaced.PrimalVariant>) this.currentEntity;
    }

    @Override
    public ResourceLocation getTextureLocation(RabbitReplaced animatable) {
        Rabbit rabbit = this.currentEntity;
        String s = ChatFormatting.stripFormatting(rabbit.getName().getString());

        //Custom names
        if("Toast".equals(s))
            return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/rabbit/toast.png");
        else if ("Bonnie".equals(s))
            return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/rabbit/bonnie.png");

        //Primal variants
        if(getVariantHolder().primal$getVariant().getId()!=99)
            return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/rabbit/primal/"+getVariantHolder().primal$getVariant().getSerializedName()+".png");

        //Vanilla variants
        return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/rabbit/"+rabbit.getVariant().getSerializedName()+".png");
    }
}
