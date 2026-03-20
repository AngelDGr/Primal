package org.primal.registry;

import net.minecraft.world.level.block.Blocks;
import org.primal.Primal_Registries;
import org.primal.entity.animal.LionEntity;
import org.primal.entity.animal.*;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.primal.entity.misc.ExploseedEntity;

public final class Primal_Entities {

    //──────────────────────────────────── Animals - 1.0 ────────────────────────────────────
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
                    .sized(1.75f, 1.3f)
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

    //──────────────────────────────────── Animals - 1.1 ────────────────────────────────────
    public static DeferredHolder<EntityType<?>, EntityType<CassowaryEntity>> CASSOWARY = Primal_Registries.ENTITIES.register(
            "cassowary",
            () -> EntityType.Builder.of(CassowaryEntity::new, MobCategory.CREATURE)
                    //Hitbox
                    .sized(1.15f, 1.85f)
                    .clientTrackingRange(8)
                    .build("cassowary"));

    public static DeferredHolder<EntityType<?>, EntityType<WalrusEntity>> WALRUS = Primal_Registries.ENTITIES.register(
            "walrus",
            () -> EntityType.Builder.of(WalrusEntity::new, MobCategory.CREATURE)
                    //Hitbox
                    .sized(1.85f, 1.85f)
                    .immuneTo(Blocks.POWDER_SNOW)
                    .clientTrackingRange(8)
                    .build("walrus"));

    public static DeferredHolder<EntityType<?>, EntityType<LionEntity>> LION = Primal_Registries.ENTITIES.register(
            "lion",
            () -> EntityType.Builder.of(LionEntity::new, MobCategory.CREATURE)
                    //Hitbox
                    .sized(1.5f, 1.75f)
                    .clientTrackingRange(8)
                    .build("lion"));

    public static DeferredHolder<EntityType<?>, EntityType<SnakeEntity>> SNAKE = Primal_Registries.ENTITIES.register(
            "snake",
            () -> EntityType.Builder.of(SnakeEntity::new, MobCategory.CREATURE)
                    //Hitbox
                    .sized(0.9375f, 0.75f)
                    .eyeHeight(0.6f)
                    .clientTrackingRange(8)
                    .build("snake"));

    public static DeferredHolder<EntityType<?>, EntityType<DeerEntity>> DEER = Primal_Registries.ENTITIES.register(
            "deer",
            () -> EntityType.Builder.of(DeerEntity::new, MobCategory.CREATURE)
                    //Hitbox
                    .sized(0.85f, 1.75f)
                    .clientTrackingRange(8)
                    .build("deer"));

    //──────────────────────────────────── Misc ────────────────────────────────────
    public static final DeferredHolder<EntityType<?>, EntityType<ExploseedEntity>> EXPLOSEED = Primal_Registries.ENTITIES.register(
            "exploseed",
            () -> EntityType.Builder.<ExploseedEntity>of(ExploseedEntity::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f).build("exploseed"));
    public static void init() {}
}
