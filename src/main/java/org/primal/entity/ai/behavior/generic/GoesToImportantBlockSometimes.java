package org.primal.entity.ai.behavior.generic;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import org.jetbrains.annotations.NotNull;
import org.primal.registry.Primal_MemoryModuleTypes;
import org.primal.util.Primal_Util;

import java.util.function.Predicate;

public class GoesToImportantBlockSometimes extends Behavior<Mob> {

    private final float startDistance;
    private final int wantToReachDistance;
    private final Predicate<Mob> extraStartConditions;
    private final int minCooldown;
    private final int maxExtraCooldown;

    public GoesToImportantBlockSometimes(float startDistance, int wantToReachDistance,
                                         Predicate<Mob> extraStartConditions,
                                         int minCooldown,
                                         int maxExtraCooldown) {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET,
                MemoryStatus.VALUE_ABSENT,
                Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get(),
                MemoryStatus.VALUE_PRESENT,
                Primal_MemoryModuleTypes.WAS_TOWARDS_IMPORTANT_BLOCK.get(),
                MemoryStatus.VALUE_ABSENT)
        );
        this.startDistance=startDistance;
        this.wantToReachDistance=wantToReachDistance;
        this.extraStartConditions=extraStartConditions;
        this.minCooldown=minCooldown;
        this.maxExtraCooldown=maxExtraCooldown;
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, Mob mob) {
        var importantBlock=mob.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get());

        return importantBlock.filter(pos -> pos.distManhattan(mob.getOnPos()) > startDistance
                && extraStartConditions.test(mob)).isPresent();

    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, @NotNull Mob entity, long gameTime) {
        var importantBlock=entity.getBrain().getMemory(Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get());
        return importantBlock.isPresent()&& Primal_Util.Ai.canReachPos(entity, importantBlock.get());
    }

    @Override
    protected void start(@NotNull ServerLevel level, Mob mob, long gameTime) {
        var nearestImportantBlock = mob.getBrain().getMemory(
                Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get());

        if(nearestImportantBlock.isEmpty()) return;

        mob.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        mob.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(nearestImportantBlock.get(), 1.0f, wantToReachDistance));
    }

    @Override
    protected void stop(@NotNull ServerLevel level, Mob mob, long gameTime) {
        mob.getBrain().setMemoryWithExpiry(Primal_MemoryModuleTypes.WAS_TOWARDS_IMPORTANT_BLOCK.get(), true, level.getRandom().nextIntBetweenInclusive(0, maxExtraCooldown)+ minCooldown);
    }

    //──────────────────────────────────── Builders ────────────────────────────────────
    public static GoesToImportantBlockSometimes create(float startDistance, int wantToReachDistance,
                                                       Predicate<Mob> extraStartConditions,
                                                       int minCooldown,
                                                       int maxExtraCooldown){
        return new GoesToImportantBlockSometimes(startDistance, wantToReachDistance, extraStartConditions, minCooldown, maxExtraCooldown);
    }

    public static GoesToImportantBlockSometimes create(float startDistance, int wantToReachDistance,
                                                       int minCooldown,
                                                       int maxExtraCooldown){
        return new GoesToImportantBlockSometimes(startDistance, wantToReachDistance, m->true, minCooldown, maxExtraCooldown);
    }

}
