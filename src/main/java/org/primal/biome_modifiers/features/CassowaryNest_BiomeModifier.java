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

public class CassowaryNest_BiomeModifier implements BiomeModifier {

    @Override
    public void modify(@NotNull Holder<Biome> biome, @NotNull Phase phase, ModifiableBiomeInfo.BiomeInfo.@NotNull Builder builder) {
        if (phase == Phase.ADD) {
            Primal_Util.Generation.createBiomeModifier(biome, builder, Primal_Tags.Biome.SPAWNS_CASSOWARY,
                    //ExtraBiomes
                    Primal_Main.COMMON_CONFIG.cassowaryNestExtraBiomes.get().stream().map(Object::toString).toList(),
                    //Step
                    GenerationStep.Decoration.VEGETAL_DECORATION,
                    //Enabled/Disabled
                    Primal_Main.COMMON_CONFIG.cassowaryNestSpawnInWorld.get(),
                    //Feature
                    Primal_WorldGen.CASSOWARY_NEST.get(),
                    //Config
                    new RandomPatchCustomConfig(Primal_Main.COMMON_CONFIG.cassowaryNestTries.get(),Primal_Main.COMMON_CONFIG.cassowaryNestSpreadXZ.get(),Primal_Main.COMMON_CONFIG.cassowaryNestSpreadY.get()),
                    //Heightmap
                    PlacementUtils.HEIGHTMAP,
                    //Rarity
                    Primal_Main.COMMON_CONFIG.cassowaryNestRarity.get());
        }
    }

    @Override
    public @NotNull MapCodec<? extends BiomeModifier> codec() {
        return Primal_Util.Generation.createBiomeModifierSerializer("cassowary_nest_spawn").get();
    }

    public static void register() {
        Primal_Registries.BIOME_MODIFIERS.register(
                "cassowary_nest_spawn",
                () -> MapCodec.unit(new CassowaryNest_BiomeModifier()));
    }
}
