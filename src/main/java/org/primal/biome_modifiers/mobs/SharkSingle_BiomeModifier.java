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
import org.primal.util.MiscUtil;

public class SharkSingle_BiomeModifier implements BiomeModifier {

    @Override
    public void modify(@NotNull Holder<Biome> biome, @NotNull Phase phase, ModifiableBiomeInfo.BiomeInfo.@NotNull Builder builder) {
        if (phase == Phase.ADD) {

            MiscUtil.createBiomeModifier(biome, builder, Primal_Tags.Biome.SPAWNS_SHARK,
                    Primal_Main.COMMON_CONFIG.sharkSingleExtraBiomes.get().stream().map(Object::toString).toList(),
                    Primal_Main.COMMON_CONFIG.enableSingleSharkSpawn.get(),
                    Primal_Main.COMMON_CONFIG.sharkSingleSpawnWeight.get(),
                    Primal_Main.COMMON_CONFIG.sharkSingleMinGroup.get(),
                    Primal_Main.COMMON_CONFIG.sharkSingleMaxGroup.get(),
                    Primal_Entities.SHARK.get());
        }
    }

    @Override
    public @NotNull MapCodec<? extends BiomeModifier> codec() {
        return MiscUtil.createBiomeModifierSerializer("shark_single_spawn").get();
    }

    public static void register() {
        Primal_Registries.BIOME_MODIFIERS.register(
                "shark_single_spawn",
                () -> MapCodec.unit(new SharkSingle_BiomeModifier()));
    }
}
