package org.primal.datagen.providers;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.primal.Primal_Main;
import org.primal.Primal_Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.primal.registry.Primal_BannerPatterns;

import java.util.Arrays;
import java.util.List;
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
                .filter(entry -> entry.getKey().location().getNamespace().equals(Primal_Main.MOD_ID) && !(entry.get() instanceof BlockItem))
                .forEach((entry)->addItemTranslation(entry.getKey()));

        Primal_Registries.BLOCKS.getEntries().stream()
                .filter(entry -> entry.getKey().location().getNamespace().equals(Primal_Main.MOD_ID))
                .forEach((entry)->addBlockTranslation(entry.getKey()));

        Primal_Registries.MOB_EFFECTS.getEntries().stream()
                .filter(entry -> entry.getKey().location().getNamespace().equals(Primal_Main.MOD_ID))
                .forEach((entry)-> {
                    addEffectTranslation(entry.getKey());
                    addPotionTranslation(entry.getKey());
                });

        //Subtitles
        {
            add("subtitles.primal.entity.zombie.destroy_generic_egg", "Animal Egg stomped");

            add("subtitles.primal.entity.generic.chest", "Animal Chest equips");
            add("subtitles.primal.entity.generic.places_egg", "Places an egg");
            add("subtitles.primal.entity.generic.lays_egg", "Animal lays egg");

            add("subtitles.primal.entity.generic.egg_break", "Animal Egg breaks");
            add("subtitles.primal.entity.generic.egg_crack", "Animal Egg cracks");
            add("subtitles.primal.entity.generic.egg_hatch", "Animal Egg hatches");

            //Bear
            {
                add("subtitles.primal.entity.bear.idle", "Bear growls");
                add("subtitles.primal.entity.bear.idle_angry", "Bear growls angrily");
                add("subtitles.primal.entity.bear.hurt", "Bear hurts");
                add("subtitles.primal.entity.bear.death", "Bear dies");
                add("subtitles.primal.entity.bear.roar", "Bear roars");
                add("subtitles.primal.entity.bear.snoring", "Bear snores");
                add("subtitles.primal.entity.bear.wake_up", "Bear wakes up");
                add("subtitles.primal.entity.bear.eat", "Bear eats");
            }

            //Shark
            {
                add("subtitles.primal.entity.shark.idle", "Shark groans");
                add("subtitles.primal.entity.shark.attack", "Shark attacks");
                add("subtitles.primal.entity.shark.hurt", "Shark hurts");
                add("subtitles.primal.entity.shark.death", "Shark dies");
                add("subtitles.primal.entity.shark.flop", "Shark flops");
            }

            //Crocodile
            {
                add("subtitles.primal.entity.crocodile.idle", "Crocodile growls");
                add("subtitles.primal.entity.crocodile.attack", "Crocodile attacks");
                add("subtitles.primal.entity.crocodile.hurt", "Crocodile hurts");
                add("subtitles.primal.entity.crocodile.death", "Crocodile dies");
                add("subtitles.primal.entity.crocodile.eat", "Crocodile eats");
                add("subtitles.primal.entity.crocodile.thrash", "Crocodile thrashes");
                add("subtitles.primal.entity.crocodile.splashes", "Violent water splashes");
                add("subtitles.primal.entity.crocodile.vomit", "Crocodile spit item");
                add("subtitles.primal.entity.crocodile.clock", "Tick-tock clock sounds");
            }

            //Eagle
            {
                add("subtitles.primal.entity.eagle.idle", "Eagle screeches");
                add("subtitles.primal.entity.eagle.flap", "Eagle flaps");
                add("subtitles.primal.entity.eagle.hurt", "Eagle hurts");
                add("subtitles.primal.entity.eagle.death", "Eagle dies");
                add("subtitles.primal.entity.eagle.eat", "Eagle eats");
                add("subtitles.primal.entity.eagle.shriek", "Eagle shrieks");
                add("subtitles.primal.entity.eagle.freedom", "FREEDOM");
            }
        }

        //Misc
        add("death.attack.shark_tooth", "%1$s was killed by a dead shark");
        add("death.attack.shark_tooth.player", "%1$s was killed by a dead shark while fighting %2$s");

        add("primal.gui.animal_wandering", "%1$s will wander around");
        add("primal.gui.animal_following", "%1$s will follow you");
        add("primal.gui.animal_sitting", "%1$s will sit down");


        //Advancements
        {
            //Bear
            add("advancements.primal.tame_bear.title", "Friend-Shaped Beast");
            add("advancements.primal.tame_bear.description", "Had strong enough confidence to tame a bear");

            add("advancements.primal.tame_all_bears.title", "We Bare Bears");
            add("advancements.primal.tame_all_bears.description", "Tame all Bear variants!");

            //Shark
            add("advancements.primal.survive_shark.title", "Test The Waters");
            add("advancements.primal.survive_shark.description", "Get back to full health after facing a shark attack");

            add("advancements.primal.swim_with_shark.title", "A Sense Of Empowerment");
            add("advancements.primal.swim_with_shark.description", "Power the sea with a Conduit and tip your supremacy over sharks");

            add("advancements.primal.feed_shark.title", "One Hungry Shark");
            add("advancements.primal.feed_shark.description", "Bring all passive oceanic mobs to the shark's table");

            //Crocodile
            add("advancements.primal.punch_crocodile.title", "Florida Shenanigans");
            add("advancements.primal.punch_crocodile.description", "Punch a crocodile with your bare hands");

            add("advancements.primal.clock_croc.title", "Tick-Tock Croc");
            add("advancements.primal.clock_croc.description", "Feed a crocodile with a clock, this probably will scare pillagers!");

            add("advancements.primal.tickle_crocodile.title", "Tickle tickle");
            add("advancements.primal.tickle_crocodile.description", "Tickle a crocodile with a feather and retrieve some items from its stomach");

            //Eagle
            add("advancements.primal.kill_captain.title", "FREEDOM!!!!!");
            add("advancements.primal.kill_captain.description", "Kill a raid captain alongside your eagle");

            add("advancements.primal.tame_all_birds.title", "Birds Of Feathers");
            add("advancements.primal.tame_all_birds.description", "Tame all Birds including their variants. (Parrots & Eagle)");

            add("advancements.primal.get_eagle_egg.title", "Angry Birds");
            add("advancements.primal.get_eagle_egg.description", "Get an eagle egg. Don’t forget to feed raw chicken to the baby, be a good parent!");

            //Misc
            add("advancements.primal.eat_apple_fritter.title", "Double-Glazed");
            add("advancements.primal.eat_apple_fritter.description", "Consume and absorb the nutrients of one apple fritter");
        }

        Primal_BannerPatterns.Banner_Patterns.forEach(
                this::addBannerTranslation
        );
    }

    private void addBannerTranslation(ResourceKey<BannerPattern> banner) {
        String path = banner.location().getPath();
        String baseBannerTranslation = translate(path);
        String baseBannerTranslationKey="block.minecraft.banner.primal."+path;
        add(baseBannerTranslationKey, baseBannerTranslation);

        for(DyeColor dyeColor: DyeColor.values()){
            String translatedDyeColor= translate(dyeColor.getName());
            add(baseBannerTranslationKey+"."+dyeColor.getName(), translatedDyeColor+" "+baseBannerTranslation);
        }
    }

    private void addPotionTranslation(ResourceKey<MobEffect> effectKey) {
        String effect=effectKey.location().getPath();

        List<Pair<String, String>> types=List.of(
                Pair.of("potion", "Potion of "),
                Pair.of( "splash_potion", "Splash Potion of "),
                Pair.of("lingering_potion", "Lingering Potion of "),
                Pair.of("tipped_arrow", "Arrow of "));


        for (Pair<String, String> type: types){
            String id = "item.minecraft." +type.getFirst()+ ".effect."+effect;

            String translation = type.getSecond()+translate(effect);

            add(id, translation);
        }
    }

    private void addEntityTranslation(ResourceKey<EntityType<?>> item) {
        String path = item.location().getPath();
        String translation = translate(path);
        add(BuiltInRegistries.ENTITY_TYPE.get(item.location()), translation);
    }

    private void addItemTranslation(ResourceKey<Item> item) {
        String path = item.location().getPath();
        String translation = translate(path);

        //Handles descriptions and names for banner patterns
        if(BuiltInRegistries.ITEM.get(item.location()) instanceof BannerPatternItem bannerPatternItem){
            String resourceKeyPath= bannerPatternItem.getBannerPattern().location().getPath();
            String[] resourceKeySplitted = resourceKeyPath.split("/");
            //Name (All are named "Banner Pattern")
            add(BuiltInRegistries.ITEM.get(item.location()), "Banner Pattern");

            String translationBanner = translate(resourceKeySplitted[1]);
            //Description
            add(BuiltInRegistries.ITEM.get(item.location()).getDescriptionId()+".desc", translationBanner);

        } else {
            add(BuiltInRegistries.ITEM.get(item.location()), translation);
        }
    }

    private void addBlockTranslation(ResourceKey<Block> block) {
        String path = block.location().getPath();
        String translation = translate(path);
        add(BuiltInRegistries.BLOCK.get(block.location()), translation);
    }

    @SuppressWarnings("all")
    private void addEffectTranslation(ResourceKey<MobEffect> block) {
        String path = block.location().getPath();
        String translation = translate(path);
        add(BuiltInRegistries.MOB_EFFECT.get(block.location()), translation);
    }

    private String translate(String path) {
        return Arrays.stream(path.split("_"))
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
                .collect(Collectors.joining(" "));
    }
}
