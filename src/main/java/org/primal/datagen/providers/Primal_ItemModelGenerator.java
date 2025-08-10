package org.primal.datagen.providers;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.primal.Primal_Main;
import org.primal.Primal_Registries;
import org.primal.registry.Primal_Items;

import java.util.Objects;

public class Primal_ItemModelGenerator extends ItemModelProvider {

    public Primal_ItemModelGenerator(final PackOutput output, final ExistingFileHelper existingFileHelper) {
        super(output, Primal_Main.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        //Spawn Eggs
        {
            eggItem(Primal_Items.BEAR_SPAWN_EGG.get());
        }

    }

    protected void eggItem(final Item eggItem) {
        getBuilder(
                Objects.requireNonNull(Primal_Registries.ITEMS.getRegistry().get().getKey(eggItem)).toString())
                .parent(new ModelFile.UncheckedModelFile("item/template_spawn_egg"));
    }
}
