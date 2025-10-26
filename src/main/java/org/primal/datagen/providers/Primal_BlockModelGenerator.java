package org.primal.datagen.providers;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;
import org.primal.Primal_Main;
import org.primal.registry.Primal_Blocks;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class Primal_BlockModelGenerator extends BlockModelProvider {

    public Primal_BlockModelGenerator(final PackOutput output, final ExistingFileHelper existingFileHelper) {
        super(output, Primal_Main.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.withExistingParentFalseAmbientOcclusion("shark_tooth_base", ResourceLocation.withDefaultNamespace("block/pointed_dripstone"))
                .texture("cross", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/shark_tooth_base"));
        this.withExistingParentFalseAmbientOcclusion("shark_tooth_tip", ResourceLocation.withDefaultNamespace("block/pointed_dripstone"))
                .texture("cross", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/shark_tooth_tip"));
        this.withExistingParent("shark_tooth_tip_down", ResourceLocation.withDefaultNamespace("block/pointed_dripstone"))
                .texture("cross", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/shark_tooth_tip_down"));

        this.createCubeAll(Primal_Blocks.CROCODILE_SCUTE_BLOCK);
        this.createCubeAll(Primal_Blocks.CROCODILE_SCUTE_SHINGLE);
        this.createCubeAll(Primal_Blocks.CHISELED_CROCODILE_SCUTE);
        this.simpleStairs(Primal_Blocks.CROCODILE_SCUTE_STAIRS, Primal_Blocks.CROCODILE_SCUTE_BLOCK);
        this.simpleSlab(Primal_Blocks.CROCODILE_SCUTE_SLAB, Primal_Blocks.CROCODILE_SCUTE_BLOCK);

        this.createCubeAll(Primal_Blocks.ARID_CROCODILE_SCUTE_BLOCK);
        this.createCubeAll(Primal_Blocks.ARID_CROCODILE_SCUTE_SHINGLE);
        this.createCubeAll(Primal_Blocks.ARID_CHISELED_CROCODILE_SCUTE);
        this.simpleStairs(Primal_Blocks.ARID_CROCODILE_SCUTE_STAIRS, Primal_Blocks.ARID_CROCODILE_SCUTE_BLOCK);
        this.simpleSlab(Primal_Blocks.ARID_CROCODILE_SCUTE_SLAB, Primal_Blocks.ARID_CROCODILE_SCUTE_BLOCK);

        this.createCubeAll(Primal_Blocks.HUMID_CROCODILE_SCUTE_BLOCK);
        this.createCubeAll(Primal_Blocks.HUMID_CROCODILE_SCUTE_SHINGLE);
        this.createCubeAll(Primal_Blocks.HUMID_CHISELED_CROCODILE_SCUTE);
        this.simpleStairs(Primal_Blocks.HUMID_CROCODILE_SCUTE_STAIRS, Primal_Blocks.HUMID_CROCODILE_SCUTE_BLOCK);
        this.simpleSlab(Primal_Blocks.HUMID_CROCODILE_SCUTE_SLAB, Primal_Blocks.HUMID_CROCODILE_SCUTE_BLOCK);

        this.tintedOverlayCross("river_reeds_top_0",
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/river_reeds_top_0"),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/river_reeds_top_0_overlay"));
        this.tintedOverlayCross("river_reeds_top_1",
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/river_reeds_top_1"),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/river_reeds_top_1_overlay"));

        this.tintedCross("river_reeds_middle",
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/river_reeds_middle"));

        this.tintedCross("river_reeds_bottom", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/river_reeds_bottom"));

        this.tintedCross(Primal_Blocks.SHORT_RIVER_REEDS.getId().getPath(),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/short_river_reeds"));

        this.createEggModel("eagle_egg_one",         "template_one_eagle_egg");
        this.createEggModel("eagle_egg_one_chipped", "template_one_eagle_egg");
        this.createEggModel("eagle_egg_one_cracked", "template_one_eagle_egg");

        this.createEggModel("eagle_egg_two",         "template_two_eagle_eggs");
        this.createEggModel("eagle_egg_two_chipped", "template_two_eagle_eggs");
        this.createEggModel("eagle_egg_two_cracked", "template_two_eagle_eggs");

        this.createEggModel("crocodile_egg_one",         "template_one_crocodile_egg");
        this.createEggModel("crocodile_egg_one_chipped", "template_one_crocodile_egg");
        this.createEggModel("crocodile_egg_one_cracked", "template_one_crocodile_egg");

        this.createEggModel("crocodile_egg_two",         "template_two_crocodile_eggs");
        this.createEggModel("crocodile_egg_two_chipped", "template_two_crocodile_eggs");
        this.createEggModel("crocodile_egg_two_cracked", "template_two_crocodile_eggs");

        this.createEggModel("crocodile_egg_three",         "template_three_crocodile_eggs");
        this.createEggModel("crocodile_egg_three_chipped", "template_three_crocodile_eggs");
        this.createEggModel("crocodile_egg_three_cracked", "template_three_crocodile_eggs");

        this.columBlock(Primal_Blocks.STRAW_BALE, "block/straw_bale_side", "block/straw_bale_top");
    }

    private void columBlock(DeferredHolder<Block, Block> block, String side, String top){
        this.cubeColumn(block.getRegisteredName(),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, side),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, top));

        this.cubeColumnHorizontal(block.getRegisteredName()+"_horizontal",
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, side),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, top));
    }

    private void tintedCross(String name, ResourceLocation cross) {
        singleTexture(name, BLOCK_FOLDER + "/tinted_cross", "cross", cross);
    }

    private void tintedOverlayCross(String name, ResourceLocation cross, ResourceLocation overlay) {
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

    private void singleTexture(String name, String parent, String textureKey, ResourceLocation texture) {
        singleTexture(name, mcLoc(parent), textureKey, texture);
    }

    private void createEggModel(String name, String type){
        this.createEggModel(name, type, name);
    }

    private void createEggModel(String name, String parent, String texture){
        this.withExistingParent(name, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, parent))
                .texture("egg", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+texture));
    }

    @VisibleForTesting
    public final Map<ResourceLocation, BlockModelBuilder> falseAmbientOcclusionGeneratedModels = new HashMap<>();


    public BlockModelBuilder withExistingParentFalseAmbientOcclusion(String name, ResourceLocation parent) {
        return getBuilderE(name).parent(getExistingFile(parent));
    }

    public BlockModelBuilder getBuilderE(String path) {
        Preconditions.checkNotNull(path, "Path must not be null");
        ResourceLocation outputLoc = extendWithFolder(path.contains(":") ? ResourceLocation.parse(path) : ResourceLocation.fromNamespaceAndPath(modid, path));
        this.existingFileHelper.trackGenerated(outputLoc, MODEL);
        return falseAmbientOcclusionGeneratedModels.computeIfAbsent(outputLoc, factory);
    }

    private ResourceLocation extendWithFolder(ResourceLocation rl) {
        if (rl.getPath().contains("/")) {
            return rl;
        }
        return ResourceLocation.fromNamespaceAndPath(rl.getNamespace(), folder + "/" + rl.getPath());
    }

    @Override
    protected @NotNull CompletableFuture<?> generateAll(@NotNull CachedOutput cache) {
        CompletableFuture<?>[] futures = new CompletableFuture<?>[this.generatedModels.size() + this.falseAmbientOcclusionGeneratedModels.size()];
        int i = 0;

        for (BlockModelBuilder model : this.generatedModels.values()) {
            Path target = getPath(model);
            futures[i++] = DataProvider.saveStable(cache, model.toJson(), target);
        }

        for (BlockModelBuilder model : this.falseAmbientOcclusionGeneratedModels.values()) {
            Path target = getPath(model);
            JsonObject finalJson=model.toJson();
            finalJson.addProperty("ambientocclusion", false);
            futures[i++] = DataProvider.saveStable(cache, finalJson, target);
        }

        return CompletableFuture.allOf(futures);
    }
}
