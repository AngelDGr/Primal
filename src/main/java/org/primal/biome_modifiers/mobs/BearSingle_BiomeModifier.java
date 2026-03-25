package org.primal.biome_modifiers.mobs;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import org.primal.Primal_Main;
import org.primal.registry.Primal_Entities;
import org.primal.registry.Primal_Tags;
import org.primal.util.Primal_Util;

public class BearSingle_BiomeModifier implements BiomeModifier {

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD) {
            Primal_Util.Generation.createBiomeModifier(biome, builder, Primal_Tags.Biome.SPAWNS_BEAR,
                    Primal_Main.COMMON_CONFIG.bearSingleExtraBiomes.get().stream().map(Object::toString).toList(),
                    Primal_Main.COMMON_CONFIG.enableBearSingleSpawn.get(),
                    Primal_Main.COMMON_CONFIG.bearSingleSpawnWeight.get(),
                    Primal_Main.COMMON_CONFIG.bearSingleMinGroup.get(),
                    Primal_Main.COMMON_CONFIG.bearSingleMaxGroup.get(),
                    Primal_Entities.BEAR.get());
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return Primal_Util.Generation.createBiomeModifierSerializer("bear_single_spawn").get();
    }

    public static Codec<? extends BiomeModifier> makeCodec() {
        return Codec.unit(BearSingle_BiomeModifier::new);
    }
}
