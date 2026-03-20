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

public class LionSavanna_BiomeModifier implements BiomeModifier {

    @Override
    public void modify(@NotNull Holder<Biome> biome, @NotNull Phase phase, ModifiableBiomeInfo.BiomeInfo.@NotNull Builder builder) {
        if (phase == Phase.ADD) {
            Primal_Util.Generation.createBiomeModifier(biome, builder, Primal_Tags.Biome.SPAWNS_LION,
                    Primal_Main.COMMON_CONFIG.lionSavannaExtraBiomes.get().stream().map(Object::toString).toList(),
                    Primal_Main.COMMON_CONFIG.enableLionSavannaSpawn.get(),
                    Primal_Main.COMMON_CONFIG.lionSavannaSpawnWeight.get(),
                    Primal_Main.COMMON_CONFIG.lionSavannaMinGroup.get(),
                    Primal_Main.COMMON_CONFIG.lionSavannaMaxGroup.get(),
                    Primal_Entities.LION.get());
        }
    }

    @Override
    public @NotNull MapCodec<? extends BiomeModifier> codec() {
        return Primal_Util.Generation.createBiomeModifierSerializer("lion_savanna_spawn").get();
    }

    public static void register() {
        Primal_Registries.BIOME_MODIFIERS.register(
                "lion_savanna_spawn",
                () -> MapCodec.unit(new LionSavanna_BiomeModifier()));
    }
}
