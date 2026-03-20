package org.primal.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class SnakeSkinFlakeParticle extends TextureSheetParticle {
    private final SpriteSet spriteProvider;

    protected SnakeSkinFlakeParticle(final ClientLevel world, final double x, final double y, final double z, double xSpeed, double ySpeed, double zSpeed, final SpriteSet spriteProvider) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.gravity = 0.225F;
        this.friction = 1.0F;
        this.spriteProvider = spriteProvider;
        this.xd = xSpeed + (Math.random() * 2.0 - 1.0) * 0.05F;
        this.yd = ySpeed + (Math.random() * 2.0 - 1.0) * 0.05F;
        this.zd = zSpeed + (Math.random() * 2.0 - 1.0) * 0.05F;
        this.quadSize = 0.15F * (this.random.nextFloat() * this.random.nextFloat() * 1.0F + 1.0F);
        this.lifetime = (int)(16.0 / ((double)this.random.nextFloat() * 0.8 + 0.2)) + 2;
        this.setSpriteFromAge(spriteProvider);
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.spriteProvider);
        this.xd *= 0.95F;
        this.yd *= 0.9F;
        this.zd *= 0.95F;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;

        public Factory(final SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(@NotNull final SimpleParticleType defaultParticleType,
                                       @NotNull final ClientLevel clientWorld,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new SnakeSkinFlakeParticle(clientWorld, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteProvider);
        }
    }
}
