package org.primal.biome_modifiers.features;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.Primal_Registries;
import org.primal.block.SeashellsBlock;
import org.primal.registry.Primal_Blocks;
import org.primal.registry.Primal_Tags;
import org.primal.registry.Primal_WorldGen;
import org.primal.util.MiscUtil;

public class Seashells_BiomeModifier implements BiomeModifier {

    @Override
    public void modify(@NotNull Holder<Biome> biome, @NotNull Phase phase, ModifiableBiomeInfo.BiomeInfo.@NotNull Builder builder) {
        if (phase == Phase.ADD) {
            MiscUtil.createBiomeModifier(biome, builder, Primal_Tags.SPAWNS_SEASHELLS,
                    //ExtraBiomes
                    Primal_Main.COMMON_CONFIG.seaShellsExtraBiomes.get().stream().map(Object::toString).toList(),
                    //Step
                    GenerationStep.Decoration.VEGETAL_DECORATION,
                    //Enabled/Disabled
                    Primal_Main.COMMON_CONFIG.seaShellsSpawnInWorld.get(),
                    //Feature
                    Feature.FLOWER,
                    //Config
                    createSeashells(Primal_Main.COMMON_CONFIG.seaShellsPatchTries.get(), Primal_Main.COMMON_CONFIG.seaShellsPatchSpreadXZ.get(), Primal_Main.COMMON_CONFIG.seaShellsPatchSpreadY.get()),
                    //Heightmap
                    PlacementUtils.HEIGHTMAP_TOP_SOLID,
                    //Rarity
                    Primal_Main.COMMON_CONFIG.seaShellsPatchRarity.get());
        }
    }

    @SuppressWarnings("all")
    private static RandomPatchConfiguration createSeashells(int tries, int xzSpread, int ySpread){
        SimpleWeightedRandomList.Builder<BlockState> builder = SimpleWeightedRandomList.builder();

        for (int i = 1; i <= 4; i++) {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                builder.add(
                        Primal_Blocks.SEASHELLS.get().defaultBlockState().setValue(SeashellsBlock.AMOUNT, i).setValue(SeashellsBlock.FACING, direction), 1
                );
            }
        }

        return new RandomPatchConfiguration(
                tries, xzSpread, ySpread,
                PlacementUtils.filtered(Primal_WorldGen.SEASHELLS.get(), new SimpleBlockConfiguration(new WeightedStateProvider(builder)), BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE)
        );
    }

    @Override
    public @NotNull MapCodec<? extends BiomeModifier> codec() {
        return MiscUtil.createBiomeModifierSerializer("seashells_patch_spawn").get();
    }

    public static void register() {
        Primal_Registries.BIOME_MODIFIERS.register(
                "seashells_patch_spawn",
                () -> MapCodec.unit(new Seashells_BiomeModifier()));
    }
}
