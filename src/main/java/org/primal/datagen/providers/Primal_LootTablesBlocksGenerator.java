package org.primal.datagen.providers;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.LimitCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.block.*;
import org.primal.registry.Primal_Blocks;
import org.primal.registry.Primal_Items;

import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

public class Primal_LootTablesBlocksGenerator extends BlockLootSubProvider {

    public Primal_LootTablesBlocksGenerator() {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS);
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return BuiltInRegistries.BLOCK.entrySet().stream()
                .filter(e -> e.getKey().location().getNamespace().equals(Primal_Main.MOD_ID)).map(Map.Entry::getValue).toList();
    }

    @Override
    public void generate() {
        this.createDropThemselves(
                Primal_Blocks.SHARK_TOOTH.get(),
                Primal_Blocks.STRAW_BALE.get(),
                Primal_Blocks.DRIED_STRAW_BALE.get(),
                Primal_Blocks.WEAVED_STRAW.get(),
                Primal_Blocks.WEAVED_STRAW_STAIRS.get(),
                Primal_Blocks.WEAVED_STRAW_SLAB.get(),
                Primal_Blocks.STRAW_BASKET.get(),
                Primal_Blocks.CROCODILE_SCUTE_BLOCK.get(),
                Primal_Blocks.CROCODILE_SCUTE_SHINGLE.get(),
                Primal_Blocks.CHISELED_CROCODILE_SCUTE.get(),
                Primal_Blocks.CROCODILE_SCUTE_STAIRS.get(),
                Primal_Blocks.CROCODILE_SCUTE_SLAB.get(),
                Primal_Blocks.CHOMP_TRAP_GREEN.get(),
                Primal_Blocks.CHOMP_TRAP_ARID.get(),
                Primal_Blocks.CHOMP_TRAP_HUMID.get(),
                Primal_Blocks.ARID_CROCODILE_SCUTE_BLOCK.get(),
                Primal_Blocks.ARID_CROCODILE_SCUTE_SHINGLE.get(),
                Primal_Blocks.ARID_CHISELED_CROCODILE_SCUTE.get(),
                Primal_Blocks.ARID_CROCODILE_SCUTE_STAIRS.get(),
                Primal_Blocks.ARID_CROCODILE_SCUTE_SLAB.get(),
                Primal_Blocks.HUMID_CROCODILE_SCUTE_BLOCK.get(),
                Primal_Blocks.HUMID_CROCODILE_SCUTE_SHINGLE.get(),
                Primal_Blocks.HUMID_CHISELED_CROCODILE_SCUTE.get(),
                Primal_Blocks.HUMID_CROCODILE_SCUTE_STAIRS.get(),
                Primal_Blocks.HUMID_CROCODILE_SCUTE_SLAB.get(),
                Primal_Blocks.PETRIFIED_FRUIT.get(),
                Primal_Blocks.THORNY_ACACIA_LOG.get(),
                Primal_Blocks.THORNY_ACACIA_WOOD.get(),
                Primal_Blocks.THORNY_ACACIA_SAPLING.get()
        );

        this.add(Primal_Blocks.THORNY_ACACIA_LEAVES.get(),
                block -> this.createLeavesDropsWithExtraItem(block, Primal_Blocks.THORNY_ACACIA_SAPLING.get(), Primal_Items.EXPLOSEED.get(), NORMAL_LEAVES_SAPLING_CHANCES));

        createHollowLogDrop(Primal_Blocks.HOLLOW_OAK_LOG.get());
        createHollowLogDrop(Primal_Blocks.HOLLOW_SPRUCE_LOG.get());
        createHollowLogDrop(Primal_Blocks.HOLLOW_BIRCH_LOG.get());
        createHollowLogDrop(Primal_Blocks.HOLLOW_JUNGLE_LOG.get());
        createHollowLogDrop(Primal_Blocks.HOLLOW_ACACIA_LOG.get());
        createHollowLogDrop(Primal_Blocks.HOLLOW_DARK_OAK_LOG.get());
        createHollowLogDrop(Primal_Blocks.HOLLOW_MANGROVE_LOG.get());

        this.createSingleDropShearOrSilkTouch(Primal_Blocks.SHORT_RIVER_REEDS.get());
        this.createSingleDropShearOrSilkTouch(Primal_Blocks.RIVER_REEDS.get());
        this.createSingleDropShearOrSilkTouch(Primal_Blocks.CATTAILS.get());

        this.add(Primal_Blocks.WARM_SEASHELLS.get(), createSeashellsDrops(Primal_Blocks.WARM_SEASHELLS.get()));
        this.add(Primal_Blocks.COLD_SEASHELLS.get(), createSeashellsDrops(Primal_Blocks.COLD_SEASHELLS.get()));
        this.add(Primal_Blocks.TEMPERATE_SEASHELLS.get(), createSeashellsDrops(Primal_Blocks.TEMPERATE_SEASHELLS.get()));

        this.dropWhenSilkTouch(Primal_Blocks.CROCODILE_EGG.get());

        this.dropWhenSilkTouch(Primal_Blocks.EAGLE_EGG.get());

        this.add(Primal_Blocks.NEST_BLOCK.get(),
                this.createSilkTouchOrShearsDispatchTable(
                        Primal_Blocks.NEST_BLOCK.get(),
                        this.applyExplosionCondition(Primal_Blocks.NEST_BLOCK.get(),
                                LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 6))))
                )
        );

        this.dropWhenSilkTouch(Primal_Blocks.CASSOWARY_EGG.get());

        this.dropOther(Primal_Blocks.LITCHI_SAPLING.get(), Primal_Items.LITCHI_SEEDS.get());
        this.dropOther(Primal_Blocks.KIWANO_SAPLING.get(), Primal_Items.KIWANO_SEEDS.get());
        this.dropOther(Primal_Blocks.STARFRUIT_SAPLING.get(), Primal_Items.STARFRUIT_SEEDS.get());

        this.createFruitTreeDrops(Primal_Blocks.LITCHI_TREE.get(), Primal_Items.LITCHI.get());
        this.createFruitBulkDrops(Primal_Blocks.KIWANO_BULK.get(), Primal_Items.KIWANO.get());
        this.createFruitTreeDrops(Primal_Blocks.STARFRUIT_TREE.get(), Primal_Items.STARFRUIT.get());

        this.createAntlerDrops(Primal_Blocks.FALLOW_DEER_ANTLER.get());
        this.createAntlerDrops(Primal_Blocks.REINDEER_ANTLER.get());
        this.createAntlerDrops(Primal_Blocks.WHITETAIL_DEER_ANTLER.get());

         this.createDreamcatcherDrop(Primal_Blocks.DREAMCATCHER.get());
    }

    protected void createHollowLogDrop(Block block) {
        this.add(block,
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1.0F))
                                        .add(
                                                LootItem.lootTableItem(block)
                                                        .when(HAS_SILK_TOUCH)
                                                        .apply(
                                                                CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                                                                        .copy("Animals", "BlockEntityTag.Animals")
                                                        )
                                                        .otherwise(LootItem.lootTableItem(block))
                                        )
                        ));
    }

    private void createDropThemselves(Block... blocks){
        for (Block holder : blocks)
            this.dropSelf(holder);
    }

    private void createFruitTreeDrops(Block tree, Item fruit){
        this.add(
                tree,
                builder -> this.applyExplosionDecay(
                        builder,
                        LootTable.lootTable()
                                .withPool(
                                        LootPool.lootPool()
                                                .when(
                                                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(tree)
                                                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(FruitTree.AGE, 3))
                                                                .and(LootItemBlockStatePropertyCondition.hasBlockStateProperties(tree)
                                                                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(FruitTree.HALF, DoubleBlockHalf.UPPER)))
                                                )
                                                .add(LootItem.lootTableItem(fruit))
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F)))
                                                .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))
                                )
                                .withPool(
                                        LootPool.lootPool()
                                                .when(
                                                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(tree)
                                                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SweetBerryBushBlock.AGE, 2))
                                                                .and(LootItemBlockStatePropertyCondition.hasBlockStateProperties(tree)
                                                                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(FruitTree.HALF, DoubleBlockHalf.UPPER)))
                                                )
                                                .add(LootItem.lootTableItem(fruit))
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                                                .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))
                                )
                )
        );
    }

    private void createFruitBulkDrops(Block tree, Item fruit){
        this.add(
                tree,
                builder -> this.applyExplosionDecay(
                        builder,
                        LootTable.lootTable()
                                .withPool(
                                        LootPool.lootPool()
                                                .when(
                                                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(tree)
                                                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(FruitBulk.AGE, 1))
                                                )
                                                .add(LootItem.lootTableItem(fruit))
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 7.0F)))
                                                .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))
                                                .apply(LimitCount.limitCount(IntRange.upperBound(10)))
                                )
                )
        );
    }

    private void createSingleDropShearOrSilkTouch(Block block){
        this.add(block,
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool().when(HAS_SHEARS)
                                        .add(LootItem.lootTableItem(block).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
                        ));
    }

    protected void createDreamcatcherDrop(Block dreamcatcher) {
        this.add(dreamcatcher,
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1.0F))
                                        .add(
                                                this.applyExplosionDecay(
                                                        dreamcatcher,
                                                        LootItem.lootTableItem(dreamcatcher)
                                                                .when(
                                                                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(dreamcatcher)
                                                                                .setProperties(
                                                                                        StatePropertiesPredicate.Builder.properties()
                                                                                                .hasProperty(DreamcatcherBlock.HALF, Half.TOP)
                                                                                )
                                                                )
                                                )
                                        )
                        )
        );
    }

    protected void createAntlerDrops(Block antler) {
        this.add(antler,
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1.0F))
                                        .add(
                                                this.applyExplosionDecay(
                                                        antler,
                                                        LootItem.lootTableItem(antler)
                                                                .apply(
                                                                        SetItemCountFunction.setCount(ConstantValue.exactly(2))
                                                                                .when(
                                                                                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(antler)
                                                                                                .setProperties(
                                                                                                        StatePropertiesPredicate.Builder.properties()
                                                                                                                .hasProperty(DeerAntlerBlock.DOUBLE, true)
                                                                                                )
                                                                                )
                                                                )
                                                                .apply(
                                                                        SetItemCountFunction.setCount(ConstantValue.exactly(1))
                                                                                .when(
                                                                                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(antler)
                                                                                                .setProperties(
                                                                                                        StatePropertiesPredicate.Builder.properties()
                                                                                                                .hasProperty(DeerAntlerBlock.DOUBLE, false)
                                                                                                )
                                                                                )
                                                                )
                                                )
                                        )
                        )
                );
    }

    protected LootTable.Builder createSeashellsDrops(Block seashells) {
        return LootTable.lootTable()
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(
                                        this.applyExplosionDecay(
                                                seashells,
                                                LootItem.lootTableItem(seashells)
                                                        .apply(
                                                                IntStream.rangeClosed(1, 4).boxed().toList(),
                                                                integer -> SetItemCountFunction.setCount(ConstantValue.exactly((float) integer))
                                                                        .when(
                                                                                LootItemBlockStatePropertyCondition.hasBlockStateProperties(seashells)
                                                                                        .setProperties(
                                                                                                StatePropertiesPredicate.Builder.properties().hasProperty(SeashellsBlock.AMOUNT, integer)
                                                                                        )
                                                                        )
                                                        )
                                        )
                                )
                )
                .withPool(
                        LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                                .add(
                                        this.applyExplosionDecay(
                                                seashells,
                                                LootItem.lootTableItem(Items.SNOWBALL)
                                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                                        .when(
                                                                LootItemBlockStatePropertyCondition.hasBlockStateProperties(seashells)
                                                                        .setProperties(
                                                                                StatePropertiesPredicate.Builder.properties().hasProperty(BlockStateProperties.SNOWY, true)
                                                                        )
                                                        )
                                                        .when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.SHOVELS)))
                                        )
                                ));
    }

    protected LootTable.Builder createLeavesDropsWithExtraItem(Block thornyAcaciaLeavesBlock, Block saplingBlock, Item extraDrop, float... chances) {
        return this.createLeavesDrops(thornyAcaciaLeavesBlock, saplingBlock, chances)
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .when(this.doesNotHaveShearsOrSilkTouch())
                                .add(
                                        ((LootPoolSingletonContainer.Builder<?>)this.applyExplosionCondition(thornyAcaciaLeavesBlock, LootItem.lootTableItem(extraDrop)))
                                                .when(
                                                        BonusLevelTableCondition.bonusLevelFlatChance(
                                                                Enchantments.BLOCK_FORTUNE, 0.005F, 0.0055555557F, 0.00625F, 0.008333334F, 0.025F
                                                        )
                                                )
                                )
                );
    }

    private LootItemCondition.Builder hasShearsOrSilkTouch() {
        return HAS_SHEARS.or(HAS_SILK_TOUCH);
    }

    private LootItemCondition.Builder doesNotHaveShearsOrSilkTouch() {
        return this.hasShearsOrSilkTouch().invert();
    }
}
