package org.primal.datagen.providers;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import org.primal.Primal_Main;
import org.primal.entity.animal.SnakeEntity;
import org.primal.registry.Primal_Blocks;
import org.primal.registry.Primal_Items;

import java.util.Objects;

public class Primal_ItemModelGenerator extends ItemModelProvider {

    public Primal_ItemModelGenerator(final PackOutput output, final ExistingFileHelper existingFileHelper) {
        super(output, Primal_Main.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(Primal_Items.PRIMAL.get());

        eggItem(Primal_Items.BEAR_SPAWN_EGG.get());
        eggItem(Primal_Items.SHARK_SPAWN_EGG.get());
        eggItem(Primal_Items.CROCODILE_SPAWN_EGG.get());
        eggItem(Primal_Items.EAGLE_SPAWN_EGG.get());
        eggItem(Primal_Items.CASSOWARY_SPAWN_EGG.get());
        eggItem(Primal_Items.WALRUS_SPAWN_EGG.get());
        eggItem(Primal_Items.LION_SPAWN_EGG.get());
        eggItem(Primal_Items.SNAKE_SPAWN_EGG.get());
        eggItem(Primal_Items.DEER_SPAWN_EGG.get());

        basicItem(Primal_Items.APPLE_FRITTER.get());
        basicItem(Primal_Items.GOLDEN_APPLE_FRITTER.get());
        basicLayered(Primal_Items.ENCHANTED_GOLDEN_APPLE_FRITTER.get(),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "item/golden_apple_fritter"));

        basicItem(Primal_Items.SHARK_TOOTH.get());
        basicItem(Primal_Items.WARM_SEASHELLS.get());
        basicItem(Primal_Items.COLD_SEASHELLS.get());
        basicItem(Primal_Items.TEMPERATE_SEASHELLS.get());

        basicItem(Primal_Items.CROCODILE_SCUTE.get());

        basicItem(Primal_Items.VENISON.get());
        basicItem(Primal_Items.COOKED_VENISON.get());
        basicItem(Primal_Items.FALLOW_DEER_ANTLER.get());
        basicItem(Primal_Items.REINDEER_ANTLER.get());
        basicItem(Primal_Items.WHITETAIL_DEER_ANTLER.get());

        conchShell(Primal_Items.WARM_CONCH_SHELL, "item/conch_shell_warm");
        conchShell(Primal_Items.TEMPERATE_CONCH_SHELL, "item/conch_shell_temperate");
        conchShell(Primal_Items.COLD_CONCH_SHELL, "item/conch_shell_cold");

        simpleBlockItem(Primal_Blocks.THORNY_ACACIA_LOG.get());
        simpleBlockItem(Primal_Blocks.THORNY_ACACIA_WOOD.get());
        simpleBlockItem(Primal_Blocks.THORNY_ACACIA_LEAVES.get());
        basicLayered(Primal_Items.THORNY_ACACIA_SAPLING.get(),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/thorny_acacia_sapling"));
        basicItem(Primal_Items.EXPLOSEED.get());

        simpleBlockItem(Primal_Blocks.HOLLOW_OAK_LOG.get());
        simpleBlockItem(Primal_Blocks.HOLLOW_SPRUCE_LOG.get());
        simpleBlockItem(Primal_Blocks.HOLLOW_BIRCH_LOG.get());
        simpleBlockItem(Primal_Blocks.HOLLOW_JUNGLE_LOG.get());
        simpleBlockItem(Primal_Blocks.HOLLOW_ACACIA_LOG.get());
        simpleBlockItem(Primal_Blocks.HOLLOW_DARK_OAK_LOG.get());
        simpleBlockItem(Primal_Blocks.HOLLOW_MANGROVE_LOG.get());

        simpleBlockItem(Primal_Blocks.CROCODILE_SCUTE_BLOCK.get());
        simpleBlockItem(Primal_Blocks.CROCODILE_SCUTE_SHINGLE.get());
        simpleBlockItem(Primal_Blocks.CHISELED_CROCODILE_SCUTE.get());
        simpleBlockItem(Primal_Blocks.CROCODILE_SCUTE_STAIRS.get());
        simpleBlockItem(Primal_Blocks.CROCODILE_SCUTE_SLAB.get());
        simpleBlockItem(Primal_Blocks.CHOMP_TRAP_GREEN.get());

        simpleBlockItem(Primal_Blocks.ARID_CROCODILE_SCUTE_BLOCK.get());
        simpleBlockItem(Primal_Blocks.ARID_CROCODILE_SCUTE_SHINGLE.get());
        simpleBlockItem(Primal_Blocks.ARID_CHISELED_CROCODILE_SCUTE.get());
        simpleBlockItem(Primal_Blocks.ARID_CROCODILE_SCUTE_STAIRS.get());
        simpleBlockItem(Primal_Blocks.ARID_CROCODILE_SCUTE_SLAB.get());
        simpleBlockItem(Primal_Blocks.CHOMP_TRAP_ARID.get());

        simpleBlockItem(Primal_Blocks.HUMID_CROCODILE_SCUTE_BLOCK.get());
        simpleBlockItem(Primal_Blocks.HUMID_CROCODILE_SCUTE_SHINGLE.get());
        simpleBlockItem(Primal_Blocks.HUMID_CHISELED_CROCODILE_SCUTE.get());
        simpleBlockItem(Primal_Blocks.HUMID_CROCODILE_SCUTE_STAIRS.get());
        simpleBlockItem(Primal_Blocks.HUMID_CROCODILE_SCUTE_SLAB.get());
        simpleBlockItem(Primal_Blocks.CHOMP_TRAP_HUMID.get());

        basicLayered("river_reeds",
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "item/river_reeds"),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "item/river_reeds_overlay"));

