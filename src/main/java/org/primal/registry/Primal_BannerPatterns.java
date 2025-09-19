package org.primal.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.primal.Primal_Main;

import java.util.List;

public class Primal_BannerPatterns {
    public static final ResourceKey<BannerPattern> PAW = create("paw");
    public static final ResourceKey<BannerPattern> JAWS = create("jaws");
    public static final ResourceKey<BannerPattern> MARSH = create("marsh");
    public static final ResourceKey<BannerPattern> EYRIE = create("eyrie");

    public static final List<ResourceKey<BannerPattern>> Banner_Patterns= List.of(PAW, JAWS, MARSH, EYRIE);

    @SuppressWarnings("unused")
    public static void bootstrap(BootstrapContext<BannerPattern> context) {
        register(context, PAW);
        register(context, JAWS);
        register(context, MARSH);
        register(context, EYRIE);
    }

    private static ResourceKey<BannerPattern> create(String name) {
        return ResourceKey.create(Registries.BANNER_PATTERN, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, name));
    }

    public static void register(BootstrapContext<BannerPattern> context, ResourceKey<BannerPattern> resourceKey) {
        context.register(resourceKey, new BannerPattern(resourceKey.location(), "block.minecraft.banner." + resourceKey.location().toShortLanguageKey()));
    }
}
