package org.primal.biome_modifiers.mobs;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.Primal_Registries;
import org.primal.registry.Primal_Entities;
import org.primal.registry.Primal_Tags;
import org.primal.util.Primal_Util;

public class WalrusOcean_BiomeModifier implements BiomeModifier {

    @Override
    public void modify(@NotNull Holder<Biome> biome, @NotNull Phase phase, ModifiableBiomeInfo.BiomeInfo.@NotNull Builder builder) {
        if (phase == Phase.ADD) {
            Primal_Util.Generation.createBiomeModifier(biome, builder, Primal_Tags.Biome.SPAWNS_WALRUS_OCEAN,
                    Primal_Main.COMMON_CONFIG.walrusOceanExtraBiomes.get().stream().map(Object::toString).toList(),
                    Primal_Main.COMMON_CONFIG.enableWalrusOceanSpawn.get(),
                    Primal_Main.COMMON_CONFIG.walrusOceanSpawnWeight.get(),
                    Primal_Main.COMMON_CONFIG.walrusOceanMinGroup.get(),
                    Primal_Main.COMMON_CONFIG.walrusOceanMaxGroup.get(),
                    Primal_Entities.WALRUS.get());
        }
    }

    @Override
    public @NotNull MapCodec<? extends BiomeModifier> codec() {
        return Primal_Util.Generation.createBiomeModifierSerializer("walrus_ocean_spawn").get();
    }

    public static void register() {
        Primal_Registries.BIOME_MODIFIERS.register(
                "walrus_ocean_spawn",
                () -> MapCodec.unit(new WalrusOcean_BiomeModifier()));
    }
}
