package org.primal.registry;

import org.primal.Primal_Registries;
import org.primal.entity.animal.BearEntity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.primal.entity.animal.SharkEntity;

public final class Primal_Entities {

    public static DeferredHolder<EntityType<?>, EntityType<BearEntity>> BEAR = Primal_Registries.ENTITIES.register(
            "bear",
            () -> EntityType.Builder.of(BearEntity::new, MobCategory.CREATURE)
                    //Hitbox
                    .sized(1.5f, 1.75f)
                    .eyeHeight(1.25f)
                    .clientTrackingRange(8)
                    .build("bear"));

    public static DeferredHolder<EntityType<?>, EntityType<SharkEntity>> SHARK = Primal_Registries.ENTITIES.register(
            "shark",
            () -> EntityType.Builder.of(SharkEntity::new, MobCategory.WATER_CREATURE)
                    //Hitbox
                    .sized(1.5f, 1.5f)
                    .clientTrackingRange(8)
                    .build("shark"));

    public static void init() {}
}
