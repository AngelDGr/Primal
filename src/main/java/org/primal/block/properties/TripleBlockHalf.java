package org.primal.block.properties;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum TripleBlockHalf implements StringRepresentable {
    UPPER,
    MIDDLE,
    LOWER;

    @Override
    public String toString() {
        return this.getSerializedName();
    }

    @Override
    public @NotNull String getSerializedName() {
        return this == UPPER ? "upper" : this == MIDDLE? "middle": "lower";
    }
}
