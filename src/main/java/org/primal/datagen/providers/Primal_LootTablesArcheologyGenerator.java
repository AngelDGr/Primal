package org.primal.datagen.providers;

import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.NotNull;
import org.primal.registry.Primal_Items;
import org.primal.registry.Primal_LootTables;

import java.util.function.BiConsumer;

public class Primal_LootTablesArcheologyGenerator implements LootTableSubProvider {


    public Primal_LootTablesArcheologyGenerator() {
    }

    @Override
    public void generate(@NotNull final BiConsumer<ResourceLocation, LootTable.Builder> biConsumer) {
        biConsumer.accept(Primal_LootTables.CASSOWARY_NEST_LOOT,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))

                                .add(LootItem.lootTableItem(Primal_Items.PETRIFIED_FRUIT.get()).setWeight(1)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))

                                .add(LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(1)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))

                                .add(LootItem.lootTableItem(Items.MELON_SEEDS).setWeight(1)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))

                                .add(LootItem.lootTableItem(Items.WHEAT_SEEDS).setWeight(1)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))

                                .add(LootItem.lootTableItem(Items.STICK).setWeight(1)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                        ));

    }
}
