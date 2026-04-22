package org.primal.entity.replaced;

import com.mojang.serialization.Codec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.AnimationState;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

public interface DolphinReplaced {
    //──────────────────────────────────── Variants ────────────────────────────────────
    enum Variant implements StringRepresentable {
        LUKEWARM(0, "lukewarm"),
        WARM(1, "warm"),
        COLD(2, "cold");

        public static final Codec<DolphinReplaced.Variant> CODEC = StringRepresentable.fromEnum(DolphinReplaced.Variant::values);

        private static final IntFunction<DolphinReplaced.Variant> BY_ID = ByIdMap.continuous(DolphinReplaced.Variant::getId, values(),
                ByIdMap.OutOfBoundsStrategy.CLAMP);

        public static DolphinReplaced.Variant byId(int id) {
            return BY_ID.apply(id);
        }

        final int id;

        private final String name;

        Variant(int id, String name) {
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
}
