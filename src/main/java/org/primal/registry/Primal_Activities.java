package org.primal.registry;

import net.minecraft.world.entity.schedule.Activity;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Registries;

public class Primal_Activities {

    public static final DeferredHolder<Activity, Activity> SIT = register("primal_sit");
    public static final DeferredHolder<Activity, Activity> FOLLOW = register("primal_follow");
    public static final DeferredHolder<Activity, Activity> JOCKEY = register("primal_jockey");

    public static final DeferredHolder<Activity, Activity> BEACHED = register("primal_beached");

    public static final DeferredHolder<Activity, Activity> EXPLODING = register("primal_exploding");

    public static final DeferredHolder<Activity, Activity> NESTED = register("primal_nested");

    public static final DeferredHolder<Activity, Activity> PLAY = register("primal_play");
    public static final DeferredHolder<Activity, Activity> ESCAPE = register("primal_escape");

    public static final DeferredHolder<Activity, Activity> STALK = register("primal_stalk");
    public static final DeferredHolder<Activity, Activity> CAUTIOUS = register("primal_cautious");

    public static final DeferredHolder<Activity, Activity> GRAB = register("primal_grab");

    public static final DeferredHolder<Activity, Activity> DANCING = register("primal_dancing");
    public static void init() {}

    private static @NotNull DeferredHolder<Activity, Activity> register(String key) {
        return Primal_Registries.ACTIVITIES.register(key, ()->new Activity(key));
    }
}
