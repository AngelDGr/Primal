package org.primal.registry;

import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.JukeboxSong;
import org.primal.Primal_Main;

public class Primal_JukeboxSongs {

    public static ResourceKey<JukeboxSong> OH_DEER = create("oh_deer");

    public static void bootstrap(BootstrapContext<JukeboxSong> context){
        context.register(
                OH_DEER,
                new JukeboxSong(
                        Primal_Sounds.OH_DEER,
                        Component.translatable(Util.makeDescriptionId("jukebox_song", OH_DEER.location())),
                        (float)175,
                        10)
        );
    }

    private static ResourceKey<JukeboxSong> create(String name) {
        return ResourceKey.create(Registries.JUKEBOX_SONG, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, name));
    }
}