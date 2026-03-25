package org.primal.datagen.providers;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.primal.Primal_Main;
import org.primal.block.*;
import org.primal.block.properties.SharkToothThickness;
import org.primal.block.properties.TripleBlockHalf;
import org.primal.registry.Primal_Blocks;
import org.primal.util.block_types.AnimalEgg;
import org.primal.util.Primal_Util;

public class Primal_BlockStateGenerator extends BlockStateProvider {

    public Primal_BlockStateGenerator(final PackOutput output, final ExistingFileHelper existingFileHelper) {
        super(output, Primal_Main.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        this.createSharkTooth(Primal_Blocks.SHARK_TOOTH,
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/shark_tooth_tip"),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/shark_tooth_base"));

        this.createSeashells(Primal_Blocks.WARM_SEASHELLS, SeashellsBlock.FACING, SeashellsBlock.AMOUNT,
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/seashells_1"),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/seashells_2"),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/seashells_3"),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/seashells_4"));

        this.createSeashells(Primal_Blocks.COLD_SEASHELLS, SeashellsBlock.FACING, SeashellsBlock.AMOUNT,
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/cold_seashells_1"),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/cold_seashells_2"),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/cold_seashells_3"),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/cold_seashells_4"));

        this.createSeashells(Primal_Blocks.TEMPERATE_SEASHELLS, SeashellsBlock.FACING, SeashellsBlock.AMOUNT,
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/temperate_seashells_1"),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/temperate_seashells_2"),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/temperate_seashells_3"),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/temperate_seashells_4"));

        this.simpleBlock(Primal_Blocks.CROCODILE_SCUTE_BLOCK.get());
        this.simpleBlock(Primal_Blocks.CROCODILE_SCUTE_SHINGLE.get());
        this.simpleBlock(Primal_Blocks.CHISELED_CROCODILE_SCUTE.get());
        this.simpleStairs(Primal_Blocks.CROCODILE_SCUTE_STAIRS, Primal_Blocks.CROCODILE_SCUTE_BLOCK);
        this.simpleSlab(Primal_Blocks.CROCODILE_SCUTE_SLAB, Primal_Blocks.CROCODILE_SCUTE_BLOCK);
        this.simpleBlock(Primal_Blocks.CHOMP_TRAP_GREEN.get(), this.models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/chomp_trap_green")));

        this.simpleBlock(Primal_Blocks.ARID_CROCODILE_SCUTE_BLOCK.get());
        this.simpleBlock(Primal_Blocks.ARID_CROCODILE_SCUTE_SHINGLE.get());
        this.simpleBlock(Primal_Blocks.ARID_CHISELED_CROCODILE_SCUTE.get());
        this.simpleStairs(Primal_Blocks.ARID_CROCODILE_SCUTE_STAIRS, Primal_Blocks.ARID_CROCODILE_SCUTE_BLOCK);
        this.simpleSlab(Primal_Blocks.ARID_CROCODILE_SCUTE_SLAB, Primal_Blocks.ARID_CROCODILE_SCUTE_BLOCK);
        this.simpleBlock(Primal_Blocks.CHOMP_TRAP_ARID.get(), this.models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/chomp_trap_arid")));

        this.simpleBlock(Primal_Blocks.HUMID_CROCODILE_SCUTE_BLOCK.get());
        this.simpleBlock(Primal_Blocks.HUMID_CROCODILE_SCUTE_SHINGLE.get());
        this.simpleBlock(Primal_Blocks.HUMID_CHISELED_CROCODILE_SCUTE.get());
        this.simpleStairs(Primal_Blocks.HUMID_CROCODILE_SCUTE_STAIRS, Primal_Blocks.HUMID_CROCODILE_SCUTE_BLOCK);
        this.simpleSlab(Primal_Blocks.HUMID_CROCODILE_SCUTE_SLAB, Primal_Blocks.HUMID_CROCODILE_SCUTE_BLOCK);
        this.simpleBlock(Primal_Blocks.CHOMP_TRAP_HUMID.get(), this.models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/chomp_trap_humid")));

        this.getVariantBuilder(Primal_Blocks.RIVER_REEDS.get())
                .partialState().with(ThreeTallPlantBlock.HALF, TripleBlockHalf.UPPER).with(ThreeTallPlantBlock.AGE, 0).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/river_reeds_top_0"))))
                .partialState().with(ThreeTallPlantBlock.HALF, TripleBlockHalf.UPPER).with(ThreeTallPlantBlock.AGE, 1).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/river_reeds_top_1"))))
                .partialState().with(ThreeTallPlantBlock.HALF, TripleBlockHalf.MIDDLE).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/river_reeds_middle"))))
                .partialState().with(ThreeTallPlantBlock.HALF, TripleBlockHalf.LOWER).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/river_reeds_bottom"))));

        this.getVariantBuilder(Primal_Blocks.CATTAILS.get())
                .partialState().with(ThreeTallPlantBlock.HALF, TripleBlockHalf.UPPER).with(ThreeTallPlantBlock.AGE, 0).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/cattails_top_0"))))
                .partialState().with(ThreeTallPlantBlock.HALF, TripleBlockHalf.UPPER).with(ThreeTallPlantBlock.AGE, 1).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/cattails_top_1"))))
                .partialState().with(ThreeTallPlantBlock.HALF, TripleBlockHalf.MIDDLE).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/cattails_middle"))))
                .partialState().with(ThreeTallPlantBlock.HALF, TripleBlockHalf.LOWER).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/cattails_bottom"))));

        this.simpleBlock(Primal_Blocks.SHORT_RIVER_REEDS.get(), this.models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/short_river_reeds")));

        this.getVariantBuilder(Primal_Blocks.CROCODILE_EGG.get())
                .partialState().with(Primal_Util.EGGS_3, 1).with(AnimalEgg.HATCH, 0).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/crocodile_egg_one"))))
                .partialState().with(Primal_Util.EGGS_3, 1).with(AnimalEgg.HATCH, 1).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/crocodile_egg_one_chipped"))))
                .partialState().with(Primal_Util.EGGS_3, 1).with(AnimalEgg.HATCH, 2).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/crocodile_egg_one_cracked"))))

                .partialState().with(Primal_Util.EGGS_3, 2).with(AnimalEgg.HATCH, 0).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/crocodile_egg_two"))))
                .partialState().with(Primal_Util.EGGS_3, 2).with(AnimalEgg.HATCH, 1).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/crocodile_egg_two_chipped"))))
                .partialState().with(Primal_Util.EGGS_3, 2).with(AnimalEgg.HATCH, 2).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/crocodile_egg_two_cracked"))))

                .partialState().with(Primal_Util.EGGS_3, 3).with(AnimalEgg.HATCH, 0).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/crocodile_egg_three"))))
                .partialState().with(Primal_Util.EGGS_3, 3).with(AnimalEgg.HATCH, 1).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/crocodile_egg_three_chipped"))))
                .partialState().with(Primal_Util.EGGS_3, 3).with(AnimalEgg.HATCH, 2).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/crocodile_egg_three_cracked"))));

        this.getVariantBuilder(Primal_Blocks.EAGLE_EGG.get())
                .partialState().with(Primal_Util.EGGS_2, 1).with(AnimalEgg.HATCH, 0).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/eagle_egg_one"))))
                .partialState().with(Primal_Util.EGGS_2, 1).with(AnimalEgg.HATCH, 1).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/eagle_egg_one_chipped"))))
                .partialState().with(Primal_Util.EGGS_2, 1).with(AnimalEgg.HATCH, 2).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/eagle_egg_one_cracked"))))

                .partialState().with(Primal_Util.EGGS_2, 2).with(AnimalEgg.HATCH, 0).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/eagle_egg_two"))))
                .partialState().with(Primal_Util.EGGS_2, 2).with(AnimalEgg.HATCH, 1).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/eagle_egg_two_chipped"))))
                .partialState().with(Primal_Util.EGGS_2, 2).with(AnimalEgg.HATCH, 2).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/eagle_egg_two_cracked"))));

        this.getVariantBuilder(Primal_Blocks.CASSOWARY_EGG.get())
                .partialState().with(AnimalEgg.HATCH, 0).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/cassowary_egg"))))
                .partialState().with(AnimalEgg.HATCH, 1).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/cassowary_egg_chipped"))))
                .partialState().with(AnimalEgg.HATCH, 2).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/cassowary_egg_cracked"))));

        this.createAnimalNest(
                Primal_Blocks.NEST_BLOCK,
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/nest_base"),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/nest_north_side"),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/nest_south_side"),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/nest_east_side"),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/nest_west_side"));

        this.axisBlock((RotatedPillarBlock) Primal_Blocks.STRAW_BALE.get(),
                modelOf("block/straw_bale"),
                modelOf("block/straw_bale_horizontal"));

        this.axisBlock((RotatedPillarBlock) Primal_Blocks.DRIED_STRAW_BALE.get(),
                modelOf("block/dried_straw_bale"),
                modelOf("block/dried_straw_bale_horizontal"));
        this.simpleBlock(Primal_Blocks.WEAVED_STRAW.get());

        this.simpleBlock(Primal_Blocks.THORNY_ACACIA_SAPLING.get(), this.models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/thorny_acacia_sapling")));

        this.getVariantBuilder(Primal_Blocks.THORNY_ACACIA_LOG.get())
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y)
                .modelForState().modelFile(modelOf("block/thorny_acacia_log")).addModel()
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Z)
                .modelForState().modelFile(modelOf("block/thorny_acacia_log")).rotationX(90).addModel()
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.X)
                .modelForState().modelFile(modelOf("block/thorny_acacia_log")).rotationX(90).rotationY(90).addModel();

        this.getVariantBuilder(Primal_Blocks.THORNY_ACACIA_WOOD.get())
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y)
                .modelForState().modelFile(modelOf("block/thorny_acacia_wood")).addModel()
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Z)
                .modelForState().modelFile(modelOf("block/thorny_acacia_wood")).rotationX(90).addModel()
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.X)
                .modelForState().modelFile(modelOf("block/thorny_acacia_wood")).rotationX(90).rotationY(90).addModel();

        this.simpleBlock(Primal_Blocks.THORNY_ACACIA_LEAVES.get());

        this.hollowBlock(Primal_Blocks.HOLLOW_OAK_LOG, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/hollow_oak_log"));
        this.hollowBlock(Primal_Blocks.HOLLOW_SPRUCE_LOG, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/hollow_spruce_log"));
        this.hollowBlock(Primal_Blocks.HOLLOW_BIRCH_LOG, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/hollow_birch_log"));
        this.hollowBlock(Primal_Blocks.HOLLOW_JUNGLE_LOG, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/hollow_jungle_log"));
        this.hollowBlock(Primal_Blocks.HOLLOW_ACACIA_LOG, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/hollow_acacia_log"));
        this.hollowBlock(Primal_Blocks.HOLLOW_DARK_OAK_LOG, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/hollow_dark_oak_log"));
        this.hollowBlock(Primal_Blocks.HOLLOW_MANGROVE_LOG, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/hollow_mangrove_log"));

        this.simpleBlock(Primal_Blocks.PETRIFIED_FRUIT.get(), this.models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/petrified_fruit")));
        this.simpleBlock(Primal_Blocks.LITCHI_SAPLING.get(), this.models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/litchi_sapling")));
        this.createFruitTree(Primal_Blocks.LITCHI_TREE, "litchi");
        this.simpleBlock(Primal_Blocks.KIWANO_SAPLING.get(), this.models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/kiwano_sapling")));
        this.createBulkFruit(Primal_Blocks.KIWANO_BULK, "kiwano");
        this.simpleBlock(Primal_Blocks.STARFRUIT_SAPLING.get(), this.models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/starfruit_sapling")));
        this.createFruitTree(Primal_Blocks.STARFRUIT_TREE, "starfruit");

        this.simpleStairs(Primal_Blocks.WEAVED_STRAW_STAIRS, Primal_Blocks.WEAVED_STRAW);
        this.simpleSlab(Primal_Blocks.WEAVED_STRAW_SLAB, Primal_Blocks.WEAVED_STRAW);

        this.createAntlerBlock(Primal_Blocks.FALLOW_DEER_ANTLER);
        this.createAntlerBlock(Primal_Blocks.REINDEER_ANTLER);
        this.createAntlerBlock(Primal_Blocks.WHITETAIL_DEER_ANTLER);

        this.simpleBlock(Primal_Blocks.DREAMCATCHER.get(), this.models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/dreamcatcher")));
        this.simpleBlock(Primal_Blocks.STRAW_BASKET.get(), this.models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/straw_basket")));
    }
    private ModelFile modelOf(String location){
        return new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, location));
    }

    private void simpleStairs(RegistryObject<Block> stairs, RegistryObject<Block> baseBlock){
        this.stairsBlock((StairBlock) stairs.get(),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+baseBlock.getId().getPath()));
    }

    private void simpleSlab(RegistryObject<Block> slab, RegistryObject<Block> baseBlock){
        this.slabBlock((SlabBlock) slab.get(),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+baseBlock.getId().getPath()),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+baseBlock.getId().getPath()));
    }

    private void hollowBlock(RegistryObject<Block> block, ResourceLocation model){
        var modelSide = ResourceLocation.fromNamespaceAndPath(model.getNamespace(), model.getPath()+"_side");

        this.getVariantBuilder(block.get())
                //Axis Y
                .partialState().with(HollowLogBlock.FACING, Direction.NORTH).with(HollowLogBlock.AXIS, Direction.Axis.Y).addModels(
                        ConfiguredModel.builder()
                                .modelFile(new ModelFile.UncheckedModelFile(model))
                                .rotationX(0)
                                .rotationY(0)
                                .build())

                .partialState().with(HollowLogBlock.FACING, Direction.WEST).with(HollowLogBlock.AXIS, Direction.Axis.Y).addModels(
                        ConfiguredModel.builder()
                                .modelFile(new ModelFile.UncheckedModelFile(model))
                                .rotationX(0)
                                .rotationY(270)
                                .build())

                .partialState().with(HollowLogBlock.FACING, Direction.SOUTH).with(HollowLogBlock.AXIS, Direction.Axis.Y).addModels(
                        ConfiguredModel.builder()
                                .modelFile(new ModelFile.UncheckedModelFile(model))
                                .rotationX(0)
                                .rotationY(180)
                                .build())

                .partialState().with(HollowLogBlock.FACING, Direction.EAST).with(HollowLogBlock.AXIS, Direction.Axis.Y).addModels(
                        ConfiguredModel.builder()
                                .modelFile(new ModelFile.UncheckedModelFile(model))
                                .rotationX(0)
                                .rotationY(90)
                                .build())
                //Axis Z
                .partialState().with(HollowLogBlock.FACING, Direction.NORTH).with(HollowLogBlock.AXIS, Direction.Axis.Z).addModels(
                        ConfiguredModel.builder()
                                .modelFile(new ModelFile.UncheckedModelFile(model))
                                .rotationX(270)
                                .rotationY(0)
                                .build())

                .partialState().with(HollowLogBlock.FACING, Direction.WEST).with(HollowLogBlock.AXIS, Direction.Axis.Z).addModels(
                        ConfiguredModel.builder()
                                .modelFile(new ModelFile.UncheckedModelFile(modelSide))
                                .rotationX(90)
                                .rotationY(0)
                                .build())

                .partialState().with(HollowLogBlock.FACING, Direction.SOUTH).with(HollowLogBlock.AXIS, Direction.Axis.Z).addModels(
                        ConfiguredModel.builder()
                                .modelFile(new ModelFile.UncheckedModelFile(model))
                                .rotationX(90)
                                .rotationY(0)
                                .build())

                .partialState().with(HollowLogBlock.FACING, Direction.EAST).with(HollowLogBlock.AXIS, Direction.Axis.Z).addModels(
                        ConfiguredModel.builder()
                                .modelFile(new ModelFile.UncheckedModelFile(modelSide))
                                .rotationX(90)
                                .rotationY(180)
                                .build())

                //Axis X
                .partialState().with(HollowLogBlock.FACING, Direction.NORTH).with(HollowLogBlock.AXIS, Direction.Axis.X).addModels(
                        ConfiguredModel.builder()
                                .modelFile(new ModelFile.UncheckedModelFile(model))
                                .rotationX(270)
                                .rotationY(270)
                                .build())

                .partialState().with(HollowLogBlock.FACING, Direction.WEST).with(HollowLogBlock.AXIS, Direction.Axis.X).addModels(
                        ConfiguredModel.builder()
                                .modelFile(new ModelFile.UncheckedModelFile(modelSide))
                                .rotationX(270)
                                .rotationY(270)
                                .build())

                .partialState().with(HollowLogBlock.FACING, Direction.SOUTH).with(HollowLogBlock.AXIS, Direction.Axis.X).addModels(
                        ConfiguredModel.builder()
                                .modelFile(new ModelFile.UncheckedModelFile(model))
                                .rotationX(90)
                                .rotationY(90)
                                .build())

                .partialState().with(HollowLogBlock.FACING, Direction.EAST).with(HollowLogBlock.AXIS, Direction.Axis.X).addModels(
                        ConfiguredModel.builder()
                                .modelFile(new ModelFile.UncheckedModelFile(modelSide))
                                .rotationX(90)
                                .rotationY(90)
                                .build());
    }

    private void createSharkTooth(RegistryObject<Block> block,
                                  ResourceLocation tip,
                                  ResourceLocation base){
        this.getVariantBuilder(block.get())
                //Tip
                .partialState().with(SharkToothBlock.FACING, Direction.UP).with(SharkToothBlock.THICKNESS, SharkToothThickness.TIP).addModels(
                        ConfiguredModel.builder()
                                .modelFile(new ModelFile.UncheckedModelFile(tip))
                                .rotationX(0)
                                .rotationY(0)
                                .build())

                .partialState().with(SharkToothBlock.FACING, Direction.DOWN).with(SharkToothBlock.THICKNESS, SharkToothThickness.TIP).addModels(
                        ConfiguredModel.builder()
                                .modelFile(new ModelFile.UncheckedModelFile(tip))
                                .rotationX(180)
                                .rotationY(0)
                                .build())

                .partialState().with(SharkToothBlock.FACING, Direction.NORTH).with(SharkToothBlock.THICKNESS, SharkToothThickness.TIP).addModels(
                        ConfiguredModel.builder()
                                .modelFile(new ModelFile.UncheckedModelFile(tip))
                                .rotationX(90)
                                .rotationY(0)
                                .build())

                .partialState().with(SharkToothBlock.FACING, Direction.EAST).with(SharkToothBlock.THICKNESS, SharkToothThickness.TIP).addModels(
                        ConfiguredModel.builder()
                                .modelFile(new ModelFile.UncheckedModelFile(tip))
                                .rotationX(90)
                                .rotationY(90)
                                .build())

                .partialState().with(SharkToothBlock.FACING, Direction.SOUTH).with(SharkToothBlock.THICKNESS, SharkToothThickness.TIP).addModels(
                        ConfiguredModel.builder()
                                .modelFile(new ModelFile.UncheckedModelFile(tip))
                                .rotationX(90)
                                .rotationY(180)
                                .build())

                .partialState().with(SharkToothBlock.FACING, Direction.WEST).with(SharkToothBlock.THICKNESS, SharkToothThickness.TIP).addModels(
                        ConfiguredModel.builder()
                                .modelFile(new ModelFile.UncheckedModelFile(tip))
                                .rotationX(90)
                                .rotationY(270)
                                .build())

                //Base
                .partialState().with(SharkToothBlock.FACING, Direction.UP).with(SharkToothBlock.THICKNESS, SharkToothThickness.BASE).addModels(
                        ConfiguredModel.builder()
                                .modelFile(new ModelFile.UncheckedModelFile(base))
                                .rotationX(0)
                                .rotationY(0)
                                .build())

                .partialState().with(SharkToothBlock.FACING, Direction.DOWN).with(SharkToothBlock.THICKNESS, SharkToothThickness.BASE).addModels(
                        ConfiguredModel.builder()
                                .modelFile(new ModelFile.UncheckedModelFile(base))
                                .rotationX(180)
                                .rotationY(0)
                                .build())

                .partialState().with(SharkToothBlock.FACING, Direction.NORTH).with(SharkToothBlock.THICKNESS, SharkToothThickness.BASE).addModels(
                        ConfiguredModel.builder()
                                .modelFile(new ModelFile.UncheckedModelFile(base))
                                .rotationX(90)
                                .rotationY(0)
                                .build())

                .partialState().with(SharkToothBlock.FACING, Direction.EAST).with(SharkToothBlock.THICKNESS, SharkToothThickness.BASE).addModels(
                        ConfiguredModel.builder()
                                .modelFile(new ModelFile.UncheckedModelFile(base))
                                .rotationX(90)
                                .rotationY(90)
                                .build())

                .partialState().with(SharkToothBlock.FACING, Direction.SOUTH).with(SharkToothBlock.THICKNESS, SharkToothThickness.BASE).addModels(
                        ConfiguredModel.builder()
                                .modelFile(new ModelFile.UncheckedModelFile(base))
                                .rotationX(90)
                                .rotationY(180)
                                .build())

                .partialState().with(SharkToothBlock.FACING, Direction.WEST).with(SharkToothBlock.THICKNESS, SharkToothThickness.BASE).addModels(
                        ConfiguredModel.builder()
                                .modelFile(new ModelFile.UncheckedModelFile(base))
                                .rotationX(90)
                                .rotationY(270)
                                .build());
    }

    @SuppressWarnings("all")
    private void createSeashells(RegistryObject<Block> block, DirectionProperty property, IntegerProperty amountProperty, ResourceLocation amount1, ResourceLocation amount2, ResourceLocation amount3, ResourceLocation amount4){

        this.getMultipartBuilder(block.get())
                //Normal
                .part().modelFile(new ModelFile.UncheckedModelFile(amount1))
                .addModel().condition(property, Direction.NORTH).condition(amountProperty, 1, 2, 3, 4).condition(BlockStateProperties.SNOWY, false).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount1)).rotationY(90)
                .addModel().condition(property, Direction.EAST).condition(amountProperty, 1, 2, 3, 4).condition(BlockStateProperties.SNOWY, false).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount1)).rotationY(180)
                .addModel().condition(property, Direction.SOUTH).condition(amountProperty, 1, 2, 3, 4).condition(BlockStateProperties.SNOWY, false).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount1)).rotationY(270)
                .addModel().condition(property, Direction.WEST).condition(amountProperty, 1, 2, 3, 4).condition(BlockStateProperties.SNOWY, false).end()

                .part().modelFile(new ModelFile.UncheckedModelFile(amount2))
                .addModel().condition(property, Direction.NORTH).condition(amountProperty, 2, 3, 4).condition(BlockStateProperties.SNOWY, false).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount2)).rotationY(90)
                .addModel().condition(property, Direction.EAST).condition(amountProperty, 2, 3, 4).condition(BlockStateProperties.SNOWY, false).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount2)).rotationY(180)
                .addModel().condition(property, Direction.SOUTH).condition(amountProperty, 2, 3, 4).condition(BlockStateProperties.SNOWY, false).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount2)).rotationY(270)
                .addModel().condition(property, Direction.WEST).condition(amountProperty, 2, 3, 4).condition(BlockStateProperties.SNOWY, false).end()

                .part().modelFile(new ModelFile.UncheckedModelFile(amount3))
                .addModel().condition(property, Direction.NORTH).condition(amountProperty, 3, 4).condition(BlockStateProperties.SNOWY, false).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount3)).rotationY(90)
                .addModel().condition(property, Direction.EAST).condition(amountProperty, 3, 4).condition(BlockStateProperties.SNOWY, false).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount3)).rotationY(180)
                .addModel().condition(property, Direction.SOUTH).condition(amountProperty, 3, 4).condition(BlockStateProperties.SNOWY, false).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount3)).rotationY(270)
                .addModel().condition(property, Direction.WEST).condition(amountProperty, 3, 4).condition(BlockStateProperties.SNOWY, false).end()

                .part().modelFile(new ModelFile.UncheckedModelFile(amount4))
                .addModel().condition(property, Direction.NORTH).condition(amountProperty, 4).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount4)).rotationY(90)
                .addModel().condition(property, Direction.EAST).condition(amountProperty, 4).condition(BlockStateProperties.SNOWY, false).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount4)).rotationY(180)
                .addModel().condition(property, Direction.SOUTH).condition(amountProperty, 4).condition(BlockStateProperties.SNOWY, false).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount4)).rotationY(270)
                .addModel().condition(property, Direction.WEST).condition(amountProperty, 4).condition(BlockStateProperties.SNOWY, false).end()


                //Snowy
                .part().modelFile(new ModelFile.UncheckedModelFile(amount1+"_snowy"))
                .addModel().condition(property, Direction.NORTH).condition(amountProperty, 1, 2, 3, 4).condition(BlockStateProperties.SNOWY, true).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount1+"_snowy")).rotationY(90)
                .addModel().condition(property, Direction.EAST).condition(amountProperty, 1, 2, 3, 4).condition(BlockStateProperties.SNOWY, true).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount1+"_snowy")).rotationY(180)
                .addModel().condition(property, Direction.SOUTH).condition(amountProperty, 1, 2, 3, 4).condition(BlockStateProperties.SNOWY, true).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount1+"_snowy")).rotationY(270)
                .addModel().condition(property, Direction.WEST).condition(amountProperty, 1, 2, 3, 4).condition(BlockStateProperties.SNOWY, true).end()

                .part().modelFile(new ModelFile.UncheckedModelFile(amount2+"_snowy"))
                .addModel().condition(property, Direction.NORTH).condition(amountProperty, 2, 3, 4).condition(BlockStateProperties.SNOWY, true).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount2+"_snowy")).rotationY(90)
                .addModel().condition(property, Direction.EAST).condition(amountProperty, 2, 3, 4).condition(BlockStateProperties.SNOWY, true).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount2+"_snowy")).rotationY(180)
                .addModel().condition(property, Direction.SOUTH).condition(amountProperty, 2, 3, 4).condition(BlockStateProperties.SNOWY, true).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount2+"_snowy")).rotationY(270)
                .addModel().condition(property, Direction.WEST).condition(amountProperty, 2, 3, 4).condition(BlockStateProperties.SNOWY, true).end()

                .part().modelFile(new ModelFile.UncheckedModelFile(amount3+"_snowy"))
                .addModel().condition(property, Direction.NORTH).condition(amountProperty, 3, 4).condition(BlockStateProperties.SNOWY, true).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount3+"_snowy")).rotationY(90)
                .addModel().condition(property, Direction.EAST).condition(amountProperty, 3, 4).condition(BlockStateProperties.SNOWY, true).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount3+"_snowy")).rotationY(180)
                .addModel().condition(property, Direction.SOUTH).condition(amountProperty, 3, 4).condition(BlockStateProperties.SNOWY, true).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount3+"_snowy")).rotationY(270)
                .addModel().condition(property, Direction.WEST).condition(amountProperty, 3, 4).condition(BlockStateProperties.SNOWY, true).end()

                .part().modelFile(new ModelFile.UncheckedModelFile(amount4+"_snowy"))
                .addModel().condition(property, Direction.NORTH).condition(amountProperty, 4).condition(BlockStateProperties.SNOWY, true).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount4+"_snowy")).rotationY(90)
                .addModel().condition(property, Direction.EAST).condition(amountProperty, 4).condition(BlockStateProperties.SNOWY, true).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount4+"_snowy")).rotationY(180)
                .addModel().condition(property, Direction.SOUTH).condition(amountProperty, 4).condition(BlockStateProperties.SNOWY, true).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount4+"_snowy")).rotationY(270)
                .addModel().condition(property, Direction.WEST).condition(amountProperty, 4).condition(BlockStateProperties.SNOWY, true).end()

                .part().modelFile(new ModelFile.UncheckedModelFile(ResourceLocation.withDefaultNamespace("block/snow_height2")))
                .addModel().condition(BlockStateProperties.SNOWY, true).end();
    }

    private void createAnimalNest(RegistryObject<Block> block,
                                  ResourceLocation base,
                                  ResourceLocation northSide,
                                  ResourceLocation southSide,
                                  ResourceLocation eastSide,
                                  ResourceLocation westSide){
        this.getMultipartBuilder(block.get())
                .part().modelFile(new ModelFile.UncheckedModelFile(base))
                .addModel().end()

                //NorthSide
                .part().modelFile(new ModelFile.UncheckedModelFile(northSide))
                .addModel().condition(NestBlock.NORTH, true).end()

                //SouthSide
                .part().modelFile(new ModelFile.UncheckedModelFile(southSide))
                .addModel().condition(NestBlock.SOUTH, true).end()

                //EastSide
                .part().modelFile(new ModelFile.UncheckedModelFile(eastSide))
                .addModel().condition(NestBlock.EAST, true).end()

                //WestSide
                .part().modelFile(new ModelFile.UncheckedModelFile(westSide))
                .addModel().condition(NestBlock.WEST, true).end();
    }

    private void createBulkFruit(RegistryObject<Block> block, String type){
        this.getVariantBuilder(block.get())
                //Little
                .partialState().with(FruitBulk.AGE, 0).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+type+"_juvenile"))))
                //Big
                .partialState().with(FruitBulk.AGE, 1).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+type+"_full"))));
    }

    private void createFruitTree(RegistryObject<Block> block, String type){
        var juvenileModel = new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+type+"_tree_juvenile"));
        var trunkModel = new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+type+"_tree_trunk"));
        var top1 = new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+type+"_tree_top_1"));
        var top2 = new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+type+"_tree_top_2"));
        var top3 = new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+type+"_tree_top_3"));

        var rotNorth = 0;
        var rotEast  = 90;
        var rotSouth = 180;
        var rotWest  = 270;

        this.getVariantBuilder(block.get())
                //Bottom - Little tree
                .partialState().with(FruitTree.HALF, DoubleBlockHalf.LOWER).with(FruitTree.AGE, 0)
                .with(FruitTree.FACING, Direction.NORTH)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(juvenileModel)
                                .rotationY(rotNorth)
                                .build())

                //Bottom - Trunk
                .partialState().with(FruitTree.HALF, DoubleBlockHalf.LOWER).with(FruitTree.AGE, 1)
                .with(FruitTree.FACING, Direction.NORTH)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(trunkModel)
                                .rotationY(rotNorth)
                                .build())
                //Bottom - Trunk
                .partialState().with(FruitTree.HALF, DoubleBlockHalf.LOWER).with(FruitTree.AGE, 2)
                .with(FruitTree.FACING, Direction.NORTH)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(trunkModel)
                                .rotationY(rotNorth)
                                .build())
                //Bottom - Trunk
                .partialState().with(FruitTree.HALF, DoubleBlockHalf.LOWER).with(FruitTree.AGE, 3)
                .with(FruitTree.FACING, Direction.NORTH)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(trunkModel)
                                .rotationY(rotNorth)
                                .build())

                //Top - Fruitless
                .partialState().with(FruitTree.HALF, DoubleBlockHalf.UPPER).with(FruitTree.AGE, 0)
                .with(FruitTree.FACING, Direction.NORTH)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(top1)
                                .rotationY(rotNorth)
                                .build())
                //Top - Fruitless
                .partialState().with(FruitTree.HALF, DoubleBlockHalf.UPPER).with(FruitTree.AGE, 1)
                .with(FruitTree.FACING, Direction.NORTH)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(top1)
                                .rotationY(rotNorth)
                                .build())
                //Top - Little Fruit
                .partialState().with(FruitTree.HALF, DoubleBlockHalf.UPPER).with(FruitTree.AGE, 2)
                .with(FruitTree.FACING, Direction.NORTH)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(top2)
                                .rotationY(rotNorth)
                                .build())

                //Top - Lot Fruits
                .partialState().with(FruitTree.HALF, DoubleBlockHalf.UPPER).with(FruitTree.AGE, 3)
                .with(FruitTree.FACING, Direction.NORTH)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(top3)
                                .rotationY(rotNorth)
                                .build())

        //South
                //Bottom - Little tree
                .partialState().with(FruitTree.HALF, DoubleBlockHalf.LOWER).with(FruitTree.AGE, 0)
                .with(FruitTree.FACING, Direction.SOUTH)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(juvenileModel)
                                .rotationY(rotSouth)
                                .build())

                //Bottom - Trunk
                .partialState().with(FruitTree.HALF, DoubleBlockHalf.LOWER).with(FruitTree.AGE, 1)
                .with(FruitTree.FACING, Direction.SOUTH)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(trunkModel)
                                .rotationY(rotSouth)
                                .build())
                //Bottom - Trunk
                .partialState().with(FruitTree.HALF, DoubleBlockHalf.LOWER).with(FruitTree.AGE, 2)
                .with(FruitTree.FACING, Direction.SOUTH)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(trunkModel)
                                .rotationY(rotSouth)
                                .build())
                //Bottom - Trunk
                .partialState().with(FruitTree.HALF, DoubleBlockHalf.LOWER).with(FruitTree.AGE, 3)
                .with(FruitTree.FACING, Direction.SOUTH)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(trunkModel)
                                .rotationY(rotSouth)
                                .build())

                //Top - Fruitless
                .partialState().with(FruitTree.HALF, DoubleBlockHalf.UPPER).with(FruitTree.AGE, 0)
                .with(FruitTree.FACING, Direction.SOUTH)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(top1)
                                .rotationY(rotSouth)
                                .build())
                //Top - Fruitless
                .partialState().with(FruitTree.HALF, DoubleBlockHalf.UPPER).with(FruitTree.AGE, 1)
                .with(FruitTree.FACING, Direction.SOUTH)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(top1)
                                .rotationY(rotSouth)
                                .build())
                //Top - Little Fruit
                .partialState().with(FruitTree.HALF, DoubleBlockHalf.UPPER).with(FruitTree.AGE, 2)
                .with(FruitTree.FACING, Direction.SOUTH)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(top2)
                                .rotationY(rotSouth)
                                .build())

                //Top - Lot Fruits
                .partialState().with(FruitTree.HALF, DoubleBlockHalf.UPPER).with(FruitTree.AGE, 3)
                .with(FruitTree.FACING, Direction.SOUTH)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(top3)
                                .rotationY(rotSouth)
                                .build())

        //East
                //Bottom - Little tree
                .partialState().with(FruitTree.HALF, DoubleBlockHalf.LOWER).with(FruitTree.AGE, 0)
                .with(FruitTree.FACING, Direction.EAST)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(juvenileModel)
                                .rotationY(rotEast)
                                .build())

                //Bottom - Trunk
                .partialState().with(FruitTree.HALF, DoubleBlockHalf.LOWER).with(FruitTree.AGE, 1)
                .with(FruitTree.FACING, Direction.EAST)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(trunkModel)
                                .rotationY(rotEast)
                                .build())
                //Bottom - Trunk
                .partialState().with(FruitTree.HALF, DoubleBlockHalf.LOWER).with(FruitTree.AGE, 2)
                .with(FruitTree.FACING, Direction.EAST)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(trunkModel)
                                .rotationY(rotEast)
                                .build())
                //Bottom - Trunk
                .partialState().with(FruitTree.HALF, DoubleBlockHalf.LOWER).with(FruitTree.AGE, 3)
                .with(FruitTree.FACING, Direction.EAST)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(trunkModel)
                                .rotationY(rotEast)
                                .build())

                //Top - Fruitless
                .partialState().with(FruitTree.HALF, DoubleBlockHalf.UPPER).with(FruitTree.AGE, 0)
                .with(FruitTree.FACING, Direction.EAST)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(top1)
                                .rotationY(rotEast)
                                .build())
                //Top - Fruitless
                .partialState().with(FruitTree.HALF, DoubleBlockHalf.UPPER).with(FruitTree.AGE, 1)
                .with(FruitTree.FACING, Direction.EAST)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(top1)
                                .rotationY(rotEast)
                                .build())
                //Top - Little Fruit
                .partialState().with(FruitTree.HALF, DoubleBlockHalf.UPPER).with(FruitTree.AGE, 2)
                .with(FruitTree.FACING, Direction.EAST)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(top2)
                                .rotationY(rotEast)
                                .build())

                //Top - Lot Fruits
                .partialState().with(FruitTree.HALF, DoubleBlockHalf.UPPER).with(FruitTree.AGE, 3)
                .with(FruitTree.FACING, Direction.EAST)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(top3)
                                .rotationY(rotEast)
                                .build())

        //West
                //Bottom - Little tree
                .partialState().with(FruitTree.HALF, DoubleBlockHalf.LOWER).with(FruitTree.AGE, 0)
                .with(FruitTree.FACING, Direction.WEST)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(juvenileModel)
                                .rotationY(rotWest)
                                .build())

                //Bottom - Trunk
                .partialState().with(FruitTree.HALF, DoubleBlockHalf.LOWER).with(FruitTree.AGE, 1)
                .with(FruitTree.FACING, Direction.WEST)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(trunkModel)
                                .rotationY(rotWest)
                                .build())
                //Bottom - Trunk
                .partialState().with(FruitTree.HALF, DoubleBlockHalf.LOWER).with(FruitTree.AGE, 2)
                .with(FruitTree.FACING, Direction.WEST)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(trunkModel)
                                .rotationY(rotWest)
                                .build())
                //Bottom - Trunk
                .partialState().with(FruitTree.HALF, DoubleBlockHalf.LOWER).with(FruitTree.AGE, 3)
                .with(FruitTree.FACING, Direction.WEST)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(trunkModel)
                                .rotationY(rotWest)
                                .build())

                //Top - Fruitless
                .partialState().with(FruitTree.HALF, DoubleBlockHalf.UPPER).with(FruitTree.AGE, 0)
                .with(FruitTree.FACING, Direction.WEST)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(top1)
                                .rotationY(rotWest)
                                .build())
                //Top - Fruitless
                .partialState().with(FruitTree.HALF, DoubleBlockHalf.UPPER).with(FruitTree.AGE, 1)
                .with(FruitTree.FACING, Direction.WEST)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(top1)
                                .rotationY(rotWest)
                                .build())
                //Top - Little Fruit
                .partialState().with(FruitTree.HALF, DoubleBlockHalf.UPPER).with(FruitTree.AGE, 2)
                .with(FruitTree.FACING, Direction.WEST)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(top2)
                                .rotationY(rotWest)
                                .build())

                //Top - Lot Fruits
                .partialState().with(FruitTree.HALF, DoubleBlockHalf.UPPER).with(FruitTree.AGE, 3)
                .with(FruitTree.FACING, Direction.WEST)
                .addModels(
                        ConfiguredModel.builder()
                                .modelFile(top3)
                                .rotationY(rotWest)
                                .build())
        ;
    }

    private void createAntlerBlock(RegistryObject<Block> block){
        var normalModel = ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+block.getId().getPath());
        var rightModel = ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+block.getId().getPath()+"_right");
        var doubleModel = ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+block.getId().getPath()+"_double");
        var wallModel = ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+block.getId().getPath()+"_wall");
        var rightWallModel = ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+block.getId().getPath()+"_wall_right");

        this.getMultipartBuilder(block.get())
                //Normal Model
                .part().modelFile(new ModelFile.UncheckedModelFile(normalModel))
                .addModel().condition(DeerAntlerBlock.FACING, Direction.NORTH)
                .condition(DeerAntlerBlock.DOUBLE, false)
                .condition(DeerAntlerBlock.ATTACHED, false)
                .condition(DeerAntlerBlock.RIGHT_SIDE, false)
                .end()

                .part().modelFile(new ModelFile.UncheckedModelFile(normalModel)).rotationY(90)
                .addModel().condition(DeerAntlerBlock.FACING, Direction.EAST)
                .condition(DeerAntlerBlock.DOUBLE, false)
                .condition(DeerAntlerBlock.ATTACHED, false)
                .condition(DeerAntlerBlock.RIGHT_SIDE, false)
                .end()

                .part().modelFile(new ModelFile.UncheckedModelFile(normalModel)).rotationY(180)
                .addModel().condition(DeerAntlerBlock.FACING, Direction.SOUTH)
                .condition(DeerAntlerBlock.DOUBLE, false)
                .condition(DeerAntlerBlock.ATTACHED, false)
                .condition(DeerAntlerBlock.RIGHT_SIDE, false)
                .end()

                .part().modelFile(new ModelFile.UncheckedModelFile(normalModel)).rotationY(270)
                .addModel().condition(DeerAntlerBlock.FACING, Direction.WEST)
                .condition(DeerAntlerBlock.DOUBLE, false)
                .condition(DeerAntlerBlock.ATTACHED, false)
                .condition(DeerAntlerBlock.RIGHT_SIDE, false)
                .end()


                //Right Model
                .part().modelFile(new ModelFile.UncheckedModelFile(rightModel))
                .addModel().condition(DeerAntlerBlock.FACING, Direction.NORTH)
                .condition(DeerAntlerBlock.DOUBLE, false)
                .condition(DeerAntlerBlock.ATTACHED, false)
                .condition(DeerAntlerBlock.RIGHT_SIDE, true)
                .end()

                .part().modelFile(new ModelFile.UncheckedModelFile(rightModel)).rotationY(90)
                .addModel().condition(DeerAntlerBlock.FACING, Direction.EAST)
                .condition(DeerAntlerBlock.DOUBLE, false)
                .condition(DeerAntlerBlock.ATTACHED, false)
                .condition(DeerAntlerBlock.RIGHT_SIDE, true)
                .end()

                .part().modelFile(new ModelFile.UncheckedModelFile(rightModel)).rotationY(180)
                .addModel().condition(DeerAntlerBlock.FACING, Direction.SOUTH)
                .condition(DeerAntlerBlock.DOUBLE, false)
                .condition(DeerAntlerBlock.ATTACHED, false)
                .condition(DeerAntlerBlock.RIGHT_SIDE, true)
                .end()

                .part().modelFile(new ModelFile.UncheckedModelFile(rightModel)).rotationY(270)
                .addModel().condition(DeerAntlerBlock.FACING, Direction.WEST)
                .condition(DeerAntlerBlock.DOUBLE, false)
                .condition(DeerAntlerBlock.ATTACHED, false)
                .condition(DeerAntlerBlock.RIGHT_SIDE, true)
                .end()


                //Wall Model
                .part().modelFile(new ModelFile.UncheckedModelFile(wallModel))
                .addModel().condition(DeerAntlerBlock.FACING, Direction.NORTH)
                .condition(DeerAntlerBlock.DOUBLE, false)
                .condition(DeerAntlerBlock.ATTACHED, true)
                .condition(DeerAntlerBlock.RIGHT_SIDE, false)
                .end()

                .part().modelFile(new ModelFile.UncheckedModelFile(wallModel)).rotationY(90)
                .addModel().condition(DeerAntlerBlock.FACING, Direction.EAST)
                .condition(DeerAntlerBlock.DOUBLE, false)
                .condition(DeerAntlerBlock.ATTACHED, true)
                .condition(DeerAntlerBlock.RIGHT_SIDE, false)
                .end()

                .part().modelFile(new ModelFile.UncheckedModelFile(wallModel)).rotationY(180)
                .addModel().condition(DeerAntlerBlock.FACING, Direction.SOUTH)
                .condition(DeerAntlerBlock.DOUBLE, false)
                .condition(DeerAntlerBlock.ATTACHED, true)
                .condition(DeerAntlerBlock.RIGHT_SIDE, false)
                .end()

                .part().modelFile(new ModelFile.UncheckedModelFile(wallModel)).rotationY(270)
                .addModel().condition(DeerAntlerBlock.FACING, Direction.WEST)
                .condition(DeerAntlerBlock.DOUBLE, false)
                .condition(DeerAntlerBlock.ATTACHED, true)
                .condition(DeerAntlerBlock.RIGHT_SIDE, false)
                .end()


                //Wall Right Model
                .part().modelFile(new ModelFile.UncheckedModelFile(rightWallModel))
                .addModel().condition(DeerAntlerBlock.FACING, Direction.NORTH)
                .condition(DeerAntlerBlock.DOUBLE, false)
                .condition(DeerAntlerBlock.ATTACHED, true)
                .condition(DeerAntlerBlock.RIGHT_SIDE, true)
                .end()

                .part().modelFile(new ModelFile.UncheckedModelFile(rightWallModel)).rotationY(90)
                .addModel().condition(DeerAntlerBlock.FACING, Direction.EAST)
                .condition(DeerAntlerBlock.DOUBLE, false)
                .condition(DeerAntlerBlock.ATTACHED, true)
                .condition(DeerAntlerBlock.RIGHT_SIDE, true)
                .end()

                .part().modelFile(new ModelFile.UncheckedModelFile(rightWallModel)).rotationY(180)
                .addModel().condition(DeerAntlerBlock.FACING, Direction.SOUTH)
                .condition(DeerAntlerBlock.DOUBLE, false)
                .condition(DeerAntlerBlock.ATTACHED, true)
                .condition(DeerAntlerBlock.RIGHT_SIDE, true)
                .end()

                .part().modelFile(new ModelFile.UncheckedModelFile(rightWallModel)).rotationY(270)
                .addModel().condition(DeerAntlerBlock.FACING, Direction.WEST)
                .condition(DeerAntlerBlock.DOUBLE, false)
                .condition(DeerAntlerBlock.ATTACHED, true)
                .condition(DeerAntlerBlock.RIGHT_SIDE, true)
                .end()

                //Double Model
                .part().modelFile(new ModelFile.UncheckedModelFile(doubleModel))
                .addModel().condition(DeerAntlerBlock.FACING, Direction.NORTH)
                .condition(DeerAntlerBlock.DOUBLE, true)
                .end()

                .part().modelFile(new ModelFile.UncheckedModelFile(doubleModel)).rotationY(90)
                .addModel().condition(DeerAntlerBlock.FACING, Direction.EAST)
                .condition(DeerAntlerBlock.DOUBLE, true)
                .end()

                .part().modelFile(new ModelFile.UncheckedModelFile(doubleModel)).rotationY(180)
                .addModel().condition(DeerAntlerBlock.FACING, Direction.SOUTH)
                .condition(DeerAntlerBlock.DOUBLE, true)
                .end()

                .part().modelFile(new ModelFile.UncheckedModelFile(doubleModel)).rotationY(270)
                .addModel().condition(DeerAntlerBlock.FACING, Direction.WEST)
                .condition(DeerAntlerBlock.DOUBLE, true)
                .end();
    }
}
