package org.primal.entity.ai.behavior;

import java.util.Map;

import org.primal.entity.animal.Bear;

import com.google.common.collect.ImmutableMap;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import software.bernie.geckolib.animatable.GeoEntity;

public class Beg extends Behavior<Mob> {

    private Player player;

    public Beg() {
        super(ImmutableMap.of(
                MemoryModuleType.ROAR_TARGET,
                MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.ATTACK_TARGET,
                MemoryStatus.VALUE_ABSENT), Integer.MAX_VALUE);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, Mob owner) {
        this.player = level.getNearestPlayer(TargetingConditions.forNonCombat().range(8.f), owner);
        return owner.isBaby() && this.player != null && this.player.getMainHandItem().is(Items.HONEYCOMB);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, Mob entity, long gameTime) {
        return this.player != null && this.player.getMainHandItem().is(Items.HONEYCOMB) && entity.isBaby();
    }

    @Override
    protected void tick(ServerLevel level, Mob owner, long gameTime) {
        Brain<?> brain = owner.getBrain();
        brain.eraseMemory(MemoryModuleType.WALK_TARGET);
        brain.setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(this.player, true));
    }

    @Override
    protected void start(ServerLevel level, Mob entity, long gameTime) {
        entity.setPose(Pose.SNIFFING);
    }

    @Override
    protected void stop(ServerLevel level, Mob entity, long gameTime) {
        ((GeoEntity)entity).triggerAnim("base_controller", "beg_end");
        entity.setPose(Pose.STANDING);
    }

}
