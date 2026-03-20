package org.primal.registry;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import org.primal.Primal_Registries;

public class Primal_Particles {

    public static final SimpleParticleType SNAKE_SKIN_FLAKE = new SimpleParticleType(false){};
    public static final SimpleParticleType DREAMCATCHER_ASH = new SimpleParticleType(false){};

    public static void init(){
        register("snake_skin_flake", SNAKE_SKIN_FLAKE);
        register("dreamcatcher_ash", DREAMCATCHER_ASH);
    }

    public static void register(final String name, ParticleType<?> holder) {
        Primal_Registries.PARTICLES.register(name, ()-> holder);
    }
}
