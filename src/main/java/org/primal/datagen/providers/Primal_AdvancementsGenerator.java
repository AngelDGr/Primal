package org.primal.datagen.providers;

import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.advancements.criterion.Primal_CustomCriterion;
import org.primal.advancements.criterion.SharkKillsEntity;
import org.primal.entity.animal.BearEntity;
import org.primal.entity.animal.EagleEntity;
import org.primal.registry.Primal_Advancements;
import org.primal.registry.Primal_Entities;
import org.primal.registry.Primal_Items;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class Primal_AdvancementsGenerator extends AdvancementProvider {

    //xTODO: Advancements
    // --- Eagle ---
    // “FREEDOM!!!!!”
    //      Evade the Exile by killing a Pillager Captain using your Eagle.
    // “Birds Of Feathers” (Challenge Advancement)
    //      Tame all Birds including their variants. (Parrots & Eagle)
    //xxx Crocodile xxx
    //“Tick Tock Croc” (Secret Advancement)
    //      Feed a crocodile with a Clock, then let them devour a Drowned.
    //“Floridian Shenanigans”
    //      Hit a crocodile.
    //xxx Shark xxx
    // “Test The Waters”
    //      Get back to full health, after facing its attack.
    // “One Hungry Shark” (Challenge Advancement)
    //      Bring all marine mobs to the shark's table.
    // “A Sense Of Empowerment”
    //      Power the sea with Conduit and tip your supremacy over sharks.
    //“Parrot and the Bats” <- Added automatically
    //xxx Bear xxx
    // “Friend-Shaped Beast”
    //       Had strong enough confidence to tame a bear.
    // “We Bare Bears” (Challenge Advancement)
    //      Bring all the bears from different climates to your base.
    //--- Vanilla ---
    // "Two by Two" <- Probably impossible to add the mod animals to this because the advancements are already generated as data - Tenebris

    public Primal_AdvancementsGenerator(final PackOutput output, final CompletableFuture<HolderLookup.Provider> lookupProvider, final ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, existingFileHelper, List.of(new Generator()));
    }

    private static final class Generator implements AdvancementGenerator {

        @SuppressWarnings("removal")
        @Override
        public void generate(final HolderLookup.@NotNull Provider registries, final @NotNull Consumer<AdvancementHolder> consumer, final @NotNull ExistingFileHelper existingFileHelper) {

            //Bear
            {
                final AdvancementHolder tameBear = Advancement.Builder.advancement()
                        .parent(ResourceLocation.withDefaultNamespace("husbandry/tame_an_animal"))
                        .display(
                                Items.HONEYCOMB, // The display icon
                                Component.translatable("advancements.primal.tame_bear.title"), // The title
                                Component.translatable("advancements.primal.tame_bear.description"), // The description
                                null,
                                AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                false // Hidden in the advancement tab
                        )
                        .addCriterion("tame_bear",
                                TameAnimalTrigger.TriggerInstance.tamedAnimal(EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(Primal_Entities.BEAR.get()))))
                        .save(consumer, Primal_Main.MOD_ID + "/tame_bear");

                Advancement.Builder.advancement()
                        .parent(tameBear)
                        .display(
                                Items.BARREL, // The display icon
                                Component.translatable("advancements.primal.tame_all_bears.title"), // The title
                                Component.translatable("advancements.primal.tame_all_bears.description"), // The description
                                null,
                                AdvancementType.CHALLENGE, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                false // Hidden in the advancement tab
                        )
                        .rewards(AdvancementRewards.Builder.experience(50))
                        .addCriterion("tame_grizzly", TameAnimalTrigger.TriggerInstance.tamedAnimal(Primal_Advancements.bearVariantTamed(BearEntity.Variant.GRIZZLY)))
                        .addCriterion("tame_grolar", TameAnimalTrigger.TriggerInstance.tamedAnimal(Primal_Advancements.bearVariantTamed(BearEntity.Variant.GROLAR)))
                        .addCriterion("tame_warm", TameAnimalTrigger.TriggerInstance.tamedAnimal(Primal_Advancements.bearVariantTamed(BearEntity.Variant.WARM)))
                        .save(consumer, Primal_Main.MOD_ID + "/tame_all_bears");
            }

            //Shark
            {
                Advancement.Builder.advancement()
                        .parent(ResourceLocation.withDefaultNamespace("husbandry/root"))
                        .display(
                                Items.WATER_BUCKET, // The display icon
                                Component.translatable("advancements.primal.survive_shark.title"), // The title
                                Component.translatable("advancements.primal.survive_shark.description"), // The description
                                null,
                                AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                false // Hidden in the advancement tab
                        )
                        .addCriterion("survive_shark", Primal_CustomCriterion.Conditions.createSurviveShark())
                        .save(consumer, Primal_Main.MOD_ID + "/survive_shark");

                addMarineAnimalsToFeedShark(Advancement.Builder.advancement())
                        .parent(ResourceLocation.withDefaultNamespace("husbandry/root"))
                        .display(
                                Primal_Items.SHARK_TOOTH.get(), // The display icon
                                Component.translatable("advancements.primal.feed_shark.title"), // The title
                                Component.translatable("advancements.primal.feed_shark.description"), // The description
                                null,
                                AdvancementType.CHALLENGE, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                false // Hidden in the advancement tab
                        )
                        .rewards(AdvancementRewards.Builder.experience(100))
                        .save(consumer, Primal_Main.MOD_ID + "/feed_shark");

                Advancement.Builder.advancement()
                        .parent(ResourceLocation.withDefaultNamespace("husbandry/root"))
                        .display(
                                Items.HEART_OF_THE_SEA, // The display icon
                                Component.translatable("advancements.primal.swim_with_shark.title"), // The title
                                Component.translatable("advancements.primal.swim_with_shark.description"), // The description
                                null,
                                AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                false // Hidden in the advancement tab
                        )
                        .addCriterion("swim_with_shark", Primal_CustomCriterion.Conditions.createSwimWithShark())
                        .save(consumer, Primal_Main.MOD_ID + "/swim_with_shark");
            }

            //Crocodile
            {
                Advancement.Builder.advancement()
                        .parent(ResourceLocation.withDefaultNamespace("husbandry/root"))
                        .display(
                                Primal_Items.CROCODILE_SCUTE.get(), // The display icon
                                Component.translatable("advancements.primal.punch_crocodile.title"), // The title
                                Component.translatable("advancements.primal.punch_crocodile.description"), // The description
                                null,
                                AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                false // Hidden in the advancement tab
                        )
                        .addCriterion("punch_crocodile", Primal_CustomCriterion.Conditions.createPunchCrocodile())
                        .save(consumer, Primal_Main.MOD_ID + "/punch_crocodile");

                Advancement.Builder.advancement()
                        .parent(ResourceLocation.withDefaultNamespace("husbandry/root"))
                        .display(
                                Items.CLOCK, // The display icon
                                Component.translatable("advancements.primal.clock_croc.title"), // The title
                                Component.translatable("advancements.primal.clock_croc.description"), // The description
                                null,
                                AdvancementType.CHALLENGE, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                true // Hidden in the advancement tab
                        )
                        .addCriterion("clock_croc", Primal_CustomCriterion.Conditions.createClockCroc())
                        .save(consumer, Primal_Main.MOD_ID + "/clock_croc");

                Advancement.Builder.advancement()
                        .parent(ResourceLocation.withDefaultNamespace("husbandry/root"))
                        .display(
                                Items.FEATHER, // The display icon
                                Component.translatable("advancements.primal.tickle_crocodile.title"), // The title
                                Component.translatable("advancements.primal.tickle_crocodile.description"), // The description
                                null,
                                AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                false // Hidden in the advancement tab
                        )
                        .addCriterion("tickle_crocodile", Primal_CustomCriterion.Conditions.createTickleCrocodile())
                        .save(consumer, Primal_Main.MOD_ID + "/tickle_crocodile");
            }

            //Eagle
            {
                HolderLookup.RegistryLookup<BannerPattern> registrylookup = registries.lookupOrThrow(Registries.BANNER_PATTERN);

                Advancement.Builder.advancement()
                        .parent(ResourceLocation.withDefaultNamespace("husbandry/root"))
                        .display(
                                Raid.getLeaderBannerInstance(registrylookup), // The display icon
                                Component.translatable("advancements.primal.kill_captain.title"), // The title
                                Component.translatable("advancements.primal.kill_captain.description"), // The description
                                null,
                                AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                false // Hidden in the advancement tab
                        )
                        .addCriterion("kill_captain", Primal_CustomCriterion.Conditions.createKillCaptain())
                        .save(consumer, Primal_Main.MOD_ID + "/kill_captain");

                Advancement.Builder.advancement()
                        .parent(ResourceLocation.withDefaultNamespace("husbandry/tame_an_animal"))
                        .display(
                                Items.EGG, // The display icon
                                Component.translatable("advancements.primal.tame_all_birds.title"), // The title
                                Component.translatable("advancements.primal.tame_all_birds.description"), // The description
                                null,
                                AdvancementType.CHALLENGE, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                false // Hidden in the advancement tab
                        )
                        .rewards(AdvancementRewards.Builder.experience(50))
                        .addCriterion("tame_bald", TameAnimalTrigger.TriggerInstance.tamedAnimal(Primal_Advancements.eagleVariantTamed(EagleEntity.Variant.BALD)))
                        .addCriterion("tame_harpy", TameAnimalTrigger.TriggerInstance.tamedAnimal(Primal_Advancements.eagleVariantTamed(EagleEntity.Variant.HARPY)))
                        .addCriterion("tame_philippine", TameAnimalTrigger.TriggerInstance.tamedAnimal(Primal_Advancements.eagleVariantTamed(EagleEntity.Variant.PHILIPPINE)))
                        .addCriterion("tame_golden", TameAnimalTrigger.TriggerInstance.tamedAnimal(Primal_Advancements.eagleVariantTamed(EagleEntity.Variant.GOLDEN)))

                        .addCriterion("tame_blue", TameAnimalTrigger.TriggerInstance.tamedAnimal(Primal_Advancements.parrotVariantTamed(Parrot.Variant.BLUE)))
                        .addCriterion("tame_gray", TameAnimalTrigger.TriggerInstance.tamedAnimal(Primal_Advancements.parrotVariantTamed(Parrot.Variant.GRAY)))
                        .addCriterion("tame_green", TameAnimalTrigger.TriggerInstance.tamedAnimal(Primal_Advancements.parrotVariantTamed(Parrot.Variant.GREEN)))
                        .addCriterion("tame_red_blue", TameAnimalTrigger.TriggerInstance.tamedAnimal(Primal_Advancements.parrotVariantTamed(Parrot.Variant.RED_BLUE)))
                        .addCriterion("tame_yellow_blue", TameAnimalTrigger.TriggerInstance.tamedAnimal(Primal_Advancements.parrotVariantTamed(Parrot.Variant.YELLOW_BLUE)))
                        .save(consumer, Primal_Main.MOD_ID + "/tame_all_birds");

                Advancement.Builder.advancement()
                        .parent(ResourceLocation.withDefaultNamespace("husbandry/root"))
                        .display(
                                Primal_Items.EAGLE_EGG.get(), // The display icon
                                Component.translatable("advancements.primal.get_eagle_egg.title"), // The title
                                Component.translatable("advancements.primal.get_eagle_egg.description"), // The description
                                null,
                                AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                false // Hidden in the advancement tab
                        )
                        .addCriterion("eagle_egg", InventoryChangeTrigger.TriggerInstance.hasItems(Primal_Items.EAGLE_EGG.get()))
                        .save(consumer, Primal_Main.MOD_ID + "/get_eagle_egg");
            }

            //Misc
            {
                Advancement.Builder.advancement()
                        .parent(ResourceLocation.withDefaultNamespace("husbandry/root"))
                        .display(
                                Primal_Items.APPLE_FRITTER.get(), // The display icon
                                Component.translatable("advancements.primal.eat_apple_fritter.title"), // The title
                                Component.translatable("advancements.primal.eat_apple_fritter.description"), // The description
                                null,
                                AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                false // Hidden in the advancement tab
                        )
                        .addCriterion("eat_apple_fritter", ConsumeItemTrigger.TriggerInstance.usedItem(Primal_Items.APPLE_FRITTER.get()))
                        .save(consumer, Primal_Main.MOD_ID + "/eat_apple_fritter");
            }
        }
    }

    private static Advancement.Builder addMarineAnimalsToFeedShark(Advancement.Builder builder) {
        Primal_Advancements.ANIMALS_SHARK_NEEDS_TO_KILL
                .forEach(
                marineEntity -> builder.addCriterion(
                        EntityType.getKey(marineEntity).toString(),
                        SharkKillsEntity.Conditions.create(EntityPredicate.Builder.entity().of(marineEntity))
                )
        );
        return builder;
    }
}
