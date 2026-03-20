package org.primal.datagen.providers;

import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;
import org.primal.entity.animal.DeerEntity;
import org.primal.registry.Primal_Entities;
import org.primal.registry.Primal_Items;

import java.util.Map;
import java.util.stream.Stream;

public class Primal_LootTablesEntitiesGenerator extends EntityLootSubProvider {

    public Primal_LootTablesEntitiesGenerator(final HolderLookup.Provider lookupProvider) {
        super(FeatureFlags.DEFAULT_FLAGS, lookupProvider);
    }

    @Override
    protected @NotNull Stream<EntityType<?>> getKnownEntityTypes() {
        return BuiltInRegistries.ENTITY_TYPE.entrySet().stream()
                .filter(e -> e.getKey().location().getNamespace().equals(Primal_Main.MOD_ID)).map(Map.Entry::getValue);
    }

    @Override
    public void generate() {

        this.add(Primal_Entities.BEAR.get(), LootTable.lootTable());

        this.add(Primal_Entities.SHARK.get(), LootTable.lootTable()
                .withPool(
                        LootPool.lootPool()
                                .setRolls(UniformGenerator.between(2.0f, 4.0f))
                                .add(
                                        LootItem.lootTableItem(Items.COD)
                                                .apply(SmeltItemFunction.smelted().when(this.shouldSmeltLoot()))
                                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
                                )
                                .add(
                                        LootItem.lootTableItem(Items.SALMON)
                                                .apply(SmeltItemFunction.smelted().when(this.shouldSmeltLoot()))
                                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
                                )
                                .add(
                                        LootItem.lootTableItem(Items.TROPICAL_FISH)
                                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
                                )
                )
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0f))
                                .add(
                                        LootItem.lootTableItem(Primal_Items.SHARK_TOOTH.get())
                                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
                                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,
                                                        0.30f, 0.1f))
                                )
                ));

        this.add(Primal_Entities.CROCODILE.get(), LootTable.lootTable());

        this.add(Primal_Entities.EAGLE.get(),
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1.0F))
                                        .add(
                                                LootItem.lootTableItem(Items.FEATHER)
                                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
                                        )
                        ));

        this.add(Primal_Entities.CASSOWARY.get(),
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1.0F))
                                        .add(
                                                LootItem.lootTableItem(Items.FEATHER)
                                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F)))
                                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
                                        )
                        ));

        this.add(Primal_Entities.WALRUS.get(), LootTable.lootTable());

        this.add(Primal_Entities.LION.get(), LootTable.lootTable());

        this.add(Primal_Entities.SNAKE.get(), LootTable.lootTable());

        this.add(Primal_Entities.DEER.get(),
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1.0F))
                                        .add(
                                                LootItem.lootTableItem(Primal_Items.VENISON.get())
                                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F)))
                                                        .apply(SmeltItemFunction.smelted().when(this.shouldSmeltLoot()))
                                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
                                        )
                        )
                        .withPool(
                                antlerPool(DeerEntity.Variant.FALLOW.getId(), false, Primal_Items.FALLOW_DEER_ANTLER.get())
                        )
                        .withPool(
                                antlerPool(DeerEntity.Variant.FALLOW.getId(), true, Primal_Items.FALLOW_DEER_ANTLER.get())
                        )

                        .withPool(
                                antlerPool(DeerEntity.Variant.REINDEER.getId(), false, Primal_Items.REINDEER_ANTLER.get())
                        )
                        .withPool(
                                antlerPool(DeerEntity.Variant.REINDEER.getId(), true, Primal_Items.REINDEER_ANTLER.get())
                        )

                        .withPool(
                                antlerPool(DeerEntity.Variant.WHITETAIL.getId(), false, Primal_Items.WHITETAIL_DEER_ANTLER.get())
                        )
                        .withPool(
                                antlerPool(DeerEntity.Variant.WHITETAIL.getId(), true, Primal_Items.WHITETAIL_DEER_ANTLER.get())
                        )
        );
    }

    private LootPool.Builder antlerPool(int variant, boolean isLeft, Item antlerItem){
        CompoundTag variantTag = new CompoundTag();
        variantTag.putInt("Variant", variant);

        CompoundTag leftAntlerTag = new CompoundTag();
        leftAntlerTag.putBoolean("HasLeftAntler", true);

        CompoundTag rightAntlerTag = new CompoundTag();
        rightAntlerTag.putBoolean("HasRightAntler", true);

        return LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F))
                .add(LootItem.lootTableItem(antlerItem))
                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                .when(LootItemRandomChanceCondition.randomChance(0.05F))// 5% chance
                .when(LootItemEntityPropertyCondition.hasProperties(
                                        LootContext.EntityTarget.THIS,
                                        EntityPredicate.Builder.entity()
                                                .nbt(new NbtPredicate(variantTag))
                                )
                                .and(

                                        LootItemEntityPropertyCondition.hasProperties(
                                                LootContext.EntityTarget.THIS,
                                                EntityPredicate.Builder.entity()
                                                        .nbt(new NbtPredicate(isLeft? leftAntlerTag: rightAntlerTag))
                                        )
                                )
                );
    }
}