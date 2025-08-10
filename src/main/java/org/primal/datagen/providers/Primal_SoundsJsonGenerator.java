package org.primal.datagen.providers;

import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.primal.Primal_Main;

import java.util.List;

public class Primal_SoundsJsonGenerator extends SoundDefinitionsProvider {

    public Primal_SoundsJsonGenerator(final PackOutput output, final ExistingFileHelper helper) {
        super(output, Primal_Main.MOD_ID, helper);
    }

    @Override
    public void registerSounds() {

    }

    private void addSpecificSound(final DeferredHolder<SoundEvent, SoundEvent> sound, final List<String> names) {
        final SoundDefinition definition = definition().subtitle("subtitles." + Primal_Main.MOD_ID + "." + sound.getId().getPath());

        for(final String name: names){
            definition.with(sound(name));
        }

        this.add(sound.get(), definition);
    }

    private void addSoundEntity(final DeferredHolder<SoundEvent, SoundEvent> sound, final int soundVariationAmount, final String subfolder) {
        addSound(sound, soundVariationAmount, "entity/", subfolder);
    }

    private void addSound(final DeferredHolder<SoundEvent, SoundEvent> sound, final int soundVariationAmount, final String type, final String subfolder) {
        final SoundDefinition definition = definition().subtitle("subtitles." + Primal_Main.MOD_ID + "." + sound.getId().getPath());

        for (int i = 1; i <= soundVariationAmount; i++)
            definition.with(sound(Primal_Main.MOD_ID + ":" + type + subfolder + "/" + sound.getId().getPath() + i));

        this.add(sound.get(), definition);
    }
}
