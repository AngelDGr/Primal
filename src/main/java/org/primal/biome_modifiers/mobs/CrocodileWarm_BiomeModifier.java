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

public class CrocodileWarm_BiomeModifier implements BiomeModifier {

    @Override
    public void modify(@NotNull Holder<Biome> biome, @NotNull Phase phase, ModifiableBiomeInfo.BiomeInfo.@NotNull Builder builder) {
        if (phase == Phase.ADD) {
            Primal_Util.Generation.createBiomeModifier(biome, builder, Primal_Tags.Biome.SPAWNS_BROWN_CROCODILE,
                    Primal_Main.COMMON_CONFIG.crocodileWarmExtraBiomes.get().stream().map(Object::toString).toList(),
                    Primal_Main.COMMON_CONFIG.enableCrocodileWarmSpawn.get(),
                    Primal_Main.COMMON_CONFIG.crocodileWarmSpawnWeight.get(),
                    Primal_Main.COMMON_CONFIG.crocodileWarmMinGroup.get(),
                    Primal_Main.COMMON_CONFIG.crocodileWarmMaxGroup.get(),
                    Primal_Entities.CROCODILE.get());
        }
    }

    @Override
    public @NotNull Codec<? extends BiomeModifier> codec() {
        return Primal_Util.Generation.createBiomeModifierSerializer("crocodile_warm_spawn").get();
    }

    public static Codec<? extends BiomeModifier> makeCodec() {
        return Codec.unit(CrocodileWarm_BiomeModifier::new);
    }
}
