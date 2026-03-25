package org.primal.registry;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class Primal_Food {
    public static final FoodProperties APPLE_FRITTER = new FoodProperties.Builder()
            .nutrition(8)
            .saturationMod(0.3F)
            .build();

    public static final FoodProperties GOLDEN_APPLE_FRITTER = new FoodProperties.Builder()
            .nutrition(8)
            .saturationMod(1.4F)
            .effect(()-> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 0), 1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.ABSORPTION, 2400, 0), 1.0F)
            .alwaysEat()
            .build();

    public static final FoodProperties ENCHANTED_GOLDEN_APPLE_FRITTER = new FoodProperties.Builder()
            .nutrition(8)
            .saturationMod(1.4F)
            .effect(()-> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1200, 1), 1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000, 0), 1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 6000, 0), 1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.ABSORPTION, 2400, 3), 1.0F)
            .alwaysEat()
            .build();

    public static final FoodProperties LITCHI = new FoodProperties.Builder()
            .nutrition(3)
            .saturationMod(0.5F)
            .effect(()-> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 0), 1.0F)
            .alwaysEat()
            .build();

    public static final FoodProperties KIWANO = new FoodProperties.Builder()
            .nutrition(4)
            .saturationMod(0.3F)
            .effect(()-> new MobEffectInstance(Primal_Effects.THORNED.get(), 600, 0), 1.0F)
            .alwaysEat()
            .build();

    public static final FoodProperties STARFRUIT = new FoodProperties.Builder()
            .nutrition(5)
            .saturationMod(0.8F)
            .effect(()-> new MobEffectInstance(MobEffects.DIG_SPEED, 600, 0), 1.0F)
            .alwaysEat()
            .build();

    public static final FoodProperties WEIRD_APPLE = new FoodProperties.Builder()
            .nutrition(20)
            .saturationMod(2.0F)
            .alwaysEat()
            .build();

    public static final FoodProperties VENISON = new FoodProperties.Builder()
            .nutrition(4)
            .saturationMod(0.6F)
            .build();

    public static final FoodProperties COOKED_VENISON = new FoodProperties.Builder()
            .nutrition(8)
            .saturationMod(1.2F)
            .build();
}
