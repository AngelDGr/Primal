package org.primal.datagen.providers;

import net.minecraft.world.entity.EntityType;
import org.primal.Primal_Main;
import org.primal.Primal_Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Primal_LanguageFileGenerator extends LanguageProvider {
    public Primal_LanguageFileGenerator(PackOutput output) {
        super(output, Primal_Main.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.primal", "Primal");

        Primal_Registries.ENTITIES.getEntries().stream()
                .filter(entry -> entry.getKey().location().getNamespace().equals(Primal_Main.MOD_ID))
                .forEach((entry)->addEntityTranslation(entry.getKey()));

        Primal_Registries.ITEMS.getEntries().stream()
                .filter(entry -> entry.getKey().location().getNamespace().equals(Primal_Main.MOD_ID))
                .forEach((entry)->addItemTranslation(entry.getKey()));
        //Misc
        add("primal.gui.animal_wandering", "%1$s will wander around");
        add("primal.gui.animal_following", "%1$s will follow you");
        add("primal.gui.animal_sitting", "%1$s will sit down");


        //Advancements
        add("advancements.primal.tame_bear.title", "Friend-Shaped Beast");
        add("advancements.primal.tame_bear.description", "Had strong enough confidence to tame a bear");

        add("advancements.primal.tame_all_bears.title", "We Bare Bears");
        add("advancements.primal.tame_all_bears.description", "Tame all Bear variants!");

        add("advancements.primal.survive_shark.title", "Test The Waters");
        add("advancements.primal.survive_shark.description", "Get back to full health after facing a shark attack");

        add("advancements.primal.swim_with_shark.title", "A Sense Of Empowerment");
        add("advancements.primal.swim_with_shark.description", "Power the sea with a Conduit and tip your supremacy over sharks");

        add("advancements.primal.feed_shark.title", "One Hungry Shark");
        add("advancements.primal.feed_shark.description", "Bring all passive oceanic mobs to the shark's table");
    }

    private void addEntityTranslation(ResourceKey<EntityType<?>> item) {
        String path = item.location().getPath();
        String translation = translate(path);
        add(BuiltInRegistries.ENTITY_TYPE.get(item.location()), translation);
    }

    private void addItemTranslation(ResourceKey<Item> item) {
        String path = item.location().getPath();
        String translation = translate(path);
        add(BuiltInRegistries.ITEM.get(item.location()), translation);
    }

    private String translate(String path) {
        return Arrays.stream(path.split("_"))
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
                .collect(Collectors.joining(" "));
    }
}
