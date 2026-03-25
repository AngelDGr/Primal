package org.primal.biome_modifiers.mobs;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.registry.Primal_Entities;
import org.primal.registry.Primal_Tags;
import org.primal.util.Primal_Util;

public class DeerSnowy_BiomeModifier implements BiomeModifier {

    @Override
    public void modify(@NotNull Holder<Biome> biome, @NotNull Phase phase, ModifiableBiomeInfo.BiomeInfo.@NotNull Builder builder) {
        if (phase == Phase.ADD) {
            Primal_Util.Generation.createBiomeModifier(biome, builder, Primal_Tags.Biome.SPAWNS_REINDEER,
                    Primal_Main.COMMON_CONFIG.deerSnowyExtraBiomes.get().stream().map(Object::toString).toList(),
                    Primal_Main.COMMON_CONFIG.enableDeerSnowySpawn.get(),
                    Primal_Main.COMMON_CONFIG.deerSnowySpawnWeight.get(),
                    Primal_Main.COMMON_CONFIG.deerSnowyMinGroup.get(),
                    Primal_Main.COMMON_CONFIG.deerSnowyMaxGroup.get(),
                    Primal_Entities.DEER.get());
        }
    }

    @Override
    public @NotNull Codec<? extends BiomeModifier> codec() {
        return Primal_Util.Generation.createBiomeModifierSerializer("deer_snowy_spawn").get();
    }

    public static Codec<? extends BiomeModifier> makeCodec() {
        return Codec.unit(DeerSnowy_BiomeModifier::new);
    }
}
