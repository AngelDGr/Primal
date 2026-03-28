package org.primal.item;

import com.google.gson.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.event.LootTableLoadEvent;
import org.primal.entity.animal.SnakeEntity;
import org.primal.item.component.SnakeComponent;
import org.primal.registry.Primal_Items;
import org.primal.util.Primal_Util;

public class SnakeItem extends Item {
    public SnakeItem(Properties properties) {
        super(properties);
    }

    public static void addSnakeToLootTable(LootTableLoadEvent event, ResourceLocation lootTable, float probability){
        if(Primal_Util.isLootTable(event, lootTable))
            //Add snakes to pool
            event.getTable().addPool(SnakeItem.getSnakeItemPool(probability));
    }

    public static void addMarineSnakeToLootTable(LootTableLoadEvent event, ResourceLocation lootTable, float probability){
        if(Primal_Util.isLootTable(event, lootTable))
            //Add snakes to pool
            event.getTable().addPool(SnakeItem.getMarineSnakePool(probability));
    }

    public static LootPool getMarineSnakePool(float probability){
        return getSnakeItemPool(SnakeEntity.Variant.MARINE, probability);
    }

    public static LootPool getSnakeItemPool(float probability){
        return getSnakeItemPool(SnakeEntity.Variant.NULL, probability);
    }

    public static LootPool getSnakeItemPool(SnakeEntity.Variant variant, float probability){
        return LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .when(LootItemRandomChanceCondition.randomChance(probability))
                .add(getSnakeItemEntry(variant))
                .build();
    }

    public static LootPoolSingletonContainer.Builder<?> getSnakeItemEntry(){
        return getSnakeItemEntry(SnakeEntity.Variant.NULL);
    }

    public static LootPoolSingletonContainer.Builder<?> getSnakeItemEntry(SnakeEntity.Variant variant){
        return LootItem.lootTableItem(Primal_Items.PLACEHOLDER_CHESTED_SNAKE.get())
                .apply(SetNbtFunction.setTag(
                        new SnakeComponent(variant)
                                .addComponent(new CompoundTag())
                ));
    }

    public static boolean hasVariant(ItemStack stack, SnakeEntity.Variant variant){
        var component = Primal_Util.OneTwentyEquivalent.Components.get(stack, Primal_Items.Components.SNAKE_SPAWN);
        if(component != null)
            return component.variant().equals(variant);

        return false;
    }
}
