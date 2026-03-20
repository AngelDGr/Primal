package org.primal.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import org.primal.Primal_Main;

public class Primal_EntityAttributes {

    public static final Holder<Attribute> REFLECTED_DAMAGE = register(
            "reflected_damage",
            new RangedAttribute("attribute.variant.primal.reflected_damage", 1.0, 1.0, 10.0).setSyncable(true)
    );

    @SuppressWarnings("all")
    private static Holder<Attribute> register(String id, Attribute attribute) {
        return Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID,id), attribute);
    }
}
