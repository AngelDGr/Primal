package org.primal.advancements.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class Primal_CustomCriterion extends SimpleCriterionTrigger<Primal_CustomCriterion.Conditions> {

    @Override
    public @NotNull Codec<Primal_CustomCriterion.Conditions> codec() {
        return Conditions.CODEC;
    }

    public void trigger(final ServerPlayer player) {
        this.trigger(player, conditions -> true);
    }

    public record Conditions(Optional<ContextAwarePredicate> player) implements SimpleInstance {

        public static final Codec<Primal_CustomCriterion.Conditions> CODEC =
                RecordCodecBuilder.create(instance ->
                        instance.group(
                                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player")
                                        .forGetter(Primal_CustomCriterion.Conditions::player))
                                .apply(instance, Primal_CustomCriterion.Conditions::new));


        public static Criterion<Primal_CustomCriterion.Conditions> create(DeferredHolder<CriterionTrigger<?>, ? extends Primal_CustomCriterion> holder) {
            return holder.get().createCriterion(new Primal_CustomCriterion.Conditions(Optional.empty()));
        }
    }
}
