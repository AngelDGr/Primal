package org.primal.registry;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.RegistryObject;
import org.primal.Primal_Registries;

import java.util.function.Supplier;

public class Primal_EntityAttributes {

    public static final RegistryObject<Attribute> REFLECTED_DAMAGE = register(
            "reflected_damage",
            ()-> new RangedAttribute("attribute.variant.primal.reflected_damage", 1.0, 1.0, 10.0).setSyncable(true)
    );

    public static void init(){}

    @SuppressWarnings("all")
    private static RegistryObject<Attribute> register(String id, Supplier<Attribute> attribute) {
        return Primal_Registries.ATTRIBUTES.register(id, attribute);
    }
}
