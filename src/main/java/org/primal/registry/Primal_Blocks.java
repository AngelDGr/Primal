package org.primal.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.RegistryObject;
import org.primal.Primal_Registries;
import org.primal.block.*;

import java.util.function.Supplier;

public class Primal_Blocks {

    public static RegistryObject<Block> SHARK_TOOTH;

    public static RegistryObject<Block> CROCODILE_EGG;
    public static RegistryObject<Block> EAGLE_EGG;
    public static RegistryObject<Block> CASSOWARY_EGG;
    public static RegistryObject<Block> NEST_BLOCK;

    public static RegistryObject<Block> CROCODILE_SCUTE_BLOCK;
    public static RegistryObject<Block> CROCODILE_SCUTE_SHINGLE;
    public static RegistryObject<Block> CHISELED_CROCODILE_SCUTE;
    public static RegistryObject<Block> CROCODILE_SCUTE_STAIRS;
    public static RegistryObject<Block> CROCODILE_SCUTE_SLAB;

    public static RegistryObject<Block> ARID_CROCODILE_SCUTE_BLOCK;
    public static RegistryObject<Block> ARID_CROCODILE_SCUTE_SHINGLE;
    public static RegistryObject<Block> ARID_CHISELED_CROCODILE_SCUTE;
    public static RegistryObject<Block> ARID_CROCODILE_SCUTE_STAIRS;
    public static RegistryObject<Block> ARID_CROCODILE_SCUTE_SLAB;

    public static RegistryObject<Block> HUMID_CROCODILE_SCUTE_BLOCK;
    public static RegistryObject<Block> HUMID_CROCODILE_SCUTE_SHINGLE;
    public static RegistryObject<Block> HUMID_CHISELED_CROCODILE_SCUTE;
    public static RegistryObject<Block> HUMID_CROCODILE_SCUTE_STAIRS;
    public static RegistryObject<Block> HUMID_CROCODILE_SCUTE_SLAB;

    public static RegistryObject<Block> CHOMP_TRAP_GREEN;
    public static RegistryObject<Block> CHOMP_TRAP_ARID;
    public static RegistryObject<Block> CHOMP_TRAP_HUMID;

    public static RegistryObject<Block> HOLLOW_OAK_LOG;
    public static RegistryObject<Block> HOLLOW_SPRUCE_LOG;
    public static RegistryObject<Block> HOLLOW_BIRCH_LOG;
    public static RegistryObject<Block> HOLLOW_JUNGLE_LOG;
    public static RegistryObject<Block> HOLLOW_ACACIA_LOG;
    public static RegistryObject<Block> HOLLOW_DARK_OAK_LOG;
    public static RegistryObject<Block> HOLLOW_MANGROVE_LOG;

    public static RegistryObject<Block> STRAW_BALE;
    public static RegistryObject<Block> DRIED_STRAW_BALE;
    public static RegistryObject<Block> WEAVED_STRAW;
    public static RegistryObject<Block> WEAVED_STRAW_STAIRS;
    public static RegistryObject<Block> WEAVED_STRAW_SLAB;
    public static RegistryObject<Block> STRAW_BASKET;

    public static RegistryObject<Block> DREAMCATCHER;

    //Flora
    public static RegistryObject<Block> RIVER_REEDS;
    public static RegistryObject<Block> SHORT_RIVER_REEDS;
    public static RegistryObject<Block> CATTAILS;
    public static RegistryObject<Block> WARM_SEASHELLS;
    public static RegistryObject<Block> COLD_SEASHELLS;
    public static RegistryObject<Block> TEMPERATE_SEASHELLS;

    public static RegistryObject<Block> THORNY_ACACIA_SAPLING;
    public static RegistryObject<Block> THORNY_ACACIA_LOG;
    public static RegistryObject<Block> THORNY_ACACIA_WOOD;
    public static RegistryObject<Block> THORNY_ACACIA_LEAVES;

    //Fruit Trees
    public static RegistryObject<Block> PETRIFIED_FRUIT;

    public static RegistryObject<Block> LITCHI_SAPLING;
    public static RegistryObject<Block> LITCHI_TREE;

