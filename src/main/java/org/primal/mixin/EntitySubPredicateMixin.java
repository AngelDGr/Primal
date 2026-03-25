package org.primal.mixin;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.advancements.critereon.EntitySubPredicate;
import org.primal.registry.Primal_Advancements;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntitySubPredicate.Types.class)
public class EntitySubPredicateMixin {

    @Shadow @Final @Mutable
    public static BiMap<String, EntitySubPredicate.Type> TYPES;

    static {
        TYPES = ImmutableBiMap.<String, EntitySubPredicate.Type>builder()
                .putAll(TYPES.entrySet())
                .put("bear", Primal_Advancements.BEAR.type())
                .put("eagle", Primal_Advancements.EAGLE.type())

                .build();
    }
}
