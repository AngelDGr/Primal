package org.primal.datagen.providers.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.Primal_Main;
import org.primal.registry.Primal_Blocks;
import org.primal.registry.Primal_Tags;

import java.util.concurrent.CompletableFuture;

public class Primal_BlockTagsGenerator extends BlockTagsProvider {

    public Primal_BlockTagsGenerator(final PackOutput output, final CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable final ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Primal_Main.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(final HolderLookup.@NotNull Provider lookup) {
        this.tag(Primal_Tags.Block.BEAR_REPELLENTS)
                .addTag(BlockTags.CAMPFIRES);

        this.tag(Primal_Tags.Block.SHARK_ATTRACTORS)
                .add(Blocks.CONDUIT);

        this.tag(Primal_Tags.Block.CROCODILE_ATTRACTORS)
                .add(Primal_Blocks.RIVER_REEDS.get())
                .add(Primal_Blocks.SHORT_RIVER_REEDS.get())
                .add(Primal_Blocks.CATTAILS.get());

        this.tag(Primal_Tags.Block.IS_ANIMAL_EGG)
                .add(Primal_Blocks.CROCODILE_EGG.get())
                .add(Primal_Blocks.EAGLE_EGG.get())
                .add(Primal_Blocks.CASSOWARY_EGG.get())
                .add(Blocks.TURTLE_EGG)
                .add(Blocks.SNIFFER_EGG)
                .add(Blocks.DRAGON_EGG)
                .addOptional(ResourceLocation.fromNamespaceAndPath("nomansland", "tortoise_egg"));

        this.tag(Primal_Tags.Block.RIVER_REED_SOIL)
                .add(Blocks.GRAVEL)
                .add(Blocks.SUSPICIOUS_GRAVEL)
                .add(Blocks.SAND)
                .add(Blocks.SUSPICIOUS_SAND)
                .add(Blocks.RED_SAND)
                .add(Blocks.CLAY);

        this.tag(Primal_Tags.Block.FRUIT_TREE_PLANTABLE_ON)
                .addTag(BlockTags.DIRT)
                .add(Blocks.DECORATED_POT)
                .addTag(Primal_Tags.Block.FRUIT_TREE_GROW_FAST_ON);

        this.tag(Primal_Tags.Block.FRUIT_TREE_GROW_FAST_ON)
                .add(Blocks.FARMLAND);

        this.tag(Primal_Tags.Block.CASSOWARY_NEST_CAN_SPAWN_ON)
                .addTag(BlockTags.DIRT)
                .add(Blocks.SUSPICIOUS_GRAVEL)
                .add(Blocks.MUD);

        this.tag(BlockTags.MINEABLE_WITH_AXE)
                .add(Primal_Blocks.STARFRUIT_TREE.get())
                .add(Primal_Blocks.LITCHI_TREE.get())
                .add(Primal_Blocks.KIWANO_BULK.get())
                .addTag(Primal_Tags.Block.HOLLOW_LOGS);

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(Primal_Blocks.PETRIFIED_FRUIT.get())
                .add(Primal_Blocks.CHOMP_TRAP_GREEN.get())
                .add(Primal_Blocks.CHOMP_TRAP_ARID.get())
                .add(Primal_Blocks.CHOMP_TRAP_HUMID.get());

        this.tag(Primal_Tags.Block.WALRUS_SPAWN_ON)
                .addTag(BlockTags.ICE)
                .addTag(BlockTags.STONE_ORE_REPLACEABLES)
                .addTag(BlockTags.SAND)
                .addTag(BlockTags.SNOW);

        this.tag(Primal_Tags.Block.WALRUS_SLAM_CAN_BREAK)
                .add(Blocks.ICE)
                .addTag(BlockTags.LEAVES);

        this.tag(Primal_Tags.Block.SNAKE_SPAWN_ON)
                .add(Blocks.GRASS_BLOCK)
                .addTag(BlockTags.DIRT)
                .addTag(BlockTags.SAND);

        this.tag(Primal_Tags.Block.DEER_SPAWN_ON)
                .add(Blocks.GRASS_BLOCK)
                .addTag(BlockTags.SNOW);

        this.tag(Primal_Tags.Block.LION_SPAWN_ON)
                .add(Blocks.GRASS_BLOCK)
                .add(Blocks.COARSE_DIRT)
                .addTag(BlockTags.SNOW);

        this.tag(BlockTags.ACACIA_LOGS)
                .add(Primal_Blocks.THORNY_ACACIA_LOG.get())
                .add(Primal_Blocks.THORNY_ACACIA_WOOD.get());

        this.tag(BlockTags.LEAVES)
                .add(Primal_Blocks.THORNY_ACACIA_LEAVES.get());

        this.tag(BlockTags.MINEABLE_WITH_HOE)
                .add(Primal_Blocks.STRAW_BALE.get())
                .add(Primal_Blocks.DRIED_STRAW_BALE.get())
                .add(Primal_Blocks.WEAVED_STRAW.get())
                .add(Primal_Blocks.WEAVED_STRAW_STAIRS.get())
                .add(Primal_Blocks.WEAVED_STRAW_SLAB.get())
                .add(Primal_Blocks.STRAW_BASKET.get())
                .add(Primal_Blocks.THORNY_ACACIA_LEAVES.get());

        this.tag(BlockTags.SNAPS_GOAT_HORN)
                .add(Primal_Blocks.THORNY_ACACIA_LOG.get());

        this.tag(BlockTags.OVERWORLD_NATURAL_LOGS)
                .add(Primal_Blocks.THORNY_ACACIA_LOG.get());

        this.tag(BlockTags.SAPLINGS)
                .add(Primal_Blocks.THORNY_ACACIA_SAPLING.get());

        this.tag(BlockTags.MAINTAINS_FARMLAND)
                .add(Primal_Blocks.STARFRUIT_SAPLING.get())
                .add(Primal_Blocks.KIWANO_SAPLING.get())
                .add(Primal_Blocks.LITCHI_SAPLING.get());

        this.tag(BlockTags.CROPS)
                .add(Primal_Blocks.STARFRUIT_SAPLING.get())
                .add(Primal_Blocks.KIWANO_SAPLING.get())
                .add(Primal_Blocks.LITCHI_SAPLING.get());

        this.tag(Primal_Tags.Block.CREATES_WIDE_SMOKE)
                .add(Primal_Blocks.STRAW_BALE.get())
                .add(Primal_Blocks.DRIED_STRAW_BALE.get());

        this.tag(Primal_Tags.Block.HOLLOW_LOGS)
                .add(Primal_Blocks.HOLLOW_OAK_LOG.get())
                .add(Primal_Blocks.HOLLOW_SPRUCE_LOG.get())
                .add(Primal_Blocks.HOLLOW_BIRCH_LOG.get())
                .add(Primal_Blocks.HOLLOW_JUNGLE_LOG.get())
                .add(Primal_Blocks.HOLLOW_ACACIA_LOG.get())
                .add(Primal_Blocks.HOLLOW_DARK_OAK_LOG.get())
                .add(Primal_Blocks.HOLLOW_MANGROVE_LOG.get());

        this.tag(Primal_Tags.Block.NEVER_OBSTRUCT_NEST)
                .add(Blocks.VINE)
                .addTag(BlockTags.CAVE_VINES)
                .addTag(BlockTags.TALL_FLOWERS);

        this.tag(BlockTags.LOGS)
                .addTag(Primal_Tags.Block.HOLLOW_LOGS);
    }
}