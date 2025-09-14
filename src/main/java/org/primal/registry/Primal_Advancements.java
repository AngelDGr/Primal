package org.primal.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.EntitySubPredicate;
import net.minecraft.advancements.critereon.EntitySubPredicates;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Parrot;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.Primal_Registries;
import org.primal.advancements.criterion.Primal_CustomCriterion;
import org.primal.advancements.criterion.SharkKillsEntity;
import org.primal.entity.animal.BearEntity;
import org.primal.entity.animal.EagleEntity;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class Primal_Advancements {
    //Shark
    public static final DeferredHolder<CriterionTrigger<?>, Primal_CustomCriterion> SURVIVE_SHARK =
            Primal_Registries.CRITERIA.register(Primal_Main.MOD_ID+"/survive_shark", Primal_CustomCriterion::new);

    public static final DeferredHolder<CriterionTrigger<?>, Primal_CustomCriterion> SWIM_WITH_SHARK =
            Primal_Registries.CRITERIA.register(Primal_Main.MOD_ID+"/swim_with_sharks", Primal_CustomCriterion::new);

    public static final DeferredHolder<CriterionTrigger<?>, SharkKillsEntity> FEED_SHARK =
            Primal_Registries.CRITERIA.register(Primal_Main.MOD_ID+"/feed_shark", SharkKillsEntity::new);

    //Crocodile
    public static final DeferredHolder<CriterionTrigger<?>, Primal_CustomCriterion> PUNCH_CROCODILE =
            Primal_Registries.CRITERIA.register(Primal_Main.MOD_ID+"/punch_crocodile", Primal_CustomCriterion::new);

    public static final DeferredHolder<CriterionTrigger<?>, Primal_CustomCriterion> CLOCK_CROC =
            Primal_Registries.CRITERIA.register(Primal_Main.MOD_ID+"/clock_croc", Primal_CustomCriterion::new);

    public static final DeferredHolder<CriterionTrigger<?>, Primal_CustomCriterion> TICKLE_CROC =
            Primal_Registries.CRITERIA.register(Primal_Main.MOD_ID+"/tickle_crocodile", Primal_CustomCriterion::new);

    //Eagle
    public static final DeferredHolder<CriterionTrigger<?>, Primal_CustomCriterion> KILL_CAPTAIN =
            Primal_Registries.CRITERIA.register(Primal_Main.MOD_ID+"/kill_captain", Primal_CustomCriterion::new);

    public static final List<EntityType<?>> ANIMALS_SHARK_NEEDS_TO_KILL = List.of(
            //Fishes
            EntityType.COD,
            EntityType.SALMON,
            EntityType.TROPICAL_FISH,
            EntityType.PUFFERFISH,
            //Squids
            EntityType.SQUID,
            EntityType.GLOW_SQUID,
            //Misc
            EntityType.TURTLE,
            EntityType.DOLPHIN
    );

    public static void initCriteria() {}

    //EntitySubPredicates
    public static final EntitySubPredicates.EntityVariantPredicateType<BearEntity.Variant> BEAR = register(
            "bear",
            EntitySubPredicates.EntityVariantPredicateType.create(
                    BearEntity.Variant.CODEC, entity -> entity instanceof BearEntity bear ? Optional.of(bear.getVariant()) : Optional.empty()
            )
    );

    public static final EntitySubPredicates.EntityVariantPredicateType<EagleEntity.Variant> EAGLE = register(
            "eagle",
            EntitySubPredicates.EntityVariantPredicateType.create(
                    EagleEntity.Variant.CODEC, entity -> entity instanceof EagleEntity bear ? Optional.of(bear.getVariant()) : Optional.empty()
            )
    );

    private static <V> EntitySubPredicates.EntityVariantPredicateType<V> register(String name, EntitySubPredicates.EntityVariantPredicateType<V> predicateType) {
        Primal_Registries.ENTITY_SUB_PREDICATE_TYPES.register(name, ()-> predicateType.codec);
        return predicateType;
    }

    public static @NotNull DeferredHolder<MapCodec<? extends EntitySubPredicate>, MapCodec<? extends EntitySubPredicate>> register(String name, Supplier<MapCodec<? extends EntitySubPredicate>> predicate){
        return Primal_Registries.ENTITY_SUB_PREDICATE_TYPES.register(name, predicate);
    }

    public static void initEntitySubPredicates(){}

    public static EntityPredicate.@NotNull Builder bearVariantTamed(BearEntity.Variant variant){
        return EntityPredicate.Builder.entity().subPredicate(BEAR.createPredicate(variant));
    }

    public static EntityPredicate.@NotNull Builder eagleVariantTamed(EagleEntity.Variant variant){
        return EntityPredicate.Builder.entity().subPredicate(EAGLE.createPredicate(variant));
    }

    public static EntityPredicate.@NotNull Builder parrotVariantTamed(Parrot.Variant variant){
        return EntityPredicate.Builder.entity().subPredicate(EntitySubPredicates.PARROT.createPredicate(variant));
    }

}
