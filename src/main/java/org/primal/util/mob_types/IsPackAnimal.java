package org.primal.util.mob_types;

import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import org.jetbrains.annotations.NotNull;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.util.Primal_Util;

public interface IsPackAnimal<T extends Mob & IsPackAnimal<T>> {

    @SuppressWarnings("unchecked")
    private T self() {
        return (T) this;
    }

    boolean isLeader();

    void setLeader(boolean isLeader);

    default boolean canBeLeader(){
        return true;
    }

    default void addAdditionalDataPackAnimal(@NotNull CompoundTag compound){
        compound.putBoolean("IsLeader", this.isLeader());
    }

    default void readAdditionalDataPackAnimal(@NotNull CompoundTag compound){
        this.setLeader(compound.getBoolean("IsLeader"));
    }

    default void assignPackLeaderAndHome(Class<T> entityClass){

        if(self().tickCount%20==0){
            //If not leader and doesn't exist one already, assign a nearby leader
            if(!isLeader() && !hasLeader()){
                var possibleLeaders= Primal_Util.Ai.getNearestMobs(self(), entityClass, m->m.canBeLeader() && !m.isLeader() && m!=self());

                var possibleLeader = possibleLeaders.stream().findFirst();

                possibleLeader.ifPresent(t -> {
                    t.level().broadcastEntityEvent(t, (byte)14);
                    t.setLeader(true);
                });
            }
        }

        //Updates each two minutes, only if it's not attacking, or if is the first 10s
        if((self().tickCount<200 || self().tickCount%4800==0) && self().getTarget()==null){
            //If leads, set its home each minute its own position
            if(self().isLeader()){
                self().getBrain().setMemory(MemoryModuleType.HOME, GlobalPos.of(self().level().dimension(), self().getOnPos()));
            }
            //Puts the home as the leader
            else if(hasLeader()){
                self().getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_LEADER.get()).ifPresent(
                        m->
                                self().getBrain().setMemory(MemoryModuleType.HOME, GlobalPos.of(m.level().dimension(), m.getOnPos()))
                );
            }
            //Removes if not leader
            else {
                self().getBrain().eraseMemory(MemoryModuleType.HOME);
            }
        }
    }

    default boolean hasLeader(){
        return self().getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_LEADER.get()).isPresent();
    }
}
