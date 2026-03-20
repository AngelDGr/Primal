package org.primal.registry;

import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class Primal_TreeGrower {

    public static final TreeGrower THORNY_ACACIA =
            new TreeGrower("thorny_acacia",
                    Optional.empty(),
                    Optional.of(Primal_WorldGen.ConfiguredFeatures.THORNY_ACACIA),
                    Optional.empty());
}
