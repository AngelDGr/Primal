package org.primal.registry;

import org.primal.Primal_Registries;
import org.primal.entity.animal.BearEntity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;

public final class Primal_Entities {

    public static DeferredHolder<EntityType<?>, EntityType<BearEntity>> BEAR = Primal_Registries.ENTITIES.register(
            "bear",
            () -> EntityType.Builder.of(BearEntity::new, MobCategory.CREATURE)
                    //Hitbox
                    .sized(1.5f, 1.75f)
                    .clientTrackingRange(8)
                    .build("bear"));

    public static void init() {}
}
