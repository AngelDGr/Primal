package org.primal.advancements.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.primal.registry.Primal_Advancements;

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


        public static Criterion<Primal_CustomCriterion.Conditions> createSurviveShark() {
            return Primal_Advancements.SURVIVE_SHARK.get().createCriterion(new Conditions(Optional.empty()));
        }

        public static Criterion<Primal_CustomCriterion.Conditions> createSwimWithShark() {
            return Primal_Advancements.SWIM_WITH_SHARK.get().createCriterion(new Conditions(Optional.empty()));
        }

    }
}
