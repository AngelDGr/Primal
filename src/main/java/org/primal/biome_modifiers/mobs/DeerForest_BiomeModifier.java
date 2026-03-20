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

public class DeerForest_BiomeModifier implements BiomeModifier {

    @Override
    public void modify(@NotNull Holder<Biome> biome, @NotNull Phase phase, ModifiableBiomeInfo.BiomeInfo.@NotNull Builder builder) {
        if (phase == Phase.ADD) {
            Primal_Util.Generation.createBiomeModifier(biome, builder, Primal_Tags.Biome.SPAWNS_DEER,
                    Primal_Main.COMMON_CONFIG.deerForestExtraBiomes.get().stream().map(Object::toString).toList(),
                    Primal_Main.COMMON_CONFIG.enableDeerForestSpawn.get(),
                    Primal_Main.COMMON_CONFIG.deerForestSpawnWeight.get(),
                    Primal_Main.COMMON_CONFIG.deerForestMinGroup.get(),
                    Primal_Main.COMMON_CONFIG.deerForestMaxGroup.get(),
                    Primal_Entities.DEER.get());
        }
    }

    @Override
    public @NotNull MapCodec<? extends BiomeModifier> codec() {
        return Primal_Util.Generation.createBiomeModifierSerializer("deer_forest_spawn").get();
    }

    public static void register() {
        Primal_Registries.BIOME_MODIFIERS.register(
                "deer_forest_spawn",
                () -> MapCodec.unit(new DeerForest_BiomeModifier()));
    }
}
