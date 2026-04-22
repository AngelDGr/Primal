package org.primal.client.renderer.replaced;

import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Rabbit;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.client.model.entity.replaced.RabbitModel;
import org.primal.entity.replaced.RabbitReplaced;
import org.primal.util.mob_types.ReplacedEntityNewVariantHolder;

public class RabbitRenderer extends MobRenderer<Rabbit, RabbitModel<Rabbit>> {

    public RabbitRenderer(EntityRendererProvider.Context context) {
        super(context, new RabbitModel<>(context.bakeLayer(RabbitModel.LAYER_LOCATION)), 0.3f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Rabbit rabbit) {
        String s = ChatFormatting.stripFormatting(rabbit.getName().getString());

        //Custom names
        if("Toast".equals(s))
            return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/rabbit/toast.png");
        else if ("Bonnie".equals(s))
            return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/rabbit/bonnie.png");

        //Primal variants
        if(getVariantHolder(rabbit).primal$getVariant().getId()!=99)
            return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/rabbit/primal/"+getVariantHolder(rabbit).primal$getVariant().getSerializedName()+".png");

        //Vanilla variants
        return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "textures/entity/rabbit/"+rabbit.getVariant().getSerializedName()+".png");
    }

    @SuppressWarnings("unchecked")
    protected ReplacedEntityNewVariantHolder<RabbitReplaced.PrimalVariant> getVariantHolder(Rabbit rabbit) {
        return (ReplacedEntityNewVariantHolder<RabbitReplaced.PrimalVariant>) rabbit;
    }
}
