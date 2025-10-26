package org.primal.datagen.providers;

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
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.primal.registry.Primal_Entities;
import org.primal.registry.Primal_Tags;
import org.primal.util.MiscUtil;

public final class Primal_EntityTagGenerator extends EntityTypeTagsProvider {

    public Primal_EntityTagGenerator(PackOutput output, CompletableFuture<Provider> provider,
                                     @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, Primal_Main.MOD_ID, existingFileHelper);
    }

    //No Man's Land Mobs
    ResourceLocation firebomb = MiscUtil.nomanslandLoc("firebomb");
    ResourceLocation ink_bomb = MiscUtil.nomanslandLoc("ink_bomb");
    ResourceLocation explosive = MiscUtil.nomanslandLoc("explosive");
    ResourceLocation living_urn = MiscUtil.nomanslandLoc("living_urn");
    ResourceLocation incendiary_arrow = MiscUtil.nomanslandLoc("incendiary_arrow");
    ResourceLocation ember = MiscUtil.nomanslandLoc("ember");
    ResourceLocation lingering_cloud = MiscUtil.nomanslandLoc("lingering_cloud");
    ResourceLocation ink_cloud = MiscUtil.nomanslandLoc("ink_cloud");
    ResourceLocation pacified_cloud = MiscUtil.nomanslandLoc("pacified_cloud");

    ResourceLocation billhook_bass = MiscUtil.nomanslandLoc("billhook_bass");
    ResourceLocation deer = MiscUtil.nomanslandLoc("deer");
    ResourceLocation goose = MiscUtil.nomanslandLoc("goose");
    // ResourceLocation buried = MiscUtil.nomanslandLoc("buried"); // if you uncomment the BURIED entity
    ResourceLocation moose = MiscUtil.nomanslandLoc("moose");
    ResourceLocation tortoise = MiscUtil.nomanslandLoc("tortoise");

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
                        EntityType.RABBIT)
                .addOptional(goose)
                .addOptional(deer)
                .addOptional(moose);

        this.tag(Primal_Tags.Entity.SHARK_HUNTABLE)
                .add(
                        EntityType.SQUID,
                        EntityType.GLOW_SQUID);

        this.tag(EntityTypeTags.AQUATIC)
                .add(Primal_Entities.SHARK.get());

        this.tag(EntityTypeTags.CAN_BREATHE_UNDER_WATER)
                .add(Primal_Entities.SHARK.get());

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
                        EntityType.WOLF)
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
                        EntityType.COD)
                .addOptional(billhook_bass);
    }
}
