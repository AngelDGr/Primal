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

public class BearGroup_BiomeModifier implements BiomeModifier {

    @Override
    public void modify(@NotNull Holder<Biome> biome, @NotNull Phase phase, ModifiableBiomeInfo.BiomeInfo.@NotNull Builder builder) {
        if (phase == Phase.ADD) {
            MiscUtil.createBiomeModifier(biome, builder, Primal_Tags.SPAWNS_BEAR,
                    Primal_Main.COMMON_CONFIG.bearGroupExtraBiomes.get().stream().map(Object::toString).toList(),
                    Primal_Main.COMMON_CONFIG.enableBearGroupSpawn.get(),
                    Primal_Main.COMMON_CONFIG.bearGroupSpawnWeight.get(),
                    Primal_Main.COMMON_CONFIG.bearGroupMinGroup.get(),
                    Primal_Main.COMMON_CONFIG.bearGroupMaxGroup.get(),
                    Primal_Entities.BEAR.get());
        }
    }

    @Override
    public @NotNull MapCodec<? extends BiomeModifier> codec() {
        return MiscUtil.createBiomeModifierSerializer("bear_group_spawn").get();
    }

    public static void register() {
        Primal_Registries.BIOME_MODIFIERS.register(
                "bear_group_spawn",
                () -> MapCodec.unit(new BearGroup_BiomeModifier()));
    }
}