    public static RegistryObject<Block> KIWANO_SAPLING;
    public static RegistryObject<Block> KIWANO_BULK;

    public static RegistryObject<Block> STARFRUIT_SAPLING;
    public static RegistryObject<Block> STARFRUIT_TREE;

    public static RegistryObject<Block> FALLOW_DEER_ANTLER;
    public static RegistryObject<Block> REINDEER_ANTLER;
    public static RegistryObject<Block> WHITETAIL_DEER_ANTLER;

    public static void init(){

        //Animal Eggs
        {
            CROCODILE_EGG=register("crocodile_egg",
                    ()-> new CrocodileEgg(BlockBehaviour.Properties.of()
                            .mapColor(MapColor.SAND)
                            .forceSolidOn()
                            .strength(0.5F)
                            .sound(SoundType.METAL)
                            .randomTicks()
                            .noOcclusion()
                            .pushReaction(PushReaction.DESTROY)));

            EAGLE_EGG=register("eagle_egg",
                    ()-> new EagleEgg(BlockBehaviour.Properties.of()
                            .mapColor(MapColor.TERRACOTTA_BROWN)
                            .forceSolidOn()
                            .strength(0.5F)
                            .sound(SoundType.METAL)
                            .randomTicks()
                            .noOcclusion()
                            .pushReaction(PushReaction.DESTROY)));

            CASSOWARY_EGG=register("cassowary_egg",
                    ()-> new CassowaryEgg(BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_LIGHT_GREEN)
                            .strength(0.5F)
                            .sound(SoundType.METAL)
                            .noOcclusion()
                            .randomTicks()));

            NEST_BLOCK=register("nest",
                    ()-> new NestBlock(BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BROWN)
                            .forceSolidOn()
                            .strength(0.5F)
                            .sound(SoundType.MANGROVE_ROOTS)
                            .randomTicks()
                            .noOcclusion()
                            .ignitedByLava()
                            .pushReaction(PushReaction.DESTROY)));
        }

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
                                .pushReaction(PushReaction.NORMAL)
                                .isRedstoneConductor(Primal_Blocks::never)));

        {
            CROCODILE_SCUTE_BLOCK = register("crocodile_scute_block",
                    () -> new Block(BlockBehaviour.Properties.of()
                            .instabreak()
                            .explosionResistance(1200f)
                            .sound(SoundType.SCAFFOLDING)));

            CROCODILE_SCUTE_SHINGLE = register("crocodile_scute_shingle",
                    () -> new Block(BlockBehaviour.Properties.of()
                            .instabreak()
                            .explosionResistance(1200f)
                            .sound(SoundType.SCAFFOLDING)));

            CHISELED_CROCODILE_SCUTE = register("chiseled_crocodile_scute",
                    () -> new Block(BlockBehaviour.Properties.of()
                            .instabreak()
                            .explosionResistance(1200f)
                            .sound(SoundType.SCAFFOLDING)));

            CROCODILE_SCUTE_STAIRS = register("crocodile_scute_stairs",
                    () -> new StairBlock(()-> CROCODILE_SCUTE_BLOCK.get().defaultBlockState(),
                            BlockBehaviour.Properties.of()
                                    .instabreak()
                                    .explosionResistance(1200f)
                                    .sound(SoundType.SCAFFOLDING)));

            CROCODILE_SCUTE_SLAB = register("crocodile_scute_slab",
                    () -> new SlabBlock(BlockBehaviour.Properties.of()
                            .instabreak()
                            .explosionResistance(1200f)
                            .sound(SoundType.SCAFFOLDING)));

            CHOMP_TRAP_GREEN = register("chomp_trap_green",
                    () -> new ChompTrapBlock(BlockBehaviour.Properties.of()
                            .noOcclusion()
                            .strength(0.3f)
                            .isSuffocating(Primal_Blocks::never)
                            .explosionResistance(1200f)
                            .sound(SoundType.SCAFFOLDING), "green"));
        }

        {
            ARID_CROCODILE_SCUTE_BLOCK=register("arid_crocodile_scute_block",
                    ()-> new Block(BlockBehaviour.Properties.of()
                            .instabreak()
                            .explosionResistance(1200f)
                            .sound(SoundType.SCAFFOLDING)));

            ARID_CROCODILE_SCUTE_SHINGLE=register("arid_crocodile_scute_shingle",
                    ()-> new Block(BlockBehaviour.Properties.of()
                            .instabreak()
                            .explosionResistance(1200f)
                            .sound(SoundType.SCAFFOLDING)));

            ARID_CHISELED_CROCODILE_SCUTE =register("arid_chiseled_crocodile_scute",
                    ()-> new Block(BlockBehaviour.Properties.of()
                            .instabreak()
                            .explosionResistance(1200f)
                            .sound(SoundType.SCAFFOLDING)));

            ARID_CROCODILE_SCUTE_STAIRS=register("arid_crocodile_scute_stairs",
                    ()-> new StairBlock(()-> ARID_CROCODILE_SCUTE_BLOCK.get().defaultBlockState(),
                            BlockBehaviour.Properties.of()
                                    .instabreak()
                                    .explosionResistance(1200f)
                                    .sound(SoundType.SCAFFOLDING)));

            ARID_CROCODILE_SCUTE_SLAB=register("arid_crocodile_scute_slab",
                    ()-> new SlabBlock(BlockBehaviour.Properties.of()
                            .instabreak()
                            .explosionResistance(1200f)
                            .sound(SoundType.SCAFFOLDING)));

            CHOMP_TRAP_ARID = register("chomp_trap_arid",
                    () -> new ChompTrapBlock(BlockBehaviour.Properties.of()
                            .noOcclusion()
                            .strength(0.3f)
                            .isSuffocating(Primal_Blocks::never)
                            .explosionResistance(1200f)
                            .sound(SoundType.SCAFFOLDING), "arid"));
        }

        {
            HUMID_CROCODILE_SCUTE_BLOCK = register("humid_crocodile_scute_block",
                    () -> new Block(BlockBehaviour.Properties.of()
                            .instabreak()
                            .explosionResistance(1200f)
                            .sound(SoundType.SCAFFOLDING)));

            HUMID_CROCODILE_SCUTE_SHINGLE = register("humid_crocodile_scute_shingle",
                    () -> new Block(BlockBehaviour.Properties.of()
                            .instabreak()
                            .explosionResistance(1200f)
                            .sound(SoundType.SCAFFOLDING)));

            HUMID_CHISELED_CROCODILE_SCUTE = register("humid_chiseled_crocodile_scute",
                    () -> new Block(BlockBehaviour.Properties.of()
                            .instabreak()
                            .explosionResistance(1200f)
                            .sound(SoundType.SCAFFOLDING)));

            HUMID_CROCODILE_SCUTE_STAIRS = register("humid_crocodile_scute_stairs",
                    () -> new StairBlock(()-> HUMID_CROCODILE_SCUTE_BLOCK.get().defaultBlockState(),
                            BlockBehaviour.Properties.of()
                                    .instabreak()
                                    .explosionResistance(1200f)
                                    .sound(SoundType.SCAFFOLDING)));

            HUMID_CROCODILE_SCUTE_SLAB = register("humid_crocodile_scute_slab",
                    () -> new SlabBlock(BlockBehaviour.Properties.of()
                            .instabreak()
                            .explosionResistance(1200f)
                            .sound(SoundType.SCAFFOLDING)));

            CHOMP_TRAP_HUMID = register("chomp_trap_humid",
                    () -> new ChompTrapBlock(BlockBehaviour.Properties.of()
                            .noOcclusion()
                            .strength(0.3f)
                            .isSuffocating(Primal_Blocks::never)
                            .explosionResistance(1200f)
                            .sound(SoundType.SCAFFOLDING), "humid"));
        }

        RIVER_REEDS=register("river_reeds",
                ()-> new RiverReedBlock(BlockBehaviour.Properties.of()
                        .mapColor(MapColor.PLANT)
                        .noCollission()
                        .instabreak()
                        .sound(SoundType.GRASS)
                        .offsetType(BlockBehaviour.OffsetType.XZ)
                        .ignitedByLava()
                        .pushReaction(PushReaction.DESTROY)));

        CATTAILS=register("cattails",
                ()-> new CattailsBlock(BlockBehaviour.Properties.of()
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

        WARM_SEASHELLS =register("seashells",
                ()-> new SeashellsBlock(BlockBehaviour.Properties.of()
                        .mapColor(MapColor.TERRACOTTA_WHITE)
                        .noCollission()
                        .instabreak()
                        .randomTicks()
                        .sound(SoundType.METAL)
                        .pushReaction(PushReaction.DESTROY)));

        COLD_SEASHELLS=register("cold_seashells",
                ()-> new SeashellsBlock(BlockBehaviour.Properties.of()
                        .mapColor(MapColor.TERRACOTTA_WHITE)
                        .noCollission()
                        .instabreak()
                        .randomTicks()
                        .sound(SoundType.METAL)
                        .pushReaction(PushReaction.DESTROY)));

        TEMPERATE_SEASHELLS=register("temperate_seashells",
                ()-> new SeashellsBlock(BlockBehaviour.Properties.of()
                        .mapColor(MapColor.TERRACOTTA_WHITE)
                        .noCollission()
                        .instabreak()
                        .randomTicks()
                        .sound(SoundType.METAL)
                        .pushReaction(PushReaction.DESTROY)));

        FALLOW_DEER_ANTLER =register("fallow_deer_antler",
                ()-> new DeerAntlerBlock(BlockBehaviour.Properties.of()
                        .mapColor(MapColor.TERRACOTTA_WHITE)
                        .forceSolidOn()
                        .noOcclusion()
                        .sound(SoundType.BONE_BLOCK)
                        .strength(0.25F)
                        .dynamicShape()));

        REINDEER_ANTLER =register("reindeer_antler",
                ()-> new DeerAntlerBlock(BlockBehaviour.Properties.of()
                        .mapColor(MapColor.ICE)
                        .forceSolidOn()
                        .noOcclusion()
                        .sound(SoundType.BONE_BLOCK)
                        .strength(0.25F)
                        .dynamicShape()));

        WHITETAIL_DEER_ANTLER =register("whitetail_deer_antler",
                ()-> new DeerAntlerBlock(BlockBehaviour.Properties.of()
                        .mapColor(MapColor.SAND)
                        .forceSolidOn()
                        .noOcclusion()
                        .sound(SoundType.BONE_BLOCK)
                        .strength(0.25F)
                        .dynamicShape()));

        DREAMCATCHER = register("dreamcatcher",
                () -> new DreamcatcherBlock(BlockBehaviour.Properties.of()
                        .noOcclusion()
                        .instabreak()
                        .noCollission()
                        .sound(SoundType.BONE_BLOCK)));

        //Thorny Acacia
        {

            THORNY_ACACIA_LOG = register("thorny_acacia_log",
                    () -> new ThornyAcaciaLog(
                            BlockBehaviour.Properties.of()
                                    .mapColor(state -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.COLOR_ORANGE : MapColor.STONE)
                                    .instrument(NoteBlockInstrument.BASS)
                                    .strength(2.0F)
                                    .sound(SoundType.WOOD)
                                    .ignitedByLava(), false
                    ));

            THORNY_ACACIA_WOOD = register(
                    "thorny_acacia_wood",
                    () -> new ThornyAcaciaLog(
                            BlockBehaviour.Properties.of()
                                    .mapColor(MapColor.COLOR_GRAY)
                                    .instrument(NoteBlockInstrument.BASS)
                                    .strength(2.0F)
                                    .sound(SoundType.WOOD)
                                    .ignitedByLava(), true
                    )
            );

            THORNY_ACACIA_LEAVES = register("thorny_acacia_leaves",
                    () -> new LeavesBlock(
                            BlockBehaviour.Properties.of()
                                    .mapColor(MapColor.PLANT)
                                    .strength(0.2F)
                                    .randomTicks()
                                    .sound(SoundType.GRASS)
                                    .noOcclusion()
                                    .isValidSpawn(Primal_Blocks::ocelotOrParrot)
                                    .isSuffocating(Primal_Blocks::never)
                                    .isViewBlocking(Primal_Blocks::never)
                                    .ignitedByLava()
                                    .pushReaction(PushReaction.DESTROY)
                                    .isRedstoneConductor(Primal_Blocks::never)
                    ));

            THORNY_ACACIA_SAPLING = register(
                    "thorny_acacia_sapling",
                    () -> new SaplingBlock(
                            Primal_TreeGrower.THORNY_ACACIA,
                            BlockBehaviour.Properties.of()
                                    .mapColor(MapColor.PLANT)
                                    .noCollission()
                                    .randomTicks()
                                    .instabreak()
                                    .sound(SoundType.GRASS)
                                    .pushReaction(PushReaction.DESTROY)
                    )
            );
        }

        STRAW_BALE =register("straw_bale",
                ()-> new StrawBlock(BlockBehaviour.Properties.of()
                        .mapColor(MapColor.COLOR_LIGHT_GREEN)
                        .instrument(NoteBlockInstrument.BANJO)
                        .strength(0.3F)
                        .sound(SoundType.GRASS)
                        .ignitedByLava()));

        DRIED_STRAW_BALE =register("dried_straw_bale",
                ()-> new StrawBlock(BlockBehaviour.Properties.of()
                        .mapColor(MapColor.TERRACOTTA_YELLOW)
                        .instrument(NoteBlockInstrument.BANJO)
                        .strength(0.3F)
                        .sound(SoundType.GRASS)
                        .ignitedByLava()));

        WEAVED_STRAW =register("weaved_straw",
                ()-> new StrawBlock(BlockBehaviour.Properties.of()
                        .mapColor(MapColor.TERRACOTTA_YELLOW)
                        .instrument(NoteBlockInstrument.BANJO)
                        .strength(0.3F)
                        .sound(SoundType.GRASS)
                        .ignitedByLava()));

        WEAVED_STRAW_STAIRS = register("weaved_straw_stairs",
                () -> new StairBlock(()-> WEAVED_STRAW.get().defaultBlockState(),
                        BlockBehaviour.Properties.of()
                        .mapColor(MapColor.TERRACOTTA_YELLOW)
                        .instrument(NoteBlockInstrument.BANJO)
                        .strength(0.3F)
                        .sound(SoundType.GRASS)
                        .ignitedByLava()));

        WEAVED_STRAW_SLAB = register("weaved_straw_slab",
                () -> new SlabBlock(BlockBehaviour.Properties.of()
                        .mapColor(MapColor.TERRACOTTA_YELLOW)
                        .instrument(NoteBlockInstrument.BANJO)
                        .strength(0.3F)
                        .sound(SoundType.GRASS)
                        .ignitedByLava()));

        STRAW_BASKET = register("straw_basket",
                () -> new StrawBasketBlock(BlockBehaviour.Properties.of()
                        .mapColor(MapColor.TERRACOTTA_YELLOW)
                        .instrument(NoteBlockInstrument.BANJO)
                        .strength(0.3F)
                        .sound(SoundType.GRASS)
                        .ignitedByLava(),
                        () -> Primal_BlockEntities.STRAW_BASKET.get()));

        //Fruit Trees
        {
            PETRIFIED_FRUIT =register("petrified_fruit",
                    ()-> new PetrifiedFruitBlock(BlockBehaviour.Properties.of()
                            .sound(SoundType.STONE)
                            .mapColor(MapColor.STONE)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .strength(2.0F, 6.0F)
                            .pushReaction(PushReaction.DESTROY)));

            LITCHI_SAPLING =register("litchi_sapling",
                    ()-> new FruitSapling(BlockBehaviour.Properties.of()
                            .randomTicks()
                            .instabreak()
                            .noCollission()
                            .sound(SoundType.BAMBOO_SAPLING)
                            .ignitedByLava()
                            .pushReaction(PushReaction.DESTROY),
                            Primal_Items.LITCHI_SEEDS,
                            Primal_Blocks.LITCHI_TREE));

            LITCHI_TREE = register("litchi_tree", ()-> new FruitTree(BlockBehaviour.Properties.of()
                    .randomTicks()
                    .noCollission()
                    .sound(SoundType.BAMBOO_SAPLING)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY),
                    Primal_Items.LITCHI,
                    Primal_Items.LITCHI_SEEDS));

            KIWANO_SAPLING =register("kiwano_sapling",
                    ()-> new FruitSapling(BlockBehaviour.Properties.of()
                            .randomTicks()
                            .instabreak()
                            .noCollission()
                            .sound(SoundType.BAMBOO_SAPLING)
                            .ignitedByLava()
                            .pushReaction(PushReaction.DESTROY),
                            Primal_Items.KIWANO_SEEDS,
                            Primal_Blocks.KIWANO_BULK));

            KIWANO_BULK = register("kiwano_bulk", ()-> new FruitBulk(BlockBehaviour.Properties.of()
                    .randomTicks()
                    .noCollission()
                    .strength(1.0F)
                    .sound(SoundType.BAMBOO_SAPLING)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY),
                    Primal_Items.KIWANO_SEEDS));

            STARFRUIT_SAPLING =register("starfruit_sapling",
                    ()-> new FruitSapling(BlockBehaviour.Properties.of()
                            .randomTicks()
                            .instabreak()
                            .noCollission()
                            .sound(SoundType.BAMBOO_SAPLING)
                            .ignitedByLava()
                            .pushReaction(PushReaction.DESTROY),
                            Primal_Items.STARFRUIT_SEEDS,
                            Primal_Blocks.STARFRUIT_TREE));

            STARFRUIT_TREE = register("starfruit_tree", ()-> new FruitTree(BlockBehaviour.Properties.of()
                    .randomTicks()
                    .noCollission()
                    .sound(SoundType.BAMBOO_SAPLING)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY),
                    Primal_Items.STARFRUIT,
                    Primal_Items.STARFRUIT_SEEDS));
        }

        HOLLOW_OAK_LOG = register("hollow_oak_log",
                () -> new HollowLogBlock(
                        BlockBehaviour.Properties.copy(Blocks.OAK_LOG)
                                .strength(4.0F)));

        HOLLOW_SPRUCE_LOG = register("hollow_spruce_log",
                () -> new HollowLogBlock(
                        BlockBehaviour.Properties.copy(Blocks.SPRUCE_LOG)
                                .strength(4.0F)));

        HOLLOW_BIRCH_LOG = register("hollow_birch_log",
                () -> new HollowLogBlock(
                        BlockBehaviour.Properties.copy(Blocks.BIRCH_LOG)
                                .strength(4.0F)));

        HOLLOW_JUNGLE_LOG = register("hollow_jungle_log",
                () -> new HollowLogBlock(
                        BlockBehaviour.Properties.copy(Blocks.JUNGLE_LOG)
                                .strength(4.0F)));

        HOLLOW_ACACIA_LOG = register("hollow_acacia_log",
                () -> new HollowLogBlock(
                        BlockBehaviour.Properties.copy(Blocks.ACACIA_LOG)
                                .strength(4.0F)));

        HOLLOW_DARK_OAK_LOG = register("hollow_dark_oak_log",
                () -> new HollowLogBlock(
                        BlockBehaviour.Properties.copy(Blocks.DARK_OAK_LOG)
                                .strength(4.0F)));

        HOLLOW_MANGROVE_LOG = register("hollow_mangrove_log",
                () -> new HollowLogBlock(
                        BlockBehaviour.Properties.copy(Blocks.MANGROVE_LOG)
                                .strength(4.0F)));
    }

    private static Boolean ocelotOrParrot(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType) {
        return entityType == EntityType.OCELOT || entityType == EntityType.PARROT;
    }


    public static RegistryObject<Block> register(final String name, final Supplier<Block> block) {
        return Primal_Registries.BLOCKS.register(name, block);
    }

    private static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return false;
    }
}
