package org.primal.datagen.providers;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.primal.Primal_Main;
import org.primal.block.*;
import org.primal.block.properties.SharkToothThickness;
import org.primal.block.properties.TripleBlockHalf;
import org.primal.registry.Primal_Blocks;
import org.primal.util.AnimalEgg;
import org.primal.util.MiscUtil;

public class Primal_BlockStateGenerator extends BlockStateProvider {

    public Primal_BlockStateGenerator(final PackOutput output, final ExistingFileHelper existingFileHelper) {
        super(output, Primal_Main.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        this.createSharkTooth(Primal_Blocks.SHARK_TOOTH,
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/shark_tooth_tip"),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/shark_tooth_base"));

//        this.getVariantBuilder(Primal_Blocks.SHARK_TOOTH.get())
//                .partialState().with(SharkToothBlock.THICKNESS, SharkToothThickness.TIP).addModels(
//                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/shark_tooth_tip"))))
//                .partialState().with(SharkToothBlock.THICKNESS, SharkToothThickness.BASE).addModels(
//                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/shark_tooth_base"))));

//        this.getVariantBuilder(Primal_Blocks.SHARK_TOOTH.get())
//                .partialState().with(SharkToothBlock.THICKNESS, SharkToothThickness.TIP).with(SharkToothBlock.TIP_DIRECTION, Direction.UP).addModels(
//                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/shark_tooth_tip"))))
//                .partialState().with(SharkToothBlock.THICKNESS, SharkToothThickness.BASE).addModels(
//                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/shark_tooth_base"))))
//                .partialState().with(SharkToothBlock.THICKNESS, SharkToothThickness.TIP).with(SharkToothBlock.TIP_DIRECTION, Direction.DOWN).addModels(
//                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/shark_tooth_tip_down"))));

        this.createSimilarToPinkPetals(Primal_Blocks.SEASHELLS, SeashellsBlock.FACING, SeashellsBlock.AMOUNT,
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/seashells_1"),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/seashells_2"),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/seashells_3"),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/seashells_4"));

        this.simpleBlock(Primal_Blocks.CROCODILE_SCUTE_BLOCK.get());

        this.simpleBlock(Primal_Blocks.CROCODILE_SCUTE_SHINGLE.get());

        this.simpleBlock(Primal_Blocks.CHISELED_CROCODILE_SCUTE.get());

        this.simpleStairs(Primal_Blocks.CROCODILE_SCUTE_STAIRS, Primal_Blocks.CROCODILE_SCUTE_BLOCK);

        this.simpleSlab(Primal_Blocks.CROCODILE_SCUTE_SLAB, Primal_Blocks.CROCODILE_SCUTE_BLOCK);

        this.getVariantBuilder(Primal_Blocks.RIVER_REEDS.get())
                .partialState().with(RiverReeds.HALF, TripleBlockHalf.UPPER).with(RiverReeds.AGE, 0).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/river_reeds_top_0"))))
                .partialState().with(RiverReeds.HALF, TripleBlockHalf.UPPER).with(RiverReeds.AGE, 1).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/river_reeds_top_1"))))
                .partialState().with(RiverReeds.HALF, TripleBlockHalf.MIDDLE).with(RiverReeds.AGE, 0).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/river_reeds_middle_0"))))
                .partialState().with(RiverReeds.HALF, TripleBlockHalf.MIDDLE).with(RiverReeds.AGE, 1).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/river_reeds_middle_1"))))
                .partialState().with(RiverReeds.HALF, TripleBlockHalf.LOWER).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/river_reeds_bottom"))));

        this.simpleBlock(Primal_Blocks.SHORT_RIVER_REEDS.get());

        this.getVariantBuilder(Primal_Blocks.CROCODILE_EGG.get())
                .partialState().with(MiscUtil.EGGS_3, 1).with(AnimalEgg.HATCH, 0).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/crocodile_egg_one"))))
                .partialState().with(MiscUtil.EGGS_3, 1).with(AnimalEgg.HATCH, 1).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/crocodile_egg_one_chipped"))))
                .partialState().with(MiscUtil.EGGS_3, 1).with(AnimalEgg.HATCH, 2).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/crocodile_egg_one_cracked"))))

                .partialState().with(MiscUtil.EGGS_3, 2).with(AnimalEgg.HATCH, 0).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/crocodile_egg_two"))))
                .partialState().with(MiscUtil.EGGS_3, 2).with(AnimalEgg.HATCH, 1).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/crocodile_egg_two_chipped"))))
                .partialState().with(MiscUtil.EGGS_3, 2).with(AnimalEgg.HATCH, 2).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/crocodile_egg_two_cracked"))))

                .partialState().with(MiscUtil.EGGS_3, 3).with(AnimalEgg.HATCH, 0).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/crocodile_egg_three"))))
                .partialState().with(MiscUtil.EGGS_3, 3).with(AnimalEgg.HATCH, 1).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/crocodile_egg_three_chipped"))))
                .partialState().with(MiscUtil.EGGS_3, 3).with(AnimalEgg.HATCH, 2).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/crocodile_egg_three_cracked"))));

        this.getVariantBuilder(Primal_Blocks.EAGLE_EGG.get())
                .partialState().with(MiscUtil.EGGS_2, 1).with(AnimalEgg.HATCH, 0).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/eagle_egg_one"))))
                .partialState().with(MiscUtil.EGGS_2, 1).with(AnimalEgg.HATCH, 1).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/eagle_egg_one_chipped"))))
                .partialState().with(MiscUtil.EGGS_2, 1).with(AnimalEgg.HATCH, 2).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/eagle_egg_one_cracked"))))

                .partialState().with(MiscUtil.EGGS_2, 2).with(AnimalEgg.HATCH, 0).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/eagle_egg_two"))))
                .partialState().with(MiscUtil.EGGS_2, 2).with(AnimalEgg.HATCH, 1).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/eagle_egg_two_chipped"))))
                .partialState().with(MiscUtil.EGGS_2, 2).with(AnimalEgg.HATCH, 2).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/eagle_egg_two_cracked"))));

        this.createAnimalNest(
                Primal_Blocks.NEST_BLOCK,
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/nest_base"),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/nest_north_side"),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/nest_south_side"),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/nest_east_side"),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/nest_west_side"));
    }

    private void simpleStairs(DeferredHolder<Block, Block> stairs, DeferredHolder<Block, Block> baseBlock){
        this.stairsBlock((StairBlock) stairs.get(),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+baseBlock.getId().getPath()));
    }

    private void simpleSlab(DeferredHolder<Block, Block> slab, DeferredHolder<Block, Block> baseBlock){
        this.slabBlock((SlabBlock) slab.get(),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+baseBlock.getId().getPath()),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+baseBlock.getId().getPath()));
    }

    private void createSharkTooth(DeferredHolder<Block, Block> block,
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
    private void createSimilarToPinkPetals(DeferredHolder<Block, Block> block, DirectionProperty property, IntegerProperty amountProperty, ResourceLocation amount1, ResourceLocation amount2, ResourceLocation amount3, ResourceLocation amount4){
        this.getMultipartBuilder(block.get())
                .part().modelFile(new ModelFile.UncheckedModelFile(amount1))
                .addModel().condition(property, Direction.NORTH).condition(amountProperty, 1, 2, 3, 4).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount1)).rotationY(90)
                .addModel().condition(property, Direction.EAST).condition(amountProperty, 1, 2, 3, 4).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount1)).rotationY(180)
                .addModel().condition(property, Direction.SOUTH).condition(amountProperty, 1, 2, 3, 4).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount1)).rotationY(270)
                .addModel().condition(property, Direction.WEST).condition(amountProperty, 1, 2, 3, 4).end()

                .part().modelFile(new ModelFile.UncheckedModelFile(amount2))
                .addModel().condition(property, Direction.NORTH).condition(amountProperty, 2, 3, 4).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount2)).rotationY(90)
                .addModel().condition(property, Direction.EAST).condition(amountProperty, 2, 3, 4).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount2)).rotationY(180)
                .addModel().condition(property, Direction.SOUTH).condition(amountProperty, 2, 3, 4).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount2)).rotationY(270)
                .addModel().condition(property, Direction.WEST).condition(amountProperty, 2, 3, 4).end()

                .part().modelFile(new ModelFile.UncheckedModelFile(amount3))
                .addModel().condition(property, Direction.NORTH).condition(amountProperty, 3, 4).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount3)).rotationY(90)
                .addModel().condition(property, Direction.EAST).condition(amountProperty, 3, 4).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount3)).rotationY(180)
                .addModel().condition(property, Direction.SOUTH).condition(amountProperty, 3, 4).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount3)).rotationY(270)
                .addModel().condition(property, Direction.WEST).condition(amountProperty, 3, 4).end()

                .part().modelFile(new ModelFile.UncheckedModelFile(amount4))
                .addModel().condition(property, Direction.NORTH).condition(amountProperty, 4).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount4)).rotationY(90)
                .addModel().condition(property, Direction.EAST).condition(amountProperty, 4).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount4)).rotationY(180)
                .addModel().condition(property, Direction.SOUTH).condition(amountProperty, 4).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount4)).rotationY(270)
                .addModel().condition(property, Direction.WEST).condition(amountProperty, 4).end();
    }

    @SuppressWarnings("all")
    private void createAnimalNest(DeferredHolder<Block, Block> block,
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

    private PropertyDispatch createColumnWithFacing() {
        return PropertyDispatch.property(BlockStateProperties.FACING)
                .select(Direction.DOWN, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
                .select(Direction.UP, Variant.variant())
                .select(Direction.NORTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
                .select(
                        Direction.SOUTH,
                        Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
                )
                .select(
                        Direction.WEST,
                        Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
                )
                .select(
                        Direction.EAST,
                        Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                );
    }
}
