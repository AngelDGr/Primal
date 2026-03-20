package org.primal.biome_modifiers.mobs;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.Primal_Registries;
import org.primal.registry.Primal_Tags;
import org.primal.util.Primal_Util;

public class DolphinCold_BiomeModifier implements BiomeModifier {

    @Override
    public void modify(@NotNull Holder<Biome> biome, @NotNull Phase phase, ModifiableBiomeInfo.BiomeInfo.@NotNull Builder builder) {
        if (phase == Phase.ADD) {
            Primal_Util.Generation.createBiomeModifier(biome, builder, Primal_Tags.Biome.SPAWNS_DOLPHIN_COLD,
                    Primal_Main.COMMON_CONFIG.dolphinColdExtraBiomes.get().stream().map(Object::toString).toList(),
                    Primal_Main.COMMON_CONFIG.enableDolphinColdSpawn.get(),
                    Primal_Main.COMMON_CONFIG.dolphinColdSpawnWeight.get(),
                    Primal_Main.COMMON_CONFIG.dolphinColdMinGroup.get(),
                    Primal_Main.COMMON_CONFIG.dolphinColdMaxGroup.get(),
                    EntityType.DOLPHIN);
        }
    }

    @Override
    public @NotNull MapCodec<? extends BiomeModifier> codec() {
        return Primal_Util.Generation.createBiomeModifierSerializer("cold_dolphin_spawn").get();
    }

    public static void register() {
        Primal_Registries.BIOME_MODIFIERS.register(
                "cold_dolphin_spawn",
                () -> MapCodec.unit(new DolphinCold_BiomeModifier()));
    }
}
