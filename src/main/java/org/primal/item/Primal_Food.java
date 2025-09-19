package org.primal.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class Primal_Food {
    public static final FoodProperties APPLE_FRITTER = new FoodProperties.Builder().nutrition(8).saturationModifier(0.3F).build();

    public static final FoodProperties GOLDEN_APPLE_FRITTER = new FoodProperties.Builder()
            .nutrition(8)
            .saturationModifier(1.4F)
            .effect(()-> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 0), 1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.ABSORPTION, 2400, 0), 1.0F)
            .alwaysEdible()
            .build();

    public static final FoodProperties ENCHANTED_GOLDEN_APPLE_FRITTER = new FoodProperties.Builder()
            .nutrition(8)
            .saturationModifier(1.4F)
            .effect(()-> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1200, 1), 1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000, 0), 1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 6000, 0), 1.0F)
            .effect(()-> new MobEffectInstance(MobEffects.ABSORPTION, 2400, 3), 1.0F)
            .alwaysEdible()
            .build();
}
