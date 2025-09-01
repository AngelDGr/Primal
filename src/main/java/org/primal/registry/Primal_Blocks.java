package org.primal.registry;


import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.primal.Primal_Registries;
import org.primal.block.*;

import java.util.function.Supplier;

public class Primal_Blocks {

    public static DeferredHolder<Block, Block> SHARK_TOOTH;

    public static DeferredHolder<Block, Block> CROCODILE_SCUTE_BLOCK;
    public static DeferredHolder<Block, Block> CROCODILE_SCUTE_SHINGLE;
    public static DeferredHolder<Block, Block> CHISELED_CROCODILE_SCUTE;
    public static DeferredHolder<Block, Block> CROCODILE_SCUTE_STAIRS;
    public static DeferredHolder<Block, Block> CROCODILE_SCUTE_SLAB;
    public static DeferredHolder<Block, Block> CROCODILE_EGG;

    //Flora
    public static DeferredHolder<Block, Block> RIVER_REEDS;
    public static DeferredHolder<Block, Block> SHORT_RIVER_REEDS;
    public static DeferredHolder<Block, Block> SEASHELLS;

    public static void init(){
        SHARK_TOOTH=register("shark_tooth",
                ()-> new SharkToothBlock(BlockBehaviour.Properties.of()
                                .mapColor(MapColor.TERRACOTTA_WHITE)
                                .forceSolidOn()
                                .instrument(NoteBlockInstrument.BASEDRUM)
                                .noOcclusion()
                                .sound(SoundType.BONE_BLOCK)
                                .randomTicks()
                                .strength(1.0F)
                                .instabreak()
                                .dynamicShape()
                                .pushReaction(PushReaction.DESTROY)
                                .isRedstoneConductor(Primal_Blocks::never)));

        CROCODILE_SCUTE_BLOCK=register("crocodile_scute_block",
                ()-> new Block(BlockBehaviour.Properties.of()
                        .instabreak()
                        .explosionResistance(1200f)
                        .sound(SoundType.SCAFFOLDING)));

        CROCODILE_SCUTE_SHINGLE=register("crocodile_scute_shingle",
                ()-> new Block(BlockBehaviour.Properties.of()
                        .instabreak()
                        .explosionResistance(1200f)
                        .sound(SoundType.SCAFFOLDING)));

        CHISELED_CROCODILE_SCUTE =register("chiseled_crocodile_scute",
                ()-> new Block(BlockBehaviour.Properties.of()
                        .instabreak()
                        .explosionResistance(1200f)
                        .sound(SoundType.SCAFFOLDING)));

        CROCODILE_SCUTE_STAIRS=register("crocodile_scute_stairs",
                ()-> new StairBlock(CROCODILE_SCUTE_BLOCK.get().defaultBlockState(),
                        BlockBehaviour.Properties.of()
                        .instabreak()
                        .explosionResistance(1200f)
                        .sound(SoundType.SCAFFOLDING)));

        CROCODILE_SCUTE_SLAB=register("crocodile_scute_slab",
                ()-> new SlabBlock(BlockBehaviour.Properties.of()
                                .instabreak()
                                .explosionResistance(1200f)
                                .sound(SoundType.SCAFFOLDING)));

        CROCODILE_EGG=register("crocodile_egg",
                ()-> new CrocodileEgg(BlockBehaviour.Properties.of()
                        .mapColor(MapColor.SAND)
                        .forceSolidOn()
                        .strength(0.5F)
                        .sound(SoundType.METAL)
                        .randomTicks()
                        .noOcclusion()
                        .pushReaction(PushReaction.DESTROY)));

        RIVER_REEDS=register("river_reeds",
                ()-> new RiverReeds(BlockBehaviour.Properties.of()
                        .mapColor(MapColor.PLANT)
                        .noCollission()
                        .instabreak()
                        .sound(SoundType.GRASS)
                        .offsetType(BlockBehaviour.OffsetType.XZ)
                        .ignitedByLava()
                        .pushReaction(PushReaction.DESTROY)));

        SHORT_RIVER_REEDS=register("short_river_reeds",
                ()-> new ShortRiverReeds(BlockBehaviour.Properties.of()
                        .mapColor(MapColor.PLANT)
                        .noCollission()
                        .instabreak()
                        .sound(SoundType.GRASS)
                        .offsetType(BlockBehaviour.OffsetType.XZ)
                        .ignitedByLava()
                        .pushReaction(PushReaction.DESTROY)));

        SEASHELLS=register("seashells",
                ()-> new SeashellsBlock(BlockBehaviour.Properties.of()
                        .mapColor(MapColor.TERRACOTTA_WHITE)
                        .noCollission()
                        .instabreak()
                        .sound(SoundType.METAL)
                        .ignitedByLava()
                        .pushReaction(PushReaction.DESTROY)));
    }


    public static DeferredHolder<Block, Block> register(final String name, final Supplier<Block> block) {
        return Primal_Registries.BLOCKS.register(name, block);
    }

    private static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return false;
    }
}
