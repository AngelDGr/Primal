package org.primal.mixin;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.sensing.VillagerHostilesSensor;
import org.primal.registry.Primal_Entities;
import org.spongepowered.asm.mixin.*;

@Mixin(VillagerHostilesSensor.class)
public class VillagerHostilesSensorMixin {
    @Unique
    private static final float primal$distanceDanger = 12.0f;
    @Unique
    private static final float primal$distanceMediumDanger = 16.0f;
    @Unique
    private static final float primal$distanceExtremeDanger = 24.0f;


    @Shadow @Final @Mutable
    private static ImmutableMap<Object, Object> ACCEPTABLE_DISTANCE_FROM_HOSTILES;

    static {
                ACCEPTABLE_DISTANCE_FROM_HOSTILES = ImmutableMap.builder()
                .putAll(ACCEPTABLE_DISTANCE_FROM_HOSTILES.entrySet())

                        .put(Primal_Entities.BEAR.get(), primal$distanceDanger)
                        .put(Primal_Entities.SHARK.get(), primal$distanceDanger)
                        .put(Primal_Entities.CROCODILE.get(), primal$distanceMediumDanger)

                        .build();
    }

}
