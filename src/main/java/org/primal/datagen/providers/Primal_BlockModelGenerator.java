package org.primal.datagen.providers;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
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
        this.withExistingParent(Primal_Blocks.CHOMP_TRAP_GREEN.getId().getPath(), ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/template_chomp_trap"))
                .texture("texture", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/chomp_trap/green"))
                .texture("particle", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/crocodile_scute_block"));

        this.createCubeAll(Primal_Blocks.ARID_CROCODILE_SCUTE_BLOCK);
        this.createCubeAll(Primal_Blocks.ARID_CROCODILE_SCUTE_SHINGLE);
        this.createCubeAll(Primal_Blocks.ARID_CHISELED_CROCODILE_SCUTE);
        this.simpleStairs(Primal_Blocks.ARID_CROCODILE_SCUTE_STAIRS, Primal_Blocks.ARID_CROCODILE_SCUTE_BLOCK);
        this.simpleSlab(Primal_Blocks.ARID_CROCODILE_SCUTE_SLAB, Primal_Blocks.ARID_CROCODILE_SCUTE_BLOCK);
        this.withExistingParent(Primal_Blocks.CHOMP_TRAP_ARID.getId().getPath(), ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/template_chomp_trap"))
                .texture("texture", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/chomp_trap/arid"))
                .texture("particle", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/arid_crocodile_scute_block"));

        this.createCubeAll(Primal_Blocks.HUMID_CROCODILE_SCUTE_BLOCK);
        this.createCubeAll(Primal_Blocks.HUMID_CROCODILE_SCUTE_SHINGLE);
        this.createCubeAll(Primal_Blocks.HUMID_CHISELED_CROCODILE_SCUTE);
        this.simpleStairs(Primal_Blocks.HUMID_CROCODILE_SCUTE_STAIRS, Primal_Blocks.HUMID_CROCODILE_SCUTE_BLOCK);
        this.simpleSlab(Primal_Blocks.HUMID_CROCODILE_SCUTE_SLAB, Primal_Blocks.HUMID_CROCODILE_SCUTE_BLOCK);
        this.withExistingParent(Primal_Blocks.CHOMP_TRAP_HUMID.getId().getPath(), ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/template_chomp_trap"))
                .texture("texture", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/chomp_trap/humid"))
                .texture("particle", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/humid_crocodile_scute_block"));

        this.tintedOverlayCross("river_reeds_top_0",
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/river_reeds_top_0"),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/river_reeds_top_0_overlay"));
        this.tintedOverlayCross("river_reeds_top_1",
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/river_reeds_top_1"),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/river_reeds_top_1_overlay"));
        this.tintedCross("river_reeds_middle",
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/river_reeds_middle"));
        this.tintedCross("river_reeds_bottom", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/river_reeds_bottom"));

        this.tintedCross(Primal_Blocks.SHORT_RIVER_REEDS);

        this.tintedOverlayCross("cattails_top_0",
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/cattails_top_0"),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/cattails_top_0_overlay"));
        this.tintedOverlayCross("cattails_top_1",
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/cattails_top_1"),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/cattails_top_1_overlay"));
        this.tintedCross("cattails_middle",
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/cattails_middle"));
        this.tintedCross("cattails_bottom", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/cattails_bottom"));

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

        this.createEggModel("cassowary_egg",         "template_cassowary_egg");
        this.createEggModel("cassowary_egg_chipped",         "template_cassowary_egg");
        this.createEggModel("cassowary_egg_cracked",         "template_cassowary_egg");

        this.createSeashells("seashells");
        this.createSeashells("cold_seashells");
        this.createSeashells("temperate_seashells");

        this.columBlock(Primal_Blocks.STRAW_BALE, "block/straw_bale_side", "block/straw_bale_top");
        this.columBlock(Primal_Blocks.DRIED_STRAW_BALE, "block/dried_straw_bale_side", "block/dried_straw_bale_top");
        this.createCubeAll(Primal_Blocks.WEAVED_STRAW);
        this.simpleStairs(Primal_Blocks.WEAVED_STRAW_STAIRS, Primal_Blocks.WEAVED_STRAW);
        this.simpleSlab(Primal_Blocks.WEAVED_STRAW_SLAB, Primal_Blocks.WEAVED_STRAW);
        this.withoutParentInvisible(Primal_Blocks.STRAW_BASKET, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/weaved_straw"));

        this.thornyAcacia(Primal_Blocks.THORNY_ACACIA_LOG);
        this.thornyAcacia(Primal_Blocks.THORNY_ACACIA_WOOD);

        this.hollowLog(Primal_Blocks.HOLLOW_OAK_LOG, "oak");
        this.hollowLog(Primal_Blocks.HOLLOW_SPRUCE_LOG, "spruce");
        this.hollowLog(Primal_Blocks.HOLLOW_BIRCH_LOG, "birch");
        this.hollowLog(Primal_Blocks.HOLLOW_JUNGLE_LOG, "jungle");
        this.hollowLog(Primal_Blocks.HOLLOW_ACACIA_LOG, "acacia");
        this.hollowLog(Primal_Blocks.HOLLOW_DARK_OAK_LOG, "dark_oak");
        this.hollowLog(Primal_Blocks.HOLLOW_MANGROVE_LOG, "mangrove");

        this.cross(Primal_Blocks.THORNY_ACACIA_SAPLING.getId().getPath(), ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/thorny_acacia_sapling"));
        this.tintedCross(Primal_Blocks.LITCHI_SAPLING);
        this.tintedCross(Primal_Blocks.KIWANO_SAPLING);
        this.tintedCross(Primal_Blocks.STARFRUIT_SAPLING);

        this.createAntlerModel(Primal_Blocks.FALLOW_DEER_ANTLER, "fallow");
        this.createAntlerModel(Primal_Blocks.WHITETAIL_DEER_ANTLER, "whitetail");
        this.createAntlerModel(Primal_Blocks.REINDEER_ANTLER, "reindeer");
    }

    private void columBlock(RegistryObject<Block> block, String side, String top){
        this.cubeColumn(block.getId().getPath(),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, side),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, top));

        this.cubeColumnHorizontal(block.getId().getPath()+"_horizontal",
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, side),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, top));
    }

    private void hollowLog(RegistryObject<Block> block, String name){
        //Normal
        this.cube(block.getId().getPath(),
                        //Down
                        ResourceLocation.withDefaultNamespace("block/"+name+"_log_top"),
                        //Up
                        ResourceLocation.withDefaultNamespace("block/"+name+"_log_top"),
                        //North
                        ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/hollow_log/"+name),
                        //South
                        ResourceLocation.withDefaultNamespace("block/"+name+"_log"),
                        //East
                        ResourceLocation.withDefaultNamespace("block/"+name+"_log"),
                        //West
                        ResourceLocation.withDefaultNamespace("block/"+name+"_log"))
                .texture("particle", ResourceLocation.withDefaultNamespace("block/"+name+"_log"));

        //Side
        this.cube(block.getId().getPath()+"_side",
                        //Down
                        ResourceLocation.withDefaultNamespace("block/"+name+"_log_top"),
                        //Up
                        ResourceLocation.withDefaultNamespace("block/"+name+"_log_top"),
                        //North
                        ResourceLocation.withDefaultNamespace("block/"+name+"_log"),
                        //South
                        ResourceLocation.withDefaultNamespace("block/"+name+"_log"),
                        //East
                        ResourceLocation.withDefaultNamespace("block/"+name+"_log"),
                        //West
                        ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/hollow_log/"+name)
                )
                .texture("particle", ResourceLocation.withDefaultNamespace("block/"+name+"_log"));
    }

    private void tintedCross(RegistryObject<Block> block) {
        singleTexture(block.getId().getPath(), BLOCK_FOLDER + "/tinted_cross", "cross", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+block.getId().getPath()));
    }

    private void tintedCross(String name, ResourceLocation cross) {
        singleTexture(name, BLOCK_FOLDER + "/tinted_cross", "cross", cross);
    }

    private void tintedOverlayCross(String name, ResourceLocation cross, ResourceLocation overlay) {
        withExistingParent(name, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "tinted_cross_overlay"))
                .texture("cross", cross)
                .texture("overlay", overlay);
    }

    private void createSeashells(String name) {
        withExistingParent(name+"_1", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "template_seashells_1"))
                .texture("texture", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+ name));

        withExistingParent(name+"_2", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "template_seashells_2"))
                .texture("texture", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+ name));

        withExistingParent(name+"_3", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "template_seashells_3"))
                .texture("texture", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+ name));

        withExistingParent(name+"_4", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "template_seashells_4"))
                .texture("texture", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+ name));


        withExistingParent(name+"_1_snowy", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "template_seashells_1_snowy"))
                .texture("texture", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+ name));

        withExistingParent(name+"_2_snowy", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "template_seashells_2_snowy"))
                .texture("texture", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+ name));

        withExistingParent(name+"_3_snowy", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "template_seashells_3_snowy"))
                .texture("texture", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+ name));

        withExistingParent(name+"_4_snowy", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "template_seashells_4_snowy"))
                .texture("texture", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+ name));
    }

    private void thornyAcacia(RegistryObject<Block> block) {
        this.withExistingParent(block.getId().getPath(), ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "template_thorny_acacia"))
                .texture("texture", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+block.getId().getPath()));
    }

    private void createCubeAll(RegistryObject<Block> block){
        this.cubeAll(block.getId().getPath(),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+block.getId().getPath()));
    }

    private void simpleStairs(RegistryObject<Block> stairs, RegistryObject<Block> baseBlock){
        this.stairs(stairs.getId().getPath(),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+baseBlock.getId().getPath()),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+baseBlock.getId().getPath()),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+baseBlock.getId().getPath()));
    }

    private void simpleSlab(RegistryObject<Block> slab, RegistryObject<Block> baseBlock){
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

    private void withoutParentInvisible(RegistryObject<Block> name, ResourceLocation particle){
        this.getBuilder(name.getId().getPath())
                .texture("particle", particle);
    }

    private void createEggModel(String name, String parent, String texture){
        this.withExistingParent(name, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, parent))
                .texture("egg", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/"+texture));
    }

    private void createAntlerModel(RegistryObject<Block> block, String texture){
        this.withExistingParentFalseAmbientOcclusion(block.getId().getPath(), ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/template_antler"))
                .texture("texture", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/antler_block/"+texture))
                .texture("particle", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/antler_block/"+texture+"_particle"));

        this.withExistingParentFalseAmbientOcclusion(block.getId().getPath()+"_right", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/template_antler_right"))
                .texture("texture", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/antler_block/"+texture))
                .texture("particle", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/antler_block/"+texture+"_particle"));

        this.withExistingParentFalseAmbientOcclusion(block.getId().getPath()+"_double", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/template_antler_double"))
                .texture("texture", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/antler_block/"+texture))
                .texture("particle", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/antler_block/"+texture+"_particle"));

        this.withExistingParentFalseAmbientOcclusion(block.getId().getPath()+"_wall", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/template_antler_wall"))
                .texture("texture", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/antler_block/"+texture))
                .texture("particle", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/antler_block/"+texture+"_particle"));

        this.withExistingParentFalseAmbientOcclusion(block.getId().getPath()+"_wall_right", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/template_antler_wall_right"))
                .texture("texture", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/antler_block/"+texture))
                .texture("particle", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/antler_block/"+texture+"_particle"));
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
