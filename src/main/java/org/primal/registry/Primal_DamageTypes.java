package org.primal.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;

public class Primal_DamageTypes {

    public static ResourceKey<DamageType> SHARK_TOOTH = register("shark_tooth");

    public static void boostrapDamageTypes(@NotNull final BootstrapContext<DamageType> damageTypeRegisterable) {
        damageTypeRegisterable.register(SHARK_TOOTH, new DamageType("shark_tooth", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0f));
    }

    public static DamageSource sharkTooth(final Level world) {
        return new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(SHARK_TOOTH));
    }

    public static ResourceKey<DamageType> register(final String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, name));
    }

    public static DamageSource of(final Level world, final ResourceKey<DamageType> key) {
        return new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key));
    }
}
