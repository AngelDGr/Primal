package org.primal.registry;

import net.minecraft.world.entity.schedule.Activity;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Registries;

public class Primal_Activities {

    public static final DeferredHolder<Activity, Activity> SIT = register("sit");
    public static final DeferredHolder<Activity, Activity> FOLLOW = register("follow");
    public static final DeferredHolder<Activity, Activity> JOCKEY = register("jockey");

    public static final DeferredHolder<Activity, Activity> BEACHED = register("beached");

    public static final DeferredHolder<Activity, Activity> THRASH = register("thrash");

    public static final DeferredHolder<Activity, Activity> SNATCH = register("snatch");
    public static final DeferredHolder<Activity, Activity> NESTED = register("nested");


    public static void init() {}

    private static @NotNull DeferredHolder<Activity, Activity> register(String key) {
        return Primal_Registries.ACTIVITIES.register(key, ()->new Activity(key));
    }
}
