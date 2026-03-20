package org.primal.injection;

public interface FoxUnstuck {

    default int primal$mustDoUnstuck() {
        return 0;
    }

    default void primal$setMustDoUnstuck(int mustDo) {}
}
