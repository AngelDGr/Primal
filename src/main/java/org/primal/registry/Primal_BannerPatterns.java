package org.primal.registry;

import net.minecraft.core.registries.Registries;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.primal.Primal_Main;
import org.primal.Primal_Registries;

import java.util.List;

public class Primal_BannerPatterns {
    public static final ResourceKey<BannerPattern> PAW = create("paw");
    public static final ResourceKey<BannerPattern> JAWS = create("jaws");
    public static final ResourceKey<BannerPattern> MARSH = create("marsh");
    public static final ResourceKey<BannerPattern> EYRIE = create("eyrie");
    public static final ResourceKey<BannerPattern> SLITHER = create("slither");
    public static final ResourceKey<BannerPattern> ROYAL = create("royal");

    public static final List<ResourceKey<BannerPattern>> Banner_Patterns= List.of(PAW, JAWS, MARSH, EYRIE, SLITHER, ROYAL);

    public static void init() {
        register(PAW.location().getPath(), "pw");
        register(JAWS.location().getPath(), "jws");
        register(MARSH.location().getPath(), "msh");
        register(EYRIE.location().getPath(), "eyr");
        register(SLITHER.location().getPath(), "slh");
        register(ROYAL.location().getPath(), "ryl");
    }

    private static ResourceKey<BannerPattern> create(String name) {
        return ResourceKey.create(Registries.BANNER_PATTERN, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, name));
    }

    public static void register(String name, String hash){
        Primal_Registries.BANNER_PATTERNS.register(name, ()-> new BannerPattern(hash));
    }
}
