package org.primal.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;

public class Primal_DamageTypes {

    public static ResourceKey<DamageType> SHARK_TOOTH = register("shark_tooth");
    public static ResourceKey<DamageType> THORNY_ACACIA = register("thorny_acacia");
    public static ResourceKey<DamageType> CHOMP_TRAP = register("chomp_trap");

    public static void boostrapDamageTypes(@NotNull final BootstapContext<DamageType> damageTypeRegisterable) {
        damageTypeRegisterable.register(SHARK_TOOTH, new DamageType("shark_tooth", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0f));
    }

    public static DamageSource sharkTooth(final Level world) {
        return new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(SHARK_TOOTH));
    }

    public static DamageSource thornyAcacia(final Level world) {
        return new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(THORNY_ACACIA));
    }

    public static DamageSource chompTrap(final Level world, Vec3 pos) {
        return new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(CHOMP_TRAP), null, null, pos);
    }

    public static ResourceKey<DamageType> register(final String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, name));
    }

    public static DamageSource of(final Level world, final ResourceKey<DamageType> key) {
        return new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key));
    }
}
