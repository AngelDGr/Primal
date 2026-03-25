package org.primal.biome_modifiers.mobs;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.registry.Primal_Tags;
import org.primal.util.Primal_Util;

public class RabbitBadlands_BiomeModifier implements BiomeModifier {

    @Override
    public void modify(@NotNull Holder<Biome> biome, @NotNull Phase phase, ModifiableBiomeInfo.BiomeInfo.@NotNull Builder builder) {
        if (phase == Phase.ADD) {
            Primal_Util.Generation.createBiomeModifier(biome, builder, Primal_Tags.Biome.SPAWNS_RABBIT_SAND,
                    Primal_Main.COMMON_CONFIG.rabbitBadlandsExtraBiomes.get().stream().map(Object::toString).toList(),
                    Primal_Main.COMMON_CONFIG.enableRabbitBadlandsSpawn.get(),
                    Primal_Main.COMMON_CONFIG.rabbitBadlandsSpawnWeight.get(),
                    Primal_Main.COMMON_CONFIG.rabbitBadlandsMinGroup.get(),
                    Primal_Main.COMMON_CONFIG.rabbitBadlandsMaxGroup.get(),
                    EntityType.RABBIT);
        }
    }

    @Override
    public @NotNull Codec<? extends BiomeModifier> codec() {
        return Primal_Util.Generation.createBiomeModifierSerializer("badlands_rabbit_spawn").get();
    }

    public static Codec<? extends BiomeModifier> makeCodec() {
        return Codec.unit(RabbitBadlands_BiomeModifier::new);
    }
}
