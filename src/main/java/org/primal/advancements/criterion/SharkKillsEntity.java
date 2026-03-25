package org.primal.advancements.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.NotNull;
import org.primal.Primal_Main;

import java.util.Optional;

public class SharkKillsEntity extends SimpleCriterionTrigger<SharkKillsEntity.Conditions> {

    @Override
    protected @NotNull Conditions createInstance(@NotNull JsonObject jsonObject, @NotNull ContextAwarePredicate contextAwarePredicate, @NotNull DeserializationContext deserializationContext) {
        ContextAwarePredicate lootContextPredicate2 = EntityPredicate.fromJson(jsonObject, "entity", deserializationContext);
        return new SharkKillsEntity.Conditions(contextAwarePredicate, Optional.of(lootContextPredicate2));
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "feed_shark");
    }

    public void trigger(final ServerPlayer player, final LivingEntity entity) {
        final LootContext lootContext = EntityPredicate.createContext(player, entity);
        this.trigger(player, conditions ->
                conditions.entity.map(lootContextPredicate -> lootContextPredicate.matches(lootContext))
                .orElse(false));
    }

    public static class Conditions extends AbstractCriterionTriggerInstance {
        Optional<ContextAwarePredicate> entity;

        public Conditions(ContextAwarePredicate contextAwarePredicate, Optional<ContextAwarePredicate> optional) {
            super(ResourceLocation.fromNamespaceAndPath(Primal_Main.MOD_ID, "feed_shark"), contextAwarePredicate);
            entity=optional;
        }

        public static Conditions create(final EntityPredicate.Builder entity) {
            return new SharkKillsEntity.Conditions(ContextAwarePredicate.ANY, Optional.of(EntityPredicate.wrap(entity.build())));
        }

        @Override
        public @NotNull JsonObject serializeToJson(@NotNull SerializationContext serializationContext) {
            JsonObject jsonObject = super.serializeToJson(serializationContext);
            jsonObject.add("entity", this.entity.get().toJson(serializationContext));
            return jsonObject;
        }
    }
}