        basicLayered("cattails",
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "item/cattails"),
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "item/cattails_overlay"));

        basicLayered("short_river_reeds",
                ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "item/short_river_reeds"));

        basicItem(Primal_Items.CROCODILE_EGG.get());
        basicItem(Primal_Items.EAGLE_EGG.get());
        basicItem(Primal_Items.CASSOWARY_EGG.get());
        basicItem(Primal_Items.PETRIFIED_FRUIT.get());
        basicItem(Primal_Items.LITCHI.get());
        basicItem(Primal_Items.LITCHI_SEEDS.get());
        basicItem(Primal_Items.KIWANO.get());
        basicItem(Primal_Items.KIWANO_SEEDS.get());
        basicItem(Primal_Items.STARFRUIT.get());
        basicItem(Primal_Items.WEIRD_APPLE.get());
        basicItem(Primal_Items.STARFRUIT_SEEDS.get());

        simpleBlockItem(Primal_Blocks.NEST_BLOCK.get());

        basicItem(Primal_Items.PAW_BANNER_PATTERN.get());
        basicItem(Primal_Items.JAWS_BANNER_PATTERN.get());
        basicItem(Primal_Items.MARSH_BANNER_PATTERN.get());
        basicItem(Primal_Items.EYRIE_BANNER_PATTERN.get());
        basicItem(Primal_Items.SLITHER_BANNER_PATTERN.get());
        basicItem(Primal_Items.ROYAL_BANNER_PATTERN.get());
        musicDisc(Primal_Items.MUSIC_DISC_OH_DEER);

        simpleBlockItem(Primal_Blocks.STRAW_BALE.get());
        simpleBlockItem(Primal_Blocks.DRIED_STRAW_BALE.get());
        simpleBlockItem(Primal_Blocks.WEAVED_STRAW.get());
        simpleBlockItem(Primal_Blocks.WEAVED_STRAW_STAIRS.get());
        simpleBlockItem(Primal_Blocks.WEAVED_STRAW_SLAB.get());
        basicItem(Primal_Items.DREAMCATCHER.get());

        placeHolderSnake(Primal_Items.PLACEHOLDER_CHESTED_SNAKE, SnakeEntity.Variant.MARINE);
        chestedSnakeVariant(SnakeEntity.Variant.MARINE);

        chestItem(Primal_Items.STRAW_BASKET, ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "block/weaved_straw"));
    }

    protected void eggItem(final Item eggItem) {
        getBuilder(
                Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(eggItem)).toString())
                .parent(new ModelFile.UncheckedModelFile("item/template_spawn_egg"));
    }

    private void conchShell(RegistryObject<Item> item, String texture){
        var tooting= this.withExistingParent(item.getId().getPath()+"_tooting", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "item/tooting_conch_shell"))
                .texture("texture", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, texture));

        this.withExistingParent(item.getId().getPath(), ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "item/conch_shell"))
                .texture("texture", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, texture))
                .override().predicate(ResourceLocation.parse("tooting"), 1)
                .model(tooting);
    }

    public void basicLayered(Item item, ResourceLocation... textureLocations) {
        basicLayered(item.toString(), textureLocations);
    }

    public void basicLayered(String name, ResourceLocation... layers) {
        var builder= getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile("item/generated"));

        for(int i=0; i<layers.length; i++){
            builder.texture("layer"+i, layers[i]);
        }
    }

    public void simpleBlockItem(Block block) {
        simpleBlockItem(Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block)));
    }

    public void simpleBlockItem(ResourceLocation block) {
        withExistingParent(block.toString(), ResourceLocation.fromNamespaceAndPath(block.getNamespace(), "block/" + block.getPath()));
    }

    private void withParent(RegistryObject<Item> item, ResourceLocation parent, ResourceLocation texture){
        this.withExistingParent(item.getId().getPath(), parent)
                .texture("texture", texture);
    }

    private void musicDisc(RegistryObject<Item> item){
        this.withExistingParent(item.getId().getPath(), ResourceLocation.withDefaultNamespace("item/template_music_disc"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "item/"+item.getId().getPath()));
    }

    public ItemModelBuilder chestItem(RegistryObject<Item> item, ResourceLocation base) {
        return getBuilder(item.getId().getPath())
                .parent(new ModelFile.UncheckedModelFile("item/chest"))
                .texture("particle", base);
    }

    public void placeHolderSnake(RegistryObject<Item> item, SnakeEntity.Variant... variants) {
        var builder = getBuilder(item.getId().getPath())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(
                        Primal_Main.MOD_ID,
                        "item/" + item.getId().getPath()
                ));

        for (SnakeEntity.Variant variant : variants) {
            builder.override()
                    .model(new ModelFile.UncheckedModelFile(
                            ResourceLocation.fromNamespaceAndPath(
                                    Primal_Main.MOD_ID,
                                    "item/placeholder_chested_snake_" + variant.getSerializedName()
                            )
                    ))
                    .predicate(
                            ResourceLocation.fromNamespaceAndPath(
                                    Primal_Main.MOD_ID,
                                    variant.getSerializedName()
                            ),
                            1
                    );
        }
    }

    public void chestedSnakeVariant(SnakeEntity.Variant variant) {
        getBuilder("placeholder_chested_snake_" + variant.getSerializedName())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "item/placeholder_chested_snake_" + variant.getSerializedName()));
    }
}
