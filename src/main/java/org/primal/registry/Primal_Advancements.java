package org.primal.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.EntitySubPredicate;
import net.minecraft.advancements.critereon.EntitySubPredicates;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.Primal_Registries;
import org.primal.advancements.criterion.Primal_CustomCriterion;
import org.primal.advancements.criterion.SharkKillsEntity;
import org.primal.entity.animal.BearEntity;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class Primal_Advancements {
    //Criteria
    public static final DeferredHolder<CriterionTrigger<?>, Primal_CustomCriterion> SURVIVE_SHARK =
            Primal_Registries.CRITERIA.register(Primal_Main.MOD_ID+"/survive_shark", Primal_CustomCriterion::new);

    public static final DeferredHolder<CriterionTrigger<?>, Primal_CustomCriterion> SWIM_WITH_SHARK =
            Primal_Registries.CRITERIA.register(Primal_Main.MOD_ID+"/swim_with_sharks", Primal_CustomCriterion::new);

    public static final DeferredHolder<CriterionTrigger<?>, SharkKillsEntity> FEED_SHARK =
            Primal_Registries.CRITERIA.register(Primal_Main.MOD_ID+"/feed_shark", SharkKillsEntity::new);

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

}
