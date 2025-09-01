package org.primal.block.properties;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum SharkToothThickness implements StringRepresentable {
    TIP("tip"),
    BASE("base");

    private final String name;

    private SharkToothThickness(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }
}
