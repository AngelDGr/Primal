package org.primal.injection;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public interface IsEagleTarget {

    default Optional<UUID> primal$eagleAttacking(){return Optional.empty();}

    default void primal$setEagleAttacking(@Nullable UUID eagleUUID){}
}
