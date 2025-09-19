package org.primal.datagen.providers;

import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.primal.Primal_Main;
import org.primal.registry.Primal_Sounds;

public class Primal_SoundsJsonGenerator extends SoundDefinitionsProvider {

    public Primal_SoundsJsonGenerator(final PackOutput output, final ExistingFileHelper helper) {
        super(output, Primal_Main.MOD_ID, helper);
    }

    @Override
    public void registerSounds() {

        addSpecificSound(Primal_Sounds.ANIMAL_CHEST,
                "minecraft:mob/chicken/plop"
        );

        addSpecificSound(Primal_Sounds.PLACES_EGG,
                "minecraft:mob/chicken/plop"
        );

        addSpecificSound(Primal_Sounds.LAYS_EGG,
                "minecraft:mob/turtle/egg/drop_egg1",
                "minecraft:mob/turtle/egg/drop_egg2"
        );

        addSpecificSound(Primal_Sounds.ZOMBIE_DESTROY_GENERIC_EGG,
                "minecraft:mob/turtle/egg/jump_egg1",
                "minecraft:mob/turtle/egg/jump_egg2",
                "minecraft:mob/turtle/egg/jump_egg3",
                "minecraft:mob/turtle/egg/jump_egg4"
        );

        addSpecificSound(Primal_Sounds.EGG_BREAK,
                "minecraft:mob/turtle/egg/egg_break1",
                "minecraft:mob/turtle/egg/egg_break2"
        );

        addSpecificSound(Primal_Sounds.EGG_HATCH,
                "minecraft:mob/turtle/baby/egg_hatched1",
                "minecraft:mob/turtle/baby/egg_hatched2",
                "minecraft:mob/turtle/baby/egg_hatched3"
        );

        addSpecificSound(Primal_Sounds.EGG_CRACK,
                "minecraft:mob/turtle/egg/egg_crack1",
                "minecraft:mob/turtle/egg/egg_crack2",
                "minecraft:mob/turtle/egg/egg_crack3",
                "minecraft:mob/turtle/egg/egg_crack4",
                "minecraft:mob/turtle/egg/egg_crack5"
        );

        addSoundEntity(Primal_Sounds.BEAR_IDLE, 5);
        addSoundEntity(Primal_Sounds.BEAR_IDLE_ANGRY, 4);
        addSoundEntity(Primal_Sounds.BEAR_HURT, 4);
        addSoundEntity(Primal_Sounds.BEAR_DEATH, 4);
        addSoundEntity(Primal_Sounds.BEAR_ROAR, 1);
        addSoundEntity(Primal_Sounds.BEAR_SNORING, 1);
        addSoundEntity(Primal_Sounds.BEAR_WAKE_UP, 1);
        addSpecificSound(Primal_Sounds.BEAR_EAT,
                "minecraft:mob/sniffer/eat1",
                "minecraft:mob/sniffer/eat2",
                "minecraft:mob/sniffer/eat3"
        );

        addSoundEntity(Primal_Sounds.CROCODILE_IDLE, 5);
        addSoundEntity(Primal_Sounds.CROCODILE_HURT, 3);
        addSoundEntity(Primal_Sounds.CROCODILE_ATTACK, 3);
        addSoundEntity(Primal_Sounds.CROCODILE_DEATH, 2);
        addSoundEntity(Primal_Sounds.CROCODILE_EAT, 3);
        addSoundEntity(Primal_Sounds.CROCODILE_VOMIT, 4);
        addSoundEntity(Primal_Sounds.CROCODILE_THRASH, 1);
        addSoundEntity(Primal_Sounds.CROCODILE_SPLASHES, 1);
        addSpecificSound(Primal_Sounds.CROCODILE_CLOCK,
                "minecraft:note/hat"
        );

        addSoundEntity(Primal_Sounds.EAGLE_IDLE, 5);
        addSoundEntity(Primal_Sounds.EAGLE_HURT, 4);
        addSoundEntity(Primal_Sounds.EAGLE_FLAP, 3);
        addSoundEntity(Primal_Sounds.EAGLE_DEATH, 1);
        addSpecificSound(Primal_Sounds.EAGLE_EAT,
                "minecraft:mob/parrot/eat1",
                "minecraft:mob/parrot/eat2",
                "minecraft:mob/parrot/eat3");
        addSoundEntity(Primal_Sounds.EAGLE_SHRIEK, 2);
        addSoundEntity(Primal_Sounds.EAGLE_FREEDOM, 1);
    }

    private void addSpecificSound(final DeferredHolder<SoundEvent, SoundEvent> sound, final String... sounds) {
        final SoundDefinition definition = definition().subtitle("subtitles." + Primal_Main.MOD_ID + "." + sound.getId().getPath());

        for(final String name: sounds){
            definition.with(sound(name));
        }

        this.add(sound.get(), definition);
    }

    private void addSoundEntity(final DeferredHolder<SoundEvent, SoundEvent> sound, final int soundVariationAmount) {
        final SoundDefinition definition = definition().subtitle("subtitles." + Primal_Main.MOD_ID + "." + sound.getId().getPath());

        String[] soundPathSplitted= sound.getId().getPath().split("\\.");

        String mobType  = soundPathSplitted[1];
        String soundType= soundPathSplitted[2];

        String finalPath=mobType+"_"+soundType;

        for (int i = 1; i <= soundVariationAmount; i++)
            definition.with(sound(Primal_Main.MOD_ID + ":entity/" + mobType + "/" + finalPath + i));

        this.add(sound.get(), definition);
    }
}
