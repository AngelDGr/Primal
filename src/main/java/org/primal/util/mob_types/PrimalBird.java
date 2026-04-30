package org.primal.util.mob_types;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.FlyingAnimal;
import org.jetbrains.annotations.NotNull;
import org.primal.util.Primal_Util;

public interface PrimalBird {

    int DEFAULT_FLYING_ENERGY = Primal_Util.toTicks(30);

    @SuppressWarnings("unchecked")
    private<T extends Mob & FlyingAnimal> T self(){
        return (T) this;
    }

    int getFlyingEnergy();

    void setFlyingEnergy(int flyingEnergy);

    int getRecoveredEnergy();

    void setRecoveredEnergy(int recoveredEnergy);

    default boolean canContinueFlying() {
        return this.getFlyingEnergy() > 0;
    }

    default int getBaseFlyingEnergy(){
        return DEFAULT_FLYING_ENERGY;
    }

    default boolean canStartFlyingAgain() {
        return this.getFlyingEnergy() >= this.getBaseFlyingEnergy();
    }

    default void addAdditionalDataPrimalBird(@NotNull CompoundTag compound){
        compound.putInt("FlyingEnergy", this.getFlyingEnergy());
        compound.putInt("RecoveredTime", this.getRecoveredEnergy());
    }

    default void readAdditionalDataPrimalBird(@NotNull CompoundTag compound){
        this.setFlyingEnergy(compound.getInt("FlyingEnergy"));
        this.setRecoveredEnergy(compound.getInt("RecoveredTime"));
    }

    default void birdAiStep(){
        if(self().isFlying() && !self().isInWaterOrBubble()){
            this.setFlyingEnergy(Mth.clamp(this.getFlyingEnergy() - 1, 0, Integer.MAX_VALUE));
        } else {
            this.setFlyingEnergy(Mth.clamp(this.getFlyingEnergy() + 1, 0, Integer.MAX_VALUE));
        }

        if(self().onGround() || self().isInWaterOrBubble()){
            this.setRecoveredEnergy(Mth.clamp(this.getRecoveredEnergy() + 1,0, Integer.MAX_VALUE));
        } else {
            this.setRecoveredEnergy(Mth.clamp(this.getRecoveredEnergy() - 1, 0, Integer.MAX_VALUE));
        }

        //Restores Energy
        if(this.getFlyingEnergy() + this.getRecoveredEnergy() >= this.getBaseFlyingEnergy()){
            this.setFlyingEnergy(this.getFlyingEnergy() + this.getRecoveredEnergy());
            this.setRecoveredEnergy(0);
        }
    }


    static<T extends LivingEntity & FlyingAnimal & PrimalBird> boolean basicBirdCanFly(T bird){
        if(bird.isBaby())
            return false;

        if(bird.isFlying())
            return bird.canContinueFlying();

        if(bird.onGround())
            return bird.canStartFlyingAgain();

        return false;
    }

}
