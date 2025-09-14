package org.primal.datagen.providers;

import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.primal.Primal_Main;
import org.primal.registry.Primal_Sounds;

import java.util.List;

public class Primal_SoundsJsonGenerator extends SoundDefinitionsProvider {

    public Primal_SoundsJsonGenerator(final PackOutput output, final ExistingFileHelper helper) {
        super(output, Primal_Main.MOD_ID, helper);
    }

    @Override
    public void registerSounds() {

        addSpecificSound(Primal_Sounds.ANIMAL_CHEST, List.of(
                "minecraft:mob/chicken/plop"
        ));

        addSpecificSound(Primal_Sounds.PLACES_EGG, List.of(
                "minecraft:mob/chicken/plop"
        ));

        addSpecificSound(Primal_Sounds.LAYS_EGG, List.of(
                "minecraft:mob/turtle/egg/drop_egg1",
                "minecraft:mob/turtle/egg/drop_egg2"
        ));

        addSpecificSound(Primal_Sounds.ZOMBIE_DESTROY_GENERIC_EGG, List.of(
                "minecraft:mob/turtle/egg/jump_egg1",
                "minecraft:mob/turtle/egg/jump_egg2",
                "minecraft:mob/turtle/egg/jump_egg3",
                "minecraft:mob/turtle/egg/jump_egg4"
        ));

        addSpecificSound(Primal_Sounds.EGG_BREAK, List.of(
                "minecraft:mob/turtle/egg/egg_break1",
                "minecraft:mob/turtle/egg/egg_break2"
        ));

        addSpecificSound(Primal_Sounds.EGG_HATCH, List.of(
                "minecraft:mob/turtle/baby/egg_hatched1",
                "minecraft:mob/turtle/baby/egg_hatched2",
                "minecraft:mob/turtle/baby/egg_hatched3"
        ));

        addSpecificSound(Primal_Sounds.EGG_CRACK, List.of(
                "minecraft:mob/turtle/egg/egg_crack1",
                "minecraft:mob/turtle/egg/egg_crack2",
                "minecraft:mob/turtle/egg/egg_crack3",
                "minecraft:mob/turtle/egg/egg_crack4",
                "minecraft:mob/turtle/egg/egg_crack5"
        ));

        addSpecificSound(Primal_Sounds.CROCODILE_CLOCK, List.of(
                "minecraft:note/hat"
        ));

        addSoundEntity(Primal_Sounds.CROCODILE_IDLE, 5, "crocodile");
        addSoundEntity(Primal_Sounds.CROCODILE_HURT, 3, "crocodile");
        addSoundEntity(Primal_Sounds.CROCODILE_ATTACK, 3, "crocodile");
        addSoundEntity(Primal_Sounds.CROCODILE_DEATH, 2, "crocodile");
        addSoundEntity(Primal_Sounds.CROCODILE_EAT, 3, "crocodile");
        addSoundEntity(Primal_Sounds.CROCODILE_VOMIT, 4, "crocodile");

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

        String[] soundPathSplitted= sound.getId().getPath().split("\\.");

        String mobType  = soundPathSplitted[1];
        String soundType= soundPathSplitted[2];

        String finalPath=mobType+"_"+soundType;

        for (int i = 1; i <= soundVariationAmount; i++)
            definition.with(sound(Primal_Main.MOD_ID + ":" + type + subfolder + "/" + finalPath + i));

        this.add(sound.get(), definition);
    }
}
