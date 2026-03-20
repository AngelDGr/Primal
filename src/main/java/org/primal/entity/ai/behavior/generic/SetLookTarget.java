package org.primal.entity.ai.behavior.generic;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import org.primal.registry.Primal_MemoryModuleTypes;

import java.util.Optional;

public class SetLookTarget {

    public static BehaviorControl<LivingEntity> fromCautiousTarget() {
        return BehaviorBuilder.create(
                instance -> instance.group(
                                instance.registered(MemoryModuleType.LOOK_TARGET),
                                instance.registered(Primal_MemoryModuleTypes.NEAREST_CAUTIOUS.get()),
                                instance.absent(MemoryModuleType.ATTACK_TARGET)
                        )
                        .apply(instance, (look_target,
                                          nearest_cautious,
                                          attackTarget)
                                -> (level, entity, l) -> {
                            Optional<BlockPos> optionalCautiousTarget =
                                    instance
                                            .tryGet(nearest_cautious)
                                            .map(Entity::blockPosition);
                            if (optionalCautiousTarget.isEmpty()) {
                                return false;
                            } else {
                                look_target.set(new BlockPosTracker(optionalCautiousTarget.get()));
                                return true;
                            }
                        })
        );
    }

    public static BehaviorControl<LivingEntity> fromRoarTarget() {
        return BehaviorBuilder.create(
                instance -> instance.group(
                                instance.registered(MemoryModuleType.LOOK_TARGET),
                                instance.registered(MemoryModuleType.ROAR_TARGET),
                                instance.absent(MemoryModuleType.ATTACK_TARGET)
                        )
                        .apply(instance, (look_target,
                                          roar_target,
                                          attackTarget)
                                -> (level, entity, l) -> {
                            Optional<BlockPos> optionalRoarTarget =
                                    instance
                                    .tryGet(roar_target)
                                    .map(Entity::blockPosition);
                            if (optionalRoarTarget.isEmpty()) {
                                return false;
                            } else {
                                look_target.set(new BlockPosTracker(optionalRoarTarget.get()));
                                return true;
                            }
                        })
        );
    }

    public static BehaviorControl<LivingEntity> fromImportantBlockTarget() {
        return BehaviorBuilder.create(
                instance -> instance.group(
                                instance.registered(MemoryModuleType.LOOK_TARGET),
                                instance.registered(Primal_MemoryModuleTypes.NEAREST_IMPORTANT_BLOCK.get()),
                                instance.absent(MemoryModuleType.ATTACK_TARGET)
                        )
                        .apply(instance, (look_target,
                                          nearest_cautious,
                                          attackTarget)
                                -> (level, entity, l) -> {
                            Optional<BlockPos> optionalCautiousTarget =
                                    instance
                                            .tryGet(nearest_cautious);
                            if (optionalCautiousTarget.isEmpty()) {
                                return false;
                            } else {
                                look_target.set(new BlockPosTracker(optionalCautiousTarget.get()));
                                return true;
                            }
                        })
        );
    }

    public static BehaviorControl<LivingEntity> fromMusicBlock() {
        return BehaviorBuilder.create(
                instance -> instance.group(
                                instance.registered(MemoryModuleType.LOOK_TARGET),
                                instance.registered(Primal_MemoryModuleTypes.MUSIC_BLOCK.get()),
                                instance.absent(MemoryModuleType.ATTACK_TARGET)
                        )
                        .apply(instance, (look_target,
                                          nearest_cautious,
                                          attackTarget)
                                -> (level, entity, l) -> {
                            Optional<BlockPos> optionalCautiousTarget =
                                    instance
                                            .tryGet(nearest_cautious);
                            if (optionalCautiousTarget.isEmpty()) {
                                return false;
                            } else {
                                look_target.set(new BlockPosTracker(optionalCautiousTarget.get()));
                                return true;
                            }
                        })
        );
    }

    public static BehaviorControl<LivingEntity> fromAttackTarget() {
        return BehaviorBuilder.create(
                instance -> instance.group(
                                instance.registered(MemoryModuleType.LOOK_TARGET),
                                instance.registered(MemoryModuleType.ATTACK_TARGET)
                        )
                        .apply(instance, (look_target,
                                          attackTarget)
                                -> (level, entity, l) -> {
                            Optional<LivingEntity> optionalAttackTarget = instance.tryGet(attackTarget);

                            if (optionalAttackTarget.isEmpty()) {
                                return false;
                            } else {
                                look_target.set(new EntityTracker(optionalAttackTarget.get(), true));
                                return true;
                            }
                        })
        );
    }

    public static BehaviorControl<LivingEntity> fromPlayTarget(int yOffset) {
        return BehaviorBuilder.create(
                instance -> instance.group(
                                instance.registered(MemoryModuleType.LOOK_TARGET),
                                instance.registered(Primal_MemoryModuleTypes.NEAREST_PLAY_MOB.get())
                        )
                        .apply(instance, (look_target,
                                          play_target)
                                -> (level, entity, l) -> {
                            Optional<BlockPos> optionalCautiousTarget =
                                    instance
                                            .tryGet(play_target)
                                            .map(Entity::blockPosition);
                            if (optionalCautiousTarget.isEmpty()) {
                                return false;
                            } else {
                                look_target.set(new BlockPosTracker(new BlockPos(optionalCautiousTarget.get().getX(), optionalCautiousTarget.get().getY()+yOffset, optionalCautiousTarget.get().getZ())));
                                return true;
                            }
                        })
        );
    }
}
