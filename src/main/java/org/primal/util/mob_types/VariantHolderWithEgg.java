package org.primal.util.mob_types;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;

public interface VariantHolderWithEgg<M, T extends Animal> {

    void setVariantFromBiome(T animal, Holder<Biome> holder);

    default M getRareVariant(T animal){return null;}

    default boolean getRareVariantProbability(@NotNull Level level){
        return level.getRandom().nextIntBetweenInclusive(0, 16)<=1;
    }
}
