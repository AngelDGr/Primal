package org.primal.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.primal.Primal_Main;
import org.primal.Primal_Registries;
import org.primal.effects.HeavyEffect;

import java.util.function.Supplier;

public class Primal_Effects {

    public static DeferredHolder<MobEffect, MobEffect> HEAVINESS;
    public static void init(){

        HEAVINESS = register("heaviness", ()-> new HeavyEffect(MobEffectCategory.HARMFUL, 0x565567)
                .addAttributeModifier(
                        Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "heaviness_speed"),
                        -0.25,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE)

                .addAttributeModifier(
                Attributes.GRAVITY, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "heaviness_gravity"),
                1.5,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    }

    @SuppressWarnings("unused")
    private static ResourceLocation id(final String name){
        return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, name);
    }

    private static DeferredHolder<MobEffect, MobEffect> register(final String id, final Supplier<MobEffect> statusEffect) {
        return Primal_Registries.MOB_EFFECTS.register(id, statusEffect);
    }
}
