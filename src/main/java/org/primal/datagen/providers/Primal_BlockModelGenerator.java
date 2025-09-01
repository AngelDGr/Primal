package org.primal.datagen.providers;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.primal.Primal_Main;
import org.primal.registry.Primal_Blocks;

public class Primal_BlockModelGenerator extends BlockModelProvider {

    public Primal_BlockModelGenerator(final PackOutput output, final ExistingFileHelper existingFileHelper) {
        super(output, Primal_Main.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.withExistingParent("shark_tooth_base", ResourceLocation.withDefaultNamespace("block/pointed_dripstone"))
                .texture("cross", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/shark_tooth_base"));
        this.withExistingParent("shark_tooth_tip", ResourceLocation.withDefaultNamespace("block/pointed_dripstone"))
                .texture("cross", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/shark_tooth_tip"));
        this.withExistingParent("shark_tooth_tip_down", ResourceLocation.withDefaultNamespace("block/pointed_dripstone"))
                .texture("cross", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/shark_tooth_tip_down"));

        this.createCubeAll(Primal_Blocks.CROCODILE_SCUTE_BLOCK);

        this.createCubeAll(Primal_Blocks.CROCODILE_SCUTE_SHINGLE);

        this.createCubeAll(Primal_Blocks.CHISELED_CROCODILE_SCUTE);

        this.simpleStairs(Primal_Blocks.CROCODILE_SCUTE_STAIRS, Primal_Blocks.CROCODILE_SCUTE_BLOCK);

        this.simpleSlab(Primal_Blocks.CROCODILE_SCUTE_SLAB, Primal_Blocks.CROCODILE_SCUTE_BLOCK);

        this.tintedOverlayCross("river_reeds_top_0",
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/river_reeds_top_0"),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/river_reeds_top_0_overlay"));
        this.tintedOverlayCross("river_reeds_top_1",
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/river_reeds_top_1"),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/river_reeds_top_1_overlay"));
        this.tintedOverlayCross("river_reeds_middle_0",
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/river_reeds_middle_0"),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/river_reeds_middle_0_overlay"));
        this.tintedOverlayCross("river_reeds_middle_1",
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/river_reeds_middle_1"),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/river_reeds_middle_1_overlay"));

        this.cross("river_reeds_bottom", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/river_reeds_bottom"));

        this.tintedOverlayCross(Primal_Blocks.SHORT_RIVER_REEDS.getId().getPath(),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/short_river_reeds"),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/short_river_reeds_overlay"));

        this.createEggModel("crocodile_egg_one", "template_one_crocodile_egg");
        this.createEggModel("crocodile_egg_one_chipped", "template_one_crocodile_egg");
        this.createEggModel("crocodile_egg_one_cracked", "template_one_crocodile_egg");

        this.createEggModel("crocodile_egg_two", "template_two_crocodile_eggs");
        this.createEggModel("crocodile_egg_two_chipped", "template_two_crocodile_eggs");
        this.createEggModel("crocodile_egg_two_cracked", "template_two_crocodile_eggs");

        this.createEggModel("crocodile_egg_three", "template_three_crocodile_eggs");
        this.createEggModel("crocodile_egg_three_chipped", "template_three_crocodile_eggs");
        this.createEggModel("crocodile_egg_three_cracked", "template_three_crocodile_eggs");
    }

    public void tintedCross(String name, ResourceLocation cross) {
        singleTexture(name, BLOCK_FOLDER + "/tinted_cross", "cross", cross);
    }

    public void tintedOverlayCross(String name, ResourceLocation cross, ResourceLocation overlay) {
        withExistingParent(name, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "tinted_cross_overlay"))
                .texture("cross", cross)
                .texture("overlay", overlay);
    }


    private void createCubeAll(DeferredHolder<Block, Block> block){
        this.cubeAll(block.getId().getPath(),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+block.getId().getPath()));
    }

    private void simpleStairs(DeferredHolder<Block, Block> stairs, DeferredHolder<Block, Block> baseBlock){
        this.stairs(stairs.getId().getPath(),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+baseBlock.getId().getPath()),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+baseBlock.getId().getPath()),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+baseBlock.getId().getPath()));
    }

    private void simpleSlab(DeferredHolder<Block, Block> slab, DeferredHolder<Block, Block> baseBlock){
        this.slab(slab.getId().getPath(),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+baseBlock.getId().getPath()),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+baseBlock.getId().getPath()),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+baseBlock.getId().getPath()));
    }

    private void createCross(DeferredHolder<Block, Block> block, boolean tinted){
        if(tinted)
            this.tintedCross(block.getId().getPath(),
                    ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+block.getId().getPath()));
        else
            this.cross(block.getId().getPath(),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+block.getId().getPath()));
    }


    private BlockModelBuilder singleTexture(String name, String parent, String textureKey, ResourceLocation texture) {
        return singleTexture(name, mcLoc(parent), textureKey, texture);
    }

    private void createEggModel(String name, String type){
        this.createEggModel(name, type, name);
    }

    private void createEggModel(String name, String parent, String texture){
        this.withExistingParent(name, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, parent))
                .texture("egg", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+texture));
    }

}
