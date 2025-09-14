package org.primal.registry;

import org.primal.Primal_Registries;
import org.primal.entity.animal.BearEntity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.primal.entity.animal.CrocodileEntity;
import org.primal.entity.animal.EagleEntity;
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
                    .eyeHeight(0.75f)
                    .clientTrackingRange(8)
                    .build("shark"));

    public static DeferredHolder<EntityType<?>, EntityType<CrocodileEntity>> CROCODILE = Primal_Registries.ENTITIES.register(
            "crocodile",
            () -> EntityType.Builder.of(CrocodileEntity::new, MobCategory.CREATURE)
                    //Hitbox
                    .sized(1.9975f, 1.1f)
                    .eyeHeight(1.0f)
                    .clientTrackingRange(8)
                    .build("crocodile"));

    public static DeferredHolder<EntityType<?>, EntityType<EagleEntity>> EAGLE = Primal_Registries.ENTITIES.register(
            "eagle",
            () -> EntityType.Builder.of(EagleEntity::new, MobCategory.CREATURE)
                    //Hitbox
                    .sized(0.9f, 1.25f)
                    .clientTrackingRange(8)
                    .build("eagle"));

    public static void init() {}
}
