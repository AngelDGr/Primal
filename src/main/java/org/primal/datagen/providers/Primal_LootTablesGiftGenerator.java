package org.primal.datagen.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.NotNull;
import org.primal.registry.Primal_Items;
import org.primal.registry.Primal_LootTables;

import java.util.function.BiConsumer;

public class Primal_LootTablesGiftGenerator implements LootTableSubProvider {

    private final HolderLookup.Provider lookupProvider;

    public Primal_LootTablesGiftGenerator(final HolderLookup.Provider lookupProvider) {
        this.lookupProvider=lookupProvider;
    }

    @Override
    public void generate(@NotNull final BiConsumer<ResourceKey<LootTable>, LootTable.Builder> biConsumer) {
        final HolderLookup.RegistryLookup<Enchantment> enchantmentRegistryLookup = this.lookupProvider.lookupOrThrow(Registries.ENCHANTMENT);

        biConsumer.accept(Primal_LootTables.CASSOWARY_PROCESSED_SEEDS,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))

                                .add(LootItem.lootTableItem(Primal_Items.LITCHI_SEEDS.get()).setWeight(100)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))

                                .add(LootItem.lootTableItem(Primal_Items.KIWANO_SEEDS.get()).setWeight(100)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))

                                .add(LootItem.lootTableItem(Primal_Items.STARFRUIT_SEEDS.get()).setWeight(100)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))

                                .add(LootItem.lootTableItem(Primal_Items.WEIRD_APPLE.get()).setWeight(1)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                        ));

        biConsumer.accept(Primal_LootTables.LION_MORNING_GIFT,
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1.0F))
                                        .add(LootItem.lootTableItem(Items.LEATHER).setWeight(10))
                                        .add(LootItem.lootTableItem(Items.BEEF).setWeight(10))
                                        .add(LootItem.lootTableItem(Items.CHICKEN).setWeight(10))
                                        .add(LootItem.lootTableItem(Items.MUTTON).setWeight(10))
                                        .add(LootItem.lootTableItem(Items.PORKCHOP).setWeight(10))
                                        .add(LootItem.lootTableItem(Items.RABBIT).setWeight(10))
                                        .add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(10))
                                        .add(LootItem.lootTableItem(Items.SPIDER_EYE).setWeight(10))
                                        .add(LootItem.lootTableItem(Items.BONE).setWeight(10))
                                        .add(LootItem.lootTableItem(Items.GUNPOWDER).setWeight(8))
                                        .add(LootItem.lootTableItem(Items.PHANTOM_MEMBRANE).setWeight(6))
                                        .add(LootItem.lootTableItem(Primal_Items.ROYAL_BANNER_PATTERN.get()).setWeight(1))
                        ));
    }
}
