package org.primal.datagen.providers;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.primal.Primal_Main;
import org.primal.Primal_Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.primal.registry.Primal_BannerPatterns;
import org.primal.registry.Primal_Items;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Primal_LanguageFileGenerator extends LanguageProvider {
    public Primal_LanguageFileGenerator(PackOutput output) {
        super(output, Primal_Main.MOD_ID, "en_us");
    }

    List<String> EXCEPTIONS = List.of(
            Primal_Items.WARM_SEASHELLS.getId().getPath(),
            Primal_Items.MUSIC_DISC_OH_DEER.getId().getPath(),
            Primal_Items.CHOMP_TRAP_GREEN.getId().getPath(),
            Primal_Items.CHOMP_TRAP_ARID.getId().getPath(),
            Primal_Items.CHOMP_TRAP_HUMID.getId().getPath(),
            Primal_Items.VENISON.getId().getPath(),
            Primal_Items.PLACEHOLDER_CHESTED_SNAKE.getId().getPath()
    );

    @Override
    protected void addTranslations() {
        add("itemGroup.primal", "Primal");

        Primal_Registries.ENTITIES.getEntries().stream()
                .filter(entry -> entry.getKey().location().getNamespace().equals(Primal_Main.MOD_ID))
                .forEach((entry)->addEntityTranslation(entry.getKey()));

        Primal_Registries.ITEMS.getEntries().stream()
                .filter(entry -> entry.getKey().location().getNamespace().equals(Primal_Main.MOD_ID) && (!(entry.get() instanceof BlockItem) || entry.get() instanceof ItemNameBlockItem) && !EXCEPTIONS.contains(entry.getKey().location().getPath()))
                .forEach((entry)->addItemTranslation(entry.getKey()));

        Primal_Registries.BLOCKS.getEntries().stream()
                .filter(entry -> entry.getKey().location().getNamespace().equals(Primal_Main.MOD_ID) && !EXCEPTIONS.contains(entry.getKey().location().getPath()))
                .forEach((entry)->addBlockTranslation(entry.getKey()));

        Primal_Registries.MOB_EFFECTS.getEntries().stream()
                .filter(entry -> entry.getKey().location().getNamespace().equals(Primal_Main.MOD_ID))
                .forEach((entry)-> {
                    addEffectTranslation(entry.getKey());
                    addPotionTranslation(entry.getKey());
                });

        //Subtitles
        {
            add("subtitles.primal.entity.zombie.destroy_generic_egg", "Animal egg stomped");

            add("subtitles.primal.entity.generic.chest", "Animal chest equips");
            add("subtitles.primal.entity.generic.lays_egg", "Animal lays egg");

            add("subtitles.primal.entity.generic.egg_break", "Animal egg breaks");
            add("subtitles.primal.entity.generic.egg_crack", "Animal egg cracks");
            add("subtitles.primal.entity.generic.egg_hatch", "Animal egg hatches");

            add("subtitles.primal.item.conch_shell.bind", "Pet bound");
            add("subtitles.primal.item.conch_shell.call", "Conch shell plays");
            add("subtitles.primal.item.exploseed.throw", "Exploseed flies");

            add("subtitles.primal.block.nest.places_egg", "Places an egg");
            add("subtitles.primal.block.nest.removes_egg", "Removes an egg");
            add("subtitles.primal.block.chomp_trap.close", "Chomp trap closes");
            add("subtitles.primal.block.chomp_trap.break", "Chomp trap breaks");
            add("subtitles.primal.block.chomp_trap.repair", "Chomp trap repairs");
            add("subtitles.primal.block.hollow_log.offspring", "Animal produces offspring");

            add("subtitles.primal.block.dreamcatcher.attack", "Dreamcatcher attacks");

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

            //Cassowary
            {
                add("subtitles.primal.entity.cassowary.idle", "Cassowary rumbles");
                add("subtitles.primal.entity.cassowary.lunge", "Cassowary lunges");
                add("subtitles.primal.entity.cassowary.hurt", "Cassowary hurts");
                add("subtitles.primal.entity.cassowary.death", "Cassowary dies");
                add("subtitles.primal.entity.cassowary.eat", "Cassowary eats");
                add("subtitles.primal.entity.cassowary.plop", "Cassowary plops item");
            }

            //Walrus
            {
                add("subtitles.primal.entity.walrus.idle", "Walrus groans");
                add("subtitles.primal.entity.walrus.hurt", "Walrus hurts");
                add("subtitles.primal.entity.walrus.death", "Walrus dies");
                add("subtitles.primal.entity.walrus.eat", "Walrus eats");
                add("subtitles.primal.entity.walrus.step", "Footsteps");
                add("subtitles.primal.entity.walrus.whistle", "Walrus whistles");
                add("subtitles.primal.entity.walrus.whistle_rare", "Walrus whistles");
                add("subtitles.primal.entity.walrus.slam", "Walrus does a ground slam");
                add("subtitles.primal.entity.walrus.whirlwind", "Walrus does a whirlwind");
                add("subtitles.primal.entity.walrus.song", "Walrus plays a song");
            }

            //Lion
            {
                add("subtitles.primal.entity.lion.idle", "Lion growls");
                add("subtitles.primal.entity.lion.idle_angry", "Lion growls angrily");
                add("subtitles.primal.entity.lion.hurt", "Lion hurts");
                add("subtitles.primal.entity.lion.death", "Lion dies");
                add("subtitles.primal.entity.lion.eat", "Lion eats");
                add("subtitles.primal.entity.lion.roar", "Lion roars");
                add("subtitles.primal.entity.lion.snoring", "Lion snores");
            }

            //Snake
            {
                add("subtitles.primal.entity.snake.idle", "Snake hisses");
                add("subtitles.primal.entity.snake.hurt", "Snake hurts");
                add("subtitles.primal.entity.snake.death", "Snake dies");
                add("subtitles.primal.entity.snake.gulp", "Snake gulps");
                add("subtitles.primal.entity.snake.snoring", "Snake hisses quietly");
                add("subtitles.primal.entity.snake.pop_in", "Snake enters hollow log");
                add("subtitles.primal.entity.snake.pop_out", "Snake leaves hollow log");
                add("subtitles.primal.entity.snake.break_skin", "Snake breaks its shed skin");
            }

            //Deer
            {
                add("subtitles.primal.entity.deer.idle", "Deer bellows");
                add("subtitles.primal.entity.deer.hurt", "Deer hurts");
                add("subtitles.primal.entity.deer.death", "Deer dies");
                add("subtitles.primal.entity.deer.antler_break", "Deer antler breaks off");
                add("subtitles.primal.entity.deer.ram_impact", "Deer rams");
            }
        }

        //Misc
        {
            //Variant names
            {
                add("entity.primal.bear.grolar", "Grolar");

                add("entity.primal.lion.cave", "Cave Lion");

                add("entity.primal.deer.reindeer", "Reindeer");

                add("entity.minecraft.dolphin.cold", "Beluga");

                add("entity.minecraft.wolf.spotted", "Wild Dog");
                add("entity.minecraft.wolf.rusty", "Dhole");
            }

            add("death.attack.shark_tooth", "%1$s was killed by a dead shark");
            add("death.attack.shark_tooth.player", "%1$s was killed by a dead shark while fighting %2$s");

            add("death.attack.thorny_acacia", "%1$s hugged a spiky tree");
            add("death.attack.thorny_acacia.player", "%1$s was hugged a tree while fighting %2$s");

            add("death.attack.chomp_trap.message", "%1$s was eaten by a dead crocodile");

            add("primal.gui.animal_wandering", "%1$s will wander around");
            add("primal.gui.animal_following", "%1$s will follow you");
            add("primal.gui.animal_sitting", "%1$s will sit down");

            add("block.primal.seashells", "Warm Seashells");
            add("item.primal.bound_conch_shell", "Bounded Conch Shell");
            add("item.primal.music_disc_oh_deer", "Music Disc");
            add("item.minecraft.music_disc_oh_deer.desc", "MistieJam - Oh Deer");
            add("item.primal.venison", "Raw Venison");
            add("item.primal.placeholder_chested_snake", "Snake on Chest");
            add("item.primal.placeholder_chested_snake.biome", "Variant from Biome");
            add("item.primal.placeholder_chested_snake.marine", "Marine");
            add("jukebox_song.primal.oh_deer", "MistieJam - Oh Deer");

            add("block.primal.chomp_trap_green", "Chomp Trap");
            add("block.primal.chomp_trap_humid", "Humid Chomp Trap");
            add("block.primal.chomp_trap_arid", "Arid Chomp Trap");

            add("helmet_decoration.decorations", "Decorations: ");
            add("helmet_decoration.side.right", "Right");
            add("helmet_decoration.side.left", "Left");

            add("helmet_decoration.primal.fallow_antler", "Fallow Deer Antler");
            add("helmet_decoration.primal.reindeer_antler", "Reindeer Antler");
            add("helmet_decoration.primal.whitetail_antler", "Whitetail Deer Antler");
            add("helmet_decoration.primal.goat_horn", "Goat Horn");

            add("container.primal.strawBasket", "Straw Basket");
            add("container.primal.strawBasketDouble", "Large Straw Basket");

            //Field Guide Descriptions/Titles
            {
                add("entity.primal.bear.description",
                        "A neutral mob found in Forests and Jungles. If approached slowly, it can be tamed with honeycombs. Once tamed, it can use a barrel from which it will eat at low health.");

                add("entity.primal.eagle.description",
                        "A neutral mob found on mountain peaks alongside its nests. While it is a baby, it can be tamed using raw chicken.");

                add("entity.primal.crocodile.description",
                        "A neutral mob found in Swamps and rarely in Deserts. It always spawns near water and stores items from entities it kills in its stomach. They can be retrieved with a feather.");

                add("entity.primal.shark.description",
                        "An aquatic neutral mob found in Oceans. Attacks any entity with low health. It is attracted to conduits.");

                add("entity.primal.cassowary.description",
                        "A neutral mob found in Jungles alongside its nests. Attacks illagers, villagers, and players on sight. Petrified fruit buried in its nests may yield exotic seeds when pecked by it.");

                add("entity.primal.walrus.description",
                        "A neutral mob found in Frozen Oceans and shores that can be tamed by riding. When mounted, it can perform a whirlwind attack in water and a slam on land.");

                add("entity.primal.lion.description",
                        "A neutral mob found in Savannas, with a rare variant in Snowy Plains. If approached slowly, it can be tamed with large amounts of meat. It will maul its prey.");

                add("entity.primal.snake.description",
                        "A neutral mob found in the overworld. Distract it with music to tame it using raw rabbit. Once tamed, it can sit on the owner's neck and assist in combat.");

                add("entity.primal.deer.description",
                        "A passive mob found in the forests that is easily scared by predators, players and loud noises. It is really fast and it can jump two blocks easily.");


                add("fieldguide.name.primal.thorny_acacia_sapling", "Thorny Acacia Tree");
            }
        }
        //Attributes
        {
            add("attribute.variant.primal.reflected_damage", "Reflected Damage");
        }

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

            add("advancements.primal.eagle_attacks_snake.title", "¡Viva México!");
            add("advancements.primal.eagle_attacks_snake.description", "See a golden eagle grabbing a snake. You could build a city here.");

            //Cassowary
            add("advancements.primal.get_cassowary_egg.title", "Green Eggs and Ham");
            add("advancements.primal.get_cassowary_egg.description", "Steal a cassowary egg, beware of the father!");

            add("advancements.primal.give_petrified_fruit.title", "Fruit Flavored");
            add("advancements.primal.give_petrified_fruit.description", "Feed the cassowary a petrified fruit");

            add("advancements.primal.all_exotic_fruits.title", "Niche Fruits");
            add("advancements.primal.all_exotic_fruits.description", "Collect all the exotic fruits");

            //Walrus
            add("advancements.primal.tame_walrus.title", "Blubberous Beast");
            add("advancements.primal.tame_walrus.description", "Tame a Walrus");

            add("advancements.primal.walrus_plays.title", "My Singing Pinniped");
            add("advancements.primal.walrus_plays.description", "Make a walrus play a Conch Shell");

            //Lion
            add("advancements.primal.tame_lion.title", "Hakuna Matata");
            add("advancements.primal.tame_lion.description", "Tame a Lion");

            add("advancements.primal.lion_nap.title", "The Lion Sleeps Tonight");
            add("advancements.primal.lion_nap.description", "Let a lion nap on your lap");

            //Snake
            add("advancements.primal.snake_chest.title", "Solid Snake");
            add("advancements.primal.snake_chest.description", "Get spooked by a snake that's hiding in a chest");

            add("advancements.primal.snake_fill_bottle.title", "Liquid Snake");
            add("advancements.primal.snake_fill_bottle.description", "Make a snake bite into an empty bottle");

            //Deer
            add("advancements.primal.deer_disc.title", "Oh Biscuits!");
            add("advancements.primal.deer_disc.description", "Watch a deer being hit by a lighting-bolt");

            //Misc
            add("advancements.primal.eat_apple_fritter.title", "Double-Glazed");
            add("advancements.primal.eat_apple_fritter.description", "Consume and absorb the nutrients of one apple fritter");

            add("advancements.primal.add_helmet_decoration.title", "Helmet Fashion");
            add("advancements.primal.add_helmet_decoration.description", "Add a helmet decoration to your helmet");

            add("advancements.primal.add_helmet_horns.title", "Dovahkiin");
            add("advancements.primal.add_helmet_horns.description", "Our hero, our hero claims a warrior's heart. I tell you, I tell you the Dragonborn comes.");
        }

        //Tooltips
        {
            add("item.primal.conch_shell.tooltip", "Right click a pet to bind");
            add("item.primal.conch_shell.tooltip.name", "Bound To ");
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
        String effectName=effectKey.location().getPath();
        String effectNameStrong="strong_"+effectKey.location().getPath();
        String effectNameLong="long_"+effectKey.location().getPath();

        List<Pair<String, String>> types=List.of(
                Pair.of("potion", "Potion of "),
                Pair.of( "splash_potion", "Splash Potion of "),
                Pair.of("lingering_potion", "Lingering Potion of "),
                Pair.of("tipped_arrow", "Arrow of "));


        for (Pair<String, String> type: types){
            String idMain = "item.minecraft." +type.getFirst()+ ".effect."+effectName;
            add(idMain, type.getSecond()+translate(effectName));

            String idStrong = "item.minecraft." +type.getFirst()+ ".effect."+effectNameStrong;
            add(idStrong, type.getSecond()+translate(effectName));

            String idLong = "item.minecraft." +type.getFirst()+ ".effect."+effectNameLong;
            add(idLong, type.getSecond()+translate(effectName));
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
