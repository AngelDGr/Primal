package org.primal.injection;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import org.primal.registry.Primal_Entities;
import org.primal.registry.Primal_Sounds;

import javax.annotation.Nullable;

public interface SetNeckEntity {

    private Player self() {
        return (Player) this;
    }

    CompoundTag primal$getNeckEntity();

    void primal$setNeckEntity(CompoundTag entityCompound);

    default void playNeckEntityAmbientSound(@Nullable CompoundTag entityCompound) {
        if (entityCompound != null && (!entityCompound.contains("Silent") || !entityCompound.getBoolean("Silent")) && self().level().random.nextInt(200) == 0) {
            String s = entityCompound.getString("id");
            var entityOnNeck = EntityType.byString(s);

            if(entityOnNeck.isPresent()){
                if(entityOnNeck.get().equals(Primal_Entities.SNAKE.get()))
                    self().playSound(Primal_Sounds.SNAKE_IDLE.get());
            }
        }
    }

    default void respawnEntityOnNeck(CompoundTag entityCompound) {
        if (!self().level().isClientSide && !entityCompound.isEmpty()) {
            EntityType.create(entityCompound, self().level()).ifPresent(entity -> {
                if (entity instanceof TamableAnimal) {
                    ((TamableAnimal)entity).setOwnerUUID(self().getUUID());
                }

                entity.setPos(self().getX(), self().getY() + 0.7F, self().getZ());
                ((ServerLevel)self().level()).addWithUUID(entity);
            });
        }
    }

    default void removeEntitiesOnNeck() {
        if (this.primal$getTimeEntitySatOnNeck() + 20L < self().level().getGameTime()) {
            this.respawnEntityOnNeck(this.primal$getNeckEntity());
            this.primal$setNeckEntity(new CompoundTag());
        }
    }

    long primal$getTimeEntitySatOnNeck();

    default boolean setEntityOnNeck(CompoundTag entityCompound) {
        if (self().isPassenger() || !self().onGround() || self().isInWater() || self().isInPowderSnow) {
            return false;
        } else if (this.primal$getNeckEntity().isEmpty()) {
            this.primal$setNeckEntity(entityCompound);
            this.primal$setTimeEntitySatOnNeck(self().level().getGameTime());
            return true;
        } else {
            return false;
        }
    }

    void primal$setTimeEntitySatOnNeck(long gameTime);
}
