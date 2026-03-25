package org.primal.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BaseAshSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.FastColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class DreamcatcherAshParticle extends BaseAshSmokeParticle {
    protected DreamcatcherAshParticle(
        ClientLevel level,
        double x,
        double y,
        double z,
        double xSpeed,
        double ySpeed,
        double zSpeed,
        float quadSizeMultiplier,
        SpriteSet sprites
    ) {
        super(level, x, y, z, 0.0F, -0.05F, 0.0F,
                xSpeed, 0.0, zSpeed, quadSizeMultiplier, sprites,
                0.0F, 40,
                1f/100f*((float) ySpeed), false);
        this.rCol = (float) FastColor.ARGB32.red(12235202) / 255.0F;
        this.gCol = (float)FastColor.ARGB32.green(12235202) / 255.0F;
        this.bCol = (float)FastColor.ARGB32.blue(12235202) / 255.0F;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Factory(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(
                @NotNull SimpleParticleType type,
                @NotNull ClientLevel level,
                double x,
                double y,
                double z,
                double xSpeed,
                double ySpeed,
                double zSpeed
        ) {
            return new DreamcatcherAshParticle(level, x, y, z, 0.0, ySpeed, 0.0, 1.0F, this.sprites);
        }
    }
}