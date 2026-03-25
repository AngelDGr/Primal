package org.primal.biome_modifiers.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.registry.Primal_Tags;
import org.primal.registry.Primal_WorldGen;
import org.primal.util.Primal_Util;
import org.primal.worldgen.RandomPatchCustomConfig;

public class Cattails_BiomeModifier implements BiomeModifier {

    @Override
    public void modify(@NotNull Holder<Biome> biome, @NotNull Phase phase, ModifiableBiomeInfo.BiomeInfo.@NotNull Builder builder) {
        if (phase == Phase.ADD) {
            Primal_Util.Generation.createBiomeModifier(biome, builder, Primal_Tags.Biome.SPAWNS_CATTAILS,
                    //ExtraBiomes
                    Primal_Main.COMMON_CONFIG.cattailsExtraBiomes.get().stream().map(Object::toString).toList(),
                    //Step
                    GenerationStep.Decoration.VEGETAL_DECORATION,
                    //Enabled/Disabled
                    Primal_Main.COMMON_CONFIG.cattailsSpawnInWorld.get(),
                    //Feature
                    Primal_WorldGen.CATTAILS_FEATURE.get(),
                    //Config
                    new RandomPatchCustomConfig(10, Primal_Main.COMMON_CONFIG.cattailsPatchSpreadXZ.get(), Primal_Main.COMMON_CONFIG.cattailsPatchSpreadY.get()),
                    //Heightmap
                    PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                    //Rarity
                    Primal_Main.COMMON_CONFIG.cattailsPatchRarity.get());
        }
    }

    @Override
    public @NotNull Codec<? extends BiomeModifier> codec() {
        return Primal_Util.Generation.createBiomeModifierSerializer("cattails_patch_spawn").get();
    }

    public static Codec<? extends BiomeModifier> makeCodec() {
        return Codec.unit(Cattails_BiomeModifier::new);
    }
}
