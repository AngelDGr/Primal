package org.primal.util.mob_types;

import net.minecraft.core.Holder;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.biome.Biome;

public interface ReplacedEntityNewVariantHolder<T extends StringRepresentable> {

    void primal$setVariant(T variant);

    T primal$getVariant();

    <M extends ReplacedEntityNewVariantHolder<T>>void primal$setVariantFromBiome(M animal, Holder<Biome> holder);
}
