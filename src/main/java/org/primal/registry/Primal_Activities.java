package org.primal.registry;

import net.minecraft.world.entity.schedule.Activity;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Registries;

public class Primal_Activities {

    public static final RegistryObject<Activity> SIT = register("primal_sit");
    public static final RegistryObject<Activity> FOLLOW = register("primal_follow");
    public static final RegistryObject<Activity> JOCKEY = register("primal_jockey");

    public static final RegistryObject<Activity> BEACHED = register("primal_beached");

    public static final RegistryObject<Activity> EXPLODING = register("primal_exploding");

    public static final RegistryObject<Activity> NESTED = register("primal_nested");

    public static final RegistryObject<Activity> PLAY = register("primal_play");
    public static final RegistryObject<Activity> ESCAPE = register("primal_escape");

    public static final RegistryObject<Activity> STALK = register("primal_stalk");
    public static final RegistryObject<Activity> CAUTIOUS = register("primal_cautious");

    public static final RegistryObject<Activity> GRAB = register("primal_grab");

    public static final RegistryObject<Activity> DANCING = register("primal_dancing");
    public static void init() {}

    private static @NotNull RegistryObject<Activity> register(String key) {
        return Primal_Registries.ACTIVITIES.register(key, ()->new Activity(key));
    }
}
