package org.primal.advancements.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.NotNull;
import org.primal.registry.Primal_Advancements;

import java.util.Optional;

public class SharkKillsEntity extends SimpleCriterionTrigger<SharkKillsEntity.Conditions> {

    @Override
    public @NotNull Codec<SharkKillsEntity.Conditions> codec() {
        return SharkKillsEntity.Conditions.CODEC;
    }

    public void trigger(final ServerPlayer player, final LivingEntity entity) {
        final LootContext lootContext = EntityPredicate.createContext(player, entity);
        this.trigger(player, conditions ->
                conditions.entity.map(lootContextPredicate -> lootContextPredicate.matches(lootContext))
                .orElse(false));
    }

    public record Conditions(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> entity) implements SimpleInstance {

        public static final Codec<SharkKillsEntity.Conditions> CODEC =
                RecordCodecBuilder.create(instance ->
                        instance.group(
                                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player")
                                                .forGetter(SharkKillsEntity.Conditions::player),
                                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("entity")
                                                .forGetter(SharkKillsEntity.Conditions::entity))
                                .apply(instance, SharkKillsEntity.Conditions::new));

        public static Criterion<SharkKillsEntity.Conditions> create(final EntityPredicate.Builder entity) {
            return Primal_Advancements.FEED_SHARK.get().createCriterion(
                    new SharkKillsEntity.Conditions(
                            Optional.empty(),
                            Optional.of(EntityPredicate.wrap(entity))));
        }

    }
}
