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
import net.minecraft.world.item.ItemStack;
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
import org.primal.item.component.HelmetDecorationComponent;
import org.primal.registry.Primal_Advancements;
import org.primal.registry.Primal_Entities;
import org.primal.registry.Primal_HelmetDecorations;
import org.primal.registry.Primal_Items;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class Primal_AdvancementsGenerator extends AdvancementProvider {

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
                        .addCriterion("survive_shark", Primal_CustomCriterion.Conditions.create(Primal_Advancements.SURVIVE_SHARK))
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
                        .addCriterion("swim_with_shark", Primal_CustomCriterion.Conditions.create(Primal_Advancements.SWIM_WITH_SHARK))
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
                        .addCriterion("punch_crocodile", Primal_CustomCriterion.Conditions.create(Primal_Advancements.PUNCH_CROCODILE))
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
                        .addCriterion("clock_croc", Primal_CustomCriterion.Conditions.create(Primal_Advancements.CLOCK_CROC))
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
                        .addCriterion("tickle_crocodile", Primal_CustomCriterion.Conditions.create(Primal_Advancements.TICKLE_CROC))
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
                        .addCriterion("kill_captain", Primal_CustomCriterion.Conditions.create(Primal_Advancements.KILL_CAPTAIN))
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

                Advancement.Builder.advancement()
                        .parent(ResourceLocation.withDefaultNamespace("husbandry/root"))
                        .display(
                                Items.CACTUS, // The display icon
                                Component.translatable("advancements.primal.eagle_attacks_snake.title"), // The title
                                Component.translatable("advancements.primal.eagle_attacks_snake.description"), // The description
                                null,
                                AdvancementType.CHALLENGE, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                true // Hidden in the advancement tab
                        )
                        .addCriterion("eagle_attacks_snake", Primal_CustomCriterion.Conditions.create(Primal_Advancements.EAGLE_ATTACKS_SNAKE))
                        .rewards(AdvancementRewards.Builder.experience(50))
                        .save(consumer, Primal_Main.MOD_ID + "/eagle_attacks_snake");
            }

            //Cassowary
            {
                Advancement.Builder.advancement()
                        .parent(ResourceLocation.withDefaultNamespace("husbandry/root"))
                        .display(
                                Primal_Items.CASSOWARY_EGG.get(), // The display icon
                                Component.translatable("advancements.primal.get_cassowary_egg.title"), // The title
                                Component.translatable("advancements.primal.get_cassowary_egg.description"), // The description
                                null,
                                AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                false // Hidden in the advancement tab
                        )
                        .addCriterion("get_cassowary_egg", InventoryChangeTrigger.TriggerInstance.hasItems(Primal_Items.CASSOWARY_EGG.get()))
                        .save(consumer, Primal_Main.MOD_ID + "/get_cassowary_egg");

                final AdvancementHolder feedFruit = Advancement.Builder.advancement()
                        .parent(ResourceLocation.withDefaultNamespace("husbandry/root"))
                        .display(
                                Primal_Items.PETRIFIED_FRUIT.get(), // The display icon
                                Component.translatable("advancements.primal.give_petrified_fruit.title"), // The title
                                Component.translatable("advancements.primal.give_petrified_fruit.description"), // The description
                                null,
                                AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                false // Hidden in the advancement tab
                        )
                        .addCriterion("give_petrified_fruit", Primal_CustomCriterion.Conditions.create(Primal_Advancements.FEED_PETRIFIED))
                        .save(consumer, Primal_Main.MOD_ID + "/give_petrified_fruit");

                Advancement.Builder.advancement()
                        .parent(feedFruit)
                        .display(
                                Primal_Items.WEIRD_APPLE.get(), // The display icon
                                Component.translatable("advancements.primal.all_exotic_fruits.title"), // The title
                                Component.translatable("advancements.primal.all_exotic_fruits.description"), // The description
                                null,
                                AdvancementType.CHALLENGE, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                true // Hidden in the advancement tab
                        )
                        .rewards(AdvancementRewards.Builder.experience(50))
                        .addCriterion("all_exotic_fruits", InventoryChangeTrigger.TriggerInstance.hasItems(
                                Primal_Items.KIWANO.get(),
                                Primal_Items.LITCHI.get(),
                                Primal_Items.STARFRUIT.get()))
                        .save(consumer, Primal_Main.MOD_ID + "/all_exotic_fruits");
            }

            //Walrus
            {
                Advancement.Builder.advancement()
                        .parent(ResourceLocation.withDefaultNamespace("husbandry/tame_an_animal"))
                        .display(
                                Primal_Items.COLD_SEASHELLS.get(), // The display icon
                                Component.translatable("advancements.primal.tame_walrus.title"), // The title
                                Component.translatable("advancements.primal.tame_walrus.description"), // The description
                                null,
                                AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                false // Hidden in the advancement tab
                        )
                        .addCriterion("tame_walrus",
                                TameAnimalTrigger.TriggerInstance.tamedAnimal(EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(Primal_Entities.WALRUS.get()))))
                        .save(consumer, Primal_Main.MOD_ID + "/tame_walrus");

                Advancement.Builder.advancement()
                        .parent(ResourceLocation.withDefaultNamespace("husbandry/root"))
                        .display(
                                Primal_Items.WARM_CONCH_SHELL.get(), // The display icon
                                Component.translatable("advancements.primal.walrus_plays.title"), // The title
                                Component.translatable("advancements.primal.walrus_plays.description"), // The description
                                null,
                                AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                true // Hidden in the advancement tab
                        )
                        .addCriterion("walrus_plays", Primal_CustomCriterion.Conditions.create(Primal_Advancements.WALRUS_PLAYS))
                        .save(consumer, Primal_Main.MOD_ID + "/walrus_plays");
            }

            //Lion
            {
                final AdvancementHolder tameLion = Advancement.Builder.advancement()
                        .parent(ResourceLocation.withDefaultNamespace("husbandry/tame_an_animal"))
                        .display(
                                Items.BEEF, // The display icon
                                Component.translatable("advancements.primal.tame_lion.title"), // The title
                                Component.translatable("advancements.primal.tame_lion.description"), // The description
                                null,
                                AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                false // Hidden in the advancement tab
                        )
                        .addCriterion("tame_lion",
                                TameAnimalTrigger.TriggerInstance.tamedAnimal(EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(Primal_Entities.LION.get()))))
                        .save(consumer, Primal_Main.MOD_ID + "/tame_lion");

                Advancement.Builder.advancement()
                        .parent(tameLion)
                        .display(
                                Items.ORANGE_BED, // The display icon
                                Component.translatable("advancements.primal.lion_nap.title"), // The title
                                Component.translatable("advancements.primal.lion_nap.description"), // The description
                                null,
                                AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                false // Hidden in the advancement tab
                        )
                        .addCriterion("lion_nap", Primal_CustomCriterion.Conditions.create(Primal_Advancements.LION_NAP))
                        .save(consumer, Primal_Main.MOD_ID + "/lion_nap");
            }

            //Snake
            {
                Advancement.Builder.advancement()
                        .parent(ResourceLocation.withDefaultNamespace("husbandry/root"))
                        .display(
                                Items.CHEST, // The display icon
                                Component.translatable("advancements.primal.snake_chest.title"), // The title
                                Component.translatable("advancements.primal.snake_chest.description"), // The description
                                null,
                                AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                true // Hidden in the advancement tab
                        )
                        .addCriterion("snake_chest", Primal_CustomCriterion.Conditions.create(Primal_Advancements.SNAKE_CHEST))
                        .save(consumer, Primal_Main.MOD_ID + "/snake_chest");

                Advancement.Builder.advancement()
                        .parent(ResourceLocation.withDefaultNamespace("husbandry/root"))
                        .display(
                                Items.POTION, // The display icon
                                Component.translatable("advancements.primal.snake_fill_bottle.title"), // The title
                                Component.translatable("advancements.primal.snake_fill_bottle.description"), // The description
                                null,
                                AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                false // Hidden in the advancement tab
                        )
                        .addCriterion("snake_fill_bottle", Primal_CustomCriterion.Conditions.create(Primal_Advancements.SNAKE_FILL_BOTTLE))
                        .save(consumer, Primal_Main.MOD_ID + "/snake_fill_bottle");
            }

            //Deer
            {
                Advancement.Builder.advancement()
                        .parent(ResourceLocation.withDefaultNamespace("husbandry/root"))
                        .display(
                                Items.LIGHTNING_ROD, // The display icon
                                Component.translatable("advancements.primal.deer_disc.title"), // The title
                                Component.translatable("advancements.primal.deer_disc.description"), // The description
                                null,
                                AdvancementType.CHALLENGE, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                true // Hidden in the advancement tab
                        )
                        .rewards(AdvancementRewards.Builder.experience(50))
                        .addCriterion("deer_disc", Primal_CustomCriterion.Conditions.create(Primal_Advancements.DEER_DISC))
                        .save(consumer, Primal_Main.MOD_ID + "/deer_disc");
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

                var helmet= Advancement.Builder.advancement()
                        .parent(ResourceLocation.withDefaultNamespace("adventure/root"))
                        .display(
                                getHelmetWithDecorations(), // The display icon
                                Component.translatable("advancements.primal.add_helmet_decoration.title"), // The title
                                Component.translatable("advancements.primal.add_helmet_decoration.description"), // The description
                                null,
                                AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                false // Hidden in the advancement tab
                        )
                        .addCriterion("add_helmet_decoration", Primal_CustomCriterion.Conditions.create(Primal_Advancements.ADD_HELMET_DECORATION))
                        .save(consumer, Primal_Main.MOD_ID + "/add_helmet_decoration");

                Advancement.Builder.advancement()
                        .parent(helmet)
                        .display(
                                getHelmetDragonborn(), // The display icon
                                Component.translatable("advancements.primal.add_helmet_horns.title"), // The title
                                Component.translatable("advancements.primal.add_helmet_horns.description"), // The description
                                null,
                                AdvancementType.CHALLENGE, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                true // Hidden in the advancement tab
                        )
                        .addCriterion("add_helmet_horns", Primal_CustomCriterion.Conditions.create(Primal_Advancements.ADD_HELMET_HORNS))
                        .save(consumer, Primal_Main.MOD_ID + "/add_helmet_horns");
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

    public static ItemStack getHelmetWithDecorations() {
        var icon = Items.DIAMOND_HELMET.getDefaultInstance();
        icon.set(Primal_Items.Components.HELMET_DECORATION, new HelmetDecorationComponent(Primal_HelmetDecorations.FALLOW_DEER.get(), Primal_HelmetDecorations.FALLOW_DEER.get()));
        return icon;
    }

    public static ItemStack getHelmetDragonborn() {
        var icon = Items.NETHERITE_HELMET.getDefaultInstance();
        icon.set(Primal_Items.Components.HELMET_DECORATION, new HelmetDecorationComponent(Primal_HelmetDecorations.GOAT.get(), Primal_HelmetDecorations.GOAT.get()));
        return icon;
    }
}
