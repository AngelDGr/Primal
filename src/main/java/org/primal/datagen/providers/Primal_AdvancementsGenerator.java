package org.primal.datagen.providers;

import net.minecraft.advancements.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class Primal_AdvancementsGenerator extends AdvancementProvider {

    //TODO: Advancements
    // --- Vanilla --- <- Idk how to do it, but I will try, lol - Tenebris
    // “Parrot and the Bats” & “Two by Two"
    //    All breedable mobs are included by this advancement.
    // --- Bear ---
    // “Friend-Shaped Beast”
    //      Had strong enough confidence to tame a bear.
    // “We Bare Bears” (Challenge Advancement)
    //      Bring all the bears from different climates to your base.
    // --- Eagle ---
    // “FREEDOM!!!!!”
    //      Evade the Exile by killing a Pillager Captain using your Eagle.
    // “Birds Of Feathers” (Challenge Advancement)
    //      Tame all Birds including their variants. (Parrots & Eagle)
    // --- Shark ---
    // “Test The Waters”
    //      Get back to full health, after facing its attack.
    // “One Hungry Shark” (Challenge Advancement)
    //      Bring all marine mobs to the shark's table.
    // “A Sense Of Empowerment”
    //      Power the sea with Conduit and tip your supremacy over sharks.
    // --- Crocodile ---
    // “Floridian Shenanigans”
    //      Hit a crocodile with a shovel.
    // “Tick Tock Croc” (Secret Advancement)
    //      Feed a crocodile with a Clock, then let them devour a Drowned.


    public Primal_AdvancementsGenerator(final PackOutput output, final CompletableFuture<HolderLookup.Provider> lookupProvider, final ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, existingFileHelper, List.of(new Generator()));
    }

    private static final class Generator implements AdvancementGenerator {
        @Override
        public void generate(final HolderLookup.@NotNull Provider registries, final @NotNull Consumer<AdvancementHolder> consumer, final @NotNull ExistingFileHelper existingFileHelper) {

        }
    }
}
