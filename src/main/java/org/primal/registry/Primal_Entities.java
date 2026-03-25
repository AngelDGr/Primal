package org.primal.registry;

import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.RegistryObject;
import org.primal.Primal_Registries;
import org.primal.entity.animal.*;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.primal.entity.misc.ExploseedEntity;

public final class Primal_Entities {

    //──────────────────────────────────── Animals - 1.0 ────────────────────────────────────
    public static RegistryObject<EntityType<BearEntity>> BEAR = Primal_Registries.ENTITIES.register(
            "bear",
            () -> EntityType.Builder.of(BearEntity::new, MobCategory.CREATURE)
                    //Hitbox
                    .sized(1.5f, 1.75f)
                    .clientTrackingRange(8)
                    .build("bear"));

    public static RegistryObject<EntityType<SharkEntity>> SHARK = Primal_Registries.ENTITIES.register(
            "shark",
            () -> EntityType.Builder.of(SharkEntity::new, MobCategory.WATER_CREATURE)
                    //Hitbox
                    .sized(1.75f, 1.3f)
                    .clientTrackingRange(8)
                    .build("shark"));

    public static RegistryObject<EntityType<CrocodileEntity>> CROCODILE = Primal_Registries.ENTITIES.register(
            "crocodile",
            () -> EntityType.Builder.of(CrocodileEntity::new, MobCategory.CREATURE)
                    //Hitbox
                    .sized(1.9975f, 1.1f)
                    .clientTrackingRange(8)
                    .build("crocodile"));

    public static RegistryObject<EntityType<EagleEntity>> EAGLE = Primal_Registries.ENTITIES.register(
            "eagle",
            () -> EntityType.Builder.of(EagleEntity::new, MobCategory.CREATURE)
                    //Hitbox
                    .sized(0.9f, 1.25f)
                    .clientTrackingRange(8)
                    .build("eagle"));

    //──────────────────────────────────── Animals - 1.1 ────────────────────────────────────
    public static RegistryObject<EntityType<CassowaryEntity>> CASSOWARY = Primal_Registries.ENTITIES.register(
            "cassowary",
            () -> EntityType.Builder.of(CassowaryEntity::new, MobCategory.CREATURE)
                    //Hitbox
                    .sized(1.15f, 1.85f)
                    .clientTrackingRange(8)
                    .build("cassowary"));

    public static RegistryObject<EntityType<WalrusEntity>> WALRUS = Primal_Registries.ENTITIES.register(
            "walrus",
            () -> EntityType.Builder.of(WalrusEntity::new, MobCategory.CREATURE)
                    //Hitbox
                    .sized(1.85f, 1.85f)
                    .immuneTo(Blocks.POWDER_SNOW)
                    .clientTrackingRange(8)
                    .build("walrus"));

    public static RegistryObject<EntityType<LionEntity>> LION = Primal_Registries.ENTITIES.register(
            "lion",
            () -> EntityType.Builder.of(LionEntity::new, MobCategory.CREATURE)
                    //Hitbox
                    .sized(1.5f, 1.75f)
                    .clientTrackingRange(8)
                    .build("lion"));

    public static RegistryObject<EntityType<SnakeEntity>> SNAKE = Primal_Registries.ENTITIES.register(
            "snake",
            () -> EntityType.Builder.of(SnakeEntity::new, MobCategory.CREATURE)
                    //Hitbox
                    .sized(0.9375f, 0.75f)
                    .clientTrackingRange(8)
                    .build("snake"));

    public static RegistryObject<EntityType<DeerEntity>> DEER = Primal_Registries.ENTITIES.register(
            "deer",
            () -> EntityType.Builder.of(DeerEntity::new, MobCategory.CREATURE)
                    //Hitbox
                    .sized(0.85f, 1.75f)
                    .clientTrackingRange(8)
                    .build("deer"));

    //──────────────────────────────────── Misc ────────────────────────────────────
    public static final RegistryObject<EntityType<ExploseedEntity>> EXPLOSEED = Primal_Registries.ENTITIES.register(
            "exploseed",
            () -> EntityType.Builder.<ExploseedEntity>of(ExploseedEntity::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f).build("exploseed"));
    public static void init() {}
}
