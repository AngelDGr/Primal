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

        addSpecificSound(Primal_Sounds.REMOVE_EGG,
                sound( "minecraft:mob/chicken/plop").pitch(0.4)
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

        addSoundEntity(Primal_Sounds.SHARK_IDLE, 4);
        addSoundEntity(Primal_Sounds.SHARK_ATTACK, 3);
        addSoundEntity(Primal_Sounds.SHARK_HURT, 4);
        addSoundEntity(Primal_Sounds.SHARK_DEATH, 1);
        addSpecificSound(Primal_Sounds.SHARK_FLOP,
                "minecraft:entity/fish/flop1",
                "minecraft:entity/fish/flop2",
                "minecraft:entity/fish/flop3",
                "minecraft:entity/fish/flop4"
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

        addSoundEntity(Primal_Sounds.CASSOWARY_IDLE, 3);
        addSoundEntity(Primal_Sounds.CASSOWARY_LUNGE, 2);
        addSoundEntity(Primal_Sounds.CASSOWARY_HURT, 4);
        addSoundEntity(Primal_Sounds.CASSOWARY_DEATH, 1);
        addSpecificSound(Primal_Sounds.CASSOWARY_EAT,
                "minecraft:mob/parrot/eat1",
                "minecraft:mob/parrot/eat2",
                "minecraft:mob/parrot/eat3");
        addSpecificSound(Primal_Sounds.CASSOWARY_PLOP,
                "minecraft:mob/chicken/plop"
        );


        addSoundEntity(Primal_Sounds.WALRUS_IDLE, 5);
        addSoundEntity(Primal_Sounds.WALRUS_HURT, 3);
        addSoundEntity(Primal_Sounds.WALRUS_DEATH, 1);
        addSpecificSound(Primal_Sounds.WALRUS_EAT,
                sound("minecraft:mob/dolphin/eat1").pitch(0.5),
                sound("minecraft:mob/dolphin/eat2").pitch(0.5),
                sound("minecraft:mob/dolphin/eat3").pitch(0.5)
        );
        addSoundEntity(Primal_Sounds.WALRUS_STEP, 6);
        addSoundEntity(Primal_Sounds.WALRUS_WHISTLE, 3);
        addSoundEntity(Primal_Sounds.WALRUS_WHISTLE_RARE, 1);
        addSoundEntity(Primal_Sounds.WALRUS_SLAM, 2);
        addSoundEntity(Primal_Sounds.WALRUS_WHIRLWIND,2);

        addSoundEntity(Primal_Sounds.LION_IDLE, 3);
        addSoundEntity(Primal_Sounds.LION_IDLE_ANGRY, 3);
        addSoundEntity(Primal_Sounds.LION_HURT, 3);
        addSoundEntity(Primal_Sounds.LION_DEATH, 1);
        addSpecificSound(Primal_Sounds.LION_EAT,
                "minecraft:mob/sniffer/eat1",
                "minecraft:mob/sniffer/eat2",
                "minecraft:mob/sniffer/eat3"
        );
        addSoundEntity(Primal_Sounds.LION_ROAR, 2);
        addSoundEntity(Primal_Sounds.LION_SNORING, 4);

        addSoundEntity(Primal_Sounds.SNAKE_IDLE, 4);
        addSoundEntity(Primal_Sounds.SNAKE_HURT, 3);
        addSoundEntity(Primal_Sounds.SNAKE_DEATH, 1);
        addSpecificSound(Primal_Sounds.SNAKE_EATS,
                "minecraft:item/bottle/drink_honey1",
                "minecraft:item/bottle/drink_honey2"
        );
        addSpecificSound(Primal_Sounds.SNAKE_SNORING,
                sound("primal:entity/snake/snake_snoring1").attenuationDistance(12).volume(0.6f),
                sound("primal:entity/snake/snake_snoring2").attenuationDistance(12).volume(0.6f),
                sound("primal:entity/snake/snake_snoring3").attenuationDistance(12).volume(0.6f),
                sound("primal:entity/snake/snake_snoring4").attenuationDistance(12).volume(0.6f));
        addSpecificSound(Primal_Sounds.SNAKE_POP_IN,
                sound("primal:entity/snake/snake_pop_in1").attenuationDistance(14).pitch(0.7f),
                sound("primal:entity/snake/snake_pop_in1").attenuationDistance(14).pitch(0.8f));
        addSpecificSound(Primal_Sounds.SNAKE_POP_OUT,
                sound("primal:entity/snake/snake_pop_out1").attenuationDistance(14).pitch(1.0),
                sound("primal:entity/snake/snake_pop_out1").attenuationDistance(14).pitch(0.9),
                sound("primal:entity/snake/snake_pop_out1").attenuationDistance(14).pitch(1.1));
        addSpecificSound(Primal_Sounds.SNAKE_BREAKS_SKIN,
                sound("minecraft:block/sweet_berry_bush/break1").pitch(1.4),
                sound("minecraft:block/sweet_berry_bush/break2").pitch(1.4),
                sound("minecraft:block/sweet_berry_bush/break3").pitch(1.4),
                sound("minecraft:block/sweet_berry_bush/break4").pitch(1.4));

        addSoundEntity(Primal_Sounds.DEER_IDLE, 3);
        addSoundEntity(Primal_Sounds.DEER_HURT, 4);
        addSoundEntity(Primal_Sounds.DEER_DEATH, 1);
        addSpecificSound(Primal_Sounds.DEER_EAT,
                sound("minecraft:mob/panda/eat1"),
                sound("minecraft:mob/panda/eat2"),
                sound("minecraft:mob/panda/eat3"),
                sound("minecraft:mob/panda/eat4"),
                sound("minecraft:mob/panda/eat5"),
                sound("minecraft:mob/panda/eat6"));
        addSpecificSound(Primal_Sounds.DEER_RAM_IMPACT,
                sound("minecraft:mob/goat/impact1"),
                sound("minecraft:mob/goat/impact2"));
        addSpecificSound(Primal_Sounds.DEER_ANTLER_BREAK,
                sound("minecraft:mob/goat/horn_break1"),
                sound("minecraft:mob/goat/horn_break2"),
                sound("minecraft:mob/goat/horn_break3"),
                sound("minecraft:mob/goat/horn_break4"));

        addSpecificSound(Primal_Sounds.PET_BIND,
                "minecraft:block/conduit/activate"
        );
        addSoundItem(Primal_Sounds.CONCH_SHELL_CALL, 3);

        addSpecificSound(Primal_Sounds.EXPLOSEED_THROW,
                "minecraft:random/bow"
        );


        addSpecificSound(Primal_Sounds.CHOMP_TRAP_CLOSES,
                "primal:entity/crocodile/crocodile_attack1",
                "primal:entity/crocodile/crocodile_attack2",
                "primal:entity/crocodile/crocodile_attack3"
        );
        addSpecificSound(Primal_Sounds.CHOMP_TRAP_BREAKS,
                "minecraft:random/break");
        addSpecificSound(Primal_Sounds.CHOMP_TRAP_REPAIRS,
                "minecraft:block/smithing_table/smithing_table1",
                "minecraft:block/smithing_table/smithing_table2",
                "minecraft:block/smithing_table/smithing_table3");

        addSpecificSound(Primal_Sounds.HOLLOW_LOG_OFFSPRING,
                sound("minecraft:mob/chicken/plop").pitch(0.6f),
                sound("minecraft:mob/chicken/plop").pitch(0.7f),
                sound("minecraft:mob/chicken/plop").pitch(0.8f)
        );

        addSpecificSound(Primal_Sounds.DREAMCATCHER_ATTACK_TARGET,
                sound("minecraft:block/conduit/attack1"),
                sound("minecraft:block/conduit/attack2"),
                sound("minecraft:block/conduit/attack3")
        );

        addWalrusSong(Primal_Sounds.OIIA_OIIA);
        addWalrusSong(Primal_Sounds.MEGALOVANIA);
        addWalrusSong(Primal_Sounds.RICKROLL);
        addWalrusSong(Primal_Sounds.REVENGE);
        addWalrusSong(Primal_Sounds.CRAB);
        addWalrusSong(Primal_Sounds.DRIFTVEIL_CITY);
        addWalrusSong(Primal_Sounds.FUR_ELISE);
        addWalrusSong(Primal_Sounds.RASPUTIN);
        addWalrusSong(Primal_Sounds.SARIA);
        addWalrusSong(Primal_Sounds.TES);
        addWalrusSong(Primal_Sounds.TLOZ);

        addMusicDisc(Primal_Sounds.OH_DEER);
    }

    private void addSpecificSound(final DeferredHolder<SoundEvent, SoundEvent> sound, SoundDefinition.Sound... soundData) {
        final SoundDefinition definition = definition().subtitle("subtitles." + Primal_Main.MOD_ID + "." + sound.getId().getPath());

        for (SoundDefinition.Sound soundDatum : soundData) {
            definition.with(soundDatum);
        }

        this.add(sound.get(), definition);
    }

    private void addSpecificSound(final DeferredHolder<SoundEvent, SoundEvent> sound, final String... sounds) {
        final SoundDefinition definition = definition().subtitle("subtitles." + Primal_Main.MOD_ID + "." + sound.getId().getPath());

        for(final String name: sounds){
            definition.with(sound(name));
        }

        this.add(sound.get(), definition);
    }

    @SuppressWarnings("all")
    private void addSoundItem(final DeferredHolder<SoundEvent, SoundEvent> sound, final int soundVariationAmount) {
        final SoundDefinition definition = definition().subtitle("subtitles." + Primal_Main.MOD_ID + "." + sound.getId().getPath());

        String[] soundPathSplitted= sound.getId().getPath().split("\\.");

        String mobType  = soundPathSplitted[1];
        String soundType= soundPathSplitted[2];

        String finalPath=soundType;

        for (int i = 1; i <= soundVariationAmount; i++)
            definition.with(sound(Primal_Main.MOD_ID + ":item/" + mobType + "/" + finalPath + i));

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

    private void addWalrusSong(final DeferredHolder<SoundEvent, SoundEvent> sound) {
        final SoundDefinition definition = definition().subtitle("subtitles.primal.entity.walrus.song");

        String[] soundPathSplitted= sound.getId().getPath().split("\\.");

        String songName= soundPathSplitted[2];

        definition.with(sound(Primal_Main.MOD_ID + ":records/walrus/" + songName));

        this.add(sound.get(), definition);
    }

    @SuppressWarnings("all")
    private void addMusicDisc(final DeferredHolder<SoundEvent, SoundEvent> sound) {
        final SoundDefinition definition = definition();

        String[] soundPathSplitted= sound.getId().getPath().split("\\.");

        String songName= soundPathSplitted[1];

        definition.with(sound(Primal_Main.MOD_ID + ":records/" + songName).stream());

        this.add(sound.get(), definition);
    }
}
