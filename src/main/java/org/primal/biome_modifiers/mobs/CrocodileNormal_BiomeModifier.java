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

public class CrocodileNormal_BiomeModifier implements BiomeModifier {

    @Override
    public void modify(@NotNull Holder<Biome> biome, @NotNull Phase phase, ModifiableBiomeInfo.BiomeInfo.@NotNull Builder builder) {
        if (phase == Phase.ADD) {
            MiscUtil.createBiomeModifier(biome, builder, Primal_Tags.SPAWNS_CROCODILE,
                    Primal_Main.COMMON_CONFIG.crocodileNormalExtraBiomes.get().stream().map(Object::toString).toList(),
                    Primal_Main.COMMON_CONFIG.enableCrocodileNormalSpawn.get(),
                    Primal_Main.COMMON_CONFIG.crocodileNormalSpawnWeight.get(),
                    Primal_Main.COMMON_CONFIG.crocodileNormalMinGroup.get(),
                    Primal_Main.COMMON_CONFIG.crocodileNormalMaxGroup.get(),
                    Primal_Entities.CROCODILE.get());
        }
    }

    @Override
    public @NotNull MapCodec<? extends BiomeModifier> codec() {
        return MiscUtil.createBiomeModifierSerializer("crocodile_normal_spawn").get();
    }

    public static void register() {
        Primal_Registries.BIOME_MODIFIERS.register(
                "crocodile_normal_spawn",
                () -> MapCodec.unit(new CrocodileNormal_BiomeModifier()));
    }
}
