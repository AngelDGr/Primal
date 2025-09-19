package org.primal.entity.ai.behavior.eagle;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.EagleEntity;
import org.primal.registry.Primal_MemoryModuleTypes;

import java.util.Optional;

public class EagleDetectHostile extends Behavior<EagleEntity> {
    public EagleDetectHostile() {
        super(ImmutableMap.of(
                MemoryModuleType.NEAREST_HOSTILE,
                MemoryStatus.VALUE_PRESENT,
                Primal_MemoryModuleTypes.CHIRP_COOLDOWN.get(),
                MemoryStatus.VALUE_ABSENT
        ));
    }

    @Override
    protected void start(@NotNull ServerLevel level, @NotNull EagleEntity eagle, long gameTime) {

        if(eagle.getShriekSound()!=null)
            eagle.playSound(eagle.getShriekSound(), 1, 1);

        Optional<LivingEntity> hostile= eagle.getBrain().getMemory(MemoryModuleType.NEAREST_HOSTILE);

        hostile.ifPresent(entity -> {
                    entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 0));
                    eagle.getBrain().setMemoryWithExpiry(Primal_MemoryModuleTypes.CHIRP_COOLDOWN.get(), true, 300);
                }
        );
    }
}
