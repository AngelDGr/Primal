package org.primal.entity.replaced;

import com.mojang.serialization.Codec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.AnimationState;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

public interface RabbitReplaced {
    //──────────────────────────────────── Variants ────────────────────────────────────
    enum PrimalVariant implements StringRepresentable {
        GRAY(0, "gray"),
        SAND(1, "sand"),
        NONE(99, "none");

        public static final Codec<RabbitReplaced.PrimalVariant> CODEC = StringRepresentable.fromEnum(RabbitReplaced.PrimalVariant::values);

        private static final IntFunction<PrimalVariant> BY_ID = ByIdMap.continuous(PrimalVariant::ordinal, values(), ByIdMap.OutOfBoundsStrategy.CLAMP);

        public static RabbitReplaced.PrimalVariant byId(int id) {
            return BY_ID.apply(id);
        }

        final int id;

        private final String name;

        PrimalVariant(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return this.id;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }

    AnimationState primal$idleAnimationState();

    AnimationState primal$rearAnimationState();

    AnimationState primal$hopAnimationState();
}
