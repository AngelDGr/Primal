package org.primal.entity.misc;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.primal.registry.Primal_Entities;
import org.primal.registry.Primal_Items;

public class ExploseedEntity extends ThrowableItemProjectile {


    public ExploseedEntity(final EntityType<? extends ThrowableItemProjectile> entityType, final Level world) {
        super(entityType, world);
    }

    public ExploseedEntity(final Level world, final double x, final double y, final double z) {
        super(Primal_Entities.EXPLOSEED.get(), x, y, z, world);
    }

    public ExploseedEntity(final Level world, final LivingEntity owner) {
        super(Primal_Entities.EXPLOSEED.get(), owner, world);
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return Primal_Items.EXPLOSEED.get();
    }

    @Override
    public void handleEntityEvent(final byte status) {

        if (status == EntityEvent.DEATH) {

            int particleCount = 80;   // total particles
            double initialRadius = 1.1D;
            double speed = 0.4D;     // explosion strength

            double centerX = this.getX();
            double centerY = this.getY() + 0.2D;
            double centerZ = this.getZ();

            for (int i = 0; i < particleCount; i++) {

                // Random direction on unit sphere
                double theta = this.random.nextDouble() * 2 * Math.PI;
                double phi = Math.acos(2 * this.random.nextDouble() - 1);

                double xDir = Math.sin(phi) * Math.cos(theta);
                double yDir = Math.sin(phi) * Math.sin(theta);
                double zDir = Math.cos(phi);

                double xOffset = xDir * initialRadius;
                double yOffset = yDir * initialRadius;
                double zOffset = zDir * initialRadius;

                this.level().addParticle(
                        new BlockParticleOption(
                                ParticleTypes.BLOCK,
                                Blocks.MUDDY_MANGROVE_ROOTS.defaultBlockState()
                        ),
                        centerX + xOffset,
                        centerY + yOffset,
                        centerZ + zOffset,
                        xDir * speed,
                        yDir * speed,
                        zDir * speed
                );
            }
        }

        super.handleEntityEvent(status);
    }

    @Override
    protected void onHitEntity(@NotNull final EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        final Entity entity = entityHitResult.getEntity();
        entity.hurt(this.damageSources().thrown(this, this.getOwner()), 4);
    }

    @Override
    protected void onHit(@NotNull HitResult hitResult) {
        super.onHit(hitResult);

        if (!this.level().isClientSide) {
            this.playSound(SoundEvents.DRAGON_FIREBALL_EXPLODE, 0.8f, 1.4f);
            this.level().broadcastEntityEvent(this, EntityEvent.DEATH);
            this.discard();

            var list = this.level().getEntitiesOfClass(Entity.class, this.getBoundingBox().inflate(1.7));

            for (Entity entity : list) entity.hurt(this.damageSources().thrown(this, this.getOwner()), 4);
        }
    }
}