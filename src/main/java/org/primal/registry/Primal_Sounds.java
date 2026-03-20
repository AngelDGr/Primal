package org.primal.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.primal.Primal_Main;
import org.primal.Primal_Registries;

public class Primal_Sounds {
    public static final DeferredHolder<SoundEvent, SoundEvent> ANIMAL_CHEST = register("entity.generic.chest");

    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_DESTROY_GENERIC_EGG = register("entity.zombie.destroy_generic_egg");
    public static final DeferredHolder<SoundEvent, SoundEvent> EGG_BREAK = register("entity.generic.egg_break");
    public static final DeferredHolder<SoundEvent, SoundEvent> EGG_CRACK = register("entity.generic.egg_crack");
    public static final DeferredHolder<SoundEvent, SoundEvent> EGG_HATCH = register("entity.generic.egg_hatch");

    public static final DeferredHolder<SoundEvent, SoundEvent> LAYS_EGG = register("entity.generic.lays_egg");

    //Bear
    public static final DeferredHolder<SoundEvent, SoundEvent> BEAR_IDLE = register("entity.bear.idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> BEAR_IDLE_ANGRY = register("entity.bear.idle_angry");
    public static final DeferredHolder<SoundEvent, SoundEvent> BEAR_HURT = register("entity.bear.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> BEAR_DEATH = register("entity.bear.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> BEAR_ROAR = register("entity.bear.roar");
    public static final DeferredHolder<SoundEvent, SoundEvent> BEAR_SNORING = register("entity.bear.snoring");
    public static final DeferredHolder<SoundEvent, SoundEvent> BEAR_WAKE_UP = register("entity.bear.wake_up");
    public static final DeferredHolder<SoundEvent, SoundEvent> BEAR_EAT = register("entity.bear.eat");

    //Shark
    public static final DeferredHolder<SoundEvent, SoundEvent> SHARK_IDLE = register("entity.shark.idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> SHARK_ATTACK = register("entity.shark.attack");
    public static final DeferredHolder<SoundEvent, SoundEvent> SHARK_HURT = register("entity.shark.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> SHARK_DEATH = register("entity.shark.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> SHARK_FLOP = register("entity.shark.flop");

    //Crocodile
    public static final DeferredHolder<SoundEvent, SoundEvent> CROCODILE_IDLE = register("entity.crocodile.idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> CROCODILE_ATTACK = register("entity.crocodile.attack");
    public static final DeferredHolder<SoundEvent, SoundEvent> CROCODILE_HURT = register("entity.crocodile.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> CROCODILE_DEATH = register("entity.crocodile.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> CROCODILE_EAT = register("entity.crocodile.eat");
    public static final DeferredHolder<SoundEvent, SoundEvent> CROCODILE_VOMIT = register("entity.crocodile.vomit");
    public static final DeferredHolder<SoundEvent, SoundEvent> CROCODILE_CLOCK = register("entity.crocodile.clock");
    public static final DeferredHolder<SoundEvent, SoundEvent> CROCODILE_THRASH = register("entity.crocodile.thrash");
    public static final DeferredHolder<SoundEvent, SoundEvent> CROCODILE_SPLASHES = register("entity.crocodile.splashes");

    //Eagle
    public static final DeferredHolder<SoundEvent, SoundEvent> EAGLE_IDLE = register("entity.eagle.idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> EAGLE_FLAP = register("entity.eagle.flap");
    public static final DeferredHolder<SoundEvent, SoundEvent> EAGLE_HURT = register("entity.eagle.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> EAGLE_DEATH = register("entity.eagle.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> EAGLE_EAT = register("entity.eagle.eat");
    public static final DeferredHolder<SoundEvent, SoundEvent> EAGLE_SHRIEK = register("entity.eagle.shriek");
    public static final DeferredHolder<SoundEvent, SoundEvent> EAGLE_FREEDOM = register("entity.eagle.freedom");

    //Cassowary
    public static final DeferredHolder<SoundEvent, SoundEvent> CASSOWARY_IDLE = register("entity.cassowary.idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> CASSOWARY_LUNGE = register("entity.cassowary.lunge");
    public static final DeferredHolder<SoundEvent, SoundEvent> CASSOWARY_HURT = register("entity.cassowary.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> CASSOWARY_DEATH = register("entity.cassowary.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> CASSOWARY_EAT = register("entity.cassowary.eat");
    public static final DeferredHolder<SoundEvent, SoundEvent> CASSOWARY_PLOP = register("entity.cassowary.plop");

    //Walrus
    public static final DeferredHolder<SoundEvent, SoundEvent> WALRUS_IDLE = register("entity.walrus.idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> WALRUS_HURT = register("entity.walrus.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> WALRUS_DEATH = register("entity.walrus.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> WALRUS_EAT = register("entity.walrus.eat");
    public static final DeferredHolder<SoundEvent, SoundEvent> WALRUS_STEP = register("entity.walrus.step");
    public static final DeferredHolder<SoundEvent, SoundEvent> WALRUS_WHISTLE = register("entity.walrus.whistle");
    public static final DeferredHolder<SoundEvent, SoundEvent> WALRUS_WHISTLE_RARE = register("entity.walrus.whistle_rare");
    public static final DeferredHolder<SoundEvent, SoundEvent> WALRUS_SLAM = register("entity.walrus.slam");
    public static final DeferredHolder<SoundEvent, SoundEvent> WALRUS_WHIRLWIND = register("entity.walrus.whirlwind");

    //Lion
    public static final DeferredHolder<SoundEvent, SoundEvent> LION_IDLE = register("entity.lion.idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> LION_IDLE_ANGRY = register("entity.lion.idle_angry");
    public static final DeferredHolder<SoundEvent, SoundEvent> LION_HURT = register("entity.lion.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> LION_DEATH = register("entity.lion.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> LION_EAT = register("entity.lion.eat");
    public static final DeferredHolder<SoundEvent, SoundEvent> LION_ROAR = register("entity.lion.roar");
    public static final DeferredHolder<SoundEvent, SoundEvent> LION_SNORING = register("entity.lion.snoring");

    //Snake
    public static final DeferredHolder<SoundEvent, SoundEvent> SNAKE_IDLE = register("entity.snake.idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> SNAKE_HURT = register("entity.snake.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> SNAKE_DEATH = register("entity.snake.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> SNAKE_EATS = register("entity.snake.gulp");
    public static final DeferredHolder<SoundEvent, SoundEvent> SNAKE_SNORING = register("entity.snake.snoring");
    public static final DeferredHolder<SoundEvent, SoundEvent> SNAKE_POP_IN = register("entity.snake.pop_in");
    public static final DeferredHolder<SoundEvent, SoundEvent> SNAKE_POP_OUT = register("entity.snake.pop_out");
    public static final DeferredHolder<SoundEvent, SoundEvent> SNAKE_BREAKS_SKIN = register("entity.snake.break_skin");

    //Deer
    public static final DeferredHolder<SoundEvent, SoundEvent> DEER_IDLE = register("entity.deer.idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> DEER_HURT = register("entity.deer.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> DEER_DEATH = register("entity.deer.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> DEER_EAT = register("entity.deer.eat");
    public static final DeferredHolder<SoundEvent, SoundEvent> DEER_RAM_IMPACT = register("entity.deer.ram_impact");
    public static final DeferredHolder<SoundEvent, SoundEvent> DEER_ANTLER_BREAK = register("entity.deer.antler_break");

    //Item
    public static final DeferredHolder<SoundEvent, SoundEvent> PET_BIND = register("item.conch_shell.bind");
    public static final DeferredHolder<SoundEvent, SoundEvent> CONCH_SHELL_CALL = register("item.conch_shell.call");
    public static final DeferredHolder<SoundEvent, SoundEvent> EXPLOSEED_THROW = register("item.exploseed.throw");

    //Block
    public static final DeferredHolder<SoundEvent, SoundEvent> CHOMP_TRAP_CLOSES = register("block.chomp_trap.close");
    public static final DeferredHolder<SoundEvent, SoundEvent> CHOMP_TRAP_BREAKS = register("block.chomp_trap.break");
    public static final DeferredHolder<SoundEvent, SoundEvent> CHOMP_TRAP_REPAIRS = register("block.chomp_trap.repair");

    public static final DeferredHolder<SoundEvent, SoundEvent> HOLLOW_LOG_OFFSPRING = register("block.hollow_log.offspring");

    public static final DeferredHolder<SoundEvent, SoundEvent> PLACES_EGG = register("block.nest.places_egg");
    public static final DeferredHolder<SoundEvent, SoundEvent> REMOVE_EGG = register("block.nest.removes_egg");

    public static final DeferredHolder<SoundEvent, SoundEvent> DREAMCATCHER_ATTACK_TARGET = register("block.dreamcatcher.attack");

    //Walrus Song
    public static final DeferredHolder<SoundEvent, SoundEvent> OIIA_OIIA = register("walrus.song.oiia_oiia");
    public static final DeferredHolder<SoundEvent, SoundEvent> MEGALOVANIA = register("walrus.song.megalovania");
    public static final DeferredHolder<SoundEvent, SoundEvent> RICKROLL = register("walrus.song.rickroll");
    public static final DeferredHolder<SoundEvent, SoundEvent> REVENGE = register("walrus.song.revenge");
    public static final DeferredHolder<SoundEvent, SoundEvent> CRAB = register("walrus.song.crab");
    public static final DeferredHolder<SoundEvent, SoundEvent> DRIFTVEIL_CITY = register("walrus.song.driftveil_city");
    public static final DeferredHolder<SoundEvent, SoundEvent> FUR_ELISE = register("walrus.song.fur_elise");
    public static final DeferredHolder<SoundEvent, SoundEvent> RASPUTIN = register("walrus.song.rasputin");
    public static final DeferredHolder<SoundEvent, SoundEvent> SARIA = register("walrus.song.saria");
    public static final DeferredHolder<SoundEvent, SoundEvent> TES = register("walrus.song.tes");
    public static final DeferredHolder<SoundEvent, SoundEvent> TLOZ = register("walrus.song.tloz");

    public static final DeferredHolder<SoundEvent, SoundEvent> OH_DEER = register("music_disc.oh_deer");

    public static void init(){}

    public static DeferredHolder<SoundEvent, SoundEvent> register(final String name) {
        return Primal_Registries.SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, name)));
    }
}
