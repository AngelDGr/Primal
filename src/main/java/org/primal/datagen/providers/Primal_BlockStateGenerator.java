package org.primal.datagen.providers;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.primal.Primal_Main;
import org.primal.block.CrocodileEgg;
import org.primal.block.RiverReeds;
import org.primal.block.SeashellsBlock;
import org.primal.block.SharkToothBlock;
import org.primal.block.properties.SharkToothThickness;
import org.primal.block.properties.TripleBlockHalf;
import org.primal.registry.Primal_Blocks;

public class Primal_BlockStateGenerator extends BlockStateProvider {

    public Primal_BlockStateGenerator(final PackOutput output, final ExistingFileHelper existingFileHelper) {
        super(output, Primal_Main.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        this.getVariantBuilder(Primal_Blocks.SHARK_TOOTH.get())
                .partialState().with(SharkToothBlock.THICKNESS, SharkToothThickness.TIP).with(SharkToothBlock.TIP_DIRECTION, Direction.UP).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/shark_tooth_tip"))))
                .partialState().with(SharkToothBlock.THICKNESS, SharkToothThickness.BASE).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/shark_tooth_base"))))
                .partialState().with(SharkToothBlock.THICKNESS, SharkToothThickness.TIP).with(SharkToothBlock.TIP_DIRECTION, Direction.DOWN).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/shark_tooth_tip_down"))));

        this.createSimilarToPinkPetals(Primal_Blocks.SEASHELLS, SeashellsBlock.FACING,
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
                .partialState().with(CrocodileEgg.EGGS, 1).with(CrocodileEgg.HATCH, 0).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/crocodile_egg_one"))))
                .partialState().with(CrocodileEgg.EGGS, 1).with(CrocodileEgg.HATCH, 1).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/crocodile_egg_one_chipped"))))
                .partialState().with(CrocodileEgg.EGGS, 1).with(CrocodileEgg.HATCH, 2).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/crocodile_egg_one_cracked"))))

                .partialState().with(CrocodileEgg.EGGS, 2).with(CrocodileEgg.HATCH, 0).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/crocodile_egg_two"))))
                .partialState().with(CrocodileEgg.EGGS, 2).with(CrocodileEgg.HATCH, 1).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/crocodile_egg_two_chipped"))))
                .partialState().with(CrocodileEgg.EGGS, 2).with(CrocodileEgg.HATCH, 2).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/crocodile_egg_two_cracked"))))

                .partialState().with(CrocodileEgg.EGGS, 3).with(CrocodileEgg.HATCH, 0).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/crocodile_egg_three"))))
                .partialState().with(CrocodileEgg.EGGS, 3).with(CrocodileEgg.HATCH, 1).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/crocodile_egg_three_chipped"))))
                .partialState().with(CrocodileEgg.EGGS, 3).with(CrocodileEgg.HATCH, 2).addModels(
                        new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/crocodile_egg_three_cracked"))));
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

    private void createSimilarToPinkPetals(DeferredHolder<Block, Block> block, DirectionProperty property, ResourceLocation amount1, ResourceLocation amount2, ResourceLocation amount3, ResourceLocation amount4){
        this.getMultipartBuilder(block.get())
                .part().modelFile(new ModelFile.UncheckedModelFile(amount1))
                .addModel().condition(property, Direction.NORTH).condition(SeashellsBlock.AMOUNT, 1, 2, 3, 4).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount1)).rotationY(90)
                .addModel().condition(property, Direction.EAST).condition(SeashellsBlock.AMOUNT, 1, 2, 3, 4).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount1)).rotationY(180)
                .addModel().condition(property, Direction.SOUTH).condition(SeashellsBlock.AMOUNT, 1, 2, 3, 4).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount1)).rotationY(270)
                .addModel().condition(property, Direction.WEST).condition(SeashellsBlock.AMOUNT, 1, 2, 3, 4).end()

                .part().modelFile(new ModelFile.UncheckedModelFile(amount2))
                .addModel().condition(property, Direction.NORTH).condition(SeashellsBlock.AMOUNT, 2, 3, 4).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount2)).rotationY(90)
                .addModel().condition(property, Direction.EAST).condition(SeashellsBlock.AMOUNT, 2, 3, 4).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount2)).rotationY(180)
                .addModel().condition(property, Direction.SOUTH).condition(SeashellsBlock.AMOUNT, 2, 3, 4).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount2)).rotationY(270)
                .addModel().condition(property, Direction.WEST).condition(SeashellsBlock.AMOUNT, 2, 3, 4).end()

                .part().modelFile(new ModelFile.UncheckedModelFile(amount3))
                .addModel().condition(property, Direction.NORTH).condition(SeashellsBlock.AMOUNT, 3, 4).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount3)).rotationY(90)
                .addModel().condition(property, Direction.EAST).condition(SeashellsBlock.AMOUNT, 3, 4).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount3)).rotationY(180)
                .addModel().condition(property, Direction.SOUTH).condition(SeashellsBlock.AMOUNT, 3, 4).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount3)).rotationY(270)
                .addModel().condition(property, Direction.WEST).condition(SeashellsBlock.AMOUNT, 3, 4).end()

                .part().modelFile(new ModelFile.UncheckedModelFile(amount4))
                .addModel().condition(property, Direction.NORTH).condition(SeashellsBlock.AMOUNT, 4).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount4)).rotationY(90)
                .addModel().condition(property, Direction.EAST).condition(SeashellsBlock.AMOUNT, 4).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount4)).rotationY(180)
                .addModel().condition(property, Direction.SOUTH).condition(SeashellsBlock.AMOUNT, 4).end()
                .part().modelFile(new ModelFile.UncheckedModelFile(amount4)).rotationY(270)
                .addModel().condition(property, Direction.WEST).condition(SeashellsBlock.AMOUNT, 4).end();
    }


}
