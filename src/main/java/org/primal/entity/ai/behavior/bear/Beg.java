package org.primal.entity.ai.behavior.bear;

import com.google.common.collect.ImmutableMap;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.primal.entity.animal.BearEntity;

public class Beg extends Behavior<BearEntity> {

    private Player player;

    public Beg() {
        super(ImmutableMap.of(
                MemoryModuleType.ROAR_TARGET,
                MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.ATTACK_TARGET,
                MemoryStatus.VALUE_ABSENT), Integer.MAX_VALUE);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, @NotNull BearEntity bear) {
        this.player = level.getNearestPlayer(TargetingConditions.forNonCombat().range(3.5f), bear);

        return this.player != null && !bear.bearCollapses() &&
                //Begs for honeycombs when untamed
                ((BearEntity.isTameFood(this.player.getMainHandItem()) && !bear.isTame())
                //Begs for sweet berries if it has low health and tamed
                || (BearEntity.isHealFood(this.player.getMainHandItem()) && bear.getHealth()<bear.getMaxHealth() && bear.isTame())
                        //Begs for salmon in a bucket if isn't in love and isn't a baby
                        || (BearEntity.isMatingFood(this.player.getMainHandItem()) && !bear.isBaby() && bear.canFallInLove() && bear.getAge()==0));
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, @NotNull BearEntity entity, long gameTime) {
        return checkExtraStartConditions(level, entity);
    }

    @Override
    protected void tick(@NotNull ServerLevel level, BearEntity owner, long gameTime) {
        Brain<?> brain = owner.getBrain();
        brain.eraseMemory(MemoryModuleType.WALK_TARGET);
        BehaviorUtils.lookAtEntity(owner, this.player);
    }

    @Override
    protected void start(@NotNull ServerLevel level, BearEntity entity, long gameTime) {
        entity.setPose(Pose.SNIFFING);
    }

    @Override
    protected void stop(@NotNull ServerLevel level, BearEntity entity, long gameTime) {
        entity.triggerAnim("base_controller", "beg_end");
        entity.setPose(Pose.STANDING);
    }
}
