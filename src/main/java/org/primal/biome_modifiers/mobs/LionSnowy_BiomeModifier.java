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

public class LionSnowy_BiomeModifier implements BiomeModifier {

    @Override
    public void modify(@NotNull Holder<Biome> biome, @NotNull Phase phase, ModifiableBiomeInfo.BiomeInfo.@NotNull Builder builder) {
        if (phase == Phase.ADD) {
            Primal_Util.Generation.createBiomeModifier(biome, builder, Primal_Tags.Biome.SPAWNS_CAVE_LION,
                    Primal_Main.COMMON_CONFIG.lionSnowyExtraBiomes.get().stream().map(Object::toString).toList(),
                    Primal_Main.COMMON_CONFIG.enableLionSnowySpawn.get(),
                    Primal_Main.COMMON_CONFIG.lionSnowySpawnWeight.get(),
                    Primal_Main.COMMON_CONFIG.lionSnowyMinGroup.get(),
                    Primal_Main.COMMON_CONFIG.lionSnowyMaxGroup.get(),
                    Primal_Entities.LION.get());
        }
    }

    @Override
    public @NotNull MapCodec<? extends BiomeModifier> codec() {
        return Primal_Util.Generation.createBiomeModifierSerializer("lion_snowy_spawn").get();
    }

    public static void register() {
        Primal_Registries.BIOME_MODIFIERS.register(
                "lion_snowy_spawn",
                () -> MapCodec.unit(new LionSnowy_BiomeModifier()));
    }
}
