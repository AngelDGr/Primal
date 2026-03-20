package org.primal.util.mob_types;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import org.primal.registry.Primal_MemoryModuleTypes;

public interface HideOnLog {
    private Mob self(){
        return (Mob) this;
    }

    int getEnterCooldown();

    SoundEvent getInsideLogSound();

    SoundEvent getEnterLogSound();

    SoundEvent getExitLogSound();

    void setVariantFromLog(int otherParentVariant);

    default boolean staysOnLog(){
        return false;
    }

    default boolean isPregnant(){
        return self().getBrain().getMemory(MemoryModuleType.IS_PREGNANT).isPresent();
    }

    default int getMateVariant(){
        if(self().getBrain().getMemory(Primal_MemoryModuleTypes.MATE_VARIANT.get()).isEmpty())
            return -1;

        return self().getBrain().getMemory(Primal_MemoryModuleTypes.MATE_VARIANT.get()).get();
    }
}
