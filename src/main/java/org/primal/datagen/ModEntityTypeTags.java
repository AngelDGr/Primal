package org.primal.datagen;

import org.primal.Primal_Main;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public final class ModEntityTypeTags {

    public static final TagKey<EntityType<?>> BEAR_HUNTABLE = create("bear_huntable");

    private static TagKey<EntityType<?>> create(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Primal_Main.MODID, name));
    }
}
