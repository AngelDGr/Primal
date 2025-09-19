package org.primal.datagen.providers;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.primal.Primal_Main;
import org.primal.Primal_Registries;
import org.primal.registry.Primal_Blocks;
import org.primal.registry.Primal_Items;

import java.util.Objects;

public class Primal_ItemModelGenerator extends ItemModelProvider {

    public Primal_ItemModelGenerator(final PackOutput output, final ExistingFileHelper existingFileHelper) {
        super(output, Primal_Main.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        eggItem(Primal_Items.BEAR_SPAWN_EGG.get());
        eggItem(Primal_Items.SHARK_SPAWN_EGG.get());
        eggItem(Primal_Items.CROCODILE_SPAWN_EGG.get());
        eggItem(Primal_Items.EAGLE_SPAWN_EGG.get());

        basicItem(Primal_Items.APPLE_FRITTER.get());
        basicItem(Primal_Items.GOLDEN_APPLE_FRITTER.get());
        basicLayered(Primal_Items.ENCHANTED_GOLDEN_APPLE_FRITTER.get(),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "item/golden_apple_fritter"));

        basicItem(Primal_Items.SHARK_TOOTH.get());
        basicItem(Primal_Items.SEASHELLS.get());

        basicItem(Primal_Items.CROCODILE_SCUTE.get());

        simpleBlockItem(Primal_Blocks.CROCODILE_SCUTE_BLOCK.get());
        simpleBlockItem(Primal_Blocks.CROCODILE_SCUTE_SHINGLE.get());
        simpleBlockItem(Primal_Blocks.CHISELED_CROCODILE_SCUTE.get());
        simpleBlockItem(Primal_Blocks.CROCODILE_SCUTE_STAIRS.get());
        simpleBlockItem(Primal_Blocks.CROCODILE_SCUTE_SLAB.get());

        basicLayered("river_reeds",
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "item/river_reeds"),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "item/river_reeds_overlay"));

        basicLayered("short_river_reeds",
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "item/short_river_reeds"));

        basicItem(Primal_Items.CROCODILE_EGG.get());
        basicItem(Primal_Items.EAGLE_EGG.get());

        simpleBlockItem(Primal_Blocks.NEST_BLOCK.get());

        basicItem(Primal_Items.PAW_BANNER_PATTERN.get());
        basicItem(Primal_Items.JAWS_BANNER_PATTERN.get());
        basicItem(Primal_Items.MARSH_BANNER_PATTERN.get());
        basicItem(Primal_Items.EYRIE_BANNER_PATTERN.get());
    }

    protected void eggItem(final Item eggItem) {
        getBuilder(
                Objects.requireNonNull(Primal_Registries.ITEMS.getRegistry().get().getKey(eggItem)).toString())
                .parent(new ModelFile.UncheckedModelFile("item/template_spawn_egg"));
    }

    public void basicLayered(Item item, ResourceLocation... textureLocations) {
        basicLayered(item.toString(), textureLocations);
    }

    public void basicLayered(String name, ResourceLocation... layers) {
        var builder= getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile("item/generated"));

        for(int i=0; i<layers.length; i++){
            builder.texture("layer"+i, layers[i]);
        }
    }
}
