package org.primal.entity.ai.behavior.generic;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.jetbrains.annotations.NotNull;
import org.primal.registry.Primal_MemoryModuleTypes;

import java.util.Optional;
import java.util.function.Predicate;

public class AnimalWanderFromScared extends Behavior<Mob> {
    final int yMinWander;
    final int yMaxWander;

    final int xzMinWander;
    final int xzMaxWander;

    final Predicate<Mob> canUse;

    public AnimalWanderFromScared(int yMinWander, int yMaxWander, int xzMinWander, int xzMaxWander, Predicate<Mob> canUse) {
        super(ImmutableMap.of(
                Primal_MemoryModuleTypes.NEAREST_SCARED.get(),
                MemoryStatus.VALUE_PRESENT));
        this.xzMinWander=xzMinWander;
        this.xzMaxWander=xzMaxWander;
        this.yMinWander=yMinWander;
        this.yMaxWander=yMaxWander;
        this.canUse=canUse;
    }

    //For not flying or aquatic mobs
    public AnimalWanderFromScared(int xzMinWander, int xzMaxWander) {
        this(0, 0, xzMinWander, xzMaxWander,m-> true);
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, @NotNull Mob entity, long gameTime) {
        return hasRequiredMemories(entity) && canUse.test(entity);
    }

    @Override
    protected void start(@NotNull ServerLevel level, @NotNull Mob entity, long gameTime) {
        Optional<LivingEntity> scaredOff = entity.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_SCARED.get());

        if(scaredOff.isPresent()){
            BlockPos wantedPos= entity.getOnPos().offset(
                    entity.getRandom().nextInt(xzMinWander, xzMaxWander),
                    entity.getRandom().nextInt(yMinWander,  yMaxWander),
                    entity.getRandom().nextInt(xzMinWander, xzMaxWander));

            BehaviorUtils.setWalkAndLookTargetMemories(entity, wantedPos, 1, 2);
        } else {
            stop(level, entity, gameTime);
        }
    }
}
