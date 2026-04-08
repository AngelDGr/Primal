package org.primal.item;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.primal.Primal_Registries;
import org.primal.registry.Primal_HelmetDecorations;
import org.primal.client.model.helmet_decoration.*;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 Class for helmet decorations.
 @see Primal_HelmetDecorations
 */
@SuppressWarnings("all")
public class HelmetDecoration<T extends LivingEntity> {

    private final Supplier<Item> asItem;
    private final Style descriptionStyle;
    /**
     * This needs to be assigned on the client
     * @see HelmetDecoration#registerOnClient(Supplier, ModelLayerLocation, Class)
     */
    private Function<EntityRendererProvider.Context, HumanoidModel<?>> modelFactory;

    /**
     * Constructor for registration
     @param asItem The item that is used to apply the helmet decoration to a helmet
     @param descriptionStyle The style that the text will have in the description of the helmet with the decoration
     */
    public HelmetDecoration(Supplier<Item> asItem, Style descriptionStyle) {
        this.asItem = asItem;
        this.descriptionStyle = descriptionStyle;
    }

    public Style getDescriptionStyle() {
        return descriptionStyle;
    }

    public Item getAsItem() {
        return asItem.get();
    }

    public static HelmetDecoration<?> fromItem(Item item) {
        for (var helmetDecorationEntry : Primal_Registries.HELMET_DECORATIONS_REGISTRY.get().getEntries()){
            if(helmetDecorationEntry.getValue().getAsItem().equals(item)){
                return helmetDecorationEntry.getValue();
            }
        }

        return Primal_HelmetDecorations.NONE.get();
    }

    @OnlyIn(Dist.CLIENT)
    public void setModelFactory(Function<EntityRendererProvider.Context, HumanoidModel<?>> factory) {
        this.modelFactory = factory;
    }

    @OnlyIn(Dist.CLIENT)
    public HumanoidModel<?> createHelmetDecorationModel(EntityRendererProvider.Context ctx) {
        if (modelFactory == null) {
            throw new IllegalStateException("Model factory not set for helmet decoration: " + BuiltInRegistries.ITEM.getKey(this.asItem.get()));
        }
        return modelFactory.apply(ctx);
    }

    /**
     * Useful registering for simple models on the main client event, you can use {@link HelmetDecoration#setModelFactory(Function)} instead.
     * @param decoration The supplier registered of the helmet decoration
     * @param layer The layer that uses the model
     * @param modelClass Like {@link DeerAntlersModel} and {@link GoatHornsModel}.
     * It needs two main bones called "right" and "left"
     */
    @OnlyIn(Dist.CLIENT)
    public static void registerOnClient(Supplier<HelmetDecoration<?>> decoration, ModelLayerLocation layer, Class<? extends HumanoidModel<?>> modelClass) {
        decoration.get().setModelFactory(ctx -> {
            try {
                return modelClass.getConstructor(ModelPart.class).newInstance(ctx.bakeLayer(layer));
            } catch (Exception e) {
                throw new RuntimeException("Failed to create model: " + modelClass, e);
            }
        });
    }
}
