package org.primal.item;

import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.event.LootTableLoadEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
        return getSnakeItemPool(null, probability);
    }

    public static LootPool getSnakeItemPool(SnakeEntity.Variant variant, float probability){
        return LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .when(LootItemRandomChanceCondition.randomChance(probability))
                .add(getSnakeItemEntry(variant))
                .build();
    }

    public static LootPoolSingletonContainer.Builder<?> getSnakeItemEntry(){
        return getSnakeItemEntry(null);
    }

    public static LootPoolSingletonContainer.Builder<?> getSnakeItemEntry(SnakeEntity.Variant variant){
        return LootItem.lootTableItem(Primal_Items.PLACEHOLDER_CHESTED_SNAKE.get())
                .apply(SetSnakeLoot.builder(variant));
    }

    public static class SetSnakeLoot extends LootItemConditionalFunction {
        @NotNull
        private final SnakeComponent snakeComponent;

        SetSnakeLoot(LootItemCondition[] lootItemConditions, @NotNull SnakeComponent snakeComponent) {
            super(lootItemConditions);
            this.snakeComponent = snakeComponent;
        }

        public static LootItemConditionalFunction.Builder<?> builder(@Nullable SnakeEntity.Variant variant) {
            return SetSnakeLoot.simpleBuilder(conditions -> new SetSnakeLoot(conditions, new SnakeComponent(variant)));
        }

        public @NotNull LootItemFunctionType getType() {
            return Primal_Items.SNAKE_LOOT.get();
        }

        @Override
        public @NotNull ItemStack run(@NotNull ItemStack itemStack, @NotNull LootContext lootContext) {
            Primal_Util.OneTwentyEquivalent.Components.set(itemStack, snakeComponent);
            return itemStack;
        }

        public static class Serializer extends LootItemConditionalFunction.Serializer<SetSnakeLoot> {

            @Override
            public void serialize(@NotNull JsonObject jsonObject, @NotNull SnakeItem.SetSnakeLoot setSnakeLoot, @NotNull JsonSerializationContext jsonSerializationContext) {
                super.serialize(jsonObject, setSnakeLoot, jsonSerializationContext);
                if(setSnakeLoot.snakeComponent.variant()!=null) jsonObject.addProperty("snake_variant", setSnakeLoot.snakeComponent.variant().getId());
            }

            @Override
            public @NotNull SnakeItem.SetSnakeLoot deserialize(@NotNull JsonObject jsonObject, @NotNull JsonDeserializationContext p_80451_, LootItemCondition @NotNull [] lootItemConditions) {
                return new SetSnakeLoot(lootItemConditions,
                        new SnakeComponent(jsonObject.has("snake_variant")? SnakeEntity.Variant.byId(
                                GsonHelper.getAsInt(jsonObject, "snake_variant")): null
                        )
                );
            }
        }
    }
}
