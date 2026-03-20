package org.primal.util.mob_types;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

public interface AnimalRoars {

    @Nullable SoundEvent getRoarSound();

    default boolean canRoarAtEntity(LivingEntity target){
        return true;
    }

    default boolean hasCustomWaterRoar(){
        return false;
    }
}
