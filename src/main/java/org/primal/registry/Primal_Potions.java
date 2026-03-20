package org.primal.registry;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.primal.Primal_Registries;

import java.util.function.Supplier;

public class Primal_Potions {

    public static DeferredHolder<Potion, Potion> HEAVINESS;

    public static DeferredHolder<Potion, Potion> THORNED;
    public static DeferredHolder<Potion, Potion> STRONG_THORNED;
    public static DeferredHolder<Potion, Potion> LONG_THORNED;
    public static void init(){

        HEAVINESS = register("heaviness", ()-> new Potion(new MobEffectInstance(Primal_Effects.HEAVINESS, 3600)));

        THORNED = register("thorned", ()-> new Potion(new MobEffectInstance(Primal_Effects.THORNED, 3600)));
        STRONG_THORNED = register("strong_thorned", ()-> new Potion(new MobEffectInstance(Primal_Effects.THORNED, 1800, 1)));
        LONG_THORNED = register("long_thorned", ()-> new Potion(new MobEffectInstance(Primal_Effects.THORNED, 9600)));
    }

    private static DeferredHolder<Potion, Potion> register(final String id, final Supplier<Potion> statusEffect) {
        return Primal_Registries.POTIONS.register(id, statusEffect);
    }
}
