package org.primal.sounds;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import org.primal.registry.Primal_Sounds;

public enum WalrusSong {
    OIIA_OIIA(Primal_Sounds.OIIA_OIIA, 36, 0),
    MEGALOVANIA(Primal_Sounds.MEGALOVANIA, 50, 1),
    RICKROLL(Primal_Sounds.RICKROLL, 82, 2),
    REVENGE(Primal_Sounds.REVENGE, 163, 3),
    CRAB(Primal_Sounds.CRAB, 63, 4),
    DRIFTVEIL_CITY(Primal_Sounds.DRIFTVEIL_CITY, 86, 5),
    FUR_ELISE(Primal_Sounds.FUR_ELISE, 50, 6),
    RASPUTIN(Primal_Sounds.RASPUTIN, 120, 7),
    SARIA(Primal_Sounds.SARIA, 100, 8),
    TES(Primal_Sounds.TES, 134, 9),
    TLOZ(Primal_Sounds.TLOZ, 185, 10);

    public final Holder<SoundEvent> soundEvent;
    public final int lengthInSeconds;
    public final int id;
    WalrusSong(Holder<SoundEvent> soundEvent, int lengthInSeconds, int id){
        this.soundEvent=soundEvent;
        this.lengthInSeconds=lengthInSeconds;
        this.id=id;
    }

    public static WalrusSong byId(int id) {
        for (WalrusSong song : values()) {
            if (song.id == id) {
                return song;
            }
        }
        return null; // or throw
    }
}
