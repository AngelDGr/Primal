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
import org.primal.Primal_Registries;
import org.primal.item.HelmetDecoration;
import org.primal.registry.Primal_HelmetDecorations;

import java.util.Objects;

public class Primal_HelmetDecorationModelGenerator extends ModelProvider<ItemModelBuilder> {

    public Primal_HelmetDecorationModelGenerator(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Primal_Main.MOD_ID, "helmet_decoration", ItemModelBuilder::new, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicDecoration(Primal_HelmetDecorations.FALLOW_DEER.get());
        basicDecoration(Primal_HelmetDecorations.REINDEER.get());
        basicDecoration(Primal_HelmetDecorations.WHITETAIL.get());
        basicDecoration(Primal_HelmetDecorations.GOAT.get());
    }

    public void basicDecoration(HelmetDecoration<?> type) {
        this.basicDecoration(Objects.requireNonNull(Primal_Registries.HELMET_DECORATIONS_REGISTRY.getKey(type)).getPath(), "_l");
        this.basicDecoration(Objects.requireNonNull(Primal_Registries.HELMET_DECORATIONS_REGISTRY.getKey(type)).getPath(), "_r");
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
