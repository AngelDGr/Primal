package org.primal.advancements.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class Primal_CustomCriterion extends SimpleCriterionTrigger<Primal_CustomCriterion.Conditions> {
    private final ResourceLocation id;

    public Primal_CustomCriterion(ResourceLocation id){
        this.id =id;
    }

    @Override
    public @NotNull Primal_CustomCriterion.Conditions createInstance(@NotNull JsonObject jsonObject, @NotNull ContextAwarePredicate awarePredicate, @NotNull DeserializationContext deserializationContext) {
        return new Primal_CustomCriterion.Conditions(this.id, awarePredicate);
    }

    public void trigger(final ServerPlayer player) {
        this.trigger(player, conditions -> true);
    }

    public @NotNull ResourceLocation getId() {
        return id;
    }

    public static class Conditions extends AbstractCriterionTriggerInstance {

        public Conditions(ResourceLocation id, ContextAwarePredicate entity) {
            super(id, entity);
        }

        public static Primal_CustomCriterion.Conditions create(Primal_CustomCriterion holder) {
            return new Primal_CustomCriterion.Conditions(holder.getId(), ContextAwarePredicate.ANY);
        }
    }
}
