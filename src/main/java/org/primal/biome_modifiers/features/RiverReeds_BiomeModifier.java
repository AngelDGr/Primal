package org.primal.biome_modifiers.features;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.Primal_Registries;
import org.primal.registry.Primal_Tags;
import org.primal.registry.Primal_WorldGen;
import org.primal.util.Primal_Util;
import org.primal.worldgen.RandomPatchCustomConfig;

public class RiverReeds_BiomeModifier implements BiomeModifier {

    @Override
    public void modify(@NotNull Holder<Biome> biome, @NotNull Phase phase, ModifiableBiomeInfo.BiomeInfo.@NotNull Builder builder) {
        if (phase == Phase.ADD) {
            Primal_Util.Generation.createBiomeModifier(biome, builder, Primal_Tags.Biome.SPAWNS_RIVER_REEDS,
                    //ExtraBiomes
                    Primal_Main.COMMON_CONFIG.riverReedsExtraBiomes.get().stream().map(Object::toString).toList(),
                    //Step
                    GenerationStep.Decoration.VEGETAL_DECORATION,
                    //Enabled/Disabled
                    Primal_Main.COMMON_CONFIG.riverReedsSpawnInWorld.get(),
                    //Feature
                    Primal_WorldGen.RIVER_REEDS_FEATURE.get(),
                    //Config
                    new RandomPatchCustomConfig(10, Primal_Main.COMMON_CONFIG.riverReedsPatchSpreadXZ.get(), Primal_Main.COMMON_CONFIG.riverReedsPatchSpreadY.get()),
                    //Heightmap
                    PlacementUtils.HEIGHTMAP,
                    //Rarity
                    Primal_Main.COMMON_CONFIG.riverReedsPatchRarity.get());
        }
    }

    @Override
    public @NotNull MapCodec<? extends BiomeModifier> codec() {
        return Primal_Util.Generation.createBiomeModifierSerializer("river_reeds_patch_spawn").get();
    }

    public static void register() {
        Primal_Registries.BIOME_MODIFIERS.register(
                "river_reeds_patch_spawn",
                () -> MapCodec.unit(new RiverReeds_BiomeModifier()));
    }
}
