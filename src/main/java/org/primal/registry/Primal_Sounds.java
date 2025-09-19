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

    public static final DeferredHolder<SoundEvent, SoundEvent> PLACES_EGG = register("entity.generic.places_egg");
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

    public static void init(){}

    public static DeferredHolder<SoundEvent, SoundEvent> register(final String name) {
        return Primal_Registries.SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, name)));
    }
}
