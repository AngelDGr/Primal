/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package org.primal.datagen.providers;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.item.HelmetDecorationType;

public class Primal_HelmetDecorationModelGenerator extends ModelProvider<ItemModelBuilder> {

    public Primal_HelmetDecorationModelGenerator(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Primal_Main.MOD_ID, "helmet_decoration", ItemModelBuilder::new, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicDecoration(HelmetDecorationType.FALLOW_DEER);
        basicDecoration(HelmetDecorationType.REINDEER);
        basicDecoration(HelmetDecorationType.WHITETAIL);
        basicDecoration(HelmetDecorationType.GOAT);
    }

    public void basicDecoration(HelmetDecorationType type) {
        this.basicDecoration(type.getName(), "_l");
        this.basicDecoration(type.getName(), "_r");
    }

    public void basicDecoration(String id, String suffix) {
        this.getBuilder(id + suffix)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "helmet_decoration/item/" + id + suffix));
    }

    @Override
    public @NotNull String getName() {
        return "Helmet Decorations Models: " + modid;
    }
}
