package org.primal.registry;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.EntitySubPredicate;
import net.minecraft.advancements.critereon.EntityVariantPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Parrot;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.advancements.criterion.Primal_CustomCriterion;
import org.primal.advancements.criterion.SharkKillsEntity;
import org.primal.entity.animal.BearEntity;
import org.primal.entity.animal.EagleEntity;

import java.util.List;
import java.util.Optional;

public class Primal_Advancements {
    //Shark
    public static final Primal_CustomCriterion SURVIVE_SHARK =
            CriteriaTriggers.register(new Primal_CustomCriterion(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID,"survive_shark")));

    public static final Primal_CustomCriterion SWIM_WITH_SHARK =
            CriteriaTriggers.register(new Primal_CustomCriterion(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "swim_with_sharks")));

    public static final SharkKillsEntity FEED_SHARK =
            CriteriaTriggers.register(new SharkKillsEntity());

    //Crocodile
    public static final Primal_CustomCriterion PUNCH_CROCODILE =
            CriteriaTriggers.register(new Primal_CustomCriterion(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "punch_crocodile")));

    public static final Primal_CustomCriterion CLOCK_CROC =
            CriteriaTriggers.register(new Primal_CustomCriterion(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "clock_croc")));

    public static final Primal_CustomCriterion TICKLE_CROC =
            CriteriaTriggers.register(new Primal_CustomCriterion(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "tickle_crocodile")));

    //Eagle
    public static final Primal_CustomCriterion KILL_CAPTAIN =
            CriteriaTriggers.register(new Primal_CustomCriterion(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "kill_captain")));

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

    //Eagle
    public static final Primal_CustomCriterion EAGLE_ATTACKS_SNAKE =
            CriteriaTriggers.register(new Primal_CustomCriterion(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "eagle_attacks_snake")));

    //Cassowary
    public static final Primal_CustomCriterion FEED_PETRIFIED =
            CriteriaTriggers.register(new Primal_CustomCriterion(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "feed_petrified")));

    //Walrus
    public static final Primal_CustomCriterion WALRUS_PLAYS =
            CriteriaTriggers.register(new Primal_CustomCriterion(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "walrus_plays")));

    //Lion
    public static final Primal_CustomCriterion LION_NAP =
            CriteriaTriggers.register(new Primal_CustomCriterion(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "lion_nap")));

    //Snake
    public static final Primal_CustomCriterion SNAKE_CHEST =
            CriteriaTriggers.register(new Primal_CustomCriterion(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "snake_chest")));

    public static final Primal_CustomCriterion SNAKE_FILL_BOTTLE =
            CriteriaTriggers.register(new Primal_CustomCriterion(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "snake_fill_bottle")));

    //Deer
    public static final Primal_CustomCriterion DEER_DISC =
            CriteriaTriggers.register(new Primal_CustomCriterion(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "deer_disc")));

    //Misc
    public static final Primal_CustomCriterion ADD_HELMET_DECORATION =
            CriteriaTriggers.register(new Primal_CustomCriterion(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "add_helmet_decoration")));

    public static final Primal_CustomCriterion ADD_HELMET_HORNS =
            CriteriaTriggers.register(new Primal_CustomCriterion(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "add_helmet_horns")));

    public static void initCriteria() {}

    //EntitySubPredicates
    public static final EntityVariantPredicate<BearEntity.Variant> BEAR = EntityVariantPredicate.create(
            BearEntity.Variant.CODEC, entity -> entity instanceof BearEntity bear ? Optional.of(bear.getVariant()) : Optional.empty()
    );

    public static final EntityVariantPredicate<EagleEntity.Variant> EAGLE = EntityVariantPredicate.create(
            EagleEntity.Variant.CODEC, entity -> entity instanceof EagleEntity bear ? Optional.of(bear.getVariant()) : Optional.empty()
    );

    public static void initEntitySubPredicates(){}

    public static EntityPredicate.@NotNull Builder bearVariantTamed(BearEntity.Variant variant){
        return EntityPredicate.Builder.entity().subPredicate(BEAR.createPredicate(variant));
    }

    public static EntityPredicate.@NotNull Builder eagleVariantTamed(EagleEntity.Variant variant){
        return EntityPredicate.Builder.entity().subPredicate(EAGLE.createPredicate(variant));
    }

    public static EntityPredicate.@NotNull Builder parrotVariantTamed(Parrot.Variant variant){
        return EntityPredicate.Builder.entity().subPredicate(EntitySubPredicate.Types.PARROT.createPredicate(variant));
    }

}
