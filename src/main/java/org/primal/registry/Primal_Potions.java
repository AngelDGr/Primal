package org.primal.registry;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.RegistryObject;
import org.primal.Primal_Registries;

import java.util.function.Supplier;

public class Primal_Potions {

//    public static RegistryObject<Potion> HEAVINESS;

    public static RegistryObject<Potion> THORNED;
    public static RegistryObject<Potion> STRONG_THORNED;
    public static RegistryObject<Potion> LONG_THORNED;
    public static void init(){

//        HEAVINESS = register("heaviness", ()-> new Potion(new MobEffectInstance(Primal_Effects.HEAVINESS, 3600)));

        THORNED = register("thorned", ()-> new Potion(new MobEffectInstance(Primal_Effects.THORNED.get(), 3600)));
        STRONG_THORNED = register("strong_thorned", ()-> new Potion(new MobEffectInstance(Primal_Effects.THORNED.get(), 1800, 1)));
        LONG_THORNED = register("long_thorned", ()-> new Potion(new MobEffectInstance(Primal_Effects.THORNED.get(), 9600)));
    }

    private static RegistryObject<Potion> register(final String id, final Supplier<Potion> statusEffect) {
        return Primal_Registries.POTIONS.register(id, statusEffect);
    }
}
