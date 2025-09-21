package org.primal.datagen.providers;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.block.SeashellsBlock;
import org.primal.registry.Primal_Blocks;

import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

public class Primal_LootTablesBlocksGenerator extends BlockLootSubProvider {

    public Primal_LootTablesBlocksGenerator(final HolderLookup.Provider lookupProvider) {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, lookupProvider);
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return BuiltInRegistries.BLOCK.entrySet().stream()
                .filter(e -> e.getKey().location().getNamespace().equals(Primal_Main.MOD_ID)).map(Map.Entry::getValue).toList();
    }

    @Override
    public void generate() {
        final HolderLookup.RegistryLookup<Enchantment> enchantmentRegistryLookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

        createDropThemselves(
                Primal_Blocks.SHARK_TOOTH.get(),
                Primal_Blocks.CROCODILE_SCUTE_BLOCK.get(),
                Primal_Blocks.CROCODILE_SCUTE_SHINGLE.get(),
                Primal_Blocks.CHISELED_CROCODILE_SCUTE.get(),
                Primal_Blocks.CROCODILE_SCUTE_STAIRS.get(),
                Primal_Blocks.CROCODILE_SCUTE_SLAB.get(),
                Primal_Blocks.STRAW_BALE.get()
        );

        this.createSingleDropShearOrSilkTouch(Primal_Blocks.SHORT_RIVER_REEDS.get());

        this.createSingleDropShearOrSilkTouch(Primal_Blocks.RIVER_REEDS.get());

        this.add(Primal_Blocks.SEASHELLS.get(), createSeashellsDrops(Primal_Blocks.SEASHELLS.get()));

        this.dropWhenSilkTouch(Primal_Blocks.CROCODILE_EGG.get());

        this.dropWhenSilkTouch(Primal_Blocks.EAGLE_EGG.get());

        this.add(Primal_Blocks.NEST_BLOCK.get(),
                this.createSilkTouchOrShearsDispatchTable(
                        Primal_Blocks.NEST_BLOCK.get(),
                        this.applyExplosionCondition(Primal_Blocks.NEST_BLOCK.get(),
                                LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 6))))
                )
        );
    }

    private void createDropThemselves(Block... blocks){
        for (Block holder : blocks)
            this.dropSelf(holder);
    }

    private void createSingleDropShearOrSilkTouch(Block block){
        this.add(block,
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool().when(HAS_SHEARS)
                                        .add(LootItem.lootTableItem(block).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
                        ));
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
                );
    }
}
