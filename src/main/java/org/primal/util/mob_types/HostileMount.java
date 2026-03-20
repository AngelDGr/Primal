package org.primal.util.mob_types;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.NotNull;
import org.primal.registry.Primal_MemoryModuleTypes;

public interface HostileMount {

    private Mob self() {
        return (Mob) this;
    }

    float getHealthWhenStartRiding();

    void setHealthWhenStartRiding(float healthWhenStartRiding);

    default void hurtAndReleasePassenger(float healthLossToBeReleased, long cooldown){
        //If receives more than the threshold of damage that when first got its pray, it dismounts it and gets confused for a few seconds
        if(self().isVehicle() && self().getHealth() < (this.getHealthWhenStartRiding() - healthLossToBeReleased)){
            self().ejectPassengers();

            self().getBrain().setMemoryWithExpiry(Primal_MemoryModuleTypes.IS_STUNNED.get(), true, cooldown);
        }
    }

    default void addAdditionalSaveDataHostileMount(@NotNull CompoundTag compound) {
        compound.putFloat("HealthWhenStarted", this.getHealthWhenStartRiding());
    }

    default void readAdditionalSaveDataHostileMount(@NotNull CompoundTag compound) {
        this.setHealthWhenStartRiding(compound.getFloat("HealthWhenStarted"));
    }
}
