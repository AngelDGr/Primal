package org.primal.datagen.providers.tag;

import java.util.concurrent.CompletableFuture;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.Primal_Main;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.primal.registry.Primal_Entities;
import org.primal.registry.Primal_Tags;
import org.primal.util.Primal_Util;

@SuppressWarnings("unused")
public final class Primal_EntityTagGenerator extends EntityTypeTagsProvider {

    public Primal_EntityTagGenerator(PackOutput output, CompletableFuture<Provider> provider,
                                     @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, Primal_Main.MOD_ID, existingFileHelper);
    }

    //No Man's Land Mobs
    ResourceLocation firebomb = Primal_Util.nomanslandLoc("firebomb");
    ResourceLocation ink_bomb = Primal_Util.nomanslandLoc("ink_bomb");
    ResourceLocation explosive = Primal_Util.nomanslandLoc("explosive");
    ResourceLocation living_urn = Primal_Util.nomanslandLoc("living_urn");
    ResourceLocation incendiary_arrow = Primal_Util.nomanslandLoc("incendiary_arrow");
    ResourceLocation ember = Primal_Util.nomanslandLoc("ember");
    ResourceLocation lingering_cloud = Primal_Util.nomanslandLoc("lingering_cloud");
    ResourceLocation ink_cloud = Primal_Util.nomanslandLoc("ink_cloud");
    ResourceLocation pacified_cloud = Primal_Util.nomanslandLoc("pacified_cloud");

    ResourceLocation billhook_bass = Primal_Util.nomanslandLoc("billhook_bass");
    ResourceLocation deer = Primal_Util.nomanslandLoc("deer");
    ResourceLocation goose = Primal_Util.nomanslandLoc("goose");
    // ResourceLocation buried = MiscUtil.nomanslandLoc("buried"); // if you uncomment the BURIED entity
    ResourceLocation moose = Primal_Util.nomanslandLoc("moose");
    ResourceLocation tortoise = Primal_Util.nomanslandLoc("tortoise");

    @Override
    protected void addTags(@NotNull Provider provider) {
        this.tag(Primal_Tags.Entity.NEVER_ATTACK)
                .add(EntityType.CREEPER);

        this.tag(Primal_Tags.Entity.BEAR_HUNTABLE)
                .add(
                        EntityType.PLAYER,
                        EntityType.VILLAGER,
                        EntityType.PILLAGER,
                        EntityType.VINDICATOR,
                        EntityType.PIG,
                        EntityType.SHEEP,
                        EntityType.CHICKEN,
                        EntityType.COW,
                        EntityType.HORSE,
                        EntityType.DONKEY,
                        EntityType.LLAMA,
                        EntityType.RABBIT,
                        EntityType.SALMON,
                        Primal_Entities.DEER.get())
                .addOptional(goose)
                .addOptional(deer)
                .addOptional(moose);

        this.tag(Primal_Tags.Entity.SHARK_HUNTABLE)
                .add(
                        EntityType.SQUID,
                        EntityType.GLOW_SQUID);

        this.tag(Primal_Tags.Entity.CROCODILE_HUNTABLE)
                .add(
                        //Nether
                        EntityType.PIGLIN,
                        EntityType.PIGLIN_BRUTE,
                        EntityType.HOGLIN,

                        //Overworld
                        EntityType.AXOLOTL,
                        EntityType.CAMEL,
                        EntityType.CAT,
                        EntityType.CHICKEN,

                        EntityType.COW,
                        EntityType.DONKEY,
                        EntityType.FROG,
                        EntityType.GLOW_SQUID,
                        EntityType.HORSE,
                        EntityType.MOOSHROOM,
                        EntityType.MULE,
                        EntityType.OCELOT,
                        EntityType.PARROT,
                        EntityType.PIG,
                        EntityType.RABBIT,
                        EntityType.SHEEP,
                        EntityType.SNIFFER,
                        EntityType.SQUID,
                        EntityType.TURTLE,
                        EntityType.DOLPHIN,
                        EntityType.FOX,
                        EntityType.GOAT,
                        EntityType.LLAMA,
                        EntityType.PANDA,
                        EntityType.TRADER_LLAMA,
                        EntityType.WOLF,
                        Primal_Entities.DEER.get())
                .addOptional(deer)
                .addOptional(goose)
                .addOptional(moose)
                .addOptional(tortoise);

        this.tag(Primal_Tags.Entity.CROCODILE_NEVER_ATTACK)
                .add(EntityType.TROPICAL_FISH,
                        EntityType.SALMON,
                        EntityType.COD)
                .addOptional(billhook_bass);

        this.tag(Primal_Tags.Entity.EAGLE_HUNTABLE)
                .add(
                        EntityType.CHICKEN,
                        EntityType.RABBIT,
                        EntityType.SALMON,
                        EntityType.COD,
                        Primal_Entities.SNAKE.get())
                .addOptional(billhook_bass);

        this.tag(Primal_Tags.Entity.CASSOWARY_HUNTABLE)
                .add(EntityType.PLAYER)
                .add(EntityType.VILLAGER, EntityType.WANDERING_TRADER)
                .addTag(EntityTypeTags.RAIDERS)
                .add(Primal_Entities.SNAKE.get());

        this.tag(Primal_Tags.Entity.LION_HUNTABLE)
                .add(
                        EntityType.VILLAGER,
                        EntityType.PILLAGER,
                        EntityType.VINDICATOR,
                        EntityType.PIG,
                        EntityType.SHEEP,
                        EntityType.CHICKEN,
                        EntityType.COW,
                        EntityType.HORSE,
                        EntityType.DONKEY,
                        EntityType.LLAMA,
                        EntityType.RABBIT,
                        Primal_Entities.DEER.get())
                .addOptional(goose)
                .addOptional(deer);

        this.tag(Primal_Tags.Entity.SNAKE_HUNTABLE)
                .add(
                        EntityType.CHICKEN,
                        EntityType.RABBIT);

        this.tag(Primal_Tags.Entity.DEER_SCARED)
                .add(EntityType.VILLAGER, EntityType.PLAYER)
                .addTag(EntityTypeTags.RAIDERS)
                .addTag(Primal_Tags.Entity.DEER_VERY_SCARED);

        this.tag(Primal_Tags.Entity.DEER_VERY_SCARED)
                .add(Primal_Entities.BEAR.get(), Primal_Entities.LION.get(), Primal_Entities.CROCODILE.get(), EntityType.WOLF);

        this.tag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)
                .add(Primal_Entities.WALRUS.get());

        this.tag(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS)
                .add(Primal_Entities.DEER.get());
    }
}
